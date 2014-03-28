package attributes.core;

public class AttributeValueCount{
    public String value;
    public Integer count;

    AttributeValueCount(String value, Integer count) {
        this.value = value;
        this.count = count;
    }

    public String getValue() {
        return value;
    }

    void setValue(String value) {
        this.value = value;
    }

    public Integer getCount() {
        return count;
    }

    void setCount(Integer count) {
        this.count = count;
    }
}
