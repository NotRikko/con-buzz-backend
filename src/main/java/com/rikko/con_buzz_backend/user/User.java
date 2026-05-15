package com.rikko.con_buzz_backend.user;

import com.rikko.con_buzz_backend.channel.Channel;
import com.rikko.con_buzz_backend.post.Post;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Table(
        name = "users",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "email"),
                @UniqueConstraint(columnNames = "username")
        },
        indexes = {
                @Index(name = "idx_user_email",    columnList = "email"),
                @Index(name = "idx_user_username", columnList = "username")
        }
)
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = {"posts", "channels"})
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class User implements org.springframework.security.core.userdetails.UserDetails {

    // Identity

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false, nullable = false)
    @EqualsAndHashCode.Include
    private String id;

    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String passwordHash;

    // Profile

    @Column(length = 100)
    private String displayName;

    @Column(length = 500)
    private String bio;

    @Column(length = 500)
    private String avatarUrl;

    // Authorization

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Role role = Role.USER;

    // Account state

    @Column(nullable = false)
    private boolean enabled = true;

    @Column(nullable = false)
    private boolean accountNonLocked = true;

    // Audit timestamps  (requires @EnableJpaAuditing on your config class)

    @CreatedDate
    @Column(updatable = false, nullable = false)
    private Instant createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private Instant updatedAt;

    // Relationships

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Post> posts = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "user_channels",
            joinColumns        = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "channel_id")
    )
    private List<Channel> channels = new ArrayList<>();

    // Factory method

    public static User create(String username, String email, String passwordHash) {
        User user = new User();
        user.username     = username;
        user.email        = email;
        user.passwordHash = passwordHash;
        return user;
    }

    // UserDetails contract  (Spring Security)

    @Override
    @NonNull
    public Collection<? extends org.springframework.security.core.GrantedAuthority> getAuthorities() {
        return List.of(new org.springframework.security.core.authority.SimpleGrantedAuthority(
                "ROLE_" + role.name()
        ));
    }

    @Override @NonNull public String   getPassword()                { return passwordHash; }
    @Override @NonNull public String   getUsername()                { return email; }  // login by email
    @Override public boolean  isEnabled()                  { return enabled; }
    @Override public boolean  isAccountNonLocked()         { return accountNonLocked; }
}