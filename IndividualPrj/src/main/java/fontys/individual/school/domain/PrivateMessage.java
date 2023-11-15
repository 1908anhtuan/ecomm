package fontys.individual.school.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PrivateMessage {

    private Long id;
    private Account sender;
    private Account receiver;
    private String content;
    @Builder.Default private LocalDateTime timestamp = LocalDateTime.now();
}
