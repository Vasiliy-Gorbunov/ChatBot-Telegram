package teamwork.chatbottelegrem.timer;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class NotificationTaskTimer {
    @Scheduled(fixedRate = 30, timeUnit = TimeUnit.SECONDS)
    public void task() {

    }

}
