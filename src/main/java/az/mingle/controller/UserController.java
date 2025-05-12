package az.mingle.controller;

import az.mingle.dto.UserDto;
import az.mingle.dto.UserUpdateRequest;
import az.mingle.entity.User;
import az.mingle.mapper.UserMapper;
import az.mingle.service.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import az.mingle.exception.AccessDeniedException;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;


    @PostMapping
    public ResponseEntity<UserDto> createUser(@RequestBody UserDto userDto) {
        return ResponseEntity.ok(userService.createUser(userDto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id,
                                               Authentication authentication) {
        Long loggedInUserId = userService.getUserIdByUsername(authentication.getName());

        if (!loggedInUserId.equals(id)) {
            throw new AccessDeniedException("Yalnız öz məlumatınızı görə bilərsiniz.");
        }

        User user = userService.findById(id);
        return ResponseEntity.ok(userMapper.toDto(user));
    }

    @GetMapping("/profile")
    public ResponseEntity<UserDto> getProfile() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        UserDto userDto = userService.getUserByEmail(email);
        return ResponseEntity.ok(userDto);

    }

    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable Long id,
                                              @RequestBody UserUpdateRequest request,
                                              Authentication authentication) {
        Long loggedInUserId = userService.getUserIdByUsername(authentication.getName());
        if (!loggedInUserId.equals(id)) {
            throw new AccessDeniedException("Yalnız öz hesabınızı yeniləyə bilərsiniz.");
        }
        UserDto updatedUser = userService.updateUser(id, request);
        return ResponseEntity.ok(updatedUser);
    }

    @GetMapping("/my-profile")
    public ResponseEntity<UserDto> getMyProfile(Authentication authentication) {
        String username = authentication.getName();
        User user = userService.findByUsername(username);
        return ResponseEntity.ok(userMapper.toDto(user));
    }

    @GetMapping("/search")
    public ResponseEntity<Page<UserDto>> searchUsers(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String surname,
            @RequestParam(required = false) String username,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<User> users = userService.searchUsers(name, surname, username, page, size);
        Page<UserDto> userDtos = users.map(userMapper::toDto);

        return ResponseEntity.ok(userDtos);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id,
                                           Authentication authentication) {
        Long loggedInUserId = userService.getUserIdByUsername(authentication.getName());
        if (!loggedInUserId.equals(id)) {
            throw new AccessDeniedException("Yalnız öz hesabınızı silə bilərsiniz.");
        }
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/me")
    public ResponseEntity<User> getAuthenticatedUser() {
        User user = userService.getAuthenticatedUser();
        return ResponseEntity.ok(user);
    }

    @PostMapping("/{id}/upload-profile-image")
    public ResponseEntity<String> uploadProfileImage(@PathVariable Long id,
                                                     @RequestParam("image") MultipartFile file) {
        userService.uploadProfileImage(id, file);
        return ResponseEntity.ok("Profil şəkliniz yükləndi.");
    }

    @DeleteMapping("/{id}/profile-image")
    public ResponseEntity<String> deleteProfileImage(@PathVariable Long id) {
        userService.deleteProfileImage(id);
        return ResponseEntity.ok("Profil şəkliniz silindi. ");
    }


}

