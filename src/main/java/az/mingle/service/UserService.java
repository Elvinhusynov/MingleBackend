package az.mingle.service;

import az.mingle.dto.UserDto;
import az.mingle.dto.UserRegisterRequest;
import az.mingle.dto.UserUpdateRequest;
import az.mingle.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService extends UserDetailsService {

    User findById(Long id);

    Long getUserIdByUsername(String username);

    User findByUsername(String username);

    List<UserDto> getAllUsers();

    UserDto getUserByEmail(String email);

    UserDto updateUser(Long id, UserUpdateRequest request);

    void deleteUser(Long id);

    UserDto createUser(UserRegisterRequest request);

    Page<User> searchUsers(String name, String surname, String username, int page, int size);

    User getAuthenticatedUser();

    void uploadProfileImage(Long userId, MultipartFile file);

    void deleteProfileImage(Long userId);

    UserDto getCurrentUser();

    UserDto getUserById(Long id);












}
