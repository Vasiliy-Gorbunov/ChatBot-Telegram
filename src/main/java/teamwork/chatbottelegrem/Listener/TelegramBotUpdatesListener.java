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
import org.springframework.stereotype.Component;
import teamwork.chatbottelegrem.Model.CatUsers;
import teamwork.chatbottelegrem.Model.Context;
import teamwork.chatbottelegrem.Model.DogUsers;
import teamwork.chatbottelegrem.botInterface.ButtonCommand;
import teamwork.chatbottelegrem.botInterface.KeyBoard;
import teamwork.chatbottelegrem.service.CatUsersService;
import teamwork.chatbottelegrem.service.ContextService;
import teamwork.chatbottelegrem.service.DogUsersService;

import javax.annotation.PostConstruct;
import java.util.List;
/**
 * Класс для взаимодействия с телеграмм ботом
 */
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

    public TelegramBotUpdatesListener(TelegramBot telegramBot, KeyBoard keyBoard,ContextService contextService, CatUsersService catUsersService, DogUsersService dogUsersService) {
        this.telegramBot = telegramBot;
        this.keyBoard = keyBoard;
        this.contextService = contextService;
        this.catUsersService = catUsersService;
        this.dogUsersService = dogUsersService;

    }
    /**
     * Метод реализующий, комманды для виртуальной клавиатуры бота
     */
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
     * Метод реализующий логику телеграмм бота
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
                            sendResponseMessage(chatId, "Здравствуйте,"+ message.chat().firstName()+ "! " +
                                    "Я телеграм-бот приюта для животных, я помогу Вам ответить на Ваши вопросы. Выберите, пожалуйста, по какому приюту Ваш вопрос.");
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
                        sendResponseMessage(chatId, "Приют для кошек");
                        keyBoard.shelterMainMenu(chatId);
                    }
                    case DOG -> {
                        Context context = contextService.getByChatId(chatId).get();
                        if(dogUsersService.getByChatId(chatId).isEmpty()) {
                            DogUsers dogUsers = new DogUsers();
                            dogUsers.setChatId(chatId);
                            dogUsersService.save(dogUsers);
                            context.setDogUsers(dogUsers);
                        }
                        context.setShelterType(ButtonCommand.DOG.getCommand());
                        contextService.saveContext(context);
                        sendResponseMessage(chatId, "Приют для собак");
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
                                    Адрес кошачего приюта - ...
                                    График работы - ...
                                    """);
                        }
                    }
                    case VOLUNTEER -> {
                        sendResponseMessage(chatId, "Сообщение передано волонтёру");
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
                                    "Необходимые документы, для приобретения кота: ...");
                        } else if(context.getShelterType().equals(ButtonCommand.DOG.getCommand())) {
                            sendResponseMessage(chatId,
                                    "Необходимые документы, для приобретения собаки: ...");
                        }
                    }
                    case null -> {
                        Context context = contextService.getByChatId(chatId).get();
                        if(context.getShelterType().equals(
                                ButtonCommand.CAT.getCommand()) && update.message() != null && contact != null) {
                            CatUsers catUsers = context.getCatUsers();
                            catUsers.setPhone(contact.phoneNumber());
                            catUsers.setName(contact.firstName());
                            catUsersService.update(catUsers);
                        } else if(context.getShelterType().equals(
                                ButtonCommand.DOG.getCommand()) && update.message() != null && contact != null) {
                            DogUsers dogUsers = context.getDogUsers();
                            dogUsers.setPhone(contact.phoneNumber());
                            dogUsers.setName(contact.firstName());
                            dogUsersService.save(dogUsers);
                        }
                        sendForwardMessage(chatId, messageId);
                        sendResponseMessage(chatId, "Контактные данные приняты");

                    }
                    default -> sendResponseMessage(chatId, "Команда не распознанна");
                }
            });
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }
    /**
     * Метод отправки текстовых сообщений волонтёру.
     *
     */
    public void sendForwardMessage(long chatId, int messageId) {
        ForwardMessage forwardMessage = new ForwardMessage(volunteerChatId, chatId, messageId);
        SendResponse sendResponse = telegramBot.execute(forwardMessage);
        if (!sendResponse.isOk()) {
            logger.error("Error during sending message: {}", sendResponse.description());
        }
    }

    /**
     * Метод отправки текстовых сообщений.
     *
     */
    public void sendResponseMessage(long chatId, String text) {
        SendMessage sendMessage = new SendMessage(chatId, text);
        SendResponse sendResponse = telegramBot.execute(sendMessage);
        if (!sendResponse.isOk()) {
            logger.error("Error during sending message: {}", sendResponse.description());
        }
    }

}






