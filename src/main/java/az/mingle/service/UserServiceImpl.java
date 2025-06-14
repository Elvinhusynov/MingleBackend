package az.mingle.service;

import az.mingle.dto.UserDto;
import az.mingle.dto.UserUpdateRequest;
import az.mingle.entity.User;
import az.mingle.exception.AccessDeniedException;
import az.mingle.exception.ResourceNotFoundException;
import az.mingle.exception.UserNotFoundException;
import az.mingle.mapper.UserMapper;
import az.mingle.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
        return userMapper.toDto(user);
    }

    @Override
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("İstifadəçi tapılmadı"));
        userRepository.delete(user);
    }

    @Override
    public UserDto getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return userMapper.toDto(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .accountLocked(user.isLocked())
                .disabled(!user.isEnabled())
                .authorities(new ArrayList<>())
                .build();
    }

    @Override
    public UserDto getCurrentUser() {
        User authenticatedUser = getAuthenticatedUser();
        return userMapper.toPublicDto(authenticatedUser);
    }

    @Override
    public Page<UserDto> searchUsers(String name, String surname, String username, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<User> users = userRepository.search(name, surname, username, pageable);
        return userMapper.toDtoPage(users);
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("İstifadəçi tapılmadı: " + username));
    }

    @Override
    public Long getUserIdByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("İstifadəçi tapılmadı"))
                .getUserId();
    }

    @Override
    public UserDto updateUser(Long id, UserUpdateRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("İstifadəçi tapılmadı"));

        user.setName(request.getName());
        user.setSurname(request.getSurname());
        user.setModifiedAt(LocalDateTime.now());
        userRepository.save(user);

        return userMapper.toDto(user);
    }

    @Override
    public User getOwnUserById(Long id, String username) {
        Long loggedInUserId = getUserIdByUsername(username);
        if (!loggedInUserId.equals(id)) {
            throw new AccessDeniedException("Yalnız öz məlumatınızı görə bilərsiniz.");
        }
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("İstifadəçi tapılmadı"));
    }

    @Override
    public User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Override
    public void uploadProfileImage(Long userId, MultipartFile file) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();


        Path uploadPath = Paths.get("uploads/profile_images");
        try {
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            Path filePath = uploadPath.resolve(fileName);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            user.setProfileImage("/uploads/profile_images/" + fileName);
            userRepository.save(user);
        } catch (IOException e) {
            throw new RuntimeException("Şəkil yüklənərkən xəta baş verdi. ", e);
        }
    }

    @Override
    public void deleteProfileImage(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("İstifadəçi tapılmadı"));

        String existingImage = user.getProfileImage();
        if (existingImage != null) {
            String cleanedFilename = existingImage.replace("/uploads/profile_images/", "");
            Path filePath = Paths.get("uploads", "profile_images", cleanedFilename);
            try {
                Files.deleteIfExists(filePath);
            } catch (IOException e) {
                throw new RuntimeException("Şəkili silmək mümkün olmadı. ", e);
            }

            user.setProfileImage(null);
            userRepository.save(user);
        }
    }
}