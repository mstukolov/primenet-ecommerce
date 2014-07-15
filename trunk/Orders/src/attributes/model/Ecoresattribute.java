package attributes.model;

import org.orders.entity.BaseEntityAudit;

import javax.persistence.*;

@Entity
@Table(name = "ecoresattribute")
@AttributeOverride(name = "recid", column = @Column(name = "recid",
        nullable = false, columnDefinition = "BIGINT UNSIGNED"))
public class Ecoresattribute extends BaseEntityAudit {
    private String attributeName;
    private long attributeGroupRef;
    private long attributeTypeRef;
    private boolean inItemCardFill;
    private boolean inItemCardShow;
    private boolean isFilterBuild;

    @Column(name = "attributeName")
    @Basic
    public String getAttributeName() {
        return attributeName;
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    @Column(name = "attributeGroupRef")
    @Basic
    public long getAttributeGroupRef() {
        return attributeGroupRef;
    }

    public void setAttributeGroupRef(long attributeGroupRef) {
        this.attributeGroupRef = attributeGroupRef;
    }

    @Column(name = "attributeTypeRef")
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

    @Column(name = "InItemCardFill")
    @Basic
    public boolean isInItemCardFill() {
        return inItemCardFill;
    }

    public void setInItemCardFill(boolean inItemCardFill) {
        this.inItemCardFill = inItemCardFill;
    }

    @Column(name = "InItemCardShow")
    @Basic
    public boolean isInItemCardShow() {
        return inItemCardShow;
    }

    public void setInItemCardShow(boolean inItemCardShow) {
        this.inItemCardShow = inItemCardShow;
    }

    @Column(name = "IsFilterBuild")
    @Basic
    public boolean isFilterBuild() {
        return isFilterBuild;
    }

    public void setFilterBuild(boolean filterBuild) {
        isFilterBuild = filterBuild;
    }
}
