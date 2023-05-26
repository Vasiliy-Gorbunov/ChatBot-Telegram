package teamwork.chatbottelegrem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import teamwork.chatbottelegrem.model.DogUsers;

import java.util.Set;

/**
 * Репозиторий класса владельца собаки
 */
@Repository
public interface DogUsersRepository extends JpaRepository<DogUsers, Long> {
    Set<DogUsers> findByChatId(Long chatId);
}
