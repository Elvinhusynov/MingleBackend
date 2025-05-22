package az.mingle.service;

import az.mingle.dto.PostRequest;
import az.mingle.dto.PostResponse;
import az.mingle.dto.BaseResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PostService {

    PostResponse createPost(PostRequest postRequest, Long userId);

    PostResponse updatePost(Long postId, PostRequest postRequest, Long userId);

    void deletePost(Long postId, Long userId);

    Page<PostResponse> getAllPosts(Pageable pageable);

    Page<PostResponse> getUserPosts(Long userId, Pageable pageable);

    BaseResponse<List<PostResponse>> getFeedPosts(String username, Pageable pageable);

    BaseResponse<List<PostResponse>> getExplorePosts(String username, Pageable pageable);
}

