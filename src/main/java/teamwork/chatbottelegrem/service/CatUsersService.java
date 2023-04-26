package teamwork.chatbottelegrem.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import teamwork.chatbottelegrem.Model.CatUsers;
import teamwork.chatbottelegrem.exception.CatUserNotFoundException;
import teamwork.chatbottelegrem.repository.CatUsersRepository;

import java.util.Collection;

@Service
public class CatUsersService {
    private final CatUsersRepository repository;
    private static final Logger logger = LoggerFactory.getLogger(CatUsersRepository.class);
    public CatUsersService(CatUsersRepository repository) {
        this.repository = repository;
    }
//    public CatUsers getById(Long id) {
//        logger.info("Was invoked method to get a CatOwners by id={}", id);
//        return this.repository.findById(id)
//                .orElseThrow(CatUserNotFoundException::new);
//    }
    public CatUsers create(CatUsers catUsers) {
        logger.info("Was invoked method to create a catOwners");
        return this.repository.save(catUsers);
    }
    public CatUsers update(String name, String phone, Long chatId) {
            CatUsers catUser = new CatUsers(name, phone, chatId);
            return repository.save(catUser);
    }
    public CatUsers update(CatUsers catUser) {
        return repository.save(catUser);
    }
    public void removeById(Long id) {
        logger.info("Was invoked method to remove a catOwners by id={}", id);
        this.repository.deleteById(id);
    }
    public Collection<CatUsers> getAll() {
        logger.info("Was invoked method to get all catOwners");

        return this.repository.findAll();
    }
    public Collection<CatUsers> getByChatId(Long chatId) {
        logger.info("Was invoked method to remove a catOwners by chatId={}", chatId);

        return this.repository.findByChatId(chatId);
    }
}