package teamwork.chatbottelegrem.Model;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Objects;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "dogUsers")
public class DogUsers {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "second_name")
    private String secondName;
    @Column(name = "number")
    private int number;
    @Override
    public String toString() {
        return "model.Employee{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + secondName + '\'' +
                ", number='" + number + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        teamwork.chatbottelegrem.Model.DogUsers dogUsers = (teamwork.chatbottelegrem.Model.DogUsers) o;
        return id == dogUsers.id && number == dogUsers.number && Objects.equals(firstName, dogUsers.firstName) && Objects.equals(secondName, dogUsers.secondName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, secondName, number);
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