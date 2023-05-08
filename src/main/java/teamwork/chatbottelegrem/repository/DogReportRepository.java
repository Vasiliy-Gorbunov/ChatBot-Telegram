package teamwork.chatbottelegrem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import teamwork.chatbottelegrem.Model.DogReport;

import java.time.LocalDate;
import java.util.Date;


public interface DogReportRepository extends JpaRepository<DogReport, Long> {
    DogReport  findByDate (LocalDate date);
}
