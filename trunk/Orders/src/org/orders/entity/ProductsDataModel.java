package org.orders.entity;


import org.primefaces.model.SelectableDataModel;

import javax.faces.model.ListDataModel;
import java.util.List;

public class ProductsDataModel extends ListDataModel<Ecoresproductcategory> implements SelectableDataModel<Ecoresproductcategory> {

    public ProductsDataModel() {
    }

    public ProductsDataModel(List<Ecoresproductcategory> data) {
        super(data);
    }

    @Override
    public Ecoresproductcategory getRowData(String rowKey) {
        //In a real app, a more efficient way like a query by rowKey should be implemented to deal with huge data

        List<Ecoresproductcategory> ecoresproductcategories = (List<Ecoresproductcategory>) getWrappedData();

        for(Ecoresproductcategory item : ecoresproductcategories) {
            if(item.getRecid().equals(rowKey))
                return item;
        }

        return null;
    }

    @Override
    public Object getRowKey(Ecoresproductcategory item) {
        return item.getRecid();
    }

}
