package org.orders.entity;


public class ShopingCartItem {
    private Double cartItemId;
    private Proposal proposal;
    private Double Qty;
    private Double price;
    private Double Amount;
    private String configuration;


    public ShopingCartItem() {
        this.cartItemId = Math.random();
    }

    public String getConfiguration() {
        return configuration;
    }

    public void setConfiguration(String configuration) {
        this.configuration = configuration;
    }

    public Double getCartItemId() {
        return cartItemId;
    }

    public void setCartItemId(Double cartItemId) {
        this.cartItemId = cartItemId;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Proposal getProposal() {
        return proposal;
    }

    public void setProposal(Proposal proposal) {
        this.proposal = proposal;
    }

    public Double getQty() {
        return Qty;
    }

    public void setQty(Double qty) {
        Qty = qty;
    }

    public Double getAmount() {
        return Amount;
    }

    public void setAmount(Double amount) {
        Amount = amount;
    }
}
