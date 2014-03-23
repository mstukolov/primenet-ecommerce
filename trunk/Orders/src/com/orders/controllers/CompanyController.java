package com.orders.controllers;

import com.orders.facade.CompanyFacade;
import org.orders.entity.Company;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.transaction.UserTransaction;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@ManagedBean(name="companyController")
@SessionScoped
public class CompanyController {
    private List<Company> companyList;
    private Company selected;
    private StreamedContent content;
    @Resource
    UserTransaction utx;
    @EJB
    private CompanyFacade companyFacade;

    private static Logger log = Logger.getLogger(CompanyController.class.getName());

    @PostConstruct
    public void init(){
        companyList = new ArrayList<Company>();
        if(!companyFacade.findAll().isEmpty()){
            companyList = companyFacade.findAll();
            for(Company company: companyList){
                if(company.getActive().booleanValue() == true){ selected = company;}
            }
        }else{selected = new Company();}
    }
    public void refresh(){
        companyList = companyFacade.findAll();
        selected = companyFacade.find(selected.getRecid());
    }
    public void handleFileUpload(FileUploadEvent event) throws IOException {
        UploadedFile file = event.getFile();

        ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
        String newFileName = servletContext.getRealPath("") + File.separator + "resources\\content" + File.separator+ file.getFileName();
        String path = servletContext.getRealPath("") + File.separator + "resources\\content\\";

        String name = "logo"
                + event.getFile().getFileName().substring(
                event.getFile().getFileName().lastIndexOf('.'));
        InputStream is = file.getInputstream();
        File outfile = new File(path + name);
        OutputStream out = new FileOutputStream(outfile);
        byte buf[] = new byte[1024];
        int len;
        while ((len = is.read(buf)) > 0)
            out.write(buf, 0, len);
        is.close();
        out.close();
        selected.setLogo(file.getContents());
        this.save();
        addMessage("upload.successful" + file.getFileName() + " is uploaded.");
    }

    public StreamedContent getImage() throws IOException {
        if(selected.getLogo() != null){
            return new DefaultStreamedContent(new ByteArrayInputStream(selected.getLogo()));
        }
        return null;
    }
    public void create(){
        log.info("Начинаем создавать компанию vvvv");
        Company company = new Company();
        company.setName("New company");
        company.setVersion(Long.valueOf(1));
        selected = company;
        log.info("Объект инициализирован с индексом:" + company.getRecid());
        companyFacade.create(company);
        companyList.clear();
        companyList = companyFacade.findAll();
        addMessage("Компания создана!");
    }

    public void save(){
        try{
            companyFacade.edit(selected);
            selected = companyFacade.find(selected.getRecid());
            companyList.clear();
            companyList = companyFacade.findAll();
            addMessage("Компания сохранена!");
        }
        catch (Exception ex){addMessage(ex.getCause().getMessage());}
    }

    public void delete(Company company){
        log.info("Начинаем УДАЛАЯТЬ");
        try{
            log.info("Удаляем объект");
            companyFacade.remove(company);
            addMessage("Компания удалена!");
        }
        catch (Exception ex){addMessage(ex.getCause().getMessage());}
        companyList = companyFacade.findAll();
        selected = companyList.get(0);
    }
    public void search(){
        addMessage("Пользователь найден!");
    }

    public List<Company> getCompanyList() {
        return companyList;
    }

    public void setCompanyList(List<Company> companyList) {
        this.companyList = companyList;
    }

    public Company getSelected() {
        return selected;
    }

    public void setSelected(Company selected) {
        this.selected = selected;
    }

    public CompanyFacade getCompanyFacade() {
        return companyFacade;
    }

    public void setCompanyFacade(CompanyFacade companyFacade) {
        this.companyFacade = companyFacade;
    }

    public StreamedContent getContent() {
        return content;
    }

    public void setContent(StreamedContent content) {
        this.content = content;
    }
    public void addMessage(String summary) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary,  null);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
}
