package it.itsacademy.springoauth2resourceserver.service;

import org.springframework.web.multipart.MultipartFile;

public interface AvatarPictureService {
    void uploadAvatarUrl(MultipartFile image);
}
