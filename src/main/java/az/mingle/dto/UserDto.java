package az.mingle.dto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;


@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class UserDto {

    private Long id;

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

