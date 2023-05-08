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
    private final Logger logger = LoggerFactory.getLogger(CatReportService.class);
    private final CatReportRepository  catReportRepository;
    private final TelegramBot telegramBot;


    public CatReportService(CatReportRepository catReportRepository, TelegramBot telegramBot) {
        this.catReportRepository = catReportRepository;
        this.telegramBot = telegramBot;
    }


    /**
     * Метод сохранения отчета о коте в БД
     */
    public void save(Update update) {
        if (update.message() != null) {
            Long chatId = update.message().chat().id();
            PhotoSize photo = update.message().photo()[update.message().photo().length - 1];
            String text = update.message().text();
            String fileId = photo.fileId();


            if (catReportRepository.findByDate(LocalDate.now()) == null
                    && text != null && !text.isEmpty() && !text.isBlank() || photo != null) {
                try {
                    ReportHandler reportHandler = new ReportHandler(telegramBot);
                    reportHandler.checkReport(update);
                    CatReport catReport = new CatReport();
                    catReport.setId(update.updateId());
                    catReport.setChatId(chatId);
                    catReport.setDate(LocalDate.now());
                    catReport.setTextReport(text);
                    catReport.setFileId(fileId);
                    catReportRepository.save(catReport);
                } catch (ReportDataNotFoundException e) {
                    logger.error(e.getMessage(), e);
                }

            } else if (catReportRepository.findByDate(LocalDate.now()) != null
                    && text != null && !text.isEmpty() && !text.isBlank() || photo != null) {

                CatReport catReport = catReportRepository.findByDate(LocalDate.now());

                if (text != null) {
                    catReport.setTextReport(text);
                }
                if (photo != null) {
                    catReport.setFileId(fileId);
                }
                catReportRepository.save(catReport);
            }
        }
    }
}

