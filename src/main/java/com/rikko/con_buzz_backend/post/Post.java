package com.rikko.con_buzz_backend.post;

import com.rikko.con_buzz_backend.channel.Channel;
import com.rikko.con_buzz_backend.comment.Comment;
import com.rikko.con_buzz_backend.user.User;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
        name = "posts",
        indexes = {
                @Index(name = "idx_post_author",  columnList = "user_id"),
                @Index(name = "idx_post_channel", columnList = "channel_id"),
                @Index(name = "idx_post_status",  columnList = "status"),
                @Index(name = "idx_post_created", columnList = "created_at"),
                @Index(name = "idx_post_channel_status_created", columnList = "channel_id, status, created_at")
        }
)
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = {"channel", "author"})
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false, nullable = false)
    @EqualsAndHashCode.Include
    private String id;

    // Content
    @Column(nullable = false, length = 200)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PostStatus status = PostStatus.PUBLISHED;

    // Engagement Counters

    @Column(nullable = false)
    private int viewCount = 0;

    @Column(nullable = false)
    private int likeCount = 0;

    @Column(nullable = false)
    private int dislikeCount = 0;

    @Column(nullable = false)
    private int commentCount = 0;

    // Moderation

    @Column(nullable = false)
    private boolean pinned = false;

    @Column(nullable = false)
    private boolean locked = false;

    // Relationships

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User author;

    @ManyToOne
    @JoinColumn(name = "channel_id", nullable = false)
    private Channel channel;

    // Audit timestamps

    @CreatedDate
    @Column(name = "created_at", updatable = false, nullable = false)
    private Instant createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Column(name = "deleted_at")
    private Instant deletedAt;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    // Factory method

    public static Post create(User author, Channel channel, String title, String content) {
        Post post    = new Post();
        post.author  = author;
        post.channel = channel;
        post.title   = title;
        post.content = content;
        return post;
    }

    // Domain behaviour

    public boolean isDeleted() {
        return deletedAt != null;
    }

    public void softDelete() {
        this.deletedAt = Instant.now();
        this.status    = PostStatus.DELETED;
    }

    public void pin()   { this.pinned = true;  }
    public void unpin() { this.pinned = false; }
    public void lock()  { this.locked = true;  }
    public void unlock(){ this.locked = false; }

    public void incrementViewCount()    { this.viewCount++;    }
    public void incrementCommentCount() { this.commentCount++; }
    public void decrementCommentCount() { this.commentCount = Math.max(0, this.commentCount - 1); }


}
