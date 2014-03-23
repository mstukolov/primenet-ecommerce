package attributes.model;

import org.orders.entity.BaseEntityAudit;

import javax.persistence.*;

@Entity
@Table(name = "ecoresproductattributevalue", schema = "", catalog = "orders")
@AttributeOverride(name = "recid", column = @Column(name = "recid",
        nullable = false, columnDefinition = "BIGINT UNSIGNED"))
public class Ecoresproductattributevalue extends BaseEntityAudit {

    private long productRef;
    private Long ecoResValueRef;
    private String relationType;
    private long attributeRef;

    @Column(name = "productRef")
    @Basic
    public long getProductRef() {
        return productRef;
    }

    public void setProductRef(long productRef) {
        this.productRef = productRef;
    }

    @Column(name = "ecoResValueRef")
    @Basic
    public Long getEcoResValueRef() {
        return ecoResValueRef;
    }

    public void setEcoResValueRef(Long ecoResValueRef) {
        this.ecoResValueRef = ecoResValueRef;
    }

    @Column(name = "relationType")
    @Basic
    public String getRelationType() {
        return relationType;
    }

    public void setRelationType(String relationType) {
        this.relationType = relationType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Ecoresproductattributevalue that = (Ecoresproductattributevalue) o;

        if (productRef != that.productRef) return false;
        if (getRecid() != that.getRecid()) return false;
        if (ecoResValueRef != null ? !ecoResValueRef.equals(that.ecoResValueRef) : that.ecoResValueRef != null)
            return false;
        if (relationType != null ? !relationType.equals(that.relationType) : that.relationType != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (getRecid() ^ (getRecid() >>> 32));
        result = 31 * result + (int) (productRef ^ (productRef >>> 32));
        result = 31 * result + (ecoResValueRef != null ? ecoResValueRef.hashCode() : 0);
        result = 31 * result + (relationType != null ? relationType.hashCode() : 0);
        return result;
    }

    @Column(name = "attributeRef")
    @Basic
    public long getAttributeRef() {
        return attributeRef;
    }

    public void setAttributeRef(long attributeRef) {
        this.attributeRef = attributeRef;
    }
}
