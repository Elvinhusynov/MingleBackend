package az.mingle.service;

import az.mingle.dto.UserDto;
import az.mingle.dto.UserUpdateRequest;
import az.mingle.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.multipart.MultipartFile;

public interface UserService extends UserDetailsService {

    User getOwnUserById(Long id, String username);

    Long getUserIdByUsername(String username);

    User findByUsername(String username);

    UserDto getUserByEmail(String email);

    UserDto updateUser(Long id, UserUpdateRequest request);

    void deleteUser(Long id);

    Page<UserDto> searchUsers(String name, String surname, String username, int page, int size);

    User getAuthenticatedUser();

    void uploadProfileImage(Long userId, MultipartFile file);

    void deleteProfileImage(Long userId);

    UserDto getCurrentUser();

    UserDto getUserById(Long id);
}
