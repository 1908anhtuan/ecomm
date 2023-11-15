package fontys.individual.school;

import fontys.individual.school.business.Interface.FacilityUseCases;
import fontys.individual.school.business.exception.FacilityAlreadyExistsException;
import fontys.individual.school.business.impl.FacilityServiceImpl;
import fontys.individual.school.domain.HttpResponseRequest.CreateFacilityRequest;
import fontys.individual.school.domain.HttpResponseRequest.CreateFacilityResponse;
import fontys.individual.school.domain.HttpResponseRequest.GetAllFacilitiesResponse;
import fontys.individual.school.domain.PickUpFacility;
import fontys.individual.school.persistence.FacilityRepository;
import fontys.individual.school.persistence.entity.FacilityEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class FacilityServiceTest {
    @Mock
    private FacilityRepository facilityRepository;

    private FacilityUseCases facilityUseCases;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        facilityUseCases = new FacilityServiceImpl(facilityRepository);
    }

    @Test
    public void testCreateFacility_WhenFacilityDoesNotExist_ShouldReturnFacilityId() {
        // Arrange
        CreateFacilityRequest request = new CreateFacilityRequest("FacilityName", "PostCode", "City",
                LocalTime.of(9, 0), LocalTime.of(17, 0), 5L);
        Mockito.when(facilityRepository.existsByNameAndPostCode(Mockito.anyString(), Mockito.anyString()))
                .thenReturn(false);
        Mockito.when(facilityRepository.save(Mockito.any(FacilityEntity.class)))
                .thenReturn(new FacilityEntity(1L, "FacilityName", "PostCode", "City",
                        LocalTime.of(9, 0), LocalTime.of(17, 0)));

        // Act
        CreateFacilityResponse response = facilityUseCases.createFacility(request);

        // Assert
        Assertions.assertEquals(1L, response.getId());
    }

    @Test
    public void testCreateFacility_WhenFacilityAlreadyExists_ShouldThrowFacilityAlreadyExistsException() {
        // Arrange
        CreateFacilityRequest request = new CreateFacilityRequest("FacilityName", "PostCode", "City",
                LocalTime.of(9, 0), LocalTime.of(17, 0), 5L);
        Mockito.when(facilityRepository.existsByNameAndPostCode(Mockito.anyString(), Mockito.anyString()))
                .thenReturn(true);

        Assertions.assertThrows(FacilityAlreadyExistsException.class,
                () -> facilityUseCases.createFacility(request));
    }

    @Test
    public void testGetAll_ShouldReturnAllFacilities() {
        // Arrange
        List<FacilityEntity> facilityEntities = new ArrayList<>();
        facilityEntities.add(new FacilityEntity(1L, "Facility1", "PostCode1", "City1",
                LocalTime.of(9, 0), LocalTime.of(17, 0)));
        facilityEntities.add(new FacilityEntity(2L, "Facility2", "PostCode2", "City2",
                LocalTime.of(10, 0), LocalTime.of(18, 0)));

        Mockito.when(facilityRepository.findAll()).thenReturn(facilityEntities);

        // Act
        GetAllFacilitiesResponse response = facilityUseCases.getAll();

        // Assert
        List<PickUpFacility> expectedFacilities = new ArrayList<>();
        expectedFacilities.add(new PickUpFacility(1L, "Facility1", "PostCode1", "City1",
                LocalTime.of(9, 0), LocalTime.of(17, 0)));
        expectedFacilities.add(new PickUpFacility(2L, "Facility2", "PostCode2", "City2",
                LocalTime.of(10, 0), LocalTime.of(18, 0)));

        Assertions.assertEquals(expectedFacilities, response.getPickUpFacilityList());
    }
}