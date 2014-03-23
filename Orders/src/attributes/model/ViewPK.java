package attributes.model;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class ViewPK implements Serializable {
    private String attributeRecid;

    public ViewPK() {
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof ViewPK){
            ViewPK viewPK = (ViewPK) obj;

            if(!viewPK.getAttributeRecid().equals(attributeRecid)){
                return false;
            }

            return true;
        }

        return false;
    }

    @Override
    public int hashCode() {
        return attributeRecid.hashCode();
    }

    public String getAttributeRecid() {
        return attributeRecid;
    }

    public void setAttributeRecid(String attributeRecid) {
        this.attributeRecid = attributeRecid;
    }
}
