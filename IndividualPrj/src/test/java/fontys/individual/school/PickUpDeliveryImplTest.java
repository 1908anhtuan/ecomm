package fontys.individual.school;

import fontys.individual.school.business.Interface.PickUpDeliveryUseCases;
import fontys.individual.school.business.exception.BadRequestException;
import fontys.individual.school.business.exception.DeliveryNotFoundException;
import fontys.individual.school.business.exception.IllegalAdvertException;
import fontys.individual.school.business.impl.PickUpDeliveryImpl;
import fontys.individual.school.domain.*;
import fontys.individual.school.domain.HttpResponseRequest.*;
import fontys.individual.school.domain.enumClasses.DeliveryStatus;
import fontys.individual.school.persistence.AdvertRepository;
import fontys.individual.school.persistence.PickUpDeliveryRepository;
import fontys.individual.school.persistence.entity.BiddingAdEntity;
import fontys.individual.school.persistence.entity.DigitalAdvertEntity;
import fontys.individual.school.persistence.entity.FacilityEntity;
import fontys.individual.school.persistence.entity.PickUpEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class PickUpDeliveryImplTest {
    @Mock
    private PickUpDeliveryRepository pickUpDeliveryRepository;
    @Mock
    private AdvertRepository advertRepository;

    private PickUpDeliveryUseCases pickUpDeliveryUseCases;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        pickUpDeliveryUseCases = new PickUpDeliveryImpl(pickUpDeliveryRepository, advertRepository);
    }

    @Test
    public void testGetAvailableTimeSlots() {
        PickUpFacility selectedFacility = PickUpFacility.builder()
                .openTime(LocalTime.of(9, 0))
                .closeTime(LocalTime.of(18, 0))
                .build();

        LocalDate selectedDate = LocalDate.now();

        GetAvailableTimeSlotsRequest request = GetAvailableTimeSlotsRequest.builder()
                .selectedFacility(selectedFacility)
                .selectedDate(selectedDate)
                .build();

        LocalTime startTime = selectedFacility.getOpenTime();
        LocalTime endTime = selectedFacility.getCloseTime();
        int intervalMinutes = 30;

        List<TimeSlot> expectedTimeSlots = new ArrayList<>();

        while (startTime.isBefore(endTime)) {
            TimeSlot timeSlot = TimeSlot.builder()
                    .startTime(startTime)
                    .endTime(startTime.plusMinutes(intervalMinutes))
                    .deliveryDate(selectedDate)
                    .build();
            expectedTimeSlots.add(timeSlot);
            startTime = startTime.plusMinutes(intervalMinutes);
        }

        GetAvailableTimeSlotsResponse expectedResponse = GetAvailableTimeSlotsResponse.builder()
                .timeSlotList(expectedTimeSlots)
                .build();

        GetAvailableTimeSlotsResponse actualResponse = pickUpDeliveryUseCases.getAvailableTimeSlots(request);

        Assertions.assertEquals(expectedResponse, actualResponse);
    }

    @Test
    public void testRegisterTimeSlot_SuccessfulRegistration() {
        RegisterTimeSlotRequest request = RegisterTimeSlotRequest.builder()
                .facility(PickUpFacility.builder().build())
                .timeSlot(TimeSlot.builder().deliveryDate(LocalDate.now().plusDays(10)).startTime(LocalTime.of(9,0)).endTime(LocalTime.of(22,00)).build())
                .digitalAdvertId(123L)
                .build();

        PickUpEntity pickUpEntity = new PickUpEntity();

        Mockito.when(pickUpDeliveryRepository.save(Mockito.any(PickUpEntity.class))).thenReturn(pickUpEntity);
        Mockito.when(advertRepository.findById(123L)).thenReturn(Optional.of(new BiddingAdEntity()));

        RegisterTimeSlotResponse response = pickUpDeliveryUseCases.registerTimeSlot(request);

        Assertions.assertEquals("Successfully registered", response.getMessage());
        Mockito.verify(pickUpDeliveryRepository).save(Mockito.any(PickUpEntity.class));
        Mockito.verify(advertRepository).save(Mockito.any(DigitalAdvertEntity.class));
    }

    @Test
    public void testRegisterTimeSlot_IllegalAdvertException() {
        RegisterTimeSlotRequest request = RegisterTimeSlotRequest.builder()
                .facility(PickUpFacility.builder().build())
                .timeSlot(TimeSlot.builder().deliveryDate(LocalDate.now()).startTime(LocalTime.now()).endTime(LocalTime.now().plusHours(10)).build())
                .digitalAdvertId(123L)
                .build();

        Mockito.when(pickUpDeliveryRepository.save(Mockito.any(PickUpEntity.class))).thenReturn(new PickUpEntity());
        Mockito.when(advertRepository.findById(123L)).thenReturn(Optional.empty());

        Assertions.assertThrows(IllegalAdvertException.class, () -> {
            pickUpDeliveryUseCases.registerTimeSlot(request);
        });

        Mockito.verify(pickUpDeliveryRepository, Mockito.never()).save(Mockito.any(PickUpEntity.class));
        Mockito.verify(advertRepository, Mockito.never()).save(Mockito.any(DigitalAdvertEntity.class));
    }

    @Test
    public void testCountOfPickUpDelivery_validDates_shouldReturnCount() {
        GetCountOfPickUpDeliveryFromAPeriodOfTimeRequest request = new GetCountOfPickUpDeliveryFromAPeriodOfTimeRequest();
        request.setStartDate(LocalDate.of(2023, 1, 1));
        request.setEndDate(LocalDate.of(2023, 1, 5));

        Mockito.when(pickUpDeliveryRepository.getPickUpDeliveryCount(request.getStartDate(), request.getEndDate()))
                .thenReturn(5L);

        GetCountOfPickUpDeliveryFromAPeriodOfTimeResponse response = pickUpDeliveryUseCases.countOfPickUpDelivery(request);

        Assertions.assertEquals(5L, response.getCount(), "Expected count mismatch");
    }

    @Test
    public void testCountOfPickUpDelivery_startDateAfterEndDate_shouldThrowBadRequestException() {
        GetCountOfPickUpDeliveryFromAPeriodOfTimeRequest request = new GetCountOfPickUpDeliveryFromAPeriodOfTimeRequest();
        request.setStartDate(LocalDate.of(2023, 1, 5));
        request.setEndDate(LocalDate.of(2023, 1, 1));

        Assertions.assertThrows(BadRequestException.class, () -> pickUpDeliveryUseCases.countOfPickUpDelivery(request));
    }

    @Test
    public void testCountOfPickUpDelivery_sameStartDateAndEndDate_shouldThrowBadRequestException() {
        GetCountOfPickUpDeliveryFromAPeriodOfTimeRequest request = new GetCountOfPickUpDeliveryFromAPeriodOfTimeRequest();
        request.setStartDate(LocalDate.of(2023, 1, 1));
        request.setEndDate(LocalDate.of(2023, 1, 1));

        Assertions.assertThrows(BadRequestException.class, () -> pickUpDeliveryUseCases.countOfPickUpDelivery(request));
    }

    @Test
    public void testGetAll_shouldReturnAllDeliveries() {
        List<PickUpEntity> entities = Arrays.asList(
                PickUpEntity.builder().facility(FacilityEntity.builder().build()).build(), PickUpEntity.builder().facility(FacilityEntity.builder().build()).build(), PickUpEntity.builder().facility(FacilityEntity.builder().build()).build());

        Mockito.when(pickUpDeliveryRepository.findAll()).thenReturn(entities);

        GetAllPickUpDeliveriesResponse response = pickUpDeliveryUseCases.getAll();

        Assertions.assertEquals(entities.size(), response.getDeliveries().size());
    }

    @Test
    public void testUpdateDeliveryStatus_existingDelivery_shouldReturnIsUpdatedTrue() {
        UpdatePickUpDeliveryRequest request = new UpdatePickUpDeliveryRequest();
        request.setId(1L);
        request.setDeliveryStatus(DeliveryStatus.Delivered);

        Optional<PickUpEntity> entity = Optional.of(new PickUpEntity());

        Mockito.when(pickUpDeliveryRepository.findById(request.getId())).thenReturn(entity);
        Mockito.when(pickUpDeliveryRepository.updateDeliveryStatus(request.getId(), request.getDeliveryStatus()))
                .thenReturn(1);

        UpdatePickUpDeliveryResponse response = pickUpDeliveryUseCases.updateDeliveryStatus(request);

        Assertions.assertTrue(response.isUpdated());
    }

    @Test
    public void testUpdateDeliveryStatus_nonExistingDelivery_shouldThrowDeliveryNotFoundException() {
        UpdatePickUpDeliveryRequest request = new UpdatePickUpDeliveryRequest();
        request.setId(1L);
        request.setDeliveryStatus(DeliveryStatus.Delivered);

        Mockito.when(pickUpDeliveryRepository.findById(request.getId())).thenReturn(Optional.empty());

        Assertions.assertThrows(DeliveryNotFoundException.class, () -> pickUpDeliveryUseCases.updateDeliveryStatus(request));
    }
}