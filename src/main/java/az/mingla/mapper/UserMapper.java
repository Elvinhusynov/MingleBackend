package az.mingla.mapper;

import az.mingla.dto.UserDto;
import az.mingla.dto.UserRegisterRequest;
import az.mingla.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto toDto(User user);

    User toEntity(UserDto userDto);

    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "bio", ignore = true)
    @Mapping(target = "profileImage", ignore = true)
    @Mapping(target = "locked", constant = "false")
    @Mapping(target = "enabled", constant = "true")
    @Mapping(target = "lastLogin", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
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
        return dto;
    }
}



