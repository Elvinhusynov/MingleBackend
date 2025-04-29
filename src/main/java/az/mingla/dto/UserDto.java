package az.mingla.dto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {

    private Long userId;

    private String name;

    private String surname;

    private String email;

    private String username;

    private String bio;

    private String profileImage;

    private boolean locked;

    private boolean enabled;

    private LocalDateTime lastLogin;

    private LocalDate birthDate;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}

