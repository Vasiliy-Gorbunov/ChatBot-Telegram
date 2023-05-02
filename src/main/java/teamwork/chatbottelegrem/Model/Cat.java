package teamwork.chatbottelegrem.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
/**
 * Класс кота
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cat {

    @Id
    @GeneratedValue
    private Long id;
    //id
    private String name;
    //Имя
    private String breed;
    //Порода кота
    private int yearOfBirth;
    //Год рождения
    private String info;
    // Доп.информация



    @Override
    public String toString() {
        return "Кличка: " + getName() + "Год рождения: " + getYearOfBirth() +
                "Порода: " + getBreed() + "Доп.информация:  " + getInfo();
    }
}