package fontys.individual.school;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import fontys.individual.school.business.Interface.HomeDeliveryUseCases;
import fontys.individual.school.business.exception.BadRequestException;
import fontys.individual.school.business.exception.DeliveryNotFoundException;
import fontys.individual.school.business.exception.IllegalAdvertException;
import fontys.individual.school.business.impl.HomeDeliveryImpl;
import fontys.individual.school.domain.*;
import fontys.individual.school.domain.HttpResponseRequest.*;
import fontys.individual.school.domain.enumClasses.DeliveryStatus;
import fontys.individual.school.persistence.AdvertRepository;
import fontys.individual.school.persistence.HomeDeliveryRepository;
import fontys.individual.school.persistence.entity.BiddingAdEntity;
import fontys.individual.school.persistence.entity.DigitalAdvertEntity;
import fontys.individual.school.persistence.entity.HomeDeliveryEntity;
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

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

public class HomeDeliveryImplTest {
    @Mock
    private HomeDeliveryRepository homeDeliveryRepository;
    @Mock
    private AdvertRepository advertRepository;

    private HomeDeliveryUseCases homeDeliveryUseCases;
//
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        homeDeliveryUseCases = new HomeDeliveryImpl(homeDeliveryRepository, advertRepository);
    }
//z
    @Test
    public void testGetAvailableTimeSlots() {
        LocalTime startTime = LocalTime.of(9, 0);
        LocalTime endTime = LocalTime.of(18, 0);
        int intervalMinutes = 30;

        List<TimeSlot> expectedTimeSlots = new ArrayList<>();

        while (startTime.isBefore(endTime)) {
            TimeSlot slot = TimeSlot.builder()
                    .startTime(startTime)
                    .endTime(startTime.plusMinutes(intervalMinutes))
                    .build();

            expectedTimeSlots.add(slot);
            startTime = startTime.plusMinutes(intervalMinutes);
        }

        GetAvailableTimeSlotsResponse expectedResponse = GetAvailableTimeSlotsResponse.builder()
                .timeSlotList(expectedTimeSlots)
                .build();

        GetAvailableTimeSlotsResponse actualResponse = homeDeliveryUseCases.getAvailableTimeSlots();

        Assertions.assertEquals(expectedResponse, actualResponse);
    }

    @Test
    public void testRegisterTimeSlot_SuccessfulRegistration() {
        RegisterHomeDeliveryTimeSlotRequest request = RegisterHomeDeliveryTimeSlotRequest.builder()
                .selectedDate(LocalDate.now().plusDays(3))
                .timeSlot(TimeSlot.builder().startTime(LocalTime.of(9, 0)).endTime(LocalTime.of(9, 30)).build())
                .digitalAdvertId(123L)
                .address(HomeDeliveryAddress.builder().city("City").street("Street").houseNumber("1").postCode("12345").build())
                .build();

        DigitalAdvertEntity digitalAdvert = new BiddingAdEntity();
        digitalAdvert.setId(123L);

        Mockito.when(advertRepository.findById(123L)).thenReturn(Optional.of(digitalAdvert));

        RegisterTimeSlotResponse response = homeDeliveryUseCases.registerTimeSlot(request);

        Assertions.assertEquals("Successfully registered", response.getMessage());
        Mockito.verify(homeDeliveryRepository).save(Mockito.any(HomeDeliveryEntity.class));
        Mockito.verify(advertRepository).save(digitalAdvert);
    }

    @Test
    public void testRegisterTimeSlot_IllegalAdvertException() {
        RegisterHomeDeliveryTimeSlotRequest request = RegisterHomeDeliveryTimeSlotRequest.builder()
                .selectedDate(LocalDate.now())
                .timeSlot(TimeSlot.builder().startTime(LocalTime.of(9, 0)).endTime(LocalTime.of(9, 30)).build())
                .digitalAdvertId(123L)
                .address(HomeDeliveryAddress.builder().city("City").street("Street").houseNumber("1").postCode("12345").build())
                .build();

        Mockito.when(advertRepository.findById(123L)).thenReturn(Optional.empty());

        Assertions.assertThrows(IllegalAdvertException.class, () -> {
            homeDeliveryUseCases.registerTimeSlot(request);
        });

        Mockito.verify(homeDeliveryRepository, Mockito.never()).save(Mockito.any(HomeDeliveryEntity.class));
        Mockito.verify(advertRepository, Mockito.never()).save(Mockito.any(DigitalAdvertEntity.class));
    }

    @Test
    public void testCountOfHomeDelivery_validDates_shouldReturnCount() {
        GetCountOfHomeDeliveryFromAPeriodOfTimeRequest request = new GetCountOfHomeDeliveryFromAPeriodOfTimeRequest();
        request.setStartDate(LocalDate.of(2023, 1, 1));
        request.setEndDate(LocalDate.of(2023, 1, 5));

        Mockito.when(homeDeliveryRepository.getHomeDeliveryCount(request.getStartDate(), request.getEndDate()))
                .thenReturn(5L);

        GetCountOfHomeDeliveryFromAPeriodOfTimeResponse response = homeDeliveryUseCases.countOfHomeDelivery(request);

        Assertions.assertEquals(5L, response.getCount(), "Expected count mismatch");
    }

    @Test
    public void testCountOfHomeDelivery_startDateAfterEndDate_shouldThrowBadRequestException() {
        GetCountOfHomeDeliveryFromAPeriodOfTimeRequest request = new GetCountOfHomeDeliveryFromAPeriodOfTimeRequest();
        request.setStartDate(LocalDate.of(2023, 1, 5));
        request.setEndDate(LocalDate.of(2023, 1, 1));

        Assertions.assertThrows(BadRequestException.class, () -> homeDeliveryUseCases.countOfHomeDelivery(request));
    }

    @Test
    public void testCountOfHomeDelivery_sameStartDateAndEndDate_shouldThrowBadRequestException() {
        GetCountOfHomeDeliveryFromAPeriodOfTimeRequest request = new GetCountOfHomeDeliveryFromAPeriodOfTimeRequest();
        request.setStartDate(LocalDate.of(2023, 1, 1));
        request.setEndDate(LocalDate.of(2023, 1, 1));

        Assertions.assertThrows(BadRequestException.class, () -> homeDeliveryUseCases.countOfHomeDelivery(request));
    }

    @Test
    public void testGetAll_shouldReturnAllDeliveries() {
        List<HomeDeliveryEntity> entities = Arrays.asList(
                new HomeDeliveryEntity(), new HomeDeliveryEntity(), new HomeDeliveryEntity());

        Mockito.when(homeDeliveryRepository.findAll()).thenReturn(entities);

        GetAllHomeDeliveriesResponse response = homeDeliveryUseCases.getAll();

        assertEquals(entities.size(), response.getDeliveries().size());
    }

    @Test
    public void testUpdateDeliveryStatus_existingDelivery_shouldReturnIsUpdatedTrue() {
        UpdateHomeDeliveryRequest request = new UpdateHomeDeliveryRequest();
        request.setId(1L);
        request.setDeliveryStatus(DeliveryStatus.Delivered);

        Optional<HomeDeliveryEntity> entity = Optional.of(new HomeDeliveryEntity());

        Mockito.when(homeDeliveryRepository.findById(request.getId())).thenReturn(entity);
        Mockito.when(homeDeliveryRepository.updateDeliveryStatus(request.getId(), request.getDeliveryStatus()))
                .thenReturn(1);

        UpdateHomeDeliveryResponse response = homeDeliveryUseCases.updateDeliveryStatus(request);

        Assertions.assertTrue(response.isUpdated());
    }

    @Test
    public void testUpdateDeliveryStatus_nonExistingDelivery_shouldThrowDeliveryNotFoundException() {
        UpdateHomeDeliveryRequest request = new UpdateHomeDeliveryRequest();
        request.setId(1L);
        request.setDeliveryStatus(DeliveryStatus.Delivered);

        Mockito.when(homeDeliveryRepository.findById(request.getId())).thenReturn(Optional.empty());

        Assertions.assertThrows(DeliveryNotFoundException.class, () -> homeDeliveryUseCases.updateDeliveryStatus(request));
    }
}