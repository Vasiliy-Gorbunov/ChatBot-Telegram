package teamwork.chatbottelegrem.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import teamwork.chatbottelegrem.model.Dog;
import teamwork.chatbottelegrem.model.DogUsers;
import teamwork.chatbottelegrem.service.DogUsersService;

import java.util.ArrayList;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class DogUsersControllerTest {
    DogUsers USER_EXAMPLE = new DogUsers(123123L, "ФИО", 1992, "9171122335", 131313L,
            new Dog(123123123L, "Шарик", "Дворняга", 2020, "Информация"));
    @Mock
    DogUsersService service;
    @InjectMocks
    DogUsersController controller;
    @BeforeEach
    public void initMock(){
        MockitoAnnotations.initMocks(this);
    }
    @Test
    void getById() {
        USER_EXAMPLE.setId(123321L);
        when(service.getById(USER_EXAMPLE.getId())).thenReturn(USER_EXAMPLE);
        assertEquals(USER_EXAMPLE, controller.getById(USER_EXAMPLE.getId()));
    }

    @Test
    void save() {
        controller.save(USER_EXAMPLE);
        verify(service).create(USER_EXAMPLE);

    }

    @Test
    void update() {
        controller.update(USER_EXAMPLE);
        verify(service).update(USER_EXAMPLE);

    }

    @Test
    void remove() {
        USER_EXAMPLE.setId(123321L);
        controller.remove(USER_EXAMPLE.getId());
        verify(service).removeById(USER_EXAMPLE.getId());
    }

    @Test
    void getAll() {
        Collection<DogUsers> catUsers = new ArrayList<>();
        catUsers.add(USER_EXAMPLE);
        when(service.getByChatId(USER_EXAMPLE.getChatId())).thenReturn(catUsers);
        assertEquals(catUsers, controller.getAll(USER_EXAMPLE.getChatId()));
        controller.getAll(null);
        verify(service).getAll();

    }

}