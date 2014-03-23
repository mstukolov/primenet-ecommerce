package com.orders.controllers;

import com.orders.facade.ProductFacade;
import com.orders.facade.ProposalFacade;
import com.orders.facade.RelevantprodFacade;
import org.orders.entity.Product;
import org.orders.entity.Proposal;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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

    @EJB
    private ProductFacade productFacade;
    @EJB
    private ProposalFacade proposalFacade;
    @EJB
    private RelevantprodFacade relevantprodFacade;

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

        rating = 0;
    }

    public void calcRating(){
        rating = Math.round((current.getQty() * 5)/current.getStartQty());
    }

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
}
