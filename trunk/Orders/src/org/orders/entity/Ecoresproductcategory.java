package org.orders.entity;

import javax.persistence.AttributeOverride;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;


@Entity
@javax.persistence.Table(name = "ecoresproductcategory", schema = "", catalog = "orders")
@AttributeOverride(name = "recid", column = @Column(name = "recid", nullable = false, columnDefinition = "BIGINT UNSIGNED"))
public class Ecoresproductcategory extends BaseEntityAudit{

    private Long category;
    private Long product;

    @javax.persistence.Column(name = "Category")
    @Basic
    public Long getCategory() {
        return category;
    }

    public void setCategory(Long category) {
        this.category = category;
    }

    @javax.persistence.Column(name = "Product")
    @Basic
    public Long getProduct() {
        return product;
    }

    public void setProduct(Long product) {
        this.product = product;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Ecoresproductcategory that = (Ecoresproductcategory) o;

        if (category != null ? !category.equals(that.category) : that.category != null) return false;
        if (product != null ? !product.equals(that.product) : that.product != null) return false;
        if (getRecid() != null ? !getRecid().equals(that.getRecid()) : that.getRecid() != null) return false;
        if (getVersion() != null ? !getVersion().equals(that.getVersion()) : that.getVersion() != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = getRecid() != null ? getRecid().hashCode() : 0;
        result = 31 * result + (category != null ? category.hashCode() : 0);
        result = 31 * result + (product != null ? product.hashCode() : 0);
        result = 31 * result + (getVersion() != null ? getVersion().hashCode() : 0);
        return result;
    }
}
