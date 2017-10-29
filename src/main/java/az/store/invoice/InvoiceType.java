package az.store.invoice;

import az.store.types.CodeType;

/**
 * @author Rashad Amirjanov
 */
public class InvoiceType {

    private int id;
    private String name;
    private String description;
    private int sign;
    private String clientLabelName;
    private CodeType clientCodeType;
    private boolean production;

    public InvoiceType() {
    }

    public InvoiceType(int id) {
        this.id = id;
    }

    public InvoiceType(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public InvoiceType(int id, String name, String desciption) {
        this.id = id;
        this.name = name;
        this.description = desciption;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getSign() {
        return sign;
    }

    public void setSign(int sign) {
        this.sign = sign;
    }

    public String getClientLabelName() {
        return clientLabelName;
    }

    public void setClientLabelName(String clientLabelName) {
        this.clientLabelName = clientLabelName;
    }

    public CodeType getClientCodeType() {
        return clientCodeType;
    }

    public void setClientCodeType(CodeType clientCodeType) {
        this.clientCodeType = clientCodeType;
    }

    public boolean isProduction() {
        return production;
    }

    public void setProduction(boolean production) {
        this.production = production;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj instanceof InvoiceType) {
            InvoiceType invObj = (InvoiceType) obj;
            if (invObj.getId() == this.id) {
                return true;
            }
        } else if (obj != null && obj instanceof Integer) {
            int invObj = (int) obj;
            if (invObj == this.id) {
                return true;
            }
        }

        return false;
    }

    @Override
    public String toString() {
        return name == null ? "" : name;
    }

}
