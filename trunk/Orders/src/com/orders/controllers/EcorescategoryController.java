package com.orders.controllers;

import attributes.core.AttributeValueCount;
import attributes.core.AttributesController;
import attributes.core.SearchAttributeFacade;
import attributes.model.ProductAttributesvaluesView;
import com.orders.facade.EcorescategoryFacade;
import com.orders.facade.EcoresproductcategoryFacade;
import com.orders.facade.ProductFacade;
import com.orders.facade.ProposalFacade;
import org.orders.entity.*;
import org.primefaces.event.MenuActionEvent;
import org.primefaces.event.NodeSelectEvent;
import org.primefaces.event.SlideEndEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import org.primefaces.model.menu.*;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.persistence.Tuple;
import java.util.*;
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

    private MenuModel menuModel, breadCrumbModel;
    private List<Ecorescategory> treeUp;

    private int minPrice = 0, maxPrice = 2000;

    private Map<String, List<AttributeValueCount>> searchAttributes;
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

        /*if(!productFacade.findAll().isEmpty()){
            for(Product product : productFacade.findAll()){
                SelectedProduct selectedProduct = new SelectedProduct();
                selectedProduct.setProduct(product);
                selectedProduct.setSelected(false);
                selectedProducts.add(selectedProduct);
            }

        }*/
        if(!ecorescategoryFacade.findAll().isEmpty()){
            ecorescategories = ecorescategoryFacade.findAll();
            selected = ecorescategories.get(0);
        }else{selected = new Ecorescategory();}

        /*if(!productFacade.findAll().isEmpty()){products = productFacade.findAll();}*/

        productsDataModel = new ProductsDataModel(ecoresproductcategories);

        root = new DefaultTreeNode("Root", null);
        selectedNode = root;
        buildtree();
        buildSearchButtons();

    }

    public void filterChildren(Long category){

          if(!ecorescategoryFacade.findChildCategories(category).isEmpty()){
          for(Ecorescategory ecorescategory: ecorescategoryFacade.findChildCategories(category)){

            /*for (Ecoresproductcategory ecoresproductcategory: ecoresproductcategoryFacade.findByCategory(ecorescategory.getRecid())){
                searchAttributeFacade.addProducts(ecoresproductcategory.getProduct());

                for(Proposal proposal: proposalFacade.findPropolsalsByProduct(Long.valueOf(ecoresproductcategory.getProduct()))){
                    //searchproposals.add(proposal);
                    //addMessage("Предложение:" + proposal.getRecid().toString() + ",Товар: " + ecoresproductcategory.getProduct());
                    }
                }*/
                filterChildren(ecorescategory.getRecid());
            }
          }else{
              log.info("Категория является самым нижним уровнем");
              for (Ecoresproductcategory ecoresproductcategory: ecoresproductcategoryFacade.findByCategory(category)){
                  searchAttributeFacade.addProducts(ecoresproductcategory.getProduct());
                  for(Proposal proposal: proposalFacade.findPropolsalsByProduct(Long.valueOf(ecoresproductcategory.getProduct()))){
                      //[STUM] Пересечение методов
                      searchproposals.add(proposal);
                  }
              }
          }

    }

   /* public void searchProposalsByAttribute(String attribute){
      searchproposals.clear();
      for(ProductAttributesvaluesView view : searchAttributeFacade.findProductByAttributeSelection(attribute)){
          for(Proposal proposal: proposalFacade.findPropolsalsByProduct(view.getProductRef())){
              searchproposals.add(proposal);
              log.info("Предложение из метода searchProposalsByAttribute:" + proposal.getRecid().toString());
          }
      }
      proposalController.searchProposals(searchproposals);
      addMessage("Поиск по атрибуту: " + attribute);
    }*/
    public void searchProposalsByPriceRange(SlideEndEvent event){

            addMessage("Цена выбрана от: " + minPrice + " до " + maxPrice);
    }
    public void searchProposals(Object _arg){
        searchproposals.clear();
        if(_arg instanceof Long){
            log.info("Поиск продуктов по КАТЕГОРИИ: " + _arg);
            searchAttributeFacade.clearProducts();
            filterChildren((Long) _arg);
            searchAttributes = searchAttributeFacade.buildSearchAttributes();
        } else if(_arg instanceof String){
            log.info("Поиск продуктов по АТРИБУТУ: " + _arg);
            for(ProductAttributesvaluesView view : searchAttributeFacade.findProductByAttributeSelection(_arg)){
                for(Proposal proposal: proposalFacade.findPropolsalsByProduct(view.getProductRef())){
                    searchproposals.add(proposal);
                    log.info("Предложение из метода searchProposals:" + proposal.getRecid().toString());
                }
            }
        }
        proposalController.searchProposals(searchproposals);
    }

    public void buildBreadCrumb(Long id){
        breadCrumbModel = new DefaultMenuModel();
        treeUp = new ArrayList<Ecorescategory>();
        treeUp.add(ecorescategoryFacade.find(id));
        parentCategoriesTreeUp(ecorescategoryFacade.find(id));

        DefaultMenuItem catalog = new DefaultMenuItem("Каталог");
        breadCrumbModel.addElement(catalog);

        ListIterator iterator= treeUp.listIterator(treeUp.size());
        while(iterator.hasPrevious()){
            Ecorescategory ecorescategory = (Ecorescategory)iterator.previous();
            DefaultMenuItem item = new DefaultMenuItem(ecorescategory.getName());
            item.setAjax(true);
            item.setUpdate("@form");
            item.setParam("menuId", (ecorescategory.getRecid()));
            item.setCommand("#{ecorescategoryController.displayList}");
            breadCrumbModel.addElement(item);
        }
    }

    public void parentCategoriesTreeUp(Ecorescategory ecorescategory){
        if(findParentCategory(ecorescategory).getParentrecid() != 0){
            treeUp.add(findParentCategory(ecorescategory));
            parentCategoriesTreeUp(findParentCategory(ecorescategory));
        }
    }

    public Ecorescategory findParentCategory(Ecorescategory ecorescategory){
        return ecorescategoryFacade.find(ecorescategoryFacade.find(ecorescategory.getRecid()).getParentrecid());
    }
    public void buildSearchButtons(){
        menuModel  = new DefaultMenuModel();
        searchcategories.clear();

        for(Ecorescategory ecorescategory: ecorescategoryFacade.findChildCategories(findRoot().getRecid())){
            searchcategories.add(ecorescategory);
            //[STUM][Issue 1] Добавить в разделе выбора категорий, отражение всех уровней классификатора
            DefaultSubMenu subMenu = new DefaultSubMenu(ecorescategory.getName());
            //subMenu.setIcon("ui-icon-check");

            DefaultMenuColumn column = new DefaultMenuColumn();
            if(!ecorescategoryFacade.findChildCategories(ecorescategory.getRecid()).isEmpty()){
                for(Ecorescategory ecorescategory1: ecorescategoryFacade.findChildCategories(ecorescategory.getRecid())){
                   if(!ecorescategoryFacade.findChildCategories(ecorescategory1.getRecid()).isEmpty()){
                            DefaultSubMenu subMenu2 = new DefaultSubMenu(ecorescategory1.getName());
                            for(Ecorescategory ecorescategory2: ecorescategoryFacade.findChildCategories(ecorescategory1.getRecid())){
                                DefaultMenuItem item = new DefaultMenuItem(ecorescategory2.getName());
                                item.setAjax(true);
                                item.setUpdate("@form");
                                item.setParam("menuId", ecorescategory2.getRecid());
                                item.setCommand("#{ecorescategoryController.displayList}");
                                item.setStyle("padding-right: 5%");
                                subMenu2.addElement(item);
                            }
                                column.addElement(subMenu2);

                        }else{
                                DefaultSubMenu subMenu2 = new DefaultSubMenu(ecorescategory1.getName());
                                DefaultMenuItem item = new DefaultMenuItem(ecorescategory1.getName());
                                item.setAjax(true);
                                item.setUpdate("@form");
                                item.setParam("menuId", ecorescategory1.getRecid());
                                item.setCommand("#{ecorescategoryController.displayList}");
                                subMenu2.addElement(item);
                                column.addElement(subMenu2);
                                log.info("Категория 2 уровня: " + ecorescategory1.getName() + " не имеет детей!");
                             }
                        }

            }else{
                DefaultSubMenu subMenu1 = new DefaultSubMenu(ecorescategory.getName());
                DefaultMenuItem item = new DefaultMenuItem(ecorescategory.getName());
                item.setAjax(true);
                item.setUpdate("@form");
                item.setParam("menuId", ecorescategory.getRecid());
                item.setCommand("#{ecorescategoryController.displayList}");
                subMenu1.addElement(item);
                column.addElement(subMenu1);
            }
            subMenu.addElement(column);
            menuModel.addElement(subMenu);
        }

    }
    public String displayList(ActionEvent event) {
        MenuItem menuItem = ((MenuActionEvent) event).getMenuItem();
        Long id = Long.parseLong(menuItem.getParams().get("menuId").get(0));
        buildBreadCrumb(id);
        log.info("Выбрана категория: " + id);
        this.searchProposals(id);
        return "shop_c";// + "?faces-redirect=true";
    }

    public void refreshCategoriesAfterRestore(){
        if(!ecorescategoryFacade.findAll().isEmpty()){
            ecorescategories = ecorescategoryFacade.findAll();
            selected = ecorescategories.get(0);
        }else{selected = new Ecorescategory();}
        buildSearchButtons();
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

    public Map<String, List<AttributeValueCount>> getSearchAttributes() {
        return searchAttributes;
    }

    public void setSearchAttributes(Map<String, List<AttributeValueCount>> searchAttributes) {
        this.searchAttributes = searchAttributes;
    }

    public int getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(int minPrice) {
        this.minPrice = minPrice;
    }

    public int getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(int maxPrice) {
        this.maxPrice = maxPrice;
    }

    public MenuModel getMenuModel() {
        return menuModel;
    }

    public void setMenuModel(MenuModel menuModel) {
        this.menuModel = menuModel;
    }

    public MenuModel getBreadCrumbModel() {
        return breadCrumbModel;
    }

    public void setBreadCrumbModel(MenuModel breadCrumbModel) {
        this.breadCrumbModel = breadCrumbModel;
    }
}
