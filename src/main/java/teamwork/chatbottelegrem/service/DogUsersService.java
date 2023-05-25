package teamwork.chatbottelegrem.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import teamwork.chatbottelegrem.Model.CatUsers;
import teamwork.chatbottelegrem.Model.DogUsers;
import teamwork.chatbottelegrem.exception.CatUserNotFoundException;
import teamwork.chatbottelegrem.exception.DogUserNotFoundException;
import teamwork.chatbottelegrem.repository.CatUsersRepository;
import teamwork.chatbottelegrem.repository.DogUsersRepository;

import java.util.Collection;

/**
 * Сервис класса пользователя собаки
 */
@Service
public class DogUsersService {
    private final DogUsersRepository repository;
    private static final Logger logger = LoggerFactory.getLogger(CatUsersRepository.class);
    public DogUsersService(DogUsersRepository repository) {
        this.repository = repository;
    }
    public DogUsers getById(Long id) {
        return this.repository.findById(id)
                .orElseThrow(DogUserNotFoundException::new);
    }
    /**
     * Создание нового пользователя
     */
    public DogUsers create(DogUsers dogUsers) {
        return this.repository.save(dogUsers);
    }
    /**
     * обновление данных пользователя
     */
    public DogUsers update(DogUsers dogUsers) {
        if (dogUsers.getId() != null && getById(dogUsers.getId()) != null) {
            return repository.save(dogUsers);
        }
        throw new DogUserNotFoundException();
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
    public Collection<DogUsers> getAll() {
        return this.repository.findAll();
    }
    /**
     * Выдать одного пользователя по номеру
     */
    public Collection<DogUsers> getByChatId(Long chatId) {
        return this.repository.findByChatId(chatId);
    }
}