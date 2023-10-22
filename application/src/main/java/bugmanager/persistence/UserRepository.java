package bugmanager.persistence;

import bugmanager.model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.List;

public class UserRepository {
    private final SessionFactory sessionFactory;

    public UserRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public boolean addUser(User user) {
        boolean added = false;
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                session.persist(user);
                tx.commit();
                added = true;
            } catch (RuntimeException e) {
                e.printStackTrace();
                if (tx != null)
                    tx.rollback();
            }
        }
        return added;
    }

    public User getUserByUsername(String username) {
        User user = null;
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = null;
            try {
                transaction = session.beginTransaction();
                user = session.get(User.class, username);
                transaction.commit();
            } catch (RuntimeException e) {
                e.printStackTrace();
                if (transaction != null)
                    transaction.rollback();
            }
        }
        return user;
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = null;
            try {
                transaction = session.beginTransaction();
                users = session.createQuery("from User as u", User.class)
                        .list();
                transaction.commit();
            } catch (RuntimeException e) {
                e.printStackTrace();
                if (transaction != null)
                    transaction.rollback();
            }
        }
        return users;
    }

    public boolean updateUser(User user) {
        boolean updated = false;
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                session.merge(user);
                tx.commit();
                updated = true;
            } catch (RuntimeException e) {
                e.printStackTrace();
                if (tx != null)
                    tx.rollback();
            }
        }
        return updated;
    }

    /*
    public boolean deleteUserByUsername(String username) {
        boolean deleted=false;
        try(Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                User user=new User();
                user.setUsername(username);
                session.remove(user);
                tx.commit();
                deleted=true;
            } catch (RuntimeException ex) {
                System.err.println("Eroare la stergere "+ex);
                if (tx != null)
                    tx.rollback();
            }
        }
        return deleted;
    }

     */
}
