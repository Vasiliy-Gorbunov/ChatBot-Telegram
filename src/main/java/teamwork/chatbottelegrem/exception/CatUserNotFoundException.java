package teamwork.chatbottelegrem.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
/**
 * Ошибка отстутсвия владельца кота
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class CatUserNotFoundException extends RuntimeException {
    public CatUserNotFoundException() {
        super("Владелец кота не найден");
    }
}
