package teamwork.chatbottelegrem.Listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.model.request.Keyboard;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TelegramBotUpdatesListener implements UpdatesListener {

    private final Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

    private final TelegramBot telegramBot;

    public TelegramBotUpdatesListener(TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }
    @PostConstruct
    public void init(){
        telegramBot.setUpdatesListener(this);
    }

    @Override
    public int process(List<Update> updates) {
        try {
            updates.forEach(update -> {
                logger.info("Handles update: {}", update);
                Message message = update.message();
                Long chatId = message.chat().id();
                String text = message.text();
                switch (text){
                    case "/start":
//                    if (message.newChatMembers() == null)
                        SendMessage sendMessage = new SendMessage(chatId,"Здравствуйте,"+ message.chat().firstName()+ "! " +
                                "я телеграм-бот приюта для животных, я помогу Вам ответить на Ваши вопросы. Выберите, пожалуйста, по какому приюту Ваш вопрос.");

                    InlineKeyboardButton shelterForCat = new InlineKeyboardButton("Приют для кошек");
                    shelterForCat.callbackData("Приют для кошек");
                    InlineKeyboardButton shelterForDog = new InlineKeyboardButton("Приют для собак");
                    shelterForDog.callbackData("Приют для собак");
                    Keyboard keyboard = new InlineKeyboardMarkup(shelterForCat, shelterForDog);
//                    SendMessage sendMessage = new SendMessage(chatId,"");
                    sendMessage.replyMarkup(keyboard);

                    SendResponse sendResponse = telegramBot.execute(sendMessage);
                        if(!sendResponse.isOk()){
                            logger.error("Error during sending message: {}",sendResponse.description());
                        }

                        case "Приют для кошек":
                        case "Приют для собак":
                            InlineKeyboardButton FindOutInformationAboutShelter = new InlineKeyboardButton("Узнать информацию о приюте");
                            FindOutInformationAboutShelter.callbackData("Узнать информацию о приюте");
                            InlineKeyboardButton HowTakeAnimalFromShelter = new InlineKeyboardButton("Как взять животное из приюта");
                            HowTakeAnimalFromShelter.callbackData("Как взять животное из приюта");
                            InlineKeyboardButton SendPetReport = new InlineKeyboardButton("Прислать отчет о питомце");
                            SendPetReport.callbackData("Прислать отчет о питомце");
                            InlineKeyboardButton CallVolunteer = new InlineKeyboardButton("Позвать волонтера");
                            CallVolunteer.callbackData("Позвать волонтера");
                            Keyboard keyboard1 = new InlineKeyboardMarkup(FindOutInformationAboutShelter, HowTakeAnimalFromShelter, SendPetReport, CallVolunteer);
                            SendMessage sendMessage1 = new SendMessage(chatId,"");
                            sendMessage1.replyMarkup(keyboard1);
                            SendResponse sendResponse1 = telegramBot.execute(sendMessage1);
                            if(!sendResponse1.isOk()){
                                logger.error("Error during sending message: {}",sendResponse1.description());
                            }
                            break;

                    }




            });
        }catch (Exception e){
            logger.error(e.getMessage(),e);
        }
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

}
