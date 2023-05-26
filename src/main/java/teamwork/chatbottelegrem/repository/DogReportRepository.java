package teamwork.chatbottelegrem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import teamwork.chatbottelegrem.model.DogReport;

import java.time.LocalDate;


public interface DogReportRepository extends JpaRepository<DogReport, Long> {
    DogReport  findByDate (LocalDate date);
}
