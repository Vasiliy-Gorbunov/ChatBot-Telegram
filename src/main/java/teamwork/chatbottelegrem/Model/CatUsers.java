package teamwork.chatbottelegrem.Model;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Objects;

@Data
@Entity
@Table(name = "catUsers")
public class CatUsers {
    @Id
    @GeneratedValue
    //id пользователя
    private Long id;
    //name пользователя
    private String name;
    //yearOfBirth год рождения пользователя
    private int yearOfBirth;
    //phone телефон пользователя
    private String phone;
    //mail електроная почта пользователя
    private String mail;
    //adвress пользователя
    private String address;
    //chatId номер чата пользователя с ботом
    private Long chatId;
    //status пользователя
    @JsonBackReference
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cat_id")
    private Cat cat;
    //Пустой конструктор класса
    public CatUsers() {
    }
    //конструктор класса с полями name, phone, chatId.
    public CatUsers(String name, String phone, Long chatId) {
        this.name = name;
        this.phone = phone;
        this.chatId = chatId;
    }
    //Конструктор класса со всеми полями.
    public CatUsers(Long id, String name, int yearOfBirth, String phone, String mail, String address, Long chatId) {
        this.id = id;
        this.name = name;
        this.yearOfBirth = yearOfBirth;
        this.phone = phone;
        this.mail = mail;
        this.address = address;
        this.chatId = chatId;
    }
}