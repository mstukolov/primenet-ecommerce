package org.orders.entity;

import javax.persistence.AttributeOverride;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;


@Entity
@javax.persistence.Table(name = "relevantproducts", schema = "", catalog = "orders")
@AttributeOverride(name = "recid", column = @Column(name = "recid",
        nullable = false, columnDefinition = "BIGINT UNSIGNED"))
public class Relevantproducts extends BaseEntityAudit{


    private Long product;
    private Long relevantprod;


    @javax.persistence.Column(name = "product")
    @Basic
    public Long getProduct() {
        return product;
    }

    public void setProduct(Long product) {
        this.product = product;
    }

    @javax.persistence.Column(name = "relevantprod")
    @Basic
    public Long getRelevantprod() {
        return relevantprod;
    }

    public void setRelevantprod(Long relevantprod) {
        this.relevantprod = relevantprod;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Relevantproducts that = (Relevantproducts) o;

        if (product != null ? !product.equals(that.product) : that.product != null) return false;
        if (getRecid() != null ? !getRecid().equals(that.getRecid()) : that.getRecid() != null) return false;
        if (relevantprod != null ? !relevantprod.equals(that.relevantprod) : that.relevantprod != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = getRecid() != null ? getRecid().hashCode() : 0;
        result = 31 * result + (product != null ? product.hashCode() : 0);
        result = 31 * result + (relevantprod != null ? relevantprod.hashCode() : 0);
        return result;
    }
}
