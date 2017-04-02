package by.bsuir.matys_rozin_zarudny.layer.persistence.dto;

import java.util.Date;

import by.bsuir.matys_rozin_zarudny.layer.utils.datamapper.DataMapper;

/**
 * HTTP error response data transfer object.
 */
public class HttpResponse {

    private String status;
    private Date timestamp;

    /**
     *
     * @param status status code for response.
     */
    public HttpResponse(String status) {
        this.status = status;
        this.timestamp = new Date();
    }

    public String getStatus() {
        return status;
    }

    public void setCode(String status) {
        this.status = status;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * Return JSON string representation of the object.
     */
    @Override
    public String toString() {
        return DataMapper.getInstance().getObjectAsJson(this);
    }
}
