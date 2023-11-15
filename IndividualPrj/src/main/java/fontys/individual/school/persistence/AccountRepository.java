package fontys.individual.school.persistence;

import fontys.individual.school.persistence.entity.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<AccountEntity, Long> {
    boolean existsByAccountName(String accountName);
    @Query(value = "SELECT * FROM account WHERE account_name = :accountName AND password = :password", nativeQuery = true)
    AccountEntity existsByAccountNameAndPassword(@Param("accountName")String accountName, @Param("password")String password);

    Optional<AccountEntity> findByAccountName(String accountName);

    @Transactional
    @Modifying
    @Query("UPDATE AccountEntity a " +
            "SET a.firstName = CASE WHEN :firstName IS NULL THEN a.firstName ELSE :firstName END, " +
            "a.lastName = CASE WHEN :lastName IS NULL THEN a.lastName ELSE :lastName END, " +
            "a.phoneNumber = CASE WHEN :phoneNumber IS NULL THEN a.phoneNumber ELSE :phoneNumber END, " +
            "a.email = CASE WHEN :email IS NULL THEN a.email ELSE :email END " +
            "WHERE a.id = :id")
    int updateUserInfo(
            @Param("id") Long id,
            @Param("firstName") String firstName,
            @Param("lastName") String lastName,
            @Param("phoneNumber") String phoneNumber,
            @Param("email") String email
    );
}
