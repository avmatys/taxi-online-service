package by.bsuir.matys_rozin_zarudny.layer.service.entities.taxi;

import java.io.Serializable;

/**
 * Interface representing a state a taxi can be in and common operations.
 * State design pattern.
 */
public interface TaxiState extends Serializable {

    public void goOffDuty(Taxi taxi);

    public void goOnDuty(Taxi taxi);

    public void acceptJob(Taxi taxi);
    
    public void completeJob(Taxi taxi);
}
