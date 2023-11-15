package fontys.individual.school.persistence.entity;

import fontys.individual.school.domain.enumClasses.AdStatus;
import fontys.individual.school.domain.enumClasses.ProductCondition;
import fontys.individual.school.domain.enumClasses.VerificationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "discriminator", discriminatorType = DiscriminatorType.CHAR, length = 50)
@DiscriminatorValue("K")//IN ORDER FOR PRJ TO EXECUTE
@Table(name = "digital_advert_base_table")
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public abstract class DigitalAdvertEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "product_condition")
    private ProductCondition condition;
    @Column(name = "title",nullable = true, columnDefinition = "char(50) default 'empty'")
    private String title;
    @Column(name = "product_description",nullable = true, columnDefinition = "char(50) default 'empty'")
    private String productDescription;
    @Column(name = "view_count",nullable = true, columnDefinition = "INT DEFAULT 0")
    private int viewCount ;
    @NotNull
    @ManyToOne
    @JoinColumn(name = "category_id")
    private CategoryEntity category;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "advertiser_id")
    private AccountEntity advertiser;
    @Column(name = "is_sold",nullable = true, columnDefinition = "BOOLEAN DEFAULT false")
    @Builder.Default private boolean isSold = false;
    @Column(name = "is_ended",nullable = true, columnDefinition = "BOOLEAN DEFAULT false")
    @Builder.Default private boolean isEnded = false;
    @Enumerated(EnumType.STRING)
    @Column(name = "verification_status")
    @Builder.Default private VerificationStatus verification = VerificationStatus.Pending;
    @Enumerated(EnumType.STRING)
    @Column(name = "advert_status")
    @Builder.Default private AdStatus adStatus = AdStatus.Unavailable;

    @OneToOne
    @JoinColumn(name = "delivery_type_id")
    private DeliveryEntity deliveryType;


}
