package teamwork.chatbottelegrem.Model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;

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

    public void setCatUsers(CatUsers catUsers) {
    }

    public void setDogUsers(DogUsers dogUsers) {
    }

    public CatUsers getCatUsers() {
        return null;
    }

    public DogUsers getDogUsers() {
        return null;
    }
}
