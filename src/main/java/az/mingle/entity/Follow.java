package az.mingle.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "follows", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"follower_id", "followed_id"})
})
public class Follow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // A user (follower)
    @ManyToOne
    @JoinColumn(name = "follower_id", nullable = false)
    private User follower;

    // B user (followed)
    @ManyToOne
    @JoinColumn(name = "followed_id", nullable = false)
    private User followed;

    private LocalDateTime createdAt = LocalDateTime.now();
}

