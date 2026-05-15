package com.rikko.con_buzz_backend.reaction;

import com.rikko.con_buzz_backend.user.User;
import com.rikko.con_buzz_backend.post.Post;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;

@Entity
@Table(
        name = "reactions",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"user_id", "post_id"})  // enforce one reaction per user per post
        },
        indexes = {
                @Index(name = "idx_reaction_post", columnList = "post_id"),
                @Index(name = "idx_reaction_user", columnList = "user_id")
        }
)
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = {"user", "post"})
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Reaction {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false, nullable = false)
    @EqualsAndHashCode.Include
    private String id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private ReactionType type;

    @CreatedDate
    @Column(name = "created_at", updatable = false, nullable = false)
    private Instant createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    // Factory Method

    public static Reaction create(User user, Post post, ReactionType type) {
        Reaction reaction = new Reaction();
        reaction.user = user;
        reaction.post = post;
        reaction.type = type;
        return reaction;
    }
}