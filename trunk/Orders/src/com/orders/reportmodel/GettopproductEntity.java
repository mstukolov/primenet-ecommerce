package com.orders.reportmodel;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Id;


@javax.persistence.Table(name = "gettopproduct", schema = "", catalog = "orders")
@Entity
public class GettopproductEntity {
    @Id
    private String product;
    private Double total;
    private String name;

    @javax.persistence.Column(name = "product")
    @Basic
    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }
    @javax.persistence.Column(name = "name")
    @Basic
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @javax.persistence.Column(name = "total")
    @Basic
    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GettopproductEntity that = (GettopproductEntity) o;

        if (product != null ? !product.equals(that.product) : that.product != null) return false;
        if (total != null ? !total.equals(that.total) : that.total != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = product != null ? product.hashCode() : 0;
        result = 31 * result + (total != null ? total.hashCode() : 0);
        return result;
    }
}
