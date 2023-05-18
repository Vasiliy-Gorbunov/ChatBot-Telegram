    package teamwork.chatbottelegrem.config;

    import com.pengrad.telegrambot.TelegramBot;
    import org.springframework.beans.factory.annotation.Value;
    import org.springframework.context.annotation.Bean;
    import org.springframework.context.annotation.Configuration;
    /**
     * Класс конфигурации Телеграм-бота
     */
    @Configuration
    public class TelegramBotConfig {

        @Bean
        public TelegramBot telegramBot(@Value("${telegram.bot.token}") String token){
            return new TelegramBot(token);

        }
    }
