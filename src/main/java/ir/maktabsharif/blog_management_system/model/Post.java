package ir.maktabsharif.blog_management_system.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "post")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    @Column(unique = true, nullable = false)
    private String title;
    @Column(unique = true, nullable = false)
    private String content;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @Column(unique = true, nullable = false)
    private LocalDateTime createdAt;
}
