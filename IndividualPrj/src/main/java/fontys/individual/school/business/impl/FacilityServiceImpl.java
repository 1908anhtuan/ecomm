package fontys.individual.school.business.impl;

import fontys.individual.school.business.Interface.FacilityUseCases;
import fontys.individual.school.business.exception.FacilityAlreadyExistsException;
import fontys.individual.school.business.impl.Converter.FacilityConverter;
import fontys.individual.school.domain.HttpResponseRequest.CreateFacilityRequest;
import fontys.individual.school.domain.HttpResponseRequest.CreateFacilityResponse;
import fontys.individual.school.domain.HttpResponseRequest.GetAllFacilitiesResponse;
import fontys.individual.school.domain.PickUpFacility;
import fontys.individual.school.persistence.FacilityRepository;
import fontys.individual.school.persistence.entity.FacilityEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class FacilityServiceImpl implements FacilityUseCases {
    private final FacilityRepository facilityRepository;
    @Override
    public CreateFacilityResponse createFacility(CreateFacilityRequest request) {
        if(facilityRepository.existsByNameAndPostCode(request.getFacilityName(), request.getPostCode())){
            throw new FacilityAlreadyExistsException();
        }

        FacilityEntity savedFacilityEntity = SaveFacility(request);

        return CreateFacilityResponse.builder()
                .id(savedFacilityEntity.getId())
                .build();

    }


    @Override
    public GetAllFacilitiesResponse getAll() {
        List<FacilityEntity> results = facilityRepository.findAll();

        List<PickUpFacility> facilities = results
                .stream()
                .map(FacilityConverter::convertToFacility)
                .toList();

        return GetAllFacilitiesResponse.builder()
                .pickUpFacilityList(facilities)
                .build();
    }
    public FacilityEntity SaveFacility(CreateFacilityRequest request){
        FacilityEntity fEntity = FacilityEntity.builder()
                .city(request.getCity())
                .postCode(request.getPostCode())
                .name(request.getFacilityName())
                .openTime(request.getOpenTime())
                .closeTime(request.getCloseTime())
                .build();
        return facilityRepository.save(fEntity);
    }
}
