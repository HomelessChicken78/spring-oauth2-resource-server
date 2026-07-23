package it.itsacademy.springoauth2resourceserver.mapper;

import it.itsacademy.springoauth2resourceserver.dto.user.UserProfileResponseDTO;
import it.itsacademy.springoauth2resourceserver.dto.user.UserProfileShortResponseDTO;
import it.itsacademy.springoauth2resourceserver.model.UserProfile;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserProfileMapper {
    UserProfileResponseDTO toDto(UserProfile entity);

    UserProfileShortResponseDTO toShortDto(UserProfile entities);
}
