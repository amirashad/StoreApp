package az.store.types;

import java.util.Objects;

/**
 *
 * @author Rashad Amirjanov
 */
public class CodeType {

    public static final CodeType POSTAVSIK = new CodeType(1, "Məhsul gətirənlər");
    public static final CodeType CLIENTS = new CodeType(2, "Satış məntəqələri");
    public static final CodeType WORKERS = new CodeType(3, "İşçilər");
    public static final CodeType EXPENDITURES = new CodeType(4, "Xərclər (Pul şəklində)");
    public static final CodeType PRODUCT_TYPE = new CodeType(5, "Məhsul tipləri");
    public static final CodeType PRODUCT_MEASURE_TYPE = new CodeType(6, "Ölçü vahidləri");
    public static final CodeType CURRENCY_TYPE = new CodeType(7, "Valyuta növləri");
    public static final CodeType PRODUCERS = new CodeType(8, "İstehsalçılar");
    public static final CodeType ESTIMATE = new CodeType(9, "Qalıq məhsul");
    public static final CodeType PRODUCT_SOURCE_TYPE = new CodeType(10, "Məhsulun mənbə tipləri (Hazır, İstehsal)");
    //
    private Integer id;
    private String name;
    private String description;
    private boolean valid;

    public CodeType() {
    }

    public CodeType(Integer id) {
        this.id = id;
    }

    public CodeType(Integer id, String typeName) {
        this.id = id;
        this.name = typeName;
        this.valid = true;
    }

    public CodeType(Integer id, String typeName, boolean valid) {
        this.id = id;
        this.name = typeName;
        this.valid = valid;
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
        if (obj != null && obj instanceof CodeType) {
            CodeType codeType = (CodeType) obj;
            if (Objects.equals(codeType.id, this.id)) {
                return true;
            }
        }

        return false;
    }
}
