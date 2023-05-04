package teamwork.chatbottelegrem.timer;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import teamwork.chatbottelegrem.Model.CatUserReportStatus;
import teamwork.chatbottelegrem.Model.CatUsersReports;
import teamwork.chatbottelegrem.repository.CatUserReportStatusRepository;
import teamwork.chatbottelegrem.repository.CatUsersReportsRepository;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class NotificationTaskTimer {

    private final CatUsersReportsRepository catUsersReportsRepository;
    private final CatUserReportStatusRepository catUserReportStatusRepository;
    private final TelegramBot telegramBot;

    public NotificationTaskTimer(CatUsersReportsRepository catUsersReportsRepository, CatUserReportStatusRepository catUserReportStatusRepository, TelegramBot telegramBot) {
        this.catUsersReportsRepository = catUsersReportsRepository;
        this.catUserReportStatusRepository = catUserReportStatusRepository;
        this.telegramBot = telegramBot;
    }

    /**
     * Метод получает список всех отчётов пользователей и по нему создаёт HashMap с последними отчётами каждого пользователя.
     * Если отчёт был прислан более 24 часов назад, присылает пользователю сообщение с напоминанием.
     */
    //TODO Добавить БД с отметкой об отправке оповещения пользователю
    @Scheduled(cron = "0 0 20 * * *")
    public void task() {
        HashMap<Long, LocalDateTime> lastReports = getLastReports();
        if (lastReports.isEmpty()) {
            return;
        }
        for (Map.Entry<Long, LocalDateTime> lastReport : lastReports.entrySet()) {
            Long chatId = lastReport.getKey();
            LocalDateTime dateTime = lastReport.getValue();
            if (dateTime.plusDays(1).isBefore(LocalDateTime.now())) {
                CatUserReportStatus lastUserReport = catUserReportStatusRepository.getReferenceByCatUsers_ChatId(chatId);
                lastUserReport.setReportSent(false);
                catUserReportStatusRepository.save(lastUserReport);
                if (!lastUserReport.isReportSent()) {
                    SendMessage sendMessage = new SendMessage(chatId,
                            "Дорогой усыновитель, не забудь прислать отчёт сегодня!");
                    telegramBot.execute(sendMessage);
                    lastUserReport.setReportSent(true);
                    catUserReportStatusRepository.save(lastUserReport);
                }
            }
        }
    }

    private HashMap<Long, LocalDateTime> getLastReports() {
        List<CatUsersReports> allCatUsersReports = catUsersReportsRepository.findAll();
        HashMap<Long, LocalDateTime> lastReportMap = new HashMap<>();
        for (CatUsersReports Report : allCatUsersReports) {
            Long chatId = Report.getCatUsers().getChatId();
            LocalDateTime dateTime = Report.getDateTime();
            if (!lastReportMap.containsKey(chatId)) {
                lastReportMap.put(chatId, dateTime);
            }
            if (lastReportMap.get(chatId).isBefore(dateTime)
                    || !lastReportMap.containsKey(chatId)) {
                lastReportMap.replace(chatId, dateTime);
            }
        }
        return lastReportMap;
    }

}
