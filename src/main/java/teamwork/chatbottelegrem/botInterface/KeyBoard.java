package teamwork.chatbottelegrem.botInterface;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.request.KeyboardButton;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
/**
 * Класс клавиатуры телграмм бота
 */
@Component
public class KeyBoard {
    private final Logger logger = LoggerFactory.getLogger(KeyBoard.class);
    private TelegramBot telegramBot;

    public KeyBoard(TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }

    /**
     * Метод, отображающий меню выбора
     *
     */
    public void chooseMenu(long chatId) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup(
                ButtonCommand.CAT.getCommand(), ButtonCommand.DOG.getCommand());
        sendResponseMenu(chatId, replyKeyboardMarkup, "Выберите приют в меню ниже.");
    }

    /**
     * Метод, отображающий главное меню приюта
     *
     */
    public void shelterMainMenu(long chatId) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup(
                new String[]{ButtonCommand.SHELTER_INFO_MENU.getCommand(), ButtonCommand.HOW_ADOPT_PET_INFO.getCommand()},
                new String[]{"Прислать отчет о питомце", ButtonCommand.VOLUNTEER.getCommand()});
        sendResponseMenu(chatId, replyKeyboardMarkup, "Ниже представлено главное меню приюта.");
    }

    /**
     * Метод, отображающий меню с основной информацией
     *
     */
    public void shelterInfoMenu(long chatId) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup(
                ButtonCommand.SHELTER_INFO.getCommand(),
                ButtonCommand.SHELTER_ADDRESS_SCHEDULE.getCommand());
        replyKeyboardMarkup.addRow(new KeyboardButton(ButtonCommand.VOLUNTEER.getCommand()),
                new KeyboardButton(ButtonCommand.SEND_CONTACT.getCommand()).requestContact(true));
        replyKeyboardMarkup.addRow(ButtonCommand.MAIN_MENU.getCommand());
        sendResponseMenu(chatId, replyKeyboardMarkup, "Вы можете получить информацию о приюте в меню.");
    }

    /**
     * Метод, отображающий меню, с информацией о том, как взять питомца из приюта
     *
     */
    public void shelterInfoHowAdoptPetMenu(long chatId) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup(ButtonCommand.RECOMMENDATIONS_LIST.getCommand(),
                ButtonCommand.DOCUMENTS_LIST.getCommand());
        replyKeyboardMarkup.addRow(new KeyboardButton(ButtonCommand.VOLUNTEER.getCommand()),
                new KeyboardButton(ButtonCommand.SEND_CONTACT.getCommand()).requestContact(true));
        replyKeyboardMarkup.addRow(ButtonCommand.MAIN_MENU.getCommand());
        sendResponseMenu(chatId, replyKeyboardMarkup, "Информация о том, как взять животное из приюта");
    }

    /**
     * Метод, принимающий сообщение, и отвечающий на него
     *
     */
    public void sendResponseMenu(long chatId, ReplyKeyboardMarkup replyKeyboardMarkup, String text) {
        SendMessage sendMessage = new SendMessage(
                chatId, text).replyMarkup(replyKeyboardMarkup.resizeKeyboard(true));
        SendResponse sendResponse = telegramBot.execute(sendMessage);
        if (!sendResponse.isOk()) {
            logger.error("Error during sending message: {}", sendResponse.description());
        }
    }
}