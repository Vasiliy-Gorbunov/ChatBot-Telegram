package teamwork.chatbottelegrem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import teamwork.chatbottelegrem.model.CatReport;

import java.util.List;


public interface CatReportRepository extends JpaRepository<CatReport, Long> {

    List<CatReport> findAllByChatId(Long chatId);
}