package teamwork.chatbottelegrem.model;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import javax.persistence.*;

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
        this.chatId = chatId;
    }
}