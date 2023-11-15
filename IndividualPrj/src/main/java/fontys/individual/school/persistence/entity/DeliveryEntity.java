package fontys.individual.school.persistence.entity;

import fontys.individual.school.domain.enumClasses.DeliveryStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "discriminator", discriminatorType = DiscriminatorType.CHAR, length = 50)
@DiscriminatorValue("K")//IN ORDER FOR PROJECT TO EXECUTE
@Table(name = "selected_delivery_options")
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public abstract class DeliveryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "delivery_date")
    private LocalDate deliveryDate;
    @Column(name = "start_time")
    private LocalTime start_time;
    @Column(name = "end_time")
    private LocalTime end_time;
    @Column(name = "delivery_fee")
    private double deliveryFee;
    @Enumerated(EnumType.STRING)
    @Column(name = "delivery_status")
    private DeliveryStatus deliveryStatus;

}
