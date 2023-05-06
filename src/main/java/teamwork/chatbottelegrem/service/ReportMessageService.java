package teamwork.chatbottelegrem.service;

import org.springframework.stereotype.Service;
import teamwork.chatbottelegrem.Model.ReportMessage;
import teamwork.chatbottelegrem.exception.ReportMessageNotFoundException;
import teamwork.chatbottelegrem.repository.ReportMessageRepository;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class ReportMessageService {
    private final ReportMessageRepository repository;
    public ReportMessageService(ReportMessageRepository reportMessageRepository) {
        this.repository = reportMessageRepository;
    }
    public void uploadReportMessage(Long chatId,String name, byte[] pictureFile,
                                 String ration, String health, String behaviour,
                                 String filePath, Date lastMessage) throws IOException {
        ReportMessage report = new ReportMessage();
        report.setChatId(chatId);
        report.setName(name);
        report.setData(pictureFile);
        report.setRation(ration);
        report.setHealth(health);
        report.setBehaviour(behaviour);
        report.setFilePath(filePath);
        report.setLastMessage(lastMessage);
        this.repository.save(report);
    }
    public ReportMessage findById(Long id) {
        return this.repository.findById(id)
                .orElseThrow(()->new ReportMessageNotFoundException("Data not found exceptions"));
    }
    public ReportMessage findByChatId(Long chatId) {
        return this.repository.findByChatId(chatId);
    }
    public Collection<ReportMessage> findListByChatId(Long chatId) {
        return this.repository.findListByChatId(chatId);
    }
    public ReportMessage save(ReportMessage reportMessage) {
        return this.repository.save(reportMessage);
    }
    public void remove(Long id) {
        this.repository.deleteById(id);
    }
    public List<ReportMessage> getAll() {
        return this.repository.findAll();
    }

}

