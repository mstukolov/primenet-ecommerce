package org.datamodel;

import org.orders.entity.Orders;
import org.primefaces.model.SelectableDataModel;

import javax.faces.model.ListDataModel;
import java.util.List;

public class OrderDataModel extends ListDataModel<Orders> implements SelectableDataModel<Orders> {

    public  OrderDataModel(){

    }
    public OrderDataModel(List<Orders> data) {
        super(data);
    }

    @Override
    public Orders getRowData(String rowKey) {
        //In a real app, a more efficient way like a query by rowKey should be implemented to deal with huge data

        List<Orders> Orders = (List<Orders>) getWrappedData();

        for(Orders order : Orders) {
            if(order.getRecid().equals(rowKey))
                return order;
        }

        return null;
    }

    @Override
    public Object getRowKey(Orders Orders) {
        return Orders.getRecid();
    }
}
