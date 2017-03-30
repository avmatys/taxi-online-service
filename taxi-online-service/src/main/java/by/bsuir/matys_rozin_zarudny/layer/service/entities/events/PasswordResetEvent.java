package by.bsuir.matys_rozin_zarudny.layer.service.entities.events;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import org.joda.time.DateTime;

/**
 * Entity class for password reset events.
 *
 */
@Entity
@Table(name = "PASSWORD_RESET_EVENT")
@NamedQueries({
    @NamedQuery(
            name = "PasswordResetEvent.findPasswordResetByUsernameAndCode",
            query = "SELECT e FROM PasswordResetEvent e WHERE e.username = :username AND e.code = :code AND e.active=1"
    ),
    @NamedQuery(
            name = "PasswordResetEvent.findActivePasswordResetByUsername",
            query = "SELECT e FROM PasswordResetEvent e WHERE e.username = :username"
    )
})
public class PasswordResetEvent extends Event {

    @Column(name = "USERNAME")
    private String username;

    @Column(name = "CODE")
    private String code;

    @Column(name = "ACTIVE")
    private boolean active;

    @Column(name = "EXPIRY")
    private DateTime expiry;

    public PasswordResetEvent() {
    }

    public PasswordResetEvent(String username, String code, DateTime expiry) {
        this.username = username;
        this.code = code;
        this.expiry = expiry;
        this.active = true;
    }
   
    public String getUsername() {
        return username;
    }

    public String getCode() {
        return code;
    }

    public DateTime getExpiry() {
        return expiry;
    }

    public void setInactive() {
        this.setActive(false);
    }

    public boolean isActive() {
        return this.active;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
    
    public void setExpiry(DateTime expiry) {
        this.expiry = expiry;
    }

    /**
     * Return true if code is valid, has not expired and is active, else false.
     */
    public boolean validateCode(String code) {
        return this.code.equals(code)
                && this.isActive()
                && new DateTime().isBefore(this.expiry);
    }
}
