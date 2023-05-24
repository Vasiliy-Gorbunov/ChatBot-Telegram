
package teamwork.chatbottelegrem.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.PhotoSize;
import com.pengrad.telegrambot.model.Update;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import teamwork.chatbottelegrem.Model.CatReport;
import teamwork.chatbottelegrem.exception.ReportDataNotFoundException;

import teamwork.chatbottelegrem.repository.CatReportRepository;

import java.time.LocalDate;


@Service
public class CatReportService {

    private final CatReportRepository  catReportRepository;

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

        CatReport catReport = new CatReport();
        catReport.setChatId(update.message().chat().id());
        catReport.setDate(LocalDate.now());
        catReport.setTextReport(update.message().caption());
        catReport.setFileId(update.message()
                .photo()[update.message().photo().length - 1]
                .fileId());
        return catReport;
    }
}


