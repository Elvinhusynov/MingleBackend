package az.mingle.service;

import az.mingle.dto.UserDto;
import az.mingle.entity.Follow;
import az.mingle.entity.User;
import az.mingle.exception.AlreadyFollowingException;
import az.mingle.exception.NotFoundException;
import az.mingle.exception.SelfFollowException;
import az.mingle.mapper.UserMapper;
import az.mingle.repository.FollowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import az.mingle.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FollowServiceImpl implements FollowService {

    private final FollowRepository followRepository;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public void follow(String followerUsername, Long followedId) {
        User follower = userRepository.findByUsername(followerUsername)
                .orElseThrow(() -> new NotFoundException("Follower user not found"));

        User followed = userRepository.findById(followedId)
                .orElseThrow(() -> new NotFoundException("Followed user not found"));

        if (follower.getUserId().equals(followed.getUserId())) {
            throw new SelfFollowException("You cannot follow yourself");
        }

        if (followRepository.findByFollowerIdAndFollowedId(follower.getUserId(), followed.getUserId()).isPresent()) {
            throw new AlreadyFollowingException("Already following this user");
        }

        Follow follow = new Follow();
        follow.setFollower(follower);
        follow.setFollowed(followed);
        follow.setCreatedAt(LocalDateTime.now());

        followRepository.save(follow);
    }

    @Override
    public void unfollow(String followerUsername, Long followedId) {
        User follower = userRepository.findByUsername(followerUsername)
                .orElseThrow(() -> new NotFoundException("Follower user not found"));

        Follow follow = followRepository.findByFollowerIdAndFollowedId(follower.getUserId(), followedId)
                .orElseThrow(() -> new NotFoundException("Follow relationship not found"));

        followRepository.delete(follow);
    }

    @Override
    public List<UserDto> getFollowers(Long userId) {
        return followRepository.findByFollowedId(userId)
                .stream()
                .map(f -> userMapper.toDto(f.getFollower()))
                .collect(Collectors.toList());
    }

    @Override
    public List<UserDto> getFollowing(Long userId) {
        return followRepository.findByFollowerId(userId)
                .stream()
                .map(f -> userMapper.toDto(f.getFollowed()))
                .collect(Collectors.toList());
    }
}