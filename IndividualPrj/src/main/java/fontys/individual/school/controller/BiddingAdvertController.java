package fontys.individual.school.controller;

import fontys.individual.school.business.Interface.BiddingAdvertUseCases;
import fontys.individual.school.business.exception.*;
import fontys.individual.school.configuration.security.isauthenticated.IsAuthenticated;
import fontys.individual.school.domain.BiddingAdvert;
import fontys.individual.school.domain.HttpResponseRequest.*;
import fontys.individual.school.domain.enumClasses.AdStatus;
import fontys.individual.school.domain.enumClasses.VerificationStatus;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/biddingAds")
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:3000/", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.PATCH})
public class BiddingAdvertController {
    private final BiddingAdvertUseCases biddingAdvertUseCases;

    @PostMapping()
    @IsAuthenticated
    @RolesAllowed({"Admin", "RegularUser"})
    public ResponseEntity<CreateBiddingAdvertResponse> createAdvert(@RequestBody @Valid CreateBiddingAdvertRequest request){
        CreateBiddingAdvertResponse response = biddingAdvertUseCases.createBiddingAdvert(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<GetAllBiddingAdvertsResponse> getAllBiddingAdvert(){
        GetAllBiddingAdvertsResponse response = biddingAdvertUseCases.getAll();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/filter")
    public ResponseEntity<GetAllBiddingAdvertsByFilterResponse> getAllBiddingAdvertsByFilter(
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) VerificationStatus verificationStatus,
            @RequestParam(required = false) AdStatus adStatus
    ) {
        GetAllBiddingAdvertsByFilterResponse response = biddingAdvertUseCases.getAllFilteredBiddingAds(GetAllBiddingAdvertsByFilterRequest.builder().adStatus(adStatus).categoryId(categoryId).verificationStatus(verificationStatus).build());
        return ResponseEntity.ok(response);
    }
    @GetMapping("{id}")
    public ResponseEntity<GetBiddingAdvertResponse> getBiddingAdvertById(@PathVariable Long id){
        Optional<BiddingAdvert> advert = biddingAdvertUseCases.getBiddingAdvertDetails(id);

        if(advert.isPresent()){
            GetBiddingAdvertResponse response = GetBiddingAdvertResponse.builder().biddingAdvert(advert.get()).build();
            return ResponseEntity.ok().body(response);
        }

        return ResponseEntity.notFound().build();
    }
    @IsAuthenticated
    @RolesAllowed({"Admin"})
    @DeleteMapping("{id}")
    public ResponseEntity<DeleteBiddingAdvertResponse> deleteBiddingAdvertById(@PathVariable Long id){
        boolean deleted = biddingAdvertUseCases.deleteBiddingAdvert(id);
        if(deleted){
            return ResponseEntity.ok(DeleteBiddingAdvertResponse.builder().stringResponse("Bidding advert successfully deleted").build());
        }else{
            return ResponseEntity.notFound().build();
        }
    }
    @IsAuthenticated
    @RolesAllowed({"RegularUser","Admin"})
    @PostMapping("/{adId}/buynow")
    public ResponseEntity<BuyNowBiddingAdvertResponse> buyBiddingAdvert(@PathVariable Long adId,
                                                                        @RequestBody BuyNowBiddingAdvertRequest request) {
        try {
            BuyNowBiddingAdvertResponse response = biddingAdvertUseCases.buyBiddingAdvert(adId, request);
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
    @RolesAllowed({"RegularUser", "Admin"})
    @GetMapping("/history/{id}")
    public ResponseEntity<GetPersonalBiddingAdsResponse> getPersonalBiddingAds(@PathVariable Long id) {
        GetPersonalBiddingAdsResponse response = biddingAdvertUseCases.getPersonalBiddingAdverts(GetPersonalBiddingAdsRequest.builder().id(id).build());
        return ResponseEntity.ok(response);
    }
    @IsAuthenticated
    @RolesAllowed({"RegularUser", "Admin"})
    @PostMapping("/{id}/bid")
    public ResponseEntity<BidResponse> bidAdvert(@PathVariable Long id, @RequestBody BidRequest request) {
        try {
            BidResponse response = biddingAdvertUseCases.bidAdvert(id, request);
            return ResponseEntity.ok(response);
        } catch (AdvertNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (BidException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
    @IsAuthenticated
    @RolesAllowed({"Admin"})
    @PatchMapping("/{id}/status")
    public ResponseEntity<SetAdvertStatusResponse> setAdStatus(@PathVariable Long id, @RequestBody SetAdvertStatusRequest request) {
        request.setId(id);
        SetAdvertStatusResponse response = biddingAdvertUseCases.setAdStatus(request);
        return ResponseEntity.ok(response);
    }
}
