package com.orders.controllers;


import com.orders.facade.CustomerFacade;
import com.orders.facade.UserFacade;
import org.orders.entity.Customer;
import org.orders.entity.UsersE;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.transaction.UserTransaction;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

@ManagedBean(name="userController")
@SessionScoped
public class UserController {

    //private List<UsersEntity> usersList;
    private List<UsersE> usersList;
    private UsersE selected;
    private List<Customer> customerList;
    private Customer selectedCustomer;
    private static Logger log = Logger.getLogger(UserController.class.getName());

    @EJB
    private com.orders.facade.UserFacade userFacade;

    @EJB
    private CustomerFacade customerFacade;

    @Resource
    UserTransaction utx;

    public UserController() {
    }
    @PostConstruct
    public void init(){
        //FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        usersList = new ArrayList<UsersE>();
        if(!userFacade.findAll().isEmpty()){
            usersList = userFacade.findAll();
            selected = usersList.get(0);
        }else{selected = new UsersE();}
        if(!customerFacade.findAll().isEmpty()){
            customerList = customerFacade.findAll();
            selectedCustomer = customerList.get(0);
        }else{selectedCustomer = new Customer();}
    }
    public void refresh(){
        customerList = customerFacade.findAll();
        usersList = userFacade.findAll();
        addMessage("Обновлен");
    }
    public void createCustomer(){
        Customer customer = new Customer();
        customer.setName("Name");
        customer.setSurname("SurName");
        customer.setCreatedBy("Admin");
        customer.setCreatedAt(new Date());
        customer.setUpdatedBy("Admin");
        customer.setUpdatedAt(new Date());
        customer.setUser("100");
        selectedCustomer = customer;
        customerFacade.create(customer);
        customerList = customerFacade.findAll();
        addMessage("Клиент создан!");
    }
    public void editCustomer(){
        customerFacade.edit(selectedCustomer);
        selectedCustomer = customerFacade.find(selectedCustomer.getRecid());

        customerList = customerFacade.findAll();
        addMessage("Клиент обновлен!");
    }
    public void deleteCustomer(){
        customerFacade.remove(selectedCustomer);
        customerList = customerFacade.findAll();
        selectedCustomer = customerList.get(0);
        addMessage("Клиент удален!");
    }

    public List<Customer> getCustomerList() {
        return customerList;
    }

    public void setCustomerList(List<Customer> customerList) {
        this.customerList = customerList;
    }

    public Customer getSelectedCustomer() {
        return selectedCustomer;
    }

    public void setSelectedCustomer(Customer selectedCustomer) {
        this.selectedCustomer = selectedCustomer;
    }

    public UsersE getSelected() {
        return selected;
    }

    public void setSelected(UsersE selected) {
        this.selected = selected;
    }

    public UserFacade getUserFacade() {
        return userFacade;
    }

    public void setUserFacade(UserFacade userFacade) {
        this.userFacade = userFacade;
    }

    public CustomerFacade getCustomerFacade() {
        return customerFacade;
    }

    public void setCustomerFacade(CustomerFacade customerFacade) {
        this.customerFacade = customerFacade;
    }

    public void buildUsersList(){
       /* CriteriaBuilder criteriaBuilder = userFacade.getEntityManager().getCriteriaBuilder();
        CriteriaQuery cq = criteriaBuilder.createQuery(UsersEntity.class);
        Root<UsersEntity> root = cq.from(UsersEntity.class);
        cq.select(root);
        setUsersList(userFacade.getEntityManager().createQuery(cq).getResultList());*/
    }
    public void create(){

        UsersE user = new UsersE();
        user.setLogin("New User");
        user.setPassword("password");
        user.setRole("Admin");
        //user.setDataareaid(Long.valueOf(851));
        userFacade.create(user);
        selected = user;
        usersList.clear();
        usersList = userFacade.findAll();
        addMessage("Пользователь создан ХАХА!");
    }
    public void save(){
        try{
            userFacade.edit(selected);
            addMessage("Пользователь сохранен!");
           }
        catch (Exception ex){addMessage(ex.getCause().getMessage());}
    }

    public void edit(){

        addMessage("Пользователь обновлен!");
    }

    public void delete(UsersE user){
        log.info("Начинаем УДАЛАЯТЬ");
        try{
            log.info("Удаляем объект:" + user.getRecid());
            userFacade.remove(user);
            addMessage("Пользователь удален!");
        }
        catch (Exception ex){addMessage(ex.getCause().getMessage());}
        usersList = userFacade.findAll();
        selected = usersList.get(0);
    }
    public void search(){
        addMessage("Пользователь найден!");
    }

    public List<UsersE> getUsersList() {
        return usersList;
    }

    public void setUsersList(List<UsersE> usersList) {
        this.usersList = usersList;
    }

    public void addMessage(String summary) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary,  null);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
}
