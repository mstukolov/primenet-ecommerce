package attributes.model;

import org.orders.entity.BaseEntityAudit;

import javax.persistence.AttributeOverride;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;


@Entity
@javax.persistence.Table(name = "ecoresattributegroup", schema = "", catalog = "orders")
@AttributeOverride(name = "recid", column = @Column(name = "recid",
        nullable = false, columnDefinition = "BIGINT UNSIGNED"))
public class Ecoresattributegroup extends BaseEntityAudit {

    private String name;


    @javax.persistence.Column(name = "name")
    @Basic
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Ecoresattributegroup that = (Ecoresattributegroup) o;

        if (getRecid() != that.getRecid()) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (getRecid() ^ (getRecid() >>> 32));
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }
}
