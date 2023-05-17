package teamwork.chatbottelegrem.Model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
/**
 * Класс контекста для действий пользователя бота
 */
@Entity
@Data
@NoArgsConstructor
public class Context {
    @Id
    private Long chatId;
    private String shelterType;
    @OneToOne
    CatUsers catUser;
    @OneToOne
    DogUsers dogUser;

    public Context(Long chatId, String shelterType) {
        this.chatId = chatId;
        this.shelterType = shelterType;
    }

    public Context(Long chatId, CatUsers catUsers) {
        this.chatId = chatId;
        this.catUser = catUsers;
    }

    public Context(Long chatId, DogUsers dogUsers) {
        this.chatId = chatId;
        this.dogUser = dogUsers;
    }

    public void setCatUsers(CatUsers catUsers) {
    }

    public void setDogUsers(DogUsers dogUsers) {
    }

    public CatUsers getCatUsers() {
        return catUser;
    }

    public DogUsers getDogUsers() {
        return dogUser;
    }
}
