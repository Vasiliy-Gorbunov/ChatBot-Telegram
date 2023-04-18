package teamwork.chatbottelegrem.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import teamwork.chatbottelegrem.repository.TelegramBotRepository;

@Service
public class TelegramBotService {

    final TelegramBotRepository repository;

    public TelegramBotService(TelegramBotRepository repository) {
        this.repository = repository;
    }

    public String getStringFromRepository(String buttonName) {
        return repository.findByButtonName(buttonName).getDescription();
    }


}
