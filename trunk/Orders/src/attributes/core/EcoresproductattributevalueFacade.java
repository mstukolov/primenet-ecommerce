package attributes.core;

import attributes.model.Ecoresattribute;
import attributes.model.Ecoresproductattributevalue;
import attributes.model.Ecoresvalue;
import com.orders.facade.AbstractFacade;
import org.orders.entity.Product;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Stateless
public class EcoresproductattributevalueFacade extends AbstractFacade<Ecoresproductattributevalue> {
    private static Logger _log = Logger.getLogger(EcoresproductattributevalueFacade.class.getName());
    @PersistenceContext(unitName = "OrdersPU")
    private EntityManager em;

    @EJB
    private EcoresvalueFacade ecoresvalueFacade;
    @EJB
    private EcoresattributetypeFacade ecoresattributetypeFacade;

    public EntityManager getEntityManager() {
        return em;
    }

    public EcoresproductattributevalueFacade() {
        super(Ecoresproductattributevalue.class);
    }

    public void createAttributeValue(Product product, Ecoresattribute ecoresattribute){
        _log.info("Создание значения атрибута продукта: " + product.getRecid() + ", атрибут: " + ecoresattribute.getAttributeName());
        _log.info("Тип атрибута продукта: " + ecoresattributetypeFacade.getAttributeDataType(ecoresattribute));

        Ecoresproductattributevalue ecoresproductattributevalue = new Ecoresproductattributevalue();
        ecoresproductattributevalue.setProductRef(product.getRecid());
        ecoresproductattributevalue.setEcoResValueRef(
                ecoresvalueFacade.createEcoresValue(ecoresattribute,
                        ecoresattributetypeFacade.getAttributeDataType(ecoresattribute)));
        ecoresproductattributevalue.setRelationType(ecoresattributetypeFacade.getAttributeDataType(ecoresattribute));
        ecoresproductattributevalue.setAttributeRef(ecoresattribute.getRecid());
        ecoresproductattributevalue.setUpdatedBy("System");
        ecoresproductattributevalue.setCreatedBy("System");
        super.create(ecoresproductattributevalue);
    }

    public Boolean existProductattributevalue(Product product, Ecoresattribute ecoresattribute){
        try{
            javax.persistence.criteria.CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
            javax.persistence.criteria.CriteriaQuery cq = criteriaBuilder.createQuery(Ecoresproductattributevalue.class);
            Root<Ecoresproductattributevalue> root = cq.from(Ecoresproductattributevalue.class);
            Predicate predicate =
            criteriaBuilder.and(criteriaBuilder.equal(root.get("productRef"), product.getRecid()),
                    criteriaBuilder.equal(root.get("relationType"), ecoresattributetypeFacade.getAttributeDataType(ecoresattribute)));
            //Формирование списка параметров запроса
            List<Predicate> predicates = new ArrayList<Predicate>();
            predicates.add(criteriaBuilder.equal(root.get("productRef"), product.getRecid()));
            predicates.add(criteriaBuilder.equal(root.get("relationType"), ecoresattributetypeFacade.getAttributeDataType(ecoresattribute)));
            predicates.add(criteriaBuilder.equal(root.get("attributeRef"), ecoresattribute.getRecid()));

            //cq.select(root).where(predicate);
            cq.select(root).where(predicates.toArray(new Predicate[]{}));

            if(!getEntityManager().createQuery(cq).getResultList().isEmpty())
                                        {
                                            _log.info("Существует связка атрибута с продуктом: " + product.getRecid() + "::" + ecoresattribute.getAttributeTypeRef());
                                            _log.info("Код записи связки: " + ((Ecoresproductattributevalue)getEntityManager().createQuery(cq).getSingleResult()).getRecid());
                                        }
            return getEntityManager().createQuery(cq).getResultList().isEmpty();
        }catch(NoResultException e) {
            return false;
        }

    }

    public Ecoresvalue findEcoresValue(Product product, Ecoresattribute ecoresattribute){
        _log.info("Выбранный атрибут: " + ecoresattribute.getRecid() + ", ПРОДУКТ: " + product.getRecid());
        try{
            javax.persistence.criteria.CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
            javax.persistence.criteria.CriteriaQuery cq = criteriaBuilder.createQuery(Ecoresproductattributevalue.class);
            Root<Ecoresproductattributevalue> root = cq.from(Ecoresproductattributevalue.class);
            Predicate predicate =
                    criteriaBuilder.and(criteriaBuilder.equal(root.get("productRef"), product.getRecid()),
                            criteriaBuilder.equal(root.get("relationType"), ecoresattributetypeFacade.getAttributeDataType(ecoresattribute)));

            //Формирование списка параметров запроса
            List<Predicate> predicates = new ArrayList<Predicate>();
            predicates.add(criteriaBuilder.equal(root.get("productRef"), product.getRecid()));
            predicates.add(criteriaBuilder.equal(root.get("relationType"), ecoresattributetypeFacade.getAttributeDataType(ecoresattribute)));
            predicates.add(criteriaBuilder.equal(root.get("attributeRef"), ecoresattribute.getRecid()));

            cq.select(root).where(predicates.toArray(new Predicate[]{}));

            Ecoresproductattributevalue ecoresproductattributevalue = new Ecoresproductattributevalue();

            if(!getEntityManager().createQuery(cq).getResultList().isEmpty())
            {
                ecoresproductattributevalue = (Ecoresproductattributevalue)getEntityManager().createQuery(cq).getResultList().get(0);
                _log.info("Значение атрибута по relationType найдено: " + ecoresproductattributevalue.getEcoResValueRef());
                return ecoresvalueFacade.find(ecoresproductattributevalue.getEcoResValueRef());
            }else{_log.info("Значение атрибута по relationType не найдено"); return null;}

        }catch(NoResultException e) {
            return null;
        }

    }
}
