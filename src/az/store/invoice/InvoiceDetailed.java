package az.store.invoice;

import az.store.product.Product;
import java.io.Serializable;

/**
 *
 * @author Rashad Amirjanov
 */
public class InvoiceDetailed implements Serializable {

    private int id;
    private int count;
    private double priceBuy;
    private double priceSale;

    private Invoice invoice;
    private Product product;

    //only for reporting:
    private Double totalPriceBuy;
    private Double totalPriceSale;
    private String displayPriceBuy;
    private String displayPriceSale;

    public InvoiceDetailed() {
    }

    public InvoiceDetailed(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public double getPriceSale() {
        return priceSale;
    }

    public void setPriceSale(double priceSale) {
        this.priceSale = priceSale;
    }

    public Invoice getInvoice() {
        return invoice;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }

    public double getPriceBuy() {
        return priceBuy;
    }

    public void setPriceBuy(double priceBuy) {
        this.priceBuy = priceBuy;
    }

    public String getCountWithMeasure() {
        String measure = "";
        if (product != null && product.getMeasureName() != null) {
            measure = product.getMeasureName();
        }

        return count + " " + measure;
    }

    public void setTotalPriceBuy(Double totalPriceBuy) {
        this.totalPriceBuy = totalPriceBuy;
    }

    public void setTotalPriceSale(Double totalPriceSale) {
        this.totalPriceSale = totalPriceSale;
    }

    public double getTotalPriceBuy() {
        if (totalPriceBuy != null) {
            return totalPriceBuy;
        } else {
            return count * priceBuy;
        }
    }

    public double getTotalPriceSale() {
        if (totalPriceSale != null) {
            return totalPriceSale;
        } else {
            return count * priceSale;
        }
    }

    public double getTotalIncome() {
        return getTotalPriceSale() - getTotalPriceBuy();
    }

    public String getDisplayPriceBuy() {
        return displayPriceBuy;
    }

    public void setDisplayPriceBuy(String displayPriceBuy) {
        this.displayPriceBuy = displayPriceBuy;
    }

    public String getDisplayPriceSale() {
        return displayPriceSale;
    }

    public void setDisplayPriceSale(String displayPriceSale) {
        this.displayPriceSale = displayPriceSale;
    }

}
