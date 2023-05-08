package teamwork.chatbottelegrem.Model;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Objects;

/**
 * Класс владельца собаки
 */
@Data
@Entity
@AllArgsConstructor
@Table(name = "dogUsers")
public class DogUsers {
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
    @JoinColumn(name = "dog_id")
    private Dog dog;
    //Пустой конструктор класса
    public DogUsers() {
    }
    //конструктор класса с полями name, phone, chatId.
    public DogUsers(String name, String phone, Long chatId) {
        this.name = name;
        this.number = phone;
    }

    public void setChatId(long chatId) {
    }

    public void setPhone(String phoneNumber) {
    }

    public void setName(String firstName) {
    }
}