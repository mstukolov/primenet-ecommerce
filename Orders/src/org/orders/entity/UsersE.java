package org.orders.entity;

import javax.persistence.*;
import java.io.Serializable;


@javax.persistence.Table(name = "users", schema = "", catalog = "orders")
@Entity
@AttributeOverride(name = "recid", column = @Column(name = "recid",
        nullable = false, columnDefinition = "BIGINT UNSIGNED"))
public class UsersE extends BaseEntityAudit implements Serializable {
    //private int userId;
    private String login;
    private String password;
    private String role;
    private Boolean enabled;

    public UsersE() {
        this.setCreatedBy("Admin");
        this.setUpdatedBy("Admin");
    }

    @javax.persistence.Column(name = "enabled")
    @Basic
    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    @javax.persistence.Column(name = "login")
    @Basic
    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    @javax.persistence.Column(name = "password")
    @Basic
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @javax.persistence.Column(name = "role")
    @Basic
    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UsersE usersE = (UsersE) o;

        //if (userId != usersE.userId) return false;
        if (login != null ? !login.equals(usersE.login) : usersE.login != null) return false;
        if (password != null ? !password.equals(usersE.password) : usersE.password != null) return false;
        if (role != null ? !role.equals(usersE.role) : usersE.role != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = getRecid().intValue();
        result = 31 * result + (login != null ? login.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (role != null ? role.hashCode() : 0);
        return result;
    }
}
