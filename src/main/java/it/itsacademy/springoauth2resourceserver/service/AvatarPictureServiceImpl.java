package it.itsacademy.springoauth2resourceserver.service;

import it.itsacademy.springoauth2resourceserver.exception.*;
import it.itsacademy.springoauth2resourceserver.security.CurrentUserProvider;
import lombok.RequiredArgsConstructor;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import static org.springframework.util.StringUtils.getFilenameExtension;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.async.AsyncRequestBody;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.S3Utilities;
import software.amazon.awssdk.services.s3.model.GetUrlRequest;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Service @Transactional
@RequiredArgsConstructor
public class AvatarPictureServiceImpl implements AvatarPictureService {
    @Value("${S3_BUCKET_NAME}")
    private String bucketS3;

    @Value("${S3_ROOT_PREFIX}")
    private String rootPrefix; // Ends with "/"

    @Value("#{'${AVATAR_IMAGE_FORMATS:image/jpeg}'.split(',')}")
    private List<String> avatarImageFormats;

    @Value("${AVATAR_IMAGE_MAX_SIZE:1048576}")
    private long maxImageSize;

    private final Tika tika = new Tika();
    private final S3AsyncClient s3Client;
    private final S3Utilities s3Utilities;
    private final CurrentUserProvider currentUser;

    private boolean isValidFileMimeType(MultipartFile file, List<String> allowedFormats) {
        if (file == null || file.isEmpty()) return false;

        try (InputStream inputStream = file.getInputStream()) {
            String detectedType = tika.detect(inputStream);

            return detectedType != null && allowedFormats.contains(detectedType.toLowerCase());
        } catch (IOException e) {
            return false;
        }
    }

    private static String humanReadableBytes(long bytes) {
        final String[] UNITS = {"B", "KB", "MB", "GB", "TB", "PB", "EB"};
        double value = bytes;
        int unitIndex = 0;

        while (value >= 1024 && unitIndex < UNITS.length - 1) {
            value /= 1024;
            unitIndex++;
        }

        return String.format("%.2f %s", value, UNITS[unitIndex]);
    }

    private static boolean isFileSizeValid(MultipartFile file, long maxBytes) {
        return file.getSize() < maxBytes;
    }

    @Override
    public void uploadAvatarUrl(MultipartFile image) {
        if (!isValidFileMimeType(image, avatarImageFormats))
            throw new BadRequestException("File is empty or MIME type not supported. Expected MIME type(s): " + String.join(", ", avatarImageFormats) + ".");
        if (!isFileSizeValid(image, maxImageSize))
            throw new ContentTooLargeException("File size must be less than " + humanReadableBytes(maxImageSize) + ".");

        try {
            byte[] fileBytes = image.getBytes();
            final String extension = tika.detect(fileBytes);
            final String s3ObjectKey = rootPrefix + currentUser.getProfile().getNickname() + "/" + image.getOriginalFilename() + getFilenameExtension(extension);

            s3Client.putObject(b -> b.bucket(bucketS3).key(s3ObjectKey).contentType(extension).build(),
                    AsyncRequestBody.fromBytes(fileBytes)).join();

            final String imageUrl = s3Utilities.getUrl(GetUrlRequest.builder().bucket(bucketS3).key(s3ObjectKey).build()).toString();
            currentUser.getProfile().setAvatarUrl(imageUrl);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
