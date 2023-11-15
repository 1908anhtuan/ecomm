package fontys.individual.school.domain;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import fontys.individual.school.business.impl.Converter.OptionalSerializer;
import fontys.individual.school.domain.enumClasses.DeliveryStatus;
import fontys.individual.school.domain.enumClasses.DeliveryType;
import lombok.AllArgsConstructor;
import lombok.Builder;
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
public abstract class Delivery {
    private Long id;
    @JsonSerialize(using = OptionalSerializer.class)
    private Optional<LocalDate> deliveryDate;
    @JsonSerialize(using = OptionalSerializer.class)
    private Optional<LocalTime> startTime;
    @JsonSerialize(using = OptionalSerializer.class)
    private Optional<LocalTime> endTime;
    protected double deliveryFee;
    @Builder.Default private DeliveryStatus deliveryStatus = DeliveryStatus.Undefined;
    public abstract double getDeliveryFee();
    public abstract boolean registerTimeSlot(LocalDate deliveryDate, LocalTime startTime, LocalTime endTime);
    public abstract String getTimeSlotDetails();
    public abstract DeliveryType getDeliveryType();


}
