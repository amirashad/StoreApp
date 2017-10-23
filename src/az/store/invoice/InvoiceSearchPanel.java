package az.store.invoice;

import az.store.Inits;
import az.store.types.CodeValue;
import az.util.components.ExcelWriter;
import az.util.components.ObjectTableModel;
import az.util.components.ObjectTableModelEx;
import az.util.utils.Utils;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import javax.swing.JOptionPane;
import jxl.write.WriteException;

/**
 *
 * @author Rashad Amirjanov
 */
public final class InvoiceSearchPanel extends javax.swing.JPanel {

    private InvoiceDialog productionSaleDialog = new InvoiceDialog(null, true);

    private final ObjectTableModelEx tmInvoices = new ObjectTableModelEx(new String[]{
        "Qaimə No", "Tarix", "Müştəri", "Qiyməti", "Satış qiym.", "Gəlir", "Qaimə"}) {

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            Invoice invoice = (Invoice) this.getRowObjects().get(rowIndex);

            if (invoice.getId() == -2) {
                return "";
            } else if (invoice.getId() == -1 && columnIndex == 0) {
                return "";
            }

            Object result = "";

            columnIndex = convertToMyColumn(columnIndex);
            switch (columnIndex) {
                case 0:
                    result = String.valueOf(invoice.getId());
                    break;
                case 1:
                    result = Utils.toStringDate(invoice.getDate());
                    break;
                case 2:
                    result = invoice.getClient().getName();
                    break;
                case 3:
                    result = Utils.toString(invoice.getTotalPriceBuy());
                    break;
                case 4:
                    result = Utils.toString(invoice.getTotalPriceSale());
                    break;
                case 5:
                    result = Utils.toString(invoice.getTotalPriceSale() - invoice.getTotalPriceBuy());
                    break;
                case 6:
                    result = invoice.getInvoiceType();
            }

            if (result == null) {
                result = "";
            }

            return result;
        }
    };

    /**
     * Creates new form InvoiceSearchPanel
     */
    public InvoiceSearchPanel() {
        initComponents();

        refreshInvoiceTypes();
        refreshClients();

        jtbResult.setModel(tmInvoices);
        resetProductionColumns();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jpnlSearchParameters = new javax.swing.JPanel();
        jpnlSearchInvoice = new javax.swing.JPanel();
        jcbInvoiceTypes = new javax.swing.JComboBox();
        jlbClient = new javax.swing.JLabel();
        jcbClients = new javax.swing.JComboBox();
        jLabel3 = new javax.swing.JLabel();
        jdtDateFrom = new az.util.components.JDateChooserEx();
        jLabel4 = new javax.swing.JLabel();
        jdtDateTo = new az.util.components.JDateChooserEx();
        jlbCodeValue1 = new javax.swing.JLabel();
        jtxtInvoiceNumber = new javax.swing.JTextField();
        jpnlButtons = new javax.swing.JPanel();
        jbtnSearch = new javax.swing.JButton();
        jbtnClear = new javax.swing.JButton();
        jscrollPaneProductResult = new javax.swing.JScrollPane();
        jtbResult = new javax.swing.JTable();
        jpnlOperations = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jbtnShowDetailed = new javax.swing.JButton();
        jbtnDeleteInvoice = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jbtnExcel = new javax.swing.JButton();

        setLayout(new java.awt.BorderLayout());

        jpnlSearchParameters.setPreferredSize(new java.awt.Dimension(1041, 80));
        jpnlSearchParameters.setLayout(new javax.swing.BoxLayout(jpnlSearchParameters, javax.swing.BoxLayout.PAGE_AXIS));

        jpnlSearchInvoice.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jpnlSearchInvoice.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jpnlSearchInvoice.add(jcbInvoiceTypes);

        jlbClient.setText("Müştəri:");
        jpnlSearchInvoice.add(jlbClient);

        jcbClients.setPreferredSize(new java.awt.Dimension(80, 20));
        jpnlSearchInvoice.add(jcbClients);

        jLabel3.setText("Tarixdən:");
        jpnlSearchInvoice.add(jLabel3);

        jdtDateFrom.setPreferredSize(new java.awt.Dimension(120, 20));
        jpnlSearchInvoice.add(jdtDateFrom);

        jLabel4.setText("Tarixədək:");
        jpnlSearchInvoice.add(jLabel4);

        jdtDateTo.setPreferredSize(new java.awt.Dimension(120, 20));
        jpnlSearchInvoice.add(jdtDateTo);

        jlbCodeValue1.setText("Qaimə No:");
        jpnlSearchInvoice.add(jlbCodeValue1);

        jtxtInvoiceNumber.setPreferredSize(new java.awt.Dimension(70, 20));
        jpnlSearchInvoice.add(jtxtInvoiceNumber);

        jpnlSearchParameters.add(jpnlSearchInvoice);

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

        jpnlSearchParameters.add(jpnlButtons);

        add(jpnlSearchParameters, java.awt.BorderLayout.NORTH);

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
        jpnlOperations.setPreferredSize(new java.awt.Dimension(1024, 80));
        jpnlOperations.setLayout(new java.awt.GridLayout(1, 0));

        java.awt.FlowLayout flowLayout1 = new java.awt.FlowLayout(java.awt.FlowLayout.LEFT);
        flowLayout1.setAlignOnBaseline(true);
        jPanel1.setLayout(flowLayout1);

        jbtnShowDetailed.setText("Seçilmişə ətraflı bax");
        jbtnShowDetailed.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnShowDetailedActionPerformed(evt);
            }
        });
        jPanel1.add(jbtnShowDetailed);

        jbtnDeleteInvoice.setIcon(new javax.swing.ImageIcon(getClass().getResource("/az/store/images/minus.png"))); // NOI18N
        jbtnDeleteInvoice.setText("Səhv qaiməni sil");
        jbtnDeleteInvoice.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnDeleteInvoiceActionPerformed(evt);
            }
        });
        jPanel1.add(jbtnDeleteInvoice);

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

    private InvoiceType getSelectedInvoiceType() {
        return (InvoiceType) jcbInvoiceTypes.getSelectedItem();
    }

    public void refreshClients() {
        jcbClients.removeAllItems();
        jcbClients.addItem(null);

        if (getSelectedInvoiceType() == null) {
            return;
        }

        ArrayList<CodeValue> codeValuesClients = Inits.getSelectQueries().
                getCodeValues(getSelectedInvoiceType().getClientCodeType());
        for (CodeValue codeValue : codeValuesClients) {
            jcbClients.addItem(codeValue);
        }
    }

    public void refreshInvoiceTypes() {
        jcbInvoiceTypes.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                resetProductionColumns();
                tmInvoices.removeAll();
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

        ArrayList<InvoiceType> invoiceTypes = Inits.getSelectQueries().getAllInvoiceTypes();
        for (InvoiceType invoiceType : invoiceTypes) {
            jcbInvoiceTypes.addItem(invoiceType);
        }
    }

    private void resetProductionColumns() {
        tmInvoices.removeAll();

        boolean isProduction = getSelectedInvoiceType() != null && Inits.isAppTypeProduction() && getSelectedInvoiceType().isProduction();

        if (isProduction) {
            tmInvoices.setColumnsVisible(true, 4, 5);
        } else {
            tmInvoices.setColumnsVisible(false, 4, 5);
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

        Integer invoiceId = null;
        try {
            invoiceId = Integer.parseInt(jtxtInvoiceNumber.getText());
        } catch (NumberFormatException ex) {
        }

        Date dateFrom = jdtDateFrom.getDate();
        Date dateTo = jdtDateTo.getDate();

        tmInvoices.removeAll();

        ArrayList<Invoice> invoices = Inits.getSelectQueries().getInvoices(invoiceId, clientId, false, dateFrom, dateTo, invoiceTypes);
        for (Invoice invoice : invoices) {
            tmInvoices.addObject(invoice);
        }
        {//summing totals
            Invoice invoiceTotal = new Invoice(-1);
            invoiceTotal.setClient(new CodeValue(-1, "Ümumi"));
            invoiceTotal.setDate(null);

            for (Invoice invoice : invoices) {
                invoiceTotal.setTotalPriceSale(invoiceTotal.getTotalPriceSale() + invoice.getTotalPriceSale());
                invoiceTotal.setTotalPriceBuy(invoiceTotal.getTotalPriceBuy() + invoice.getTotalPriceBuy());
            }
            tmInvoices.addObject(new Invoice(-2));
            tmInvoices.addObject(invoiceTotal);
        }
    }

    private void jbtnSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnSearchActionPerformed
        research();
    }//GEN-LAST:event_jbtnSearchActionPerformed

    private void jbtnClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnClearActionPerformed
        jdtDateFrom.setDate(null);
        jdtDateTo.setDate(null);
        jcbClients.setSelectedItem(null);
        jcbInvoiceTypes.setSelectedItem(null);
        jtxtInvoiceNumber.setText(null);

        tmInvoices.removeAll();
    }//GEN-LAST:event_jbtnClearActionPerformed

    private void jbtnExcelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnExcelActionPerformed
        try {
            ExcelWriter.viewAsExcelFile(new ObjectTableModel[]{tmInvoices}, new String[]{"Qaimələr"});
        } catch (IOException | WriteException ex) {
            ex.printStackTrace();
        }
    }//GEN-LAST:event_jbtnExcelActionPerformed

    private void jbtnShowDetailedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnShowDetailedActionPerformed
        int idx = jtbResult.getSelectedRow();
        if (idx == -1) {
            return;
        }
        Invoice invoice = (Invoice) tmInvoices.getObjectAt(idx);
        if (invoice == null || invoice.getId() < 0) {
            return;
        }

        InvoiceType currentType = Inits.getSelectQueries().getInvoiceType(invoice.getInvoiceType().getId());
        productionSaleDialog.showForm(invoice.getInvoiceType().getId(), String.format("Qaimə %d (%s)", invoice.getId(), currentType.getName()), invoice);
    }//GEN-LAST:event_jbtnShowDetailedActionPerformed

    private void jbtnDeleteInvoiceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnDeleteInvoiceActionPerformed
        int idx = jtbResult.getSelectedRow();
        if (idx == -1) {
            return;
        }
        Invoice invoice = (Invoice) tmInvoices.getObjectAt(idx);
        if (invoice == null || invoice.getId() < 0) {
            return;
        }

        int res = JOptionPane.showConfirmDialog(this, "Qaimə silinsinmi?\n" + invoice.getId(),
                "Silinsinmi?", JOptionPane.YES_NO_OPTION);
        if (res != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            Inits.getDb().setAutoCommit(false);
            InvoiceType type = Inits.getSelectQueries().
                    getInvoiceType(invoice.getInvoiceType().getId());
            ArrayList<InvoiceDetailed> detaileds = Inits.getSelectQueries().
                    getInvoiceDetaileds(invoice.getId(), null, null, null, null, null, null, null, null, null, null);
            for (InvoiceDetailed detailed : detaileds) {
                Inits.getUpdateQueries().increaseProductCount(detailed.getProduct().getId(), -1 * type.getSign() * detailed.getCount());
            }
            Inits.getDeleteQueries().deleteInvoiceDetailed(null, invoice.getId());
            Inits.getDeleteQueries().deleteInvoice(invoice.getId());
        } catch (Exception ex) {
            Inits.getDb().rollback();
            JOptionPane.showMessageDialog(null, "Səhv baş verdi:\n" + ex.getMessage());
        } finally {
            Inits.getDb().setAutoCommit(true);
        }
        research();
    }//GEN-LAST:event_jbtnDeleteInvoiceActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JButton jbtnClear;
    private javax.swing.JButton jbtnDeleteInvoice;
    private javax.swing.JButton jbtnExcel;
    private javax.swing.JButton jbtnSearch;
    private javax.swing.JButton jbtnShowDetailed;
    private javax.swing.JComboBox jcbClients;
    private javax.swing.JComboBox jcbInvoiceTypes;
    private az.util.components.JDateChooserEx jdtDateFrom;
    private az.util.components.JDateChooserEx jdtDateTo;
    private javax.swing.JLabel jlbClient;
    private javax.swing.JLabel jlbCodeValue1;
    private javax.swing.JPanel jpnlButtons;
    private javax.swing.JPanel jpnlOperations;
    private javax.swing.JPanel jpnlSearchInvoice;
    private javax.swing.JPanel jpnlSearchParameters;
    private javax.swing.JScrollPane jscrollPaneProductResult;
    private javax.swing.JTable jtbResult;
    private javax.swing.JTextField jtxtInvoiceNumber;
    // End of variables declaration//GEN-END:variables
}
