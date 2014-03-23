package attributes.model;

import org.orders.entity.BaseEntityAudit;

import javax.persistence.*;

@Entity
@javax.persistence.Table(name = "Ecoresattribute", schema = "", catalog = "orders")
@AttributeOverride(name = "recid", column = @Column(name = "recid",
        nullable = false, columnDefinition = "BIGINT UNSIGNED"))
public class Ecoresattribute extends BaseEntityAudit {
    private String attributeName;
    private long attributeGroupRef;
    private long attributeTypeRef;

    @javax.persistence.Column(name = "attributeName")
    @Basic
    public String getAttributeName() {
        return attributeName;
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    @javax.persistence.Column(name = "attributeGroupRef")
    @Basic
    public long getAttributeGroupRef() {
        return attributeGroupRef;
    }

    public void setAttributeGroupRef(long attributeGroupRef) {
        this.attributeGroupRef = attributeGroupRef;
    }

    @javax.persistence.Column(name = "attributeTypeRef")
    @Basic
    public long getAttributeTypeRef() {
        return attributeTypeRef;
    }

    public void setAttributeTypeRef(long attributeTypeRef) {
        this.attributeTypeRef = attributeTypeRef;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Ecoresattribute that = (Ecoresattribute) o;

        if (attributeGroupRef != that.attributeGroupRef) return false;
        if (attributeTypeRef != that.attributeTypeRef) return false;
        if (getRecid() != that.getRecid()) return false;
        if (attributeName != null ? !attributeName.equals(that.attributeName) : that.attributeName != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (getRecid() ^ (getRecid() >>> 32));
        result = 31 * result + (attributeName != null ? attributeName.hashCode() : 0);
        result = 31 * result + (int) (attributeGroupRef ^ (attributeGroupRef >>> 32));
        result = 31 * result + (int) (attributeTypeRef ^ (attributeTypeRef >>> 32));
        return result;
    }
}
