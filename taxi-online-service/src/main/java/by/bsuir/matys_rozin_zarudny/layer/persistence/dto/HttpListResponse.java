package by.bsuir.matys_rozin_zarudny.layer.persistence.dto;

import java.util.List;

/**
 * DTO encapsulate a http response that contains a collection
 * of data.
 */
public class HttpListResponse<T> extends HttpResponse {

    private List<T> data;

    public HttpListResponse(List<T> data, String status) {
        super(status);
        this.data = data;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }
}
