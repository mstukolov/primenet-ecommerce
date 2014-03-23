package org.orders.entity;


import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class ShopingCart {
    private static Logger _log = Logger.getLogger(ShopingCart.class.getName());

    private Integer shopingCartId;
    private List<ShopingCartItem> shopingCartItemList =  new ArrayList<ShopingCartItem>();
    private Users user;
    private ShopingCartItem shopingCartItem;
    private Double cartBalance;


    public Boolean isEmpty(){
        if(shopingCartItemList.isEmpty()) {return true;}
        else return false;
    }
    public void insertItem(Proposal proposal, Double orderQty){
        shopingCartItem = new ShopingCartItem();
        shopingCartItem.setProposal(proposal);
        shopingCartItem.setQty(orderQty);
        shopingCartItem.setAmount(orderQty * proposal.getPrice());
        shopingCartItemList.add(shopingCartItem);
        calcBalance();
    }

    public void qtyPlus(ShopingCartItem shopingCartItem) {
        Double qty = shopingCartItem.getQty() + shopingCartItem.getProposal().getMinQty().doubleValue();
        Double amount = qty* shopingCartItem.getProposal().getPrice();
        shopingCartItem.setAmount(amount);
        shopingCartItem.setQty(qty);
        calcBalance();
    }
    public void qtyMinus(ShopingCartItem shopingCartItem) {
        if(shopingCartItem.getQty() - shopingCartItem.getProposal().getMinQty() > 0){
            Double qty = shopingCartItem.getQty() - shopingCartItem.getProposal().getMinQty().doubleValue();
            Double amount = qty* shopingCartItem.getProposal().getPrice();
            shopingCartItem.setAmount(amount);
            shopingCartItem.setQty(qty);
        }else{
            shopingCartItem.setQty(0.0);
        }
        calcBalance();
    }

    public ShopingCartItem findItemById(Double id){
        ShopingCartItem item = new ShopingCartItem();

        for(ShopingCartItem cartItem  : shopingCartItemList){
            if(cartItem.getCartItemId() == id){

                _log.info("Найден элемент корзины: " + cartItem.getCartItemId());
            }
        }

        return item;
    }

    public ShopingCartItem getLastItem() {
        return shopingCartItem;
    }

    public void removeItem(ShopingCartItem item){
        shopingCartItemList.remove(item);
        calcBalance();
    }
    public ShopingCartItem getFirstItem(){
        if(!shopingCartItemList.isEmpty())
        {return shopingCartItemList.get(0);}
        else return null;
    }

    public void clear(){
        if(!shopingCartItemList.isEmpty()){shopingCartItemList.clear();}
        calcBalance();
    }

    public void calcBalance(){
        cartBalance = 0.0;

        if(shopingCartItemList.isEmpty())
        {cartBalance = Double.valueOf("0");}
        else{
            for(ShopingCartItem item : shopingCartItemList){
                cartBalance += item.getProposal().getPrice().doubleValue() * item.getQty();

            }
        }
        _log.info("Сумма по корзине: " + cartBalance);
    }

    public Double getCartBalance() {
        return cartBalance;
    }

    public List<ShopingCartItem> getShopingCartItemList() {
        return shopingCartItemList;
    }

    public void setShopingCartItemList(List<ShopingCartItem> shopingCartItemList) {
        this.shopingCartItemList = shopingCartItemList;
    }

    public Integer getShopingCartId() {
        return shopingCartId;
    }

    public void setShopingCartId(Integer shopingCartId) {
        this.shopingCartId = shopingCartId;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }


}
