package org.orders.entity;


public class SelectedProduct {
    private Product product;
    private Boolean selected;
    private Ecorescategory ecorescategory;

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Boolean getSelected() {
        return selected;
    }

    public void setSelected(Boolean selected) {
        this.selected = selected;
    }

    public Ecorescategory getEcorescategory() {
        return ecorescategory;
    }

    public void setEcorescategory(Ecorescategory ecorescategory) {
        this.ecorescategory = ecorescategory;
    }
}
