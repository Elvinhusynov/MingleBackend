package az.mingle.repository;

import az.mingle.entity.Post;
import az.mingle.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    Page<Post> findByUser(User user, Pageable pageable);

    Page<Post> findByUser_UserIdIn(List<Long> userIds, Pageable pageable);

    @Query("SELECT p FROM Post p WHERE p.user.userId != :currentUserId AND p.user.userId NOT IN :followedIds")
    Page<Post> findExplorePosts(
            @Param("followedIds") List<Long> followedIds,
            @Param("currentUserId") Long currentUserId,
            Pageable pageable
    );
}