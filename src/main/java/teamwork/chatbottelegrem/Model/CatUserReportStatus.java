package teamwork.chatbottelegrem.Model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity
@Table(name = "cat_user_report_status")
public class CatUserReportStatus {

    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false)
    private Long id;

    @JsonBackReference
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_id", unique = true)
    private CatUsers catUsers;

    @Column(name = "report_sent")
    private boolean reportSent;


    public CatUserReportStatus(CatUsers catUsers, boolean reportSent) {
        this.catUsers = catUsers;
        this.reportSent = reportSent;
    }

    public CatUserReportStatus(Long id, CatUsers catUsers, boolean reportSent) {
        this.id = id;
        this.catUsers = catUsers;
        this.reportSent = reportSent;
    }
}
