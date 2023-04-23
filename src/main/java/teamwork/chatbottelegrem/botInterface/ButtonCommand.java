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
    SEND_CONTACT("Отправить контактные данные");


    private final String command;

    ButtonCommand(String command) {
        this.command = command;
    }

    public String getCommand() {
        return command;
    }
}