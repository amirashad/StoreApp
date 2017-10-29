package az.store.product;

import az.store.production.Expenditure;
import az.store.types.CodeValue;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Objects;

/**
 * @author Rashad Amirjanov
 */
public class Product {

    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#.###");
    private Integer id;
    private String name;
    private String code;
    private double priceBuy;
    private double priceSale;
    private boolean valid;
    private CodeValue type;
    private CodeValue measure;
    private CodeValue producer;
    private CodeValue sourceType;
    private int count;
    private Double totalPriceBuy;
    private Double totalPriceSale;
    private ArrayList<Expenditure> expenditures = new ArrayList<>();

    public Product() {
    }

    public Product(Integer id) {
        this.id = id;
    }

    public Product(Integer id, String name, boolean valid) {
        this.id = id;
        this.name = name;
        this.valid = valid;
    }

    public Product(Integer id, String name, String code) {
        this.id = id;
        this.name = name;
        this.code = code;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public double getPriceBuy() {
        return priceBuy;
    }

    public void setPriceBuy(double priceBuy) {
        this.priceBuy = priceBuy;
    }

    public double getPriceSale() {
        return priceSale;
    }

    public void setPriceSale(double priceSale) {
        this.priceSale = priceSale;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    //    public String getCodeAsString() {
//        return code == 0 ? "" : DECIMAL_FORMAT.format(code);
//    }
    public void setCode(String code) {
        this.code = code;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public CodeValue getProducer() {
        return producer;
    }

    public void setProducer(CodeValue producer) {
        this.producer = producer;
    }

    public CodeValue getMeasure() {
        return measure;
    }

    public void setMeasure(CodeValue measure) {
        this.measure = measure;
    }

    public int getMeasureId() {
        return measure != null ? measure.getId() : 0;
    }

    public String getMeasureName() {
        return measure != null ? measure.getName() : "";
    }

    public CodeValue getType() {
        return type;
    }

    public void setType(CodeValue type) {
        this.type = type;
    }

    public int getTypeId() {
        return type != null ? type.getId() : 0;
    }

    public String getTypeName() {
        return type != null ? type.getName() : "";
    }

    public CodeValue getSourceType() {
        return sourceType;
    }

    public void setSourceType(CodeValue sourceType) {
        this.sourceType = sourceType;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getCountWithMeasure() {
        return count + " " + getMeasureName();
    }

    public double getTotalPriceBuy() {
        if (totalPriceBuy != null) {
            return totalPriceBuy;
        } else {
            return count * priceBuy;
        }
    }

    public void setTotalPriceBuy(Double totalPriceBuy) {
        this.totalPriceBuy = totalPriceBuy;
    }

    public double getTotalPriceSale() {
        if (totalPriceSale != null) {
            return totalPriceSale;
        } else {
            return count * priceSale;
        }
    }

    public void setTotalPriceSale(Double totalPriceSale) {
        this.totalPriceSale = totalPriceSale;
    }

    public double getTotalIncome() {
        return getTotalPriceSale() - getTotalPriceBuy();
    }

    public ArrayList<Expenditure> getExpenditures() {
        return expenditures;
    }

    public void setExpenditures(ArrayList<Expenditure> expenditures) {
        this.expenditures = expenditures;
    }

    public ArrayList<Expenditure> getProductExpenditures() {
        ArrayList<Expenditure> result = new ArrayList<>();
        for (Expenditure expenditure : expenditures) {
            if (expenditure.isProduct()) {
                result.add(expenditure);
            }
        }
        return result;
    }

    public String getProductExpendituresAsString() {
        ArrayList<Expenditure> pexpenditures = getProductExpenditures();
        String result = "";

        for (Expenditure exp : pexpenditures) {
            result += exp.getObject().toString() + ", ";
        }
        if (result.lastIndexOf(", ") != -1) {
            result = result.substring(0, result.lastIndexOf(", "));
        }

        return result;
    }

    public double getProductExpendituresPriceBuy() {
        double result = 0;

        ArrayList<Expenditure> pexpenditures = getProductExpenditures();
        for (Expenditure exp : pexpenditures) {
            result += exp.getPriceOfProduct();
        }

        return result;
    }

    public ArrayList<Expenditure> getCodeValueExpenditures() {
        ArrayList<Expenditure> result = new ArrayList<>();
        for (Expenditure expenditure : expenditures) {
            if (expenditure.isCodeValue()) {
                result.add(expenditure);
            }
        }
        return result;
    }

    public Expenditure getExpenditureByCodeValue(CodeValue codeValue) {
        Expenditure result = null;

        ArrayList<Expenditure> pexpenditures = getCodeValueExpenditures();
        for (Expenditure exp : pexpenditures) {
            if (exp.getObjectAsCodeValue().equals(codeValue)) {
                result = exp;
                break;
            }
        }

        return result;
    }

    @Override
    public String toString() {
        String result = "";

        if (code != null) {
            result += code + "  ";
        }
        if (name != null) {
            result += name;
        }

        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj instanceof Product) {
            Product product = (Product) obj;
            if (Objects.equals(product.id, this.id)) {
                return true;
            }
        }
        return false;
    }
}
