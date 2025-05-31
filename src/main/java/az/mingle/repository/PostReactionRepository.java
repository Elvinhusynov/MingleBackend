package az.mingle.repository;

import az.mingle.entity.PostReaction;
import az.mingle.enums.ReactionType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostReactionRepository extends JpaRepository<PostReaction, Long> {

    Optional<PostReaction> findByUserIdAndPostId(Long userId, Long postId);

    long countByPostIdAndType(Long postId, ReactionType type);

    List<PostReaction> findByUserId(Long userId);
}
