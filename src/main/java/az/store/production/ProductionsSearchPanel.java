package az.store.production;

import az.store.Inits;
import az.store.product.ProductSelectionListener;
import az.store.types.CodeValue;
import az.util.components.ExcelWriter;
import az.util.components.ObjectTableModel;
import az.util.utils.Utils;
import jxl.write.WriteException;

import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * @author Rashad Amirjanov
 */
public class ProductionsSearchPanel extends javax.swing.JPanel {

    private final ObjectTableModel tmProductons = new ObjectTableModel(new String[]{
            "İst. məhsulu", "İst. tarixi", "İst. sayı", "İst. qiyməti"
    }) {

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            Production production = (Production) this.getRowObjects().get(rowIndex);

            if (production.getId() == -2) {
                return "";
            }

            Object result = "";

//            columnIndex = convertToMyColumn(columnIndex);
            switch (columnIndex) {
                case 0:
                    result = production.getProductionProduct().toString();
                    break;
                case 1:
                    result = Utils.toStringDateTime(production.getProductionDate());
                    break;
                case 2:
                    result = production.getCount();
                    break;
                case 3:
                    result = production.getProductionPrice();
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
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JButton jbtnClear;
    private javax.swing.JButton jbtnDelete;
    private javax.swing.JButton jbtnExcel;
    private javax.swing.JButton jbtnSearch;
    private javax.swing.JButton jbtnShowDetailed;
    private az.util.components.JDateChooserEx jdtDateFrom;
    private az.util.components.JDateChooserEx jdtDateTo;
    private az.store.product.ProductSelectPanel jpnSelectProductionProduct;
    private javax.swing.JPanel jpnlButtons;
    private javax.swing.JPanel jpnlInvoiceDetailed;
    private javax.swing.JPanel jpnlOperations;
    private javax.swing.JPanel jpnlProducts;
    private javax.swing.JPanel jpnlSearch;
    private javax.swing.JScrollPane jscrollPaneProductResult;
    private javax.swing.JTable jtbResult;

    /**
     * Creates new form ProductionSearchPanel2
     */
    public ProductionsSearchPanel() {
        initComponents();

        jtbResult.setModel(tmProductons);

        jpnSelectProductionProduct.init();
        resetProductionColumns();

        jpnSelectProductionProduct.setSelectedProductSourceType(CodeValue.PST_PRODUCTION_PRODUCT);
        jpnSelectProductionProduct.getProductSourceTypesComboBox().setVisible(false);
        jpnSelectProductionProduct.getProductTypesComboBox().setVisible(false);
        jpnSelectProductionProduct.getProductTypesLabel().setVisible(false);
        jpnSelectProductionProduct.getProductsLabel().setText("İstehsal məhsulu");

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) - 1);
        jdtDateFrom.setDate(calendar.getTime());
        jdtDateTo.setDate(new Date());

        jpnSelectProductionProduct.addProductSelectionListener(new ProductSelectionListener() {
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

        jbtnShowDetailed.setVisible(false);
    }

    private void resetProductionColumns() {
        tmProductons.removeAll();

//        boolean isProduction = jpnSelectProductionProduct.getSelectedSourceType() != null
//                && jpnSelectProductionProduct.getSelectedSourceType().equals(CodeValue.PST_PRODUCTION_PRODUCT);
//        if (isProduction) {
//            tmProductons.setColumnsVisible(true, 6, 8);
//        } else {
//            tmProductons.setColumnsVisible(false, 6, 8);
//        }
    }

    private void research() {
        Date dateFrom = jdtDateFrom.getDate();
        Date dateTo = jdtDateTo.getDate();

        Integer productionProductId = null;
        if (jpnSelectProductionProduct.getSelectedProduct() != null) {
            productionProductId = jpnSelectProductionProduct.getSelectedProduct().getId();
        }

        tmProductons.removeAll();

        ArrayList<Production> productions = Inits.getSelectQueries().getProductions(productionProductId, dateFrom, dateTo);
        for (Production prod : productions) {
            tmProductons.addObject(prod);
        }
//        {//summing totals
//            InvoiceDetailed detailedTotal = new InvoiceDetailed();
//            detailedTotal.setId(-1);
//            detailedTotal.setInvoice(new Invoice(-1));
//            detailedTotal.getInvoice().setClient(new CodeValue(-1, ""));
//            detailedTotal.getInvoice().setDate(null);
//            detailedTotal.getInvoice().setInvoiceType(new InvoiceType(-1, ""));
//            detailedTotal.setProduct(new Product(-1));
//            detailedTotal.getProduct().setName("Ümumi");
//            detailedTotal.getProduct().setType(new CodeValue(-1, ""));
//            detailedTotal.setTotalPriceBuy(0.);
//            detailedTotal.setTotalPriceSale(0.);
//            for (InvoiceDetailed detailed : productions) {
//                detailedTotal.setCount(detailedTotal.getCount() + detailed.getCount());
//                detailedTotal.setPriceBuy(detailedTotal.getPriceBuy() + detailed.getPriceBuy());
//                detailedTotal.setPriceSale(detailedTotal.getPriceSale() + detailed.getPriceSale());
//                detailedTotal.setTotalPriceBuy(detailedTotal.getTotalPriceBuy() + detailed.getTotalPriceBuy());
//                detailedTotal.setTotalPriceSale(detailedTotal.getTotalPriceSale() + detailed.getTotalPriceSale());
//            }
//            tmInvoiceDetaileds.addObject(new InvoiceDetailed(-2));
//            tmInvoiceDetaileds.addObject(detailedTotal);
//        }
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
        jpnlProducts = new javax.swing.JPanel();
        jpnSelectProductionProduct = new az.store.product.ProductSelectPanel();
        jpnlInvoiceDetailed = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jdtDateFrom = new az.util.components.JDateChooserEx();
        jLabel4 = new javax.swing.JLabel();
        jdtDateTo = new az.util.components.JDateChooserEx();
        jpnlButtons = new javax.swing.JPanel();
        jbtnSearch = new javax.swing.JButton();
        jbtnClear = new javax.swing.JButton();
        jscrollPaneProductResult = new javax.swing.JScrollPane();
        jtbResult = new javax.swing.JTable();
        jpnlOperations = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jbtnShowDetailed = new javax.swing.JButton();
        jbtnDelete = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jbtnExcel = new javax.swing.JButton();

        setLayout(new java.awt.BorderLayout());

        jpnlSearch.setLayout(new javax.swing.BoxLayout(jpnlSearch, javax.swing.BoxLayout.PAGE_AXIS));

        jpnlProducts.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jpnlProducts.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 0, 0));
        jpnlProducts.add(jpnSelectProductionProduct);

        jpnlSearch.add(jpnlProducts);

        jpnlInvoiceDetailed.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jpnlInvoiceDetailed.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jLabel3.setText("Tarixdən:");
        jpnlInvoiceDetailed.add(jLabel3);

        jdtDateFrom.setPreferredSize(new java.awt.Dimension(120, 20));
        jpnlInvoiceDetailed.add(jdtDateFrom);

        jLabel4.setText("Tarixədək:");
        jpnlInvoiceDetailed.add(jLabel4);

        jdtDateTo.setPreferredSize(new java.awt.Dimension(120, 20));
        jpnlInvoiceDetailed.add(jdtDateTo);

        jpnlSearch.add(jpnlInvoiceDetailed);

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
        jpnlOperations.setLayout(new java.awt.GridLayout());

        java.awt.FlowLayout flowLayout2 = new java.awt.FlowLayout(java.awt.FlowLayout.LEFT);
        flowLayout2.setAlignOnBaseline(true);
        jPanel1.setLayout(flowLayout2);

        jbtnShowDetailed.setText("Seçilmişə ətraflı bax");
        jbtnShowDetailed.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnShowDetailedActionPerformed(evt);
            }
        });
        jPanel1.add(jbtnShowDetailed);

        jbtnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/az/store/images/minus.png"))); // NOI18N
        jbtnDelete.setText("Səhv istehsalı sil");
        jbtnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnDeleteActionPerformed(evt);
            }
        });
        jPanel1.add(jbtnDelete);

        jpnlOperations.add(jPanel1);

        jPanel2.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        jbtnExcel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/az/store/images/excel.png"))); // NOI18N
        jbtnExcel.setText("Çapa ver");
        jbtnExcel.setPreferredSize(new java.awt.Dimension(110, 25));
        jbtnExcel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnExcelActionPerformed(evt);
            }
        });
        jPanel2.add(jbtnExcel);

        jpnlOperations.add(jPanel2);

        add(jpnlOperations, java.awt.BorderLayout.SOUTH);
    }// </editor-fold>//GEN-END:initComponents

    private void jbtnSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnSearchActionPerformed
        research();
    }//GEN-LAST:event_jbtnSearchActionPerformed

    private void jbtnClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnClearActionPerformed
        jpnSelectProductionProduct.setSelectedProduct(null);
        jdtDateFrom.setDate(null);
        jdtDateTo.setDate(null);

        tmProductons.removeAll();
    }//GEN-LAST:event_jbtnClearActionPerformed

    private void jbtnExcelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnExcelActionPerformed
        try {
            ExcelWriter.viewAsExcelFile(new ObjectTableModel[]{tmProductons}, new String[]{"İstehsallar"});
        } catch (IOException | WriteException ex) {
            ex.printStackTrace();
        }
    }//GEN-LAST:event_jbtnExcelActionPerformed

    private void jbtnShowDetailedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnShowDetailedActionPerformed
        int idx = jtbResult.getSelectedRow();
        if (idx == -1) {
            return;
        }

        Production production = (Production) tmProductons.getObjectAt(idx);
        if (production == null || production.getId() < 0) {
            return;
        }

//        productionSaleDialog.showForm(production.getInvoiceType().getId(), "Qaimə " + production.getId(), production);
    }//GEN-LAST:event_jbtnShowDetailedActionPerformed

    private void jbtnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnDeleteActionPerformed
        int idx = jtbResult.getSelectedRow();
        if (idx == -1) {
            return;
        }

        Production production = (Production) tmProductons.getObjectAt(idx);
        if (production == null || production.getId() < 0) {
            return;
        }

        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_MONTH, -7);
        if (production.getProductionDate().before(c.getTime())) {
            JOptionPane.showMessageDialog(this, "Silmək istədiyiniz məhsulu 1 həftədən artıq müddətdir ki, istehsal olunub.\nOna görə də silinə bilməz!");
            return;
        }

        int res = JOptionPane.showConfirmDialog(this, "İstehsal silinsinmi (" + production.getId() + ")?\n"
                        + "\n1. Nəzərə alın ki, istehsal məhsulu silindikdən sonra bütün mallar geri qaytarılacaq!"
                        + "\n2. Əgər bu məhsulu istehsaldan sonra satmısınızsa, silməyin, problemlər çıxa bilər! ",
                "Silinsinmi?", JOptionPane.YES_NO_OPTION);
        if (res != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            Inits.getDb().setAutoCommit(false);
            ArrayList<Expenditure> expenditures = Inits.getSelectQueries().getProductExpenditures(production.getProductionProduct().getId());

            //return simple products back
            for (Expenditure expenditure : expenditures) {
                if (expenditure.isProduct()) {//simple product
                    Inits.getUpdateQueries().increaseProductCount(expenditure.getObjectAsProduct().getId(), expenditure.getPrice() * production.getCount());
                }
            }

            Inits.getUpdateQueries().increaseProductCount(production.getProductionProduct().getId(), -production.getCount());
            Inits.getDeleteQueries().deleteProduction(production.getId());
        } catch (Exception ex) {
            Inits.getDb().rollback();
            JOptionPane.showMessageDialog(null, "Səhv baş verdi:\n" + ex.getMessage());
        } finally {
            Inits.getDb().setAutoCommit(true);
        }
        research();
    }//GEN-LAST:event_jbtnDeleteActionPerformed
    // End of variables declaration//GEN-END:variables
}
