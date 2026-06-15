package com.rikko.cosplace_backend.listing;

import com.rikko.cosplace_backend.user.User;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;

@Entity
@Table(
        name = "listings",
        indexes = {
                @Index(name = "idx_listing_author",  columnList = "user_id"),
                @Index(name = "idx_listing_status",  columnList = "status"),
                @Index(name = "idx_listing_created", columnList = "created_at"),
        }
)
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = {"author"})
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Listing {

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
    private ListingStatus status = ListingStatus.PUBLISHED;

    // Engagement Counters

    @Column(nullable = false)
    private int viewCount = 0;

    @Column(nullable = false)
    private int likeCount = 0;

    // Moderation

    @Column(nullable = false)
    private boolean pinned = false;

    @Column(nullable = false)
    private boolean locked = false;

    // Relationships

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User author;

    // Audit timestamps

    @CreatedDate
    @Column(name = "created_at", updatable = false, nullable = false)
    private Instant createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Column(name = "deleted_at")
    private Instant deletedAt;

    // Factory method

    public static Listing create(User author, String title, String content) {
        Listing listing = new Listing();
        listing.author  = author;
        listing.title   = title;
        listing.content = content;
        return listing;
    }

    // Domain behaviour

    public boolean isDeleted() {
        return deletedAt != null;
    }

    public void softDelete() {
        this.deletedAt = Instant.now();
        this.status    = ListingStatus.DELETED;
    }

    public void pin()   { this.pinned = true;  }
    public void unpin() { this.pinned = false; }
    public void lock()  { this.locked = true;  }
    public void unlock(){ this.locked = false; }

    public void incrementViewCount()    { this.viewCount++;    }
}
