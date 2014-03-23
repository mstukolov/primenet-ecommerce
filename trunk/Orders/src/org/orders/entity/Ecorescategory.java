package org.orders.entity;

import javax.persistence.*;

@Entity
@javax.persistence.Table(name = "ecorescategory", schema = "", catalog = "orders")
@AttributeOverride(name = "recid", column = @Column(name = "recid",
        nullable = false, columnDefinition = "BIGINT UNSIGNED"))

public class Ecorescategory extends BaseEntityAudit{
    private Long parentrecid;
    private String name;

    @javax.persistence.Column(name = "parentrecid")
    @Basic
    public Long getParentrecid() {
        return parentrecid;
    }

    public void setParentrecid(Long parentrecid) {
        this.parentrecid = parentrecid;
    }

    @javax.persistence.Column(name = "name")
    @Basic
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Ecorescategory that = (Ecorescategory) o;

        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (parentrecid != null ? !parentrecid.equals(that.parentrecid) : that.parentrecid != null) return false;
        if (getRecid() != null ? !getRecid().equals(that.getRecid()) : that.getRecid() != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = getRecid() != null ? getRecid().hashCode() : 0;
        result = 31 * result + (parentrecid != null ? parentrecid.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }
}
