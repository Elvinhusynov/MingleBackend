package az.mingle.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FollowDto {
    private Long followId;

    private Long followerId;

    private Long followedId;

    private LocalDateTime createdAt;
}