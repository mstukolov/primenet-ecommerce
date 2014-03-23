package org.orders.entity;

import javax.persistence.AttributeOverride;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import java.sql.Date;


@Entity
@javax.persistence.Table(name = "proposal", schema = "", catalog = "orders")
@AttributeOverride(name = "recid", column = @Column(name = "recid",
        nullable = false, columnDefinition = "BIGINT UNSIGNED"))
public class Proposal extends BaseEntityAudit{

    private Long product;
    private Long price;
    private Long minQty;
    private Long qty;
    private Long startQty;
    private String deliveryTerms;
    private Date deliveryDate;
    private String paymentTerms;
    private Boolean blocked;
    private Boolean fullFilled;
    private Boolean promo;
    private String description;

    @javax.persistence.Column(name = "description")
    @Basic
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
    public Long getMinQty() {
        return minQty;
    }

    public void setMinQty(Long minQty) {
        this.minQty = minQty;
    }

    @javax.persistence.Column(name = "qty")
    @Basic
    public Long getQty() {
        return qty;
    }

    public void setQty(Long qty) {
        this.qty = qty;
    }

    @javax.persistence.Column(name = "startQty")
    @Basic
    public Long getStartQty() {
        return startQty;
    }

    public void setStartQty(Long startQty) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Proposal proposal = (Proposal) o;

        if (blocked != null ? !blocked.equals(proposal.blocked) : proposal.blocked != null) return false;
        if (deliveryDate != null ? !deliveryDate.equals(proposal.deliveryDate) : proposal.deliveryDate != null)
            return false;
        if (deliveryTerms != null ? !deliveryTerms.equals(proposal.deliveryTerms) : proposal.deliveryTerms != null)
            return false;
        if (fullFilled != null ? !fullFilled.equals(proposal.fullFilled) : proposal.fullFilled != null) return false;
        if (minQty != null ? !minQty.equals(proposal.minQty) : proposal.minQty != null) return false;
        if (paymentTerms != null ? !paymentTerms.equals(proposal.paymentTerms) : proposal.paymentTerms != null)
            return false;
        if (price != null ? !price.equals(proposal.price) : proposal.price != null) return false;
        if (product != null ? !product.equals(proposal.product) : proposal.product != null) return false;
        if (promo != null ? !promo.equals(proposal.promo) : proposal.promo != null) return false;
        if (qty != null ? !qty.equals(proposal.qty) : proposal.qty != null) return false;
        if (getRecid() != null ? !getRecid().equals(proposal.getRecid()) : proposal.getRecid() != null) return false;
        if (startQty != null ? !startQty.equals(proposal.startQty) : proposal.startQty != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = getRecid() != null ? getRecid().hashCode() : 0;
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
        return result;
    }
}
