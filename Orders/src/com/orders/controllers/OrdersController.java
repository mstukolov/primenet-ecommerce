package com.orders.controllers;

import com.orders.facade.OrdersFacade;
import mail.controllers.MailFacade;
import org.datamodel.OrderDataModel;
import org.orders.entity.*;
import security.LoginBean;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

@ManagedBean(name="ordersController")
@SessionScoped
public class OrdersController {
    private static Logger _log = Logger.getLogger(OrdersController.class.getName());

    private List<Orders> orderList;
    //Добавление множественного выбора строк заказов
    private List<Orders> selectedOrderList, activeCustOrders, histCustOrders, filteredOrders;
    private Orders[] selectedOrders;
    private OrderDataModel ordersModel;
    private SelectItem[] statusOptions, deliveryOptions;
    private  String[] statusValues, deliveryValues;

    //[Issue 21]{Добавить общий группирующий признак строк заказов: Код заказа(SalesID)}
    private Integer salesId;

    private Order selectedOrder;
    @ManagedProperty("#{proposalController}")
    ProposalController proposalController;
    @ManagedProperty("#{userController}")
    UserController userController;
    @ManagedProperty("#{loginController}")
    LoginController loginController;
    @ManagedProperty("#{loginBean}")
    LoginBean loginBean;
    @ManagedProperty("#{uiController}")
    UIController uiController;

    @EJB
    private OrdersFacade ordersFacade;
    @EJB
    private MailFacade mailFacade;

    @PostConstruct
    public void init(){
        FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        orderList = new ArrayList<Orders>();
        orderList = ordersFacade.findAll();

        ordersModel = new OrderDataModel(orderList);

        statusValues = new String[6];
        statusValues[0] = "Оформлен";
        statusValues[1] = "Обработка";
        statusValues[2] = "Оплачен";
        statusValues[3] = "В пути";
        statusValues[4] = "Доставлен";
        statusValues[5] = "Отменено";
        statusOptions = createFilterOptions(statusValues);

        deliveryValues = new String[2];
        deliveryValues[0] = "1";
        deliveryValues[1] = "2";

        deliveryOptions = createFilterOptions(deliveryValues);
    }

    public void save(Integer value){
        if(value == 0){
            Double qty = selectedOrders[0].getQty();
            Long price = selectedOrders[0].getPrice();
            selectedOrders[0].setAmount(qty * price);
        } else if (value == 1){
            Double price = selectedOrders[0].getAmount()/ selectedOrders[0].getQty();
            selectedOrders[0].setPrice(price.longValue());
        }
        ordersFacade.edit(selectedOrders[0]);
        selectedOrders[0] = ordersFacade.find(selectedOrders[0].getRecid());

        //refreshOrders(0);
        addMessage("Заказ сохранен");
    }
    public void refreshCustomerOrders(String _login){
        activeCustOrders = ordersFacade.findCustOrders(_login);
        addMessage("Заказы обновлены");
    }
    public void refreshOrders(Integer mode){
        orderList.clear();
        orderList = ordersFacade.findAll();
        ordersModel = new OrderDataModel(orderList);
        if(mode == 1){
            if(filteredOrders != null){
                filteredOrders.clear();
                filteredOrders  = orderList;
            }
        }
    }

    public void setBonusTrue(Orders order){
        Orders order_u = ordersFacade.find(order.getRecid());
        order_u.setBonus(true);
        ordersFacade.edit(order_u);

        order.setBonus(true);
    }
    private SelectItem[] createFilterOptions(String[] data)  {
        SelectItem[] options = new SelectItem[data.length + 1];

        options[0] = new SelectItem("", "Select");
        for(int i = 0; i < data.length; i++) {
            options[i + 1] = new SelectItem(data[i], data[i]);
        }

        return options;
    }

    public SelectItem[] getStatusOptions() {
        return statusOptions;
    }
    //Функция оформить заказ из корзины покупателя
    public String Checkout(ShopingCart shopingCart){
        FacesContext context = FacesContext.getCurrentInstance();
        String auth = context.getExternalContext().getRequestParameterMap().get("isauth");
            _log.info("Статус регистрации пользователя: " + auth);
        if(true == Boolean.valueOf(auth)){
            addOrders(shopingCart);
            addMessage("Заказ отправлен в обработку!");
            return "true";
        }else{
            return uiController.goAuthPage();
        }
    }
    //Создание строк заказов из корзины покупателя на форме shop.xhtml
    public void addOrders(ShopingCart shopingCart){
        if(loginBean.isAuthenticated() == true){
            _log.info("Началось создание строк заказов.....");

            //[Issue 21]{Добавить общий группирующий признак строк заказов: Код заказа(SalesID)}
            salesId = (int) (Math.random()*1000000) + (int) (Math.random()*1000000);

            for(ShopingCartItem item: shopingCart.getShopingCartItemList()){

               Orders order = new Orders();
               order.setSalesId(salesId.longValue());
               order.setProduct(item.getProposal().getProduct());
               order.setProposal(item.getProposal().getRecid());

                /*[Issue 27]	Форма для личного кабинета + Профиль доставки*/
                order.setDeliveryTerms(shopingCart.getDeliveryOptions().toString());

                       //Запись данных о пользователе из контекста аутентификации Spring Security
                       order.setCreatedBy(loginBean.getUsername());
                       order.setCustomer(loginBean.getUsername());
                       //---------------

               order.setQty(item.getQty());
               order.setPrice(item.getProposal().getPrice());
               order.setAmount(item.getAmount());
               order.setStatus("Оформлен");
               order.setBonus(false);
               order.setBlocked(item.getProposal().getBlocked());
               order.setFullFilled(item.getProposal().getFullFilled());
               order.setPromo(item.getProposal().getPromo());
               order.setProcessingAt(new Date());
               //[Issue 34]	Указание конфигурация продукта(Определение размера, веса и т.д.)
               order.setConfiguration(item.getConfiguration());

               ordersFacade.create(order);

           }
        addMessage("Заказ отправлен на обработку!");
        proposalController.clearShoppingCart();

        }else{
            addMessage("Для создания заказа необходма авторизация!");
        }
    }
    public void viewSelected(){
        for(Orders order : selectedOrders){
            addMessage(order.getRecid().toString());
        }
    }

    public void setStatusSelectedOrders(String status){
        try{
            for(Orders order : selectedOrders){
                order.setProcessingAt(new Date());
                order.setStatus(status);
                ordersFacade.edit(order);
                /*[Issue 28]:	Автоматическая рассылка при изменении статуса заказа*/
                mailFacade.sendOrderStatusToCustomer(order);
            }
            selectedOrders = new Orders[1];

            //[Issue 21]{Добавить общий группирующий признак строк заказов: Код заказа(SalesID)}
            //Кэширование отфильтрованных строк заказов
            ArrayList<Orders> filter = new ArrayList<Orders>();
            Iterator iterator = filteredOrders.iterator();
                while(iterator.hasNext()){
                    filter.add(ordersFacade.find(((Orders)iterator.next()).getRecid()));
                }
                filteredOrders.clear();
                filteredOrders = filter;
            refreshOrders(0);
            addMessage("Статус установлен");
        }catch (NullPointerException ex){ addMessageError("Строки для смены статусов не выбраны");}
    }

    public Double getFillPercent(Proposal proposal){
        Double percent = 0.0;
        percent = getOrderedQtyOnProposal(proposal) / proposal.getStartQty();
        return percent;
    }

    public Double getOrderedQtyOnProposal(Proposal proposal){
            Double qty = Double.valueOf(0);
            for(Orders order: orderList){
               if(order.getProposal().equals(proposal.getRecid()) &&
                       (order.getStatus().equals("Оплачен") || order.getStatus().equals("Оплачен Бонусами"))
                       ){
                   qty+=order.getQty();
               }
           }
        return qty;
    }

    public  Double getRemainQty(Proposal proposal){
        Double qty = Double.valueOf(0);
        qty = proposal.getStartQty() - getOrderedQtyOnProposal(proposal);
        return qty;
    }
    public  Long getMinQty(Proposal proposal){
        return proposal.getMinQty();
    }
    public void setStatusOrder(String status){
        selectedOrder.setStatus(status);
    }

    public List<Orders> getOrderList() {
        return orderList;
    }
    public void filterOrderList(String value){
        for(Orders order : getOrderList()){

        }
    }
    public void setOrderList(List<Orders> orderList) {
        this.orderList = orderList;
    }

    public Order getSelectedOrder() {
        return selectedOrder;
    }

    public void setSelectedOrder(Order selectedOrder) {
        this.selectedOrder = selectedOrder;
    }

    public ProposalController getProposalController() {
        return proposalController;
    }

    public void setProposalController(ProposalController proposalController) {
        this.proposalController = proposalController;
    }

    public UserController getUserController() {
        return userController;
    }

    public void setUserController(UserController userController) {
        this.userController = userController;
    }

    public List<Orders> getSelectedOrderList() {
        return selectedOrderList;
    }

    public void setSelectedOrderList(List<Orders> selectedOrderList) {
        this.selectedOrderList = selectedOrderList;
    }

    public OrderDataModel getOrdersModel() {
        return ordersModel;
    }

    public void setOrdersModel(OrderDataModel ordersModel) {
        this.ordersModel = ordersModel;
    }

    public Orders[] getSelectedOrders() {
        return selectedOrders;
    }

    public void setSelectedOrders(Orders[] selectedOrders) {
        this.selectedOrders = selectedOrders;
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

    public List<Orders> getFilteredOrders() {
        return filteredOrders;
    }

    public void setFilteredOrders(List<Orders> filteredOrders) {
        this.filteredOrders = filteredOrders;
    }

    public LoginController getLoginController() {
        return loginController;
    }

    public void setLoginController(LoginController loginController) {
        this.loginController = loginController;
    }

    public UIController getUiController() {
        return uiController;
    }

    public void setUiController(UIController uiController) {
        this.uiController = uiController;
    }

    public SelectItem[] getDeliveryOptions() {
        return deliveryOptions;
    }

    public void setDeliveryOptions(SelectItem[] deliveryOptions) {
        this.deliveryOptions = deliveryOptions;
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
    public void addMessageError(String summary) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, summary,  null);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

}
