package fontys.individual.school.controller;

import fontys.individual.school.business.Interface.MessageUseCases;
import fontys.individual.school.domain.HttpResponseRequest.PrivateMessageRequest;
import lombok.AllArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Controller
@AllArgsConstructor
public class WebSocketController {
    private final MessageUseCases messageUseCases;


    @MessageMapping("/user/{userId}/queue/inboxmessages")
    public void handlePrivateWebSocketMessage(PrivateMessageRequest message, @DestinationVariable Long userId) {
        messageUseCases.saveMessage(message);
    }
}
