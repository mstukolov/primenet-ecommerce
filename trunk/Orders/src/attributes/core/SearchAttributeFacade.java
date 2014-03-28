package attributes.core;

import attributes.model.ProductAttributesvaluesView;
import com.orders.facade.AbstractFacade;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.*;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@Stateless
public class SearchAttributeFacade extends AbstractFacade<ProductAttributesvaluesView> {
    private static Logger _log = Logger.getLogger(SearchAttributeFacade.class.getName());
    private List<Long> products = new ArrayList<Long>();

    private Map<String, List<AttributeValueCount>> searchAttributeValues = new HashMap<String, List<AttributeValueCount>>();
    @EJB
    private EcoresvalueFacade ecoresvalueFacade;
    @PersistenceContext(unitName = "OrdersPU")
    private EntityManager em;

    public EntityManager getEntityManager() {
        return em;
    }

    public SearchAttributeFacade() {
        super(ProductAttributesvaluesView.class);
    }
    public void addProducts(Long product){
           this.products.add(product);
    }

    public List<Long> getProducts() {
        return products;
    }
    public void clearProducts(){
        this.products.clear();
    }
    public void findProductByAttributeSelection(String attribute){
        _log.info("Поиск продуктов по аттрибуту: " + attribute);
        javax.persistence.criteria.CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
        javax.persistence.criteria.CriteriaQuery<Tuple> cq = criteriaBuilder.createTupleQuery();
        Root<ProductAttributesvaluesView> root = cq.from(ProductAttributesvaluesView.class);
        List<Predicate> products = new ArrayList<Predicate>();
        for(Long product : this.products){
            products.add(criteriaBuilder.equal(root.get("productRef"), product));
            _log.info("Продукты для выборки по аттрибуту: " + product);
        }


    }
    public Map<String, List<AttributeValueCount>> buildSearchAttributes(){
        searchAttributeValues.clear();
        for(Tuple tuple : this.groupByProductsAttributeValues()){
            AttributeValueCount valueCount =
                        new AttributeValueCount(tuple.get("VALUE").toString(), Integer.valueOf(tuple.get("CNT").toString()));

            if(searchAttributeValues.containsKey((String) tuple.get("ATTRIBUTE")))
                {
                    _log.info("Атрибут в выборке присутствует: " + (String) tuple.get("ATTRIBUTE"));
                    searchAttributeValues.get((String) tuple.get("ATTRIBUTE")).add(valueCount);
                }
            else
                {
                    _log.info("Атрибут в выборке нет, создается новый список: " + (String) tuple.get("ATTRIBUTE"));
                    List<AttributeValueCount> list = new ArrayList<AttributeValueCount>();
                    list.add(valueCount);
                    searchAttributeValues.put((String) tuple.get("ATTRIBUTE"), list);
                }
        }
        return searchAttributeValues;
    }

    public List<Tuple> groupByProductsAttributeValues(){
        try{

            javax.persistence.criteria.CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
            javax.persistence.criteria.CriteriaQuery<Tuple> cq = criteriaBuilder.createTupleQuery();
            Root<ProductAttributesvaluesView> root = cq.from(ProductAttributesvaluesView.class);

            Expression<String> attributeName = root.get("attributeName");
            Expression<String> textValue = root.get("textValue");
            Expression<Long> productRef = root.get("productRef");
            Expression<Long> count = criteriaBuilder.count(productRef);

            //Формирование списка продуктов для передачи в запрос
            List<Predicate> predicates = new ArrayList<Predicate>();
            for(Long product : products){
                _log.info("В фильтр добавлен продукт: " + product);
                predicates.add(criteriaBuilder.equal(root.get("productRef"), product));
            }
            cq.multiselect(attributeName.alias("ATTRIBUTE"), textValue.alias("VALUE"), count.alias("CNT"));
            cq.where(criteriaBuilder.or(predicates.toArray(new Predicate[]{})));
            cq.groupBy(textValue);
            cq.orderBy(criteriaBuilder.desc(count));
            return getEntityManager().createQuery(cq).getResultList();
        }catch(NoResultException e) {
            return null;
        }
    }
    public List<ProductAttributesvaluesView> findProductsAttributeValues(){
        try{

            javax.persistence.criteria.CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
            javax.persistence.criteria.CriteriaQuery cq = criteriaBuilder.createQuery(ProductAttributesvaluesView.class);
            Root<ProductAttributesvaluesView> root = cq.from(ProductAttributesvaluesView.class);

            //Формирование списка продуктов для передачи в запрос
                List<Predicate> predicates = new ArrayList<Predicate>();
                for(Long product : products){
                    _log.info("В фильтр добавлен продукт: " + product);
                    predicates.add(criteriaBuilder.equal(root.get("productRef"), product));
                }
            cq.select(root).where(criteriaBuilder.or(predicates.toArray(new Predicate[]{})));
            return getEntityManager().createQuery(cq).getResultList();
        }catch(NoResultException e) {
            return null;
        }
    }
    public List<ProductAttributesvaluesView> findProductAttributeValues(Long product){
        try{
            _log.info("Поиск значений атрибутов продукта: " + product);
            javax.persistence.criteria.CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
            javax.persistence.criteria.CriteriaQuery cq = criteriaBuilder.createQuery(ProductAttributesvaluesView.class);
            Root<ProductAttributesvaluesView> root = cq.from(ProductAttributesvaluesView.class);
            Predicate predicate = criteriaBuilder.equal(root.get("productRef"), product);
            cq.select(root).where(predicate);
            return getEntityManager().createQuery(cq).getResultList();
        }catch(NoResultException e) {
            return null;
        }
    }
}

