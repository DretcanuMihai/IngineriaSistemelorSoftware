package bugmanager.persistence;

import bugmanager.model.Bug;
import bugmanager.model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.List;

public class BugRepository {
    private final SessionFactory sessionFactory;

    public BugRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public boolean addBug(Bug bug) {
        boolean added = false;
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                session.persist(bug);
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

    public Bug getBugById(Integer id) {
        Bug bug = null;
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = null;
            try {
                transaction = session.beginTransaction();
                bug = session.get(Bug.class, id);
                transaction.commit();
            } catch (RuntimeException e) {
                e.printStackTrace();
                if (transaction != null)
                    transaction.rollback();
            }
        }
        return bug;
    }

    public List<Bug> getAllActiveBugs() {
        List<Bug> bugs = new ArrayList<>();
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = null;
            try {
                transaction = session.beginTransaction();
                bugs = session.createQuery("from Bug as b where b.status!=2", Bug.class)
                        .list();
                transaction.commit();
            } catch (RuntimeException e) {
                e.printStackTrace();
                if (transaction != null)
                    transaction.rollback();
            }
        }
        return bugs;
    }

    public boolean updateBug(Bug bug) {
        boolean updated = false;
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                session.merge(bug);
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
    public boolean deleteBugById(Integer id) {
        boolean deleted = false;
        try(Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                Bug bug=new Bug();
                bug.setId(id);
                session.remove(bug);
                tx.commit();
                deleted = true;
            } catch (RuntimeException e) {
                e.printStackTrace();
                if (tx != null)
                    tx.rollback();
            }
        }
        return deleted;
    }
    */
}
