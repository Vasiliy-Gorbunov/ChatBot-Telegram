//package teamwork.chatbottelegrem.service;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import teamwork.chatbottelegrem.Model.Dog;
//import teamwork.chatbottelegrem.Model.DogUsers;
//import teamwork.chatbottelegrem.exception.DogUserNotFoundException;
//import teamwork.chatbottelegrem.repository.DogUsersRepository;
//
//import java.util.*;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//class DogUsersServiceTest {
//    DogUsers user = new DogUsers(123123L, "ФИО", 1992, "9171122335",
//            new Dog(123123123L, "Шарик", "Дворняга", 2020, "Информация"));
//    @Mock
//    DogUsersRepository repository;
//    @InjectMocks
//    DogUsersService service;
//
//    @BeforeEach
//    public void iniMocks() {
//        MockitoAnnotations.initMocks(this);
//    }
//
//
//    @Test
//    void getById() {
//        when(repository.findById(user.getId())).thenReturn(Optional.ofNullable(user));
//        assertEquals(user, service.getById(user.getId()));
//    }
//
//    @Test
//    void create() {
//        service.create(user);
//        verify(repository).save(user);
//    }
//
//    @Test
//    void update() {
//        when(repository.findById(user.getId())).thenReturn(Optional.ofNullable(user));
//        service.update(user);
//        verify(repository).save(user);
//        user.setId(135487L);
//        assertThrows(DogUserNotFoundException.class, () -> {
//            service.update(user);
//        }, "Владелец собаки не найден");
//        user.setId(null);
//        assertThrows(DogUserNotFoundException.class, () -> {
//            service.update(user);
//        }, "Владелец собаки не найден");
//
//    }
//
//    @Test
//    void removeById() {
//        service.removeById(user.getId());
//        verify(repository).deleteById(user.getId());
//    }
//
//    @Test
//    void getAll() {
//        List<DogUsers> users = new ArrayList<>();
//        users.add(user);
//        when(repository.findAll()).thenReturn(users);
//        assertEquals(users, service.getAll());
//
//    }
//
//    @Test
//    void getByChatId() {
//        user.setChatId(131313L);
//        Set<DogUsers> users = new HashSet<>();
//        users.add(user);
//        when(repository.findByChatId(131313L)).thenReturn(users);
//        assertEquals(users, service.getByChatId(131313L));
//    }
//}