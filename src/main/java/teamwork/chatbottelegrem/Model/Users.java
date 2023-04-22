package teamwork.chatbottelegrem.Model;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Objects;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class Users {
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
        teamwork.chatbottelegrem.Model.Users users = (teamwork.chatbottelegrem.Model.Users) o;
        return id == users.id && number == users.number && Objects.equals(firstName, users.firstName) && Objects.equals(secondName, users.secondName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, secondName, number);
    }

}