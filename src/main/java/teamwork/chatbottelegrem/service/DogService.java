package teamwork.chatbottelegrem.service;

import org.springframework.stereotype.Service;
import teamwork.chatbottelegrem.Model.Dog;
import teamwork.chatbottelegrem.exception.DogNotFoundException;
import teamwork.chatbottelegrem.repository.DogRepository;

@Service
public class DogService {
    private final DogRepository repository;

    public DogService(DogRepository repository) {
        this.repository = repository;
    }

    public Dog getById(Long id) {
        return repository.findById(id).orElseThrow(DogNotFoundException::new);
    }

    public Dog addDog(Dog dog) {
        return repository.save(dog);
    }

    public Dog update(Dog dog) {
        if (dog.getId() != null) {
            if (getById(dog.getId()) != null) {
                return repository.save(dog);
            }
        }
        throw new DogNotFoundException();
    }

    public void removeById(Long id) {
        repository.deleteById(id);
    }
}