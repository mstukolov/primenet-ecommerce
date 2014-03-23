package org.orders.entity;

import javax.persistence.*;

@javax.persistence.Table(name = "users", schema = "", catalog = "orders")
@Entity
public class Users {
    private Long userId;
    private String login;
    private String password;
    private String role;

    @javax.persistence.Column(name = "userId")
    @Id
    @TableGenerator(name="TABLE_GEN", table="ORDERS.SEQUENCE_TABLE", pkColumnName="SEQ_NAME",
            valueColumnName="SEQ_COUNT", pkColumnValue="USR_SEQ")
    @GeneratedValue(strategy=GenerationType.TABLE, generator="TABLE_GEN")
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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

        Users that = (Users) o;

        if (userId != that.userId) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = userId.intValue();
        return result;
    }
}
