package az.mingle.controller;

import az.mingle.dto.BaseResponse;
import az.mingle.dto.PostReactionRequest;
import az.mingle.service.PostReactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/reactions")
@RequiredArgsConstructor
public class PostReactionController {

    private final PostReactionService reactionService;

    @PostMapping
    public ResponseEntity<BaseResponse<String>> reactToPost(
            @RequestBody PostReactionRequest request,
            @AuthenticationPrincipal(expression = "username") String username) {
        reactionService.reactToPost(username, request);
        return ResponseEntity.ok(BaseResponse.success("Reaction processed"));
    }
}
