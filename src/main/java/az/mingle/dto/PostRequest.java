package az.mingle.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PostRequest {

    @NotBlank(message = "Title can not be blank")
    private String title;

    @NotBlank(message = "Content can not be blank")
    private String content;
}

