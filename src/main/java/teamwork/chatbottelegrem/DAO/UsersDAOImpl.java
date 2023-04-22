package teamwork.chatbottelegrem.DAO;

import org.hibernate.Session;
import org.hibernate.Transaction;
import teamwork.chatbottelegrem.Model.Users;
import teamwork.chatbottelegrem.config.HibernateSessionFactoryUtil;

import java.util.List;

public class UsersDAOImpl implements UsersDAO {
    @Override
    public void create(Users users) {
        try(Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.save(users);
            transaction.commit();
        }
    }


    @Override
    public Users readById(int id) {
        return HibernateSessionFactoryUtil.getSessionFactory().openSession().get(Users.class, id);
    }

    @Override
    public List<Users> readAll() {
        List<Users> users = HibernateSessionFactoryUtil.getSessionFactory()
                .openSession().createQuery("FROM Users ", Users.class).list();
        return users;
    }

    @Override
    public void updateEmployee(Users users) {
        try (Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.update(users);
            transaction.commit();
        }
    }

    @Override
    public void delete(Users users) {
        try(Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.delete(users);
            transaction.commit();
        }
    }
}