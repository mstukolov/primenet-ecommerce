package com.orders.reportmodel;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.sql.Date;


@javax.persistence.Table(name = "getsaleslastmonth", schema = "", catalog = "orders")
@Entity
public class GetsaleslastmonthView {
    @Id
    private String salesId;
    private Date processing_At;
    private String product;
    private Double total;

    @javax.persistence.Column(name = "salesId")
    @Basic
    public String getSalesId() {
        return salesId;
    }

    public void setSalesId(String salesId) {
        this.salesId = salesId;
    }

    @javax.persistence.Column(name = "processing_at")
    @Basic
    public Date getProcessing_At() {
        return processing_At;
    }

    public void setProcessing_At(Date processing_At) {
        this.processing_At = processing_At;
    }

    @javax.persistence.Column(name = "product")
    @Basic
    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
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

        GetsaleslastmonthView that = (GetsaleslastmonthView) o;

        if (product != null ? !product.equals(that.product) : that.product != null) return false;
        if (salesId != null ? !salesId.equals(that.salesId) : that.salesId != null) return false;
        if (total != null ? !total.equals(that.total) : that.total != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = salesId != null ? salesId.hashCode() : 0;
        result = 31 * result + (product != null ? product.hashCode() : 0);
        result = 31 * result + (total != null ? total.hashCode() : 0);
        return result;
    }
}
