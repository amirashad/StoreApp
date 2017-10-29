package az.store.product;

import az.store.Inits;
import az.store.types.CodeValue;
import az.util.components.ExcelWriter;
import az.util.components.ObjectTableModel;
import az.util.components.ObjectTableModelEx;
import az.util.utils.Utils;
import jxl.write.WriteException;

import java.io.IOException;
import java.util.ArrayList;

/**
 * @author Rashad Amirjanov
 */
public class CurrentStoreSearchPanel extends javax.swing.JPanel {

    private ObjectTableModelEx tmCurrentStore = new ObjectTableModelEx(new String[]{
            "Məhsulun tipi", "Məhsulun adı", "Sayı",
            "Qiyməti", "Satış qiyməti", "Ümumi qiyməti", "Ümumi satış qiyməti"}) {

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            Product product = (Product) this.getRowObjects().get(rowIndex);

            if (product.getId() == -2) {
                return "";
            }

            Object result = "";

            columnIndex = convertToMyColumn(columnIndex);

            if (product.getId() == -1 && (columnIndex == 3 || columnIndex == 4)) {
                return "";
            }

            switch (columnIndex) {
                case 0:
                    result = product.getTypeName();
                    break;
                case 1:
                    result = product.toString();
                    break;
                case 2:
                    result = product.getCountWithMeasure();
                    break;
                case 3:
                    result = Utils.toString(product.getPriceBuy());
                    break;
                case 4:
                    result = Utils.toString(product.getPriceSale());
                    break;
                case 5:
                    result = Utils.toString(product.getTotalPriceBuy());
                    break;
                case 6:
                    result = Utils.toString(product.getTotalPriceSale());
                    break;
            }

            if (result == null) {
                result = "";
            }

            return result;
        }
    };
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton jbtnClear;
    private javax.swing.JButton jbtnExcel;
    private javax.swing.JButton jbtnSearch;
    private javax.swing.JPanel jpnlButtons;
    private javax.swing.JPanel jpnlCurrentStore;
    private javax.swing.JPanel jpnlOperations;
    private az.store.product.ProductSelectPanel jpnlProductSelect;
    private javax.swing.JPanel jpnlSearch;
    private javax.swing.JScrollPane jscrollPaneProductResult;
    private javax.swing.JTable jtbResult;

    /**
     * Creates new form CurrentStoreSearchPanel
     */
    public CurrentStoreSearchPanel() {
        initComponents();

        jtbResult.setModel(tmCurrentStore);

        jpnlProductSelect.addProductSelectionListener(new ProductSelectionListener() {
            @Override
            public void productSourceTypeChanged() {
                resetProductionColumns();
            }

            @Override
            public void productTypeChanged() {
//                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void productSelected() {
//                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        });

        jpnlProductSelect.init();
        resetProductionColumns();
    }

    private void resetProductionColumns() {
        tmCurrentStore.removeAll();

        if (jpnlProductSelect.isProduction()) {
            tmCurrentStore.setColumnsVisible(true, 4, 6);
        } else {
            tmCurrentStore.setColumnsVisible(false, 4, 6);
        }
    }

    private void research() {
        CodeValue sourceType = null;
        if (jpnlProductSelect.getSelectedSourceType() != null) {
            sourceType = jpnlProductSelect.getSelectedSourceType();
        }

        CodeValue productType = null;
        if (jpnlProductSelect.getSelectedProductType() != null) {
            productType = jpnlProductSelect.getSelectedProductType();
        }

        Integer productId = null;
        if (jpnlProductSelect.getSelectedProduct() != null) {
            productId = jpnlProductSelect.getSelectedProduct().getId();
        }

        tmCurrentStore.removeAll();

        ArrayList<Product> products = Inits.getSelectQueries().getProducts(productId, productType, sourceType);

        for (Product product : products) {
            tmCurrentStore.addObject(product);
        }
        {//summing totals
            Product productTotal = new Product();
            productTotal.setId(-1);
            productTotal.setCode("");
            productTotal.setName("Ümumi");
            productTotal.setType(new CodeValue(0, ""));
            productTotal.setTotalPriceBuy(0.);
            productTotal.setTotalPriceSale(0.);
            productTotal.setPriceBuy(0.);
            productTotal.setPriceSale(0.);
            for (Product product : products) {
                productTotal.setCount(productTotal.getCount() + product.getCount());
                productTotal.setTotalPriceBuy(productTotal.getTotalPriceBuy() + product.getTotalPriceBuy());
                productTotal.setTotalPriceSale(productTotal.getTotalPriceSale() + product.getTotalPriceSale());
            }
            tmCurrentStore.addObject(new Product(-2));
            tmCurrentStore.addObject(productTotal);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jpnlSearch = new javax.swing.JPanel();
        jpnlCurrentStore = new javax.swing.JPanel();
        jpnlProductSelect = new az.store.product.ProductSelectPanel();
        jpnlButtons = new javax.swing.JPanel();
        jbtnSearch = new javax.swing.JButton();
        jbtnClear = new javax.swing.JButton();
        jscrollPaneProductResult = new javax.swing.JScrollPane();
        jtbResult = new javax.swing.JTable();
        jpnlOperations = new javax.swing.JPanel();
        jbtnExcel = new javax.swing.JButton();

        setLayout(new java.awt.BorderLayout());

        jpnlSearch.setLayout(new javax.swing.BoxLayout(jpnlSearch, javax.swing.BoxLayout.PAGE_AXIS));

        jpnlCurrentStore.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jpnlCurrentStore.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));
        jpnlCurrentStore.add(jpnlProductSelect);

        jpnlSearch.add(jpnlCurrentStore);

        jpnlButtons.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jpnlButtons.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        jbtnSearch.setIcon(new javax.swing.ImageIcon(getClass().getResource("/az/store/images/Search.png"))); // NOI18N
        jbtnSearch.setText("Axtar");
        jbtnSearch.setPreferredSize(new java.awt.Dimension(90, 25));
        jbtnSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnSearchActionPerformed(evt);
            }
        });
        jpnlButtons.add(jbtnSearch);

        jbtnClear.setIcon(new javax.swing.ImageIcon(getClass().getResource("/az/store/images/clear.png"))); // NOI18N
        jbtnClear.setText("Təmizlə");
        jbtnClear.setPreferredSize(new java.awt.Dimension(90, 25));
        jbtnClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnClearActionPerformed(evt);
            }
        });
        jpnlButtons.add(jbtnClear);

        jpnlSearch.add(jpnlButtons);

        add(jpnlSearch, java.awt.BorderLayout.NORTH);

        jscrollPaneProductResult.setBorder(javax.swing.BorderFactory.createTitledBorder("Nəticə"));

        jtbResult.setModel(new javax.swing.table.DefaultTableModel(
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
        jscrollPaneProductResult.setViewportView(jtbResult);

        add(jscrollPaneProductResult, java.awt.BorderLayout.CENTER);

        jpnlOperations.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jpnlOperations.setPreferredSize(new java.awt.Dimension(1024, 80));
        jpnlOperations.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        jbtnExcel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/az/store/images/excel.png"))); // NOI18N
        jbtnExcel.setText("Çapa ver");
        jbtnExcel.setPreferredSize(new java.awt.Dimension(110, 25));
        jbtnExcel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnExcelActionPerformed(evt);
            }
        });
        jpnlOperations.add(jbtnExcel);

        add(jpnlOperations, java.awt.BorderLayout.SOUTH);
    }// </editor-fold>//GEN-END:initComponents

    private void jbtnSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnSearchActionPerformed
        research();
    }//GEN-LAST:event_jbtnSearchActionPerformed

    private void jbtnClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnClearActionPerformed
        jpnlProductSelect.clear();
        tmCurrentStore.removeAll();
    }//GEN-LAST:event_jbtnClearActionPerformed

    private void jbtnExcelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnExcelActionPerformed
        try {
            ExcelWriter.viewAsExcelFile(new ObjectTableModel[]{tmCurrentStore}, new String[]{"Cari anbar"});
        } catch (IOException | WriteException ex) {
            ex.printStackTrace();
        }
    }//GEN-LAST:event_jbtnExcelActionPerformed
    // End of variables declaration//GEN-END:variables
}
