package az.mingle.service;

import az.mingle.dto.BaseResponse;
import az.mingle.dto.PostRequest;
import az.mingle.dto.PostResponse;
import az.mingle.entity.Post;
import az.mingle.entity.User;
import az.mingle.exception.NotFoundException;
import az.mingle.mapper.PostMapper;
import az.mingle.repository.FollowRepository;
import az.mingle.repository.PostRepository;
import az.mingle.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final FollowRepository followRepository;
    private final PostMapper postMapper;

    @Override
    public PostResponse createPost(PostRequest postRequest, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        Post post = postMapper.toEntity(postRequest);
        post.setUser(user);
        post.setCreatedAt(LocalDateTime.now());

        return postMapper.toDto(postRepository.save(post));
    }

    @Override
    public PostResponse updatePost(Long postId, PostRequest postRequest, Long userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException("Post not found"));

        if (!post.getUser().getUserId().equals(userId)) {
            throw new RuntimeException("You can only update your own posts");
        }

        postMapper.updatePostFromRequest(postRequest, post);

        post.setUpdatedAt(LocalDateTime.now());

        return postMapper.toDto(postRepository.save(post));
    }

    @Override
    public void deletePost(Long postId, Long userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException("Post not found"));

        if (!post.getUser().getUserId().equals(userId)) {
            throw new RuntimeException("You can only delete your own posts");
        }

        postRepository.delete(post);
    }

    @Override
    public Page<PostResponse> getAllPosts(Pageable pageable) {
        return postRepository.findAll(pageable)
                .map(postMapper::toDto);
    }

    @Override
    public Page<PostResponse> getUserPosts(Long userId, Pageable pageable) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        return postRepository.findByUser(user, pageable)
                .map(postMapper::toDto);
    }

    @Override
    public BaseResponse<List<PostResponse>> getFeedPosts(String username, Pageable pageable) {
        User currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("User not found"));

        List<Long> followedIds = getFollowedUserIds(currentUser.getUserId());

        if (followedIds.isEmpty()) {
            return BaseResponse.success(List.of(), "No followed users found.");
        }

        List<PostResponse> feedPosts = postRepository.findByUserIdIn(followedIds, pageable)
                .stream()
                .map(postMapper::toDto)
                .collect(Collectors.toList());

        return BaseResponse.success(feedPosts);
    }

    @Override
    public BaseResponse<List<PostResponse>> getExplorePosts(String username, Pageable pageable) {
        User currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("User not found"));

        List<Long> followedIds = getFollowedUserIds(currentUser.getUserId());

        List<PostResponse> explorePosts = postRepository.findByUserIdNotIn(followedIds, currentUser.getUserId(), pageable)
                .stream()
                .map(postMapper::toDto)
                .collect(Collectors.toList());

        return BaseResponse.success(explorePosts);
    }

    private List<Long> getFollowedUserIds(Long userId) {
        return followRepository.findByFollowerId(userId)
                .stream()
                .map(f -> f.getFollowed().getUserId())
                .collect(Collectors.toList());
    }
}
