package teamwork.chatbottelegrem.service;

import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import teamwork.chatbottelegrem.Model.Dog;
import teamwork.chatbottelegrem.exception.DogNotFoundException;
import teamwork.chatbottelegrem.repository.DogRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class DogServiceTest {
    Dog dog = new Dog(123123L, "Шарик", "Дворняга", 2022, "информация");
    @Mock
    DogRepository repository;
    @InjectMocks
    DogService service;


    @BeforeEach
    public void iniMocks() {
        MockitoAnnotations.initMocks(this);
    }


    @Test
    void addDog() {
        service.addDog(dog);
        verify(repository).save(dog);
    }

    @Test
    void getById() {
        when(repository.findById(dog.getId())).thenReturn(Optional.ofNullable(dog));
        assertEquals(dog, service.getById(dog.getId()));
        Long numberNeverUsed = 43214321L;
        assertThrows(DogNotFoundException.class, () -> {
            service.getById(numberNeverUsed);
        }, "Собака не найдена");
    }

    @Test
    void update() {

        assertThrows(DogNotFoundException.class, () -> {
            service.update(dog);
        }, "Собака не найдена");
        dog.setId(null);
        assertThrows(DogNotFoundException.class, () -> {
            service.update(dog);
        }, "Собака не найдена");
        dog.setId(123123L);
        when(repository.findById(dog.getId())).thenReturn(Optional.ofNullable(dog));
        service.update(dog);
        verify(repository).save(dog);

    }

    @Test
    void removeById() {
        service.removeById(dog.getId());
        verify(repository).deleteById(dog.getId());
        
    }
}