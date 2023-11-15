package fontys.individual.school.business.Interface;

import fontys.individual.school.domain.HttpResponseRequest.*;

public interface PickUpDeliveryUseCases {
    GetAvailableTimeSlotsResponse getAvailableTimeSlots(GetAvailableTimeSlotsRequest request);

    RegisterTimeSlotResponse registerTimeSlot(RegisterTimeSlotRequest request);

    GetCountOfPickUpDeliveryFromAPeriodOfTimeResponse countOfPickUpDelivery(GetCountOfPickUpDeliveryFromAPeriodOfTimeRequest request);
    GetAllPickUpDeliveriesResponse getAll();
    UpdatePickUpDeliveryResponse updateDeliveryStatus(UpdatePickUpDeliveryRequest request);

}
