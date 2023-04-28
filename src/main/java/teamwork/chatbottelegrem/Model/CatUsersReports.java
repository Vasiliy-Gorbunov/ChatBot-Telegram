package teamwork.chatbottelegrem.Model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "cat_users_reports")
public class CatUsersReports {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @JsonBackReference
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_id", nullable = false)
    private CatUsers catUsers;

    @Column(name = "report_date_time", nullable = false)
    private LocalDateTime dateTime;

    @Column(name = "report_text", nullable = false)
    private String reportText;

    @Column(name = "photo_path", nullable = false)
    private String reportPath;

    public CatUsersReports(CatUsers catUsers, LocalDateTime dateTime, String reportText, String reportPath) {
        this.catUsers = catUsers;
        this.dateTime = dateTime;
        this.reportText = reportText;
        this.reportPath = reportPath;
    }
}

