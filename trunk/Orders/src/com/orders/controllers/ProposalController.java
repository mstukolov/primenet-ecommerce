package com.orders.controllers;

import com.orders.facade.ProductFacade;
import com.orders.facade.ProposalFacade;
import org.orders.entity.Product;
import org.orders.entity.Proposal;
import org.orders.entity.ShopingCart;
import org.orders.entity.ShopingCartItem;
import org.primefaces.event.DragDropEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

@ManagedBean(name="proposalController")
@SessionScoped
public class ProposalController implements Serializable {
    private static Logger _log = Logger.getLogger(ProposalController.class.getName());

    private List<Proposal> proposalList, filteredProposals;
    private List<Proposal> availableProposals;
    private List<Proposal> droppedProposal;
    private List<Product> products;

    private Product selectedProduct, error;
    private ShopingCart shopingCart;
    private Double shoppingCartBalance;
    private Proposal selectedProposal;
    private ShopingCartItem selectedShopingCartItem;
    @ManagedProperty("#{itemController}")
    ItemController itemController;

    private Proposal selected;
    @EJB
    private ProposalFacade proposalFacade;
    @EJB
    private ProductFacade productFacade;

    @PostConstruct
    public void init(){
        FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        proposalList = new ArrayList<Proposal>();
        availableProposals = new ArrayList<Proposal>();
        droppedProposal = new ArrayList<Proposal>();

        //Иницилизация корзины покупателя
        shopingCart = new ShopingCart();

        shoppingCartBalance = Double.valueOf("0");
        if(!proposalFacade.findAll().isEmpty()){
            proposalList = proposalFacade.findAll();
            selected = proposalList.get(0);
        }else{selected = new Proposal();}

        products = productFacade.findAll();
        error =  productFacade.find(Long.valueOf("2152"));
    }

    public void refresh(){
        proposalList = proposalFacade.findAll();

    }
    public void getProdDescription(String str){
        try {
            addMessage(new String(str.getBytes("UTF-8")));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
    public ShopingCartItem setSelectedCartItem(){
        FacesContext context = FacesContext.getCurrentInstance();
        String cartitem = context.getExternalContext().getRequestParameterMap().get("cartitem");

        for(ShopingCartItem item : shopingCart.getShopingCartItemList()){
            if(item.getCartItemId().toString().equals(cartitem)){
                selectedShopingCartItem = item;
            }
        }
        return selectedShopingCartItem;
    }

    public void qtyChange(String operation){
        FacesContext context = FacesContext.getCurrentInstance();
        String cartitem = context.getExternalContext().getRequestParameterMap().get("cartitem");

        for(ShopingCartItem item : shopingCart.getShopingCartItemList()){
           if(item.getCartItemId().toString().equals(cartitem)){
                selectedShopingCartItem = item;
            }
        }

        if(operation.equals("plus")){
            shopingCart.qtyPlus(selectedShopingCartItem);
        }
        else{
            shopingCart.qtyMinus(selectedShopingCartItem);
        }
    }
    public void qtyMinus(){
        Double qty = selectedShopingCartItem.getQty() - selectedShopingCartItem.getProposal().getMinQty().doubleValue();
        Double amount = qty* selectedShopingCartItem.getProposal().getPrice();
        selectedShopingCartItem.setAmount(amount);
        selectedShopingCartItem.setQty(qty);
        shopingCart.calcBalance();
    }

    public void CreateShoppingCart(){
        shopingCart = new ShopingCart();
    }
    public void searchProposals(List<Proposal> proposals){
        proposalList.clear();
        _log.info("Список предложений очищен");
        if(proposals.isEmpty()){
            _log.info("Фильтр пустой");
        } else {
            for(Proposal pr : proposals){
               _log.info("Фильтр содержит: " + pr.getRecid());
            }
        }
        proposalList.addAll(proposals);

    }
    public void setProductForProposal(Product product){
        selectedProposal.setProduct(product.getRecid());
        this.save();
    }

    public Product findProduct(Long product){
        return productFacade.find(product);
    }


    public StreamedContent getProductImage() throws IOException {
        FacesContext context = FacesContext.getCurrentInstance();
        if (context.getCurrentPhaseId() == PhaseId.RENDER_RESPONSE) {
            // So, we're rendering the view. Return a stub StreamedContent so that it will generate right URL.
            return new DefaultStreamedContent();
        }
        else {
            String productid = context.getExternalContext().getRequestParameterMap().get("imgprod");
            Product item =  productFacade.find(Long.valueOf(productid));

            if(item != null){
                if(item.getPhoto() != null){
                    //_log.info("Картинка существует для продукта:" + item.getRecid());
                    return new DefaultStreamedContent(new ByteArrayInputStream(item.getPhoto()));
                }
                else {
                    //_log.info("Картинки для продукта не существует:" + item.getRecid());
                    return new DefaultStreamedContent(new ByteArrayInputStream(error.getPhoto()));
                }
            }else{
                //_log.info("Продукт не найден:" + product);
                return new DefaultStreamedContent(new ByteArrayInputStream(error.getPhoto()));
            }
        }
    }
    public void create(){

        Proposal proposal = new Proposal();
        proposal.setMinQty(Long.valueOf("1"));
        proposal.setPrice(Long.valueOf("0"));
        proposal.setQty(Long.valueOf("1"));
        proposal.setStartQty(Long.valueOf("1"));
        proposal.setCreatedBy("Admin");
        proposal.setCreatedAt(new Date());
        proposal.setUpdatedBy("Admin");
        proposal.setUpdatedAt(new Date());
        proposal.setVersion(Long.valueOf(1));

        selected = proposal;
        proposalFacade.create(proposal);
        proposalList = proposalFacade.findAll();
        addMessage("Товар создан!");
    }
    public void save(){
        try{
            proposalFacade.edit(selectedProposal);
            selectedProposal = proposalFacade.find(selectedProposal.getRecid());
            proposalList.clear();
            proposalList = proposalFacade.findAll();
            addMessage("Предложение сохранено!" + selectedProposal.getRecid());
        }
        catch (Exception ex){addMessage(ex.getCause().getMessage());}

    }
    public List<Proposal> getAvailableProposals() {
        return availableProposals;
    }

    public void setAvailableProposals(List<Proposal> availableProposals) {
        this.availableProposals = availableProposals;
    }

    public void setBlocked(){
        selectedProposal.setBlocked(true);
    }
    public void setPromoAction(){
        selectedProposal.setPromo(true);
    }

    public void setShoppingCartBalance(Double shoppingCartBalance) {
        this.shoppingCartBalance = shoppingCartBalance;
    }

    public Proposal getSelectedProposal() {
        return selectedProposal;
    }

    public void setSelectedProposal(Proposal selectedProposal) {
        this.selectedProposal = selectedProposal;
    }

    public void addItemShoppingCart(Proposal proposal){

            if(proposal.getQty() >= proposal.getMinQty()){
                shopingCart.insertItem(proposal, proposal.getMinQty().doubleValue());
                selectedShopingCartItem = shopingCart.getLastItem();
                addMessage("Добавлено в корзину:" + proposal.getQty());
            }else{
                addMessage("Количесво меньше чем минимальное по предложению");
            }
    }


    public void removeItemShoppingCart(){
        shopingCart.removeItem(setSelectedCartItem());

        if(shopingCart.isEmpty()){
            //selectedShopingCartItem = shopingCart.getFirstItem();
            selectedShopingCartItem = new ShopingCartItem();
        }
        addMessage("Позиция удалена из корзины!");
    }
    public void blockProposal(){
        addMessage("Предложение заблокировано");
    }

    public void clearShoppingCart(){
        shopingCart.clear();
        addMessage("Корзина очищена!");
    }

    public List<Proposal> getProposalList() {
        return proposalList;
    }

    public void setProposalList(List<Proposal> proposalList) {
        this.proposalList = proposalList;
    }

    public List<Proposal> getDroppedProposal() {
        return droppedProposal;
    }

    public void setDroppedProposal(List<Proposal> droppedProposal) {
        this.droppedProposal = droppedProposal;
    }
    public void onProposalDrop(DragDropEvent ddEvent) {
        Proposal proposal = ((Proposal) ddEvent.getData());
        addItemShoppingCart(proposal);
    }
    public void addMessage(String summary) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary,  null);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    public ShopingCart getShopingCart() {
        return shopingCart;
    }

    public void setShopingCart(ShopingCart shopingCart) {
        this.shopingCart = shopingCart;
    }

    public ShopingCartItem getSelectedShopingCartItem() {
        return selectedShopingCartItem;
    }

    public void setSelectedShopingCartItem(ShopingCartItem selectedShopingCartItem) {
        this.selectedShopingCartItem = selectedShopingCartItem;
    }

    public ItemController getItemController() {
        return itemController;
    }

    public void setItemController(ItemController itemController) {
        this.itemController = itemController;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public Product getSelectedProduct() {
        return selectedProduct;
    }

    public void setSelectedProduct(Product selectedProduct) {
        this.selectedProduct = selectedProduct;
    }

    public List<Proposal> getFilteredProposals() {
        return filteredProposals;
    }

    public void setFilteredProposals(List<Proposal> filteredProposals) {
        this.filteredProposals = filteredProposals;
    }
}
