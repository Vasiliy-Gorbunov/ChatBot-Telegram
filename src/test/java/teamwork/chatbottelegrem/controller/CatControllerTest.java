package teamwork.chatbottelegrem.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import teamwork.chatbottelegrem.Model.Cat;
import teamwork.chatbottelegrem.service.CatService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CatControllerTest {
    Cat CAT_EXAMPLE = new Cat(123123L, "Барсик>", "Без породы", 2022, "информация");

    @Mock
    CatService service;
    @InjectMocks
    CatController controller;
    @BeforeEach
    public void initMock(){
        MockitoAnnotations.initMocks(this);
    }
    @Test
    void addCat() {
        controller.addCat(CAT_EXAMPLE);
        verify(service).addCat(CAT_EXAMPLE);

    }

    @Test
    void getCatById() {
        when(controller.getCatById(CAT_EXAMPLE.getId())).thenReturn(CAT_EXAMPLE);
        assertEquals(CAT_EXAMPLE, controller.getCatById(CAT_EXAMPLE.getId()));
    }

    @Test
    void updateCatById() {
        controller.updateCatById(CAT_EXAMPLE.getId(), CAT_EXAMPLE);
        verify(service).update(CAT_EXAMPLE);

    }

    @Test
    void removeCat() {
        controller.removeCat(CAT_EXAMPLE.getId());
        verify(service).removeById(CAT_EXAMPLE.getId());
    }
}