package by.bsuir.matys_rozin_zarudny.layer.service.entities.taxi;

/**
 * Represents taxi states.
 */
public enum TaxiStates {
	
    OFF_DUTY("OFF_DUTY"),
    ON_DUTY("ON_DUTY"),
    ON_JOB("ON_JOB");
    
    private String textualDescription;
  
    TaxiStates(String description){
        this.textualDescription = description;
    }
    
    @Override
    public String toString(){
        return this.textualDescription;
    }
}
