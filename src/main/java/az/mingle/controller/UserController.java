package az.mingle.controller;

import az.mingle.dto.BaseResponse;
import az.mingle.dto.UserDto;
import az.mingle.dto.UserUpdateRequest;
import az.mingle.entity.User;
import az.mingle.exception.AccessDeniedException;
import az.mingle.mapper.UserMapper;
import az.mingle.service.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse<UserDto>> getUserById(@PathVariable Long id,
                                                             Authentication authentication) {
        User user = userService.getOwnUserById(id, authentication.getName());
        return ResponseEntity.ok(new BaseResponse<>(true, "User retrieved successfully", userMapper.toDto(user)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BaseResponse<UserDto>> updateUser(@PathVariable Long id,
                                                            @RequestBody UserUpdateRequest request,
                                                            Authentication authentication) {
        Long loggedInUserId = userService.getUserIdByUsername(authentication.getName());
        if (!loggedInUserId.equals(id)) {
            throw new AccessDeniedException("Yalnız öz hesabınızı yeniləyə bilərsiniz.");
        }
        UserDto updatedUser = userService.updateUser(id, request);
        return ResponseEntity.ok(new BaseResponse<>(true, "User updated successfully",
                updatedUser));
    }

    @GetMapping("/me")
    public ResponseEntity<BaseResponse<UserDto>> getMyProfile(Authentication authentication) {
        String username = authentication.getName();
        UserDto userDto = userMapper.toDto(userService.findByUsername(username));
        return ResponseEntity.ok(new BaseResponse<>(true, "User profile retrieved successfully",
                userDto));
    }

    @GetMapping("/search")
    public ResponseEntity<BaseResponse<Page<UserDto>>> searchUsers(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String surname,
            @RequestParam(required = false) String username,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<UserDto> userDtos = userService.searchUsers(name, surname, username, page, size);
        return ResponseEntity.ok(BaseResponse.success(userDtos, "İstifadəçilər tapıldı"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse<Void>> deleteUser(@PathVariable Long id,
                                                         Authentication authentication) {
        Long loggedInUserId = userService.getUserIdByUsername(authentication.getName());
        if (!loggedInUserId.equals(id)) {
            throw new AccessDeniedException("Yalnız öz hesabınızı silə bilərsiniz.");
        }
        userService.deleteUser(id);
        return ResponseEntity.ok(new BaseResponse<>(true, "User deleted successfully",
                null));
    }

    @PostMapping("/{id}/upload-profile-image")
    public ResponseEntity<BaseResponse<String>> uploadProfileImage(@PathVariable Long id,
                                                                   @RequestParam("image") MultipartFile file) {
        userService.uploadProfileImage(id, file);
        return ResponseEntity.ok(new BaseResponse<>(true, "Profile image uploaded",
                "Profile image uploaded."));
    }

    @DeleteMapping("/{id}/profile-image")
    public ResponseEntity<BaseResponse<String>> deleteProfileImage(@PathVariable Long id) {
        userService.deleteProfileImage(id);
        return ResponseEntity.ok(new BaseResponse<>(true, "Profile image deleted",
                "Profile image deleted."));
    }
}


