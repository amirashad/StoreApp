package az.store.invoice;

import az.store.types.CodeValue;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author Rashad Amirjanov
 */
public class Invoice {

    private int id;
    private Date date;
    private CodeValue client;

    private double totalPriceBuy;//production price
    private double totalPriceSale;

    private boolean temp;

    private ArrayList<InvoiceDetailed> detaileds;

    private InvoiceType invoiceType;

    public Invoice() {
    }

    public Invoice(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public CodeValue getClient() {
        return client;
    }

    public void setClient(CodeValue client) {
        this.client = client;
    }

    public double getTotalPriceBuy() {//production price
        return totalPriceBuy;
    }

    public void setTotalPriceBuy(double totalPriceBuy) {
        this.totalPriceBuy = totalPriceBuy;
    }

    public double getTotalPriceSale() {
        return totalPriceSale;
    }

    public void setTotalPriceSale(double totalPriceSale) {
        this.totalPriceSale = totalPriceSale;
    }

    public ArrayList<InvoiceDetailed> getDetaileds() {
        return detaileds;
    }

    public void setDetaileds(ArrayList<InvoiceDetailed> detaileds) {
        this.detaileds = detaileds;
    }

    public boolean isTemp() {
        return temp;
    }

    public void setTemp(boolean temp) {
        this.temp = temp;
    }

    public InvoiceType getInvoiceType() {
        return invoiceType;
    }

    public void setInvoiceType(InvoiceType invoiceType) {
        this.invoiceType = invoiceType;
    }

}
