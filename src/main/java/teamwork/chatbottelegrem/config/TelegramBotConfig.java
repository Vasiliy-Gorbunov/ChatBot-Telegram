    package teamwork.chatbottelegrem.config;

    import com.pengrad.telegrambot.TelegramBot;
    import org.springframework.beans.factory.annotation.Value;
    import org.springframework.context.annotation.Bean;
    import org.springframework.context.annotation.Configuration;
    import org.springframework.context.annotation.PropertySource;

    @Configuration
//    @PropertySource("application.properties")
    public class TelegramBotConfig {

        @Bean
        public TelegramBot telegramBot(@Value("${bot.token}") String token){
            return new TelegramBot(token);

        }
    }
