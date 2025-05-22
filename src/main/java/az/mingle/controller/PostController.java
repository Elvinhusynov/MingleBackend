package az.mingle.controller;

import az.mingle.dto.BaseResponse;
import az.mingle.dto.PostRequest;
import az.mingle.dto.PostResponse;
import az.mingle.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
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
    public ResponseEntity<Page<PostResponse>> getAllPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return ResponseEntity.ok(postService.getAllPosts(pageable));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<Page<PostResponse>> getUserPosts(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return ResponseEntity.ok(postService.getUserPosts(userId, pageable));
    }

    @GetMapping("/feed")
    public BaseResponse<List<PostResponse>> getFeed(@AuthenticationPrincipal UserDetails userDetails,
                                @RequestParam(defaultValue = "0") int page,
                                @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return postService.getFeedPosts(userDetails.getUsername(), pageable);
    }

    @GetMapping("/explore")
    public BaseResponse<List<PostResponse>> getExplore(@AuthenticationPrincipal UserDetails userDetails,
                                                       @RequestParam(defaultValue = "0") int page,
                                                       @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return postService.getExplorePosts(userDetails.getUsername(), pageable);
    }
}
