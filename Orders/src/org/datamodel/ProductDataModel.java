package org.datamodel;

import org.orders.entity.Product;
import org.primefaces.model.SelectableDataModel;

import javax.faces.model.ListDataModel;
import java.util.List;


    public class ProductDataModel extends ListDataModel<Product> implements SelectableDataModel<Product> {
    public ProductDataModel() {
    }
    public ProductDataModel(List<Product> data) {
        super(data);
    }
    @Override
    public Product getRowData(String rowKey) {
        //In a real app, a more efficient way like a query by rowKey should be implemented to deal with huge data

        List<Product> Products = (List<Product>) getWrappedData();

        for(Product product : Products) {
            if(product.getRecid().equals(rowKey))
                return product;
        }

        return null;
    }

    @Override
    public Object getRowKey(Product product) {
        return product.getRecid();
    }
}
