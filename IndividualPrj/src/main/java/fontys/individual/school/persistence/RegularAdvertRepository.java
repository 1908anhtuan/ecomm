package fontys.individual.school.persistence;

import fontys.individual.school.domain.enumClasses.AdStatus;
import fontys.individual.school.domain.enumClasses.VerificationStatus;
import fontys.individual.school.persistence.entity.RegularAdEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
@Repository
public interface RegularAdvertRepository extends JpaRepository<RegularAdEntity, Long> {
    @Query("SELECT ra FROM RegularAdEntity ra JOIN FETCH ra.category c WHERE (:categoryId IS NULL OR c.id = :categoryId) " +
            "AND (:verificationStatus IS NULL OR ra.verification = :verificationStatus) " +
            "AND (:adStatus IS NULL OR ra.adStatus = :adStatus)")
    List<RegularAdEntity> findByCriteria(@Param("categoryId") @Nullable Long categoryId,
                                         @Param("verificationStatus") @Nullable  VerificationStatus verificationStatus,
                                         @Param("adStatus") @Nullable AdStatus adStatus);
    @Transactional
    @Modifying
    @Query("UPDATE RegularAdEntity ad SET " +
            "ad.verification = CASE WHEN :verificationStatus IS NULL THEN ad.verification ELSE :verificationStatus END, " +
            "ad.adStatus = CASE WHEN :adStatus IS NULL THEN ad.adStatus ELSE :adStatus END " +
            "WHERE ad.id = :adId")
    int updateAdStatus(@Param("adId") Long adId,
                        @Param("verificationStatus") VerificationStatus verificationStatus,
                        @Param("adStatus") AdStatus adStatus);

    @Transactional
    @Modifying
    @Query("UPDATE RegularAdEntity r SET r.viewCount = r.viewCount + 1 WHERE r.id = :id")
    void incrementViewCount(Long id);

    List<RegularAdEntity> findByBuyerId(Long id);

}


