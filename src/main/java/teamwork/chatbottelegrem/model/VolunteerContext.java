package teamwork.chatbottelegrem.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@Table(name = "volunteer_context")
@NoArgsConstructor
@AllArgsConstructor
public class VolunteerContext {
    @Id
    @Column(name="volunteer_chat_id")
    private long volunteerChatId;
    @Column(name = "bad_report_notification")
    private boolean badReportNotification;
    @Column(name = "success_congratulations")
    private boolean successCongratulations;
    @Column(name = "additional_period_14")
    private boolean additionalPeriod14;
    @Column(name = "additional_period_30")
    private boolean additionalPeriod30;
    @Column(name = "adoption_refuse")
    private boolean adoptionRefuse;
}
