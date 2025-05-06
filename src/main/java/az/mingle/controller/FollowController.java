package az.mingle.controller;

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

    @PostMapping("/{followerId}/follow/{followedId}")
    public ResponseEntity<Void> follow(@PathVariable Long followerId, @PathVariable Long followedId) {
        followService.followUser(followerId, followedId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{followerId}/unfollow/{followedId}")
    public ResponseEntity<Void> unfollow(@PathVariable Long followerId, @PathVariable Long followedId) {
        followService.unfollowUser(followerId, followedId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{userId}/followers")
    public ResponseEntity<List<UserDto>> getFollowers(@PathVariable Long userId) {
        return ResponseEntity.ok(followService.getFollowers(userId));
    }

    @GetMapping("/{userId}/following")
    public ResponseEntity<List<UserDto>> getFollowing(@PathVariable Long userId) {
        return ResponseEntity.ok(followService.getFollowing(userId));
    }
}
