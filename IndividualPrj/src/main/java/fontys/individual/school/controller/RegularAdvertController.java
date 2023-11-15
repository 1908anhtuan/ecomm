package fontys.individual.school.controller;

import fontys.individual.school.business.Interface.RegularAdvertUseCases;
import fontys.individual.school.business.exception.AdvertNotFoundException;
import fontys.individual.school.business.exception.InsufficientBalanceException;
import fontys.individual.school.business.exception.ResourceNotAvailableException;
import fontys.individual.school.business.exception.UserNotFoundException;
import fontys.individual.school.configuration.security.isauthenticated.IsAuthenticated;
import fontys.individual.school.domain.HttpResponseRequest.*;
import fontys.individual.school.domain.enumClasses.AdStatus;
import fontys.individual.school.domain.enumClasses.VerificationStatus;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;

@RestController
@RequestMapping("/regularAds")
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.PATCH})
public class RegularAdvertController {
    private final RegularAdvertUseCases regularAdvertUseCases;

    @IsAuthenticated
    @RolesAllowed({"Admin", "RegularUser"})
    @PostMapping()
    public ResponseEntity<CreateRegularAdvertResponse> createAdvert(@RequestBody @Valid CreateRegularAdvertRequest request){
        CreateRegularAdvertResponse response = regularAdvertUseCases.createRegularAdvert(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    @GetMapping
    public ResponseEntity<GetAllRegularAdvertsResponse> getAllRegularAdvert(){
        GetAllRegularAdvertsResponse response = regularAdvertUseCases.getAll();
        return ResponseEntity.ok(response);
    }


    @GetMapping("/filter")
    public ResponseEntity<GetAllRegularAdvertsByFilterResponse> getAllRegularAdvertsByFilter(
            @RequestParam(required = false) @Nullable  Long categoryId,
            @RequestParam(required = false) @Nullable  VerificationStatus verificationStatus,
            @RequestParam(required = false) @Nullable AdStatus adStatus
    ) {
        GetAllRegularAdvertsByFilterResponse response = regularAdvertUseCases.getAllFilteredRegularAds(GetAllRegularAdvertsByFilterRequest.builder().adStatus(adStatus).categoryId(categoryId).verificationStatus(verificationStatus).build());
        return ResponseEntity.ok(response);
    }




    @GetMapping("{id}")
    public ResponseEntity<GetRegularAdvertByIdResponse> getAdvertById(@PathVariable Long id){
        GetRegularAdvertByIdResponse response = regularAdvertUseCases.getRegularAdvertDetails(id);
        return ResponseEntity.ok(response);
    }

    @IsAuthenticated
    @RolesAllowed({"RegularUser","Admin"})
    @GetMapping("/history/{id}")
    public ResponseEntity<GetPersonalRegularAdsResponse> getPersonalRegularAds(@PathVariable Long id){
        GetPersonalRegularAdsResponse response = regularAdvertUseCases.getPersonalRegularAdverts(GetPersonalRegularAdsRequest.builder().id(id).build());
        return ResponseEntity.ok(response);
    }

    @IsAuthenticated
    @RolesAllowed({"Admin"})
    @DeleteMapping("{id}")
    public ResponseEntity<DeleteRegularAdvertResponse> deleteRegularAdvertById(@PathVariable Long id){
        boolean deleted = regularAdvertUseCases.deleteRegularAdvert(id);
        if(deleted){
            return ResponseEntity.ok(DeleteRegularAdvertResponse.builder().stringResponse("Regular advert successfully deleted").build());
        }else{
            return ResponseEntity.notFound().build();
        }
    }
    @IsAuthenticated
    @RolesAllowed({"RegularUser","Admin"})
    @PostMapping("/{advertId}/buy")
    public ResponseEntity<BuyRegularAdvertResponse> buyRegularAdvert(@PathVariable Long advertId,
                                                                     @RequestBody BuyRegularAdvertRequest request) {
        try {
            BuyRegularAdvertResponse response = regularAdvertUseCases.buyRegularAdvert(advertId, request);
            return ResponseEntity.ok(response);
        } catch (AdvertNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (InsufficientBalanceException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (ResourceNotAvailableException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @IsAuthenticated
    @RolesAllowed({"Admin", "RegularUser"})
    @PatchMapping("/{id}/status")
    public ResponseEntity<SetAdvertStatusResponse> setAdStatus(@PathVariable Long id, @RequestBody SetAdvertStatusRequest request) {
        request.setId(id);
        SetAdvertStatusResponse response = regularAdvertUseCases.setAdStatus(request);
        return ResponseEntity.ok(response);
    }
}
