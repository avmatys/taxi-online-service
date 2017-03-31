package by.bsuir.matys_rozin_zarudny.layer.persistence;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import by.bsuir.matys_rozin_zarudny.layer.service.entities.events.PasswordResetEvent;


public class PasswordResetEventDao extends JpaEntityDaoImpl<Long, PasswordResetEvent> {

    /**
     * Find password reset code using username, code with status of active.
     * Return list as there is a possibility user reset their account multiple
     * times and an identical reset code is generated.
     *
     * @param username username to search by.
     * @param code reset code to search by.
     * @return collection of PasswordResetEvent with matching username and code.
     */
    public List<PasswordResetEvent> findPasswordResetByUsernameAndCode(String username, String code) {

        List<PasswordResetEvent> events = null;
        EntityManager em = this.getEntityManager();

        try {
            Query query = em.createNamedQuery(
                    "PasswordResetEvent.findPasswordResetByUsernameAndCode", PasswordResetEvent.class);
            query.setParameter("username", username);
            query.setParameter("code", code);
            events = query.getResultList();
        } finally {
            if (em.isOpen()) {
                em.close();
            }
        }

        return events;
    }

    /**
     * Return a collection of all active password resets for a given user.
     *
     * @param username username to search for active password resets.
     * @return a collection of all active password resets for a given user.
     */
    public List<PasswordResetEvent> findActivePasswordResetByUsername(String username) {

        List<PasswordResetEvent> events = null;
        EntityManager em = this.getEntityManager();

        try {
            Query query = em.createNamedQuery(
                    "PasswordResetEvent.findActivePasswordResetByUsername", PasswordResetEvent.class);
            query.setParameter("username", username);
            events = query.getResultList();
        } finally {
            if (em.isOpen()) {
                em.close();
            }
        }
        return events;
    }
}
