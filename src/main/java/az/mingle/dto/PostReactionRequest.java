package az.mingle.dto;

import az.mingle.enums.ReactionType;
import lombok.Data;

@Data
public class PostReactionRequest {
    private Long postId;

    private ReactionType type;
}
