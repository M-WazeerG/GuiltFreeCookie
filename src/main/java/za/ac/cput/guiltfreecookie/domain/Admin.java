package za.ac.cput.guiltfreecookie.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "admins")
public class Admin {

    @Id
    @GeneratedValue
    private String adminId;

    @Column(length = 100)
    private String firstName;

    @Column(length = 100)
    private String lastName;

    @Column(unique = true, length = 254)
    private String email;

    // Accepted in requests (create, login) but never written to a response:
    // even a bcrypt hash shouldn't leave the server.
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    private boolean active;
    private LocalDateTime passwordExpiresAt;

    // Bumped on every password change or reset. Each login token records the
    // version it was issued under, so older tokens stop working at once
    // (see JwtAuthenticationFilter). Null for admins created before it existed.
    @JsonIgnore
    private Integer tokenVersion;

    protected Admin() {
    }

    private Admin(Builder builder) {
        this.adminId = builder.adminId;
        this.firstName = builder.firstName;
        this.lastName = builder.lastName;
        this.email = builder.email;
        this.password = builder.password;
        this.active = builder.active;
        this.passwordExpiresAt = builder.passwordExpiresAt;
        this.tokenVersion = builder.tokenVersion;
    }

    public String getAdminId() {
        return adminId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public boolean isActive() {
        return active;
    }

    public LocalDateTime getPasswordExpiresAt() {
        return passwordExpiresAt;
    }

    public int getTokenVersion() {
        return tokenVersion == null ? 0 : tokenVersion;
    }

    @Override
    public String toString() {
        return "Admin{" +
                "adminId='" + adminId + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", active=" + active +
                '}';
    }

    public static class Builder {

        private String adminId;
        private String firstName;
        private String lastName;
        private String email;
        private String password;
        private boolean active = true;
        private LocalDateTime passwordExpiresAt;
        private Integer tokenVersion;

        public Builder setAdminId(String adminId) {
            this.adminId = adminId;
            return this;
        }

        public Builder setFirstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public Builder setLastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public Builder setEmail(String email) {
            this.email = email;
            return this;
        }

        public Builder setPassword(String password) {
            this.password = password;
            return this;
        }

        public Builder setActive(boolean active) {
            this.active = active;
            return this;
        }

        public Builder setPasswordExpiresAt(LocalDateTime passwordExpiresAt) {
            this.passwordExpiresAt = passwordExpiresAt;
            return this;
        }

        public Builder setTokenVersion(int tokenVersion) {
            this.tokenVersion = tokenVersion;
            return this;
        }

        public Builder copy(Admin admin) {
            this.adminId = admin.adminId;
            this.firstName = admin.firstName;
            this.lastName = admin.lastName;
            this.email = admin.email;
            this.password = admin.password;
            this.active = admin.active;
            this.passwordExpiresAt = admin.passwordExpiresAt;
            this.tokenVersion = admin.tokenVersion;
            return this;
        }

        public Admin build() {
            return new Admin(this);
        }
    }
}