package teamwork.chatbottelegrem.timer;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import teamwork.chatbottelegrem.Model.CatUserReportStatus;
import teamwork.chatbottelegrem.Model.CatUsers;
import teamwork.chatbottelegrem.Model.CatUsersReports;
import teamwork.chatbottelegrem.repository.CatUserReportStatusRepository;
import teamwork.chatbottelegrem.repository.CatUsersReportsRepository;
import teamwork.chatbottelegrem.repository.CatUsersRepository;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
public class NotificationTaskTimer {

    private final CatUsersRepository catUsersRepository;
    private final CatUsersReportsRepository catUsersReportsRepository;
    private final CatUserReportStatusRepository catUserReportStatusRepository;
    private final TelegramBot telegramBot;

    public NotificationTaskTimer(CatUsersRepository catUsersRepository,
                                 CatUsersReportsRepository catUsersReportsRepository,
                                 CatUserReportStatusRepository catUserReportStatusRepository,
                                 TelegramBot telegramBot) {
        this.catUsersRepository = catUsersRepository;
        this.catUsersReportsRepository = catUsersReportsRepository;
        this.catUserReportStatusRepository = catUserReportStatusRepository;
        this.telegramBot = telegramBot;
    }

    /**
     * Метод получает список пользователей и проверяет статус отчёта.
     * Если отчёт был прислан более 24 часов назад, присылает пользователю сообщение с напоминанием.
     */
    @Scheduled(cron = "0 0 15 * * *")
    public void task() {
//        HashMap<Long, LocalDateTime> lastReports = getLastReports();
//        if (lastReports.isEmpty()) {
//            return;
//        }
//        for (Map.Entry<Long, LocalDateTime> lastReport : lastReports.entrySet()) {
//            Long chatId = lastReport.getKey();
//            LocalDateTime dateTime = lastReport.getValue();
//            if (dateTime.plusDays(1).isBefore(LocalDateTime.now())) {
//                CatUserReportStatus lastUserReport = catUserReportStatusRepository.getReferenceByCatUsers_ChatId(chatId);
//                lastUserReport.setReportSent(false);
//                catUserReportStatusRepository.save(lastUserReport);
        List<CatUserReportStatus> catUserReportStatusList = catUserReportStatusRepository.findAll();
        for (CatUserReportStatus catUser : catUserReportStatusList) {
            if (!catUser.isReportSent()) {
                Long chatId = catUser.getCatUsers().getChatId();
                SendMessage sendMessage = new SendMessage(chatId,
                        "Дорогой усыновитель, не забудь прислать отчёт сегодня!");
                telegramBot.execute(sendMessage);
                catUser.setReportSent(true);
                catUserReportStatusRepository.save(catUser);
            }
        }
    }
//    }

    /**
     * Вспомогательный метод для актуализации присланных отчётов.
     * Если прошло более 24 часов, меняет статус на false.
     */
    @Scheduled(fixedRate = 1, timeUnit = TimeUnit.MINUTES)
    private void updateReportsStatus() {
        List<CatUsers> allCatUsers = catUsersRepository.findAll();
        for (CatUsers catUser : allCatUsers) {
            if (catUser.getLastReport().plusDays(1).isBefore(LocalDateTime.now())) {
                Long chatId = catUser.getChatId();
                CatUserReportStatus lastUserReport = catUserReportStatusRepository.getReferenceByCatUsers_ChatId(chatId);
                lastUserReport.setReportSent(false);
                catUserReportStatusRepository.save(lastUserReport);
            }
        }
    }

//    private HashMap<Long, LocalDateTime> getLastReports() {
//        List<CatUsersReports> allCatUsersReports = catUsersReportsRepository.findAll();
//        HashMap<Long, LocalDateTime> lastReportMap = new HashMap<>();
//        for (CatUsersReports Report : allCatUsersReports) {
//            Long chatId = Report.getCatUsers().getChatId();
//            LocalDateTime dateTime = Report.getDateTime();
//            if (!lastReportMap.containsKey(chatId)) {
//                lastReportMap.put(chatId, dateTime);
//            }
//            if (lastReportMap.get(chatId).isBefore(dateTime)
//                    || !lastReportMap.containsKey(chatId)) {
//                lastReportMap.replace(chatId, dateTime);
//            }
//        }
//        return lastReportMap;
//    }

}
