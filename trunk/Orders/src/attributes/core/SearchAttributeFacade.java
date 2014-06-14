package attributes.core;

import attributes.model.ProductAttributesvaluesView;
import com.orders.facade.AbstractFacade;
import org.orders.entity.Product;

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
    public List<ProductAttributesvaluesView> findProductByAttributeSelection(Object... attribute){

        _log.info("Начался поиск продуктов по аттрибуту: " + attribute);
        javax.persistence.criteria.CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
        javax.persistence.criteria.CriteriaQuery<ProductAttributesvaluesView> cq = criteriaBuilder.createQuery(ProductAttributesvaluesView.class);
        Root<ProductAttributesvaluesView> root = cq.from(ProductAttributesvaluesView.class);
        List<Predicate> products = new ArrayList<Predicate>();
        //Формрование запроса по присутсвующим продуктам в текущей выборке
        for(Long product : this.products){
            products.add(criteriaBuilder.equal(root.get("productRef"), product));
            _log.info("Продукты для выборки по аттрибуту: " + product);
        }

        if(attribute[0] instanceof String){
            _log.info("Передан параметр типа String: " + attribute[0]);
        }
        //Параметр для выборки по значению атрибута
        Predicate attributeValue =  criteriaBuilder.equal(root.get("textValue"), attribute[0]);
        cq.select(root);
        cq.where(criteriaBuilder.or(products.toArray(new Predicate[]{})),
                                    criteriaBuilder.and(attributeValue));
        return getEntityManager().createQuery(cq).getResultList();
    }

    public Map<String, List<AttributeValueCount>> buildSearchAttributes(){
        searchAttributeValues.clear();
        for(Tuple tuple : this.groupByProductsAttributeValues()){
            AttributeValueCount valueCount =
                        new AttributeValueCount(tuple.get("VALUE").toString(), Integer.valueOf(tuple.get("CNT").toString()));

            if(searchAttributeValues.containsKey((String) tuple.get("ATTRIBUTE")))
                {
                    searchAttributeValues.get((String) tuple.get("ATTRIBUTE")).add(valueCount);
                }
            else
                {
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
    //[Issue 34]	Добавление возможности выбрать атрибут в карточке товара
    public List<ProductAttributesvaluesView> findProductAttributeValues(Long _recId,
                                                                        Boolean _inItemCardShow,
                                                                        Boolean _isFilterBuild,
                                                                        Boolean _inItemCardFill
                                                                        ){
        try{
            javax.persistence.criteria.CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
            javax.persistence.criteria.CriteriaQuery cq = criteriaBuilder.createQuery(ProductAttributesvaluesView.class);
            Root<ProductAttributesvaluesView> root = cq.from(ProductAttributesvaluesView.class);
            Predicate predicate = criteriaBuilder.equal(root.get("productRef"), _recId);
            Predicate InItemCardShow =  criteriaBuilder.equal(root.get("inItemCardShow"), _inItemCardShow);
            Predicate IsFilterBuild =  criteriaBuilder.equal(root.get("isFilterBuild"), _isFilterBuild);
            Predicate InItemCardFill =  criteriaBuilder.equal(root.get("inItemCardFill"), _inItemCardFill);


            cq.select(root).where(predicate,
                                    criteriaBuilder.and(InItemCardShow),
                                    criteriaBuilder.and(IsFilterBuild),
                                    criteriaBuilder.and(InItemCardFill));

            return getEntityManager().createQuery(cq).getResultList();
        }catch(NoResultException e) {
            return null;
        }
    }
}

