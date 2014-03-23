package org.datamodel;

import org.orders.entity.Relevantproducts;
import org.primefaces.model.SelectableDataModel;

import javax.faces.model.ListDataModel;
import java.util.List;


public class RelevantProductsModel extends ListDataModel<Relevantproducts> implements SelectableDataModel<Relevantproducts> {
    public RelevantProductsModel() {
    }
    public RelevantProductsModel(List<Relevantproducts> data) {
        super(data);
    }
    @Override
    public Relevantproducts getRowData(String rowKey) {
        //In a real app, a more efficient way like a query by rowKey should be implemented to deal with huge data

        List<Relevantproducts> Products = (List<Relevantproducts>) getWrappedData();

        for(Relevantproducts product : Products) {
            if(product.getRecid().equals(rowKey))
                return product;
        }

        return null;
    }

    @Override
    public Object getRowKey(Relevantproducts product) {
        return product.getRecid();
    }
}
