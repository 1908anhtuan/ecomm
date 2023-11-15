package fontys.individual.school.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "message")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PrivateMessageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "sender_id", nullable = false)
    private AccountEntity sender;

    @ManyToOne
    @JoinColumn(name = "receiver_id", nullable = false)
    private AccountEntity receiver;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "timestamp", nullable = true, updatable = false)
    @CreationTimestamp
    private LocalDateTime timestamp;

}