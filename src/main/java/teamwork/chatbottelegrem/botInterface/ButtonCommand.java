package teamwork.chatbottelegrem.botInterface;

public enum ButtonCommand {

    START("/start"),
    CAT("Приют кошек"),
    DOG("Приют собак"),
    MAIN_MENU("Главное меню"),
    SHELTER_INFO_MENU("Узнать информацию о приюте"),
    HOW_ADOPT_PET_INFO("Как взять животного из приюта"),
    RECOMMENDATIONS_LIST("Список рекомендаций и советов"),
    DOCUMENTS_LIST("Требуемые документы"),
    VOLUNTEER("Связь с волонтером"),
    SHELTER_INFO("Общая информация"),
    SHELTER_ADDRESS_SCHEDULE("Адрес и график работы приюта"),
    SEND_CONTACT("Отправить контактные данные"),
    GET_REPORT_FORM("Получить форму отчета о питомце"),
    SEND_PET_REPORT("Прислать отчет о питомце"),
    BAD_REPORT_NOTIFICATION ("/bad_report_notification"),
    SUCCESS_CONGRATULATION("success_congratulations"),
    ADDITIONAL_PERIOD_14("/additional_period_14"),
    ADDITIONAL_PERIOD_30("/additional_period_30"),
    ADOPTION_REFUSE("/adoption_refuse");

    private final String command;

    ButtonCommand(String command) {
        this.command = command;
    }

    public String getCommand() {
        return command;
    }
}