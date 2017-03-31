package by.bsuir.matys_rozin_zarudny.common.dto;

import java.util.Objects;

public class TaxiLocationEventDto {
	
	private long taxiId;
	private String state;
	private long timestamp;
	private LocationDto location;

	public TaxiLocationEventDto() {
	}

	public TaxiLocationEventDto(long taxiId, String state, LocationDto location, long timestamp) {
		this.taxiId = taxiId;
		this.state = state;
		this.timestamp = timestamp;
		this.location = location;
	}

	public long getTaxiId() {
		return taxiId;
	}

	public void setTaxiId(long taxiId) {
		this.taxiId = taxiId;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public LocationDto getLocation() {
		return location;
	}

	public void setLocation(LocationDto currentLocation) {
		location = currentLocation;
	}

	public int hashCode() {
		int hash = 7;
		hash = 41 * hash + (int) (taxiId ^ taxiId >>> 32);
		hash = 41 * hash + Objects.hashCode(state);
		hash = 41 * hash + (int) (timestamp ^ timestamp >>> 32);
		hash = 41 * hash + Objects.hashCode(location);
		return hash;
	}

	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		TaxiLocationEventDto other = (TaxiLocationEventDto) obj;
		if (taxiId != taxiId) {
			return false;
		}
		if (!Objects.equals(state, state)) {
			return false;
		}
		if (timestamp != timestamp) {
			return false;
		}
		if (!Objects.equals(location, location)) {
			return false;
		}
		return true;
	}
}
