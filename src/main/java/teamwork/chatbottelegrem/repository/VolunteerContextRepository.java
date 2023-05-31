package teamwork.chatbottelegrem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import teamwork.chatbottelegrem.model.VolunteerContext;
@Repository
public interface VolunteerContextRepository extends JpaRepository<VolunteerContext, Long> {
}
