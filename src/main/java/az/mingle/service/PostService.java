package az.mingle.service;

import az.mingle.dto.PostRequest;
import az.mingle.dto.PostResponse;
import az.mingle.dto.BaseResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PostService {

    PostResponse createPost(PostRequest postRequest, String username);

    PostResponse updatePost(Long postId, PostRequest postRequest, String username);

    void deletePost(Long postId, String username);

    Page<PostResponse> getAllPosts(Pageable pageable);

    Page<PostResponse> getUserPosts(Long userId, Pageable pageable);

    BaseResponse<List<PostResponse>> getFeedPosts(String username, Pageable pageable);

    BaseResponse<List<PostResponse>> getExplorePosts(String username, Pageable pageable);
}

