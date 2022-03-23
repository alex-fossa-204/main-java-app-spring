package by.andersen.intensive.yellow.team.main.controller.dto;

import by.andersen.intensive.yellow.team.main.model.entity.Report;
import by.andersen.intensive.yellow.team.main.model.entity.Role;
import by.andersen.intensive.yellow.team.main.model.entity.User;

import java.util.*;

public class ResponseUser {

    private long id;

    private String username;

    private String firstName;

    private String lastName;

    private String emailAddress;

    private String password;

    private boolean nonLocked;

    private boolean active;

    private String userProfileImageUrl;

    private String role;

    private List<ResponseReport> reports;

    public ResponseUser(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.emailAddress = user.getEmailAddress();
        this.password = user.getPassword();
        this.nonLocked = user.isNonLocked();
        this.active = user.isActive();
        this.userProfileImageUrl = user.getUserProfileImageUrl();
        Set<Role> userRoles = user.getRoles();
        if(userRoles == null) {
            userRoles = new HashSet<>();
        }
        this.role = convertRolesToResponseRole(userRoles);
        Set<Report> userReports = user.getReports();
        if(userReports == null) {
            userReports = new HashSet<>();
        }
        this.reports = convertReportsToResponseReports(userReports);
    }

    private String convertRolesToResponseRole(Set<Role> roles) {
        List<Role> rolesList = new ArrayList<>(roles);
        return rolesList.get(0).getRoleName();
    }

    private List<ResponseReport> convertReportsToResponseReports(Set<Report> reports) {
        List<ResponseReport> responseReports = new ArrayList<>();
        reports.forEach(report -> {
            ResponseReport responseReport = new ResponseReport(report);
            responseReports.add(responseReport);
        });
        return responseReports;
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

    public boolean isNonLocked() {
        return nonLocked;
    }

    public void setNonLocked(boolean nonLocked) {
        nonLocked = nonLocked;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        active = active;
    }

    public String getUserProfileImageUrl() {
        return userProfileImageUrl;
    }

    public void setUserProfileImageUrl(String userProfileImageUrl) {
        this.userProfileImageUrl = userProfileImageUrl;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public List<ResponseReport> getReports() {
        return reports;
    }

    public void setReports(List<ResponseReport> reports) {
        this.reports = reports;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ResponseUser that = (ResponseUser) o;
        return id == that.id && nonLocked == that.nonLocked && active == that.active && Objects.equals(username, that.username) && Objects.equals(firstName, that.firstName) && Objects.equals(lastName, that.lastName) && Objects.equals(emailAddress, that.emailAddress) && Objects.equals(password, that.password) && Objects.equals(userProfileImageUrl, that.userProfileImageUrl) && Objects.equals(role, that.role) && Objects.equals(reports, that.reports);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, firstName, lastName, emailAddress, password, nonLocked, active, userProfileImageUrl, role, reports);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", ResponseUser.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("username='" + username + "'")
                .add("firstName='" + firstName + "'")
                .add("lastName='" + lastName + "'")
                .add("emailAddress='" + emailAddress + "'")
                .add("password='" + password + "'")
                .add("isNonLocked=" + nonLocked)
                .add("isActive=" + active)
                .add("userProfileImageUrl='" + userProfileImageUrl + "'")
                .add("role=" + role)
                .add("reports=" + reports)
                .toString();
    }
}
