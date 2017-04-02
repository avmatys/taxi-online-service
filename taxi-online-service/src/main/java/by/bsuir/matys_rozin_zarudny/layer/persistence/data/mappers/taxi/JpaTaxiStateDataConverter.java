package by.bsuir.matys_rozin_zarudny.layer.persistence.data.mappers.taxi;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import by.bsuir.matys_rozin_zarudny.layer.service.entities.taxi.AcceptedJobTaxiState;
import by.bsuir.matys_rozin_zarudny.layer.service.entities.taxi.OffDutyTaxiState;
import by.bsuir.matys_rozin_zarudny.layer.service.entities.taxi.OnDutyTaxiState;
import by.bsuir.matys_rozin_zarudny.layer.service.entities.taxi.TaxiState;
import by.bsuir.matys_rozin_zarudny.layer.service.entities.taxi.TaxiStates;

@Converter
public class JpaTaxiStateDataConverter implements AttributeConverter<TaxiState, String> {

    @Override
    public String convertToDatabaseColumn(TaxiState attribute) {

        if (attribute instanceof AcceptedJobTaxiState) {
            return TaxiStates.ON_JOB.toString();

        } else if (attribute instanceof OffDutyTaxiState) {
            return TaxiStates.OFF_DUTY.toString();

        } else if (attribute instanceof OnDutyTaxiState) {
            return TaxiStates.ON_DUTY.toString();

        }

        throw new IllegalArgumentException("Cannot convert object to data.");
    }

    @Override
    public TaxiState convertToEntityAttribute(String dbData) {
        
        if(dbData.equals(TaxiStates.OFF_DUTY.toString())){
            return new OffDutyTaxiState();
        }else if(dbData.equals(TaxiStates.ON_DUTY.toString())){
            return new OnDutyTaxiState();
        }else if(dbData.equals(TaxiStates.ON_JOB.toString())){
            return new AcceptedJobTaxiState();
        }

        throw new IllegalArgumentException("Cannot convert database entity to object.");

    }
}
