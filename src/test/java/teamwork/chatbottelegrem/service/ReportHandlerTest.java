package teamwork.chatbottelegrem.service;

import com.pengrad.telegrambot.BotUtils;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;


import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReportHandlerTest {


    @Mock
    TelegramBot telegramBot;
    @Mock
    Logger logger;
    @InjectMocks
    ReportHandler reportHandler;
@BeforeEach
public void iniMocks() {
    MockitoAnnotations.initMocks(this);
}

    ReportHandlerTest() throws IOException, URISyntaxException {

    }


    @Test
    void checkReport() throws URISyntaxException, IOException {
        String json = Files.readString(Path.of(ReportHandlerTest.class.getResource("update.json").toURI()));
        Update update = BotUtils.fromJson(json.replace("%text%", " "), Update.class);
        SendMessage sendMessage = new SendMessage(update.message().chat().id(), "Пожалуйста, направьте текстовый отчет о питомце");
        SendResponse sendResponse = BotUtils.fromJson("""
                {
                "result": 200,
                "ok": true
                }
                """, SendResponse.class);

        when(telegramBot.execute(sendMessage)).thenReturn(sendResponse);
        reportHandler.checkReport(update);

        verify(telegramBot).execute(sendMessage);

    }
}