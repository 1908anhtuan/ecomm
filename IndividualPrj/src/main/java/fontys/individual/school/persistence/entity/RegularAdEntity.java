package fontys.individual.school.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@Entity
@DiscriminatorValue("R")
@SuperBuilder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "regular_advert")
public class RegularAdEntity extends DigitalAdvertEntity {
    @Column(name = "price", columnDefinition = "DOUBLE DEFAULT 0")
    private double price;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "buyer_id", columnDefinition = "INT DEFAULT 0")
    private AccountEntity buyer;


}
