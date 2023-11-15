package fontys.individual.school.domain;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import fontys.individual.school.business.impl.Converter.OptionalSerializer;
import fontys.individual.school.domain.enumClasses.DeliveryType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class PickUpDelivery extends Delivery{
    @JsonSerialize(using = OptionalSerializer.class)
    private Optional<PickUpFacility> pickUpFacility;

    @Override
    public double getDeliveryFee() {
        return 0; //Pick up delivery is free
    }

    @Override
    public boolean registerTimeSlot(LocalDate deliveryDate, LocalTime startTime, LocalTime endTime) {//TimeFrame: date + start time + end time
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
            return "Pick up time from " + startTime.get().toString() + " to " + endTime.get().toString() + " On " + deliveryDate.get().toString();
        } else {
            return "Time slot has not been specified.";
        }
    }

    @Override
    public DeliveryType getDeliveryType() {
        return DeliveryType.PickUpDelivery;
    }
}
