package teamwork.chatbottelegrem.Model;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Objects;
/**
 * Класс владельца кота
 */
@Data
@Entity
@Table(name = "catUsers")
public class CatUsers {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name="yearOfBirth")
    private int yearOfBirth;
    @Column(name = "number")
    private String number;
    @Column(name = "chat_id")
    private Long chatId;
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
        this.number = phone;
    }
}