package az.mingle.mapper;

import az.mingle.dto.UserDto;
import az.mingle.dto.UserRegisterRequest;
import az.mingle.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto toDto(User user);

    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "bio", ignore = true)
    @Mapping(target = "profileImage", ignore = true)
    @Mapping(target = "locked", constant = "false")
    @Mapping(target = "enabled", constant = "true")
    @Mapping(target = "lastLogin", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "phoneNumber", source = "phoneNumber")
    User toEntity(UserRegisterRequest request);

    default UserDto toPublicDto(User user) {
        if (user == null) {
            return null;
        }
        UserDto dto = new UserDto();
        dto.setId(user.getUserId());
        dto.setName(user.getName());
        dto.setSurname(user.getSurname());
        dto.setUsername(user.getUsername());
        dto.setProfileImage(user.getProfileImage());
        dto.setBio(user.getBio());
        dto.setBirthDate(user.getBirthdate());
        dto.setLastLogin(user.getLastLogin());
        return dto;
    }
    default Page<UserDto> toDtoPage(Page<User> users) {
        return users.map(this::toDto);
    }
}