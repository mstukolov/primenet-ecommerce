package org.orders.entity;

import javax.persistence.*;
import java.util.Date;

@MappedSuperclass
public class BaseEntityAudit{

    @Id
    @TableGenerator(name="TABLEGEN", table="sequence_table", pkColumnName="SEQ_NAME",
            valueColumnName="COUNT", pkColumnValue="COMP_SEQ")
    @GeneratedValue(strategy=GenerationType.TABLE, generator="TABLEGEN")
    @javax.persistence.Column(name = "recid")
    private Long recid;

    public Long getRecid() {
        return recid;
    }

    public void setRecid(Long recid) {
        this.recid = recid;
    }

    @Column(name = "version")
    @Version
    private Long version;


    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Column(name = "created_by", length = 20)
    private String createdBy;

    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;

    @Column(name = "updated_by", length = 20)
    private String updatedBy;

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    /**
     * Sets createdAt before insert
     */
    @PrePersist
    public void setCreationDate() {
        this.createdAt = new Date();
        this.createdBy = "Admin";
        this.updatedAt = new Date();
        this.updatedBy = "Admin";
    }

    /**
     * Sets updatedAt before update
     */
    @PreUpdate
    public void setChangeDate() {
        this.updatedAt = new Date();
    }
}
