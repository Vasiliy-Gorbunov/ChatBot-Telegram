package teamwork.chatbottelegrem.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import teamwork.chatbottelegrem.model.Cat;
import teamwork.chatbottelegrem.exception.CatNotFoundException;
import teamwork.chatbottelegrem.repository.CatRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CatServiceTest {
    Cat CAT_EXAMPLE = new Cat(123123L, "Барсик>", "Без породы", 2022, "информация");
    @Mock
    CatRepository repository;
    @InjectMocks
    CatService service;
    

    @BeforeEach
    public void initMock(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void addCat() {
        service.addCat(CAT_EXAMPLE);
        verify(repository).save(CAT_EXAMPLE);
        
    }

    @Test
    void getById() {
        when(repository.findById(CAT_EXAMPLE.getId())).thenReturn(Optional.ofNullable(CAT_EXAMPLE));
        assertEquals(CAT_EXAMPLE, service.getById(CAT_EXAMPLE.getId()));
        Long numberNeverUsed = 43214321L;
        assertThrows(CatNotFoundException.class, () -> {
            service.getById(numberNeverUsed);
        }, "Не найден кот");
    }

    @Test
    void update() {
        assertThrows(CatNotFoundException.class, () -> {
            service.update(CAT_EXAMPLE);
        }, "Не найден кот");
        CAT_EXAMPLE.setId(null);
        assertThrows(CatNotFoundException.class, () -> {
            service.update(CAT_EXAMPLE);
        }, "Не найден кот");
        CAT_EXAMPLE.setId(123123L);
        when(repository.findById(CAT_EXAMPLE.getId())).thenReturn(Optional.ofNullable(CAT_EXAMPLE));
        service.update(CAT_EXAMPLE);
        verify(repository).save(CAT_EXAMPLE);

    }

    @Test
    void removeById() {
        service.removeById(CAT_EXAMPLE.getId());
        verify(repository).deleteById(CAT_EXAMPLE.getId());

    }
}