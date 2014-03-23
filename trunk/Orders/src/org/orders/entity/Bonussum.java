package org.orders.entity;

import javax.persistence.AttributeOverride;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;


@Entity
@javax.persistence.Table(name = "bonussum", schema = "", catalog = "orders")
@AttributeOverride(name = "recid", column = @Column(name = "recid",
        nullable = false, columnDefinition = "BIGINT UNSIGNED"))
public class Bonussum extends BaseEntityAudit{

    private String customer;
    private Double balls;

    @javax.persistence.Column(name = "customer")
    @Basic
    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    @javax.persistence.Column(name = "balls")
    @Basic
    public Double getBalls() {
        return balls;
    }

    public void setBalls(Double balls) {
        this.balls = balls;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Bonussum bonussum = (Bonussum) o;

        if (getRecid() != bonussum.getRecid()) return false;
        if (balls != null ? !balls.equals(bonussum.balls) : bonussum.balls != null) return false;
        if (customer != null ? !customer.equals(bonussum.customer) : bonussum.customer != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (getRecid() ^ (getRecid() >>> 32));
        result = 31 * result + (customer != null ? customer.hashCode() : 0);
        result = 31 * result + (balls != null ? balls.hashCode() : 0);
        return result;
    }
}
