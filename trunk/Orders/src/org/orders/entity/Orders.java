package org.orders.entity;

import javax.persistence.*;
import java.sql.Date;


@Entity
@javax.persistence.Table(name = "orders", schema = "", catalog = "orders")
@AttributeOverride(name = "recid", column = @Column(name = "recid",
        nullable = false, columnDefinition = "BIGINT UNSIGNED"))
public class Orders extends BaseEntityAudit{
    private Long salesId;
    private Long proposal;
    private Long product;
    private Long price;
    private Double minQty;
    private Double qty;
    private Double startQty;
    private Double amount;
    private String deliveryTerms;
    private Date deliveryDate;
    private String paymentTerms;
    private String status;
    private Boolean blocked;
    private Boolean bonus;
    private Boolean fullFilled;
    private Boolean promo;
    private String description;
    private String customer;

    @javax.persistence.Column(name = "SalesId")
    @Basic
    public Long getSalesId() {
        return salesId;
    }

    public void setSalesId(Long salesId) {
        this.salesId = salesId;
    }

    @javax.persistence.Column(name = "customer")
    @Basic
    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    @javax.persistence.Column(name = "bonus")
    @Basic
    public Boolean getBonus() {
        return bonus;
    }

    public void setBonus(Boolean bonus) {
        this.bonus = bonus;
    }

    @javax.persistence.Column(name = "status")
    @Basic
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @javax.persistence.Column(name = "amount")
    @Basic
    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
    @javax.persistence.Column(name = "proposal")
    @Basic
    public Long getProposal() {
        return proposal;
    }

    public void setProposal(Long proposal) {
        this.proposal = proposal;
    }

    @javax.persistence.Column(name = "product")
    @Basic
    public Long getProduct() {
        return product;
    }

    public void setProduct(Long product) {
        this.product = product;
    }

    @javax.persistence.Column(name = "price")
    @Basic
    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    @javax.persistence.Column(name = "minQty")
    @Basic
    public Double getMinQty() {
        return minQty;
    }

    public void setMinQty(Double minQty) {
        this.minQty = minQty;
    }

    @javax.persistence.Column(name = "qty")
    @Basic
    public Double getQty() {
        return qty;
    }

    public void setQty(Double qty) {
        this.qty = qty;
    }

    @javax.persistence.Column(name = "startQty")
    @Basic
    public Double getStartQty() {
        return startQty;
    }

    public void setStartQty(Double startQty) {
        this.startQty = startQty;
    }

    @javax.persistence.Column(name = "deliveryTerms")
    @Basic
    public String getDeliveryTerms() {
        return deliveryTerms;
    }

    public void setDeliveryTerms(String deliveryTerms) {
        this.deliveryTerms = deliveryTerms;
    }

    @javax.persistence.Column(name = "deliveryDate")
    @Basic
    public Date getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(Date deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    @javax.persistence.Column(name = "paymentTerms")
    @Basic
    public String getPaymentTerms() {
        return paymentTerms;
    }

    public void setPaymentTerms(String paymentTerms) {
        this.paymentTerms = paymentTerms;
    }

    @javax.persistence.Column(name = "blocked")
    @Basic
    public Boolean getBlocked() {
        return blocked;
    }

    public void setBlocked(Boolean blocked) {
        this.blocked = blocked;
    }

    @javax.persistence.Column(name = "fullFilled")
    @Basic
    public Boolean getFullFilled() {
        return fullFilled;
    }

    public void setFullFilled(Boolean fullFilled) {
        this.fullFilled = fullFilled;
    }

    @javax.persistence.Column(name = "promo")
    @Basic
    public Boolean getPromo() {
        return promo;
    }

    public void setPromo(Boolean promo) {
        this.promo = promo;
    }

    @javax.persistence.Column(name = "description")
    @Basic
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Orders orders = (Orders) o;

        if (blocked != null ? !blocked.equals(orders.blocked) : orders.blocked != null) return false;
        if (deliveryDate != null ? !deliveryDate.equals(orders.deliveryDate) : orders.deliveryDate != null)
            return false;
        if (deliveryTerms != null ? !deliveryTerms.equals(orders.deliveryTerms) : orders.deliveryTerms != null)
            return false;
        if (description != null ? !description.equals(orders.description) : orders.description != null) return false;
        if (fullFilled != null ? !fullFilled.equals(orders.fullFilled) : orders.fullFilled != null) return false;
        if (minQty != null ? !minQty.equals(orders.minQty) : orders.minQty != null) return false;
        if (paymentTerms != null ? !paymentTerms.equals(orders.paymentTerms) : orders.paymentTerms != null)
            return false;
        if (price != null ? !price.equals(orders.price) : orders.price != null) return false;
        if (product != null ? !product.equals(orders.product) : orders.product != null) return false;
        if (promo != null ? !promo.equals(orders.promo) : orders.promo != null) return false;
        if (proposal != null ? !proposal.equals(orders.proposal) : orders.proposal != null) return false;
        if (qty != null ? !qty.equals(orders.qty) : orders.qty != null) return false;
        if (startQty != null ? !startQty.equals(orders.startQty) : orders.startQty != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = proposal != null ? proposal.hashCode() : 0;
        result = 31 * result + (product != null ? product.hashCode() : 0);
        result = 31 * result + (price != null ? price.hashCode() : 0);
        result = 31 * result + (minQty != null ? minQty.hashCode() : 0);
        result = 31 * result + (qty != null ? qty.hashCode() : 0);
        result = 31 * result + (startQty != null ? startQty.hashCode() : 0);
        result = 31 * result + (deliveryTerms != null ? deliveryTerms.hashCode() : 0);
        result = 31 * result + (deliveryDate != null ? deliveryDate.hashCode() : 0);
        result = 31 * result + (paymentTerms != null ? paymentTerms.hashCode() : 0);
        result = 31 * result + (blocked != null ? blocked.hashCode() : 0);
        result = 31 * result + (fullFilled != null ? fullFilled.hashCode() : 0);
        result = 31 * result + (promo != null ? promo.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        return result;
    }

}
