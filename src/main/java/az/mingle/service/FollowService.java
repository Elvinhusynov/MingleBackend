package az.mingle.service;

import az.mingle.dto.UserDto;

import java.util.List;

public interface FollowService {

    void followUser(Long followerId, Long followedId);

    void unfollowUser(Long followerId, Long followedId);

    List<UserDto> getFollowers(Long userId);

    List<UserDto> getFollowing(Long userId);
}
