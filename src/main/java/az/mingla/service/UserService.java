package az.mingla.service;

import az.mingla.dto.UserDto;
import java.util.List;

public interface UserService {

    UserDto createUser(UserDto userDto);

    UserDto getUserById(Long id);

    List<UserDto> getAllUsers();

    UserDto updateUser(Long id, UserDto userDto);

    void deleteUser(Long id);

    UserDto getUserByEmail(String email);
}
