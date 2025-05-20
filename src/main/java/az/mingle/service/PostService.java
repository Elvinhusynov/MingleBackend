package az.mingle.service;

import az.mingle.dto.PostRequest;
import az.mingle.dto.PostResponse;

import java.util.List;

public interface PostService {

    PostResponse createPost(PostRequest postRequest, Long userId);

    PostResponse updatePost(Long postId, PostRequest postRequest, Long userId);

    void deletePost(Long postId, Long userId);

    List<PostResponse> getAllPosts();

    List<PostResponse> getUserPosts(Long userId);
}

