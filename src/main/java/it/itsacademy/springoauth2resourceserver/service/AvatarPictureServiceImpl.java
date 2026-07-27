package it.itsacademy.springoauth2resourceserver.service;


import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;



@Service @Transactional
@RequiredArgsConstructor
public class AvatarPictureServiceImpl implements AvatarPictureService {


    @Override
    public void uploadAvatarUrl(MultipartFile image) {


    }
}
