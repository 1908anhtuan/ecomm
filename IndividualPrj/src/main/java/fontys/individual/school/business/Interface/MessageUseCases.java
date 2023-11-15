package fontys.individual.school.business.Interface;

import fontys.individual.school.domain.HttpResponseRequest.GetMessagedAccountsResponse;
import fontys.individual.school.domain.HttpResponseRequest.PrivateMessageRequest;
import fontys.individual.school.domain.PrivateMessage;

import java.util.List;

public interface MessageUseCases {
    PrivateMessage saveMessage(PrivateMessageRequest message);
    List<PrivateMessage> getMessagesByUsers(Long senderId, Long receiverId);
    GetMessagedAccountsResponse getMessagedAccounts(Long userId);
}
