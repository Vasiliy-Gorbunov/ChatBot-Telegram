package teamwork.chatbottelegrem.DAO;

import teamwork.chatbottelegrem.Model.Users;

import java.util.List;

public interface UsersDAO {
    void create(Users users);

    Users readById(int id);
    List<Users> readAll();

    void updateEmployee(Users users);

    void delete(Users users);
}
