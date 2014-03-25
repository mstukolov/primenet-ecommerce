package com.orders.controllers;

import attributes.core.AttributesController;
import attributes.core.SearchAttributeFacade;
import com.orders.facade.EcorescategoryFacade;
import com.orders.facade.EcoresproductcategoryFacade;
import com.orders.facade.ProductFacade;
import com.orders.facade.ProposalFacade;
import org.orders.entity.*;
import org.primefaces.event.NodeSelectEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@ManagedBean(name="ecorescategoryController")
@SessionScoped
public class EcorescategoryController {

    private static Logger log = Logger.getLogger(EcorescategoryController.class.getName());
    private List<Ecorescategory> ecorescategories, searchcategories;
    private List<Proposal> searchproposals;

    private List<Ecoresproductcategory> ecoresproductcategories;
    private Ecoresproductcategory[] selectedProductcategories;
    private Ecoresproductcategory ecoresproductcategory;

    private List<SelectedProduct> selectedProducts;
    private List<Product> products;
    private Product[] productsSelection;

    private ProductsDataModel productsDataModel;

    private Ecorescategory selected;
    private TreeNode root;
    private TreeNode selectedNode;
    @EJB
    private EcorescategoryFacade ecorescategoryFacade;
    @EJB
    private EcoresproductcategoryFacade ecoresproductcategoryFacade;
    @EJB
    private ProductFacade productFacade;
    @EJB
    private ProposalFacade proposalFacade;
    @EJB
    private SearchAttributeFacade searchAttributeFacade;

    @ManagedProperty("#{proposalController}")
    ProposalController proposalController;
    @ManagedProperty("#{uiController}")
    UIController uiController;
    @ManagedProperty("#{attributesController}")
    AttributesController attributesController;

    @PostConstruct
    public void init(){
        ecorescategories = new ArrayList<Ecorescategory>();
        searchcategories = new ArrayList<Ecorescategory>();
        searchproposals = new ArrayList<Proposal>();

        selectedProducts = new ArrayList<SelectedProduct>();
        ecoresproductcategories = new ArrayList<Ecoresproductcategory>();

        if(!productFacade.findAll().isEmpty()){
            for(Product product : productFacade.findAll()){
                SelectedProduct selectedProduct = new SelectedProduct();
                selectedProduct.setProduct(product);
                selectedProduct.setSelected(false);
                selectedProducts.add(selectedProduct);
            }

        }
        if(!ecorescategoryFacade.findAll().isEmpty()){
            ecorescategories = ecorescategoryFacade.findAll();
            selected = ecorescategories.get(0);
        }else{selected = new Ecorescategory();}

        if(!productFacade.findAll().isEmpty()){products = productFacade.findAll();}

        productsDataModel = new ProductsDataModel(ecoresproductcategories);

        root = new DefaultTreeNode("Root", null);
        selectedNode = root;
        buildtree();
        buildSearchButtons();
    }

    public void filterChildren(Long category){

          for(Ecorescategory ecorescategory: ecorescategoryFacade.findChildCategories(category)){

            for (Ecoresproductcategory ecoresproductcategory: ecoresproductcategoryFacade.findByCategory(ecorescategory.getRecid())){
                searchAttributeFacade.addProducts(ecoresproductcategory.getProduct());

                for(Proposal proposal: proposalFacade.findPropolsalsByProduct(Long.valueOf(ecoresproductcategory.getProduct()))){
                    searchproposals.add(proposal);
                    addMessage("Предложение:" + proposal.getRecid().toString() + ",Товар: " + ecoresproductcategory.getProduct());
                }
           }
            filterChildren(ecorescategory.getRecid());
        }


    }

    public void searchProposals(Long category){
        searchproposals.clear();
        searchAttributeFacade.clearProducts();
        filterChildren(category);
        proposalController.searchProposals(searchproposals);
        log.info("Фильтр содержит продукты в списоке поиска..................");
        for(Long product : searchAttributeFacade.getProducts()){
            log.info("Продукт: " + product);
        }

    }


    public void buildSearchButtons(){
        searchcategories.clear();
        for(Ecorescategory ecorescategory: ecorescategoryFacade.findChildCategories(findRoot().getRecid())){
            searchcategories.add(ecorescategory);
        }
    }
    public void refreshCategoriesAfterRestore(){
        if(!ecorescategoryFacade.findAll().isEmpty()){
            ecorescategories = ecorescategoryFacade.findAll();
            selected = ecorescategories.get(0);
        }else{selected = new Ecorescategory();}

        addMessage("Классификтор обновлен");
    }

    public void updateProducts(){
        selectedProducts.clear();
        if(!productFacade.findAll().isEmpty()){
            for(Product product : productFacade.findAll()){
                SelectedProduct selectedProduct = new SelectedProduct();
                selectedProduct.setProduct(product);
                selectedProduct.setSelected(false);
                selectedProducts.add(selectedProduct);
            }
        }
    }

    public void deleteSelectedProducts(Ecoresproductcategory ecoresproductcategory){
        //FacesContext context = FacesContext.getCurrentInstance();
        //String productcategory = context.getExternalContext().getRequestParameterMap().get("productcategory");
        log.info("Удалена связь по продукту: " + ecoresproductcategory.getProduct());
        ecoresproductcategoryFacade.remove(ecoresproductcategoryFacade.find(ecoresproductcategory.getRecid()));
        ecoresproductcategories =  ecoresproductcategoryFacade.findByCategory(selected.getRecid());
        addMessage("Записи удалены");
    }
    public void showSelectedProducts(){
        if(selectedProductcategories == null){
            addMessage("Массив ноль");
        }else if(selectedProductcategories.length > 0){
            addMessage("Массив содержит элементы");
            for(int i = 0; i<= selectedProductcategories.length-1;i++){
                addMessage("Элемент массива:" + selectedProductcategories[i].getProduct());
            }
        }
    }

    public void create(){
        log.info("Начинаем создавать объект");
        Ecorescategory category = new Ecorescategory();
        category.setName("New category");
        category.setParentrecid(selected.getRecid());
        ecorescategoryFacade.create(category);
        TreeNode treeNode = new DefaultTreeNode(category, selectedNode);
        addMessage("Объект создан!");
    }

    public void save(){
        try{
            ecorescategoryFacade.edit(selected);
            selected = ecorescategoryFacade.find(selected.getRecid());
            ecorescategories.clear();
            ecorescategories = ecorescategoryFacade.findAll();
            addMessage("Категория сохранена!");
        }
        catch (Exception ex){addMessage(ex.getCause().getMessage());}
    }

    public void delete(){
        try{
            log.info("Удаляем объект");
            ecorescategoryFacade.remove(selected);
            TreeNode parent = selectedNode.getParent();
            parent.getChildren().remove(selectedNode);
            //ecorescategoryFacade.remove(selected);
            addMessage("Категория удалена!");
        }
        catch (Exception ex){addMessage(ex.getCause().getMessage());}
        ecorescategories = ecorescategoryFacade.findAll();
        selected = ecorescategories.get(0);
    }

    public void addProductToCategory(){

        if(productsSelection == null){log.info("Список нулл");} else
        {
            for(Product product : productsSelection){
                log.info("Начинается привязка продукта: " + product.getRecid());
                  try {
                      ecoresproductcategoryFacade.create(selected, product);
                  } catch (Exception ex) {addMessage("Продукт уже привязан к категории" + product.getRecid());}

                log.info("Продукта привязан: " + product.getRecid());
            }
            log.info("Продукты связаны");
        }
        productsSelection = new Product[productFacade.findAll().size()];
        ecoresproductcategories = ecoresproductcategoryFacade.findByCategory(selected.getRecid());
        productsDataModel = new ProductsDataModel(ecoresproductcategories);
    }
    public void setSelectedProductsFalse(){
        for(SelectedProduct sProduct : selectedProducts){
            if(sProduct.getSelected().equals(true)){
                sProduct.setSelected(false);
            }
        }
    }
    public void addMessage(String summary) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary,  null);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    public List<Ecorescategory> getEcorescategories() {
        return ecorescategories;
    }

    public void setEcorescategories(List<Ecorescategory> ecorescategories) {
        this.ecorescategories = ecorescategories;
    }

    public Ecorescategory getSelected() {
        return selected;
    }

    public void setSelected(Ecorescategory selected) {
        this.selected = selected;
    }
    public void buildtree(){
        TreeNode main = new DefaultTreeNode(findRoot(), root);
        //log.info("Создана root-категория:" + findRoot().getName());
        for(Ecorescategory ecorescategory : ecorescategoryFacade.findAll()){
            if(ecorescategory.getParentrecid() == findRoot().getRecid()){
                TreeNode node = new DefaultTreeNode(ecorescategory, main);
                //log.info("Создана категория 1 уровня:" + ecorescategory.getName());
                findChildrenRows(ecorescategory.getRecid(), node);
            }
        }
    }
    public Ecorescategory findRoot(){
        for(Ecorescategory ecorescategory : ecorescategoryFacade.findAll()){
            if(ecorescategory.getParentrecid() == 0)
                {return ecorescategory;}
        }
        return null;
    }
    public void findChildrenRows(Long parent, TreeNode rootNode)
    {
        for(Ecorescategory category: ecorescategoryFacade.findAll()){
            if(category.getParentrecid().equals(parent)){
                TreeNode treeNode = new DefaultTreeNode(category, rootNode);
                findChildrenRows(category.getRecid(), treeNode);
            }
        }
    }
    public void onNodeSelect(NodeSelectEvent event) {
        selected = (Ecorescategory)event.getTreeNode().getData();
        ecoresproductcategories = ecoresproductcategoryFacade.findByCategory(selected.getRecid());

        //productsDataModel = new ProductsDataModel(ecoresproductcategories);

        attributesController.refreshAttributeGroupCategory(selected);
        addMessage(selected.getRecid() + ", " + selected.getName());
    }
    public TreeNode getRoot() {
        return root;
    }

    public TreeNode getSelectedNode() {
        return selectedNode;
    }

    public void setSelectedNode(TreeNode selectedNode) {
        this.selectedNode = selectedNode;
    }

    public List<Ecoresproductcategory> getEcoresproductcategories() {
        return ecoresproductcategories;
    }

    public void setEcoresproductcategories(List<Ecoresproductcategory> ecoresproductcategories) {
        this.ecoresproductcategories = ecoresproductcategories;
    }

    public List<SelectedProduct> getSelectedProducts() {
        return selectedProducts;
    }

    public void setSelectedProducts(List<SelectedProduct> selectedProducts) {
        this.selectedProducts = selectedProducts;
    }

    public ProductsDataModel getProductsDataModel() {
        return productsDataModel;
    }

    public void setProductsDataModel(ProductsDataModel productsDataModel) {
        this.productsDataModel = productsDataModel;
    }

    public Ecoresproductcategory[] getSelectedProductcategories() {
        return selectedProductcategories;
    }

    public void setSelectedProductcategories(Ecoresproductcategory[] selectedProductcategories) {
        this.selectedProductcategories = selectedProductcategories;
    }

    public String getProductName(String recid){
        log.info("Данные на вход" + recid);
        if(productFacade.find(Long.valueOf(recid)) != null){
            return  productFacade.find(Long.valueOf(recid)).getName();}
        else {return "no value";}
    }

    public List<Ecorescategory> getSearchcategories() {
        return searchcategories;
    }

    public void setSearchcategories(List<Ecorescategory> searchcategories) {
        this.searchcategories = searchcategories;
    }

    public ProposalController getProposalController() {
        return proposalController;
    }

    public void setProposalController(ProposalController proposalController) {
        this.proposalController = proposalController;
    }

    public UIController getUiController() {
        return uiController;
    }

    public void setUiController(UIController uiController) {
        this.uiController = uiController;
    }

    public AttributesController getAttributesController() {
        return attributesController;
    }

    public void setAttributesController(AttributesController attributesController) {
        this.attributesController = attributesController;
    }

    public Product[] getProductsSelection() {
        return productsSelection;
    }

    public void setProductsSelection(Product[] productsSelection) {
        this.productsSelection = productsSelection;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public Ecoresproductcategory getEcoresproductcategory() {
        return ecoresproductcategory;
    }

    public void setEcoresproductcategory(Ecoresproductcategory ecoresproductcategory) {
        this.ecoresproductcategory = ecoresproductcategory;
    }
}
