package teamwork.chatbottelegrem.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class DogUserNotFoundException extends RuntimeException {
    public DogUserNotFoundException() {
        super("Владелец собаки не найден");
    }
}
