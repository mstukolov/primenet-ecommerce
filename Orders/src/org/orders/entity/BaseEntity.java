package org.orders.entity;

import javax.persistence.*;
import java.io.Serializable;

@MappedSuperclass
public class BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "dataareaid", nullable = true, columnDefinition = "BIGINT UNSIGNED")
    protected Long dataareaid;

    public Long getDataareaid() {
        return dataareaid;
    }

    public void setDataareaid(Long dataareaid) {
        this.dataareaid = dataareaid;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (this.getDataareaid() != null ? this.getDataareaid().hashCode() : 0);

        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object)
            return true;
        if (object == null)
            return false;
        if (getClass() != object.getClass())
            return false;

        BaseEntity other = (BaseEntity) object;
        if (this.getDataareaid() != other.getDataareaid() && (this.getDataareaid() == null || !this.getDataareaid().equals(other.getDataareaid()))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return this.getClass().getName() + " [ID=" + getDataareaid() + "]";
    }
}
