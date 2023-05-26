package teamwork.chatbottelegrem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import teamwork.chatbottelegrem.model.CatUsers;

import java.util.Set;
/**
 * Репозиторий класса владельца кота
 */
@Repository
public interface CatUsersRepository extends JpaRepository<CatUsers, Long> {
    Set<CatUsers> findByChatId(Long chatId);

}
