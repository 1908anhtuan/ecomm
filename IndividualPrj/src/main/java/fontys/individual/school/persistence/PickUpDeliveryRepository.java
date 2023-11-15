package fontys.individual.school.persistence;

import fontys.individual.school.domain.enumClasses.DeliveryStatus;
import fontys.individual.school.persistence.entity.HomeDeliveryEntity;
import fontys.individual.school.persistence.entity.PickUpEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.time.LocalDate;


@Repository
public interface PickUpDeliveryRepository extends JpaRepository<PickUpEntity, Long> {
    @Query("SELECT COUNT(p) FROM PickUpEntity p " +
            "WHERE p.deliveryDate >= :startDate AND p.deliveryDate <= :endDate ")
    long getPickUpDeliveryCount(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Transactional
    @Modifying
    @Query("UPDATE PickUpEntity p SET p.deliveryStatus = :status WHERE p.id = :id")
    int  updateDeliveryStatus(@Param("id") Long id, @Param("status") DeliveryStatus status);

}
