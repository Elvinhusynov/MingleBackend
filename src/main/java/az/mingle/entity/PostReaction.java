package az.mingle.entity;

import az.mingle.enums.ReactionType;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "post_reactions", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "post_id"})
})
public class PostReaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postReactionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "userId", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", referencedColumnName = "postId", nullable = false)
    private Post post;

    @Enumerated(EnumType.STRING)
    private ReactionType type;

    private LocalDateTime reactedAt;

    @PrePersist
    protected void onCreate() {
        this.reactedAt = LocalDateTime.now();
    }
}