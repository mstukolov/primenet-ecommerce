package attributes.model;

import org.orders.entity.BaseEntityAudit;

import javax.persistence.AttributeOverride;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
@javax.persistence.Table(name = "ecoresattributetype", schema = "", catalog = "orders")
@AttributeOverride(name = "recid", column = @Column(name = "recid",
        nullable = false, columnDefinition = "BIGINT UNSIGNED"))
public class Ecoresattributetype extends BaseEntityAudit {

    private String dataType;
    private Boolean isEnumeration;
    private String typeName;

    @javax.persistence.Column(name = "isEnumeration")
    @Basic
    public Boolean getEnumeration() {
        return isEnumeration;
    }

    public void setEnumeration(Boolean enumeration) {
        isEnumeration = enumeration;
    }
    @javax.persistence.Column(name = "typeName")
    @Basic
    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    @javax.persistence.Column(name = "dataType")
    @Basic
    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }





    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Ecoresattributetype that = (Ecoresattributetype) o;

        if (getRecid() != that.getRecid()) return false;
        if (dataType != null ? !dataType.equals(that.dataType) : that.dataType != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (getRecid() ^ (getRecid() >>> 32));
        result = 31 * result + (dataType != null ? dataType.hashCode() : 0);
        return result;
    }
}
