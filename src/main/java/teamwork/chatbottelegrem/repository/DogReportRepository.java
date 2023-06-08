package teamwork.chatbottelegrem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import teamwork.chatbottelegrem.model.DogReport;

import java.util.List;


public interface DogReportRepository extends JpaRepository<DogReport, Long> {

    List<DogReport> findAllByChatId(Long chatId);
}
