package az.store.product;

import az.store.Inits;
import az.store.types.CodeType;
import az.store.types.CodeValue;
import az.util.components.ObjectTableModel;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;

/**
 * @author Rashad Amirjanov
 */
public class ProductSearchDialog extends javax.swing.JDialog {

    private static ProductSearchDialog productSearchPanel = null;
    private boolean production = false;
    private ArrayList<Product> products = null;
    private Product selectedProduct = null;
    private ObjectTableModel tmProducts = new ObjectTableModel(new String[]{"Kodu", "Adı", "Tipi", "Anbarda sayı"}) {
        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            Product product = (Product) this.getRowObjects().get(rowIndex);

            Object result = "";
            switch (columnIndex) {
                case 0:
                    result = product.getCode();
                    break;
                case 1:
                    result = product.getName();
                    break;
                case 2:
                    result = product.getTypeName();
                    break;
                case 3:
                    result = product.getCount();
                    break;
//                case 4:
//                    result = product.getPriceBuy();
//                    break;
//                case 5:
//                    result = product.getPriceSale();
//                    break;
//                case 6:
//                    result = Utils.toString(product.getCount() * product.getPriceBuy());
//                    break;
//                case 7:
//                    result = Utils.toString(product.getCount() * product.getPriceSale());
//                    break;
            }

            if (result == null) {
                result = "";
            }

            return result;

        }
    };
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JButton jbtnOk;
    private javax.swing.JComboBox<CodeValue> jcbProductTypes;
    private javax.swing.JTable jtbProducts;
    private javax.swing.JTextField jtxtProductName;

    /**
     * Creates new form ProductSearchPanel
     */
    public ProductSearchDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        setLocationRelativeTo(parent);
        jtbProducts.setModel(tmProducts);

        jcbProductTypes.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                researchProducts();
            }

        });

        jtxtProductName.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                researchProducts();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                researchProducts();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                researchProducts();
            }
        });
    }

    public static Product showSearchProductDialog(boolean production) {
        if (productSearchPanel == null) {
            productSearchPanel = new ProductSearchDialog(null, true);
        }
        productSearchPanel.showForm(production);
        return productSearchPanel.getSelectedProduct();
    }

    void showForm(boolean production) {
        this.production = production;
        products = null;
        jcbProductTypes.setSelectedIndex(-1);
        jtxtProductName.setText("");

        initProductTypes();
        researchProducts();

        this.setTitle("Məhsulların axtarışı");
        selectedProduct = null;
        this.setVisible(true);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jcbProductTypes = new javax.swing.JComboBox<>();
        jLabel2 = new javax.swing.JLabel();
        jtxtProductName = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jbtnOk = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jtbProducts = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new java.awt.BorderLayout(10, 10));

        jLabel1.setText("Tipi");

        jLabel2.setText("Adı");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(4, 4, 4)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 94, Short.MAX_VALUE)
                                        .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jtxtProductName)
                                        .addComponent(jcbProductTypes, 0, 350, Short.MAX_VALUE)))
        );
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel1)
                                        .addComponent(jcbProductTypes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jtxtProductName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel2))
                                .addGap(0, 0, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel1, java.awt.BorderLayout.NORTH);

        jbtnOk.setText("Ok");
        jbtnOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnOkActionPerformed(evt);
            }
        });
        jPanel2.add(jbtnOk);

        getContentPane().add(jPanel2, java.awt.BorderLayout.SOUTH);

        jtbProducts.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{
                        {null, null, null, null},
                        {null, null, null, null},
                        {null, null, null, null},
                        {null, null, null, null}
                },
                new String[]{
                        "Title 1", "Title 2", "Title 3", "Title 4"
                }
        ));
        jScrollPane2.setViewportView(jtbProducts);

        getContentPane().add(jScrollPane2, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jbtnOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnOkActionPerformed
        Product product = (Product) tmProducts.getObjectAt(jtbProducts.getSelectedRow());

        if (product != null) {
            selectedProduct = product;
        } else {
            return;
        }

        this.setVisible(false);
    }//GEN-LAST:event_jbtnOkActionPerformed

    private void initProductTypes() {
        jcbProductTypes.removeAllItems();

//        if (!production) {
        jcbProductTypes.addItem(new CodeValue(-1, "Hamısı"));
        ArrayList<CodeValue> codeValuesProductType = Inits.getSelectQueries().
                getCodeValues(CodeType.PRODUCT_TYPE);
        for (CodeValue codeValue : codeValuesProductType) {
            jcbProductTypes.addItem(codeValue);
        }
//        } else {
//            jcbProductTypes.addItem(new CodeValue(-1, "İstehsal məhsulları"));
//        }
    }

    private void initProducts() {
        if (products == null || products.isEmpty()) {
            if (!production) {
                products = Inits.getSelectQueries().getProducts(null, null, CodeValue.PST_SIMPLE_PRODUCT);
            } else {
                products = Inits.getSelectQueries().getProducts(null, null, CodeValue.PST_PRODUCTION_PRODUCT);
            }
        }
    }

    private void researchProducts() {
        initProducts();

        CodeValue productType = (CodeValue) jcbProductTypes.getSelectedItem();
        String productNameLike = jtxtProductName.getText().trim().toUpperCase();

        if (productType != null && productType.getId() == -1) {
            productType = null;
        }

        tmProducts.removeAll();
        for (Product product : products) {
            if (productType != null && !product.getType().equals(productType)) {
                continue;
            }
            if (productNameLike != null && product.toString().toUpperCase().contains(productNameLike)) {
                tmProducts.addObject(product);
            }
        }
    }

    public Product getSelectedProduct() {
        return selectedProduct;
    }
    // End of variables declaration//GEN-END:variables

}
