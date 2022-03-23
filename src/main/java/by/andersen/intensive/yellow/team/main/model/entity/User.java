package by.andersen.intensive.yellow.team.main.model.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;
import java.util.Set;
import java.util.StringJoiner;

@Entity
@Table(name = "users")
public class User implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private long id;

    @Column(name = "username")
    private String username;

    @Column(name = "user_firstname")
    private String firstName;

    @Column(name = "user_lastname")
    private String lastName;

    @Column(name = "user_email")
    private String emailAddress;

    @Column(name = "user_password")
    private String password;

    @Column(name = "is_non_locked")
    private boolean isNonLocked;

    @Column(name = "is_active")
    private boolean isActive;

    @Column(name = "user_profile_image_url")
    private String userProfileImageUrl;

    @ManyToMany(fetch = FetchType.EAGER)
    @JsonSerialize
    @JoinTable(name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    private Set<Role> roles;

    @OneToMany(mappedBy = "reportedBy", fetch = FetchType.EAGER)
    @JsonSerialize
    private Set<Report> reports;

    public User(String username, String firstName, String lastName, String emailAddress) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailAddress = emailAddress;
        this.isNonLocked = true;
        this.isActive = true;
    }

    public User() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public boolean isNonLocked() {
        return isNonLocked;
    }

    public void setNonLocked(boolean nonLocked) {
        isNonLocked = nonLocked;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getUserProfileImageUrl() {
        return userProfileImageUrl;
    }

    public void setUserProfileImageUrl(String userProfileImageUrl) {
        this.userProfileImageUrl = userProfileImageUrl;
    }

    public Set<Report> getReports() {
        return reports;
    }

    public void setReports(Set<Report> reports) {
        this.reports = reports;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return isNonLocked == user.isNonLocked && isActive == user.isActive && Objects.equals(username, user.username) && Objects.equals(firstName, user.firstName) && Objects.equals(lastName, user.lastName) && Objects.equals(emailAddress, user.emailAddress) && Objects.equals(password, user.password) && Objects.equals(userProfileImageUrl, user.userProfileImageUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, firstName, lastName, emailAddress, password, isNonLocked, isActive, userProfileImageUrl);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", User.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("username='" + username + "'")
                .add("firstName='" + firstName + "'")
                .add("lastName='" + lastName + "'")
                .add("emailAddress='" + emailAddress + "'")
                .add("password='" + password + "'")
                .add("isNonLocked=" + isNonLocked)
                .add("isActive=" + isActive)
                .add("userProfileImageUrl=" + userProfileImageUrl)
                .toString();
    }
}
