 package by.bsuir.matys_rozin_zarudny.layer.service.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

/**
 * Entity class for a user.
 *
 */
@Entity
@Table(name = "ACCOUNT")
public class Account implements Serializable {
    
    @Transient
    private static final Logger LOGGER = Logger.getLogger(Account.class.getName());
    
    @Transient
    private static final long serialVersionUID = 1998061924459270142L;

    @Id
    @Column(name = "USERNAME")
    private String username;

    @NotNull
    @Column(name = "COMMON_NAME", length = 128)
    private String commonName;
    
    @NotNull
    @Column(name = "FAMILY_NAME", length = 128)
    private String familyName;

    @NotNull
    @Column(name = "PASSWORD", length = 256)
    private String password;

    @NotNull
    @Column(name = "EMAIL")
    private String email;

    @NotNull
    @Column(name = "PHONE_NUMBER", length = 15)
    private String phoneNumber;

    @Column(name = "ROLE")
    @Enumerated(EnumType.STRING)
    private AccountRole role;

    @Column(name = "ACTIVE")
    @Enumerated(EnumType.STRING)
    private AccountStatus active;
 
    public Account() {
        // Empty as per JPA 2.0 specification.
    }

    public Account(String username, String commonName, String familyName, String password, String phoneNumber, String email) {
        this.username = username;
        this.commonName = commonName;
        this.familyName = familyName;
        this.password = password;
        this.email = email;
        this.phoneNumber = phoneNumber;

        this.setActive();
    } 

   
    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }


    public String getUsername() {
        return this.username;
    }
    
  
    public void setUsername(String username){
        this.username = username;
    }

    public boolean hasRole(String role) {
        try {
            return this.role == AccountRole.valueOf(role.toUpperCase());
        } catch (IllegalArgumentException ex) {
            LOGGER.log(Level.FINEST, null, ex);
            return false;
        }
    }

    @JsonIgnore
    public String getPassword() {
        return this.password;
    }

    @JsonProperty("password")
    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    @JsonProperty("email")
    public void setEmail(String email) {
        this.email = email;
    }

    
    public AccountRole getRole() {
        return role;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

   
    public void setRole(AccountRole role) {
        if (role == null) {
            throw new IllegalArgumentException("Role cannot be null.");
        }

        this.role = role;
    }

   
    public boolean isActive() {
        return this.active.ACTIVE == AccountStatus.ACTIVE;
    }

    public void setInActive() {
        this.active = AccountStatus.ACTIVE;
    }


    public final void setActive() {
        this.active = AccountStatus.ACTIVE;
    }

    public String getCommonName() {
        return commonName;
    }

    public void setCommonName(String commonName) {
        this.commonName = commonName;
    }
    
    public String getFamilyName() {
        return familyName;
    }
   
    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }
    
    @Override
    public int hashCode() {
        return username != null ? username.hashCode() : 0;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Account other = (Account) obj;
        if (!Objects.equals(this.username, other.username)) {
            return false;
        }
        if (!Objects.equals(this.commonName, other.commonName)) {
            return false;
        }
        if (!Objects.equals(this.familyName, other.familyName)) {
            return false;
        }
        if (!Objects.equals(this.password, other.password)) {
            return false;
        }
        if (!Objects.equals(this.email, other.email)) {
            return false;
        }
        if (!Objects.equals(this.phoneNumber, other.phoneNumber)) {
            return false;
        }
        if (this.role != other.role) {
            return false;
        }
        if (this.active != other.active) {
            return false;
        }
        return true;
    }
}
