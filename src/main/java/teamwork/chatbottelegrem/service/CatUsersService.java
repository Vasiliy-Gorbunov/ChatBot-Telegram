package teamwork.chatbottelegrem.service;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import teamwork.chatbottelegrem.Model.CatUsers;
import teamwork.chatbottelegrem.exception.CatUserNotFoundException;
import teamwork.chatbottelegrem.repository.CatUsersRepository;

import java.util.Collection;
/**
 * Сервис класса пользователя кота
 */
@Service
public class CatUsersService {
    private final CatUsersRepository repository;
    private static final Logger logger = LoggerFactory.getLogger(CatUsersRepository.class);
    public CatUsersService(CatUsersRepository repository) {
        this.repository = repository;
    }
    public CatUsers getById(Long id) {
        return this.repository.findById(id)
                .orElseThrow(CatUserNotFoundException::new);
    }
    /**
     * Создание нового пользователя
     */
    public CatUsers create(CatUsers catUsers) {
        return this.repository.save(catUsers);
    }
    /**
     * обновление данных пользователя
     */
    public CatUsers update(CatUsers catUsers) {
        if (catUsers.getChatId() != null && getById(catUsers.getChatId()) != null) {
            return repository.save(catUsers);
        }
        throw new CatUserNotFoundException();
    }
    /**
     * Удаление данных о пользователе
     */
    public void removeById(Long id) {
        this.repository.deleteById(id);
    }
    /**
     * Выдать всех пользователей
     */
    public Collection<CatUsers> getAll() {
        return this.repository.findAll();
    }
    /**
     * Выдать одного пользователя по номеру
     */
    public Collection<CatUsers> getByChatId(Long chatId) {
        return this.repository.findByChatId(chatId);
    }
}