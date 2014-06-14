package com.orders.controllers;

import attributes.core.AttributeValueCount;
import attributes.core.EcoresattributetypeFacade;
import attributes.core.EcoresvalueenumerationFacade;
import attributes.core.SearchAttributeFacade;
import attributes.model.Ecoresvalueenumeration;
import attributes.model.ProductAttributesvaluesView;
import com.orders.facade.ProductFacade;
import com.orders.facade.ProposalFacade;
import com.orders.facade.RelevantprodFacade;
import org.orders.entity.Product;
import org.orders.entity.Proposal;
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
import javax.faces.event.ValueChangeEvent;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@ManagedBean(name="itemCardController")
@SessionScoped
public class ItemCardController {
    private static Logger _log = Logger.getLogger(ItemCardController.class.getName());
    private Proposal current;
    private Product error;
    private List<Proposal> relevantPropolsals;
    private List<Integer> imgPos;
    private  List<StreamedContent> productImages;
    private Integer rating;

    //[Issue 34]	Добавление возможности выбрать атрибут в карточке товара
    private List<ProductAttributesvaluesView> currentProductAttributesValues, currentProductAttributesValueFill;
    private String currentProductAttributesVariables;
    private Map<String, List<Ecoresvalueenumeration>> fillAttributeValues;
    private Map<String, String>  productConfiguration;
    //=====================================================================
    @EJB
    private ProductFacade productFacade;
    @EJB
    private ProposalFacade proposalFacade;
    @EJB
    private RelevantprodFacade relevantprodFacade;
    @EJB
    private SearchAttributeFacade searchAttributeFacade;
    @EJB
    private EcoresattributetypeFacade ecoresattributetypeFacade;
    @EJB
    private EcoresvalueenumerationFacade ecoresvalueenumerationFacade;

    @ManagedProperty("#{proposalController}")
    ProposalController proposalController;

    @PostConstruct
    public void init(){
        current = new Proposal();
        proposalController.CreateShoppingCart();
        error =  productFacade.find(Long.valueOf("2152"));

        relevantPropolsals = new ArrayList<Proposal>();
        buildRelevantProposals(current);

        imgPos = new ArrayList<Integer>();
  /*      imgPos.add(0);*/
        imgPos.add(1);
        imgPos.add(2);
        imgPos.add(3);
        imgPos.add(4);

        productImages = new ArrayList<StreamedContent>();
        currentProductAttributesValues = new ArrayList<ProductAttributesvaluesView>();
        currentProductAttributesValueFill = new ArrayList<ProductAttributesvaluesView>();
        fillAttributeValues = new HashMap<String, List<Ecoresvalueenumeration>>();
        productConfiguration = new HashMap<String, String>();

        rating = 0;
    }

    public void calcRating(){
        rating = Math.round((current.getQty() * 5)/current.getStartQty());
    }

    //[Issue 34]	Добавление возможности выбрать атрибут в карточке товара
    public void buildProductAttributesToShow(Proposal proposal){

        currentProductAttributesValues = searchAttributeFacade.findProductAttributeValues(proposal.getProduct(), true, false, false);
        currentProductAttributesValueFill = searchAttributeFacade.findProductAttributeValues(proposal.getProduct(), true, false, true);

        for(ProductAttributesvaluesView attributesvalue : currentProductAttributesValueFill){
            if(attributesvalue.isEnumeration() == true){
               List<Ecoresvalueenumeration> values = new ArrayList();
               values = ecoresvalueenumerationFacade.findTypeEnumerationList(ecoresattributetypeFacade.find(attributesvalue.getAttributeTypeRecid()));

                fillAttributeValues.put(attributesvalue.getAttributeName(), values);
            }
        }
    }
    public void setProductAttributeValue(String _key, String _value){
        productConfiguration.put(_key, _value);
        _log.info("Для продукта установлено: ");
    }

    public void buildProductAttributesToFill(Proposal proposal){
               // currentProductAttributesValueFill = searchAttributeFacade.findProductAttributeValues(proposal.getProduct(), true, false, true);
    }

    //==================================================================
    public void buildRelevantProposals(Proposal proposal){
        relevantPropolsals = relevantprodFacade.findRelevantProposals(proposal, relevantPropolsals);
    }
    public StreamedContent getProductImage() throws IOException {
        FacesContext context = FacesContext.getCurrentInstance();
        if (context.getCurrentPhaseId() == PhaseId.RENDER_RESPONSE) {
                return new DefaultStreamedContent();
        }
        else {
                Product product =  productFacade.find(current.getProduct());

                if(product != null){
                    if(product.getPhoto() != null){
                        return new DefaultStreamedContent(new ByteArrayInputStream(product.getPhoto()));
                    }
                    else {
                        return new DefaultStreamedContent(new ByteArrayInputStream(error.getPhoto()));
                    }
                }else{
                        return new DefaultStreamedContent(new ByteArrayInputStream(error.getPhoto()));
                }
        }
    }
    public StreamedContent getRelevantProductImage() throws IOException {
        FacesContext context = FacesContext.getCurrentInstance();
        if (context.getCurrentPhaseId() == PhaseId.RENDER_RESPONSE) {
            // So, we're rendering the view. Return a stub StreamedContent so that it will generate right URL.
            return new DefaultStreamedContent();
        }
        else {
            // So, browser is requesting the image. Get ID value from actual request param.
            String recid = context.getExternalContext().getRequestParameterMap().get("relevantProd");
            Product relevant =  productFacade.find(Long.valueOf(recid));
            return new DefaultStreamedContent(new ByteArrayInputStream(relevant.getPhoto()));
        }

    }

    public StreamedContent getBigProdImage(Integer flag) throws IOException {
        FacesContext context = FacesContext.getCurrentInstance();
        if (context.getCurrentPhaseId() == PhaseId.RENDER_RESPONSE) {
            return new DefaultStreamedContent();
        }
        else {
            String recid = context.getExternalContext().getRequestParameterMap().get("product");
            String number = context.getExternalContext().getRequestParameterMap().get("number");
            Integer num = Integer.valueOf(number);

            Product product =  productFacade.find(Long.valueOf(recid));
            if(num == 0) return new DefaultStreamedContent(new ByteArrayInputStream(product.getPhoto()));
            if(num == 1) return new DefaultStreamedContent(new ByteArrayInputStream(product.getImage1()));
            if(num == 2) return new DefaultStreamedContent(new ByteArrayInputStream(product.getImage2()));
            if(num == 3) return new DefaultStreamedContent(new ByteArrayInputStream(product.getImage3()));
            if(num == 4) return new DefaultStreamedContent(new ByteArrayInputStream(product.getImage4()));
        }
        return null;
    }

    public StreamedContent getProdImage(Integer flag) throws IOException {
        FacesContext context = FacesContext.getCurrentInstance();
        if (context.getCurrentPhaseId() == PhaseId.RENDER_RESPONSE) {
            return new DefaultStreamedContent();
        }
        else {
            String recid = context.getExternalContext().getRequestParameterMap().get("product");
            String number = context.getExternalContext().getRequestParameterMap().get("number");
            _log.info("Выбран продукт= " + recid + ", Позиция фото= " + number);
            Integer num = Integer.valueOf(number);

            Product product =  productFacade.find(Long.valueOf(recid));
            if(num == 0) return new DefaultStreamedContent(new ByteArrayInputStream(product.getPhoto()));
            if(num == 1) return new DefaultStreamedContent(new ByteArrayInputStream(product.getImage1()));
            if(num == 2) return new DefaultStreamedContent(new ByteArrayInputStream(product.getImage2()));
            if(num == 3) return new DefaultStreamedContent(new ByteArrayInputStream(product.getImage3()));
            if(num == 4) return new DefaultStreamedContent(new ByteArrayInputStream(product.getImage4()));
        }
        return null;
    }

    public List<Proposal> getRelevantPropolsals() {
        return relevantPropolsals;
    }

    public void setRelevantPropolsals(List<Proposal> relevantPropolsals) {
        this.relevantPropolsals = relevantPropolsals;
    }

    public Proposal getCurrent() {
        return current;
    }

    public void setCurrent(Proposal current) {
        this.current = current;
    }

    public ProposalController getProposalController() {
        return proposalController;
    }

    public void setProposalController(ProposalController proposalController) {
        this.proposalController = proposalController;
    }

    public List<Integer> getImgPos() {
        return imgPos;
    }

    public void setImgPos(List<Integer> imgPos) {
        this.imgPos = imgPos;
    }

    public List<StreamedContent> getProductImages() {
        return productImages;
    }

    public void setProductImages(List<StreamedContent> productImages) {
        this.productImages = productImages;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public List<ProductAttributesvaluesView> getCurrentProductAttributesValues() {
        return currentProductAttributesValues;
    }

    public void setCurrentProductAttributesValues(List<ProductAttributesvaluesView> currentProductAttributesValues) {
        this.currentProductAttributesValues = currentProductAttributesValues;
    }

    public String getCurrentProductAttributesVariables() {
        return currentProductAttributesVariables;
    }

    public void setCurrentProductAttributesVariables(String currentProductAttributesVariables) {
        this.currentProductAttributesVariables = currentProductAttributesVariables;
    }

    public List<ProductAttributesvaluesView> getCurrentProductAttributesValueFill() {
        return currentProductAttributesValueFill;
    }

    public void setCurrentProductAttributesValueFill(List<ProductAttributesvaluesView> currentProductAttributesValueFill) {
        this.currentProductAttributesValueFill = currentProductAttributesValueFill;
    }

    public Map<String, List<Ecoresvalueenumeration>> getFillAttributeValues() {
        return fillAttributeValues;
    }

    public void setFillAttributeValues(Map<String, List<Ecoresvalueenumeration>> fillAttributeValues) {
        this.fillAttributeValues = fillAttributeValues;
    }

    public Map<String, String> getProductConfiguration() {
        return productConfiguration;
    }

    public void setProductConfiguration(Map<String, String> productConfiguration) {
        this.productConfiguration = productConfiguration;
    }

    public void addMessage(String summary) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary,  null);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
}
