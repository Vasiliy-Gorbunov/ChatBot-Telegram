package teamwork.chatbottelegrem.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Contact;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.ForwardMessage;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import teamwork.chatbottelegrem.model.*;
import teamwork.chatbottelegrem.botInterface.ButtonCommand;
import teamwork.chatbottelegrem.botInterface.KeyBoard;
import teamwork.chatbottelegrem.service.*;
import teamwork.chatbottelegrem.service.ReportMessageService;

import javax.annotation.PostConstruct;
import java.util.*;


@Component
public class TelegramBotUpdatesListener implements UpdatesListener {
    @Value("${volunteer-chat-id}")
    private Long volunteerChatId;
    private final Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

    private final TelegramBot telegramBot;
    private final KeyBoard keyBoard;
    private final CatUsersService catUsersService;
    private final DogUsersService dogUsersService;
    private final ContextService contextService;
    private final ReportMessageService reportMessageService;
    private final CatReportService catReportService;
    private final DogReportService dogReportService;
    private final VolunteerContextService volunteerContextService;


    /**
     * Класс реализации общения бота с пользователем
     */
    public TelegramBotUpdatesListener(TelegramBot telegramBot, KeyBoard keyBoard, ContextService contextService, CatUsersService catUsersService, DogUsersService dogUsersService, ReportMessageService reportMessageService, CatReportService catReportService, DogReportService dogReportService, VolunteerContextService volunteerContextService) {
        this.telegramBot = telegramBot;
        this.keyBoard = keyBoard;
        this.contextService = contextService;
        this.catUsersService = catUsersService;
        this.dogUsersService = dogUsersService;
        this.reportMessageService = reportMessageService;
        this.catReportService = catReportService;
        this.dogReportService = dogReportService;
        this.volunteerContextService = volunteerContextService;
    }

    public static ButtonCommand parse(String buttonCommand) {
        ButtonCommand[] values = ButtonCommand.values();
        for (ButtonCommand command : values) {
            if (command.getCommand().equals(buttonCommand)) {
                return command;
            }
        }
        return null;
    }

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    /**
     * Метод отвечающий, за принятие команд ботом, и отправления ответа на них
     */
    @Override
    public int process(List<Update> updates) {
        try {
            updates.forEach(update -> {
                logger.info("Handles update: {}", update);
                Message message = update.message();
                long chatId = message.chat().id();
                String text = message.text();
                int messageId = message.messageId();
                Contact contact = update.message().contact();

                try {

                    int userChatId = Integer.parseInt(text);
                    if (chatId == volunteerChatId) {

                        if (volunteerContextService.getVolunteerContext(chatId).isPresent()) {
                            VolunteerContext volunteerContext = volunteerContextService.getVolunteerContext(chatId).get();
                            if (volunteerContext.isBadReportNotification()) {
                                sendResponseMessage(userChatId, "Дорогой усыновитель, мы заметили, что ты " +
                                        "заполняешь отчет не так подробно, как необходимо. Пожалуйста, подойди " +
                                        "ответственнее к этому занятию. В противном случае волонтеры приюта " +
                                        "будут обязаны самолично проверять условия содержания животного");
                            } else if (volunteerContext.isSuccessCongratulations()) {
                                sendResponseMessage(userChatId, "Поздравляем! Вы успешно прошли испытательный срок.");
                            } else if (volunteerContext.isAdditionalPeriod14()) {
                                sendResponseMessage(userChatId, "Вам дополнительно назначено 14 календарных дней испытательного срока");
                            } else if (volunteerContext.isAdditionalPeriod30()) {
                                sendResponseMessage(userChatId, "Вам дополнительно назначено 30 календарных дней испытательного срока");
                            } else if (volunteerContext.isAdoptionRefuse()) {
                                sendResponseMessage(userChatId, "К сожалению, Вы не прошли испытательный срок. " +
                                        "Пожалуйста, верните животное в приют в течение двух календарных дней.");
                            }
                            sendResponseMessage(chatId, "Пользователю направлено шаблонное сообщение.Что дальше?");
                            volunteerContextService.deleteVolunteerContext(chatId);
                            keyBoard.volunteerMenu(chatId);
                        } else {
                            sendResponseMessage(chatId, "Сначала выберите соответствующий пункт меню");
                            keyBoard.volunteerMenu(chatId);
                        }


                    }
                } catch (NumberFormatException e) {
                    switch (parse(text)) {
                        case START -> {
                            if (contextService.getByChatId(chatId).isEmpty()) {
                                sendResponseMessage(chatId, "Привет! Я могу показать информацию о приютах, " +
                                        "как взять животное из приюта и принять отчет о питомце");
                                Context context = new Context();
                                context.setChatId(chatId);
                                contextService.saveContext(context);
                            }
                            keyBoard.chooseMenu(chatId);
                        }
                        case CAT -> {
                            Context context = contextService.getByChatId(chatId).get();
                            if (catUsersService.getByChatId(chatId).isEmpty()) {
                                CatUsers catUsers = new CatUsers();
                                catUsers.setChatId(chatId);
                                catUsersService.create(catUsers);
                                context.setCatUsers(catUsers);
                            }
                            context.setShelterType(ButtonCommand.CAT.getCommand());
                            contextService.saveContext(context);
                            sendResponseMessage(chatId, "Вы выбрали кошачий приют.");
                            keyBoard.shelterMainMenu(chatId);
                        }
                        case DOG -> {
                            Context context = contextService.getByChatId(chatId).get();
                            if (dogUsersService.getByChatId(chatId).isEmpty()) {
                                DogUsers dogUsers = new DogUsers();
                                dogUsers.setChatId(chatId);
                                dogUsersService.create(dogUsers);
                                context.setDogUsers(dogUsers);
                            }
                            context.setShelterType(ButtonCommand.DOG.getCommand());
                            contextService.saveContext(context);
                            sendResponseMessage(chatId, "Вы выбрали собачий приют.");
                            keyBoard.shelterMainMenu(chatId);
                        }
                        case MAIN_MENU, MAIN_MENU2 -> keyBoard.shelterMainMenu(chatId);
                        case SHELTER_INFO_MENU -> keyBoard.shelterInfoMenu(chatId);
                        case SHELTER_INFO -> {
                            Context context = contextService.getByChatId(chatId).get();
                            if (context.getShelterType().equals(ButtonCommand.CAT.getCommand())) {
                                sendResponseMessage(chatId, """
                                        Информация о кошачем приюте - ...
                                        Рекомендации о технике безопасности на территории кошачего приюта - ...
                                        Контактные данные охраны - ...
                                        """);
                            } else if (context.getShelterType().equals(ButtonCommand.DOG.getCommand())) {
                                sendResponseMessage(chatId, """
                                        Информация о собачем приюте - ...
                                        Рекомендации о технике безопасности на территории собачего приюта - ...
                                        Контактные данные охраны - ...
                                        """);
                            }
                        }
                        case SHELTER_ADDRESS_SCHEDULE -> {
                            Context context = contextService.getByChatId(chatId).get();
                            if (context.getShelterType().equals(ButtonCommand.CAT.getCommand())) {
                                sendResponseMessage(chatId, """
                                        Адрес кошачего приюта - ...
                                        График работы - ...
                                        """);
                            } else if (context.getShelterType().equals(ButtonCommand.DOG.getCommand())) {
                                sendResponseMessage(chatId, """
                                        Адрес собачьего приюта - ...
                                        График работы - ...
                                        """);
                            }
                        }
                        case VOLUNTEER -> {
                            sendResponseMessage(chatId, "Мы передали ваше сообщение волонтеру.");
                            sendForwardMessage(chatId, messageId);
                        }
                        case HOW_ADOPT_PET_INFO -> keyBoard.shelterInfoHowAdoptPetMenu(chatId);
                        case RECOMMENDATIONS_LIST -> {
                            Context context = contextService.getByChatId(chatId).get();
                            if (context.getShelterType().equals(ButtonCommand.CAT.getCommand())) {
                                sendResponseMessage(chatId, """
                                        Правила знакомства с животным - ...
                                        Список рекомендаций - ...
                                        Список причин отказа в выдаче животного - ...
                                        """);
                            } else if (context.getShelterType().equals(ButtonCommand.DOG.getCommand())) {
                                sendResponseMessage(chatId, """
                                        Правила знакомства с животным - ...
                                        Список рекомендаций - ...
                                        Советы кинолога по первичному общению с собакой - ...
                                        Рекомендации по проверенным кинологам для дальнейшего обращения к ним
                                        Список причин отказа в выдаче животного - ...
                                        """);
                            }
                        }
                        case DOCUMENTS_LIST -> {
                            Context context = contextService.getByChatId(chatId).get();
                            if (context.getShelterType().equals(ButtonCommand.CAT.getCommand())) {
                                sendResponseMessage(chatId,
                                        "Для взятия кота из приюта необходимы такие документы: ...");
                            } else if (context.getShelterType().equals(ButtonCommand.DOG.getCommand())) {
                                sendResponseMessage(chatId,
                                        "Для взятия собаки из приюта необходимы такие документы: ...");
                            }
                        }

                        case GET_REPORT_FORM -> {
                            Context context = contextService.getByChatId(chatId).get();
                            if (context.getShelterType().equals(ButtonCommand.CAT.getCommand())) {
                                sendResponseMessage(chatId,
                                        """
                                                - Рацион животного:
                                                - Общее самочувствие и привыкание к новому месту:
                                                - Изменение в поведении: отказ от старых привычек, приобретение новых:
                                                  """);
                            } else if (context.getShelterType().equals(ButtonCommand.DOG.getCommand())) {
                                sendResponseMessage(chatId,
                                        """
                                                - Рацион животного:
                                                - Общее самочувствие и привыкание к новому месту:
                                                - Изменение в поведении: отказ от старых привычек, приобретение новых:
                                                 """);
                            }
                        }

                        case SEND_PET_REPORT -> {
                            Context context = contextService.getByChatId(chatId).get();
                            if (context.getShelterType().equals(ButtonCommand.CAT.getCommand())) {
                                sendResponseMessage(chatId, "Отправьте фото с описанием состояния питомца. " +
                                        "Для получения формы отправки нажмите на кнопку \"Получить форму отчета о питомце\".");
                            } else if (context.getShelterType().equals(ButtonCommand.DOG.getCommand())) {
                                sendResponseMessage(chatId, "Отправьте фото с описанием состояния питомца. " +
                                        "Для получения формы отправки нажмите на кнопку \"Получить форму отчета о питомце\".");
                            }
                        }
                        case VOLUNTEER_MENU -> {
                            if (chatId == volunteerChatId) {
                                checkVolunteerContext(chatId);
                                keyBoard.volunteerMenu(volunteerChatId);
                            } else {
                                ifUserNotVolunteer(chatId);
                            }

                        }

                        case BAD_REPORT_NOTIFICATION -> {
                            if (chatId == volunteerChatId) {
                                checkVolunteerContext(chatId);
                                VolunteerContext volunteerContext = new VolunteerContext();
                                volunteerContext.setVolunteerChatId(chatId);
                                volunteerContext.setBadReportNotification(true);
                                volunteerContextService.saveVolunteerContext(volunteerContext);
                                sendResponseMessage(chatId, "Направьте идентификатор пользователя(ChatId) для отправки ему сообщения");
                            } else {
                                ifUserNotVolunteer(chatId);
                            }
                        }


                        case SUCCESS_CONGRATULATION -> {
                            if (chatId == volunteerChatId) {
                                checkVolunteerContext(chatId);
                                VolunteerContext volunteerContext = new VolunteerContext();
                                volunteerContext.setVolunteerChatId(chatId);
                                volunteerContext.setSuccessCongratulations(true);
                                volunteerContextService.saveVolunteerContext(volunteerContext);
                                sendResponseMessage(chatId, "Направьте идентификатор пользователя(ChatId) для отправки ему сообщения");
                            } else {
                                ifUserNotVolunteer(chatId);
                            }
                        }


                        case ADDITIONAL_PERIOD_14 -> {
                            if (chatId == volunteerChatId) {
                                checkVolunteerContext(chatId);
                                VolunteerContext volunteerContext = new VolunteerContext();
                                volunteerContext.setVolunteerChatId(chatId);
                                volunteerContext.setAdditionalPeriod14(true);
                                volunteerContextService.saveVolunteerContext(volunteerContext);
                                sendResponseMessage(chatId, "Направьте идентификатор пользователя(ChatId) для отправки ему сообщения");
                            } else {
                                ifUserNotVolunteer(chatId);
                            }
                        }


                        case ADDITIONAL_PERIOD_30 -> {
                            if (chatId == volunteerChatId) {
                                checkVolunteerContext(chatId);
                                VolunteerContext volunteerContext = new VolunteerContext();
                                volunteerContext.setVolunteerChatId(chatId);
                                volunteerContext.setAdditionalPeriod30(true);
                                volunteerContextService.saveVolunteerContext(volunteerContext);
                                sendResponseMessage(chatId, "Направьте идентификатор пользователя(ChatId) для отправки ему сообщения");
                            } else {
                                ifUserNotVolunteer(chatId);
                            }
                        }


                        case ADOPTION_REFUSE -> {
                            if (chatId == volunteerChatId) {
                                checkVolunteerContext(chatId);
                                VolunteerContext volunteerContext = new VolunteerContext();
                                volunteerContext.setVolunteerChatId(chatId);
                                volunteerContext.setAdoptionRefuse(true);
                                volunteerContextService.saveVolunteerContext(volunteerContext);
                                sendResponseMessage(chatId, "Направьте идентификатор пользователя(ChatId) для отправки ему сообщения");
                            } else {
                                ifUserNotVolunteer(chatId);
                            }
                        }


                        case null -> {
                            Context context = contextService.getByChatId(chatId).get();
                            if (context.getShelterType().equals(ButtonCommand.CAT.getCommand())) {
                                if (update.message() != null && contact != null) {
                                    String lastName = Optional.ofNullable(contact.lastName()).orElse("");
                                    CatUsers catUsers = catUsersService.getByChatId(chatId).iterator().next();
                                    catUsers.setNumber(contact.phoneNumber());
                                    catUsers.setName(contact.firstName()+" "+lastName);
                                    catUsersService.update(catUsers);
                                    sendForwardMessage(chatId, messageId);
                                    sendResponseMessage(chatId, "Мы получили ваши контактные данные");
                                } else if (update.message() != null) {
                                    if (catReportService.save(update)) {
                                        sendForwardMessage(chatId, messageId);
                                    }
                                }
                            } else if (context.getShelterType().equals(
                                    ButtonCommand.DOG.getCommand())) {
                                if (update.message() != null && contact != null) {
                                    String lastName = Optional.ofNullable(contact.lastName()).orElse("");
                                    DogUsers dogUsers = dogUsersService.getByChatId(chatId).iterator().next();
                                    dogUsers.setNumber(contact.phoneNumber());
                                    dogUsers.setName(contact.firstName()+" "+lastName);
                                    dogUsersService.update(dogUsers);
                                    sendForwardMessage(chatId, messageId);
                                    sendResponseMessage(chatId, "Мы получили ваши контактные данные");
                                } else if (update.message() != null) {
                                    if (dogReportService.save(update)) {
                                        sendForwardMessage(chatId, messageId);
                                    }
                                }
                            }
                        }
                        default -> {

                            sendResponseMessage(chatId, "Неизвестная команда!");
                            keyBoard.chooseMenu(chatId);
                        }
                    }
                }
            });
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    public void volunteerCommand(long chatIdUpdate, long chatIdUser, String command) {
        if (chatIdUpdate == volunteerChatId) {

        }

    }

    public void sendForwardMessage(long chatId, int messageId) {
        ForwardMessage forwardMessage = new ForwardMessage(volunteerChatId, chatId, messageId);
        SendResponse sendResponse = telegramBot.execute(forwardMessage);
        if (!sendResponse.isOk()) {
            logger.error("Error during sending message: {}", sendResponse.description());
        }
    }

    /**
     * Метод отправки текстовых сообщений
     */
    public void sendResponseMessage(long chatId, String text) {
        SendMessage sendMessage = new SendMessage(chatId, text);
        SendResponse sendResponse = telegramBot.execute(sendMessage);
        if (!sendResponse.isOk()) {
            logger.error("Error during sending message: {}", sendResponse.description());
        }
    }

    private void ifUserNotVolunteer(long chatId) {

        sendResponseMessage(chatId, "Данный пункт доступен только волонтеру. Извините, но у Вас нет доступа");
        keyBoard.chooseMenu(chatId);
    }

    private void checkVolunteerContext(long chatId) {
        if (volunteerContextService.getVolunteerContext(chatId).isPresent()) {
            volunteerContextService.deleteVolunteerContext(chatId);
        }
    }

    /**
     * Метод контроля отчётов пользователей
     */
    @Scheduled(cron = "@daily")
    public void sendWarning() {
        for (Context context : contextService.getAll()) {
            long chatId = context.getChatId();
            long daysOfReports = reportMessageService.getAll().stream()
                    .filter(s -> Objects.equals(s.getChatId(), chatId))
                    .count();
            if (daysOfReports < 30 && daysOfReports != 0) {
                long twoDay = 172800000;
                Date nowTime = new Date(new Date().getTime() - twoDay);
                Date lastMessageDate = reportMessageService.getAll().stream()
                        .filter(s -> Objects.equals(s.getChatId(), chatId))
                        .map(ReportMessage::getLastMessage)
                        .max(Date::compareTo)
                        .orElse(null);
                if (lastMessageDate != null) {
                    if (lastMessageDate.before(nowTime)) {
                        sendResponseMessage(chatId, "Прошло два дня после отправки прошлого отчёта. Пожалуйста, отправьте отчёт!");
                        sendResponseMessage(volunteerChatId, "Пользователь под номером: " + chatId
                                + " не отправлял отчёты уже более двух дней!");
                    }
                }
            }
        }

    }

}






