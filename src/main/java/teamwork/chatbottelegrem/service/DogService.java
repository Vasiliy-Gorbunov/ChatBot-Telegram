package teamwork.chatbottelegrem.service;

import org.springframework.stereotype.Service;
import teamwork.chatbottelegrem.Model.Dog;
import teamwork.chatbottelegrem.exception.DogNotFoundException;
import teamwork.chatbottelegrem.repository.DogRepository;
/**
 * Серивис класса собаки
 */
@Service
public class DogService {
    private final DogRepository repository;
    /**
     * Добавление новой собаки в список
     */
    public Dog addDog(Dog dog) {
        return repository.save(dog);
    }

    public DogService(DogRepository repository) {
        this.repository = repository;
    }
    /**
     * получение собаки из списка
     */
    public Dog getById(Long id) {
        return repository.findById(id).orElseThrow(DogNotFoundException::new);
    }

    /**
     * Обновление собаки в списке
     */

    public Dog update(Dog dog) {
        if (dog.getId() != null) {
            if (getById(dog.getId()) != null) {
                return repository.save(dog);
            }
        }
        throw new DogNotFoundException();
    }
    /**
     * Удаление собаки из списка
     */
    public void removeById(Long id) {
        repository.deleteById(id);
    }
}