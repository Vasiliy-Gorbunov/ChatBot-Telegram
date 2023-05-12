package teamwork.chatbottelegrem.Listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Contact;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.ForwardMessage;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import teamwork.chatbottelegrem.Model.CatUsers;
import teamwork.chatbottelegrem.Model.Context;
import teamwork.chatbottelegrem.Model.DogUsers;
import teamwork.chatbottelegrem.Model.ReportMessage;
import teamwork.chatbottelegrem.botInterface.ButtonCommand;
import teamwork.chatbottelegrem.botInterface.KeyBoard;
import teamwork.chatbottelegrem.repository.CatReportRepository;
import teamwork.chatbottelegrem.repository.DogReportRepository;
import teamwork.chatbottelegrem.service.*;
import teamwork.chatbottelegrem.service.ReportMessageService;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.List;
import java.util.Objects;


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
    private final DogReportRepository dogReportRepository;
    private final CatReportRepository catReportRepository;



    /**
     * Класс реализации общения бота с пользователем
     *
     */
    public TelegramBotUpdatesListener(TelegramBot telegramBot, KeyBoard keyBoard,ContextService contextService, CatUsersService catUsersService, DogUsersService dogUsersService, ReportMessageService reportMessageService, DogReportRepository dogReportRepository, CatReportRepository catReportRepository) {
        this.telegramBot = telegramBot;
        this.keyBoard = keyBoard;
        this.contextService = contextService;
        this.catUsersService = catUsersService;
        this.dogUsersService = dogUsersService;
        this.reportMessageService= reportMessageService;
        this.catReportRepository = catReportRepository;
        this.dogReportRepository = dogReportRepository;
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
     *
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


                switch (parse(text)) {
                    case START -> {
                        if(contextService.getByChatId(chatId).isEmpty()){
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
                        if(catUsersService.getByChatId(chatId).isEmpty()) {
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
                        if(dogUsersService.getByChatId(chatId).isEmpty()) {
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
                    case MAIN_MENU -> {
                        keyBoard.shelterMainMenu(chatId);
                    }
                    case SHELTER_INFO_MENU -> {
                        keyBoard.shelterInfoMenu(chatId);
                    }
                    case SHELTER_INFO -> {
                        Context context = contextService.getByChatId(chatId).get();
                        if(context.getShelterType().equals(ButtonCommand.CAT.getCommand())) {
                            sendResponseMessage(chatId, """
                                    Информация о кошачем приюте - ...
                                    Рекомендации о технике безопасности на территории кошачего приюта - ...
                                    Контактные данные охраны - ...
                                    """);
                        } else if(context.getShelterType().equals(ButtonCommand.DOG.getCommand())){
                            sendResponseMessage(chatId, """
                                    Информация о собачем приюте - ...
                                    Рекомендации о технике безопасности на территории собачего приюта - ...
                                    Контактные данные охраны - ...
                                    """);
                        }
                    }
                    case SHELTER_ADDRESS_SCHEDULE -> {
                        Context context = contextService.getByChatId(chatId).get();
                        if(context.getShelterType().equals(ButtonCommand.CAT.getCommand())) {
                            sendResponseMessage(chatId, """
                                    Адрес кошачего приюта - ...
                                    График работы - ...
                                    """);
                        } else if(context.getShelterType().equals(ButtonCommand.DOG.getCommand())) {
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
                    case HOW_ADOPT_PET_INFO -> {
                        keyBoard.shelterInfoHowAdoptPetMenu(chatId);
                    }
                    case RECOMMENDATIONS_LIST -> {
                        Context context = contextService.getByChatId(chatId).get();
                        if(context.getShelterType().equals(ButtonCommand.CAT.getCommand())) {
                            sendResponseMessage(chatId, """
                                    Правила знакомства с животным - ...
                                    Список рекомендаций - ...
                                    Список причин отказа в выдаче животного - ...
                                    """);
                        } else if(context.getShelterType().equals(ButtonCommand.DOG.getCommand())) {
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
                        if(context.getShelterType().equals(ButtonCommand.CAT.getCommand())) {
                            sendResponseMessage(chatId,
                                    "Для взятия кота из приюта необходимы такие документы: ...");
                        } else if(context.getShelterType().equals(ButtonCommand.DOG.getCommand())) {
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
                            CatReportService catReportService = new CatReportService(catReportRepository, telegramBot);
                            catReportService.save(update);

                        } else if (context.getShelterType().equals(ButtonCommand.DOG.getCommand())) {
                            DogReportService dogReportService = new DogReportService(dogReportRepository, telegramBot);
                            dogReportService.save(update);
                        }
                    }

                    case BAD_REPORT_NOTIFICATION -> {
                        sendResponseMessage(chatId, "Дорогой усыновитель, мы заметили, что ты заполняешь отчет не так подробно, как необходимо. Пожалуйста, подойди ответственнее к этому занятию. В противном случае волонтеры приюта будут обязаны самолично проверять условия содержания животного");
                    }

                    case SUCCESS_CONGRATULATION -> {
                        sendResponseMessage(chatId, "Поздравляем! Вы успешно прошли испытательный срок.");
                    }

                    case ADDITIONAL_PERIOD_14 -> {
                        sendResponseMessage(chatId, "Вам дополнительно назначено 14 календарных дней испытательного срока");
                    }

                    case ADDITIONAL_PERIOD_30 -> {
                        sendResponseMessage(chatId, "Вам дополнительно назначено 30 календарных дней испытательного срока");
                    }

                    case ADOPTION_REFUSE -> {
                        sendResponseMessage(chatId, "К сожалению, Вы не прошли испытательный срок. Пожалуйста, верните животное в приют в течение двух календарных дней.");
                    }

                    case null -> {
                        Context context = contextService.getByChatId(chatId).get();
                        if(context.getShelterType().equals(
                                ButtonCommand.CAT.getCommand()) && update.message() != null && contact != null) {
                            CatUsers catUsers = context.getCatUsers();
                            catUsers.setNumber(contact.phoneNumber());
                            catUsers.setName(contact.firstName());
                            catUsersService.update(catUsers);
                        } else if(context.getShelterType().equals(
                                ButtonCommand.DOG.getCommand()) && update.message() != null && contact != null) {
                            DogUsers dogUsers = context.getDogUsers();
                            dogUsers.setPhone(contact.phoneNumber());
                            dogUsers.setName(contact.firstName());
                            dogUsersService.update(dogUsers);
                        }
                        sendForwardMessage(chatId, messageId);
                        sendResponseMessage(chatId, "Мы получили ваши контактные данные");

                    }
                    default -> sendResponseMessage(chatId, "Неизвестная команда!");
                }
            });
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
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
     *
     */
    public void sendResponseMessage(long chatId, String text) {
        SendMessage sendMessage = new SendMessage(chatId, text);
        SendResponse sendResponse = telegramBot.execute(sendMessage);
        if (!sendResponse.isOk()) {
            logger.error("Error during sending message: {}", sendResponse.description());
        }
    }
    /**
     * Метод контроля отчётов пользователей
     *
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






