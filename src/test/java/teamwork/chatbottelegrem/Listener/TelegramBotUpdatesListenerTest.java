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
import teamwork.chatbottelegrem.Model.CatUsers;
import teamwork.chatbottelegrem.Model.Context;
import teamwork.chatbottelegrem.botInterface.KeyBoard;
import teamwork.chatbottelegrem.service.CatUsersService;
import teamwork.chatbottelegrem.service.ContextService;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;
import static teamwork.chatbottelegrem.botInterface.ButtonCommand.*;

@ExtendWith(MockitoExtension.class)
public class TelegramBotUpdatesListenerTest {

    @Mock
    private TelegramBot telegramBot;
    @Mock
    private Context context;
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
     *
     * @throws URISyntaxException
     * @throws IOException
     */
    @Test
    public void handleStartTest() throws URISyntaxException, IOException {
        Update update = returnUpdateByCommand(START.getCommand());
        SendResponse sendResponse = returnSendResponseIsOk();
        when(telegramBot.execute(any())).thenReturn(sendResponse);

        telegramBotUpdatesListener.process(Collections.singletonList(update));

        argumentCaptor(update.message().chat().id(),
                "Привет! Я могу показать информацию о приютах, " +
                "как взять животное из приюта и принять отчет о питомце");
    }

    @Test
    public void buttonCatTest() throws URISyntaxException, IOException {
        Update update = returnUpdateByCommand(CAT.getCommand());
        SendResponse sendResponse = returnSendResponseIsOk();
        Long chatId = update.message().chat().id();
        Context context = new Context(chatId, CAT.getCommand());

        when(telegramBot.execute(any())).thenReturn(sendResponse);
        when(contextService.getByChatId(chatId)).thenReturn(Optional.of(context));
        when(catUsersService.getByChatId(chatId)).thenReturn(List.of(new CatUsers()));

        org.junit.jupiter.api.Assertions.assertEquals(CAT.getCommand(), context.getShelterType());

        telegramBotUpdatesListener.process(Collections.singletonList(update));

        argumentCaptor(chatId, "Вы выбрали кошачий приют.");

        Mockito.verify(contextService, times(1)).saveContext(context);
        Mockito.verify(keyBoard, times(1)).shelterMainMenu(chatId);
    }

    @Test
    public void buttonCatEmptyTest() throws URISyntaxException, IOException {
        Update update = returnUpdateByCommand(CAT.getCommand());
        SendResponse sendResponse = returnSendResponseIsOk();
        Long chatId = update.message().chat().id();
        Context context = new Context(chatId, CAT.getCommand());

        when(telegramBot.execute(any())).thenReturn(sendResponse);
        when(contextService.getByChatId(chatId)).thenReturn(Optional.of(context));
        when(catUsersService.getByChatId(chatId)).thenReturn(List.of());

        org.junit.jupiter.api.Assertions.assertTrue(catUsersService.getByChatId(chatId).isEmpty());
        CatUsers expected = new CatUsers();
        expected.setChatId(chatId);

        org.junit.jupiter.api.Assertions.assertEquals(CAT.getCommand(), context.getShelterType());

        telegramBotUpdatesListener.process(Collections.singletonList(update));



       argumentCaptor(chatId, "Вы выбрали кошачий приют.");

        Mockito.verify(catUsersService, times(1)).create(expected);
        Mockito.verify(contextService, times(1)).saveContext(context);
        Mockito.verify(keyBoard, times(1)).shelterMainMenu(chatId);
    }

    private Update returnUpdateByCommand(String command) throws URISyntaxException, IOException {
        String json = Files.readString(Path.of(
                TelegramBotUpdatesListenerTest.class.getResource("update.json").toURI()));
        return BotUtils.fromJson(json.replace("%text%", command), Update.class);
    }

    private SendResponse returnSendResponseIsOk() {
        return BotUtils.fromJson("""
                {
                    "ok": true
                }
                """, SendResponse.class);
    }

    private void argumentCaptor(Long chatId, String text) {
        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        Mockito.verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();

        Assertions.assertThat(actual.getParameters().get("chat_id")).isEqualTo(chatId);
        Assertions.assertThat(actual.getParameters().get("text")).isEqualTo(
                text);
    }


}
