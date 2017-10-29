/*
 * ProductDialog.java
 *
 * Created on Jan 8, 2012, 6:27:29 PM
 */
package az.store.product;

import az.store.Inits;
import az.store.production.TemplateEditDialog;
import az.store.types.CodeValue;
import az.util.components.ObjectTableModelEx;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Rashad Amirjanov
 */
public class ProductDialog extends javax.swing.JDialog {

    private static ProductDialog productDialog = null;
    private ObjectTableModelEx tmProducts = new ObjectTableModelEx(new String[]{"Kodu", "Adı",
            "Tipi", "Ölçü vahidi", "Qiyməti", "Satış qiyməti", "Sadə məhsul"}) {

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            Product product = (Product) this.getRowObjects().get(rowIndex);

            columnIndex = convertToMyColumn(columnIndex);

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
                    result = product.getMeasureName();
                    break;
                case 4:
                    result = product.getPriceBuy();
                    break;
                case 5:
                    result = product.getPriceSale();
                    break;
                case 6:
                    result = product.getProductExpendituresAsString();
                    break;
            }

            if (result == null) {
                result = "";
            }

            return result;
        }
    };
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jBtnAdd;
    private javax.swing.JButton jBtnClose;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPnlClose;
    private javax.swing.JPanel jPnlTable;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton jbtnDelete;
    private javax.swing.JButton jbtnEdit;
    private javax.swing.JButton jbtnEditTemplate;
    private javax.swing.JPanel jpnlSearch;
    private javax.swing.JTable jtblProducts;
    private az.store.product.ProductSelectPanel productSelectPanel;

    /**
     * Creates new form ProductDialog
     */
    public ProductDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();

        jtblProducts.setModel(tmProducts);

        setSize(new Dimension(800, 550));
        setLocationRelativeTo(null);
        setTitle("Məhsullar");

        productSelectPanel.addProductSelectionListener(new ProductSelectionListener() {
            @Override
            public void productSourceTypeChanged() {
                boolean vis = true;
                if (productSelectPanel.getSelectedSourceType() != null && productSelectPanel.getSelectedSourceType().equals(CodeValue.PST_SIMPLE_PRODUCT)) {
                    vis = false;
                }
                tmProducts.setColumnsVisible(vis, 6);
                jbtnEditTemplate.setVisible(vis);
                refreshProducts();
            }

            @Override
            public void productTypeChanged() {
                refreshProducts();
            }

            @Override
            public void productSelected() {
                refreshProducts();
            }
        });
    }

    public static ProductDialog instance() {
        if (productDialog == null) {
            productDialog = new ProductDialog(null, true);
        }
        return productDialog;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jpnlSearch = new javax.swing.JPanel();
        productSelectPanel = new az.store.product.ProductSelectPanel();
        jPnlTable = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jtblProducts = new javax.swing.JTable();
        jPnlClose = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jBtnAdd = new javax.swing.JButton();
        jbtnEdit = new javax.swing.JButton();
        jbtnDelete = new javax.swing.JButton();
        jBtnClose = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jbtnEditTemplate = new javax.swing.JButton();

        jpnlSearch.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));
        jpnlSearch.add(productSelectPanel);

        getContentPane().add(jpnlSearch, java.awt.BorderLayout.PAGE_START);

        jPnlTable.setLayout(new java.awt.BorderLayout());

        jScrollPane1.setBorder(javax.swing.BorderFactory.createTitledBorder("Bütün məhsullar"));

        jtblProducts.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane1.setViewportView(jtblProducts);

        jPnlTable.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        getContentPane().add(jPnlTable, java.awt.BorderLayout.CENTER);

        jPnlClose.setLayout(new javax.swing.BoxLayout(jPnlClose, javax.swing.BoxLayout.LINE_AXIS));

        jPanel2.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jBtnAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/az/store/images/add.png"))); // NOI18N
        jBtnAdd.setText("Əlavə et");
        jBtnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnAddActionPerformed(evt);
            }
        });
        jPanel2.add(jBtnAdd);

        jbtnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/az/store/images/edit.png"))); // NOI18N
        jbtnEdit.setText("Seçilmişi dəyiş");
        jbtnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnEditActionPerformed(evt);
            }
        });
        jPanel2.add(jbtnEdit);

        jbtnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/az/store/images/minus.png"))); // NOI18N
        jbtnDelete.setText("Seçilmişi sil");
        jbtnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnDeleteActionPerformed(evt);
            }
        });
        jPanel2.add(jbtnDelete);

        jBtnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/az/store/images/close.png"))); // NOI18N
        jBtnClose.setText("Bağla");
        jBtnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnCloseActionPerformed(evt);
            }
        });
        jPanel2.add(jBtnClose);

        jPnlClose.add(jPanel2);

        jPanel1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        jbtnEditTemplate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/az/store/images/edit.png"))); // NOI18N
        jbtnEditTemplate.setText("Seçilmişin istehsal şablonu");
        jbtnEditTemplate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnEditTemplateActionPerformed(evt);
            }
        });
        jPanel1.add(jbtnEditTemplate);

        jPnlClose.add(jPanel1);

        getContentPane().add(jPnlClose, java.awt.BorderLayout.SOUTH);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    public void showForm(CodeValue productSourceType) {
        productSelectPanel.init();
        productSelectPanel.setSelectedProductSourceType(productSourceType);
        refreshProducts();

        this.setVisible(true);
    }

    private void refreshProducts() {
        tmProducts.removeAll();

        Integer productId = null;
        if (productSelectPanel.getSelectedProduct() != null) {
            productId = productSelectPanel.getSelectedProduct().getId();
        }

        ArrayList<Product> products = Inits.getSelectQueries().getProducts(productId,
                productSelectPanel.getSelectedProductType(), productSelectPanel.getSelectedSourceType());
        for (Product product : products) {
            tmProducts.addObject(product);
        }
    }

    private void jBtnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnAddActionPerformed
        Product product = ProductEditDialog.instance().showForm(null, productSelectPanel.getSelectedSourceType());
        refreshProducts();
        if (product != null) {
            int i = tmProducts.indexOf(product);
            if (i != -1) {
                jtblProducts.setRowSelectionInterval(i, i);
            }
        }
    }//GEN-LAST:event_jBtnAddActionPerformed

    private void jBtnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnCloseActionPerformed
        setVisible(false);
    }//GEN-LAST:event_jBtnCloseActionPerformed

    private void jbtnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnEditActionPerformed
        if (jtblProducts.getSelectedRow() == -1) {
            return;
        }

        Object object = tmProducts.getObjectAt(jtblProducts.getSelectedRow());
        if (object == null) {
            return;
        }

        Product product = (Product) object;

        Product editedProduct = ProductEditDialog.instance().showForm(product, productSelectPanel.getSelectedSourceType());
        refreshProducts();
        if (editedProduct != null) {
            int i = tmProducts.indexOf(editedProduct);
            jtblProducts.setRowSelectionInterval(i, i);
        }
    }//GEN-LAST:event_jbtnEditActionPerformed

    private void jbtnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnDeleteActionPerformed
        if (jtblProducts.getSelectedRow() == -1) {
            return;
        }

        Object object = tmProducts.getObjectAt(jtblProducts.getSelectedRow());
        if (object == null) {
            return;
        }

        Product product = (Product) object;

        ArrayList<Product> products = Inits.getSelectQueries().getProducts(product.getId(), null, null);
        if (products.size() != 1) {
            JOptionPane.showMessageDialog(null, "Uyğun məhsul tapılmadı!");
            return;
        }

        if (products.get(0).getCount() != 0) {
            JOptionPane.showMessageDialog(null, "Anbarda hələ məhsul qalıb. Ancaq onları satdıqdan sonra silə bilərsiniz!");
            Logger.getGlobal().log(Level.WARNING, "pid = {0}, count: {1}", new Object[]{products.get(0).getId(), products.get(0).getCount()});
            return;
        }

        int res = JOptionPane.showConfirmDialog(this, "Məshul silinsinmi?\n" + product.toString());
        if (res != JOptionPane.YES_OPTION) {
            return;
        }

        products.get(0).setValid(false);
        try {
            Inits.getDeleteQueries().deleteProduct(product.getId());
        } catch (Exception ex) {
            Logger.getGlobal().log(Level.WARNING, "", ex);
            JOptionPane.showMessageDialog(null, "Səhv baş verdi");
        }
        refreshProducts();
    }//GEN-LAST:event_jbtnDeleteActionPerformed

    private void jbtnEditTemplateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnEditTemplateActionPerformed
        if (jtblProducts.getSelectedRow() == -1) {
            return;
        }

        Object object = tmProducts.getObjectAt(jtblProducts.getSelectedRow());
        if (object == null) {
            return;
        }

        Product selectedProduct = (Product) object;

        TemplateEditDialog editDialog = new TemplateEditDialog(this, true);
        editDialog.showForm(selectedProduct, true);

        refreshProducts();
    }//GEN-LAST:event_jbtnEditTemplateActionPerformed
    // End of variables declaration//GEN-END:variables
}
