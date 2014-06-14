package attributes.model;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.sql.Timestamp;

@javax.persistence.Table(name = "getproductattributesvalues", schema = "", catalog = "orders")
@Entity
public class ProductAttributesvaluesView {
    @Id
    private long recid;
    private long productRef;
    private String attributeName;
    private long relationType;
    private String textValue;
    private Timestamp dateTimeValue;
    private Float floatValue;
    private Boolean booleanValue;
    private boolean inItemCardFill;
    private boolean inItemCardShow;
    private boolean isFilterBuild;

    private boolean isEnumeration;
    private long attributeTypeRecid;

    @javax.persistence.Column(name = "isEnumeration")
    @Basic
    public boolean isEnumeration() {
        return isEnumeration;
    }

    public void setEnumeration(boolean enumeration) {
        isEnumeration = enumeration;
    }

    @javax.persistence.Column(name = "attributeTypeRecid")
    @Basic
    public long getAttributeTypeRecid() {
        return attributeTypeRecid;
    }

    public void setAttributeTypeRecid(long attributeTypeRecid) {
        this.attributeTypeRecid = attributeTypeRecid;
    }

    @javax.persistence.Column(name = "recid")
    @Basic
    public long getRecid() {
        return recid;
    }

    public void setRecid(long recid) {
        this.recid = recid;
    }

    @javax.persistence.Column(name = "productRef")
    @Basic
    public long getProductRef() {
        return productRef;
    }

    public void setProductRef(long productRef) {
        this.productRef = productRef;
    }

    @javax.persistence.Column(name = "attributeName")
    @Basic
    public String getAttributeName() {
        return attributeName;
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    @javax.persistence.Column(name = "relationType")
    @Basic
    public long getRelationType() {
        return relationType;
    }

    public void setRelationType(long relationType) {
        this.relationType = relationType;
    }

    @javax.persistence.Column(name = "TextValue")
    @Basic
    public String getTextValue() {
        return textValue;
    }

    public void setTextValue(String textValue) {
        this.textValue = textValue;
    }

    @javax.persistence.Column(name = "DateTimeValue")
    @Basic
    public Timestamp getDateTimeValue() {
        return dateTimeValue;
    }

    public void setDateTimeValue(Timestamp dateTimeValue) {
        this.dateTimeValue = dateTimeValue;
    }

    @javax.persistence.Column(name = "FloatValue")
    @Basic
    public Float getFloatValue() {
        return floatValue;
    }

    public void setFloatValue(Float floatValue) {
        this.floatValue = floatValue;
    }

    @javax.persistence.Column(name = "BooleanValue")
    @Basic
    public Boolean getBooleanValue() {
        return booleanValue;
    }

    public void setBooleanValue(Boolean booleanValue) {
        this.booleanValue = booleanValue;
    }
    @javax.persistence.Column(name = "InItemCardFill")
    @Basic
    public boolean isInItemCardFill() {
        return inItemCardFill;
    }

    public void setInItemCardFill(boolean inItemCardFill) {
        this.inItemCardFill = inItemCardFill;
    }

    @javax.persistence.Column(name = "InItemCardShow")
    @Basic
    public boolean isInItemCardShow() {
        return inItemCardShow;
    }

    public void setInItemCardShow(boolean inItemCardShow) {
        this.inItemCardShow = inItemCardShow;
    }

    @javax.persistence.Column(name = "IsFilterBuild")
    @Basic
    public boolean isFilterBuild() {
        return isFilterBuild;
    }

    public void setFilterBuild(boolean filterBuild) {
        isFilterBuild = filterBuild;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ProductAttributesvaluesView that = (ProductAttributesvaluesView) o;

        if (productRef != that.productRef) return false;
        if (recid != that.recid) return false;
        if (relationType != that.relationType) return false;
        if (attributeName != null ? !attributeName.equals(that.attributeName) : that.attributeName != null)
            return false;
        if (booleanValue != null ? !booleanValue.equals(that.booleanValue) : that.booleanValue != null) return false;
        if (dateTimeValue != null ? !dateTimeValue.equals(that.dateTimeValue) : that.dateTimeValue != null)
            return false;
        if (floatValue != null ? !floatValue.equals(that.floatValue) : that.floatValue != null) return false;
        if (textValue != null ? !textValue.equals(that.textValue) : that.textValue != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (recid ^ (recid >>> 32));
        result = 31 * result + (int) (productRef ^ (productRef >>> 32));
        result = 31 * result + (attributeName != null ? attributeName.hashCode() : 0);
        result = 31 * result + (int) (relationType ^ (relationType >>> 32));
        result = 31 * result + (textValue != null ? textValue.hashCode() : 0);
        result = 31 * result + (dateTimeValue != null ? dateTimeValue.hashCode() : 0);
        result = 31 * result + (floatValue != null ? floatValue.hashCode() : 0);
        result = 31 * result + (booleanValue != null ? booleanValue.hashCode() : 0);
        return result;
    }
}
