package teamwork.chatbottelegrem.service;

import com.pengrad.telegrambot.TelegramBot;

import com.pengrad.telegrambot.model.Update;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import teamwork.chatbottelegrem.model.DogReport;

import teamwork.chatbottelegrem.repository.DogReportRepository;

import java.time.LocalDate;


@Service
public class DogReportService {

    private final Logger logger = LoggerFactory.getLogger(DogReportService.class);

    private final DogReportRepository dogReportRepository;
    private final TelegramBot telegramBot;


    public DogReportService(DogReportRepository dogReportRepository, TelegramBot telegramBot) {
        this.dogReportRepository = dogReportRepository;
        this.telegramBot = telegramBot;
    }

    /**

     * Метод сохранения отчета о собаке в DB
     */
    public void save(Update update) {
        ReportHandler reportHandler = new ReportHandler(telegramBot);
        String dogUsers = "dogUsers";
        reportHandler.checkReport(update, dogUsers);

        dogReportRepository.save(dogReportFromUpdate(update));
    }


    /**
     * Метод создания отчета о собаке на основе данных Update
     */
    public DogReport dogReportFromUpdate(Update update) {
        DogReport dogReport = new DogReport();
        dogReport.setChatId(update.message().chat().id());
        dogReport.setDate(LocalDate.now());
        dogReport.setTextReport(update.message().caption());
        dogReport.setFileId(update.message()
                .photo()[update.message().photo().length - 1]
                .fileId());
        return dogReport;

    }
}

