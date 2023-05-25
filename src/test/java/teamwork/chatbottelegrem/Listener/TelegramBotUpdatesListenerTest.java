package teamwork.chatbottelegrem.Listener;

import com.pengrad.telegrambot.BotUtils;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.ForwardMessage;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.GetFileResponse;
import com.pengrad.telegrambot.response.SendResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import teamwork.chatbottelegrem.Model.CatUsers;
import teamwork.chatbottelegrem.Model.Context;
import teamwork.chatbottelegrem.Model.DogUsers;
import teamwork.chatbottelegrem.Model.ReportMessage;
import teamwork.chatbottelegrem.botInterface.KeyBoard;
import teamwork.chatbottelegrem.repository.CatReportRepository;
import teamwork.chatbottelegrem.repository.ContextRepository;
import teamwork.chatbottelegrem.repository.DogReportRepository;
import teamwork.chatbottelegrem.repository.ReportMessageRepository;
import teamwork.chatbottelegrem.service.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static teamwork.chatbottelegrem.botInterface.ButtonCommand.*;

@ExtendWith(MockitoExtension.class)
public class TelegramBotUpdatesListenerTest {

    @Value("${volunteer-chat-id}")
    private Long volunteerChatId;

    @Mock
    private TelegramBot telegramBot;
    @Mock
    private ContextService contextService;
    @Mock
    private ReportMessageService reportMessageService;
    @Mock
    private KeyBoard keyBoard;
    @Mock
    private CatReportRepository catReportRepository;
    @Mock
    private DogReportRepository dogReportRepository;
    @Mock
    private ContextRepository contextRepository;
    @Mock
    private ReportMessageRepository reportMessageRepository;
    @Mock
    private CatUsersService catUsersService;
    @Mock
    private DogUsersService dogUsersService;
    @Mock
    private Logger logger;

    @InjectMocks
    private TelegramBotUpdatesListener telegramBotUpdatesListener;

    /**
     * Тест на проверку работоспособности команды "/start"
     */
    @Test
    public void handleStartTest() throws URISyntaxException, IOException {
        Update update = returnUpdateByCommand(START.getCommand());
        SendResponse sendResponse = returnSendResponseIsOk();
        when(telegramBot.execute(any())).thenReturn(sendResponse);

        telegramBotUpdatesListener.process(Collections.singletonList(update));

        argumentCaptorSendMessage(update.message().chat().id(),
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

        argumentCaptorSendMessage(chatId, "Вы выбрали кошачий приют.");

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

        argumentCaptorSendMessage(chatId, "Вы выбрали собачий приют.");

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

        argumentCaptorSendMessage(chatId, """
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

        argumentCaptorSendMessage(chatId, """
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

        argumentCaptorSendMessage(chatId, """
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

        argumentCaptorSendMessage(chatId, """
                Адрес собачьего приюта - ...
                График работы - ...
                """);
    }

    @Test
    public void buttonVolunteerTest() throws URISyntaxException, IOException {
        String command = VOLUNTEER.getCommand();
        Update update = returnUpdateByCommand(command);
        SendResponse sendResponse = returnSendResponseIsOk();
        Long chatId = update.message().chat().id();
        Integer messageId = update.message().messageId();

        when(telegramBot.execute(any())).thenReturn(sendResponse);

        telegramBotUpdatesListener.process(Collections.singletonList(update));

        argumentCaptorSendMessage(chatId, "Мы передали ваше сообщение волонтеру.");
        argumentCaptorForwardMessage(chatId, messageId);
    }

    @Test
    public void buttonHowAdoptPetInfoTest() throws URISyntaxException, IOException {
        Update update = returnUpdateByCommand(HOW_ADOPT_PET_INFO.getCommand());
        telegramBotUpdatesListener.process(Collections.singletonList(update));
        Mockito.verify(keyBoard, times(1)).shelterInfoHowAdoptPetMenu(update.message().chat().id());
    }

    @Test
    public void buttonRecommendationsListCatTest() throws URISyntaxException, IOException {
        String command = RECOMMENDATIONS_LIST.getCommand();
        Update update = returnUpdateByCommand(command);
        SendResponse sendResponse = returnSendResponseIsOk();
        Long chatId = update.message().chat().id();
        Context context = new Context(chatId, command);
        context.setShelterType(CAT.getCommand());

        when(telegramBot.execute(any())).thenReturn(sendResponse);
        when(contextService.getByChatId(chatId)).thenReturn(Optional.of(context));

        telegramBotUpdatesListener.process(Collections.singletonList(update));

        argumentCaptorSendMessage(chatId, """
                Правила знакомства с животным - ...
                Список рекомендаций - ...
                Список причин отказа в выдаче животного - ...
                """);
    }

    @Test
    public void buttonRecommendationsListDogTest() throws URISyntaxException, IOException {
        String command = RECOMMENDATIONS_LIST.getCommand();
        Update update = returnUpdateByCommand(command);
        SendResponse sendResponse = returnSendResponseIsOk();
        Long chatId = update.message().chat().id();
        Context context = new Context(chatId, command);
        context.setShelterType(DOG.getCommand());

        when(telegramBot.execute(any())).thenReturn(sendResponse);
        when(contextService.getByChatId(chatId)).thenReturn(Optional.of(context));

        telegramBotUpdatesListener.process(Collections.singletonList(update));

        argumentCaptorSendMessage(chatId, """
                Правила знакомства с животным - ...
                Список рекомендаций - ...
                Советы кинолога по первичному общению с собакой - ...
                Рекомендации по проверенным кинологам для дальнейшего обращения к ним
                Список причин отказа в выдаче животного - ...
                """);
    }


    @Test
    public void buttonDocumentsListCatTest() throws URISyntaxException, IOException {
        String command = DOCUMENTS_LIST.getCommand();
        Update update = returnUpdateByCommand(command);
        SendResponse sendResponse = returnSendResponseIsOk();
        Long chatId = update.message().chat().id();
        Context context = new Context(chatId, command);
        context.setShelterType(CAT.getCommand());

        when(telegramBot.execute(any())).thenReturn(sendResponse);
        when(contextService.getByChatId(chatId)).thenReturn(Optional.of(context));

        telegramBotUpdatesListener.process(Collections.singletonList(update));

        argumentCaptorSendMessage(chatId, "Для взятия кота из приюта необходимы такие документы: ...");
    }

    @Test
    public void buttonDocumentsListDogTest() throws URISyntaxException, IOException {
        String command = DOCUMENTS_LIST.getCommand();
        Update update = returnUpdateByCommand(command);
        SendResponse sendResponse = returnSendResponseIsOk();
        Long chatId = update.message().chat().id();
        Context context = new Context(chatId, command);
        context.setShelterType(DOG.getCommand());

        when(telegramBot.execute(any())).thenReturn(sendResponse);
        when(contextService.getByChatId(chatId)).thenReturn(Optional.of(context));

        telegramBotUpdatesListener.process(Collections.singletonList(update));

        argumentCaptorSendMessage(chatId, "Для взятия собаки из приюта необходимы такие документы: ...");
    }

    @Test
    public void buttonGetReportFormCatTest() throws URISyntaxException, IOException {
        String command = GET_REPORT_FORM.getCommand();
        Update update = returnUpdateByCommand(command);
        SendResponse sendResponse = returnSendResponseIsOk();
        Long chatId = update.message().chat().id();
        Context context = new Context(chatId, command);
        context.setShelterType(CAT.getCommand());

        when(telegramBot.execute(any())).thenReturn(sendResponse);
        when(contextService.getByChatId(chatId)).thenReturn(Optional.of(context));

        telegramBotUpdatesListener.process(Collections.singletonList(update));

        argumentCaptorSendMessage(chatId, """
                - Рацион животного:
                - Общее самочувствие и привыкание к новому месту:
                - Изменение в поведении: отказ от старых привычек, приобретение новых:
                  """);
    }

    @Test
    public void buttonGetReportFormDogTest() throws URISyntaxException, IOException {
        String command = GET_REPORT_FORM.getCommand();
        Update update = returnUpdateByCommand(command);
        SendResponse sendResponse = returnSendResponseIsOk();
        Long chatId = update.message().chat().id();
        Context context = new Context(chatId, command);
        context.setShelterType(DOG.getCommand());

        when(telegramBot.execute(any())).thenReturn(sendResponse);
        when(contextService.getByChatId(chatId)).thenReturn(Optional.of(context));

        telegramBotUpdatesListener.process(Collections.singletonList(update));

        argumentCaptorSendMessage(chatId, """
                - Рацион животного:
                - Общее самочувствие и привыкание к новому месту:
                - Изменение в поведении: отказ от старых привычек, приобретение новых:
                 """);
    }

    @Test
    public void buttonSendPetReportCatTest() throws URISyntaxException, IOException {
        String command = SEND_PET_REPORT.getCommand();
        Update update = returnUpdateByCommand(command);
        Long chatId = update.message().chat().id();
        Context context = new Context(chatId, command);
        context.setShelterType(CAT.getCommand());
        byte[] testPhoto = Files.readAllBytes(Path.of(TelegramBotUpdatesListenerTest.class.getResource("foto.jpeg").toURI()));

        GetFileResponse getFileResponse = BotUtils.fromJson("""
                {
                    "result":
                    {
                        "file_id": "001",
                        "file_unique_id": "002",
                        "file_size": 157170,
                        "file_path": "photo.jpeg"
                    },
                    "ok": true
                }
                """, GetFileResponse.class);

        when(telegramBot.execute(any())).thenReturn(getFileResponse);
        when(telegramBot.getFileContent(any())).thenReturn(testPhoto);
        when(contextService.getByChatId(chatId)).thenReturn(Optional.of(context));

        telegramBotUpdatesListener.process(Collections.singletonList(update));

        Mockito.verify(catReportRepository, times(1)).save(any());

    }

    @Test
    public void buttonSendPetReportDogTest() throws URISyntaxException, IOException {
        String command = SEND_PET_REPORT.getCommand();
        Update update = returnUpdateByCommand(command);
        Long chatId = update.message().chat().id();
        Context context = new Context(chatId, command);
        context.setShelterType(DOG.getCommand());
        byte[] testPhoto = Files.readAllBytes(Path.of(TelegramBotUpdatesListenerTest.class.getResource("foto.jpeg").toURI()));

        GetFileResponse getFileResponse = BotUtils.fromJson("""
                {
                    "result":
                    {
                        "file_id": "001",
                        "file_unique_id": "002",
                        "file_size": 157170,
                        "file_path": "photo.jpeg"
                    },
                    "ok": true
                }
                """, GetFileResponse.class);

        when(telegramBot.execute(any())).thenReturn(getFileResponse);
        when(telegramBot.getFileContent(any())).thenReturn(testPhoto);
        when(contextService.getByChatId(chatId)).thenReturn(Optional.of(context));

        telegramBotUpdatesListener.process(Collections.singletonList(update));

        Mockito.verify(dogReportRepository, times(1)).save(any());

    }

    @Test
    public void buttonBadReportNotificationTest() throws URISyntaxException, IOException {
        String command = BAD_REPORT_NOTIFICATION.getCommand();
        Update update = returnUpdateByCommand(command);
        SendResponse sendResponse = returnSendResponseIsOk();
        Long chatId = update.message().chat().id();

        when(telegramBot.execute(any())).thenReturn(sendResponse);

        telegramBotUpdatesListener.process(Collections.singletonList(update));

        argumentCaptorSendMessage(chatId, "Дорогой усыновитель, мы заметили, что ты заполняешь отчет не так подробно, как необходимо. " +
                "Пожалуйста, подойди ответственнее к этому занятию. " +
                "В противном случае волонтеры приюта будут обязаны самолично проверять условия содержания животного");
    }

    @Test
    public void buttonSuccessCongratulationTest() throws URISyntaxException, IOException {
        String command = SUCCESS_CONGRATULATION.getCommand();
        Update update = returnUpdateByCommand(command);
        SendResponse sendResponse = returnSendResponseIsOk();
        Long chatId = update.message().chat().id();

        when(telegramBot.execute(any())).thenReturn(sendResponse);

        telegramBotUpdatesListener.process(Collections.singletonList(update));

        argumentCaptorSendMessage(chatId, "Поздравляем! Вы успешно прошли испытательный срок.");
    }

    @Test
    public void buttonAdditionalPeriod14Test() throws URISyntaxException, IOException {
        String command = ADDITIONAL_PERIOD_14.getCommand();
        Update update = returnUpdateByCommand(command);
        SendResponse sendResponse = returnSendResponseIsOk();
        Long chatId = update.message().chat().id();

        when(telegramBot.execute(any())).thenReturn(sendResponse);

        telegramBotUpdatesListener.process(Collections.singletonList(update));

        argumentCaptorSendMessage(chatId, "Вам дополнительно назначено 14 календарных дней испытательного срока");
    }

    @Test
    public void buttonAdditionalPeriod30Test() throws URISyntaxException, IOException {
        String command = ADDITIONAL_PERIOD_30.getCommand();
        Update update = returnUpdateByCommand(command);
        SendResponse sendResponse = returnSendResponseIsOk();
        Long chatId = update.message().chat().id();

        when(telegramBot.execute(any())).thenReturn(sendResponse);

        telegramBotUpdatesListener.process(Collections.singletonList(update));

        argumentCaptorSendMessage(chatId, "Вам дополнительно назначено 30 календарных дней испытательного срока");
    }

    @Test
    public void buttonAdoptionRefuseTest() throws URISyntaxException, IOException {
        String command = ADOPTION_REFUSE.getCommand();
        Update update = returnUpdateByCommand(command);
        SendResponse sendResponse = returnSendResponseIsOk();
        Long chatId = update.message().chat().id();

        when(telegramBot.execute(any())).thenReturn(sendResponse);

        telegramBotUpdatesListener.process(Collections.singletonList(update));

        argumentCaptorSendMessage(chatId, "К сожалению, Вы не прошли испытательный срок. " +
                "Пожалуйста, верните животное в приют в течение двух календарных дней.");
    }

    @Test
    public void buttonNullCatTest() throws URISyntaxException, IOException {
        Update update = returnUpdateByCommand("report");
        SendResponse sendResponse = returnSendResponseIsOk();
        Long chatId = update.message().chat().id();
        Context context = new Context(chatId, new CatUsers());
        context.setShelterType(CAT.getCommand());


        when(telegramBot.execute(any())).thenReturn(sendResponse);
        when(contextService.getByChatId(chatId)).thenReturn(Optional.of(context));

        telegramBotUpdatesListener.process(Collections.singletonList(update));

        Mockito.verify(catUsersService).update(new CatUsers("Johnny", "88005553535", null));
        argumentCaptorSendMessage(chatId, "Мы получили ваши контактные данные");
        argumentCaptorForwardMessage(chatId, update.message().messageId());
    }

    @Test
    public void buttonNullDogTest() throws URISyntaxException, IOException {
        Update update = returnUpdateByCommand("report");
        SendResponse sendResponse = returnSendResponseIsOk();
        Long chatId = update.message().chat().id();
        Context context = new Context(chatId, new DogUsers());
        context.setShelterType(DOG.getCommand());


        when(telegramBot.execute(any())).thenReturn(sendResponse);
        when(contextService.getByChatId(chatId)).thenReturn(Optional.of(context));

        telegramBotUpdatesListener.process(Collections.singletonList(update));

        Mockito.verify(dogUsersService).update(new DogUsers("Johnny", "88005553535", null));
        argumentCaptorSendMessage(chatId, "Мы получили ваши контактные данные");
        argumentCaptorForwardMessage(chatId, update.message().messageId());
    }

    @Test
    public void sendWarning() {
        SendResponse sendResponse = returnSendResponseIsOk();
        List<Context> contextList = new ArrayList<>();
        contextList.add(new Context(123L, new CatUsers("John", "88005553535", 123L)));
        List<ReportMessage> reportMessageList = new ArrayList<>();
        reportMessageList.add(new ReportMessage(123L, new Date("2023/5/18")));
        reportMessageList.add(new ReportMessage(123L, new Date("2023/5/19")));
        reportMessageList.add(new ReportMessage(123L, new Date("2023/5/20")));
        reportMessageList.add(new ReportMessage(123L, new Date("2023/5/21")));
        Long chatId = contextList.get(0).getChatId();

        when(contextService.getAll()).thenReturn(contextList);
        when(reportMessageService.getAll()).thenReturn(reportMessageList);
        when(telegramBot.execute(new SendMessage(chatId, "Прошло два дня после отправки прошлого отчёта. Пожалуйста, отправьте отчёт!"))).thenReturn(sendResponse);
        when(telegramBot.execute(new SendMessage(volunteerChatId, "Пользователь под номером: " + chatId
                + " не отправлял отчёты уже более двух дней!"))).thenReturn(sendResponse);


        telegramBotUpdatesListener.sendWarning();

        argumentCaptorSendMessage(chatId, "Прошло два дня после отправки прошлого отчёта. Пожалуйста, отправьте отчёт!");
        argumentCaptorSendMessage(volunteerChatId, "Пользователь под номером: " + chatId
                + " не отправлял отчёты уже более двух дней!");


    }

    @Test
    public void sendResponseIsNotOkTest() throws URISyntaxException, IOException {
        Update update = returnUpdateByCommand(START.getCommand());
        SendResponse sendResponse = BotUtils.fromJson("""
                {
                    "ok": false
                }
                """, SendResponse.class);

        when(telegramBot.execute(any())).thenReturn(sendResponse);

        telegramBotUpdatesListener.process(Collections.singletonList(update));


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

    private void argumentCaptorSendMessage(Long chatId, String text) {
        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        Mockito.verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();

        org.assertj.core.api.Assertions.assertThat(actual.getParameters().get("chat_id")).isEqualTo(chatId);
        org.assertj.core.api.Assertions.assertThat(actual.getParameters().get("text")).isEqualTo(
                text);
    }

    private void argumentCaptorForwardMessage(Long chatId, Integer messageId) {
        ArgumentCaptor<ForwardMessage> argumentCaptor = ArgumentCaptor.forClass(ForwardMessage.class);
        Mockito.verify(telegramBot).execute(argumentCaptor.capture());
        ForwardMessage actual = argumentCaptor.getValue();

        org.assertj.core.api.Assertions.assertThat(actual.getParameters().get("chat_id")).isEqualTo(volunteerChatId);
        org.assertj.core.api.Assertions.assertThat(actual.getParameters().get("from_chat_id")).isEqualTo(chatId);
        org.assertj.core.api.Assertions.assertThat(actual.getParameters().get("message_id")).isEqualTo(messageId);
    }

}
