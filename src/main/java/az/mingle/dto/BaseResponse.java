package az.mingle.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseResponse<T> {

    private boolean success;
    private String message;
    private T data;

    public static <T> BaseResponse<T> success(T data) {
        return new BaseResponse<>(true, "Request successful", data);
    }

    public static <T> BaseResponse<T> success(T data, String message) {
        return new BaseResponse<>(true, message, data);
    }

    public static <T> BaseResponse<T> error(String message) {
        return new BaseResponse<>(false, message, null);
    }
}

