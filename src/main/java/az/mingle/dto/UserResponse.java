package az.mingle.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class UserResponse {

    private Long userId;
    private String name;
    private String surname;
    private LocalDate birthdate;
    private String phoneNumber;
    private String email;
    private String username;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDate createdDate;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime modifiedDate;
}
