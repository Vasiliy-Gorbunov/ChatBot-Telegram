package teamwork.chatbottelegrem.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import teamwork.chatbottelegrem.model.CatUsers;
import teamwork.chatbottelegrem.exception.CatUserNotFoundException;
import teamwork.chatbottelegrem.repository.CatUsersRepository;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CatUsersServiceTest {
    CatUsers USER_EXAMPLE = new CatUsers("ФИО", "9171122335", 123123L);
    @Mock
    CatUsersRepository repository;
    @InjectMocks
    CatUsersService service;
    @BeforeEach
    public void initMock(){
        MockitoAnnotations.initMocks(this);
    }



    @Test
    void getById() {
        USER_EXAMPLE.setId(123123123L);
        when(repository.findById(USER_EXAMPLE.getId())).thenReturn(Optional.ofNullable(USER_EXAMPLE));
        assertEquals(USER_EXAMPLE, service.getById(USER_EXAMPLE.getId()));
        USER_EXAMPLE.setId(12313L);
        assertThrows(CatUserNotFoundException.class, () -> {
            service.getById(USER_EXAMPLE.getId());
        }, "Владелец кота не найден");
    }

    @Test
    void create() {
        service.create(USER_EXAMPLE);
        verify(repository).save(USER_EXAMPLE);
    }

    @Test
    void update() {
        USER_EXAMPLE.setId(123123123L);
        when(repository.findById(USER_EXAMPLE.getId())).thenReturn(Optional.ofNullable(USER_EXAMPLE));
        service.update(USER_EXAMPLE);
        verify(repository).save(USER_EXAMPLE);
        USER_EXAMPLE.setId(135487L);
        assertThrows(CatUserNotFoundException.class, () -> {
            service.update(USER_EXAMPLE);
        }, "Владелец кота не найден");
        USER_EXAMPLE.setId(null);
        assertThrows(CatUserNotFoundException.class, () -> {
            service.update(USER_EXAMPLE);
        }, "Владелец кота не найден");

    }

    @Test
    void removeById() {
        service.removeById(USER_EXAMPLE.getId());
        verify(repository).deleteById(USER_EXAMPLE.getId());
    }

    @Test
    void getAll() {
        List<CatUsers> users = new ArrayList<>();
        users.add(USER_EXAMPLE);
        when(repository.findAll()).thenReturn(users);
        assertEquals(users, service.getAll());

    }

    @Test
    void getByChatId() {
        Set<CatUsers> users = new HashSet<>();
        users.add(USER_EXAMPLE);
        when(repository.findByChatId(USER_EXAMPLE.getChatId())).thenReturn(users);
        assertEquals(users, service.getByChatId(USER_EXAMPLE.getChatId()));
    }
}