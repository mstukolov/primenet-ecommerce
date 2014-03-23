package attributes.model;

import org.orders.entity.BaseEntityAudit;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "ecoresvalue", schema = "", catalog = "orders")
@AttributeOverride(name = "recid", column = @Column(name = "recid",
        nullable = false, columnDefinition = "BIGINT UNSIGNED"))
public class Ecoresvalue extends BaseEntityAudit {

    private long relationType;
    private String textValue;
    private Integer intValue;
    private Boolean booleanValue;
    private Float floatValue;

    @Column(name = "DateTimeValue")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateTimeValue;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Ecoresvalue that = (Ecoresvalue) o;

        if (getRecid() != that.getRecid()) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (getRecid() ^ (getRecid() >>> 32));
        return result;
    }

    @Column(name = "relationType")
    @Basic
    public long getRelationType() {
        return relationType;
    }

    public void setRelationType(long relationType) {
        this.relationType = relationType;
    }

    @Column(name = "TextValue")
    @Basic
    public String getTextValue() {
        return textValue;
    }

    public void setTextValue(String textValue) {
        this.textValue = textValue;
    }

    @Column(name = "IntValue")
    @Basic
    public Integer getIntValue() {
        return intValue;
    }

    public void setIntValue(Integer intValue) {
        this.intValue = intValue;
    }

    @Column(name = "BooleanValue")
    @Basic
    public Boolean getBooleanValue() {
        return booleanValue;
    }

    public void setBooleanValue(Boolean booleanValue) {
        this.booleanValue = booleanValue;
    }

    public Date getDateTimeValue() {
        return dateTimeValue;
    }

    public void setDateTimeValue(Date dateTimeValue) {
        this.dateTimeValue = dateTimeValue;
    }

    @Column(name = "FloatValue")
    @Basic
    public Float getFloatValue() {
        return floatValue;
    }

    public void setFloatValue(Float floatValue) {
        this.floatValue = floatValue;
    }
}
