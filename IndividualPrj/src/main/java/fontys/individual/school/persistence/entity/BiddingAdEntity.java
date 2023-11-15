package fontys.individual.school.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@DiscriminatorValue("B")
@Table(name = "bidding_advert")
@SuperBuilder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BiddingAdEntity extends DigitalAdvertEntity{
    @Column(name = "current_highest_bid", columnDefinition = "DOUBLE DEFAULT 0")
    private double currentHighestBid = 0;
    @Column(name = "initial_bid_price", columnDefinition = "DOUBLE DEFAULT = 0")
    private double initialBidPrice ;
    @Column(name = "buy_it_now_price", columnDefinition = "DOUBLE DEFAULT = 0")
    private double binPrice;
    @Column(name = "bid_expiration_date", columnDefinition = "DATE")
    private LocalDateTime bidExpirationDate;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "highest_bidder_id", columnDefinition = "INT DEFAULT 0")
    private AccountEntity highestBidder;
    @Column(name = "lowest_bid_amount_allowed")
    private double lowestBidAmountAllowed;
}
