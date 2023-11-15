package fontys.individual.school.controller;

import fontys.individual.school.business.Interface.FacilityUseCases;
import fontys.individual.school.domain.HttpResponseRequest.CreateFacilityRequest;
import fontys.individual.school.domain.HttpResponseRequest.CreateFacilityResponse;
import fontys.individual.school.domain.HttpResponseRequest.GetAllFacilitiesResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
@RestController
@RequestMapping("/facilities")
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.PATCH})
public class FacilityController {
    private final FacilityUseCases facilityUseCase;
    @PostMapping()
    public ResponseEntity<CreateFacilityResponse> createFacility(@RequestBody @Valid CreateFacilityRequest request){
        CreateFacilityResponse response = facilityUseCase.createFacility(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping
    public ResponseEntity<GetAllFacilitiesResponse> getAllFacilities(){
        GetAllFacilitiesResponse response = facilityUseCase.getAll();
        return ResponseEntity.ok(response);
    }
}
