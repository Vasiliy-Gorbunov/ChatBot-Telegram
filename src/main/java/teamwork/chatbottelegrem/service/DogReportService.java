package teamwork.chatbottelegrem.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.PhotoSize;
import com.pengrad.telegrambot.model.Update;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import teamwork.chatbottelegrem.Model.DogReport;
import teamwork.chatbottelegrem.exception.ReportDataNotFoundException;
import teamwork.chatbottelegrem.repository.DogReportRepository;

import java.time.LocalDate;


@Service
public class DogReportService {
    private final DogReportRepository dogReportRepository;
    private final TelegramBot telegramBot;


    public DogReportService(DogReportRepository dogReportRepository, TelegramBot telegramBot) {
        this.dogReportRepository = dogReportRepository;
        this.telegramBot = telegramBot;
    }

    /**
     * Метод сохранения отчета о собаке в БД
     */
    public void save(Update update) {
        ReportHandler reportHandler = new ReportHandler(telegramBot);
        reportHandler.checkReport(update);

        Long chatId = update.message().chat().id();
        String text = update.message().text();
        String fileId = update.message()
                .photo()[update.message().photo().length - 1]
                .fileId();

        DogReport dogReport = new DogReport();
        dogReport.setId(update.updateId());
        dogReport.setChatId(chatId);
        dogReport.setDate(LocalDate.now());
        dogReport.setTextReport(text);
        dogReport.setFileId(fileId);
        dogReportRepository.save(dogReport);
    }
}


