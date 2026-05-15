package com.rikko.con_buzz_backend.convention;

import com.rikko.con_buzz_backend.channel.Channel;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
        name = "conventions"
)
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = {"channels"})
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Convention {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false, nullable = false)
    @EqualsAndHashCode.Include
    private String id;

    @Column(nullable = false, unique = true)
    private String conventionName;

    // Relationships

    @OneToMany(mappedBy = "convention", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Channel> channels = new ArrayList<>();

    // Factory method

    public static Convention create(String conventionName) {
        Convention convention = new Convention();
        convention.conventionName  = conventionName;
        return convention;
    }
}
