package teamwork.chatbottelegrem.Model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "cat_users")
public class CatUsers {
    //chatId номер чата пользователя с ботом
    @Id
    @Column(name = "chat_id")
    private Long chatId;
    //name пользователя
    private String name;
    //yearOfBirth год рождения пользователя
    @Column(name = "year_of_birth")
    private int yearOfBirth;
    //phone телефон пользователя
    private String phone;
    //mail електроная почта пользователя
    private String mail;
    //address пользователя
    private String address;
    @Column(name = "last_report")
    private LocalDateTime lastReport;
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
    public CatUsers(String name, int yearOfBirth, String phone, String mail, String address, Long chatId) {
        this.name = name;
        this.yearOfBirth = yearOfBirth;
        this.phone = phone;
        this.mail = mail;
        this.address = address;
        this.chatId = chatId;
    }
}