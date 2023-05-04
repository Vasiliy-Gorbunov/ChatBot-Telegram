package teamwork.chatbottelegrem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import teamwork.chatbottelegrem.Model.CatUsersReports;

@Repository
public interface CatUsersReportsRepository extends JpaRepository<CatUsersReports, Long> {
}
