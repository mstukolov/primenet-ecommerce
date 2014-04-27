package com.orders.controllers;

import com.orders.facade.CarouselFacade;
import com.orders.facade.ProposalFacade;
import org.orders.entity.Carousel;
import org.orders.entity.Company;
import org.orders.entity.Proposal;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.*;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.servlet.ServletContext;
import java.awt.*;
import java.io.*;
import java.util.Random;
import java.util.logging.Logger;

@ManagedBean(name="uiController")
@SessionScoped
public class UIController implements Serializable {

    private static Logger _log = Logger.getLogger(UIController.class.getName());

    private String reference;
    private String previousPage, page;
    private String backurl;
    private String searchString = "";
    private Color randomColor;
    private String htmlRandomColor;
    private Boolean editForm = false;
    private int count, countShop;
    private Boolean admin = true;
    private Company company;
    private String proposalId;

    @ManagedProperty("#{itemCardController}")
    ItemCardController itemCardController;

    //[STUM][Issue 16] При создании новой записи открывать форму редактирования
    @ManagedProperty("#{itemController}")
    ItemController itemController;
    @ManagedProperty("#{proposalController}")
    ProposalController proposalController;



    @EJB
    private CarouselFacade carouselFacade;
    @EJB
    private ProposalFacade proposalFacade;

    @PostConstruct
    public void init(){
        FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        randomColor = Color.decode("#FF0096");
        editForm = false;
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
        addMessage("upload.successful" + file.getFileName() + " is uploaded.");
    }

    public String getProposalId() {
        return proposalId;
    }

    public void setProposalId(String proposalId) {
        this.proposalId = proposalId;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public void changeAdmin(){
        if(admin == true){admin = false;}else{admin = true;}
    }

    public Boolean getAdmin() {
        return admin;
    }

    public void setAdmin(Boolean admin) {
        this.admin = admin;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getCountShop() {
        return countShop;
    }

    public void setCountShop(int countShop) {
        this.countShop = countShop;
    }

    public void increment() {
           count++;
    }
    public void incrementShop() {
        countShop++;
    }
    //[STUM][Issue 16] При создании новой записи открывать форму редактирования
    public void create(Object _arg){
       if(_arg.equals("product")){
           itemController.create();
           this.setEditForm(true);
       }
        if(_arg.equals("propolsal")){
            proposalController.create();
            this.setEditForm(true);
        }
    }
    public void edit(Boolean edit){
        if(edit == false)
            {
                this.setEditForm(true);
            }
        if(edit == true )
            {
                this.setEditForm(false);
            }

    }
    public Boolean getEditForm() {
        return editForm;
    }

    public void setEditForm(Boolean editForm) {
        this.editForm = editForm;
    }

    public void buildRandomColor(){
        Random rand = new Random();
        float r = rand.nextFloat();
        float g = rand.nextFloat();
        float b = rand.nextFloat();
        randomColor = new Color(r, g, b);
        htmlRandomColor = toHexString(randomColor);
        addMessage("Color:" + toHexString(randomColor));
    }
    public static String toHexString(Color c) {
        StringBuilder sb = new StringBuilder("#");

        if (c.getRed() < 16) sb.append('0');
        sb.append(Integer.toHexString(c.getRed()));

        if (c.getGreen() < 16) sb.append('0');
        sb.append(Integer.toHexString(c.getGreen()));

        if (c.getBlue() < 16) sb.append('0');
        sb.append(Integer.toHexString(c.getBlue()));

        return sb.toString();
    }
    public Color getRandomColor() {
        return randomColor;
    }

    public String getHtmlRandomColor() {
        return htmlRandomColor;
    }

    public void runSearch(){
         searchString = "";
        addMessage("Поиск выполнен!");
    }
    public String getSearchString() {
        _log.info("Строка поиска" + searchString);
        return searchString;
    }

    public void setSearchString(String searchString) {
        this.searchString = searchString;
    }
    public String goAttributes(){
        doAction();
        return "attributes" + "?faces-redirect=true";
    }
    public String goAttributeGroups(){
        doAction();
        return "attributgroups" + "?faces-redirect=true";
    }
    public String goAttributTypes(){
        doAction();
        return "attributetypes" + "?faces-redirect=true";
    }
    public String goCarousel(){
        doAction();
        return "carousel" + "?faces-redirect=true";
    }
    public String goCategories(){
        doAction();
        return "ecorescategories" + "?faces-redirect=true";
    }

    public String goAdministration(){
        doAction();
        return "administration" + "?faces-redirect=true";
    }
    public String goAuthPage(){
        doAction();
        return "auth_c" + "?faces-redirect=true";
    }
    public String goUsers(){
        doAction();
        addMessage("Go to users");
        return "users" + "?faces-redirect=true";
    }
    public String goMail(){
        doAction();
        return "mail" + "?faces-redirect=true";
    }
    public String goCustomers(){
        doAction();
        return "customers" + "?faces-redirect=true";
    }
    public String goItemCard(Proposal proposal){
        doAction();
        itemCardController.setCurrent(proposal);
        itemCardController.calcRating();
        itemCardController.buildRelevantProposals(proposal);
        return "itemcard_c" + "?faces-redirect=true";
    }
    public String goBonus(){
        doAction();
        return "bonus" + "?faces-redirect=true";
    }
    public String goShopcart(){
        doAction();
        return "shopcart_c" + "?faces-redirect=true";
    }
    public String goCompany(){
        doAction();
        return "company" + "?faces-redirect=true";
    }
    public String goPersonalAccount(){
        doAction();
        return "personal" + "?faces-redirect=true";
    }

    public String goItems(){
        doAction();
        return "items" + "?faces-redirect=true";
    }
    public String goShop(){
        doAction();
        return "shop" + "?faces-redirect=true";
    }
    public String goIndex(){
        doAction();
        return "index" + "?faces-redirect=true";
    }
    public String goShop_C(){
        doAction();
        return "shop_c" + "?faces-redirect=true";
    }
    public String goProposal(){
        doAction();
        return "proposal" + "?faces-redirect=true";
    }
    public String goOrders(){
        doAction();
        return "orders" + "?faces-redirect=true";
    }
    //Переход на страницу из банера карусель
    public String goPage(Carousel carousel){
        doAction();
        _log.info("Переход на страницу карусели: " + carousel.getUrl());
        return carousel.getUrl() + "?faces-redirect=true";
    }
    public String back() {
        addMessage(backurl);
        return backurl + "?faces-redirect=true";
    }

    public String getBackurl() {
        return backurl;
    }
    public void getBackRef(){
        addMessage(backurl);
    }

    public void setBackurl(String backurl) {
        this.backurl = backurl;
    }

    public String doAction() {
        String url = FacesContext.getCurrentInstance().getViewRoot().getViewId();
        backurl = url;
        return "/target?faces-redirect=true&backurl=" + url;
    }
    //Переход на страницу из банера карусель
    public String goPromo(Long item){
       doAction();
       String url = carouselFacade.find(item).getUrl();
       String[] tokens = url.split("/");
        for (String t : tokens)
            addMessage(t);
        _log.info("Ссылка выбрана на предложение: " + tokens[1]);
       itemCardController.setCurrent(proposalFacade.find(Long.valueOf(tokens[1])));

       return tokens[0] + "?faces-redirect=true";
    }
    public String updateLastVisitedPage(String pageName)
    {
        previousPage = pageName;
        addMessage("Previous Page SETUP:" + previousPage);
        return "/thirdpage.xhtml";
    }

    public String getPreviousPage() {
        return previousPage;
    }

    public void setPreviousPage(String previousPage) {
        this.previousPage = previousPage;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
        addMessage("Previous Page was:" + previousPage);
    }

    public void addMessage(String summary) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary,  null);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    public void validate(AjaxBehaviorEvent event) {
        addMessage("Changed");
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public ItemCardController getItemCardController() {
        return itemCardController;
    }

    public void setItemCardController(ItemCardController itemCardController) {
        this.itemCardController = itemCardController;
    }

    public ItemController getItemController() {
        return itemController;
    }

    public void setItemController(ItemController itemController) {
        this.itemController = itemController;
    }

    public ProposalController getProposalController() {
        return proposalController;
    }

    public void setProposalController(ProposalController proposalController) {
        this.proposalController = proposalController;
    }
}
