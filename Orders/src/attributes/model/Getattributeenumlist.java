package attributes.model;

import javax.persistence.*;


@Entity
@javax.persistence.Table(name = "Getattributeenumlist", schema = "", catalog = "orders")

public class Getattributeenumlist  {

    private long attributeRecid;
    private String attributeName;
    private long typeRecid;
    private String typeName;
    private Boolean isEnumeration;
    @Id
    private String textValue;

    //@EmbeddedId
    //private ViewPK viewPK;

    @javax.persistence.Column(name = "attributeRecid")
    @Basic
    public long getAttributeRecid() {
        return attributeRecid;
    }

    public void setAttributeRecid(long attributeRecid) {
        this.attributeRecid = attributeRecid;
    }

    @javax.persistence.Column(name = "attributeName")
    @Basic
    public String getAttributeName() {
        return attributeName;
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    @javax.persistence.Column(name = "typeRecid")
    @Basic
    public long getTypeRecid() {
        return typeRecid;
    }

    public void setTypeRecid(long typeRecid) {
        this.typeRecid = typeRecid;
    }

    @javax.persistence.Column(name = "typeName")
    @Basic
    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    @javax.persistence.Column(name = "isEnumeration")
    @Basic
    public Boolean getEnumeration() {
        return isEnumeration;
    }

    public void setEnumeration(Boolean enumeration) {
        isEnumeration = enumeration;
    }

    @javax.persistence.Column(name = "textValue")
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

        Getattributeenumlist that = (Getattributeenumlist) o;

        if (attributeRecid != that.attributeRecid) return false;
        if (typeRecid != that.typeRecid) return false;
        if (attributeName != null ? !attributeName.equals(that.attributeName) : that.attributeName != null)
            return false;
        if (isEnumeration != null ? !isEnumeration.equals(that.isEnumeration) : that.isEnumeration != null)
            return false;
        if (textValue != null ? !textValue.equals(that.textValue) : that.textValue != null) return false;
        if (typeName != null ? !typeName.equals(that.typeName) : that.typeName != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (attributeRecid ^ (attributeRecid >>> 32));
        result = 31 * result + (attributeName != null ? attributeName.hashCode() : 0);
        result = 31 * result + (int) (typeRecid ^ (typeRecid >>> 32));
        result = 31 * result + (typeName != null ? typeName.hashCode() : 0);
        result = 31 * result + (isEnumeration != null ? isEnumeration.hashCode() : 0);
        result = 31 * result + (textValue != null ? textValue.hashCode() : 0);
        return result;
    }


}
