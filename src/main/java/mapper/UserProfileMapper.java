package mapper;

import it.itsacademy.springoauth2resourceserver.dto.UserProfileResponseDTO;
import it.itsacademy.springoauth2resourceserver.model.UserProfile;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserProfileMapper {
    public UserProfileResponseDTO toDto(UserProfile entity);
}
