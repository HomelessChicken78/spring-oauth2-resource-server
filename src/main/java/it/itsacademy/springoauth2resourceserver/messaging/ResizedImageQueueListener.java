package it.itsacademy.springoauth2resourceserver.messaging;

import io.awspring.cloud.sqs.annotation.SqsListener;
import it.itsacademy.springoauth2resourceserver.model.UserProfile;
import it.itsacademy.springoauth2resourceserver.repository.UserProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import software.amazon.awssdk.eventnotifications.s3.model.S3EventNotification;
import software.amazon.awssdk.eventnotifications.s3.model.S3EventNotificationRecord;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class ResizedImageQueueListener {
    private final UserProfileRepository profileRepository;

    private static final Pattern UUID_PATTERN = Pattern.compile(
            "[a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}"
    );

    private UUID uuidFromS3ObjectKey(String objectKey) {
        if (objectKey == null) return null;

        String decodedKey = URLDecoder.decode(objectKey.replace("+", "%20"), StandardCharsets.UTF_8);
        Matcher matcher = UUID_PATTERN.matcher(decodedKey);

        if (matcher.find()) return UUID.fromString(matcher.group());

        return null;
    }

    @Transactional
    @SqsListener("${SQS_RESIZE_QUEUE_NAME}")
    public void resizedImage(String messagePayload) {
        S3EventNotification s3Event = S3EventNotification.fromJson(messagePayload);
        List<S3EventNotificationRecord> records = s3Event.getRecords();

        if (records == null || records.isEmpty()) return;

        for (S3EventNotificationRecord record : records) {
            String objectKey = record.getS3().getObject().getKey();
            UUID idProfile = uuidFromS3ObjectKey(objectKey);
            if (idProfile == null) continue;
            UserProfile profile = profileRepository.findByIdOrElseThrow(idProfile);
            profile.setResizedAvatarUrl(objectKey);
            profileRepository.save(profile);
        }
    }
}
