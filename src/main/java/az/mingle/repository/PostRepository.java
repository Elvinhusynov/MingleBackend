package az.mingle.repository;

import az.mingle.entity.Post;
import az.mingle.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    Page<Post> findByUser(User user, Pageable pageable); // ✅ Əlavə etdik

    Page<Post> findByUserIdIn(List<Long> userIds, Pageable pageable);

    @Query("SELECT p FROM Post p WHERE p.user.userId NOT IN :excludedUserIds")
    Page<Post> findByUserIdNotIn(List<Long> excludedUserIds, Pageable pageable);

    @Query("SELECT p FROM Post p WHERE p.user.userId != :currentUserId AND p.user.userId NOT IN :followedIds")
    Page<Post> findByUserIdNotIn(List<Long> followedIds, Long currentUserId, Pageable pageable);
}
