package com.orders.controllers;

import attributes.core.*;
import attributes.model.Ecoresattribute;
import attributes.model.Ecoresvalue;
import attributes.model.Getattributeenumlist;
import com.orders.facade.ProductFacade;
import com.orders.facade.RelevantprodFacade;
import org.datamodel.ProductDataModel;
import org.orders.entity.Items;
import org.orders.entity.Product;
import org.orders.entity.Relevantproducts;
import org.orders.entity.SelectedProduct;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

@ManagedBean(name="itemController")
@SessionScoped
public class ItemController {
    private static Logger log = Logger.getLogger(ItemController.class.getName());
    private List<Items> itemList;
    private Product selected;
    private List<Product> products, filteredProducts;
    private List<Relevantproducts> relevantproductes;
    private Relevantproducts selectedRelevant;
    private List<SelectedProduct> selectedProducts;

    private ProductDataModel selectedProductsModel;
    private Product[] selectedProductsA;

    private List<StreamedContent> images;

    private List<Ecoresattribute> productAttributes;
    private Ecoresattribute selectedAttribute;
    private Ecoresvalue ecoresvalue;
    private Boolean isAttributeValueEnum;
    private SelectItem[] attributeEnumValues;

    @EJB
    private ProductFacade productFacade;
    @EJB
    private RelevantprodFacade relevantprodFacade;
    @EJB
    private EcoresproductattributevalueFacade ecoresproductattributevalueFacade;
    @EJB
    private EcoresvalueFacade ecoresvalueFacade;
    @EJB
    private EcoresattributeFacade ecoresattributeFacade;
    @EJB
    private GetattributeenumlistFacade getattributeenumlistFacade;
    @EJB
    private EcoresattributetypeFacade ecoresattributetypeFacade;

    public void refresh(){
        products = productFacade.findAll();
        selected = productFacade.find(selected.getRecid());
    }
    @PostConstruct
    public void init(){
        FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        products = productFacade.findAll();
        if(!productFacade.findAll().isEmpty()){
            products = productFacade.findAll();
            selected = products.get(0);
        }else{selected = new Product();}

        relevantproductes = new ArrayList<Relevantproducts>();
        selectedProducts = new ArrayList<SelectedProduct>();

        productAttributes = new ArrayList<Ecoresattribute>();
        selectedProductsModel = new ProductDataModel(products);

        images = new ArrayList<StreamedContent>();
    }
    //Создание значений атрибутов выбранного продукта
    public void selectProductAttributeValue(){
        createProductAttributeValue();
        log.info("Выбранный атрибут товара: " + selectedAttribute.getAttributeName());
        if(ecoresattributetypeFacade.isAttributeEnum(selectedAttribute))
        {
            isAttributeValueEnum = true;
            attributeEnumValues = new SelectItem[getattributeenumlistFacade.findEnumsByAttributeId(selectedAttribute).size()];
            int i = 0;
            for(Getattributeenumlist val : getattributeenumlistFacade.findEnumsByAttributeId(selectedAttribute)){
                log.info("Значение атрибута: " + val.getTextValue());
                attributeEnumValues[i++] = new SelectItem(val.getTextValue(), val.getTextValue());
            }
        } else {isAttributeValueEnum = false;log.info("Значение атрибута имеет не перечислимый тип данных");}
    }

    public Boolean getAttributeValueEnum() {
        return isAttributeValueEnum;
    }
    //Получение списка значений атрибута перечислимого типа(enumeration)

    public SelectItem[] getAttributeEnumValues() {
        return attributeEnumValues;
    }

    public void createProductAttributeValue(){
        log.info("Выбранный атрибут товара: " + selectedAttribute.getAttributeName());


        if(!ecoresproductattributevalueFacade.existProductattributevalue(selected, selectedAttribute)){
                addMessage("Существует связка атрибута с продуктом");
        }else{
                log.info("Отсутсвует cвязка атрибута с продуктом");
                ecoresproductattributevalueFacade.createAttributeValue(selected, selectedAttribute);
                log.info("Cвязка атрибута с продуктом создана");
                addMessage("Cвязка атрибута с продуктом создана");
             }

        ecoresvalue = ecoresproductattributevalueFacade.findEcoresValue(selected, selectedAttribute);
    }
    public void editAttributeValue(){
        if(ecoresvalue !=null){
            log.info("Фактическое значение атрибута продукта: " + ecoresvalue.getTextValue());
            ecoresvalueFacade.edit(ecoresvalue);
            ecoresvalue = ecoresvalueFacade.find(ecoresvalue.getRecid());
            addMessage("Значение изменено" + ecoresvalue.getTextValue());
        }
    }

    public void getProdAttributes(){
       productAttributes = productFacade.findProductsAttributes(selected);
       for(Ecoresattribute attribute : productAttributes){
           log.info("К продукту привязан атрибут: " + attribute.getAttributeName());
       }
       addMessage("Атрибуты обновлены");
    }
    public String getProductName(String recid){
            return  productFacade.find(Long.valueOf(recid)).getName();
    }
    public void addRelevantProducts(){
        relevantprodFacade.attachReleventProducts(selectedProductsA, selected);
        productSelection();
       addMessage("Релевантные продукты связаны!");
    }
    public void deleteRelevantProduct(){
        relevantprodFacade.remove(selectedRelevant);
        relevantproductes =  relevantprodFacade.findRelevantProducts(selected.getRecid());
        selectedRelevant = relevantproductes.get(0);
        addMessage("Релевантные продукты сброшен!");
    }
    public void productSelection(){
        /*selectedAttribute = ecoresattributeFacade.findAll().get(0);
        ecoresvalue = ecoresproductattributevalueFacade.findEcoresValue(selected, selectedAttribute);
        */
        if(ecoresvalue == null && ecoresvalueFacade.findAll().isEmpty() != true)
        {log.info("Атрибут в значении NULL");ecoresvalue = ecoresvalueFacade.findAll().get(0);ecoresvalue.setBooleanValue(false);}

        selectedProductsA = null;
        relevantproductes =  relevantprodFacade.findRelevantProducts(selected.getRecid());
        getProdAttributes();
    }
   /* public void handleFileUpload(FileUploadEvent event) throws IOException {
        UploadedFile file = event.getFile();

        ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
        String newFileName = servletContext.getRealPath("") + File.separator + "resources\\content" + File.separator+ file.getFileName();
        String path = servletContext.getRealPath("") + File.separator + "resources\\content\\";

        String name = *//*selected.getItemId() +*//*
                 event.getFile().getFileName().substring(
                event.getFile().getFileName().lastIndexOf('.'));
        InputStream is = file.getInputstream();
        File outfile = new File(path + name);
        OutputStream out = new FileOutputStream(outfile);
        byte buf[] = new byte[1024];
        int len;
        while ((len = is.read(buf)) > 0)
            //fos.write(buf, 0, len);
            out.write(buf, 0, len);
        is.close();
        out.close();
        //fos.close();
        addMessage("upload.successful" + file.getFileName() + " is uploaded.");
    }*/

    public void handleFileUploadDB(FileUploadEvent event) throws IOException {

        String imagenum = (String) event.getComponent().getAttributes().get("imagenum");
        log.info("Текущая картинка: " + imagenum);

        UploadedFile file = event.getFile();
        //log.info("Item file uploaded" + file.getSize());
        switch (Integer.valueOf(imagenum)){
            case 0: {selected.setPhoto(file.getContents());log.info("Загружена main");}
            case 1: {selected.setImage1(file.getContents());log.info("Загружена image1");}
            case 2: {selected.setImage2(file.getContents());log.info("Загружена image2");}
            case 3: {selected.setImage3(file.getContents());log.info("Загружена image3");}
            case 4: {selected.setImage4(file.getContents());log.info("Загружена image4");}
        }
        //selected.setPhoto(file.getContents());

        productFacade.edit(selected);
        selected = productFacade.find(selected.getRecid());

        addMessage("upload.successful" + file.getFileName() + " is uploaded.");
    }

    public StreamedContent getImage(Integer num) throws IOException {
        switch (num){
            case 0: if(selected.getPhoto()  != null)  return new DefaultStreamedContent(new ByteArrayInputStream(selected.getPhoto()));
            case 1: if(selected.getImage1() != null)  return new DefaultStreamedContent(new ByteArrayInputStream(selected.getImage1()));
            case 2: if(selected.getImage2() != null)  return new DefaultStreamedContent(new ByteArrayInputStream(selected.getImage2()));
            case 3: if(selected.getImage3() != null)  return new DefaultStreamedContent(new ByteArrayInputStream(selected.getImage3()));
            case 4: if(selected.getImage4() != null)  return new DefaultStreamedContent(new ByteArrayInputStream(selected.getImage4()));
        }

        return null;
    }

    public List<StreamedContent> getProductImages(Product product) throws IOException {
        images.add(getProductImages(product, 0));
        images.add(getProductImages(product, 1));
        images.add(getProductImages(product, 2));
        images.add(getProductImages(product, 3));
        images.add(getProductImages(product, 4));
        return images;
    }

    public StreamedContent getProductImages(Product product, Integer num) throws IOException {

        switch (num){
            case 0: if(product.getPhoto()  != null)  return new DefaultStreamedContent(new ByteArrayInputStream(selected.getPhoto()));
            case 1: if(product.getImage1() != null)  return new DefaultStreamedContent(new ByteArrayInputStream(selected.getImage1()));
            case 2: if(product.getImage2() != null)  return new DefaultStreamedContent(new ByteArrayInputStream(selected.getImage2()));
            case 3: if(product.getImage3() != null)  return new DefaultStreamedContent(new ByteArrayInputStream(selected.getImage3()));
            case 4: if(product.getImage4() != null)  return new DefaultStreamedContent(new ByteArrayInputStream(selected.getImage4()));
        }

        return null;
    }
    public void create(){

        Product product = new Product();
        product.setName("Good");
        product.setProduct("dfsgdfg");
        product.setCreatedBy("Admin");
        product.setCreatedAt(new Date());
        product.setUpdatedBy("Admin");
        product.setUpdatedAt(new Date());
        product.setVersion(Long.valueOf(1));

        selected = product;
        productFacade.create(product);
        products = productFacade.findAll();
        addMessage("Товар создан!");
    }
    public void edit(){
        productFacade.edit(selected);
        selected = productFacade.find(selected.getRecid());
        products.clear();
        products = productFacade.findAll();
        addMessage("Товар обновлен!");
    }
    public void delete(){
        try{
            productFacade.remove(selected);
            log.info("Удален объект");
        }
        catch (Exception ex){addMessage(ex.getCause().getMessage());}
        products = productFacade.findAll();
        selected = products.get(0);
        addMessage("Товар удален!");
    }
    public void search(){

        addMessage("Товар найден!");
    }

    public List<Items> getItemList() {
        return itemList;
    }

    public void setItemList(List<Items> itemList) {
        this.itemList = itemList;
    }

    public Product getSelected() {
        return selected;
    }

    public void setSelected(Product selected) {
        this.selected = selected;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public List<Relevantproducts> getRelevantproductes() {
        return relevantproductes;
    }

    public void setRelevantproductes(List<Relevantproducts> relevantproductes) {
        this.relevantproductes = relevantproductes;
    }

    public List<Product> getFilteredProducts() {
        return filteredProducts;
    }

    public void setFilteredProducts(List<Product> filteredProducts) {
        this.filteredProducts = filteredProducts;
    }

    public List<SelectedProduct> getSelectedProducts() {
        return selectedProducts;
    }

    public void setSelectedProducts(List<SelectedProduct> selectedProducts) {
        this.selectedProducts = selectedProducts;
    }

    public ProductDataModel getSelectedProductsModel() {
        return selectedProductsModel;
    }

    public void setSelectedProductsModel(ProductDataModel selectedProductsModel) {
        this.selectedProductsModel = selectedProductsModel;
    }

    public Product[] getSelectedProductsA() {
        return selectedProductsA;
    }

    public void setSelectedProductsA(Product[] selectedProductsA) {
        this.selectedProductsA = selectedProductsA;
    }

    public Relevantproducts getSelectedRelevant() {
        return selectedRelevant;
    }

    public void setSelectedRelevant(Relevantproducts selectedRelevant) {
        this.selectedRelevant = selectedRelevant;
    }

    public List<StreamedContent> getImages() {
        return images;
    }

    public void setImages(List<StreamedContent> images) {
        this.images = images;
    }

    public void setProductAttributes(List<Ecoresattribute> productAttributes) {
        this.productAttributes = productAttributes;
    }

    public List<Ecoresattribute> getProductAttributes() {
        return productAttributes;
    }

    public Ecoresattribute getSelectedAttribute() {
        return selectedAttribute;
    }

    public void setSelectedAttribute(Ecoresattribute selectedAttribute) {
        this.selectedAttribute = selectedAttribute;
    }

    public Ecoresvalue getEcoresvalue() {
        return ecoresvalue;
    }

    public void setEcoresvalue(Ecoresvalue ecoresvalue) {
        this.ecoresvalue = ecoresvalue;
    }

    public void addMessage(String summary) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary,  null);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
}
