package teamwork.chatbottelegrem.service;

import org.springframework.stereotype.Service;
import teamwork.chatbottelegrem.Model.CatUsers;
import teamwork.chatbottelegrem.Model.CatUsersReports;
import teamwork.chatbottelegrem.repository.CatUsersReportsRepository;
import teamwork.chatbottelegrem.repository.CatUsersRepository;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;

@Service
public class CatUsersReportsService {

    private final CatUsersReportsRepository repository;
    private final CatUsersRepository catUsersRepository;

    public CatUsersReportsService(CatUsersReportsRepository repository, CatUsersRepository catUsersRepository) {
        this.repository = repository;
        this.catUsersRepository = catUsersRepository;
    }

    public void addPhoto(Long chatId, String path, LocalDateTime localDateTime, String text) {
        CatUsers catUser = catUsersRepository.findByChatId(chatId);
        CatUsersReports catUserReports = new CatUsersReports(catUser, localDateTime, text, path);
        repository.save(catUserReports);
    }

    public HashSet<Long> getAllUsersChatId() {
        HashSet<Long> usersChatId = new HashSet<>();
        List<CatUsersReports> allUsers = repository.findAll();
        for (CatUsersReports User : allUsers) {
            usersChatId.add(User.getCatUsers().getChatId());
        }
        return usersChatId;
    }
}
