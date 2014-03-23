package com.orders.facade;

import org.orders.entity.Product;
import org.orders.entity.Proposal;
import org.orders.entity.Relevantproducts;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

@Stateless
public class RelevantprodFacade extends AbstractFacade<Relevantproducts>{
    private static Logger _log = Logger.getLogger(RelevantprodFacade.class.getName());

    @PersistenceContext(unitName = "OrdersPU")
    private EntityManager em;
    private  List<Proposal> relevantProposals;

    @EJB
    private ProposalFacade proposalFacade;

    public EntityManager getEntityManager() {
        return em;
    }

    public RelevantprodFacade() {
        super(Relevantproducts.class);
    }

    public void attachReleventProducts(Product[] products, Product parent){
        _log.info("Началась привязка релевантных продуктов");
        for(Product product : products){

            if(! isProductsRelated(parent, product)){
                _log.info("Продукты не связаны");
                Relevantproducts relevant = new Relevantproducts();
                relevant.setProduct(parent.getRecid());
                relevant.setRelevantprod(product.getRecid());
                relevant.setCreatedBy("Admin");
                relevant.setCreatedAt(new Date());
                relevant.setUpdatedBy("Admin");
                this.create(relevant);
            } else {
                _log.info("Продукты связаны: ");
            }
        }

    }
    public Boolean isProductsRelated(Product parent, Product relevant) {
        javax.persistence.criteria.CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
        javax.persistence.criteria.CriteriaQuery cq = criteriaBuilder.createQuery(Relevantproducts.class);
        Root<Relevantproducts> root = cq.from(Relevantproducts.class);
        Predicate product = criteriaBuilder.and(criteriaBuilder.equal(root.get("product"), parent.getRecid()),
                                                criteriaBuilder.equal(root.get("relevantprod"), relevant.getRecid()));
        cq.select(root).where(product);
        if(getEntityManager().createQuery(cq).getResultList().isEmpty()){
            return false;
        } else {return true;}
    }

    public List<Relevantproducts> findRelevantProducts(Long productID) {
        javax.persistence.criteria.CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
        javax.persistence.criteria.CriteriaQuery cq = criteriaBuilder.createQuery(Relevantproducts.class);
        Root<Relevantproducts> root = cq.from(Relevantproducts.class);
        Predicate product = criteriaBuilder.equal(root.get("product"), productID);
        cq.select(root).where(product);
        return getEntityManager().createQuery(cq).getResultList();
    }

    public List<Proposal> findProposals(Long productID){
        javax.persistence.criteria.CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
        javax.persistence.criteria.CriteriaQuery cq = criteriaBuilder.createQuery(Proposal.class);
        Root<Proposal> root = cq.from(Proposal.class);
        Predicate product = criteriaBuilder.equal(root.get("product"), productID);
        cq.select(root).where(product);
        return getEntityManager().createQuery(cq).getResultList();
    }
    public List<Proposal> findRelevantProposals(Proposal proposal, List<Proposal> relevantList){
        int[] myIntArray = {0,5};
        relevantList.clear();
        if(! this.findRelevantProducts(proposal.getProduct()).isEmpty()){
            _log.info("Найдены релевантные продукты: ");
            for(Relevantproducts relevant : this.findRelevantProducts(proposal.getProduct())){
                _log.info("Найдены релевантный продукт: " + relevant.getRelevantprod());
                if(! this.findProposals(relevant.getRelevantprod()).isEmpty()){
                    _log.info("Найдены релевантное ПРЕДЛОЖЕНИЕ: " + this.findProposals(relevant.getRelevantprod()).get(0).getRecid());
                    relevantList.add(this.findProposals(relevant.getRelevantprod()).get(0));
                }else{
                    _log.info("НЕ ОБНАРУЖЕНО релевантных ПРЕДЛОЖЕНИЙ: " + relevant.getRecid());
                    relevantList = proposalFacade.findRange(myIntArray);
                }
            }
        }else{
            _log.info("НЕ ОБНАРУЖЕНО релевантных ПРОДУКТОВ!");
            relevantList = proposalFacade.findRange(myIntArray);
        }
        return relevantList;
    }
}
