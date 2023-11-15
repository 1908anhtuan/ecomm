package fontys.individual.school.persistence.entity;

import fontys.individual.school.domain.enumClasses.AccountType;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "user_role")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name= "id")
    private Long id;
    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private AccountType role;
    @ManyToOne
    @JoinColumn(name = "user_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private AccountEntity user;


}
