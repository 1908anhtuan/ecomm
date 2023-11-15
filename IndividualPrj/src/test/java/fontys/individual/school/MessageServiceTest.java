package fontys.individual.school;
import fontys.individual.school.business.impl.MessageServiceImpl;
import fontys.individual.school.domain.HttpResponseRequest.GetMessagedAccountsResponse;
import fontys.individual.school.domain.HttpResponseRequest.PrivateMessageRequest;
import fontys.individual.school.domain.PrivateMessage;
import fontys.individual.school.persistence.AccountRepository;
import fontys.individual.school.persistence.PrivateMessageRepository;
import fontys.individual.school.persistence.entity.AccountEntity;
import fontys.individual.school.persistence.entity.PrivateMessageEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
public class MessageServiceTest {
    @Mock
    private PrivateMessageRepository privateMessageRepository;

    @Mock
    private SimpMessagingTemplate messagingTemplate;

    @Mock
    private AccountRepository accountRepository;

    private MessageServiceImpl messageService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        messageService = new MessageServiceImpl(privateMessageRepository, messagingTemplate, accountRepository);
    }

    @Test
    void saveMessage_ValidRequest_ReturnsSavedPrivateMessage() {
        // Arrange
        PrivateMessageRequest messageRequest = new PrivateMessageRequest();
        messageRequest.setSender(1L);
        messageRequest.setReceiver(2L);
        messageRequest.setContent("Hello");

        AccountEntity sender = new AccountEntity();
        sender.setId(1L);

        AccountEntity receiver = new AccountEntity();
        receiver.setId(2L);

        PrivateMessageEntity savedEntity = new PrivateMessageEntity();
        savedEntity.setId(1L);
        savedEntity.setSender(sender);
        savedEntity.setReceiver(receiver);
        savedEntity.setContent(messageRequest.getContent());

        when(accountRepository.getReferenceById(1L)).thenReturn(sender);
        when(accountRepository.getReferenceById(2L)).thenReturn(receiver);
        when(privateMessageRepository.save(any())).thenReturn(savedEntity);

        // Act
        PrivateMessage savedMessage = messageService.saveMessage(messageRequest);

        // Assert
        assertEquals(savedEntity.getId(), savedMessage.getId());
        assertEquals(sender.getId(), savedMessage.getSender().getId());
        assertEquals(receiver.getId(), savedMessage.getReceiver().getId());
        assertEquals(messageRequest.getContent(), savedMessage.getContent());
        verify(accountRepository, times(2)).getReferenceById(any());
        verify(privateMessageRepository).save(any());
    }

    @Test
    void getMessagesByUsers_ValidUsers_ReturnsListOfPrivateMessages() {
        // Arrange
        Long senderId = 1L;
        Long receiverId = 2L;

        PrivateMessageEntity messageEntity1 = new PrivateMessageEntity();
        messageEntity1.setId(1L);
        messageEntity1.setContent("Message 1");

        PrivateMessageEntity messageEntity2 = new PrivateMessageEntity();
        messageEntity2.setId(2L);
        messageEntity2.setContent("Message 2");

        List<PrivateMessageEntity> privateMessageEntities = new ArrayList<>();
        privateMessageEntities.add(messageEntity1);
        privateMessageEntities.add(messageEntity2);

        when(privateMessageRepository.findBySenderIdAndReceiverIdOrReceiverIdAndSenderIdOrderByTimestampAsc(
                senderId, receiverId, senderId, receiverId))
                .thenReturn(privateMessageEntities);

        // Act
        List<PrivateMessage> privateMessages = messageService.getMessagesByUsers(senderId, receiverId);

        // Assert
        assertEquals(privateMessageEntities.size(), privateMessages.size());
        for (int i = 0; i < privateMessageEntities.size(); i++) {
            PrivateMessageEntity messageEntity = privateMessageEntities.get(i);
            PrivateMessage privateMessage = privateMessages.get(i);
            assertEquals(messageEntity.getId(), privateMessage.getId());
            assertEquals(messageEntity.getContent(), privateMessage.getContent());
        }
        verify(privateMessageRepository).findBySenderIdAndReceiverIdOrReceiverIdAndSenderIdOrderByTimestampAsc(
                senderId, receiverId, senderId, receiverId);
    }

    @Test
    void getMessagedAccounts_ValidUserId_ReturnsGetMessagedAccountsResponse() {
        // Arrange
        Long userId = 2L;

        AccountEntity account1 = new AccountEntity();
        account1.setId(2L);

        AccountEntity account2 = new AccountEntity();
        account2.setId(3L);

        List<PrivateMessageEntity> privateMessages = new ArrayList<>();
        PrivateMessageEntity privateMessage1 = new PrivateMessageEntity();
        privateMessage1.setId(1L);
        privateMessage1.setSender(account1);
        privateMessage1.setReceiver(account2);
        privateMessages.add(privateMessage1);

        when(privateMessageRepository.findBySenderIdOrReceiverId(userId, userId)).thenReturn(privateMessages);

        // Act
        GetMessagedAccountsResponse response = messageService.getMessagedAccounts(userId);

        // Assert
        assertEquals(1, response.getAccounts().size());
        verify(privateMessageRepository).findBySenderIdOrReceiverId(userId, userId);
    }
}
