package fontys.individual.school.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@Entity
@Table(name = "pick_up_delivery")
@DiscriminatorValue("P")
@SuperBuilder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PickUpEntity extends DeliveryEntity {
    @ManyToOne
    @JoinColumn(name = "pick_up_facility_id")
    private FacilityEntity facility;
}
