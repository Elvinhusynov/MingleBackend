package az.mingle.service;

import az.mingle.dto.PostRequest;
import az.mingle.dto.PostResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostService {

    PostResponse createPost(PostRequest postRequest, Long userId);

    PostResponse updatePost(Long postId, PostRequest postRequest, Long userId);

    void deletePost(Long postId, Long userId);

    Page<PostResponse> getAllPosts(Pageable pageable);

    Page<PostResponse> getUserPosts(Long userId, Pageable pageable);
}

