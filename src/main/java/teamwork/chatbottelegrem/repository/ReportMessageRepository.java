package teamwork.chatbottelegrem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import teamwork.chatbottelegrem.Model.ReportMessage;

import java.util.Set;
/**
 * Репозиторий класса контроля отправки уведомлений
 */
public interface ReportMessageRepository extends JpaRepository<ReportMessage, Long> {
    Set<ReportMessage> findListByChatId(Long id);
    ReportMessage findByChatId(Long id);
}
