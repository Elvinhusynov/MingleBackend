package az.mingle.repository;

import az.mingle.entity.Follow;
import az.mingle.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long> {

    List<Follow> findByFollower(User follower);

    List<Follow> findByFollowed(User followed);

    Optional<Follow> findByFollowerAndFollowed(User follower, User followed);
}
