package teamwork.chatbottelegrem.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@RequiredArgsConstructor
@NoArgsConstructor
@Data
@Table(name="dog_report")
public class DogReport  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NonNull
    private long id;
    @Column (name = "chat_id", nullable = false)
    @NonNull
    private long chatId;

    @Column (name = "text_report")
    private String textReport;

    @Column (name = "file_id")
    private String fileId;

    @Column (name= "date", nullable = false)
    @NonNull
    private LocalDate date;

}
