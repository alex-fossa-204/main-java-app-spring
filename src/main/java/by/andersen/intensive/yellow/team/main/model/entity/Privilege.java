package by.andersen.intensive.yellow.team.main.model.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;
import java.util.Objects;
import java.util.StringJoiner;

@Entity
@Table(name = "privileges")
public class Privilege implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "privilege_name")
    private String privilegeName;

    @ManyToMany(mappedBy = "privileges", fetch = FetchType.EAGER)
    private Collection<Role> roles;

    public Privilege() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPrivilegeName() {
        return privilegeName;
    }

    public void setPrivilegeName(String privilegeName) {
        this.privilegeName = privilegeName;
    }

    public Collection<Role> getRoles() {
        return roles;
    }

    public void setRoles(Collection<Role> roles) {
        this.roles = roles;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Privilege privilege = (Privilege) o;
        return Objects.equals(id, privilege.id) && Objects.equals(privilegeName, privilege.privilegeName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, privilegeName);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Privilege.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("privilegeName='" + privilegeName + "'")
                .toString();
    }
}
