package fontys.individual.school.business.impl;

import fontys.individual.school.business.Interface.HomeDeliveryUseCases;
import fontys.individual.school.business.exception.AdvertNotFoundException;
import fontys.individual.school.business.exception.BadRequestException;
import fontys.individual.school.business.exception.DeliveryNotFoundException;
import fontys.individual.school.business.exception.IllegalAdvertException;
import fontys.individual.school.business.impl.Converter.HomeDeliveryConverter;
import fontys.individual.school.business.impl.Converter.PickUpDeliveryConverter;
import fontys.individual.school.domain.HomeDelivery;
import fontys.individual.school.domain.HttpResponseRequest.*;
import fontys.individual.school.domain.TimeSlot;
import fontys.individual.school.domain.enumClasses.DeliveryStatus;
import fontys.individual.school.persistence.AdvertRepository;
import fontys.individual.school.persistence.HomeDeliveryRepository;
import fontys.individual.school.persistence.entity.DigitalAdvertEntity;
import fontys.individual.school.persistence.entity.HomeDeliveryEntity;
import fontys.individual.school.persistence.entity.PickUpEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class HomeDeliveryImpl implements HomeDeliveryUseCases {
    private final HomeDeliveryRepository homeDeliveryRepository;
    private final AdvertRepository advertRepository;
    @Override
    public GetAvailableTimeSlotsResponse getAvailableTimeSlots() {
        List<TimeSlot> timeSlots = new ArrayList<>();
        LocalTime startTime = LocalTime.of(9, 0);
        LocalTime endTime = LocalTime.of(18, 0);

        while (startTime.isBefore(endTime)) {
            TimeSlot slot = TimeSlot.builder().startTime(startTime).endTime(startTime.plusMinutes(30)).build();
            timeSlots.add(slot);
            startTime = startTime.plusMinutes(30);
        }

        return GetAvailableTimeSlotsResponse.builder().timeSlotList(timeSlots).build();
    }

    @Override
    public RegisterTimeSlotResponse registerTimeSlot(RegisterHomeDeliveryTimeSlotRequest request) {
        HomeDelivery toBeSavedHomeDelivery = HomeDelivery.builder()
                .deliveryDate(Optional.of(request.getSelectedDate()))
                .startTime(Optional.of(request.getTimeSlot().getStartTime()))
                .endTime(Optional.of(request.getTimeSlot().getEndTime()))
                .deliveryFee(0)
                .deliveryStatus(DeliveryStatus.Pending)
                .city(request.getAddress().getCity())
                .street(request.getAddress().getStreet())
                .houseNumber(request.getAddress().getHouseNumber())
                .postCode(request.getAddress().getPostCode())
                .build();

        if(!toBeSavedHomeDelivery.registerTimeSlot(request.getSelectedDate(), request.getTimeSlot().getStartTime(), request.getTimeSlot().getEndTime())){
            throw new IllegalAdvertException();
        }
        HomeDeliveryEntity homeDeliveryEntity = homeDeliveryRepository.save(new HomeDeliveryConverter().convertToEntity(toBeSavedHomeDelivery));

        Optional<DigitalAdvertEntity> digitalAdvert = advertRepository.findById(request.getDigitalAdvertId());
        if(digitalAdvert.isEmpty()){
            throw new AdvertNotFoundException();
        }
        digitalAdvert.get().setDeliveryType(homeDeliveryEntity);
        advertRepository.save(digitalAdvert.get());
        return RegisterTimeSlotResponse.builder().message("Successfully registered").build();

    }

    @Override
    public GetCountOfHomeDeliveryFromAPeriodOfTimeResponse countOfHomeDelivery(GetCountOfHomeDeliveryFromAPeriodOfTimeRequest request) {
        if(request.getStartDate().isAfter(request.getEndDate())){
            throw new BadRequestException("Start date must be before end date");
        }
        if(request.getStartDate().isEqual(request.getEndDate())){
            throw new BadRequestException("Start date must be different from end date");
        }
        Long advertCount = homeDeliveryRepository.getHomeDeliveryCount(request.getStartDate(), request.getEndDate());

        return GetCountOfHomeDeliveryFromAPeriodOfTimeResponse.builder().count(advertCount).build();
    }

    @Override
    public GetAllHomeDeliveriesResponse getAll() {
        List<HomeDeliveryEntity> entities = homeDeliveryRepository.findAll();

        return GetAllHomeDeliveriesResponse.builder().deliveries(entities.stream().map(new HomeDeliveryConverter()::convert).toList()).build();
    }

    @Override
    public UpdateHomeDeliveryResponse updateDeliveryStatus(UpdateHomeDeliveryRequest request) {
        Optional<HomeDeliveryEntity> entity = homeDeliveryRepository.findById(request.getId());
        if (entity.isEmpty()) {
            throw new DeliveryNotFoundException();
        }

        boolean isUpdated = false;
        int count = homeDeliveryRepository.updateDeliveryStatus(request.getId(), request.getDeliveryStatus());
        if(count > 0){
            isUpdated = true;
        }
        return UpdateHomeDeliveryResponse.builder().isUpdated(isUpdated).build();
    }
}
