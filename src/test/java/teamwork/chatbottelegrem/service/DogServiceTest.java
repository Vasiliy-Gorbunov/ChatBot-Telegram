package teamwork.chatbottelegrem.service;

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
    Dog DOG_EXAPMLE = new Dog(123123L, "Шарик", "Дворняга", 2022, "информация");
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
        service.addDog(DOG_EXAPMLE);
        verify(repository).save(DOG_EXAPMLE);
    }

    @Test
    void getById() {
        when(repository.findById(DOG_EXAPMLE.getId())).thenReturn(Optional.ofNullable(DOG_EXAPMLE));
        assertEquals(DOG_EXAPMLE, service.getById(DOG_EXAPMLE.getId()));
        Long numberNeverUsed = 43214321L;
        assertThrows(DogNotFoundException.class, () -> {
            service.getById(numberNeverUsed);
        }, "Собака не найдена");
    }

    @Test
    void update() {

        assertThrows(DogNotFoundException.class, () -> {
            service.update(DOG_EXAPMLE);
        }, "Собака не найдена");
        DOG_EXAPMLE.setId(null);
        assertThrows(DogNotFoundException.class, () -> {
            service.update(DOG_EXAPMLE);
        }, "Собака не найдена");
        DOG_EXAPMLE.setId(123123L);
        when(repository.findById(DOG_EXAPMLE.getId())).thenReturn(Optional.ofNullable(DOG_EXAPMLE));
        service.update(DOG_EXAPMLE);
        verify(repository).save(DOG_EXAPMLE);

    }

    @Test
    void removeById() {
        service.removeById(DOG_EXAPMLE.getId());
        verify(repository).deleteById(DOG_EXAPMLE.getId());
        
    }
}