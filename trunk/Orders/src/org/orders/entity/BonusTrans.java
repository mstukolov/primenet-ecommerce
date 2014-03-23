package org.orders.entity;


import javax.persistence.AttributeOverride;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
@javax.persistence.Table(name = "bonustrans", schema = "", catalog = "orders")
@AttributeOverride(name = "recid", column = @Column(name = "recid",
        nullable = false, columnDefinition = "BIGINT UNSIGNED"))
public class BonusTrans extends BaseEntityAudit{

    private String customer;
    private Double amount;
    private Double balls;
    private String ordernum;

    @javax.persistence.Column(name = "ordernum")
    @Basic
    public String getOrdernum() {
        return ordernum;
    }

    public void setOrdernum(String ordernum) {
        this.ordernum = ordernum;
    }

    @javax.persistence.Column(name = "customer")
    @Basic
    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    @javax.persistence.Column(name = "amount")
    @Basic
    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    @javax.persistence.Column(name = "balls")
    @Basic
    public Double getBalls() {
        return balls;
    }

    public void setBalls(Double balls) {
        this.balls = balls;
    }
}
