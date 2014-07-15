package attributes.model;

import org.orders.entity.BaseEntityAudit;

import javax.persistence.AttributeOverride;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;


@Entity
@javax.persistence.Table(name = "ecoresgroupbyattribute", schema = "", catalog = "orders")
@AttributeOverride(name = "recid", column = @Column(name = "recid",
        nullable = false, columnDefinition = "BIGINT UNSIGNED"))
public class Ecoresgroupbyattribute extends BaseEntityAudit {
    private long attributeGroupRef;
    private long attributeRef;

    @javax.persistence.Column(name = "AttributeGroupRef")
    @Basic
    public long getAttributeGroupRef() {
        return attributeGroupRef;
    }

    public void setAttributeGroupRef(long attributeGroupRef) {
        this.attributeGroupRef = attributeGroupRef;
    }

    @javax.persistence.Column(name = "AttributeRef")
    @Basic
    public long getAttributeRef() {
        return attributeRef;
    }

    public void setAttributeRef(long attributeRef) {
        this.attributeRef = attributeRef;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Ecoresgroupbyattribute that = (Ecoresgroupbyattribute) o;

        if (attributeGroupRef != that.attributeGroupRef) return false;
        if (attributeRef != that.attributeRef) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (attributeGroupRef ^ (attributeGroupRef >>> 32));
        result = 31 * result + (int) (attributeRef ^ (attributeRef >>> 32));
        return result;
    }
}
