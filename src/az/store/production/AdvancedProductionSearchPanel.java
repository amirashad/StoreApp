package az.store.production;

import az.store.Inits;
import az.store.invoice.InvoiceDetailed;
import az.store.product.Product;
import az.store.types.CodeType;
import az.store.types.CodeValue;
import az.util.components.ExcelWriter;
import az.util.components.ObjectTableModel;
import az.util.utils.Utils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import javax.swing.JTable;
import jxl.write.WriteException;

/**
 *
 * @author Rashad Amirjanov
 */
public class AdvancedProductionSearchPanel extends javax.swing.JPanel {

    class InvoiceDetailedsTableModel extends ObjectTableModel {

        protected String[] columnNamesPost = new String[]{
            "Cari ist. qiy. tək", "Cəm",
            "Cari satış qiy. tək", "Cəm",
            "Cari mənfəət tək", "Cəm"
        };
        private ArrayList<CodeValue> allExpenditureCodeValues = new ArrayList<>();

        public InvoiceDetailedsTableModel() {
            super(new String[]{"İstehsal məhsulu", "Sayı", "Sadə"});
        }

        @Override
        public Object getValueAt(int rowIndex, int column) {
            Object result = "";

            InvoiceDetailed detailed = (InvoiceDetailed) this.getRowObjects().get(rowIndex);

            if (detailed.getId() == -2) {
                result = "";
            } else if (detailed.getId() == -1 && /*(column == 1 || */ column == 2)/*)*/ {
                result = "";
            } else if (column < columnNames.length) {
                switch (column) {
                    case 0:
                        result = detailed.getProduct().toString();
                        break;
                    case 1:
                        result = detailed.getCount();
                        break;
                    case 2: {
                        result = detailed.getProduct().getProductExpendituresPriceBuy();
                        break;
                    }
                }
            } else if (column < columnNames.length + allExpenditureCodeValues.size()) {//expenditures
                CodeValue codeValue = allExpenditureCodeValues.get(column - columnNames.length);
                double price = 0;

                Expenditure exp = detailed.getProduct().getExpenditureByCodeValue(codeValue);
                if (exp != null) {
                    price = exp.getPrice();
                }
                int count = detailed.getCount();
                if (detailed.getId() == -1) {
                    count = 1;
                }

                boolean isSingle = (column - columnNames.length) % 2 == 0;

                result = Utils.toString(isSingle ? price : price * count);
                if (isSingle && detailed.getId() == -1) {
                    result = null;
                }
            } else {
                switch (column - columnNames.length - allExpenditureCodeValues.size()) {
                    case 0:
                        result = Utils.toString(detailed.getProduct().getPriceBuy());//* detailed.getCount();
                        break;
                    case 1:
                        result = Utils.toString(detailed.getTotalPriceBuy());//detailed.getInvoice() != null ? detailed.getInvoice().getTotalPriceBuy() : 0;//.getPriceBuy();
                        break;
                    case 2:
                        result = Utils.toString(detailed.getProduct().getPriceSale());//* detailed.getCount();
                        break;
                    case 3:
                        result = Utils.toString(detailed.getTotalPriceSale());//detailed.getInvoice() != null ? detailed.getInvoice().getTotalPriceSale() : 0;
                        break;
                    case 4:
                        result = Utils.toString(detailed.getProduct().getPriceSale() - detailed.getProduct().getPriceBuy());// * (detailed.getId() == -1 ? 1 : detailed.getCount()));
                        break;
                    case 5:
                        result = Utils.toString(detailed.getTotalIncome());//detailed.getInvoice() != null ? detailed.getInvoice().getTotalPriceSale() -  detailed.getInvoice().getTotalPriceBuy() : 0;//detailed.getPriceSale() - detailed.getProduct().getPriceBuy();
                        break;
                }

                boolean isSingle = (column - columnNames.length) % 2 == 0;
                if (isSingle && detailed.getId() == -1) {
                    result = null;
                }
            }

            if (result == null) {
                result = "";
            }

            return result;
        }

        @Override
        public int getColumnCount() {
            refreshExpenditures();
            return columnNames.length + allExpenditureCodeValues.size() + columnNamesPost.length;
        }

        public int getColumnCountPre() {
            return columnNames.length;
        }

        public int getColumnCountPost() {
            return columnNamesPost.length;
        }

        @Override
        public String getColumnName(int column) {
            refreshExpenditures();
            if (column < columnNames.length) {
                return columnNames[column];
            } else if (column < columnNames.length + allExpenditureCodeValues.size()) {
                return allExpenditureCodeValues.get(column - columnNames.length).getName();
            } else {
                return columnNamesPost[column - columnNames.length - allExpenditureCodeValues.size()];
            }
        }

        public void refreshExpenditures() {
            if (allExpenditureCodeValues.isEmpty()) {
                ArrayList<CodeValue> codeValues = Inits.getSelectQueries().getCodeValues(CodeType.EXPENDITURES);
//                allExpenditureCodeValues.clear();
                for (int i = 0; i < codeValues.size(); i++) {
                    CodeValue cv = codeValues.get(i);

                    CodeValue cv1 = (CodeValue) cv.clone();
                    CodeValue cv2 = (CodeValue) cv.clone();

//                    cv1.setName("<html><center><b>" + cv.getName() + "</b><br>Tək");
//                    cv2.setName("<html><center><b>" + ""/*cv.getName()*/ + "</b><br>Cəm");
                    cv1.setName(cv.getName());
                    cv2.setName("Cəm");

                    allExpenditureCodeValues.add(cv1);
                    allExpenditureCodeValues.add(cv2);
                }
            }
        }
    }
    private final InvoiceDetailedsTableModel tmInvoiceDetailedsProd = new InvoiceDetailedsTableModel();

    public AdvancedProductionSearchPanel() {
        initComponents();

        jtbResult.setModel(tmInvoiceDetailedsProd);
        jtbResult.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        jpnSelectProductionProduct.init();
        refreshClients();

        jpnSelectProductionProduct.setSelectedProductSourceType(CodeValue.PST_PRODUCTION_PRODUCT);
        jpnSelectProductionProduct.getProductSourceTypesComboBox().setVisible(false);
        jpnSelectProductionProduct.getProductTypesComboBox().setVisible(false);
        jpnSelectProductionProduct.getProductTypesLabel().setVisible(false);
        jpnSelectProductionProduct.getProductsLabel().setText("İstehsal məhsulu");
//        refreshTableColumns();
        jtbResult.getColumnModel().getColumn(jtbResult.getColumnCount() - 3).setMinWidth(100);
        jtbResult.getColumnModel().getColumn(jtbResult.getColumnCount() - 2).setMinWidth(100);
        jtbResult.getColumnModel().getColumn(jtbResult.getColumnCount() - 1).setMinWidth(150);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) - 1);
        jdtDateFrom.setDate(calendar.getTime());
        jdtDateTo.setDate(new Date());
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jpnlSearchProducts = new javax.swing.JPanel();
        jpnlCurrentStore = new javax.swing.JPanel();
        jlbCodeValue = new javax.swing.JLabel();
        jcbClients = new javax.swing.JComboBox();
        jlbCodeValue1 = new javax.swing.JLabel();
        jtxtInvoiceNumber = new javax.swing.JTextField();
        jpnlInvoiceDetailed = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jdtDateFrom = new az.util.components.JDateChooserEx();
        jLabel4 = new javax.swing.JLabel();
        jdtDateTo = new az.util.components.JDateChooserEx();
        jpnSelectProductionProduct = new az.store.product.ProductSelectPanel();
        jPanel3 = new javax.swing.JPanel();
        jbtnSearch = new javax.swing.JButton();
        jbtnClear = new javax.swing.JButton();
        jscrollPaneProductResult = new javax.swing.JScrollPane();
        jtbResult = new javax.swing.JTable();
        jpnlOperations = new javax.swing.JPanel();
        jbtnExcel = new javax.swing.JButton();

        setLayout(new java.awt.BorderLayout());

        jpnlSearchProducts.setLayout(new javax.swing.BoxLayout(jpnlSearchProducts, javax.swing.BoxLayout.PAGE_AXIS));

        jpnlCurrentStore.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jpnlCurrentStore.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jlbCodeValue.setText("Müştəri:");
        jpnlCurrentStore.add(jlbCodeValue);

        jcbClients.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jcbClients.setPreferredSize(new java.awt.Dimension(150, 20));
        jpnlCurrentStore.add(jcbClients);

        jlbCodeValue1.setText("Qaimə No:");
        jpnlCurrentStore.add(jlbCodeValue1);

        jtxtInvoiceNumber.setPreferredSize(new java.awt.Dimension(100, 20));
        jpnlCurrentStore.add(jtxtInvoiceNumber);

        jpnlSearchProducts.add(jpnlCurrentStore);

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
        jpnlInvoiceDetailed.add(jpnSelectProductionProduct);

        jpnlSearchProducts.add(jpnlInvoiceDetailed);

        jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel3.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        jbtnSearch.setIcon(new javax.swing.ImageIcon(getClass().getResource("/az/store/images/Search.png"))); // NOI18N
        jbtnSearch.setText("Axtar");
        jbtnSearch.setPreferredSize(new java.awt.Dimension(90, 25));
        jbtnSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnSearchActionPerformed(evt);
            }
        });
        jPanel3.add(jbtnSearch);

        jbtnClear.setIcon(new javax.swing.ImageIcon(getClass().getResource("/az/store/images/clear.png"))); // NOI18N
        jbtnClear.setText("Təmizlə");
        jbtnClear.setPreferredSize(new java.awt.Dimension(90, 25));
        jbtnClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnClearActionPerformed(evt);
            }
        });
        jPanel3.add(jbtnClear);

        jpnlSearchProducts.add(jPanel3);

        add(jpnlSearchProducts, java.awt.BorderLayout.NORTH);

        jscrollPaneProductResult.setBorder(javax.swing.BorderFactory.createTitledBorder("Nəticə"));

        jtbResult.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jscrollPaneProductResult.setViewportView(jtbResult);

        add(jscrollPaneProductResult, java.awt.BorderLayout.CENTER);

        jpnlOperations.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jpnlOperations.setPreferredSize(new java.awt.Dimension(963, 80));
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

    private void refreshClients() {
        jcbClients.removeAllItems();
        jcbClients.addItem("");
        ArrayList<CodeValue> codeValues = Inits.getSelectQueries().getCodeValues(CodeType.CLIENTS);
        for (CodeValue codeValue : codeValues) {
            jcbClients.addItem(codeValue);
        }
    }

    private void jbtnSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnSearchActionPerformed
        research();
    }//GEN-LAST:event_jbtnSearchActionPerformed

    private void research() {
        tmInvoiceDetailedsProd.removeAll();
//        tmInvoices.removeAll();

        Date dateFrom = jdtDateFrom.getDate();
        Date dateTo = jdtDateTo.getDate();
//        Integer simpleProductId = (jcbProducts.getSelectedIndex() == 0) ? null : ((Product) jcbProducts.getSelectedItem()).getId();
        Integer productionProductId = (jpnSelectProductionProduct.getSelectedProduct() == null) ? null : jpnSelectProductionProduct.getSelectedProduct().getId();
        Integer clientId = (jcbClients.getSelectedIndex() == 0) ? null : ((CodeValue) jcbClients.getSelectedItem()).getId();
        Integer invoiceNumber = null;
        try {
            invoiceNumber = Integer.parseInt(jtxtInvoiceNumber.getText());
        } catch (NumberFormatException ex) {
        }
        ArrayList<InvoiceDetailed> detaileds = Inits.getSelectQueries().getInvoiceDetailedsGroupByProduct(invoiceNumber,
                clientId, Boolean.FALSE, dateFrom, dateTo, null,
                CodeValue.PST_PRODUCTION_PRODUCT, null, productionProductId);
        ArrayList<Product> products = Inits.getSelectQueries().getProducts(null, null, CodeValue.PST_PRODUCTION_PRODUCT);
        for (InvoiceDetailed detailed : detaileds) {
            if (detailed.getProduct() != null && detailed.getProduct().getId() > 0) {
                int idx = products.indexOf(detailed.getProduct());
                if (idx != -1) {
                    detailed.setProduct(products.get(idx));
                    detailed.getProduct().setExpenditures(Inits.getSelectQueries().getProductExpenditures(products.get(idx).getId()));
                }
            }

            tmInvoiceDetailedsProd.addObject(detailed);
        }
        {//totals
            ArrayList<CodeValue> allExpenditures = Inits.getSelectQueries().getCodeValues(CodeType.EXPENDITURES);
            ArrayList<Expenditure> expendituresForTotal = new ArrayList<>();
            for (CodeValue exp : allExpenditures) {
                expendituresForTotal.add(new Expenditure(exp, 0, ""));
//                System.out.println("exp " + exp.getId() + exp.getName());
            }

            InvoiceDetailed invoiceDetailedTotal = new InvoiceDetailed();
            invoiceDetailedTotal.setId(-1);
            invoiceDetailedTotal.setProduct(new Product(-1, "Ümumi", false));
            invoiceDetailedTotal.getProduct().setExpenditures(expendituresForTotal);
//            invoiceDetailedTotal.getProduct().setSimpleProduct(new Product(-1, "", true));
//            invoiceDetailedTotal.setCount(1);
            for (InvoiceDetailed detailed : detaileds) {
//                detailed.getProduction().getTemplate().setExpenditures(Inits.getProductionQueries().getProductionExpenditures(detailed.getProduct().getId()));
                for (Expenditure exp : detailed.getProduct().getExpenditures()) {
                    for (Expenditure expLast : invoiceDetailedTotal.getProduct().getExpenditures()) {
                        if (exp.getObject().equals(expLast.getObject())) {
                            expLast.setPrice(expLast.getPrice() + exp.getPrice() * detailed.getCount());
                            break;
                        }//detailed.getProduct().getPriceBuy() * detailed.getCount()
                    }
                }
                invoiceDetailedTotal.setCount(invoiceDetailedTotal.getCount() + detailed.getCount());
                invoiceDetailedTotal.setTotalPriceBuy(invoiceDetailedTotal.getTotalPriceBuy() + detailed.getTotalPriceBuy());
                invoiceDetailedTotal.setTotalPriceSale(invoiceDetailedTotal.getTotalPriceSale() + detailed.getTotalPriceSale());
//                invoiceDetailedTotal.setPriceSale(invoiceDetailedTotal.getPriceSale() + detailed.getPriceSale() * detailed.getCount());
//                invoiceDetailedTotal.getProduct().setPriceBuy(invoiceDetailedTotal.getProduct().getPriceBuy() + detailed.getProduct().getPriceBuy() * detailed.getCount());
            }
            tmInvoiceDetailedsProd.addObject(new InvoiceDetailed(-2));
            tmInvoiceDetailedsProd.addObject(invoiceDetailedTotal);
        }
    }

    private void jbtnExcelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnExcelActionPerformed
        try {
            ExcelWriter.viewAsExcelFile(new ObjectTableModel[]{tmInvoiceDetailedsProd}, new String[]{"Məhsullar"});
        } catch (IOException | WriteException ex) {
            ex.printStackTrace();
        }
    }//GEN-LAST:event_jbtnExcelActionPerformed

    private void jbtnClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnClearActionPerformed
        jdtDateFrom.setDate(null);
        jdtDateTo.setDate(null);
        jcbClients.setSelectedIndex(0);
        jpnSelectProductionProduct.setSelectedProduct(null);
        jtxtInvoiceNumber.setText("");

        tmInvoiceDetailedsProd.removeAll();
//        tmInvoices.removeAll();
    }//GEN-LAST:event_jbtnClearActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JButton jbtnClear;
    private javax.swing.JButton jbtnExcel;
    private javax.swing.JButton jbtnSearch;
    private javax.swing.JComboBox jcbClients;
    private az.util.components.JDateChooserEx jdtDateFrom;
    private az.util.components.JDateChooserEx jdtDateTo;
    private javax.swing.JLabel jlbCodeValue;
    private javax.swing.JLabel jlbCodeValue1;
    private az.store.product.ProductSelectPanel jpnSelectProductionProduct;
    private javax.swing.JPanel jpnlCurrentStore;
    private javax.swing.JPanel jpnlInvoiceDetailed;
    private javax.swing.JPanel jpnlOperations;
    private javax.swing.JPanel jpnlSearchProducts;
    private javax.swing.JScrollPane jscrollPaneProductResult;
    private javax.swing.JTable jtbResult;
    private javax.swing.JTextField jtxtInvoiceNumber;
    // End of variables declaration//GEN-END:variables

}
