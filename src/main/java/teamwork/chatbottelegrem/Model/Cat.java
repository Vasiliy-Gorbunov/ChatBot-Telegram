package teamwork.chatbottelegrem.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "cat")
public class Cat {

    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false)
    private Long id;
    //id
    @Column(name = "name", nullable = false)
    private String name;
    //Имя
    @Column(name = "breed", nullable = false)
    private String breed;
    //Порода к
    @Column(name = "year_of_birth", nullable = false)
    private int yearOfBirth;
    //Год рождения
    @Column(name = "info", nullable = false)
    private String info;
    // Доп.информация



    @Override
    public String toString() {
        return "Кличка: " + getName() + "Год рождения: " + getYearOfBirth() +
                "Порода: " + getBreed() + "Доп.информация:  " + getInfo();
    }
}