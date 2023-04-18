package teamwork.chatbottelegrem.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import teamwork.chatbottelegrem.entity.ShelterEntity;

@Repository
public interface TelegramBotRepository extends CrudRepository<ShelterEntity, Long> {

    ShelterEntity findByButtonName(String buttonName);
}
