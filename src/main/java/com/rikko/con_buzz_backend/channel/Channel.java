package com.rikko.con_buzz_backend.channel;

import com.rikko.con_buzz_backend.convention.Convention;
import com.rikko.con_buzz_backend.post.Post;
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
        name = "channels",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"convention_id", "name"})  // unique name per convention
        },
        indexes = {
                @Index(name = "idx_channel_convention", columnList = "convention_id"),
                @Index(name = "idx_channel_status",     columnList = "status")
        }
)
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = {"convention", "posts"})
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Channel {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false, nullable = false)
    @EqualsAndHashCode.Include
    private String id;

    // Content

    @Column(nullable = false, unique = true)
    private String channelName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ChannelStatus status = ChannelStatus.ACTIVE;

    // Counters

    @Column(nullable = false)
    private int postCount = 0;

    // Audit

    @CreatedDate
    @Column(name = "created_at", updatable = false, nullable = false)
    private Instant createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    // Relationships

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "convention_id", nullable = false)
    private Convention convention;

    @OneToMany(mappedBy = "channel", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Post> posts = new ArrayList<>();

    // Factory method

    public static Channel create(String channelName) {
        Channel channel = new Channel();
        channel.channelName = channelName;
        return channel;
    }

    // Domain behaviour

    public void archive() { this.status = ChannelStatus.ARCHIVED; }
    public void activate() { this.status = ChannelStatus.ACTIVE; }

    public boolean isArchived() { return status == ChannelStatus.ARCHIVED; }

    public void incrementPostCount() { this.postCount++; }
    public void decrementPostCount() { this.postCount = Math.max(0, this.postCount - 1); }


}
