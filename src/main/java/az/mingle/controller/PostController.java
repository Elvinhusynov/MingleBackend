package az.mingle.controller;

import az.mingle.dto.BaseResponse;
import az.mingle.dto.PostRequest;
import az.mingle.dto.PostResponse;
import az.mingle.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping
    public ResponseEntity<BaseResponse<PostResponse>> createPost(
            @RequestBody @Valid PostRequest postRequest,
            @RequestParam Long userId) {

        PostResponse createdPost = postService.createPost(postRequest, userId);
        return new ResponseEntity<>(
                new BaseResponse<>(true, "Post created successfully", createdPost),
                HttpStatus.CREATED
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<BaseResponse<PostResponse>> updatePost(
            @PathVariable Long id,
            @RequestBody @Valid PostRequest postRequest,
            @RequestParam Long userId) {

        PostResponse updatedPost = postService.updatePost(id, postRequest, userId);
        return ResponseEntity.ok(
                new BaseResponse<>(true, "Post updated successfully", updatedPost)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse<Void>> deletePost(@PathVariable Long id, @RequestParam Long userId) {
        postService.deletePost(id, userId);
        return ResponseEntity.ok(
                new BaseResponse<>(true, "Post deleted successfully", null)
        );
    }

    @GetMapping
    public ResponseEntity<BaseResponse<List<PostResponse>>> getAllPosts() {
        List<PostResponse> posts = postService.getAllPosts();
        return ResponseEntity.ok(
                new BaseResponse<>(true, "All posts retrieved successfully", posts)
        );
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<BaseResponse<List<PostResponse>>> getUserPosts(@PathVariable Long userId) {
        List<PostResponse> posts = postService.getUserPosts(userId);
        return ResponseEntity.ok(
                new BaseResponse<>(true, "User posts retrieved successfully", posts)
        );
    }
}
