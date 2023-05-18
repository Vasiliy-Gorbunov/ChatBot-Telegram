package teamwork.chatbottelegrem.service;

import org.springframework.stereotype.Service;
import teamwork.chatbottelegrem.Model.Context;
import teamwork.chatbottelegrem.repository.ContextRepository;

import java.util.Collection;
import java.util.Optional;
/**
 * сервис класса контекста
 */
@Service
public class ContextService {
    private final ContextRepository contextRepository;

    public ContextService(ContextRepository contextRepository) {
        this.contextRepository = contextRepository;
    }
    public Context saveContext(Context context) {
        return contextRepository.save(context);
    }
    public Collection<Context> getAll() {
        return contextRepository.findAll();
    }

    public Optional<Context> getByChatId(Long chatId) {

        return contextRepository.findByChatId(chatId);
    }
}

