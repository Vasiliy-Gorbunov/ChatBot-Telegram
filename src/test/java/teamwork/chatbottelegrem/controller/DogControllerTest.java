package teamwork.chatbottelegrem.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import teamwork.chatbottelegrem.Model.Dog;
import teamwork.chatbottelegrem.service.CatService;
import teamwork.chatbottelegrem.service.DogService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class DogControllerTest {
    Dog DOG_EXAMPLE = new Dog(123123L, "Шарик", "Дворняга", 2022, "информация");
    @Mock
    DogService service;
    @InjectMocks
    DogController controller;
    @BeforeEach
    public void initMock(){
        MockitoAnnotations.initMocks(this);
    }
    @Test
    void addCat() {
        controller.addDog(DOG_EXAMPLE);
        verify(service).addDog(DOG_EXAMPLE);

    }

    @Test
    void getCatById() {
        when(controller.getDogById(DOG_EXAMPLE.getId())).thenReturn(DOG_EXAMPLE);
        assertEquals(DOG_EXAMPLE, controller.getDogById(DOG_EXAMPLE.getId()));
    }

    @Test
    void updateCatById() {
        controller.updateDogById(DOG_EXAMPLE.getId(), DOG_EXAMPLE);
        verify(service).update(DOG_EXAMPLE);

    }

    @Test
    void removeCat() {
        controller.removeDog(DOG_EXAMPLE.getId());
        verify(service).removeById(DOG_EXAMPLE.getId());
    }
}