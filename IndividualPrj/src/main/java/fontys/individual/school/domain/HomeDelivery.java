package fontys.individual.school.domain;

import fontys.individual.school.domain.enumClasses.DeliveryType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;
@Data
@SuperBuilder
@AllArgsConstructor
public class HomeDelivery extends Delivery{
    @Builder.Default private String deliveryDescription = "";
    private String postCode;
    private String city;
    private String street;
    private String houseNumber;
    @Override
    public double getDeliveryFee() {
        return 15; //15$
    }

    @Override
    public boolean registerTimeSlot(LocalDate deliveryDate, LocalTime startTime, LocalTime endTime) {
        LocalDate now = LocalDate.now();
        if(!deliveryDate.isAfter(now)){
            return false;
        }

        if(!(startTime.getMinute() % 30 == 0)){
            return false;
        }

        super.setDeliveryDate(Optional.of(deliveryDate));
        super.setStartTime(Optional.of(startTime));
        super.setEndTime(Optional.of(endTime));
        return true;
    }

    @Override
    public String getTimeSlotDetails() {
        Optional<LocalTime> startTime = getStartTime();
        Optional<LocalTime> endTime = getEndTime();
        Optional<LocalDate> deliveryDate = getDeliveryDate();

        if (startTime.isPresent() && endTime.isPresent() && deliveryDate.isPresent()) {
            return "Delivery time from " + startTime.get().toString() + " to " + endTime.get().toString() + " On " + deliveryDate.get().toString();
        } else {
            return "Time slot has not been specified.";
        }
    }

    @Override
    public DeliveryType getDeliveryType() {
        return DeliveryType.HomeDelivery;
    }
}
