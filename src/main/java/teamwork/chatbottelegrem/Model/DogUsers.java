package teamwork.chatbottelegrem.Model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "dog_users")
public class DogUsers {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "second_name")
    private String secondName;
    @Column(name = "number")
    private String number;

    @Override
    public String toString() {
        return "model.Employee{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + secondName + '\'' +
                ", number='" + number + '\'' +
                '}';
    }
}
