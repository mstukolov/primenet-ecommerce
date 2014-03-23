package attributes.model;

import org.orders.entity.BaseEntityAudit;

import javax.persistence.AttributeOverride;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
@javax.persistence.Table(name = "ecorescategoryattributegroup", schema = "", catalog = "orders")
@AttributeOverride(name = "recid", column = @Column(name = "recid",
        nullable = false, columnDefinition = "BIGINT UNSIGNED"))
public class Ecorescategoryattributegroup extends BaseEntityAudit {

    private Long attributeGroupRef;
    private Long ecoResCategoryRef;

    @javax.persistence.Column(name = "attributeGroupRef")
    @Basic
    public Long getAttributeGroupRef() {
        return attributeGroupRef;
    }

    public void setAttributeGroupRef(Long attributeGroupRef) {
        this.attributeGroupRef = attributeGroupRef;
    }

    @javax.persistence.Column(name = "ecoResCategoryRef")
    @Basic
    public Long getEcoResCategoryRef() {
        return ecoResCategoryRef;
    }

    public void setEcoResCategoryRef(Long ecoResCategoryRef) {
        this.ecoResCategoryRef = ecoResCategoryRef;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Ecorescategoryattributegroup that = (Ecorescategoryattributegroup) o;

        if (getRecid() != that.getRecid()) return false;
        if (attributeGroupRef != null ? !attributeGroupRef.equals(that.attributeGroupRef) : that.attributeGroupRef != null)
            return false;
        if (ecoResCategoryRef != null ? !ecoResCategoryRef.equals(that.ecoResCategoryRef) : that.ecoResCategoryRef != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (getRecid() ^ (getRecid() >>> 32));
        result = 31 * result + (attributeGroupRef != null ? attributeGroupRef.hashCode() : 0);
        result = 31 * result + (ecoResCategoryRef != null ? ecoResCategoryRef.hashCode() : 0);
        return result;
    }
}
