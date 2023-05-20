package teamwork.chatbottelegrem.service;

import com.pengrad.telegrambot.BotUtils;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;


import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ReportHandlerTest {

    String json = Files.readString(Path.of(ReportHandlerTest.class.getResource("update.json").toURI()));
    SendResponse sendResponse = BotUtils.fromJson("""
            {
            "ok": true
            }
            """, SendResponse.class
    );
    @Mock
    TelegramBot telegramBot;
    @Mock
    Logger logger;
    @InjectMocks
    ReportHandler reportHandler;

    ReportHandlerTest() throws IOException, URISyntaxException {
    }

    @BeforeEach
    public void iniMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void checkEmptyReport() {
        Update update = BotUtils.fromJson(json.replace("%text%", ""), Update.class);
        SendMessage sendMessage = new SendMessage(update.message().chat().id(), "Пожалуйста, направьте текстовый отчет о питомце");
        SendResponse sendResponse = BotUtils.fromJson("""
                {
                "ok": true
                }
                """, SendResponse.class
        );

        when(telegramBot.execute(any())).thenReturn(sendResponse);
        reportHandler.checkReport(update);
        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        assertEquals(actual.getParameters().get("chat_id"), update.message().chat().id());
        assertEquals(actual.getParameters().get("text"), sendMessage.getParameters().get("text"));
    }

    @Test
    void checkReport2() {

        Update update = BotUtils.fromJson(json.replace("%text%", "    "), Update.class);
        SendMessage sendMessage = new SendMessage(update.message().chat().id(), "Пожалуйста, направьте текстовый отчет о питомце");
        when(telegramBot.execute(any())).thenReturn(sendResponse);
        reportHandler.checkReport(update);
        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actual2 = argumentCaptor.getValue();
        assertEquals(actual2.getParameters().get("chat_id"), update.message().chat().id());
        assertEquals(actual2.getParameters().get("text"), sendMessage.getParameters().get("text"));
    }
    @Test
    void checkNullMessageReport() throws URISyntaxException, IOException {
        String json = Files.readString(Path.of(ReportHandlerTest.class.getResource("updateWithoutPhoto.json").toURI()));
        Update update = BotUtils.fromJson(json.replace("%text%", "С питомцем всё в порядке"), Update.class);
        SendMessage sendMessage = new SendMessage(update.message().chat().id(), "Пожалуйста, направьте фото питомца");
        when(telegramBot.execute(any())).thenReturn(sendResponse);
        reportHandler.checkReport(update);
        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actual2 = argumentCaptor.getValue();
        assertEquals(actual2.getParameters().get("chat_id"), update.message().chat().id());
        assertEquals(actual2.getParameters().get("text"), sendMessage.getParameters().get("text"));

    }

}