package by.andersen.intensive.yellow.team.main.model.dto;

import java.io.Serializable;
import java.util.Objects;

public class UserDTO implements Serializable {

    private String firstName;

    private String lastName;

    public UserDTO(String firstName, String lastName) {
        super();
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public UserDTO() {
        super();
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

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        UserDTO other = (UserDTO) obj;
        return Objects.equals(firstName, other.firstName) && Objects.equals(lastName, other.lastName);
    }

}
