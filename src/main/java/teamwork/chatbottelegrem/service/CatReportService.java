
package teamwork.chatbottelegrem.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import org.springframework.stereotype.Service;

import teamwork.chatbottelegrem.model.CatReport;

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

    public boolean save(Update update) {
        ReportHandler reportHandler = new ReportHandler(telegramBot);
        String catUsers = "catUsers";
        if (reportHandler.checkReport(update, catUsers)) {
            catReportRepository.save(catReportFromUpdate(update));
            return true;
        } else {
            return false;
        }
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


