package org.tim_18.UberApp.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;

@Entity
@Table(name = "role")
public class Role implements GrantedAuthority {

    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @Column(name="name")
    String name;

    @JsonIgnore
    @Override
    public String getAuthority() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }

    @JsonIgnore
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (!(o instanceof Role role)) return false;
//
//        if (getId() != null ? !getId().equals(role.getId()) : role.getId() != null) return false;
//        return getName() != null ? getName().equals(role.getName()) : role.getName() == null;
        Role r = (Role) o;
        return (r.getId().equals(getId()) && r.getName().equals(getName()));
    }

    @Override
    public int hashCode() {
        int result = getId() != null ? getId().hashCode() : 0;
        result = 31 * result + (getName() != null ? getName().hashCode() : 0);
        return result;
    }
}
