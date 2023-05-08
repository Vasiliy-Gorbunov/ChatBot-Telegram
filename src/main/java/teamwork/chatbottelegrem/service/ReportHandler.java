package teamwork.chatbottelegrem.service;


import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.PhotoSize;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.GetFile;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.GetFileResponse;
import com.pengrad.telegrambot.response.SendResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


import java.io.IOException;

@Component
public class ReportHandler {
    private final Logger logger = LoggerFactory.getLogger(ReportHandler.class);
    private final TelegramBot telegramBot;
    public ReportHandler(TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }

    /**
     * Метод проверки полноты отчета
     */
    public void checkReport(Update update) {
        Message message = update.message();
        Long id = update.message().chat().id();
        String text = update.message().text();
        PhotoSize photo = update.message().photo()[update.message().photo().length - 1];
        if (message != null && (text == null || text.isEmpty()
                || text.isBlank())) {
            SendMessage sendMessage = new SendMessage(id, "Пожалуйста, направьте текстовый отчет о питомце");
            SendResponse sendResponse = telegramBot.execute(sendMessage);
            if (!sendResponse.isOk()) {
                logger.error("Error during sending message: {}", sendResponse.description());
            }
        }

        if (message != null && photo == null) {
            SendMessage sendMessage = new SendMessage(id, "Пожалуйста, направьте текстовый отчет о питомце");
            SendResponse sendResponse = telegramBot.execute(sendMessage);
            if (!sendResponse.isOk()) {
                logger.error("Error during sending message: {}", sendResponse.description());
            } else {
                GetFileResponse getFileResponse = telegramBot.execute(new GetFile(photo.fileId()));
                if (getFileResponse.isOk()) {
                    try {
                        byte[] image = telegramBot.getFileContent(getFileResponse.file());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    }
}



