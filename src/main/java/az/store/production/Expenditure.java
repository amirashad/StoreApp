package az.store.production;

import az.store.product.Product;
import az.store.types.CodeValue;

import java.util.Objects;

/**
 * @author Rashad Amirjanov
 */
public class Expenditure {

    private Object object;
    private double price;
    private String description;

    public Expenditure() {
    }

    public Expenditure(CodeValue codeValue, double price, String description) {
        this.object = codeValue;
        this.price = price;
        this.description = description;
    }

    public Expenditure(Product product, double price, String description) {
        this.object = product;
        this.price = price;
        this.description = description;
    }

    public Product getObjectAsProduct() {
        if (object != null && object instanceof Product) {
            return (Product) object;
        } else {
            return null;
        }
    }

    public CodeValue getObjectAsCodeValue() {
        if (object != null && object instanceof CodeValue) {
            return (CodeValue) object;
        } else {
            return null;
        }
    }

    public boolean isProduct() {
        return (object != null && object instanceof Product);
    }

    public boolean isCodeValue() {
        return (object != null && object instanceof CodeValue);
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getPriceOfProduct() {
        Product product = getObjectAsProduct();

        return product != null ? product.getPriceBuy() * price : 0;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return object != null ? object.toString() : "";
    }

    @Override
    public boolean equals(Object other) {
        if (other != null && other instanceof Expenditure) {
            Expenditure e = (Expenditure) other;
            if (Objects.equals(e.getObject(), this.object)) {
                return true;
            }
        }

        return false;
    }
}
