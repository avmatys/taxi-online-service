package by.bsuir.matys_rozin_zarudny.layer.service.entities;

/**
 * Enum to represent account role.
 *
 */
public enum AccountRole {

    DRIVER("driver"),
    PASSENGER("passenger");

    private final String role;

    AccountRole(final String role) {
        this.role = role;
    }

    public static AccountRole getRole(String role) {
        try {
            return AccountRole.valueOf(role);
        } catch (IllegalArgumentException ex) {
            throw ex;
        }
    }

    @Override
    public String toString() {
        return this.role;
    }
}
