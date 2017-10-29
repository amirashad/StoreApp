package az.store.invoice;

import az.store.Inits;
import az.store.product.Product;
import az.store.product.ProductSelectionListener;
import az.store.types.CodeValue;
import az.util.components.ExcelWriter;
import az.util.components.ObjectTableModel;
import az.util.components.ObjectTableModelEx;
import az.util.utils.Utils;
import jxl.write.WriteException;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * @author Rashad Amirjanov
 */
public class InvoiceDetailedSearchPanel extends javax.swing.JPanel {

    private final ObjectTableModelEx tmInvoiceDetaileds = new ObjectTableModelEx(new String[]{
            "Müştəri", "Tarix",
            "Məhsulun tipi", "Məhsul", "Sayı",
            "Qiyməti", "Satış qiyməti",
            "Ümumi qiym.", "Ümumi Satış qiym.", "Qaimə"}) {

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            InvoiceDetailed detailed = (InvoiceDetailed) this.getRowObjects().get(rowIndex);

            if (detailed.getId() == -2) {
                return "";
            }

            Object result = "";

            columnIndex = convertToMyColumn(columnIndex);

            if (detailed.getId() == -1 && (columnIndex == 5 || columnIndex == 6)) {
                return "";
            }

            switch (columnIndex) {
                case 0:
                    result = detailed.getInvoice().getClient().getName();
                    break;
                case 1:
                    result = Utils.toStringDate(detailed.getInvoice().getDate());
                    break;
                case 2:
                    result = detailed.getProduct().getTypeName();
                    break;
                case 3:
                    result = detailed.getProduct().toString();
                    break;
                case 4:
                    result = detailed.getCountWithMeasure();
                    break;
                case 5:
                    result = Utils.toString(detailed.getPriceBuy());
                    break;
                case 6:
                    result = Utils.toString(detailed.getPriceSale());
                    break;
                case 7:
                    result = Utils.toString(detailed.getTotalPriceBuy());
                    break;
                case 8:
                    result = Utils.toString(detailed.getTotalPriceSale());
                    break;
                case 9:
                    result = detailed.getInvoice().getInvoiceType().getName();
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
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JButton jbtnClear;
    private javax.swing.JButton jbtnExcel;
    private javax.swing.JButton jbtnSearch;
    private javax.swing.JComboBox jcbClients;
    private javax.swing.JComboBox jcbInvoiceTypes;
    private az.util.components.JDateChooserEx jdtDateFrom;
    private az.util.components.JDateChooserEx jdtDateTo;
    private javax.swing.JLabel jlbClient;
    private javax.swing.JPanel jpnlButtons;
    private javax.swing.JPanel jpnlFromSimpleProduct;
    private javax.swing.JPanel jpnlInvoiceDetailed;
    private javax.swing.JPanel jpnlOperations;
    private az.store.product.ProductSelectPanel jpnlProductSelect;
    private javax.swing.JPanel jpnlProducts;
    private javax.swing.JPanel jpnlSearch;
    private az.store.product.ProductSelectPanel jpnlSimpleProductSelect;
    private javax.swing.JScrollPane jscrollPaneProductResult;
    private javax.swing.JTable jtbResult;

    /**
     * Creates new form ProductionSearchPanel2
     */
    public InvoiceDetailedSearchPanel() {
        initComponents();

        jtbResult.setModel(tmInvoiceDetaileds);

        jpnlProductSelect.init();
        resetProductionColumns();
        refreshInvoiceTypes();

        jpnlSimpleProductSelect.init();
        jpnlSimpleProductSelect.setSelectedProductSourceType(CodeValue.PST_SIMPLE_PRODUCT);
        jpnlSimpleProductSelect.getProductSourceTypesComboBox().setVisible(false);
        jpnlSimpleProductSelect.getProductTypesComboBox().setVisible(false);
        jpnlSimpleProductSelect.getProductTypesLabel().setVisible(false);
        jpnlSimpleProductSelect.getProductsLabel().setText("Sadə məhsul");

        jpnlProductSelect.addProductSelectionListener(new ProductSelectionListener() {
            @Override
            public void productSourceTypeChanged() {
                resetProductionColumns();
                refreshInvoiceTypes();

                boolean isProduction = jpnlProductSelect.getSelectedSourceType() != null
                        && jpnlProductSelect.getSelectedSourceType().equals(CodeValue.PST_PRODUCTION_PRODUCT);

                if (isProduction) {
                    jpnlFromSimpleProduct.setVisible(true);
                } else {
                    jpnlFromSimpleProduct.setVisible(false);
                }
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
    }

    private InvoiceType getSelectedInvoiceType() {
        return (InvoiceType) jcbInvoiceTypes.getSelectedItem();
    }

    private void resetProductionColumns() {
        tmInvoiceDetaileds.removeAll();

        boolean isProduction = jpnlProductSelect.getSelectedSourceType() != null
                && jpnlProductSelect.getSelectedSourceType().equals(CodeValue.PST_PRODUCTION_PRODUCT);

        if (isProduction) {
            tmInvoiceDetaileds.setColumnsVisible(true, 6, 8);
        } else {
            tmInvoiceDetaileds.setColumnsVisible(false, 6, 8);
        }
    }

    public void refreshClients() {
        jcbClients.removeAllItems();
        jcbClients.addItem(null);

        ArrayList<CodeValue> codeValuesClients = Inits.getSelectQueries().getCodeValues(getSelectedInvoiceType().getClientCodeType());
        for (CodeValue codeValue : codeValuesClients) {
            jcbClients.addItem(codeValue);
        }
    }

    public final void refreshInvoiceTypes() {
        jcbInvoiceTypes.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                resetProductionColumns();
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    if (getSelectedInvoiceType() != null) {
                        jlbClient.setText(getSelectedInvoiceType().getClientLabelName());
                    }

                    refreshClients();
                }
            }
        });

        jcbInvoiceTypes.removeAllItems();
        jcbInvoiceTypes.addItem(null);

        if (jpnlProductSelect.getSelectedSourceType() == null) {
            return;
        }

        ArrayList<InvoiceType> invoiceTypes = Inits.getSelectQueries().getAllInvoiceTypes();
        for (InvoiceType invoiceType : invoiceTypes) {
            if (jpnlProductSelect.getSelectedSourceType().equals(CodeValue.PST_PRODUCTION_PRODUCT) == invoiceType.isProduction()) {
                jcbInvoiceTypes.addItem(invoiceType);
            }
        }
    }

    private void research() {
        Integer clientId = null;
        if (jcbClients.getSelectedItem() != null && jcbClients.getSelectedIndex() > 0) {
            clientId = ((CodeValue) jcbClients.getSelectedItem()).getId();
        }

        List<InvoiceType> invoiceTypes = null;
        if (getSelectedInvoiceType() != null) {
            invoiceTypes = Collections.singletonList((InvoiceType) jcbInvoiceTypes.getSelectedItem());
        }

        Date dateFrom = jdtDateFrom.getDate();
        Date dateTo = jdtDateTo.getDate();

//        boolean isProduction = jpnlProductSelect.getSelectedSourceType().equals(CodeValue.PST_PRODUCTION_PRODUCT);
        CodeValue productSourceType = jpnlProductSelect.getSelectedSourceType();
        CodeValue productType = jpnlProductSelect.getSelectedProductType();

        Integer productId = null;
        if (jpnlProductSelect.getSelectedProduct() != null) {
            productId = jpnlProductSelect.getSelectedProduct().getId();
        }

        Integer simpleProductForPPId = null;
        if (jpnlProductSelect.getSelectedSourceType().equals(CodeValue.PST_PRODUCTION_PRODUCT)
                && jpnlSimpleProductSelect.getSelectedProduct() != null) {
            simpleProductForPPId = jpnlSimpleProductSelect.getSelectedProduct().getId();
        }

        tmInvoiceDetaileds.removeAll();

        ArrayList<InvoiceDetailed> detaileds;
//        if (!isProduction) {
        detaileds = Inits.getSelectQueries().
                getInvoiceDetaileds(null, clientId, false, dateFrom, dateTo, invoiceTypes,
                        productSourceType, productType, productId,
                        simpleProductForPPId, null);
//        } else {
//            detaileds = Inits.getProductionQueries().
//                    getInvoiceDetaileds(null, clientId, false, dateFrom, dateTo, null, null, invoiceTypes);
//        }

        for (InvoiceDetailed detailed : detaileds) {
            tmInvoiceDetaileds.addObject(detailed);
        }
        {//summing totals
            InvoiceDetailed detailedTotal = new InvoiceDetailed();
            detailedTotal.setId(-1);
            detailedTotal.setInvoice(new Invoice(-1));
            detailedTotal.getInvoice().setClient(new CodeValue(-1, ""));
            detailedTotal.getInvoice().setDate(null);
            detailedTotal.getInvoice().setInvoiceType(new InvoiceType(-1, ""));
            detailedTotal.setProduct(new Product(-1));
            detailedTotal.getProduct().setName("Ümumi");
            detailedTotal.getProduct().setType(new CodeValue(-1, ""));
            detailedTotal.setTotalPriceBuy(0.);
            detailedTotal.setTotalPriceSale(0.);
            for (InvoiceDetailed detailed : detaileds) {
                detailedTotal.setCount(detailedTotal.getCount() + detailed.getCount());
                detailedTotal.setPriceBuy(detailedTotal.getPriceBuy() + detailed.getPriceBuy());
                detailedTotal.setPriceSale(detailedTotal.getPriceSale() + detailed.getPriceSale());
                detailedTotal.setTotalPriceBuy(detailedTotal.getTotalPriceBuy() + detailed.getTotalPriceBuy());
                detailedTotal.setTotalPriceSale(detailedTotal.getTotalPriceSale() + detailed.getTotalPriceSale());
            }
            tmInvoiceDetaileds.addObject(new InvoiceDetailed(-2));
            tmInvoiceDetaileds.addObject(detailedTotal);
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
        jpnlProducts = new javax.swing.JPanel();
        jpnlProductSelect = new az.store.product.ProductSelectPanel();
        jpnlInvoiceDetailed = new javax.swing.JPanel();
        jcbInvoiceTypes = new javax.swing.JComboBox();
        jlbClient = new javax.swing.JLabel();
        jcbClients = new javax.swing.JComboBox();
        jLabel3 = new javax.swing.JLabel();
        jdtDateFrom = new az.util.components.JDateChooserEx();
        jLabel4 = new javax.swing.JLabel();
        jdtDateTo = new az.util.components.JDateChooserEx();
        jpnlFromSimpleProduct = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jpnlSimpleProductSelect = new az.store.product.ProductSelectPanel();
        jpnlButtons = new javax.swing.JPanel();
        jbtnSearch = new javax.swing.JButton();
        jbtnClear = new javax.swing.JButton();
        jscrollPaneProductResult = new javax.swing.JScrollPane();
        jtbResult = new javax.swing.JTable();
        jpnlOperations = new javax.swing.JPanel();
        jbtnExcel = new javax.swing.JButton();

        setLayout(new java.awt.BorderLayout());

        jpnlSearch.setLayout(new javax.swing.BoxLayout(jpnlSearch, javax.swing.BoxLayout.PAGE_AXIS));

        jpnlProducts.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jpnlProducts.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 0, 0));
        jpnlProducts.add(jpnlProductSelect);

        jpnlSearch.add(jpnlProducts);

        jpnlInvoiceDetailed.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jpnlInvoiceDetailed.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jpnlInvoiceDetailed.add(jcbInvoiceTypes);

        jlbClient.setText("Müştəri:");
        jpnlInvoiceDetailed.add(jlbClient);

        jcbClients.setPreferredSize(new java.awt.Dimension(80, 20));
        jpnlInvoiceDetailed.add(jcbClients);

        jLabel3.setText("Tarixdən:");
        jpnlInvoiceDetailed.add(jLabel3);

        jdtDateFrom.setPreferredSize(new java.awt.Dimension(120, 20));
        jpnlInvoiceDetailed.add(jdtDateFrom);

        jLabel4.setText("Tarixədək:");
        jpnlInvoiceDetailed.add(jLabel4);

        jdtDateTo.setPreferredSize(new java.awt.Dimension(120, 20));
        jpnlInvoiceDetailed.add(jdtDateTo);

        jpnlSearch.add(jpnlInvoiceDetailed);

        jpnlFromSimpleProduct.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jpnlFromSimpleProduct.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jLabel1.setText("Bu sadə məhsuldan hazırlanan bütün istehsal məhsullarını göstər:");
        jpnlFromSimpleProduct.add(jLabel1);
        jpnlFromSimpleProduct.add(jpnlSimpleProductSelect);

        jpnlSearch.add(jpnlFromSimpleProduct);

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
        jdtDateFrom.setDate(null);
        jdtDateTo.setDate(null);
        jcbClients.setSelectedItem(null);
        jcbInvoiceTypes.setSelectedItem(null);

        tmInvoiceDetaileds.removeAll();
    }//GEN-LAST:event_jbtnClearActionPerformed

    private void jbtnExcelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnExcelActionPerformed
        try {
            ExcelWriter.viewAsExcelFile(new ObjectTableModel[]{tmInvoiceDetaileds}, new String[]{"Məhsullar"});
        } catch (IOException | WriteException ex) {
            ex.printStackTrace();
        }
    }//GEN-LAST:event_jbtnExcelActionPerformed
    // End of variables declaration//GEN-END:variables
}
