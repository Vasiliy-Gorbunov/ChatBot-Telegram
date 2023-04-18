package teamwork.chatbottelegrem.Listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.model.request.Keyboard;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import jakarta.annotation.PostConstruct;
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
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    @Override
    public int process(List<Update> updates) {
        try {
            updates.forEach(update -> {
                logger.info("Handles update: {}", update);
                Long chatId = checkChatId(update);
                String messageText = checkMessage(update);
                startTalk(messageText, chatId, update);

//                int chooseShelter = startTalk(messageText, chatId, update);
                switch (messageText) {
                    case "Приют для кошек":
                        mainMenuCat(chatId);
                        break;
                    case "Приют для собак":
                        mainMenuDog(chatId);
                        break;
                    case "Узнать информацию о приюте/dog":
                        infoAboutShelterDog(chatId);
                        break;
                    case "Узнать информацию о приюте/cat":
                        infoAboutShelterCat(chatId);
                    case "Как взять животное из приюта/dog":
                        infoHowTakeAnimalFromShelterDog(chatId);
                    case "Как взять животное из приюта/cat":
                        infoHowTakeAnimalFromShelterCat(chatId);
                }
//                if (chooseShelter == 1 || chooseShelter == 2) {
//
//
////                }
//
//                InlineKeyboardButton shelterForCat = new InlineKeyboardButton("Приют для кошек");
//                shelterForCat.callbackData("Приют для кошек");
//                InlineKeyboardButton shelterForDog = new InlineKeyboardButton("Приют для собак");
//                shelterForDog.callbackData("Приют для собак");
//                Keyboard keyboard = new InlineKeyboardMarkup(shelterForCat, shelterForDog);
//                SendMessage sendMessage = new SendMessage(chatId, "");
//                sendMessage.replyMarkup(keyboard);
//
//                SendResponse sendResponse = telegramBot.execute(sendMessage);
//                if (!sendResponse.isOk()) {
//                    logger.error("Error during sending message: {}", sendResponse.description());
//                }
//
//                case "Приют для кошек":
//                case "Приют для собак":
//                    InlineKeyboardButton FindOutInformationAboutShelter = new InlineKeyboardButton("Узнать информацию о приюте");
//                    FindOutInformationAboutShelter.callbackData("Узнать информацию о приюте");
//                    InlineKeyboardButton HowTakeAnimalFromShelter = new InlineKeyboardButton("Как взять животное из приюта");
//                    HowTakeAnimalFromShelter.callbackData("Как взять животное из приюта");
//                    InlineKeyboardButton SendPetReport = new InlineKeyboardButton("Прислать отчет о питомце");
//                    SendPetReport.callbackData("Прислать отчет о питомце");
//                    InlineKeyboardButton CallVolunteer = new InlineKeyboardButton("Позвать волонтера");
//                    CallVolunteer.callbackData("Позвать волонтера");
//                    Keyboard keyboard1 = new InlineKeyboardMarkup(FindOutInformationAboutShelter, HowTakeAnimalFromShelter, SendPetReport, CallVolunteer);
//                    SendMessage sendMessage1 = new SendMessage(chatId, "");
//                    sendMessage1.replyMarkup(keyboard1);
//                    SendResponse sendResponse1 = telegramBot.execute(sendMessage1);
//                    if (!sendResponse1.isOk()) {
//                        logger.error("Error during sending message: {}", sendResponse1.description());
//                    }
//                    break;

            });

        } catch (
                Exception e) {
            logger.error(e.getMessage(), e);
        }
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }


    private void sendMsg(Long chatId, String message) {
        SendMessage sendMessage = new SendMessage(chatId, message);
        SendResponse sendResponse = telegramBot.execute(sendMessage);
        if (!sendResponse.isOk()) {
            logger.error("Error during sending message: {}", sendResponse.description());
        }

    }

    private Long checkChatId(Update update) {
        if (update.callbackQuery() != null) {
            return update.callbackQuery().message().chat().id();
        } else {
            return update.message().chat().id();
        }

    }

    private String checkMessage(Update update) {
        if (update.callbackQuery() != null) {
            return update.callbackQuery().data();
        } else {
            return update.message().text();
        }
    }

    private void startTalk(String messageText, Long chatId, Update update) {

        if ("/start".equals(messageText)) {

            SendMessage sendMessage = new SendMessage(chatId, "Здравствуйте," + update.message().chat().firstName() + "! " +
                    "я телеграм-бот приюта для животных, я помогу Вам ответить на Ваши вопросы. Выберите, пожалуйста, по какому приюту Ваш вопрос.");

            InlineKeyboardButton shelterForCat = new InlineKeyboardButton("Приют для кошек");
            shelterForCat.callbackData("Приют для кошек");

            InlineKeyboardButton shelterForDog = new InlineKeyboardButton("Приют для собак");
            shelterForDog.callbackData("Приют для собак");
            Keyboard keyboard = new InlineKeyboardMarkup(shelterForCat, shelterForDog);
            sendMessage.replyMarkup(keyboard);
            SendResponse sendResponse = telegramBot.execute(sendMessage);
            if (!sendResponse.isOk()) {
                logger.error("Error during sending message: {}", sendResponse.description());
            }
        }
    }

    private void mainMenuDog(Long chatId) {
        InlineKeyboardButton findOutInformationAboutShelter = new InlineKeyboardButton("Узнать информацию о приюте");
        findOutInformationAboutShelter.callbackData("Узнать информацию о приюте/dog");
        InlineKeyboardButton howTakeAnimalFromShelter = new InlineKeyboardButton("Как взять животное из приюта");
        howTakeAnimalFromShelter.callbackData("Как взять животное из приюта/dog");
        InlineKeyboardButton sendPetReport = new InlineKeyboardButton("Прислать отчет о питомце/dog");
        sendPetReport.callbackData("Прислать отчет о питомце");
        InlineKeyboardButton callVolunteer = new InlineKeyboardButton("Позвать волонтера");
        callVolunteer.callbackData("Позвать волонтера");
        InlineKeyboardButton back = new InlineKeyboardButton("Назад");
        back.callbackData("Назад/выбор приюта");
        Keyboard keyboard = new InlineKeyboardMarkup(findOutInformationAboutShelter, howTakeAnimalFromShelter, sendPetReport, callVolunteer, back);

        SendMessage sendMessage = new SendMessage(chatId, "Выберите интересующий Вас пункт меню");
        sendMessage.replyMarkup(keyboard);

        SendResponse sendResponse1 = telegramBot.execute(sendMessage);
        if (!sendResponse1.isOk()) {
            logger.error("Error during sending message: {}", sendResponse1.description());
        }
    }

    private void mainMenuCat(Long chatId) {
        InlineKeyboardButton findOutInformationAboutShelter = new InlineKeyboardButton("Узнать информацию о приюте");
        findOutInformationAboutShelter.callbackData("Узнать информацию о приюте/cat");
        InlineKeyboardButton howTakeAnimalFromShelter = new InlineKeyboardButton("Как взять животное из приюта");
        howTakeAnimalFromShelter.callbackData("Как взять животное из приюта/cat");
        InlineKeyboardButton sendPetReport = new InlineKeyboardButton("Прислать отчет о питомце/cat");
        sendPetReport.callbackData("Прислать отчет о питомце");
        InlineKeyboardButton callVolunteer = new InlineKeyboardButton("Позвать волонтера");
        callVolunteer.callbackData("Позвать волонтера");
        InlineKeyboardButton back = new InlineKeyboardButton("Назад");
        back.callbackData("Назад/выбор приюта");
        Keyboard keyboard = new InlineKeyboardMarkup(findOutInformationAboutShelter, howTakeAnimalFromShelter, sendPetReport, callVolunteer, back);
        SendMessage sendMessage = new SendMessage(chatId, "Выберите интересующий Вас пункт меню");
        sendMessage.replyMarkup(keyboard);

        SendResponse sendResponse1 = telegramBot.execute(sendMessage);
        if (!sendResponse1.isOk()) {
            logger.error("Error during sending message: {}", sendResponse1.description());
        }
    }

    private void infoAboutShelterDog(Long chatId) {
        InlineKeyboardButton getAddressScheduleAndDrivingDirections =
                new InlineKeyboardButton("Узнать расписание работы приюта, адрес и схему проезда");
        getAddressScheduleAndDrivingDirections
                .callbackData("Узнать расписание работы приюта, адрес и схему проезда/dog");
        InlineKeyboardButton getSecurityContactDetailsForRegistrationOfCarPass =
                new InlineKeyboardButton("Узнать контактные данные охраны для оформления пропуска на машину");
        getSecurityContactDetailsForRegistrationOfCarPass
                .callbackData("Узнать контактные данные охраны для оформления пропуска на машину/dog");
        InlineKeyboardButton getGeneralSafetyRecommendationsOnTerritoryFOfShelter =
                new InlineKeyboardButton("Узнать общие рекомендации о технике безопасности на территории приюта");
        getGeneralSafetyRecommendationsOnTerritoryFOfShelter
                .callbackData("Узнать общие рекомендации о технике безопасности на территории приюта/dog");
        InlineKeyboardButton sendContactDetailsForCommunication =
                new InlineKeyboardButton("отправить контактные данные для связи");
        sendContactDetailsForCommunication
                .callbackData("отправить контактные данные для связи/dog");
        InlineKeyboardButton anotherQuestionOrCallVolunteer =
                new InlineKeyboardButton("Другие вопросы / позвать волонтера");
        anotherQuestionOrCallVolunteer
                .callbackData("Другие вопросы / позвать волонтера/dog");
        InlineKeyboardButton back =
                new InlineKeyboardButton("Назад");
        back
                .callbackData("mainMenuDog");
        Keyboard keyboard = new InlineKeyboardMarkup(getAddressScheduleAndDrivingDirections,
                getSecurityContactDetailsForRegistrationOfCarPass, getGeneralSafetyRecommendationsOnTerritoryFOfShelter,
                sendContactDetailsForCommunication, anotherQuestionOrCallVolunteer, back);
        SendMessage sendMessage = new SendMessage(chatId, "Выберите интересующий Вас пункт меню");
        sendMessage.replyMarkup(keyboard);

        SendResponse sendResponse1 = telegramBot.execute(sendMessage);
        if (!sendResponse1.isOk()) {
            logger.error("Error during sending message: {}", sendResponse1.description());
        }



    }

    private void infoAboutShelterCat(Long chatId) {
        InlineKeyboardButton getAddressScheduleAndDrivingDirections =
                new InlineKeyboardButton("Узнать расписание работы приюта, адрес и схему проезда");
        getAddressScheduleAndDrivingDirections
                .callbackData("Узнать расписание работы приюта, адрес и схему проезда/cat");
        InlineKeyboardButton getSecurityContactDetailsForRegistrationOfCarPass =
                new InlineKeyboardButton("Узнать контактные данные охраны для оформления пропуска на машину");
        getSecurityContactDetailsForRegistrationOfCarPass
                .callbackData("Узнать контактные данные охраны для оформления пропуска на машину/cat");
        InlineKeyboardButton getGeneralSafetyRecommendationsOnTerritoryFOfShelter =
                new InlineKeyboardButton("Узнать общие рекомендации о технике безопасности на территории приюта");
        getGeneralSafetyRecommendationsOnTerritoryFOfShelter
                .callbackData("Узнать общие рекомендации о технике безопасности на территории приюта/cat");
        InlineKeyboardButton sendContactDetailsForCommunication =
                new InlineKeyboardButton("отправить контактные данные для связи");
        sendContactDetailsForCommunication
                .callbackData("отправить контактные данные для связи/cat");
        InlineKeyboardButton anotherQuestionOrCallVolunteer =
                new InlineKeyboardButton("Другие вопросы / позвать волонтера");
        anotherQuestionOrCallVolunteer
                .callbackData("Другие вопросы / позвать волонтера/cat");
        InlineKeyboardButton back =
                new InlineKeyboardButton("Назад");
        back
                .callbackData("mainMenuCat");
        Keyboard keyboard = new InlineKeyboardMarkup(getAddressScheduleAndDrivingDirections,
                getSecurityContactDetailsForRegistrationOfCarPass, getGeneralSafetyRecommendationsOnTerritoryFOfShelter,
                sendContactDetailsForCommunication, anotherQuestionOrCallVolunteer, back);
        SendMessage sendMessage = new SendMessage(chatId, "Выберите интересующий Вас пункт меню");
        sendMessage.replyMarkup(keyboard);

        SendResponse sendResponse1 = telegramBot.execute(sendMessage);
        if (!sendResponse1.isOk()) {
            logger.error("Error during sending message: {}", sendResponse1.description());

    }}

    private void infoHowTakeAnimalFromShelterDog(Long chatId) {
        InlineKeyboardButton getRulesForGettingToKnowAnimalBeforePickingItUpFromShelter =
                new InlineKeyboardButton("Получить правила знакомства с животным до того, как забрать его из приюта");
        getRulesForGettingToKnowAnimalBeforePickingItUpFromShelter
                .callbackData("Получить правила знакомства с животным до того, как забрать его из приюта/dog");

        InlineKeyboardButton getListOfDocumentsRequiredToTakeAnimalFromShelter =
                new InlineKeyboardButton(
                        "Получить список документов, необходимых для того, чтобы взять животное из приюта");
        getListOfDocumentsRequiredToTakeAnimalFromShelter
                .callbackData("Получить список документов, необходимых для того, чтобы взять животное из приюта/dog");

        InlineKeyboardButton getListOfRecommendationsForTransportingAnimal =
                new InlineKeyboardButton("Получить список рекомендаций по транспортировке животного");
        getListOfRecommendationsForTransportingAnimal
                .callbackData("список рекомендаций по транспортировке животного/dog");

        InlineKeyboardButton getListOfRecommendationsForHomeImprovementForPuppy =
                new InlineKeyboardButton("Получить список рекомендаций по обустройству дома для щенка");
        getListOfRecommendationsForHomeImprovementForPuppy
                .callbackData("Получить список рекомендаций по обустройству дома для щенка/dog");

        InlineKeyboardButton getListOfRecommendationsForHomeImprovementForAdultAnimal =
                new InlineKeyboardButton("Получить список рекомендаций по обустройству дома для взрослого животного");
        getListOfRecommendationsForHomeImprovementForAdultAnimal
                .callbackData("Получить список рекомендаций по обустройству дома для взрослого животного/dog");

        InlineKeyboardButton getListOfRecommendationsForHomeImprovementForAnimalWithDisabilities =
                new InlineKeyboardButton
                        ("Получить список рекомендаций по обустройству дома для животного " +
                                "с ограниченными возможностями (зрение, передвижение)");
        getListOfRecommendationsForHomeImprovementForAnimalWithDisabilities
                .callbackData("Получить список рекомендаций по обустройству дома для животного " +
                        "с ограниченными возможностями (зрение, передвижение)/dog");

        InlineKeyboardButton getTipsFromDogHandlerPrimaryCommunicationWithDog =
                new InlineKeyboardButton("Получить советы кинолога по первичному общению с собакой");
        getTipsFromDogHandlerPrimaryCommunicationWithDog
                .callbackData("Получить советы кинолога по первичному общению с собакой/cat");


        InlineKeyboardButton getRecommendationsOnProvenDogHandlersForFurtherReferenceToThem=
                new InlineKeyboardButton("получить рекомендации по проверенным кинологом для дальнейшего обращения к ним");
        getRecommendationsOnProvenDogHandlersForFurtherReferenceToThem
                .callbackData("получить рекомендации по проверенным кинологом для дальнейшего обращения к ним/dog");

        InlineKeyboardButton getListOfReasonsWhyTheyMayRefuseAndNotLetYouTakeTheDogFromTheShelter =
                new InlineKeyboardButton("получить список причин, почему могут отказать и не дать забрать собаку из приюта. ");
        getListOfReasonsWhyTheyMayRefuseAndNotLetYouTakeTheDogFromTheShelter
                .callbackData("Получить список причин, почему могут отказать и не дать забрать собаку из приюта. /dog");

        InlineKeyboardButton sendContactDetailsForCommunication =
                new InlineKeyboardButton("отправить контактные данные для связи");
        sendContactDetailsForCommunication
                .callbackData("отправить контактные данные для связи/dog");

        InlineKeyboardButton anotherQuestionOrCallVolunteer =
        new InlineKeyboardButton("Другие вопросы / позвать волонтера");
        anotherQuestionOrCallVolunteer
                .callbackData("Другие вопросы / позвать волонтера/dog");
        InlineKeyboardButton back =
                new InlineKeyboardButton("Назад");
        back
                .callbackData("mainMenuDog");
        Keyboard keyboard = new InlineKeyboardMarkup(getRulesForGettingToKnowAnimalBeforePickingItUpFromShelter,
                getListOfDocumentsRequiredToTakeAnimalFromShelter, getListOfRecommendationsForTransportingAnimal,
                getListOfRecommendationsForHomeImprovementForPuppy, getListOfRecommendationsForHomeImprovementForAdultAnimal,
                getListOfRecommendationsForHomeImprovementForAnimalWithDisabilities, getTipsFromDogHandlerPrimaryCommunicationWithDog,
                getRecommendationsOnProvenDogHandlersForFurtherReferenceToThem,
                getListOfReasonsWhyTheyMayRefuseAndNotLetYouTakeTheDogFromTheShelter,

                sendContactDetailsForCommunication, anotherQuestionOrCallVolunteer, back);
        SendMessage sendMessage = new SendMessage(chatId, "Выберите интересующий Вас пункт меню");
        sendMessage.replyMarkup(keyboard);

        SendResponse sendResponse1 = telegramBot.execute(sendMessage);
        if (!sendResponse1.isOk()) {
            logger.error("Error during sending message: {}", sendResponse1.description());


        }
        }
    private void infoHowTakeAnimalFromShelterCat(Long chatId) {
        InlineKeyboardButton getRulesForGettingToKnowAnimalBeforePickingItUpFromShelter =
                new InlineKeyboardButton("Получить правила знакомства с животным до того, как забрать его из приюта");
        getRulesForGettingToKnowAnimalBeforePickingItUpFromShelter
                .callbackData("Получить правила знакомства с животным до того, как забрать его из приюта/cat");

        InlineKeyboardButton getListOfDocumentsRequiredToTakeAnimalFromShelter =
                new InlineKeyboardButton(
                        "Получить список документов, необходимых для того, чтобы взять животное из приюта");
        getListOfDocumentsRequiredToTakeAnimalFromShelter
                .callbackData("Получить список документов, необходимых для того, чтобы взять животное из приюта/cat");

        InlineKeyboardButton getListOfRecommendationsForTransportingAnimal =
                new InlineKeyboardButton("Получить список рекомендаций по транспортировке животного");
        getListOfRecommendationsForTransportingAnimal
                .callbackData("список рекомендаций по транспортировке животного/cat");

        InlineKeyboardButton getListOfRecommendationsForHomeImprovementForKitten =
                new InlineKeyboardButton("Получить список рекомендаций по обустройству дома для котенка");
        getListOfRecommendationsForHomeImprovementForKitten
                .callbackData("Получить список рекомендаций по обустройству дома для котенка/cat");

        InlineKeyboardButton getListOfRecommendationsForHomeImprovementForAdultAnimal =
                new InlineKeyboardButton("Получить список рекомендаций по обустройству дома для взрослого животного");
        getListOfRecommendationsForHomeImprovementForAdultAnimal
                .callbackData("Получить список рекомендаций по обустройству дома для взрослого животного/cat");

        InlineKeyboardButton getListOfRecommendationsForHomeImprovementForAnimalWithDisabilities =
                new InlineKeyboardButton
                        ("Получить список рекомендаций по обустройству дома для животного " +
                                "с ограниченными возможностями (зрение, передвижение)");
        getListOfRecommendationsForHomeImprovementForAnimalWithDisabilities
                .callbackData("Получить список рекомендаций по обустройству дома для животного " +
                        "с ограниченными возможностями (зрение, передвижение)/cat");

        InlineKeyboardButton getListOfReasonsWhyTheyMayRefuseAndNotLetYouTakeTheDogFromTheShelter =
                new InlineKeyboardButton("получить список причин, почему могут отказать и не дать забрать собаку из приюта. ");
        getListOfReasonsWhyTheyMayRefuseAndNotLetYouTakeTheDogFromTheShelter
                .callbackData("Получить список причин, почему могут отказать и не дать забрать кошку из приюта. /cat");

        InlineKeyboardButton sendContactDetailsForCommunication =
                new InlineKeyboardButton("отправить контактные данные для связи");
        sendContactDetailsForCommunication
                .callbackData("отправить контактные данные для связи/cat");

        InlineKeyboardButton anotherQuestionOrCallVolunteer =
                new InlineKeyboardButton("Другие вопросы / позвать волонтера");
        anotherQuestionOrCallVolunteer
                .callbackData("Другие вопросы / позвать волонтера/cat");
        InlineKeyboardButton back =
                new InlineKeyboardButton("Назад");
        back
                .callbackData("mainMenuCat");
        Keyboard keyboard = new InlineKeyboardMarkup(getRulesForGettingToKnowAnimalBeforePickingItUpFromShelter,
                getListOfDocumentsRequiredToTakeAnimalFromShelter, getListOfRecommendationsForTransportingAnimal,
                getListOfRecommendationsForHomeImprovementForKitten, getListOfRecommendationsForHomeImprovementForAdultAnimal,
                getListOfRecommendationsForHomeImprovementForAnimalWithDisabilities,
                getListOfReasonsWhyTheyMayRefuseAndNotLetYouTakeTheDogFromTheShelter,

                sendContactDetailsForCommunication, anotherQuestionOrCallVolunteer, back);
        SendMessage sendMessage = new SendMessage(chatId, "Выберите интересующий Вас пункт меню");
        sendMessage.replyMarkup(keyboard);

        SendResponse sendResponse1 = telegramBot.execute(sendMessage);
        if (!sendResponse1.isOk()) {
            logger.error("Error during sending message: {}", sendResponse1.description());


        }
    }
}





