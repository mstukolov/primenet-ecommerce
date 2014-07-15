package com.orders.controllers;

import com.orders.facade.AuthoritesFacade;
import com.orders.facade.OrdersFacade;
import mail.controllers.MailFacade;
import org.orders.entity.Authorites;
import org.orders.entity.Customer;
import org.orders.entity.Orders;
import org.orders.entity.UsersE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.jsf.FacesContextUtils;
import security.LogeventsFacade;
import security.LogeventsImpl;
import security.LoginBean;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

@ManagedBean(name="loginController")
@SessionScoped
public class LoginController {
    private static Logger _log = Logger.getLogger(LoginController.class.getName());
    //Поля для аутентификации
    private String username;
    private String password;
    //Поля для регистрации
    private String name;
    private String surname;
    private String birth;
    private String phone;
    private String email;
    private String pwd;
    //Сообщения на форме
    private String warning;

    private UsersE curUser;
    private Authorites authorites;

    private Boolean isAuth = false;
    private String msg;
    private String menuTxt = "Вход";
    private Boolean isAdmin = false;
    private Boolean isUser = false;
    private Customer customer;
    private List<Orders> activeCustOrders, histCustOrders;

    private List<User> activeUsers;
    @EJB
    private com.orders.facade.UserFacade userFacade;

    @EJB
    private com.orders.facade.CustomerFacade customerFacade;

    @EJB
    private OrdersFacade ordersFacade;

    @EJB
    private MailFacade mailFacade;

    @EJB
    private AuthoritesFacade authoritesFacade;

    @EJB(name="logeventsFacadeImpl")
    private LogeventsImpl logeventsFacade;

    @ManagedProperty("#{loginBean}")
    LoginBean loginBean;

    @PostConstruct
    public void init(){
        FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        //System.out.println(sessionRegistry);
    }
    /*==========SPRING SECURITY Authentication[Issue 24]=============*/

    public void saveLogevents(){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        logeventsFacade.logUserEvents((User)principal, "Logged IN", new Date(), "127.0.0.1");
        addMessage("Лог в базу данных записан!");
    }
    public List<User> getActiveUsers(){
        return activeUsers;
    }
    public void refreshActiveUsers(){
        List<String> usersNamesList = new ArrayList<String>();
        activeUsers = new ArrayList<User>();
        ApplicationContext ac = FacesContextUtils.getWebApplicationContext(FacesContext.getCurrentInstance());
        SessionRegistry sReg = (SessionRegistry) ac.getBean("sessionRegistry");

        for (Object principal: sReg.getAllPrincipals()) {
            if (principal instanceof User) {
                activeUsers.add(((User) principal));
                usersNamesList.add(((User) principal).getUsername());
            }
        }

        addMessage("ok!");
    }

    public Boolean isAuthenticated(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && !(authentication instanceof AnonymousAuthenticationToken) && authentication.isAuthenticated();
    }
    public Boolean hasRole(String role){
        // get security context from thread local
        SecurityContext context = SecurityContextHolder.getContext();
        if (context == null)
            return false;

        Authentication authentication = context.getAuthentication();
        if (authentication == null)
            return false;

        for (GrantedAuthority auth : authentication.getAuthorities()) {
            if (role.equals(auth.getAuthority()))
                return true;
        }
        return false;
    }

    public void setCurrentUser(){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String login = ((User)principal).getUsername();
        curUser = userFacade.findUserByLogin(login);
        customer = customerFacade.findCustomer(login);
    }
    public void logout(){
        username = "";
        password = "";
        setMenuTxt("Вход");
        setMsg("Выход выполнен");
        setAdmin(false);
        setAuth(false);
        curUser = new UsersE();
        customer = new Customer();
        addMessage(msg + ": " + isAuth);
    }
    /*========================================================================*/
    public void saveUser(){
        _log.info("Сохранение пользователя");
        userFacade.edit(curUser);
        curUser = userFacade.find(curUser.getRecid());
        this.addMessage("Пользователь сохранен!");
    }
    public void createNewCustomer() throws IOException, ServletException {
        customer = new Customer();
        customer.setSurname(this.surname);
        customer.setName(this.name);
        customer.setBirth(this.birth);
        customer.setPhone(this.phone);
        customer.setUser(email);
        customer.setCreatedBy(email);
        customer.setUpdatedBy(email);

        curUser = new UsersE();
        curUser.setLogin(customer.getUser());
        curUser.setPassword(pwd);
        curUser.setRole("client");
        curUser.setCreatedBy(customer.getUser());
        curUser.setUpdatedBy(customer.getUser());
        curUser.setEnabled(true);

        authorites = new Authorites();
        authorites.setLogin(customer.getUser());
        authorites.setAuthority("ROLE_USER");


        try {
            customerFacade.create(customer);
            userFacade.create(curUser);
            authoritesFacade.create(authorites);

            warning = "Пользователь сохранен";
            addMessage("Регистрация прошла успешно!");
            mailFacade.sendRegistrationInfo(customer);
            //[Issue 31] Автоматический вход в систему после регистрации нового пользователя
                loginBean.doLogin(curUser.getLogin(), curUser.getPassword());
                loginBean.setUsername(curUser.getLogin());
            //=========================================
        }catch (Exception ex){
            warning = "Пользователь существует";
            addMessage("Пользователь существует");
        }

    }


    /*public void auth(String login){
        for(UsersE usr : userFacade.findAll()){
            _log.info("Сравниваем: " + usr.getLogin().trim() + "  ___c__   " + login.trim());

            if (usr.getLogin().trim().equals(login.trim())) {
                _log.info("Пользователь найден: " + login);

                if(usr.getPassword().trim().equals(password.trim())){
                    _log.info("Пароль верен: " + password);
                   setAuth(true);
                   setMenuTxt(login);
                   setMsg("Авторизация прошла успешно");
                   //Заглушка для ролей пользователя
                   setRole(usr.getRole());
                   curUser = usr;
                   setCustomer(customerFacade.findCustomer(login));
                   findCustOrders();
                   //-----------
                   addMessage(msg + ": " + isAuth);
                   return;

                   }else{
                       _log.info("Пароль ошибка: " + password);
                       setAuth(false);
                       setMsg("Пароль введен неверно");
                       addMessage(msg + ": " + isAuth);
                       return;
                   }

            } else {
                   setAuth(false);
                   setMsg("Пользователя не существует");
            }
        }
        addMessage(msg + ": " + isAuth);
    }*/

    /*public void logout(){
        username = "";
        password = "";
        setMenuTxt("Вход");
        setMsg("Выход выполнен");
        setAdmin(false);
        setAuth(false);
        curUser = new UsersE();
        customer = new Customer();
        addMessage(msg + ": " + isAuth);
    }*/
    /*public void setRole(String role){
       if(role.equals("Admin")){
           setAdmin(true);
       }
        if(role.equals("User")){
            setUser(true);
        }
    }*/

    public void findCustOrders(){
        _log.info("Идет поиск заказов клиента:" + customer.getName() + " " +customer.getSurname() + "--Юзер: " + customer.getUser());

        activeCustOrders = ordersFacade.findCustOrders(customer.getUser());
        _log.info("Найдено строк заказов:" + activeCustOrders.size());
    }

    public void saveCustomer(){
        customerFacade.edit(customer);
        userFacade.edit(curUser);

        customer = customerFacade.find(customer.getRecid());
        curUser = userFacade.find(curUser.getRecid());

        addMessage("Данные сохранены");
    }
    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Boolean getUser() {
        return isUser;
    }

    public void setUser(Boolean user) {
        isUser = user;
    }

    public String getMenuTxt() {
        return menuTxt;
    }

    public void setMenuTxt(String menuTxt) {
        this.menuTxt = menuTxt;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }


    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getAuth() {
        return isAuth;
    }

    public void setAuth(Boolean auth) {
        isAuth = auth;
    }

    public Boolean getAdmin() {
        return isAdmin;
    }

    public void setAdmin(Boolean admin) {
        isAdmin = admin;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public UsersE getCurUser() {
        return curUser;
    }

    public void setCurUser(UsersE curUser) {
        this.curUser = curUser;
    }

    public List<Orders> getActiveCustOrders() {
        return activeCustOrders;
    }

    public void setActiveCustOrders(List<Orders> activeCustOrders) {
        this.activeCustOrders = activeCustOrders;
    }

    public List<Orders> getHistCustOrders() {
        return histCustOrders;
    }

    public void setHistCustOrders(List<Orders> histCustOrders) {
        this.histCustOrders = histCustOrders;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getWarning() {
        return warning;
    }

    public void setWarning(String warning) {
        this.warning = warning;
    }

    public LoginBean getLoginBean() {
        return loginBean;
    }

    public void setLoginBean(LoginBean loginBean) {
        this.loginBean = loginBean;
    }

    public void addMessage(String summary) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary,  null);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
}
