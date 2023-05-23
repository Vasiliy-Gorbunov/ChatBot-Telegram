package teamwork.chatbottelegrem.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import java.util.Date;
/**
 * Класс контроля отправки уведомлений
 */
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ReportMessage {
    @Id
    @GeneratedValue
    private long id;
    private Long chatId;
    private String name;
    private String ration;
    private String health;
    private String behaviour;
    private String filePath;
    private Date lastMessage;
    @Lob
    private byte[] data;

    public ReportMessage(Long chatId, Date lastMessage) {
        this.chatId = chatId;
        this.lastMessage = lastMessage;
    }
}

