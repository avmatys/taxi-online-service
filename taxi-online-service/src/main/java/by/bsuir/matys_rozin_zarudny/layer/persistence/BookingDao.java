package by.bsuir.matys_rozin_zarudny.layer.persistence;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import by.bsuir.matys_rozin_zarudny.layer.service.entities.booking.Booking;
import by.bsuir.matys_rozin_zarudny.layer.service.entities.booking.BookingState;


public class BookingDao extends JpaEntityDaoImpl<Long, Booking> {

   
    public List<Booking> findBookingInState(BookingState state) {
        EntityManager em = this.getEntityManager();
        List<Booking> bookings = null;

        try {
            Query query = em.createNamedQuery("Booking.findBookingsInState", Booking.class);
            query.setParameter("state", state);
            bookings = query.getResultList();
        } finally {
            if (em.isOpen()) {
                em.close();
            }
        }
        return bookings;
    }

   
    public List<Booking> findBookingsForPassenger(String username) {
        return this.findBookingsForAccount("Booking.findBookingsForPassenger", username);
    }

    public List<Booking> findBookingsForDriver(String username) {
        return this.findBookingsForAccount("Booking.findBookingsForDriver", username);
    }

    public List<Booking> findInCompletedBookingsForUser(String username) {

        List<Booking> bookings = null;
        EntityManager em = this.getEntityManager();

        try {
            Query query = em.createNamedQuery("Booking.findBookingsforUserInState", Booking.class);
            query.setParameter("username", username);
            query.setParameter("state", Booking.getCompletedTaxiBookingState());
            query.setParameter("state2", Booking.getCancelledBookingState());
            bookings = query.getResultList();
        } finally {
            if (em.isOpen()) {
                em.close();
            }
        }
        return bookings;
    }

    /**
     * Find bookings for account with given username and query.
     *
     */
    private List<Booking> findBookingsForAccount(String query, String username) {

        EntityManager em = this.getEntityManager();
        List<Booking> bookings = null;

        try {
            Query namedQuery = em.createNamedQuery(query, Booking.class);
            namedQuery.setParameter("username", username);
            bookings = namedQuery.getResultList();
        } finally {
            if (em.isOpen()) {
                em.close();
            }
        }
        return bookings;
    }
}
