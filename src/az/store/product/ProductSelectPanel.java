package az.store.product;

import az.store.Inits;
import az.store.types.CodeType;
import az.store.types.CodeValue;
import java.awt.Component;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import javax.swing.JComboBox;
import javax.swing.JLabel;

/**
 *
 * @author Rashad Amirjanov
 */
public class ProductSelectPanel extends javax.swing.JPanel {

    protected ArrayList<ProductSelectionListener> itemListenerList = new ArrayList<>();

    /**
     * Creates new form ProductSelectPanel
     */
    public ProductSelectPanel() {
        initComponents();

        jcbProductSourceType.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                refreshProducts();

                CodeValue sourceType = (CodeValue) jcbProductSourceType.getSelectedItem();
                fireProductSourceTypeChanged(sourceType);
            }
        });
        jcbProductType.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                refreshProducts();

                CodeValue productType = (CodeValue) jcbProductType.getSelectedItem();
                fireProductTypeChanged(productType);
            }
        });
        jcbProducts.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    Product product = (Product) jcbProducts.getSelectedItem();
                    fireProductSelected(product);
                } else {
                    if (jcbProducts.getSelectedItem() == null) {
                        fireProductSelected(null);
                    }
                }
            }
        });

    }

    public void init() {
        refreshSourceTypes();
        refreshProductTypes();
        refreshProducts();
    }

    private void refreshProducts() {
        jcbProducts.removeAllItems();
        jcbProducts.addItem(null);

        ArrayList<Product> products = Inits.getSelectQueries().getProducts(null, getSelectedProductType(), getSelectedSourceType());
        for (Product product : products) {
            jcbProducts.addItem(product);
        }
    }

    private void refreshSourceTypes() {
        jcbProductSourceType.removeAllItems();
//        jcbProductSourceType.addItem(null);

        ArrayList<CodeValue> codeValuesProducers = Inits.getSelectQueries().getCodeValues(CodeType.PRODUCT_SOURCE_TYPE);
        for (CodeValue codeValue : codeValuesProducers) {
            jcbProductSourceType.addItem(codeValue);
        }
    }

    private void refreshProductTypes() {
        jcbProductType.removeAllItems();
        jcbProductType.addItem(null);

        ArrayList<CodeValue> codeValuesProductType = Inits.getSelectQueries().getCodeValues(CodeType.PRODUCT_TYPE);
        for (CodeValue codeValue : codeValuesProductType) {
            jcbProductType.addItem(codeValue);
        }
    }

    public CodeValue getSelectedSourceType() {
        return (CodeValue) jcbProductSourceType.getSelectedItem();
    }

    public CodeValue getSelectedProductType() {
        return (CodeValue) jcbProductType.getSelectedItem();
    }

    public Product getSelectedProduct() {
        return (Product) jcbProducts.getSelectedItem();
    }

    public void setSelectedProductSourceType(CodeValue sourceType) {
        jcbProductSourceType.setSelectedItem(sourceType);
    }

    public void setSelectedProductType(CodeValue productType) {
        jcbProductType.setSelectedItem(productType);
    }

    public void setSelectedProduct(Product product) {
        jcbProductType.setSelectedIndex(0);
        jcbProducts.setSelectedItem(product);
    }

    private void fireProductSourceTypeChanged(CodeValue sourceType) {
        for (ProductSelectionListener productSelectionListener : itemListenerList) {
            productSelectionListener.productSourceTypeChanged();
        }
    }

    private void fireProductTypeChanged(CodeValue productType) {
        for (ProductSelectionListener productSelectionListener : itemListenerList) {
            productSelectionListener.productTypeChanged();
        }
    }

    protected void fireProductSelected(Product product) {
        for (ProductSelectionListener productSelectionListener : itemListenerList) {
            productSelectionListener.productSelected();
        }
    }

    public JComboBox getProductSourceTypesComboBox() {
        return jcbProductSourceType;
    }

    public JComboBox getProductTypesComboBox() {
        return jcbProductType;
    }

    public JLabel getProductTypesLabel() {
        return jlblProductType;
    }

    public JComboBox getProductsComboBox() {
        return jcbProducts;
    }

    public JLabel getProductsLabel() {
        return jlbProducts;
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        for (Component component : getComponents()) {
            component.setEnabled(enabled);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jcbProductSourceType = new javax.swing.JComboBox();
        jlblProductType = new javax.swing.JLabel();
        jcbProductType = new javax.swing.JComboBox();
        jlbProducts = new javax.swing.JLabel();
        jcbProducts = new javax.swing.JComboBox();
        jbtnProductSearch = new javax.swing.JButton();

        add(jcbProductSourceType);

        jlblProductType.setText("Məhsulun tipi:");
        add(jlblProductType);

        add(jcbProductType);

        jlbProducts.setText("Məhsul");
        add(jlbProducts);

        jcbProducts.setPreferredSize(new java.awt.Dimension(300, 20));
        add(jcbProducts);

        jbtnProductSearch.setText("...");
        jbtnProductSearch.setToolTipText("Məhsulların ətraflı axtarışı");
        jbtnProductSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnProductSearchActionPerformed(evt);
            }
        });
        add(jbtnProductSearch);
    }// </editor-fold>//GEN-END:initComponents

    private void jbtnProductSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnProductSearchActionPerformed
        Product product = ProductSearchDialog.showSearchProductDialog(isProduction());
        if (product != null) {
            jcbProducts.setSelectedItem(product);
        }
    }//GEN-LAST:event_jbtnProductSearchActionPerformed

    public void addProductSelectionListener(ProductSelectionListener aListener) {
        itemListenerList.add(aListener);
    }

    public void removeProductSelectionListener(ProductSelectionListener aListener) {
        itemListenerList.remove(aListener);
    }

    public ArrayList<ProductSelectionListener> getProductSelectionListeners() {
        return itemListenerList;
    }

    public boolean isProduction() {
        CodeValue sourceType = getSelectedSourceType();
        return (sourceType != null && sourceType.equals(CodeValue.PST_PRODUCTION_PRODUCT));
    }

    public void clear() {
        jcbProductSourceType.setSelectedItem(null);
        jcbProductType.setSelectedItem(null);
        jcbProducts.setSelectedItem(null);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jbtnProductSearch;
    private javax.swing.JComboBox jcbProductSourceType;
    private javax.swing.JComboBox jcbProductType;
    private javax.swing.JComboBox jcbProducts;
    private javax.swing.JLabel jlbProducts;
    private javax.swing.JLabel jlblProductType;
    // End of variables declaration//GEN-END:variables

}
