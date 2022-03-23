package by.andersen.intensive.yellow.team.main.model.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;
import java.util.Set;
import java.util.StringJoiner;

@Entity
@Table(name = "roles")
public class Role implements Serializable{

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private long id;

    @Column(name = "role_name")
    private String roleName;

    @Column(name = "role_description")
    private String roleDescription;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "roles_privileges",
            joinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "privilege_id", referencedColumnName = "id"))
    private Set<Privilege> privileges;

    @ManyToMany(mappedBy = "roles")
    private Set<User> users;

    public Role(String roleName, String roleDescription) {
        this.roleName = roleName;
        this.roleDescription = roleDescription;
    }

    public Role() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleDescription() {
        return roleDescription;
    }

    public void setRoleDescription(String roleDescription) {
        this.roleDescription = roleDescription;
    }

    public Set<Privilege> getPrivileges() {
        return privileges;
    }

    public void setPrivileges(Set<Privilege> privileges) {
        this.privileges = privileges;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Role role = (Role) o;
        return id == role.id && Objects.equals(roleName, role.roleName) && Objects.equals(roleDescription, role.roleDescription);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, roleName, roleDescription);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Role.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("roleName='" + roleName + "'")
                .add("roleDescription='" + roleDescription + "'")
                .toString();
    }
}
