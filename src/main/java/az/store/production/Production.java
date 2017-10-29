package az.store.production;

import az.store.product.Product;

import java.util.Date;

/**
 * @author Rashad Amirjanov
 */
public class Production {

    private int id;
    private int count;
    private Product productionProduct;
    private Date productionDate;
    private double productionPrice;

    public Production() {
    }

    public Production(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Product getProductionProduct() {
        return productionProduct;
    }

    public void setProductionProduct(Product productionProduct) {
        this.productionProduct = productionProduct;
    }

    public Date getProductionDate() {
        return productionDate;
    }

    public void setProductionDate(Date productionDate) {
        this.productionDate = productionDate;
    }

    public double getProductionPrice() {
        return productionPrice;
    }

    public void setProductionPrice(double productionPrice) {
        this.productionPrice = productionPrice;
    }

}
