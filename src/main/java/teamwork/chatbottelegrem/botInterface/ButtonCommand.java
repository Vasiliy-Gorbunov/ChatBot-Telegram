package teamwork.chatbottelegrem.botInterface;
/**
 * Класс для констант, необходимых для реализации клавиатуры приложения
 */
public enum ButtonCommand {

    START("/start"),
    CAT("Приют кошек"),
    DOG("Приют собак"),
    MAIN_MENU("Главное меню"),
    VOLUNTEER("Обратиться к волонтёру"),
    SHELTER_INFO_MENU("Узнать информацию о приюте"),
    HOW_ADOPT_PET_INFO("Как забрать животное"),
    RECOMMENDATIONS_LIST("Рекомендации"),
    DOCUMENTS_LIST("Требуемые документы"),
    SHELTER_INFO("Общая информация"),
    SHELTER_ADDRESS_SCHEDULE("Информация о работе приюта"),
    SEND_CONTACT("Отправить контактные данные");


    private final String command;

    ButtonCommand(String command) {
        this.command = command;
    }

    public String getCommand() {
        return command;
    }
}