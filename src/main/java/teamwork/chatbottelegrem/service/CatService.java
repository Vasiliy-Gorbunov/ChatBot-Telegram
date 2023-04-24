package teamwork.chatbottelegrem.service;

import org.springframework.stereotype.Service;
import teamwork.chatbottelegrem.Model.Cat;
import teamwork.chatbottelegrem.exception.CatNotFoundException;
import teamwork.chatbottelegrem.repository.CatRepository;
/**
 * Сервис класса кота
 */
@Service
public class CatService {
    private final CatRepository repository;

    public CatService(CatRepository repository) {
        this.repository = repository;
    }

    /**
     * Добавление нового кота в список
      */
    public Cat addCat(Cat cat) {
        return this.repository.save(cat);
    }

    /**
    * получение кота из списка
    */
    public Cat getById(Long id) {
        return this.repository.findById(id).orElseThrow(CatNotFoundException::new);
    }
    /**
     * Обновление кота в списке
     */

    public Cat update(Cat cat) {
        if (cat.getId() != null && getById(cat.getId()) != null) {
            return repository.save(cat);
        }
        throw new CatNotFoundException();
    }
    /**
     * Удаление кота из списка
     */

    public void removeById(Long id) {
        this.repository.deleteById(id);
    }
}
