package fontys.individual.school.controller;
import fontys.individual.school.business.Interface.MessageUseCases;
import fontys.individual.school.configuration.security.isauthenticated.IsAuthenticated;
import fontys.individual.school.domain.HttpResponseRequest.GetMessagedAccountsResponse;
import fontys.individual.school.domain.PrivateMessage;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.CrossOrigin;

import javax.annotation.security.RolesAllowed;
import java.util.List;

@RestController
@RequestMapping("/messages")
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.PATCH}, allowCredentials = "true")
public class MessageController {
    private final MessageUseCases messageUseCases;
    @IsAuthenticated
    @RolesAllowed({"Admin", "RegularUser"})
    @GetMapping("/{senderId}/{receiverId}")
    public List<PrivateMessage> getPreviousMessages(
            @PathVariable Long senderId,
            @PathVariable Long receiverId
    ) {
        return messageUseCases.getMessagesByUsers(senderId, receiverId);
    }
    @GetMapping("/{userId}")
    public ResponseEntity<GetMessagedAccountsResponse> getMessagedAccounts(
            @PathVariable Long userId
    ) {
        return ResponseEntity.ok(messageUseCases.getMessagedAccounts(userId));
    }

}