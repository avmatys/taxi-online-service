package by.bsuir.matys_rozin_zarudny.layer.utils.http;

/**
 * Http header constants.
 */
public enum HttpHeader {

    AUTHORIZATION("Authorization");

    private String header;

    HttpHeader(String header) {
        this.header = header;
    }

    @Override
    public String toString() {
        return this.header;
    }

}
