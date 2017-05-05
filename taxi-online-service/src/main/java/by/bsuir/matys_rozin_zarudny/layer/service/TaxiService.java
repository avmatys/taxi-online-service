package by.bsuir.matys_rozin_zarudny.layer.service;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import javax.ejb.Stateless;

import by.bsuir.matys_rozin_zarudny.layer.persistence.TaxiDao;
import by.bsuir.matys_rozin_zarudny.layer.service.entities.booking.Booking;
import by.bsuir.matys_rozin_zarudny.layer.service.entities.events.EventTypes;
import by.bsuir.matys_rozin_zarudny.layer.service.entities.taxi.Taxi;
import by.bsuir.matys_rozin_zarudny.layer.utils.gcm.GcmClient;

/**
 * Taxi Service class implementation.
 *
 * @author robertnorthard
 */
@Stateless
public class TaxiService implements TaxiFacade {

    private Map<Integer, Taxi> allTaxisCache = new ConcurrentHashMap<>();
    private TaxiDao taxiDao = new TaxiDao();

    public TaxiService() {
        // left blank. 
    }

    /**
     * Constructor - used for dependency injection.
     *
     * @param taxoDao taxi dao.
     */
    public TaxiService(TaxiDao taxoDao) {
        this.taxiDao = taxoDao;
    }

    /**
     * Find taxi by id.
     *
     * @param id id of taxi.
     * @return taxi or null if not found.
     */
    @Override
    public Taxi findTaxi(Long id) {
        return this.taxiDao.findEntityById(id);
    }

    /**
     * Update taxi.
     *
     * @param taxi taxi to update.
     */
    @Override
    public void updateTaxi(Taxi taxi) {
        this.taxiDao.update(taxi);
    }

    /**
     * Return a collection of taxis on duty and available.
     *
     * @return a collection of taxis on duty and available.
     */
    @Override
    public List<Taxi> findAllTaxiOnDutyAndAvailable() {
        return this.taxiDao.findAllOnDuty();
    }

    /**
     * Return a collection of taxis on duty and available.
     *
     * @return a collection of taxis on duty and available.
     */
    @Override
    public List<Taxi> findAllTaxiOffDuty() {
        return this.taxiDao.findAllOffDuty();
    }
}
