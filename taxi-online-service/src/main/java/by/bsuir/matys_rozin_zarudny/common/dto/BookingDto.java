package by.bsuir.matys_rozin_zarudny.common.dto;

import java.io.Serializable;

public class BookingDto implements Serializable {
	private long id;
	private LocationDto startLocation;
	private LocationDto endLocation;
	private int numberPassengers;

	public BookingDto() {
	}

	public LocationDto getStartLocation() {
		return startLocation;
	}

	public void setStartLocation(LocationDto startLocation) {
		this.startLocation = startLocation;
	}

	public LocationDto getEndLocation() {
		return endLocation;
	}

	public void setEndLocation(LocationDto endLocation) {
		this.endLocation = endLocation;
	}

	public int getNumberPassengers() {
		return numberPassengers;
	}

	public void setNumberPassengers(int numberPassengers) {
		this.numberPassengers = numberPassengers;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
}
