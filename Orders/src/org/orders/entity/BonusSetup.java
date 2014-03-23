package org.orders.entity;

import javax.persistence.AttributeOverride;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
@javax.persistence.Table(name = "bonussetup", schema = "", catalog = "orders")
@AttributeOverride(name = "recid", column = @Column(name = "recid",
        nullable = false, columnDefinition = "BIGINT UNSIGNED"))
public class BonusSetup extends BaseEntityAudit{
    private Double balls;
    private Double amount;
    private Double parameter;

    @javax.persistence.Column(name = "balls")
    @Basic
    public Double getBalls() {
        return balls;
    }

    public void setBalls(Double balls) {
        this.balls = balls;
    }

    @javax.persistence.Column(name = "amount")
    @Basic
    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    @javax.persistence.Column(name = "parameter")
    @Basic
    public Double getParameter() {
        return parameter;
    }

    public void setParameter(Double parameter) {
        this.parameter = parameter;
    }
}
