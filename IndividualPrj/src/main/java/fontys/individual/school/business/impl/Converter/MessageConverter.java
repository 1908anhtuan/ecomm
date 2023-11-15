package fontys.individual.school.business.impl.Converter;

import fontys.individual.school.domain.PrivateMessage;
import fontys.individual.school.persistence.entity.PrivateMessageEntity;

public class MessageConverter {

    public static PrivateMessageEntity toEntity(PrivateMessage message) {
        PrivateMessageEntity entity = new PrivateMessageEntity();
        entity.setId(message.getId());
        entity.setSender(AccountConverter.convertToAccountEntity(message.getSender()));
        entity.setReceiver(AccountConverter.convertToAccountEntity(message.getReceiver()));
        entity.setContent(message.getContent());
        entity.setTimestamp(message.getTimestamp());
        return entity;
    }

    public static PrivateMessage toModel(PrivateMessageEntity entity) {
        PrivateMessage message = new PrivateMessage();
        message.setId(entity.getId());
        message.setSender(AccountConverter.convertToAccount(entity.getSender()));
        message.setReceiver(AccountConverter.convertToAccount(entity.getReceiver()));
        message.setContent(entity.getContent());
        message.setTimestamp(entity.getTimestamp());
        return message;
    }
}