package fontys.individual.school.controller;

import fontys.individual.school.business.Interface.HomeDeliveryUseCases;
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
@RequestMapping("/homedeliveries")
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.PATCH})
public class HomeDeliveryController {
    private final HomeDeliveryUseCases homeDeliveryUseCases;

    @GetMapping("/available")
    public ResponseEntity<GetAvailableTimeSlotsResponse> availableTimeSlots(){
        return ResponseEntity.ok(homeDeliveryUseCases.getAvailableTimeSlots());
    }
    @IsAuthenticated
    @RolesAllowed({"Admin", "RegularUser"})
    @PostMapping("")
    public ResponseEntity<RegisterTimeSlotResponse> registerTimeSlot(@RequestBody @Valid RegisterHomeDeliveryTimeSlotRequest request){
        return ResponseEntity.ok(homeDeliveryUseCases.registerTimeSlot(request));
    }
    @IsAuthenticated
    @RolesAllowed({"Admin"})
    @GetMapping("/count")
    public ResponseEntity<GetCountOfHomeDeliveryFromAPeriodOfTimeResponse> getCountOfHomeDeliveryFromAPeriodOfTime(
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        GetCountOfHomeDeliveryFromAPeriodOfTimeRequest request = new GetCountOfHomeDeliveryFromAPeriodOfTimeRequest(startDate, endDate);
        GetCountOfHomeDeliveryFromAPeriodOfTimeResponse response = homeDeliveryUseCases.countOfHomeDelivery(request);
        return ResponseEntity.ok(response);
    }

    @IsAuthenticated
    @RolesAllowed({"Admin"})
    @GetMapping("/all")
    public ResponseEntity<GetAllHomeDeliveriesResponse> getAllHomeDeliveries() {
        GetAllHomeDeliveriesResponse response = homeDeliveryUseCases.getAll();
        return ResponseEntity.ok(response);
    }
    @IsAuthenticated
    @RolesAllowed({"Admin"})
    @PutMapping("/{id}/status")
    public ResponseEntity<UpdateHomeDeliveryResponse> updateDeliveryStatus(@PathVariable Long id, @RequestBody UpdateHomeDeliveryRequest request) {
        request.setId(id);
        UpdateHomeDeliveryResponse response = homeDeliveryUseCases.updateDeliveryStatus(request);
        return ResponseEntity.ok(response);
    }
}
