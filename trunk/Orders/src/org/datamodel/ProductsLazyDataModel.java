package org.datamodel;

import com.orders.facade.ProductFacade;
import org.orders.entity.Product;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import javax.ejb.EJB;

import javax.persistence.criteria.*;
import java.util.*;


public class ProductsLazyDataModel extends LazyDataModel<Product> {
    private Map<String,String> filtersExt;
    @EJB
    private ProductFacade productFacade;

    public ProductsLazyDataModel() {
    }

    public ProductsLazyDataModel(Map<String, String> filtersExt) {
        this.filtersExt = filtersExt;
    }
    @Override
    public List<Product> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String,String> filters) {
        List<Product> data = new ArrayList<Product>();
        int rowCount = 0;

        // Criteria
        CriteriaBuilder criteriaBuilder = productFacade.getEntityManager().getCriteriaBuilder();
        CriteriaQuery query = criteriaBuilder.createQuery(Product.class);
        // From
        Root from = query.from(Product.class);
        //sort
        if(sortField != null) {
            if (sortOrder == SortOrder.ASCENDING) {
                query.orderBy(criteriaBuilder.asc(from.get(sortField)));
            }
            else {
                query.orderBy(criteriaBuilder.desc(from.get(sortField)));
            }
        }
        // filters
        Predicate external = null;
        List<Predicate> predicates = new ArrayList();
        if(!filtersExt.isEmpty()){
            for(String field: filtersExt.keySet()){
                external = criteriaBuilder.like(from.get(field), filtersExt.get(field));
                predicates.add(external);
            }
        }

        if(!filters.isEmpty()){

            for(Iterator<String> it = filters.keySet().iterator(); it.hasNext();) {
                String filterProperty = it.next(); // table column name = field name
                String filterValue = filters.get(filterProperty);

                Expression<Collection<String>> expression = from.get(filterProperty);

                if(concatFilter(filterValue).length > 1){

                    List<Predicate> subpredicates = new ArrayList();
                    Path<Object> path = from.get(filterProperty);
                    Object[] values = new Object[concatFilter(filterValue).length];
                    int j = 0;
                    for(String str : concatFilter(filterValue)){
                        values[j] = str;
                        subpredicates.add(criteriaBuilder.like(from.get(filterProperty), "%"+str+"%"));
                        j++;
                    }
                    Subquery<Product> subquery = query.subquery(Product.class);
                    Root subroot = subquery.from(Product.class);

                    subquery.where(criteriaBuilder.or(subpredicates.toArray(new Predicate[subpredicates.size()])));
                    subquery.select(subroot.get(filterProperty));

                    predicates.add(criteriaBuilder.in(path).value(subquery));
                }else{
                    Expression literal = criteriaBuilder.literal("%"+((String)filterValue).trim()+"%");
                    predicates.add(criteriaBuilder.like(from.get(filterProperty), literal));
                }

            }


            query.where(predicates.toArray(new Predicate[predicates.size()]));
            data = productFacade.getEntityManager().createQuery(query).setFirstResult(first)
                    .setMaxResults(pageSize).getResultList();
            rowCount = productFacade.count();
        }else{
            query.where(predicates.toArray(new Predicate[predicates.size()]));
            data = productFacade.getEntityManager().createQuery(query).setFirstResult(first)
                    .setMaxResults(pageSize).getResultList();
            rowCount = productFacade.count();
        }

        // row count
        setRowCount(rowCount);
        return data;
    }
    public String[] concatFilter(String filter){
        return filter.replaceAll(" ", "").split(",");
    }
}
