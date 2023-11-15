package fontys.individual.school.business.impl;

import fontys.individual.school.business.Interface.PickUpDeliveryUseCases;
import fontys.individual.school.business.exception.AdvertNotFoundException;
import fontys.individual.school.business.exception.BadRequestException;
import fontys.individual.school.business.exception.DeliveryNotFoundException;
import fontys.individual.school.business.exception.IllegalAdvertException;
import fontys.individual.school.business.impl.Converter.HomeDeliveryConverter;
import fontys.individual.school.business.impl.Converter.PickUpDeliveryConverter;
import fontys.individual.school.domain.HomeDelivery;
import fontys.individual.school.domain.HttpResponseRequest.*;
import fontys.individual.school.domain.PickUpDelivery;
import fontys.individual.school.domain.PickUpFacility;
import fontys.individual.school.domain.TimeSlot;
import fontys.individual.school.domain.enumClasses.DeliveryStatus;
import fontys.individual.school.persistence.AdvertRepository;
import fontys.individual.school.persistence.PickUpDeliveryRepository;
import fontys.individual.school.persistence.entity.DigitalAdvertEntity;
import fontys.individual.school.persistence.entity.HomeDeliveryEntity;
import fontys.individual.school.persistence.entity.PickUpEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class PickUpDeliveryImpl implements PickUpDeliveryUseCases {
    private final PickUpDeliveryRepository pickUpDeliveryRepository;
    private final AdvertRepository advertRepository;
        @Override
        public GetAvailableTimeSlotsResponse getAvailableTimeSlots(GetAvailableTimeSlotsRequest request) {
            PickUpFacility pickUpFacility = request.getSelectedFacility();
            LocalDate selectedDate = request.getSelectedDate();

            LocalTime currentTime = pickUpFacility.getOpenTime();
            List<TimeSlot> timeSlots = new ArrayList<>();
            while (currentTime.isBefore(pickUpFacility.getCloseTime())) {

                TimeSlot timeSlot = TimeSlot.builder()
                        .startTime(currentTime)
                        .endTime(currentTime.plusMinutes(30))
                        .deliveryDate(selectedDate)
                        .build();

                timeSlots.add(timeSlot);
                currentTime = currentTime.plusMinutes(30);
            }

            return GetAvailableTimeSlotsResponse.builder().timeSlotList(timeSlots).build();
        }

    @Override
    public RegisterTimeSlotResponse registerTimeSlot(RegisterTimeSlotRequest request) {
        PickUpDelivery toBeSavedPickUpDelivery = PickUpDelivery.builder()
                .pickUpFacility(Optional.of(request.getFacility()))
                .deliveryDate(Optional.of(request.getTimeSlot().getDeliveryDate()))
                .startTime(Optional.of(request.getTimeSlot().getStartTime()))
                .endTime(Optional.of(request.getTimeSlot().getEndTime()))
                .deliveryFee(0)
                .deliveryStatus(DeliveryStatus.Pending)
                .build();


        if(!toBeSavedPickUpDelivery.registerTimeSlot(request.getTimeSlot().getDeliveryDate(), request.getTimeSlot().getStartTime(), request.getTimeSlot().getEndTime())){
            throw new IllegalAdvertException();
        }
        PickUpEntity pickUpDeliveryEntity = pickUpDeliveryRepository.save(new PickUpDeliveryConverter().convertToEntity(toBeSavedPickUpDelivery));


        Optional<DigitalAdvertEntity> digitalAdvert = advertRepository.findById(request.getDigitalAdvertId());
        if(digitalAdvert.isEmpty()){
            throw new AdvertNotFoundException();
        }
        digitalAdvert.get().setDeliveryType(pickUpDeliveryEntity);
        advertRepository.save(digitalAdvert.get());
        return RegisterTimeSlotResponse.builder().message("Successfully registered").build();
    }

    @Override
    public GetCountOfPickUpDeliveryFromAPeriodOfTimeResponse countOfPickUpDelivery(GetCountOfPickUpDeliveryFromAPeriodOfTimeRequest request) {
        if (request.getStartDate().isAfter(request.getEndDate())) {
            throw new BadRequestException("Start date must be before end date");
        }
        if (request.getStartDate().isEqual(request.getEndDate())) {
            throw new BadRequestException("Start date must be different from end date");
        }
        Long deliveryCount = pickUpDeliveryRepository.getPickUpDeliveryCount(request.getStartDate(), request.getEndDate());
        return GetCountOfPickUpDeliveryFromAPeriodOfTimeResponse.builder().count(deliveryCount).build();
    }

    @Override
    public GetAllPickUpDeliveriesResponse getAll() {
        List<PickUpEntity> entities = pickUpDeliveryRepository.findAll();

        return GetAllPickUpDeliveriesResponse.builder().deliveries(entities.stream().map(new PickUpDeliveryConverter()::convert).toList()).build();
    }

    @Override
    public UpdatePickUpDeliveryResponse updateDeliveryStatus(UpdatePickUpDeliveryRequest request) {
        Optional<PickUpEntity> entity = pickUpDeliveryRepository.findById(request.getId());
        if(entity.isEmpty()){
            throw new DeliveryNotFoundException();
        }

        boolean isUpdated = false;
        int count = pickUpDeliveryRepository.updateDeliveryStatus(request.getId(), request.getDeliveryStatus());
        if(count > 0){
            isUpdated = true;
        }
        return UpdatePickUpDeliveryResponse.builder().isUpdated(isUpdated).build();
    }

}
