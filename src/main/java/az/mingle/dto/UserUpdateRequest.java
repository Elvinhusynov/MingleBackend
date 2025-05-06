package az.mingle.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class UserUpdateRequest {

    private String name;

    private String surname;

    private LocalDate birthdate;

    private String phoneNumber;

    private String bio;
}
