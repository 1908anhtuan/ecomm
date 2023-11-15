package fontys.individual.school.persistence;

import fontys.individual.school.persistence.entity.FacilityEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository

public interface FacilityRepository extends JpaRepository<FacilityEntity, Long> {
    boolean existsByNameAndPostCode(String name, String postCode);
    @Query(value = "SELECT f FROM  facilities LEFT JOIN FETCH f.facilityTimeSlots WHERE f.id = :facilityId", nativeQuery = true)
    FacilityEntity findByIdWithTimeSlots(@Param("facilityId") Long facilityId);
}
