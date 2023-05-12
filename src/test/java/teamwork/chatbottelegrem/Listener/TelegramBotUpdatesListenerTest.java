package teamwork.chatbottelegrem.Listener;

import com.pengrad.telegrambot.BotUtils;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import teamwork.chatbottelegrem.Model.Context;
import teamwork.chatbottelegrem.botInterface.ButtonCommand;
import teamwork.chatbottelegrem.botInterface.KeyBoard;
import teamwork.chatbottelegrem.service.CatUsersService;
import teamwork.chatbottelegrem.service.ContextService;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static teamwork.chatbottelegrem.botInterface.ButtonCommand.*;

@ExtendWith(MockitoExtension.class)
public class TelegramBotUpdatesListenerTest {

    @Mock
    private TelegramBot telegramBot;
    @Mock
    private ContextService contextService;
    @Mock
    private KeyBoard keyBoard;
    @Mock
    private CatUsersService catUsersService;

    @InjectMocks
    private TelegramBotUpdatesListener telegramBotUpdatesListener;

    /**
     * Тест на проверку работоспособности команды "/start"
     * @throws URISyntaxException
     * @throws IOException
     */
    @Test
    public void handleStartTest() throws URISyntaxException, IOException {
        String json = Files.readString(Path.of(
                TelegramBotUpdatesListenerTest.class.getResource("update.json").toURI()));
        Update update = BotUtils.fromJson(json.replace("%text%", START.getCommand()), Update.class);
        SendResponse sendResponse = BotUtils.fromJson("""
                {
                    "ok": true
                }
                """, SendResponse.class);
        when(telegramBot.execute(any())).thenReturn(sendResponse);

        telegramBotUpdatesListener.process(Collections.singletonList(update));

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        Mockito.verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();

        Assertions.assertThat(actual.getParameters().get("chat_id")).isEqualTo(update.message().chat().id());
        Assertions.assertThat(actual.getParameters().get("text")).isEqualTo(
                "Привет! Я могу показать информацию о приютах, " +
                "как взять животное из приюта и принять отчет о питомце");
    }

    @Test
    public void buttonCatTest() throws URISyntaxException, IOException {
        String json = Files.readString(Path.of(
                TelegramBotUpdatesListenerTest.class.getResource("update.json").toURI()));
        Update update = BotUtils.fromJson(json.replace("%text%", CAT.getCommand()), Update.class);
        SendResponse sendResponse = BotUtils.fromJson("""
                {
                    "ok": true
                }
                """, SendResponse.class);
        Long chatId = update.message().chat().id();
        Context context = new Context(chatId, CAT.getCommand());

        when(telegramBot.execute(any())).thenReturn(sendResponse);
        when(contextService.getByChatId(chatId)).thenReturn(Optional.of(context));

        org.junit.jupiter.api.Assertions.assertEquals(CAT.getCommand(), context.getShelterType());

        telegramBotUpdatesListener.process(Collections.singletonList(update));

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        Mockito.verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();

        Assertions.assertThat(actual.getParameters().get("chat_id")).isEqualTo(chatId);
        Assertions.assertThat(actual.getParameters().get("text")).isEqualTo(
                "Вы выбрали кошачий приют.");

        Mockito.verify(contextService, times(1)).saveContext(context);
        Mockito.verify(keyBoard, times(1)).shelterMainMenu(chatId);
    }


}
