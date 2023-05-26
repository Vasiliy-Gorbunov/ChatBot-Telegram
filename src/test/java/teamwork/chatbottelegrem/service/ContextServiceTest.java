package teamwork.chatbottelegrem.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import teamwork.chatbottelegrem.model.Context;
import teamwork.chatbottelegrem.repository.ContextRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ContextServiceTest {
    Context CONTEXT_EXAMPLE = new Context(123123L, "DOG");
    @Mock
    ContextRepository repository;
    @InjectMocks
    ContextService service;

    @BeforeEach
    public void iniMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void saveContext() {
        service.saveContext(CONTEXT_EXAMPLE);
        verify(repository).save(CONTEXT_EXAMPLE);
    }

    @Test
    void getAll() {
        List<Context> contexts = new ArrayList<>();
        contexts.add(CONTEXT_EXAMPLE);
        when(repository.findAll()).thenReturn(contexts);
        assertEquals(contexts, service.getAll());
    }

    @Test
    void getByChatId() {
        Optional<Context> context = Optional.ofNullable(CONTEXT_EXAMPLE);
        when(repository.findByChatId(CONTEXT_EXAMPLE.getChatId())).thenReturn(Optional.ofNullable(CONTEXT_EXAMPLE));
        assertEquals(context, service.getByChatId(CONTEXT_EXAMPLE.getChatId()));
    }
}