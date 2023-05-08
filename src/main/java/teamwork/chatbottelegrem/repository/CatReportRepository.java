package teamwork.chatbottelegrem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import teamwork.chatbottelegrem.Model.CatReport;

import java.time.LocalDate;



public interface CatReportRepository extends JpaRepository<CatReport, Long> {
    CatReport findByDate (LocalDate dateTime);
}
 