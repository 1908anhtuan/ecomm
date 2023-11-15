package fontys.individual.school.controller;

import fontys.individual.school.business.Interface.PickUpDeliveryUseCases;
import fontys.individual.school.configuration.security.isauthenticated.IsAuthenticated;
import fontys.individual.school.domain.HttpResponseRequest.*;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import java.time.LocalDate;

@RestController
@RequestMapping("/pickupdeliveries")
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.PATCH})
public class PickUpDeliveryController {
    private final PickUpDeliveryUseCases pickUpDeliveryUseCases;

    @PostMapping("/available")
    public ResponseEntity<GetAvailableTimeSlotsResponse> availableTimeSlots(@RequestBody @Valid GetAvailableTimeSlotsRequest request){
        return ResponseEntity.ok(pickUpDeliveryUseCases.getAvailableTimeSlots(request));
    }
    @IsAuthenticated
    @RolesAllowed({"Admin", "RegularUser"})
    @PostMapping("")
    public ResponseEntity<RegisterTimeSlotResponse> registerTimeSlot(@RequestBody @Valid RegisterTimeSlotRequest request){
        return ResponseEntity.ok(pickUpDeliveryUseCases.registerTimeSlot(request));
    }
    @IsAuthenticated
    @RolesAllowed({"Admin"})
    @GetMapping("/count")
    public ResponseEntity<GetCountOfPickUpDeliveryFromAPeriodOfTimeResponse> getCountOfPickUpDeliveryFromAPeriodOfTime(
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        GetCountOfPickUpDeliveryFromAPeriodOfTimeRequest request = new GetCountOfPickUpDeliveryFromAPeriodOfTimeRequest(startDate, endDate);
        GetCountOfPickUpDeliveryFromAPeriodOfTimeResponse response = pickUpDeliveryUseCases.countOfPickUpDelivery(request);
        return ResponseEntity.ok(response);
    }
    @IsAuthenticated
    @RolesAllowed({"Admin"})
    @GetMapping("/all")
    public ResponseEntity<GetAllPickUpDeliveriesResponse> getAllPickUpDeliveries() {
        GetAllPickUpDeliveriesResponse response = pickUpDeliveryUseCases.getAll();
        return ResponseEntity.ok(response);
    }
    @IsAuthenticated
    @RolesAllowed({"Admin"})
    @PutMapping("/{id}/status")
    public ResponseEntity<UpdatePickUpDeliveryResponse> updateDeliveryStatus(@PathVariable Long id, @RequestBody UpdatePickUpDeliveryRequest request) {
        request.setId(id);
        UpdatePickUpDeliveryResponse response = pickUpDeliveryUseCases.updateDeliveryStatus(request);
        return ResponseEntity.ok(response);
    }
}
