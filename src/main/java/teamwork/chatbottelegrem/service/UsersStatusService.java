package teamwork.chatbottelegrem.service;

import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public class UsersStatusService {
    private final UsersStatusRepository usersStatusRepository;

    public UsersStatusService(UsersStatusRepository contextRepository) {
        this.usersStatusRepository = contextRepository;
    }
    public UsersStatus saveUsersStatus(UsersStatus usersStatus) {
        return usersStatusRepository.save(usersStatus);
    }

    public Optional<UsersStatus> getByChatId(Long chatId) {

        return usersStatusRepository.findByChatId(chatId);
    }
}
