package fontys.individual.school.business.impl;

import fontys.individual.school.business.Interface.MessageUseCases;
import fontys.individual.school.business.impl.Converter.AccountConverter;
import fontys.individual.school.business.impl.Converter.MessageConverter;
import fontys.individual.school.domain.HttpResponseRequest.GetMessagedAccountsResponse;
import fontys.individual.school.domain.HttpResponseRequest.PrivateMessageRequest;
import fontys.individual.school.domain.PrivateMessage;
import fontys.individual.school.persistence.AccountRepository;
import fontys.individual.school.persistence.PrivateMessageRepository;
import fontys.individual.school.persistence.entity.AccountEntity;
import fontys.individual.school.persistence.entity.PrivateMessageEntity;
import lombok.AllArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class MessageServiceImpl implements MessageUseCases {
    private final PrivateMessageRepository privateMessageRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final AccountRepository accountRepository;

    @Override
    public PrivateMessage saveMessage(PrivateMessageRequest message) {
        AccountEntity sender = accountRepository.getReferenceById(message.getSender());
        AccountEntity receiver = accountRepository.getReferenceById(message.getReceiver());
        PrivateMessageEntity entity = PrivateMessageEntity.builder().sender(sender).receiver(receiver).content(message.getContent()).build();
        PrivateMessage savedMessage = MessageConverter.toModel(privateMessageRepository.save(entity));
        return savedMessage;
    }

    @Override
    public List<PrivateMessage> getMessagesByUsers(Long senderId, Long receiverId) {
        return privateMessageRepository.findBySenderIdAndReceiverIdOrReceiverIdAndSenderIdOrderByTimestampAsc(senderId, receiverId, senderId, receiverId)
                .stream()
                .map(MessageConverter::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public GetMessagedAccountsResponse getMessagedAccounts(Long userId) {
        List<PrivateMessageEntity> privateMessages = privateMessageRepository.findBySenderIdOrReceiverId(userId, userId);

        Set<AccountEntity> accountSet = new HashSet<>();
        for (PrivateMessageEntity privateMessage : privateMessages) {
            if (privateMessage.getSender().getId().equals(userId)) {
                accountSet.add(privateMessage.getReceiver());
            } else {
                accountSet.add(privateMessage.getSender());
            }
        }
        List<AccountEntity> accounts = new ArrayList<>(accountSet);
        return GetMessagedAccountsResponse.builder()
                .accounts(accounts.stream().map(AccountConverter::convertToAccount).toList())
                .build();
    }
}
