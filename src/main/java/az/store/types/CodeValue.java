package az.store.types;

import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Rashad Amirjanov
 */
public class CodeValue implements Cloneable {

    public static final CodeValue PST_SIMPLE_PRODUCT = new CodeValue(-1);
    public static final CodeValue PST_PRODUCTION_PRODUCT = new CodeValue(-2);
    private Integer id;
    private String name;
    private String description;
    private boolean valid;
    private CodeType codeType;

    public CodeValue() {
    }

    public CodeValue(Integer id) {
        this.id = id;
    }

    public CodeValue(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public CodeType getCodeType() {
        return codeType;
    }

    public void setCodeType(CodeType codeType) {
        this.codeType = codeType;
    }

    @Override
    public String toString() {
        if (name == null) {
            return "";
        } else {
            return name;
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj instanceof CodeValue) {
            CodeValue codeValue = (CodeValue) obj;
            if (Objects.equals(codeValue.id, this.id)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(CodeValue.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

}
