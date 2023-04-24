package teamwork.chatbottelegrem.service;

import org.springframework.stereotype.Service;
import teamwork.chatbottelegrem.Model.DogUsers;
import teamwork.chatbottelegrem.exception.DogUserNotFoundException;
import teamwork.chatbottelegrem.repository.DogUsersRepository;
/**
 * Сервис класса владельца собаки
 */
@Service
public class DogUsersService {
    private final DogUsersRepository dogUsersRepository;

    public DogUsersService(DogUsersRepository dogUsersRepository) {
        this.dogUsersRepository = dogUsersRepository;
    }
    /**
     * Создание нового пользователя
     */
    public DogUsers save(DogUsers dogUsers) {
        dogUsersRepository.save(dogUsers);
        return dogUsers;
    }

    public DogUsers getById(Long id) {
        return dogUsersRepository.findById(id).orElseThrow(DogUserNotFoundException::new);
    }
    /**
     * Удаление данных о пользователе
     */
    public void delete(Long id) {
        dogUsersRepository.deleteById(id);
    }
    /**
     * Выдать одного пользователя по номеру
     */
    public DogUsers getByChatId(Long chatId) {
        return dogUsersRepository.getDogOwnerByChatId(chatId);
    }
}