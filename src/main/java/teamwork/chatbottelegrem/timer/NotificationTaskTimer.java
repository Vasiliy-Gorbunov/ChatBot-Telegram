package teamwork.chatbottelegrem.timer;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import teamwork.chatbottelegrem.Model.CatUsersReports;
import teamwork.chatbottelegrem.repository.CatUsersReportsRepository;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
public class NotificationTaskTimer {

    private final CatUsersReportsRepository catUsersReportsRepository;
    private final TelegramBot telegramBot;

    public NotificationTaskTimer(CatUsersReportsRepository catUsersReportsRepository, TelegramBot telegramBot) {
        this.catUsersReportsRepository = catUsersReportsRepository;
        this.telegramBot = telegramBot;
    }

    /**
     * Метод получает список всех отчётов пользователей и по нему создаёт HashMap с последними отчётами каждого пользователя.
     * Если отчёт был прислан более 24 часов назад, присылает пользователю сообщение с напоминанием.
     */
    //TODO Добавить БД с отметкой об отправке оповещения пользователю
    @Scheduled(fixedRate = 1, timeUnit = TimeUnit.MINUTES)
    public void task() {
        List<CatUsersReports> allCatUsersReports = catUsersReportsRepository.findAll();
        HashMap<Long, LocalDateTime> lastReportMap = new HashMap<>();
        for (CatUsersReports Report : allCatUsersReports) {
            if (!lastReportMap.containsKey(Report.getCatUsers().getChatId())) {
                lastReportMap.put(Report.getCatUsers().getChatId(), Report.getDateTime());
            }
            if (lastReportMap.get(Report.getCatUsers().getChatId()).isBefore(Report.getDateTime())
                    || !lastReportMap.containsKey(Report.getCatUsers().getChatId())) {
                lastReportMap.replace(Report.getCatUsers().getChatId(), Report.getDateTime());
            }
        }
        for (Map.Entry<Long, LocalDateTime> lastReport : lastReportMap.entrySet()) {
            if (lastReport.getValue().plusDays(1).isBefore(LocalDateTime.now())) {
                SendMessage sendMessage = new SendMessage(lastReport.getKey(),
                        "Дорогой усыновитель, не забудь прислать отчёт сегодня!");
                telegramBot.execute(sendMessage);
            }
        }

    }

}
