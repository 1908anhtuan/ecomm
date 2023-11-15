package fontys.individual.school.persistence;

import fontys.individual.school.domain.enumClasses.AdStatus;
import fontys.individual.school.domain.enumClasses.VerificationStatus;
import fontys.individual.school.persistence.entity.BiddingAdEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
@Repository
public interface BiddingAdvertRepository extends JpaRepository<BiddingAdEntity, Long> {
    @Query("SELECT ra FROM BiddingAdEntity ra JOIN FETCH ra.category c WHERE (:categoryId IS NULL OR c.id = :categoryId) " +
            "AND (:verificationStatus IS NULL OR ra.verification = :verificationStatus) " +
            "AND (:adStatus IS NULL OR ra.adStatus = :adStatus)")
    List<BiddingAdEntity> findByCriteria(@Param("categoryId") @Nullable Long categoryId,
                                         @Param("verificationStatus") @Nullable  VerificationStatus verificationStatus,
                                         @Param("adStatus") @Nullable AdStatus adStatus);
    @Transactional
    @Modifying
    @Query("UPDATE BiddingAdEntity ad SET " +
            "ad.verification = CASE WHEN :verificationStatus IS NULL THEN ad.verification ELSE :verificationStatus END, " +
            "ad.adStatus = CASE WHEN :adStatus IS NULL THEN ad.adStatus ELSE :adStatus END " +
            "WHERE ad.id = :adId")
    int updateAdStatus(@Param("adId") Long adId,
                        @Param("verificationStatus") VerificationStatus verificationStatus,
                        @Param("adStatus") AdStatus adStatus);
    @Transactional
    @Modifying
    @Query("UPDATE BiddingAdEntity r SET r.viewCount = r.viewCount + 1 WHERE r.id = :id")
    void incrementViewCount(Long id);

    @Query("SELECT ad FROM BiddingAdEntity ad " +
            "WHERE ad.highestBidder.id = :bidderId " +
            "AND ad.isEnded = :adEnded")
    List<BiddingAdEntity> findAdsByHighestBidderAndStatus(
            @Param("bidderId") Long bidderId,
            @Param("adEnded") boolean adStatusValue);

}
