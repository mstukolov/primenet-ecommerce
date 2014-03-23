package com.orders.controllers;

import com.orders.facade.CarouselFacade;
import org.orders.entity.Carousel;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

@ManagedBean(name="carouselController")
@SessionScoped
public class CarouselController extends JSFUtill{
    private List<Carousel> carouselList;
    private List<StreamedContent> images;
    private List<Long> idlist;

    private Carousel selected;
    @EJB
    private CarouselFacade carouselFacade;
    private static Logger log = Logger.getLogger(CarouselController.class.getName());

    @PostConstruct
    public void init(){
        carouselList = new ArrayList<Carousel>();
        if(!carouselFacade.findAll().isEmpty()){
            carouselList = carouselFacade.findAll();
            selected = carouselList.get(0);
        }else{selected = new Carousel();}

        idlist = new ArrayList<Long>();
        for(Carousel carousel : carouselList){
           idlist.add(carousel.getRecid());
           //log.info("Инициализирована карусель: " + carousel.getRecid().toString());
        }
    }


    public StreamedContent getImage() throws IOException {
        if(selected.getImage() != null){
            return new DefaultStreamedContent(new ByteArrayInputStream(selected.getImage()));
        }
        return null;
    }

    public StreamedContent getImageByNum() throws IOException {
        FacesContext context = FacesContext.getCurrentInstance();

        if (context.getCurrentPhaseId() == PhaseId.RENDER_RESPONSE) {
            // So, we're rendering the view. Return a stub StreamedContent so that it will generate right URL.
            return new DefaultStreamedContent();
        }
        else {
            // So, browser is requesting the image. Get ID value from actual request param.
            String id = context.getExternalContext().getRequestParameterMap().get("inv");
            Carousel image = carouselFacade.find(Long.valueOf(id));
            return new DefaultStreamedContent(new ByteArrayInputStream(image.getImage()));
        }
    }

    public List<StreamedContent> getImages() {
        return images;
    }

    public void handleFileUpload(FileUploadEvent event) throws IOException {
        UploadedFile file = event.getFile();
        selected.setImage(file.getContents());
        this.save();
        addMessage("upload.successful" + file.getFileName() + " is uploaded.");
    }

    public void create(){
        log.info("Начинаем создавать банер");
        Carousel carousel = new Carousel();
        carousel.setCreatedBy("Admin");
        carousel.setCreatedAt(new Date());
        carousel.setUpdatedBy("Admin");
        carousel.setUpdatedAt(new Date());
        carousel.setVersion(Long.valueOf(1));
        selected = carousel;
        carouselFacade.create(carousel);
        carouselList.clear();
        carouselList = carouselFacade.findAll();
        addMessage("Банер создан!");
    }

    public void save(){
        try{
            carouselFacade.edit(selected);
            selected = carouselFacade.find(selected.getRecid());
            carouselList.clear();
            carouselList = carouselFacade.findAll();
            addMessage("Банер сохранен!");
        }
        catch (Exception ex){addMessage(ex.getCause().getMessage());}
    }

    public void delete(Carousel сarousel){
        log.info("Начинаем УДАЛАЯТЬ");
        try{
            log.info("Удаляем объект");
            carouselFacade.remove(сarousel);
            addMessage("Компания удалена!");
        }
        catch (Exception ex){addMessage(ex.getCause().getMessage());}
        carouselList = carouselFacade.findAll();
        selected = carouselList.get(0);
    }
    public List<Carousel> getCarouselList() {
        return carouselList;
    }

    public void setCarouselList(List<Carousel> carouselList) {
        this.carouselList = carouselList;
    }

    public Carousel getSelected() {
        return selected;
    }

    public void setSelected(Carousel selected) {
        this.selected = selected;
    }

    public List<Long> getIdlist() {
        return idlist;
    }
}
