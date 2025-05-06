package az.mingle.service;

import az.mingle.dto.UserDto;
import az.mingle.entity.Follow;
import az.mingle.entity.User;
import az.mingle.exception.NotFoundException;
import az.mingle.mapper.UserMapper;
import az.mingle.repository.FollowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import az.mingle.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FollowServiceImpl implements FollowService {

    private final FollowRepository followRepository;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public void followUser(Long followerId, Long followedId) {
        if (followerId.equals(followedId)) {
            throw new IllegalArgumentException("Özünü follow etmək olmaz");
        }

        User follower = userRepository.findById(followerId)
                .orElseThrow(() -> new NotFoundException("Follower tapılmadı"));
        User followed = userRepository.findById(followedId)
                .orElseThrow(() -> new NotFoundException("Followed tapılmadı"));

        if (followRepository.findByFollowerAndFollowed(follower, followed).isPresent()) {
            throw new IllegalStateException("Artıq follow olunub");
        }

        Follow follow = new Follow();
        follow.setFollower(follower);
        follow.setFollowed(followed);
        followRepository.save(follow);
    }

    @Override
    public void unfollowUser(Long followerId, Long followedId) {
        User follower = userRepository.findById(followerId)
                .orElseThrow(() -> new NotFoundException("Follower tapılmadı"));
        User followed = userRepository.findById(followedId)
                .orElseThrow(() -> new NotFoundException("Followed tapılmadı"));

        Follow follow = followRepository.findByFollowerAndFollowed(follower, followed)
                .orElseThrow(() -> new NotFoundException("Follow əlaqəsi tapılmadı"));

        followRepository.delete(follow);
    }

    @Override
    public List<UserDto> getFollowers(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User tapılmadı"));

        return followRepository.findByFollowed(user)
                .stream()
                .map(f -> userMapper.toDto(f.getFollower()))
                .collect(Collectors.toList());
    }

    @Override
    public List<UserDto> getFollowing(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User tapılmadı"));

        return followRepository.findByFollower(user)
                .stream()
                .map(f -> userMapper.toDto(f.getFollowed()))
                .collect(Collectors.toList());
    }
}
