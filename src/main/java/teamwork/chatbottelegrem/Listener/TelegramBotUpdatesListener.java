package teamwork.chatbottelegrem.Listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.*;
import com.pengrad.telegrambot.request.ForwardMessage;
import com.pengrad.telegrambot.request.GetFile;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import teamwork.chatbottelegrem.Model.CatUsers;
import teamwork.chatbottelegrem.Model.Context;
import teamwork.chatbottelegrem.Model.DogUsers;
import teamwork.chatbottelegrem.botInterface.ButtonCommand;
import teamwork.chatbottelegrem.botInterface.KeyBoard;
import teamwork.chatbottelegrem.service.CatUsersReportsService;
import teamwork.chatbottelegrem.service.CatUsersService;
import teamwork.chatbottelegrem.service.ContextService;
import teamwork.chatbottelegrem.service.DogUsersService;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;


@Component
public class TelegramBotUpdatesListener implements UpdatesListener {
    @Value("${volunteer-chat-id}")
    private Long volunteerChatId;
    @Value("${telegram.bot.token}")
    private String token;
    @Value("${data.path}")
    private String dataPath;
    private final Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

    private final TelegramBot telegramBot;
    private final KeyBoard keyBoard;
    private final CatUsersService catUsersService;
    private final DogUsersService dogUsersService;
    private final CatUsersReportsService catUsersReportsService;

    private final ContextService contextService;

    public TelegramBotUpdatesListener(TelegramBot telegramBot, KeyBoard keyBoard, ContextService contextService, CatUsersService catUsersService, DogUsersService dogUsersService, CatUsersReportsService catUsersReportsService) {
        this.telegramBot = telegramBot;
        this.keyBoard = keyBoard;
        this.contextService = contextService;
        this.catUsersService = catUsersService;
        this.dogUsersService = dogUsersService;
        this.catUsersReportsService = catUsersReportsService;
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
                        if (contextService.getByChatId(chatId).isEmpty()) {
                            sendResponseMessage(chatId, "Привет! Я могу показать информацию о приютах," +
                                    "как взять животное из приюта и принять отчет о питомце");
                            Context context = new Context();
                            context.setChatId(chatId);
                            contextService.saveContext(context);
                        }
                        keyBoard.chooseMenu(chatId);
                    }
                    case CAT -> {
                        Context context = contextService.getByChatId(chatId).get();
                        if (catUsersService.getByChatId(chatId) != null) {
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
                        if (dogUsersService.getById(chatId) != null) {
                            DogUsers dogUsers = new DogUsers();
                            dogUsers.setId(chatId);
                            dogUsersService.save(dogUsers);
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
                                    Адрес кошачего приюта - ...
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
                    case null -> {
                        Context context = contextService.getByChatId(chatId).get();
                        //Проверка на наличие фото в сообщении
                        if (message.photo() != null) {
                            //Фото получается в виде массива из 4-х одинаковых фото разного размера
                            PhotoSize[] photos = message.photo();
                            //Выбираем последнее фото из массива - оригинал
                            PhotoSize photo = photos[photos.length-1];
                            //Получаем фото в виде файла в телеграм формате
                            File file = telegramBot.execute(new GetFile(photo.fileId())).file();
                            //Скачиваем его
                            String path = saveReceivedFileToLocalDirectory(file, chatId);

                            //Захват подписи к фото
                            String tekst = message.caption();

                            //Путь хранения фото с подписью и текущей датой записывается в БД
                            catUsersReportsService.addPhoto(chatId, path, LocalDateTime.now(), tekst);


                        //Проверка на наличие документа (фото можно отправить документом, поэтому проверка необходима)
                        } else if (message.document() != null) {
                            File file = telegramBot.execute(new GetFile(message.document().fileId())).file();
                            saveReceivedFileToLocalDirectory(file, chatId);
                        } else if (context.getShelterType().equals(
                                ButtonCommand.CAT.getCommand()) && update.message() != null && contact != null) {
                            catUsersService.update(contact.firstName(), contact.phoneNumber(), chatId);
                        } else if (context.getShelterType().equals(
                                ButtonCommand.DOG.getCommand()) && update.message() != null && contact != null) {
                            DogUsers dogUsers = context.getDogUsers();
                            dogUsers.setNumber(contact.phoneNumber());
                            dogUsers.setFirstName(contact.firstName());
                            dogUsersService.save(dogUsers);
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

    /**
     * Метод для сохранения присланного боту файла в локальную директорию
     * с созданием в ней папки с chatId и названием файла в виде Даты-времени.
     * @param file (Формат File com.pengrad.telegrambot.model.File)
     */
    private String saveReceivedFileToLocalDirectory(File file, Long chatId) {
        String path = file.filePath();
        String localDateTime = LocalDateTime.now().toString();
        try {
            URL url = new URL("https://api.telegram.org/file/bot" + token + "/" + path);
            String filePath = dataPath + chatId + "/" + localDateTime;
            FileUtils.copyURLToFile(url, new java.io.File(filePath));
            return filePath;
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            return null;
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
     * Метод отправки текстовых сообщений.
     */
    public void sendResponseMessage(long chatId, String text) {
        SendMessage sendMessage = new SendMessage(chatId, text);
        SendResponse sendResponse = telegramBot.execute(sendMessage);
        if (!sendResponse.isOk()) {
            logger.error("Error during sending message: {}", sendResponse.description());
        }
    }

}






