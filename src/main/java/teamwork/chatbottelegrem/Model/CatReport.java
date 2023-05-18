package teamwork.chatbottelegrem.Model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@RequiredArgsConstructor
@NoArgsConstructor
@Data
@Table(name="cat_report")
public class CatReport  {
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
