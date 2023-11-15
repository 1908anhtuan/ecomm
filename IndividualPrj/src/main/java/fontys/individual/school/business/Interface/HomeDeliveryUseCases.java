package fontys.individual.school.business.Interface;

import fontys.individual.school.domain.HttpResponseRequest.*;

public interface HomeDeliveryUseCases {
    GetAvailableTimeSlotsResponse getAvailableTimeSlots();

    RegisterTimeSlotResponse registerTimeSlot(RegisterHomeDeliveryTimeSlotRequest request);

    GetCountOfHomeDeliveryFromAPeriodOfTimeResponse countOfHomeDelivery(GetCountOfHomeDeliveryFromAPeriodOfTimeRequest request);
    GetAllHomeDeliveriesResponse getAll();
    UpdateHomeDeliveryResponse updateDeliveryStatus(UpdateHomeDeliveryRequest request);


}
