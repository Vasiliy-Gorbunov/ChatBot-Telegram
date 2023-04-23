package teamwork.chatbottelegrem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import teamwork.chatbottelegrem.Model.Cat;
@Repository
public interface CatRepository extends JpaRepository<Cat, Long>  {
}
