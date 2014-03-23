package com.orders.facade;

import org.orders.entity.*;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Date;
import java.util.logging.Logger;

@Stateless
public class BonusFacade extends AbstractFacade<BonusTrans>{
    private static Logger _log = Logger.getLogger(BonusFacade.class.getName());

    @PersistenceContext(unitName = "OrdersPU")
    private EntityManager em;
    private BonusSetup setup;
    public String message;
    @EJB
    private BonussumFacade bonusSumFacade;
    @EJB
    private OrdersFacade ordersFacade;
    @EJB
    private BonusSetupFacade bonusSetupFacade;

    public EntityManager getEntityManager() {
        return em;
    }

    public BonusFacade() {
        super(BonusTrans.class);
    }
    public Integer accrualBonusTrans(String status){
        Integer i = 0;
        for(Orders order : ordersFacade.findAll()){

            if(order.getStatus().equals(status) && order.getBonus() == false){
                _log.info("Для клиента:" + order.getCustomer() + " Начислено бонусов: " + order.getAmount()
                                                                    / bonusSetupFacade.findAll().get(0).getParameter());
                BonusTrans trans = new BonusTrans();
                trans.setOrdernum(order.getRecid().toString());
                trans.setCustomer(order.getCustomer());
                trans.setAmount(order.getAmount());
                trans.setBalls(order.getAmount() / bonusSetupFacade.findAll().get(0).getParameter());

                trans.setCreatedBy(order.getCreatedBy());
                trans.setCreatedAt(new Date());
                trans.setUpdatedAt(new Date());
                trans.setUpdatedBy(order.getCreatedBy());

                this.create(trans);
                ordersFacade.find(order.getRecid()).setBonus(true);
                this.updateBonusSum(order.getCustomer(), trans.getBalls());
            i++;
            }
        }
        return i;
    }

    public String payOrderByBonus(Orders[] orders){

        for(Orders order : orders){
        _log.info("Текущий клиент: " + order.getCustomer());
        _log.info("Баланс:" + bonusSumFacade.findByCustomer(order.getCustomer()).getBalls());
        _log.info("Сумма заказа: " + order.getAmount());
        _log.info("Бонусы по заказу: " + order.getBonus());
        _log.info("Параметр для преобразования: " + bonusSetupFacade.findAll().get(0).getParameter());

        if((order.getAmount()
                    <= bonusSumFacade.findByCustomer(order.getCustomer()).getBalls()) && order.getBonus() == false){
            BonusTrans trans = new BonusTrans();
            trans.setCustomer(order.getCustomer());
            trans.setAmount(-1 * order.getAmount());
            trans.setBalls(-1 * order.getAmount());
            trans.setCreatedBy(order.getCreatedBy());
            trans.setOrdernum(order.getRecid().toString());
            trans.setUpdatedAt(new Date());
            trans.setUpdatedBy(order.getCreatedBy());
            this.create(trans);
            this.updateBonusSum(order.getCustomer(), trans.getBalls());
            //Обновление объекта в памяти необходимо до следующей перезагрузки AOS
            order.setStatus("Оплачен Бонусами");
            ordersFacade.find(order.getRecid()).setStatus("Оплачен Бонусами");
            //Обновление объекта в памяти необходимо до следующей перезагрузки AOS
            order.setBonus(true);
            //Формально установка информации об обновлении(заглушка), можно ставить пользователя из системы аутентификации
            ordersFacade.find(order.getRecid()).setUpdatedBy("Admin");
            ordersFacade.find(order.getRecid()).setBonus(true);
            message = "Бонусы списаны";
          }else{
            message = "Недостачно баллов для оплаты";
          }
        }
        return message;
    }

    public void updateBonusSum(String customer, Double balls){
        Bonussum bsum;
        bsum = bonusSumFacade.findByCustomer(customer);
        bsum.setCreatedBy("Admin");
        bsum.setUpdatedBy("Admin");
        bsum.setBalls(bonusSumFacade.findByCustomer(customer).getBalls() + balls);
        bonusSumFacade.edit(bsum);
    }


}
