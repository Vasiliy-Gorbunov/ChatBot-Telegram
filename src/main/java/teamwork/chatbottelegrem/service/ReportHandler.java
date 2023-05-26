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

import org.springframework.util.StringUtils;
import teamwork.chatbottelegrem.exception.ReportDataNotFoundException;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;


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


    public boolean checkReport(Update update, String catDogUser) {
        try {
            Message message = update.message();
            Long id = update.message().chat().id();

            String text = update.message().caption();

            if (update.message().photo() != null) {
                PhotoSize photo = update.message().photo()[update.message().photo().length - 1];
            }
        } catch (ReportDataNotFoundException e) {
            logger.error(e.getMessage(), e);
        } finally {
            if (update.message() != null && (update.message().caption() == null || update.message().caption().isEmpty()
                    || update.message().caption().isBlank())) {
                SendMessage sendMessage = new SendMessage(update.message().chat().id(), "Пожалуйста, направьте текстовый отчет о питомце");
                SendResponse sendResponse = telegramBot.execute(sendMessage);
                if (!sendResponse.isOk()) {
                    logger.error("Error during sending message: {}", sendResponse.description());
                }
            }

            if (update.message() != null && update.message().photo() == null) {
                SendMessage sendMessage = new SendMessage(update.message().chat().id(), "Пожалуйста, направьте фото питомца");
                SendResponse sendResponse = telegramBot.execute(sendMessage);
                if (!sendResponse.isOk()) {
                    logger.error("Error during sending message: {}", sendResponse.description());
                }
            } else if (update.message() != null) {
                GetFileResponse getFileResponse = telegramBot.execute(
                        new GetFile(update.message().photo()[update.message().photo().length - 1].fileId()));
                if (getFileResponse.isOk()) {
                    try {
                        String extension = StringUtils.getFilenameExtension(
                                getFileResponse.file().filePath());
                        byte[] image = telegramBot.getFileContent(getFileResponse.file());
                        Path path = Path.of("src/main/resources/reports/" + catDogUser + "/" + update.message().chat().id());
                        Files.createDirectories(path);
                        Files.write(Paths.get(path + "/" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")) + "." + extension), image);
                        return true;
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    return false;
                }
            }
        }
        return false;
    }
}

