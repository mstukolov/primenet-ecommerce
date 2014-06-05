package org.orders.entity;

import javax.persistence.*;

@Entity
@javax.persistence.Table(name = "logevents", schema = "", catalog = "orders")
@AttributeOverride(name = "recid", column = @Column(name = "recid",
        nullable = false, columnDefinition = "BIGINT UNSIGNED"))
public class Logevents extends BaseEntityAudit{
    private String user;
    @Temporal(TemporalType.TIMESTAMP)
    private java.util.Date actiondate;
    private String ipaddress;
    private String role;
    private String action;

    @javax.persistence.Column(name = "user")
    @Basic
    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    @javax.persistence.Column(name = "actiondate")
    public java.util.Date getActiondate() {
        return actiondate;
    }

    public void setActiondate(java.util.Date actiondate) {
        this.actiondate = actiondate;
    }

    @javax.persistence.Column(name = "ipaddress")
    @Basic
    public String getIpaddress() {
        return ipaddress;
    }

    public void setIpaddress(String ipaddress) {
        this.ipaddress = ipaddress;
    }

    @javax.persistence.Column(name = "role")
    @Basic
    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @javax.persistence.Column(name = "action")
    @Basic
    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Logevents logevents = (Logevents) o;

        if (action != null ? !action.equals(logevents.action) : logevents.action != null) return false;
        if (actiondate != null ? !actiondate.equals(logevents.actiondate) : logevents.actiondate != null) return false;
        if (ipaddress != null ? !ipaddress.equals(logevents.ipaddress) : logevents.ipaddress != null) return false;
        if (role != null ? !role.equals(logevents.role) : logevents.role != null) return false;
        if (user != null ? !user.equals(logevents.user) : logevents.user != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = user != null ? user.hashCode() : 0;
        result = 31 * result + (actiondate != null ? actiondate.hashCode() : 0);
        result = 31 * result + (ipaddress != null ? ipaddress.hashCode() : 0);
        result = 31 * result + (role != null ? role.hashCode() : 0);
        result = 31 * result + (action != null ? action.hashCode() : 0);
        return result;
    }
}
