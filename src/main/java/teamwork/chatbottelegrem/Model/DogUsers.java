package teamwork.chatbottelegrem.Model;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Objects;

/**
 * Класс владельца собаки
 */
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "dogUsers")
public class DogUsers {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "name")
    private String name;
    @Column(name = "yearOfBirth")
    private String yearOfBirth;
    @Column(name = "number")
    private int number;
    @Override
    public String toString() {
        return "model.Employee{" +
                "id=" + id +
                ", firstName='" + name + '\'' +
                ", number='" + number + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        teamwork.chatbottelegrem.Model.DogUsers dogUsers = (teamwork.chatbottelegrem.Model.DogUsers) o;
        return id == dogUsers.id && number == dogUsers.number && Objects.equals(name, dogUsers.name) && Objects.equals(yearOfBirth, dogUsers.yearOfBirth);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, yearOfBirth, number);
    }

    public void setChatId(long chatId) {
    }

    public boolean isEmpty() {
        return false;
    }

    public void setPhone(String phoneNumber) {
    }

    public void setName(String firstName) {
    }
}