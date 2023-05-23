
package teamwork.chatbottelegrem.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import org.springframework.stereotype.Service;

import teamwork.chatbottelegrem.Model.CatReport;
import teamwork.chatbottelegrem.repository.CatReportRepository;

import java.time.LocalDate;


@Service
public class CatReportService {
    private final CatReportRepository catReportRepository;
    private final TelegramBot telegramBot;


    public CatReportService(CatReportRepository catReportRepository, TelegramBot telegramBot) {
        this.catReportRepository = catReportRepository;
        this.telegramBot = telegramBot;
    }


    /**
     * Метод сохранения отчета о коте в базе данных
     */

    public void save(Update update) {
        ReportHandler reportHandler = new ReportHandler(telegramBot);
        reportHandler.checkReport(update);
        catReportRepository.save(catReportFromUpdate(update));
    }


    /**
     * Метод создания отчета коте на основе данных Update
     */
    public CatReport catReportFromUpdate(Update update) {
        Long chatId = update.message().chat().id();
        String text = update.message().caption();
        String fileId = update.message()
                .photo()[update.message().photo().length - 1]
                .fileId();

        CatReport catReport = new CatReport();
        catReport.setId(update.updateId());
        catReport.setChatId(chatId);
        catReport.setDate(LocalDate.now());
        catReport.setTextReport(text);
        catReport.setFileId(fileId);
        return catReport;
    }
}