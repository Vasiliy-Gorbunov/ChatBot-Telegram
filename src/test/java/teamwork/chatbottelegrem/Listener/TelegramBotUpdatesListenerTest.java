package teamwork.chatbottelegrem.Listener;

import com.pengrad.telegrambot.BotUtils;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import teamwork.chatbottelegrem.Model.CatUsers;
import teamwork.chatbottelegrem.Model.Context;
import teamwork.chatbottelegrem.Model.DogUsers;
import teamwork.chatbottelegrem.botInterface.KeyBoard;
import teamwork.chatbottelegrem.service.CatUsersService;
import teamwork.chatbottelegrem.service.ContextService;
import teamwork.chatbottelegrem.service.DogUsersService;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
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
    @Mock
    private DogUsersService dogUsersService;

    @InjectMocks
    private TelegramBotUpdatesListener telegramBotUpdatesListener;

    /**
     * Тест на проверку работоспособности команды "/start"
     *
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
        String command = CAT.getCommand();
        Update update = returnUpdateByCommand(command);
        SendResponse sendResponse = returnSendResponseIsOk();
        Long chatId = update.message().chat().id();
        Context context = new Context(chatId, command);

        when(telegramBot.execute(any())).thenReturn(sendResponse);
        when(contextService.getByChatId(chatId)).thenReturn(Optional.of(context));
        when(catUsersService.getByChatId(chatId)).thenReturn(List.of(new CatUsers()));

        Assertions.assertEquals(command, context.getShelterType());

        telegramBotUpdatesListener.process(Collections.singletonList(update));

        argumentCaptor(chatId, "Вы выбрали кошачий приют.");

        Mockito.verify(contextService, times(1)).saveContext(context);
        Mockito.verify(keyBoard, times(1)).shelterMainMenu(chatId);
    }

    @Test
    public void buttonCatEmptyTest() throws URISyntaxException, IOException {
        String command = CAT.getCommand();
        Update update = returnUpdateByCommand(command);
        SendResponse sendResponse = returnSendResponseIsOk();
        Long chatId = update.message().chat().id();
        Context context = new Context(chatId, command);

        when(telegramBot.execute(any())).thenReturn(sendResponse);
        when(contextService.getByChatId(chatId)).thenReturn(Optional.of(context));
        when(catUsersService.getByChatId(chatId)).thenReturn(List.of());

        Assertions.assertTrue(catUsersService.getByChatId(chatId).isEmpty());
        CatUsers expected = new CatUsers();
        expected.setChatId(chatId);

        telegramBotUpdatesListener.process(Collections.singletonList(update));

        Mockito.verify(catUsersService, times(1)).create(expected);
    }

    @Test
    public void buttonDogTest() throws URISyntaxException, IOException {
        String command = DOG.getCommand();
        Update update = returnUpdateByCommand(command);
        SendResponse sendResponse = returnSendResponseIsOk();
        Long chatId = update.message().chat().id();
        Context context = new Context(chatId, command);

        when(telegramBot.execute(any())).thenReturn(sendResponse);
        when(contextService.getByChatId(chatId)).thenReturn(Optional.of(context));
        when(dogUsersService.getByChatId(chatId)).thenReturn(List.of(new DogUsers()));

        Assertions.assertEquals(command, context.getShelterType());

        telegramBotUpdatesListener.process(Collections.singletonList(update));

        argumentCaptor(chatId, "Вы выбрали собачий приют.");

        Mockito.verify(contextService, times(1)).saveContext(context);
        Mockito.verify(keyBoard, times(1)).shelterMainMenu(chatId);
    }

    @Test
    public void buttonDogEmptyTest() throws URISyntaxException, IOException {
        String command = DOG.getCommand();
        Update update = returnUpdateByCommand(command);
        SendResponse sendResponse = returnSendResponseIsOk();
        Long chatId = update.message().chat().id();
        Context context = new Context(chatId, command);

        when(telegramBot.execute(any())).thenReturn(sendResponse);
        when(contextService.getByChatId(chatId)).thenReturn(Optional.of(context));
        when(dogUsersService.getByChatId(chatId)).thenReturn(List.of());

        Assertions.assertTrue(dogUsersService.getByChatId(chatId).isEmpty());
        DogUsers expected = new DogUsers();
        expected.setChatId(chatId);

        telegramBotUpdatesListener.process(Collections.singletonList(update));

        Mockito.verify(dogUsersService, times(1)).create(expected);
    }

    @Test
    public void buttonMainMenuTest() throws URISyntaxException, IOException {
        Update update = returnUpdateByCommand(MAIN_MENU.getCommand());
        telegramBotUpdatesListener.process(Collections.singletonList(update));
        Mockito.verify(keyBoard, times(1)).shelterMainMenu(update.message().chat().id());
    }

    @Test
    public void buttonShelterInfoMenuTest() throws URISyntaxException, IOException {
        Update update = returnUpdateByCommand(SHELTER_INFO_MENU.getCommand());
        telegramBotUpdatesListener.process(Collections.singletonList(update));
        Mockito.verify(keyBoard, times(1)).shelterInfoMenu(update.message().chat().id());
    }

    @Test
    public void buttonShelterInfoCatTest() throws URISyntaxException, IOException {
        String command = SHELTER_INFO.getCommand();
        Update update = returnUpdateByCommand(command);
        SendResponse sendResponse = returnSendResponseIsOk();
        Long chatId = update.message().chat().id();
        Context context = new Context(chatId, command);
        context.setShelterType(CAT.getCommand());

        when(telegramBot.execute(any())).thenReturn(sendResponse);
        when(contextService.getByChatId(chatId)).thenReturn(Optional.of(context));

        telegramBotUpdatesListener.process(Collections.singletonList(update));

        argumentCaptor(chatId, """
                                    Информация о кошачем приюте - ...
                                    Рекомендации о технике безопасности на территории кошачего приюта - ...
                                    Контактные данные охраны - ...
                                    """);
    }

    @Test
    public void buttonShelterInfoDogTest() throws URISyntaxException, IOException {
        String command = SHELTER_INFO.getCommand();
        Update update = returnUpdateByCommand(command);
        SendResponse sendResponse = returnSendResponseIsOk();
        Long chatId = update.message().chat().id();
        Context context = new Context(chatId, command);
        context.setShelterType(DOG.getCommand());

        when(telegramBot.execute(any())).thenReturn(sendResponse);
        when(contextService.getByChatId(chatId)).thenReturn(Optional.of(context));

        telegramBotUpdatesListener.process(Collections.singletonList(update));

        argumentCaptor(chatId, """
                                    Информация о собачем приюте - ...
                                    Рекомендации о технике безопасности на территории собачего приюта - ...
                                    Контактные данные охраны - ...
                                    """);
    }

    @Test
    public void buttonShelterAddressScheduleCatTest() throws URISyntaxException, IOException {
        String command = SHELTER_ADDRESS_SCHEDULE.getCommand();
        Update update = returnUpdateByCommand(command);
        SendResponse sendResponse = returnSendResponseIsOk();
        Long chatId = update.message().chat().id();
        Context context = new Context(chatId, command);
        context.setShelterType(CAT.getCommand());

        when(telegramBot.execute(any())).thenReturn(sendResponse);
        when(contextService.getByChatId(chatId)).thenReturn(Optional.of(context));

        telegramBotUpdatesListener.process(Collections.singletonList(update));

        argumentCaptor(chatId, """
                                    Адрес кошачего приюта - ...
                                    График работы - ...
                                    """);
    }

    @Test
    public void buttonShelterAddressScheduleDogTest() throws URISyntaxException, IOException {
        String command = SHELTER_ADDRESS_SCHEDULE.getCommand();
        Update update = returnUpdateByCommand(command);
        SendResponse sendResponse = returnSendResponseIsOk();
        Long chatId = update.message().chat().id();
        Context context = new Context(chatId, command);
        context.setShelterType(DOG.getCommand());

        when(telegramBot.execute(any())).thenReturn(sendResponse);
        when(contextService.getByChatId(chatId)).thenReturn(Optional.of(context));

        telegramBotUpdatesListener.process(Collections.singletonList(update));

        argumentCaptor(chatId, """
                                    Адрес собачьего приюта - ...
                                    График работы - ...
                                    """);
    }


    private Update returnUpdateByCommand(String command) throws URISyntaxException, IOException {
        String json = Files.readString(Path.of(
                Objects.requireNonNull(TelegramBotUpdatesListenerTest.class.getResource("update.json")).toURI()));
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

        org.assertj.core.api.Assertions.assertThat(actual.getParameters().get("chat_id")).isEqualTo(chatId);
        org.assertj.core.api.Assertions.assertThat(actual.getParameters().get("text")).isEqualTo(
                text);
    }


}
