package by.bsuir.matys_rozin_zarudny.layer.persistence.dto;

/**
 * A class to encapsulate objects in a http response.
 */
public class HttpObjectResponse extends HttpResponse {

    private Object data;

    public HttpObjectResponse(Object object, String status) {
        super(status);

        this.data = object;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
