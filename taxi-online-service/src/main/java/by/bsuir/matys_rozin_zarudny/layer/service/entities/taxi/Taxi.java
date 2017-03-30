package by.bsuir.matys_rozin_zarudny.layer.service.entities.taxi;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import by.bsuir.matys_rozin_zarudny.layer.persistence.data.mappers.taxi.JpaTaxiStateDataConverter;
import by.bsuir.matys_rozin_zarudny.layer.persistence.data.mappers.taxi.JsonTaxiStateDataConverter;
import by.bsuir.matys_rozin_zarudny.layer.service.entities.Account;
import by.bsuir.matys_rozin_zarudny.layer.service.entities.Location;
import by.bsuir.matys_rozin_zarudny.layer.service.entities.Vehicle;

import java.io.Serializable;
import java.util.Observable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * Represents a Taxi.
 *
 */
@Entity
@Table(name = "TAXI")
@NamedQueries({
		@NamedQuery(name = "Taxi.findTaxiForDriver", query = "SELECT t FROM Taxi t WHERE t.account.username = :username"),
		@NamedQuery(name = "Taxi.findTaxisWithState", query = "SELECT t FROM Taxi t WHERE t.state = :state") })
public class Taxi extends Observable implements Serializable {

	@Transient
	private static final long serialVersionUID = -3300934288127984894L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID")
	private Long id;

	@OneToOne(cascade = CascadeType.MERGE)
	@JoinColumn(name = "VEHICLE_ID")
	private Vehicle vehicle;

	@OneToOne(cascade = CascadeType.MERGE)
	@JoinColumn(name = "ACCOUNT_ID")
	private Account account;

	// Last known taxi location.
	@OneToOne(cascade = CascadeType.MERGE)
	@JoinColumn(name = "TAXI_LOCATION_ID")
	private Location location;

	@Column(name = "TAXI_STATE")
	@Convert(converter = JpaTaxiStateDataConverter.class)
	private TaxiState state;

	// taxi states
	@JsonIgnore
	@Transient
	private static TaxiState offDutyTaxiState = new OffDutyTaxiState();

	@JsonIgnore
	@Transient
	private static TaxiState acceptedJobTaxiState = new AcceptedJobTaxiState();

	@JsonIgnore
	@Transient
	private static TaxiState onDutyTaxiState = new OnDutyTaxiState();

	public Taxi() {
		// Empty constructor required by JPA.
	}

	public Taxi(Vehicle vehicle, Account account) {
		this.vehicle = vehicle;
		this.account = account;

		this.location = null;

		this.state = Taxi.getOffDutyTaxiState();
	}

	@JsonIgnore
	public double getCostPerKilometer() {
		return this.getVehicle().getCostPerKilometer();
	}

	public Vehicle getVehicle() {
		return this.vehicle;
	}

	public void setVehicle(Vehicle vehicle) {
		this.vehicle = vehicle;
	}

	public Account getAccount() {
		return this.account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.setId((Long) id);
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Location getLocation() {
		return this.location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	/**
	 * Return true if taxi has enough seats else false.
	 */
	public boolean checkseatAvailability(int numberSeats) {
		return this.vehicle.getNumberSeats() - 1 - numberSeats >= 0;
	}

	public void updateLocation(Location location) {
		if (location != null) {
			// location can be null.
			this.location = new Location();
			this.location.setLatitude(location.getLatitude());
			this.location.setLongitude(location.getLongitude());
		} else {
			throw new IllegalArgumentException("Location can not be null.");
		}
	}

	@JsonSerialize(using = JsonTaxiStateDataConverter.class)
	public TaxiState getState() {
		return state;
	}

	public void setState(TaxiState state) {
		this.state = state;
	}

	public void goOffDuty() {
		this.state.goOffDuty(this);

	}

	public void goOnDuty() {
		this.state.goOnDuty(this);

	}

	public void acceptJob() {
		this.state.acceptJob(this);
	}

	public static TaxiState getOffDutyTaxiState() {
		return Taxi.offDutyTaxiState;
	}

	public static TaxiState getAcceptedJobTaxiState() {
		return Taxi.acceptedJobTaxiState;
	}

	public static TaxiState getOnDutyTaxiState() {
		return Taxi.onDutyTaxiState;
	}
}
