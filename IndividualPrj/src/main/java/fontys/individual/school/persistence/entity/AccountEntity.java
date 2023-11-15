package fontys.individual.school.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Set;

@Entity
@Table(name = "account")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @NotBlank
    @Column(name = "account_name")
    private String accountName;
    @Column(name = "password")
    private String password;
    @Column(name = "balance")
    @Builder.Default private Double balance = 0.0;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private Set<RoleEntity> userRoles;

    @Column(name = "first_name",nullable = true, columnDefinition = "char(50) default 'empty'")
    private String firstName;
    @Column(name = "last_name",nullable = true, columnDefinition = "char(50) default 'empty'")
    private String lastName;
    @Column(name = "phone_number",nullable = true, columnDefinition = "char(50) default 'empty'")
    private String phoneNumber;
    @Column(name = "email",nullable = true, columnDefinition = "char(50) default 'empty'")
    private String email;

}
