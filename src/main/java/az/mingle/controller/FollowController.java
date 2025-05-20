package az.mingle.controller;

import az.mingle.dto.BaseResponse;
import az.mingle.dto.UserDto;
import az.mingle.service.FollowService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/follows")
@RequiredArgsConstructor
public class FollowController {

    private final FollowService followService;

    @PostMapping("/{followerId}/follow/{followingId}")
    public ResponseEntity<BaseResponse<Void>> follow(@PathVariable Long followerId,
                                                     @PathVariable Long followingId) {
        followService.follow(followerId, followingId);
        return ResponseEntity.ok(new BaseResponse<>(true, "Followed successfully", null));
    }

    @DeleteMapping("/{followerId}/unfollow/{followingId}")
    public ResponseEntity<BaseResponse<Void>> unfollow(@PathVariable Long followerId,
                                                       @PathVariable Long followingId) {
        followService.unfollow(followerId, followingId);
        return ResponseEntity.ok(new BaseResponse<>(true, "Unfollowed successfully", null));
    }

    @GetMapping("/{userId}/followers")
    public ResponseEntity<BaseResponse<List<UserDto>>> getFollowers(@PathVariable Long userId) {
        List<UserDto> followers = followService.getFollowers(userId);
        return ResponseEntity.ok(new BaseResponse<>(true, "Followers retrieved successfully", followers));
    }

    @GetMapping("/{userId}/following")
    public ResponseEntity<BaseResponse<List<UserDto>>> getFollowing(@PathVariable Long userId) {
        List<UserDto> following = followService.getFollowing(userId);
        return ResponseEntity.ok(new BaseResponse<>(true, "Following retrieved successfully", following));
    }
}

