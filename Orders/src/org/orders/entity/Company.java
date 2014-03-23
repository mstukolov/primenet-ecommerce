package org.orders.entity;

import javax.persistence.*;
import java.util.Arrays;


@Entity
@javax.persistence.Table(name = "company", schema = "", catalog = "orders")
@AttributeOverride(name = "recid", column = @Column(name = "recid",
        nullable = false, columnDefinition = "BIGINT UNSIGNED"))
public class Company extends BaseEntityAudit {
    private String name;
    private String phone;
    private byte[] logo;
    private Boolean active;


    public Company() {
        this.setCreatedBy("Admin");
        this.setUpdatedBy("Admin");
    }
    @javax.persistence.Column(name = "active")
    @Basic
    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    @javax.persistence.Column(name = "name")
    @Basic
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @javax.persistence.Column(name = "phone")
    @Basic
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @javax.persistence.Column(name = "logo")
    @Lob
    public byte[] getLogo() {
        return logo;
    }

    public void setLogo(byte[] logo) {
        this.logo = logo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Company company1 = (Company) o;

        if (getRecid() != null ? !getRecid().equals(company1.getRecid()) : company1.getRecid() != null) return false;
        if (!Arrays.equals(logo, company1.logo)) return false;
        if (name != null ? !name.equals(company1.name) : company1.name != null) return false;
        if (phone != null ? !phone.equals(company1.phone) : company1.phone != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (phone != null ? phone.hashCode() : 0);
        result = 31 * result + (logo != null ? Arrays.hashCode(logo) : 0);
        result = 31 * result + (getRecid() != null ? getRecid().hashCode() : 0);
        return result;
    }
}
