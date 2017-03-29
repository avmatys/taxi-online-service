package by.bsuir.matys_rozin_zarudny.layer.service.entities;

/**
 * Enum to represent account types.
 * 
 */
public enum AccountType {

    PASSENGER("passenger"),
    DRIVER("driver"),
    ADMIN("admin");

    String roleName;

    AccountType(String roleName) {
        this.roleName = roleName;
    }

    public static boolean isValidRole(String role) {

        try {
            AccountType.valueOf(role);
            return true;
        } catch (IllegalArgumentException ex) {
            return false;
        }
    }

    @Override
    public String toString() {
        return this.roleName;
    }
}
