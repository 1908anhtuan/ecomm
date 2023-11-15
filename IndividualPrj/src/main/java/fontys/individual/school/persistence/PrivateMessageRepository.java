package fontys.individual.school.persistence;

import fontys.individual.school.persistence.entity.PrivateMessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrivateMessageRepository extends JpaRepository<PrivateMessageEntity, Long> {
    List<PrivateMessageEntity> findBySenderIdAndReceiverIdOrReceiverIdAndSenderIdOrderByTimestampAsc(Long senderId1, Long receiverId1, Long receiverId2, Long senderId2);


    List<PrivateMessageEntity> findBySenderIdOrReceiverId(Long senderId, Long receiverId);

}