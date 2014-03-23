package org.orders.entity;

import javax.persistence.AttributeOverride;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import java.util.Arrays;


@Entity
@javax.persistence.Table(name = "carousel", schema = "", catalog = "orders")
@AttributeOverride(name = "recid", column = @Column(name = "recid",
        nullable = false, columnDefinition = "BIGINT UNSIGNED"))
public class Carousel extends BaseEntityAudit{

    private byte[] image;
    private String url;

    @javax.persistence.Column(name = "url")
    @Basic
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @javax.persistence.Column(name = "image")
    @Basic
    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Carousel carousel = (Carousel) o;

       // if (dataareaid != null ? !dataareaid.equals(carousel.dataareaid) : carousel.dataareaid != null) return false;
        if (!Arrays.equals(image, carousel.image)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = getRecid() != null ? getRecid().hashCode() : 0;
       // result = 31 * result + (dataareaid != null ? dataareaid.hashCode() : 0);
        result = 31 * result + (image != null ? Arrays.hashCode(image) : 0);
        return result;
    }
}
