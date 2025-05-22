package az.mingle.service;

import az.mingle.dto.UserDto;

import java.util.List;

public interface FollowService {

    void follow(String followerUsername, Long followedId);

    void unfollow(String followerUsername, Long followedId);

    List<UserDto> getFollowers(Long userId);

    List<UserDto> getFollowing(Long userId);
}
