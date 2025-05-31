package az.mingle.repository;

import az.mingle.entity.Follow;
import az.mingle.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long> {

    @Query("SELECT f FROM Follow f WHERE f.follower.userId = :followerId")
    List<Follow> findByFollowerId(Long followerId);

    @Query("SELECT f FROM Follow f WHERE f.followed.userId = :followedId")
    List<Follow> findByFollowedId(Long followedId);

    @Query("SELECT f FROM Follow f WHERE f.follower.userId = :followerId AND f.followed.userId = :followedId")
    Optional<Follow> findByFollowerIdAndFollowedId(Long followerId, Long followedId);

    @Query("SELECT CASE WHEN COUNT(f) > 0 THEN true ELSE false END " +
           "FROM Follow f WHERE f.follower.userId = :followerId AND f.followed.userId = :followedId")
    boolean existsByFollowerIdAndFollowedId(Long followerId, Long followedId);
}