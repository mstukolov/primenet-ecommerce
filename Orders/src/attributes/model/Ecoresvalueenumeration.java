package attributes.model;

import org.orders.entity.BaseEntityAudit;

import javax.persistence.AttributeOverride;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;


@Entity
@javax.persistence.Table(name = "ecoresvalueenumeration", schema = "", catalog = "orders")
@AttributeOverride(name = "recid", column = @Column(name = "recid",
        nullable = false, columnDefinition = "BIGINT UNSIGNED"))
public class Ecoresvalueenumeration extends BaseEntityAudit {
    private long instanceRelationType;
    private String textValue;

    @javax.persistence.Column(name = "InstanceRelationType")
    @Basic
    public long getInstanceRelationType() {
        return instanceRelationType;
    }

    public void setInstanceRelationType(long instanceRelationType) {
        this.instanceRelationType = instanceRelationType;
    }

    @javax.persistence.Column(name = "TextValue")
    @Basic
    public String getTextValue() {
        return textValue;
    }

    public void setTextValue(String textValue) {
        this.textValue = textValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Ecoresvalueenumeration that = (Ecoresvalueenumeration) o;

        if (instanceRelationType != that.instanceRelationType) return false;
        if (textValue != null ? !textValue.equals(that.textValue) : that.textValue != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (instanceRelationType ^ (instanceRelationType >>> 32));
        result = 31 * result + (textValue != null ? textValue.hashCode() : 0);
        return result;
    }
}
