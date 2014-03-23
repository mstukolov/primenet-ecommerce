package com.orders.controllers;

import com.orders.facade.BonusFacade;
import com.orders.facade.BonusSetupFacade;
import com.orders.facade.BonussumFacade;
import com.orders.facade.CustomerFacade;
import org.orders.entity.*;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@ManagedBean(name="bonusController")
@SessionScoped
public class BonusController {
    private static Logger _log = Logger.getLogger(BonusController.class.getName());

    private BonusSetup setup;
    private List<BonusTrans> transactions;
    private List<Bonussum> bsumList;
    private BonusTrans selected;



    @ManagedProperty("#{loginController}")
    LoginController loginController;

    @EJB
    private BonusFacade bonusFacade;
    @EJB
    private BonussumFacade bonusSumFacade;
    @EJB
    private CustomerFacade customerFacade;
    @EJB
    private BonusSetupFacade bonusSetupFacade;

    @PostConstruct
    public void init(){
        setup =  bonusSetupFacade.findAll().get(0);
        transactions = new ArrayList<BonusTrans>();
        bsumList = new ArrayList<Bonussum>();

        transactions = bonusFacade.findAll();
        bsumList = bonusSumFacade.findAll();
    }
    public Double getBallsByCustomer(String customer){
        return bonusSumFacade.findByCustomer(customer).getBalls();
    }
    public void saveSetup(){
        setup.setParameter(setup.getAmount() / setup.getBalls());
        bonusSetupFacade.edit(setup);
        setup = bonusSetupFacade.find(setup.getRecid());
        addMessage("Настройки сохранены");
    }

    public void payOrderByBonus(Orders[] orders){
        addMessage(bonusFacade.payOrderByBonus(orders));
        bsumList = bonusSumFacade.findAll();
    }

    public void accrualBonusTrans(String status){
        addMessage("Бонусные проводки обновлены:" + bonusFacade.accrualBonusTrans(status));
        transactions = bonusFacade.findAll();
        bsumList = bonusSumFacade.findAll();
    }
    public BonusSetup getSetup() {
        return setup;
    }

    public void setSetup(BonusSetup setup) {
        this.setup = setup;
    }

    public List<BonusTrans> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<BonusTrans> transactions) {
        this.transactions = transactions;
    }

    public BonusTrans getSelected() {
        return selected;
    }

    public void setSelected(BonusTrans selected) {
        this.selected = selected;
    }

    public LoginController getLoginController() {
        return loginController;
    }

    public void setLoginController(LoginController loginController) {
        this.loginController = loginController;
    }

    public List<Bonussum> getBsumList() {
        return bsumList;
    }

    public void setBsumList(List<Bonussum> bsumList) {
        this.bsumList = bsumList;
    }

    public void addMessage(String summary) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary,  null);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
}
