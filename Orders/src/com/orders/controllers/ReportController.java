package com.orders.controllers;

import com.orders.facade.CustomerFacade;
import com.orders.facade.OrdersFacade;
import com.orders.reportmodel.GetsaleslastmonthView;
import com.orders.reportmodel.GettopproductEntity;
import com.orders.reportmodel.SalesLastMonthViewFacade;
import com.orders.reportmodel.TopProductReportFacade;
import org.orders.entity.Orders;
import org.primefaces.model.chart.CartesianChartModel;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.LineChartSeries;
import org.primefaces.model.chart.PieChartModel;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.logging.Logger;

/*[Issue 30]	Форма DashBoard в админ-панели*/

@ManagedBean(name="reportController")
@SessionScoped
public class ReportController implements Serializable {
    private static Logger _log = Logger.getLogger(ReportController.class.getName());
    private Double totalSales;
    private Double totalSalesMonth;
    private Integer totalOrders;
    private Integer totalCustomersQty;

    private CartesianChartModel salesLineModel;
    private LineChartSeries sales;

    private PieChartModel model;

    @EJB
    private OrdersFacade ordersFacade;
    @EJB
    private CustomerFacade customerFacade;
    @EJB
    private TopProductReportFacade topProductReportFacade;
    @EJB
    private SalesLastMonthViewFacade salesLastMonthViewFacade;

    public ReportController() {
        model = new PieChartModel();
    }

    @PostConstruct
    public void init(){
        SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
        String lastYear = "01-01-2014 10:20:56";
        String lastMonth = "01-05-2014 10:20:56";

        try {
            totalSales = ordersFacade.calcTotalSalesPeriod(sdf.parse(lastYear));
            totalSalesMonth = ordersFacade.calcTotalSalesPeriod(sdf.parse(lastMonth));
            totalOrders = ordersFacade.findAll().size();
            totalCustomersQty = customerFacade.findAll().size();
            java.util.Date date = sdf.parse(lastYear);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        salesLineModel = new CartesianChartModel();
        sales = new LineChartSeries();

        buildGraphData();
        for(GettopproductEntity topProducts: topProductReportFacade.findAll()){

            model.set(topProducts.getProduct() + "," + topProducts.getName(), topProducts.getTotal());

        }
        //totalSalesMonth = ordersFacade.calcTotalSales();
    }
    public void refreshAllData(){
        model = new PieChartModel();
        for(GettopproductEntity topProducts: topProductReportFacade.findAll()){
            model.getData().put(topProducts.getProduct() + "," + topProducts.getName(), topProducts.getTotal());
        }
        addMessage("Refreshed");
    }
    public PieChartModel getModel() {
        return model;
    }

    public void buildGraphData(){

        sales.setLabel("Продажи");

         for(GetsaleslastmonthView salesView : salesLastMonthViewFacade.findAll()){
             _log.info(salesView.getSalesId() + ", " + salesView.getProcessing_At() +", " + salesView.getTotal());
             sales.set(salesView.getProcessing_At(), salesView.getTotal());
         }

        salesLineModel.addSeries(sales);
    }

    public CartesianChartModel getSalesLineModel() {
        return salesLineModel;
    }

    public Double getTotalSales() {
        return totalSales;
    }

    public void setTotalSales(Double totalSales) {
        this.totalSales = totalSales;
    }

    public Double getTotalSalesMonth() {
        return totalSalesMonth;
    }

    public void setTotalSalesMonth(Double totalSalesMonth) {
        this.totalSalesMonth = totalSalesMonth;
    }

    public Integer getTotalOrders() {
        return totalOrders;
    }

    public void setTotalOrders(Integer totalOrders) {
        this.totalOrders = totalOrders;
    }

    public Integer getTotalCustomersQty() {
        return totalCustomersQty;
    }

    public void setTotalCustomersQty(Integer totalCustomersQty) {
        this.totalCustomersQty = totalCustomersQty;
    }

    public void addMessage(String summary) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary,  null);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
    public void addMessageError(String summary) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, summary,  null);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
}
