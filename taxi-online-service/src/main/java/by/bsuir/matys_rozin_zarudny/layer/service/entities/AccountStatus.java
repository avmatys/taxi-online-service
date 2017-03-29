package by.bsuir.matys_rozin_zarudny.layer.service.entities;

/**
 * State of an account.
 *
 */
public enum AccountStatus {

    ACTIVE("ACTIVE"), INACTIVE("INACTIVE");

    private String status;

    AccountStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return this.status;
    }
}
