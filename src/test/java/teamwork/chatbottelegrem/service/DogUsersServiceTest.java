package teamwork.chatbottelegrem.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import teamwork.chatbottelegrem.model.Dog;
import teamwork.chatbottelegrem.model.DogUsers;
import teamwork.chatbottelegrem.exception.DogUserNotFoundException;
import teamwork.chatbottelegrem.repository.DogUsersRepository;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class DogUsersServiceTest {
    DogUsers USER_EXAMPLE = new DogUsers(123123L, "ФИО", 1992, "9171122335", 131313L,
            new Dog(123123123L, "Шарик", "Дворняга", 2020, "Информация"));
    @Mock
    DogUsersRepository repository;
    @InjectMocks
    DogUsersService service;

    @BeforeEach
    public void iniMocks() {
        MockitoAnnotations.initMocks(this);
    }


    @Test
    void getById() {
        when(repository.findById(USER_EXAMPLE.getId())).thenReturn(Optional.ofNullable(USER_EXAMPLE));
        assertEquals(USER_EXAMPLE, service.getById(USER_EXAMPLE.getId()));
        USER_EXAMPLE.setId(135487L);
        assertThrows(DogUserNotFoundException.class, () -> {
            service.getById(USER_EXAMPLE.getId());
        }, "Владелец собаки не найден");
    }

    @Test
    void create() {
        service.create(USER_EXAMPLE);
        verify(repository).save(USER_EXAMPLE);
    }

    @Test
    void update() {
        when(repository.findById(USER_EXAMPLE.getId())).thenReturn(Optional.ofNullable(USER_EXAMPLE));
        service.update(USER_EXAMPLE);
        verify(repository).save(USER_EXAMPLE);
        USER_EXAMPLE.setId(135487L);
        assertThrows(DogUserNotFoundException.class, () -> {
            service.update(USER_EXAMPLE);
        }, "Владелец собаки не найден");
        USER_EXAMPLE.setId(null);
        assertThrows(DogUserNotFoundException.class, () -> {
            service.update(USER_EXAMPLE);
        }, "Владелец собаки не найден");

    }

    @Test
    void removeById() {
        service.removeById(USER_EXAMPLE.getId());
        verify(repository).deleteById(USER_EXAMPLE.getId());
    }

    @Test
    void getAll() {
        List<DogUsers> users = new ArrayList<>();
        users.add(USER_EXAMPLE);
        when(repository.findAll()).thenReturn(users);
        assertEquals(users, service.getAll());

    }

    @Test
    void getByChatId() {
        Set<DogUsers> users = new HashSet<>();
        users.add(USER_EXAMPLE);
        when(repository.findByChatId(131313L)).thenReturn(users);
        assertEquals(users, service.getByChatId(131313L));
    }
}