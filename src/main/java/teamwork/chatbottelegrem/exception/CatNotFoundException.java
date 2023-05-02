package teamwork.chatbottelegrem.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
/**
 * Ошибка отсутствия кота
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class CatNotFoundException extends RuntimeException{
    public CatNotFoundException() {
        super("Не найден кот");
    }
}