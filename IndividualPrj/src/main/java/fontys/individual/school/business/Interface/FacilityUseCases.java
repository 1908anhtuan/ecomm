package fontys.individual.school.business.Interface;

import fontys.individual.school.domain.HttpResponseRequest.CreateFacilityRequest;
import fontys.individual.school.domain.HttpResponseRequest.CreateFacilityResponse;
import fontys.individual.school.domain.HttpResponseRequest.GetAllFacilitiesResponse;

public interface FacilityUseCases {
    CreateFacilityResponse createFacility(CreateFacilityRequest request);
    GetAllFacilitiesResponse getAll();
}
