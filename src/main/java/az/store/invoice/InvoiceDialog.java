/*
 * ProductEntryDialog.java
 *
 * Created on Jan 8, 2012, 3:12:29 PM
 */
package az.store.invoice;

import az.store.Inits;
import az.store.product.Product;
import az.store.product.ProductSelectionListener;
import az.store.types.CodeValue;
import az.util.components.ObjectTableModelEx;
import az.util.components.document.JTextFieldOnlyNumber;
import az.util.utils.Utils;

import javax.swing.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Rashad Amirjanov
 */
public class InvoiceDialog extends javax.swing.JDialog {

    private static final String NAME_ADD = "Əlavə et";
    private static final String NAME_EDIT = "Dəyiş";
    private static InvoiceDialog invoiceDialog = null;
    private Invoice currentInvoice = null;
    private InvoiceDetailed currentInvoiceDetailed = null;

    private ObjectTableModelEx tmTemporaryInvoices = new ObjectTableModelEx(
            new String[]{"ID", "Məhsul", "Sayı", "Qiyməti", "Satış qiy.",
                    "Ümumi qiyməti", "Ümumi Satış qiy.", "Ümumi Gəlir"}) {

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {

            InvoiceDetailed tempInvoice = (InvoiceDetailed) this.getRowObjects().get(rowIndex);

            Object result = "";
            columnIndex = convertToMyColumn(columnIndex);
            switch (columnIndex) {
                case 0:
                    result = tempInvoice.getId();
                    break;
                case 1:
                    result = tempInvoice.getProduct().toString();
                    break;
                case 2:
                    result = tempInvoice.getCount() + " " + tempInvoice.getProduct().getMeasureName();
                    break;
                case 3:
                    result = tempInvoice.getPriceBuy();
                    break;
                case 4:
                    result = tempInvoice.getPriceSale();
                    break;
                case 5:
                    result = Utils.toString(tempInvoice.getTotalPriceBuy());
                    break;
                case 6:
                    result = Utils.toString(tempInvoice.getTotalPriceSale());
                    break;
                case 7:
                    result = Utils.toString(tempInvoice.getTotalIncome());
                    break;
            }
            if (result == null) {
                result = "";
            }
            return result;
        }
    };

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel22;
    private javax.swing.JPanel jPanel23;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton jbtnAdd;
    private javax.swing.JButton jbtnCalculateTotal;
    private javax.swing.JButton jbtnClose;
    private javax.swing.JButton jbtnDeleteAll;
    private javax.swing.JButton jbtnDeleteSelected;
    private javax.swing.JButton jbtnEditSelected;
    private javax.swing.JButton jbtnReport;
    private javax.swing.JButton jbtnSave;
    private javax.swing.JComboBox jcbClients;
    private javax.swing.JComboBox jcbInvoiceTypes;
    private az.util.components.JDateChooserEx jdtInvoiceDate;
    private javax.swing.JLabel jlbClient;
    private javax.swing.JLabel jlbCount;
    private javax.swing.JLabel jlbPriceBuy;
    private javax.swing.JLabel jlbPriceSale;
    private javax.swing.JLabel jlbTotalBuyPrice;
    private javax.swing.JLabel jlblMeasure;
    private javax.swing.JPanel jpnlAddDetailed;
    private javax.swing.JPanel jpnlInvoiceDetails;
    private javax.swing.JPanel jpnlPriceSale;
    private az.store.product.ProductSelectPanel jpnlProductSelect;
    private javax.swing.JPanel jpnlSumPriceIncome;
    private javax.swing.JPanel jpnlSumPriceSale;
    private javax.swing.JTable jtblResult;
    private javax.swing.JTextField jtxtCount;
    private javax.swing.JTextField jtxtCountInStore;
    private javax.swing.JTextField jtxtInvoiceId;
    private javax.swing.JTextField jtxtPriceBuy;
    private javax.swing.JTextField jtxtPriceSale;
    private javax.swing.JTextField jtxtSumIncome;
    private javax.swing.JTextField jtxtSumPriceSale;
    private javax.swing.JTextField jtxtSumProductionPrice;

    /**
     * Creates new form ProductEntryDialog
     */
    public InvoiceDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();

        setLocationRelativeTo(null);
        initTableSettings();

        jcbInvoiceTypes.setVisible(false);
        jtxtCount.setDocument(new JTextFieldOnlyNumber());
        jtxtPriceBuy.setDocument(new JTextFieldOnlyNumber());
        jtxtPriceSale.setDocument(new JTextFieldOnlyNumber());

        jtxtInvoiceId.setEditable(false);

        jcbInvoiceTypes.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                resetProductionColumns();
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    InvoiceType invoiceType = (InvoiceType) jcbInvoiceTypes.getSelectedItem();
                    if (invoiceType == null) {
                        return;
                    }

                    jlbClient.setText(invoiceType.getClientLabelName());
                    jlbTotalBuyPrice.setText(jlbPriceBuy.getText());
                    jtxtPriceBuy.setEditable(invoiceType.getSign() > 0);
                    jpnlPriceSale.setVisible(invoiceType.isProduction());
                    jpnlSumPriceIncome.setVisible(invoiceType.isProduction());

                    refreshClients();
                    refreshInvoice(null);

                    jpnlProductSelect.setSelectedProductSourceType(getSelectedInvoiceType().isProduction()
                            ? CodeValue.PST_PRODUCTION_PRODUCT : CodeValue.PST_SIMPLE_PRODUCT);
                }
            }
        });

        jpnlProductSelect.addProductSelectionListener(new ProductSelectionListener() {
            @Override
            public void productSelected() {
                jtxtPriceBuy.setText(null);
                jtxtPriceSale.setText(null);
                jtxtCountInStore.setText(null);
                jlblMeasure.setText(null);

                if (jpnlProductSelect.getSelectedProduct() == null) {
                    return;
                }

                Product product = jpnlProductSelect.getSelectedProduct();
                ArrayList<Product> products = Inits.getSelectQueries().getProducts(product.getId(), null, null);
                if (products.size() == 1) {
                    product = products.get(0);
                }

                jtxtPriceBuy.setText(Utils.toString(product.getPriceBuy()));
                jtxtPriceSale.setText(Utils.toString(product.getPriceSale()));
                jtxtCountInStore.setText(Utils.toString(product.getCount()));
                jlblMeasure.setText(product.getMeasureName());

                setInvoiceDetailedEditing(false);
            }

            @Override
            public void productSourceTypeChanged() {
            }

            @Override
            public void productTypeChanged() {
            }
        });

        jpnlProductSelect.init();
        jpnlProductSelect.getProductSourceTypesComboBox().setVisible(false);
//        jtxtInvoiceId.setText(Initialization.getProductionQueries().getMaxInvoiceNumber());

        jbtnEditSelected.setVisible(false);
    }

    public static InvoiceDialog instance() {
        if (invoiceDialog == null) {
            invoiceDialog = new InvoiceDialog(null, true);
        }
        return invoiceDialog;
    }

    public void initTableSettings() {
        jtblResult.setModel(tmTemporaryInvoices);
        jtblResult.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jtblResult.setRowSelectionAllowed(true);
    }

    /**
     * @param invoiceType
     * @param title
     * @param invoice     - show already created invoice by its id
     */
    public void showForm(int invoiceType, String title, Invoice invoice) {
        this.setTitle(title);

        clearTextFields();
        refreshInvoiceTypes();
        jcbInvoiceTypes.setSelectedItem(new InvoiceType(invoiceType));

        refreshInvoice(invoice);

        this.setVisible(true);
    }

    private InvoiceType getSelectedInvoiceType() {
        return (InvoiceType) jcbInvoiceTypes.getSelectedItem();
    }

    private void resetProductionColumns() {
        tmTemporaryInvoices.removeAll();

        boolean isProduction = getSelectedInvoiceType() != null && Inits.isAppTypeProduction() && getSelectedInvoiceType().isProduction();

        if (isProduction) {
            tmTemporaryInvoices.setColumnsVisible(true, 4, 6, 7);
        } else {
            tmTemporaryInvoices.setColumnsVisible(false, 4, 6, 7);
        }
    }

    private void refreshInvoiceTypes() {
        jcbInvoiceTypes.removeAllItems();
        ArrayList<InvoiceType> invoiceTypes = Inits.getSelectQueries().getAllInvoiceTypes();
        for (InvoiceType it : invoiceTypes) {
            jcbInvoiceTypes.addItem(it);
        }
    }

    private void refreshClients() {
        jcbClients.removeAllItems();
        ArrayList<CodeValue> codeValues = Inits.getSelectQueries().getCodeValues(getSelectedInvoiceType().getClientCodeType());
        for (CodeValue codeValue : codeValues) {
            jcbClients.addItem(codeValue);
        }
//        if (jcbClients.getItemCount() > 0) {
//            jcbClients.setSelectedIndex(0);
//        }
    }

    private void refreshInvoice(Invoice invoice) {
        tmTemporaryInvoices.removeAll();

        Boolean temp = null;
        Integer invoiceId = null;

        if (invoice == null) {//new invoice
            temp = true;
            invoiceId = null;
        } else {
            invoiceId = invoice.getId();
            setInvoiceEditable(false);
        }

        jbtnEditSelected.setVisible(false);

        List<InvoiceType> invoiceTypes = Collections.singletonList((InvoiceType) jcbInvoiceTypes.getSelectedItem());
        ArrayList<Invoice> invoices = Inits.getSelectQueries().getInvoices(invoiceId, null, temp, null, null, invoiceTypes);
        if (invoices.size() > 0) {
            currentInvoice = invoices.get(0);

            jtxtInvoiceId.setText(String.valueOf(currentInvoice.getId()));
            jdtInvoiceDate.setDate(currentInvoice.getDate());
            jcbClients.setSelectedItem(currentInvoice.getClient());

            ArrayList<InvoiceDetailed> detaileds = Inits.getSelectQueries().getInvoiceDetaileds(currentInvoice.getId(), null, null, null, null, null, null, null, null, null, null);
            for (InvoiceDetailed detailed : detaileds) {
                tmTemporaryInvoices.addObject(detailed);
            }
            jcbClients.setEnabled(detaileds.isEmpty());
        } else {
            clearTextFields();
            currentInvoice = null;
            jtxtInvoiceId.setText(null);
            jdtInvoiceDate.setDate(null);
            jcbClients.setSelectedItem(null);
            jcbClients.setEnabled(true);
        }

        calculateSums();
    }

    private void clearTextFields() {
        jtxtCount.setText("");
        jtxtCountInStore.setText("");
        jlblMeasure.setText("");
        jtxtPriceBuy.setText("");
        jtxtPriceSale.setText("");
        jtxtSumProductionPrice.setText(null);
        jtxtSumPriceSale.setText(null);
        jtxtSumIncome.setText(null);

        setInvoiceDetailedEditing(false);
    }

    private void setInvoiceEditable(boolean editable) {
        jcbInvoiceTypes.setEnabled(editable);
        jpnlProductSelect.setVisible(editable);
        jpnlAddDetailed.setVisible(editable);
        jbtnDeleteSelected.setEnabled(editable);
        jbtnDeleteAll.setEnabled(editable);
        jbtnSave.setEnabled(editable);
        jdtInvoiceDate.setEnabled(editable);
        if (currentInvoice != null && !currentInvoice.isTemp()) {
            jbtnDeleteAll.setEnabled(false);
        }
    }

    private boolean isInvoiceDetailedEditing() {
        return jbtnAdd.getText().equals(NAME_EDIT) && (currentInvoiceDetailed != null);
    }

    private void setInvoiceDetailedEditing(boolean editingMode) {
        if (editingMode) {
            jbtnAdd.setText(NAME_EDIT);
            jbtnAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/az/store/images/edit.png"))); // NOI18N
        } else {
            jtxtCount.setText("");
            jbtnAdd.setText(NAME_ADD);
            jbtnAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/az/store/images/add.png"))); // NOI18N
            currentInvoiceDetailed = null;
        }
    }

    private void calculateSums() {
        try {
            jtxtSumProductionPrice.setText(null);
            jtxtSumPriceSale.setText(null);
            jtxtSumIncome.setText(null);

            if (currentInvoice != null) {
                Double[] prices = Inits.getSelectQueries().getSumPricesOfInvoice(currentInvoice.getId());

                jtxtSumProductionPrice.setText(Utils.toString(prices[0]));
                jtxtSumPriceSale.setText(Utils.toString(prices[1]));
                jtxtSumIncome.setText(Utils.toString(prices[1] - prices[0]));
            }
        } catch (Exception ex) {
//            JOptionPane.showMessageDialog(null, ex.getMessage(), "Səhv", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
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

        jPanel3 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jpnlInvoiceDetails = new javax.swing.JPanel();
        jcbInvoiceTypes = new javax.swing.JComboBox();
        jPanel6 = new javax.swing.JPanel();
        jtxtInvoiceId = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jdtInvoiceDate = new az.util.components.JDateChooserEx();
        jPanel8 = new javax.swing.JPanel();
        jlbClient = new javax.swing.JLabel();
        jcbClients = new javax.swing.JComboBox();
        jpnlAddDetailed = new javax.swing.JPanel();
        jpnlProductSelect = new az.store.product.ProductSelectPanel();
        jPanel13 = new javax.swing.JPanel();
        jlbPriceBuy = new javax.swing.JLabel();
        jtxtPriceBuy = new javax.swing.JTextField();
        jpnlPriceSale = new javax.swing.JPanel();
        jlbPriceSale = new javax.swing.JLabel();
        jtxtPriceSale = new javax.swing.JTextField();
        jPanel15 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jtxtCount = new javax.swing.JTextField();
        jlblMeasure = new javax.swing.JLabel();
        jbtnAdd = new javax.swing.JButton();
        jPanel19 = new javax.swing.JPanel();
        jPanel16 = new javax.swing.JPanel();
        jlbCount = new javax.swing.JLabel();
        jtxtCountInStore = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jtblResult = new javax.swing.JTable();
        jPanel5 = new javax.swing.JPanel();
        jPanel18 = new javax.swing.JPanel();
        jPanel23 = new javax.swing.JPanel();
        jPanel20 = new javax.swing.JPanel();
        jtxtSumProductionPrice = new javax.swing.JTextField();
        jlbTotalBuyPrice = new javax.swing.JLabel();
        jpnlSumPriceSale = new javax.swing.JPanel();
        jtxtSumPriceSale = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jbtnCalculateTotal = new javax.swing.JButton();
        jpnlSumPriceIncome = new javax.swing.JPanel();
        jtxtSumIncome = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jPanel22 = new javax.swing.JPanel();
        jbtnSave = new javax.swing.JButton();
        jbtnDeleteAll = new javax.swing.JButton();
        jbtnDeleteSelected = new javax.swing.JButton();
        jbtnClose = new javax.swing.JButton();
        jbtnReport = new javax.swing.JButton();
        jbtnEditSelected = new javax.swing.JButton();

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
                jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
                jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 100, Short.MAX_VALUE)
        );

        setMinimumSize(new java.awt.Dimension(1000, 367));

        jPanel4.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jpnlInvoiceDetails.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jpnlInvoiceDetails.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 10, 5));

        jpnlInvoiceDetails.add(jcbInvoiceTypes);

        jPanel6.setPreferredSize(new java.awt.Dimension(200, 20));
        jPanel6.setLayout(new java.awt.BorderLayout(10, 5));
        jPanel6.add(jtxtInvoiceId, java.awt.BorderLayout.CENTER);

        jLabel1.setText("Qaimənin nömrəsi");
        jPanel6.add(jLabel1, java.awt.BorderLayout.LINE_START);

        jpnlInvoiceDetails.add(jPanel6);

        jPanel7.setPreferredSize(new java.awt.Dimension(150, 20));
        jPanel7.setLayout(new java.awt.BorderLayout(10, 5));

        jLabel5.setText("Tarix");
        jPanel7.add(jLabel5, java.awt.BorderLayout.WEST);
        jPanel7.add(jdtInvoiceDate, java.awt.BorderLayout.CENTER);

        jpnlInvoiceDetails.add(jPanel7);

        jPanel8.setPreferredSize(new java.awt.Dimension(300, 20));
        jPanel8.setLayout(new java.awt.BorderLayout(10, 5));

        jlbClient.setText("Müştəri");
        jPanel8.add(jlbClient, java.awt.BorderLayout.WEST);

        jPanel8.add(jcbClients, java.awt.BorderLayout.CENTER);

        jpnlInvoiceDetails.add(jPanel8);

        jpnlAddDetailed.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jpnlAddDetailed.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 10, 5));
        jpnlAddDetailed.add(jpnlProductSelect);

        jPanel13.setPreferredSize(new java.awt.Dimension(200, 20));
        jPanel13.setLayout(new java.awt.BorderLayout(10, 5));

        jlbPriceBuy.setText("Qiyməti");
        jPanel13.add(jlbPriceBuy, java.awt.BorderLayout.WEST);
        jPanel13.add(jtxtPriceBuy, java.awt.BorderLayout.CENTER);

        jpnlAddDetailed.add(jPanel13);

        jpnlPriceSale.setPreferredSize(new java.awt.Dimension(200, 20));
        jpnlPriceSale.setLayout(new java.awt.BorderLayout(10, 5));

        jlbPriceSale.setText("Satış qiyməti");
        jpnlPriceSale.add(jlbPriceSale, java.awt.BorderLayout.WEST);
        jpnlPriceSale.add(jtxtPriceSale, java.awt.BorderLayout.CENTER);

        jpnlAddDetailed.add(jpnlPriceSale);

        jPanel15.setPreferredSize(new java.awt.Dimension(100, 20));
        jPanel15.setLayout(new java.awt.BorderLayout(10, 5));

        jLabel10.setText("Sayı");
        jPanel15.add(jLabel10, java.awt.BorderLayout.WEST);
        jPanel15.add(jtxtCount, java.awt.BorderLayout.CENTER);

        jpnlAddDetailed.add(jPanel15);

        jlblMeasure.setText("vahid");
        jpnlAddDetailed.add(jlblMeasure);

        jbtnAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/az/store/images/add.png"))); // NOI18N
        jbtnAdd.setText("Əlavə et");
        jbtnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnAddActionPerformed(evt);
            }
        });
        jpnlAddDetailed.add(jbtnAdd);

        jPanel19.setPreferredSize(new java.awt.Dimension(30, 20));

        javax.swing.GroupLayout jPanel19Layout = new javax.swing.GroupLayout(jPanel19);
        jPanel19.setLayout(jPanel19Layout);
        jPanel19Layout.setHorizontalGroup(
                jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 30, Short.MAX_VALUE)
        );
        jPanel19Layout.setVerticalGroup(
                jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 20, Short.MAX_VALUE)
        );

        jpnlAddDetailed.add(jPanel19);

        jPanel16.setPreferredSize(new java.awt.Dimension(150, 20));
        jPanel16.setLayout(new java.awt.BorderLayout(10, 5));

        jlbCount.setForeground(java.awt.Color.red);
        jlbCount.setText("Anbardakı sayı");
        jPanel16.add(jlbCount, java.awt.BorderLayout.WEST);

        jtxtCountInStore.setEditable(false);
        jPanel16.add(jtxtCountInStore, java.awt.BorderLayout.CENTER);

        jpnlAddDetailed.add(jPanel16);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
                jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jpnlInvoiceDetails, javax.swing.GroupLayout.DEFAULT_SIZE, 999, Short.MAX_VALUE)
                        .addComponent(jpnlAddDetailed, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
                jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(jpnlInvoiceDetails, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jpnlAddDetailed, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        getContentPane().add(jPanel4, java.awt.BorderLayout.PAGE_START);

        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel2.setLayout(new java.awt.BorderLayout());

        jtblResult.setModel(new javax.swing.table.DefaultTableModel(
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
        jtblResult.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jtblResultMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jtblResult);

        jPanel2.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        getContentPane().add(jPanel2, java.awt.BorderLayout.CENTER);

        jPanel5.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel5.setPreferredSize(new java.awt.Dimension(1029, 100));
        jPanel5.setLayout(new java.awt.BorderLayout());

        jPanel18.setPreferredSize(new java.awt.Dimension(400, 84));

        jPanel23.setBorder(javax.swing.BorderFactory.createTitledBorder("Ümumi"));
        jPanel23.setPreferredSize(new java.awt.Dimension(440, 107));

        jPanel20.setPreferredSize(new java.awt.Dimension(200, 20));
        jPanel20.setLayout(new java.awt.BorderLayout(18, 5));

        jtxtSumProductionPrice.setEditable(false);
        jPanel20.add(jtxtSumProductionPrice, java.awt.BorderLayout.CENTER);

        jlbTotalBuyPrice.setText("Qiyməti");
        jPanel20.add(jlbTotalBuyPrice, java.awt.BorderLayout.LINE_START);

        jpnlSumPriceSale.setPreferredSize(new java.awt.Dimension(200, 20));
        jpnlSumPriceSale.setLayout(new java.awt.BorderLayout(10, 5));

        jtxtSumPriceSale.setEditable(false);
        jpnlSumPriceSale.add(jtxtSumPriceSale, java.awt.BorderLayout.CENTER);

        jLabel12.setText("Satış qiyməti     ");
        jpnlSumPriceSale.add(jLabel12, java.awt.BorderLayout.LINE_START);

        jbtnCalculateTotal.setIcon(new javax.swing.ImageIcon(getClass().getResource("/az/store/images/calculate.png"))); // NOI18N
        jbtnCalculateTotal.setText("Hesabla");
        jbtnCalculateTotal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnCalculateTotalActionPerformed(evt);
            }
        });

        jpnlSumPriceIncome.setPreferredSize(new java.awt.Dimension(200, 20));
        jpnlSumPriceIncome.setLayout(new java.awt.BorderLayout(10, 5));

        jtxtSumIncome.setEditable(false);
        jpnlSumPriceIncome.add(jtxtSumIncome, java.awt.BorderLayout.CENTER);

        jLabel13.setText("Gəlir                  ");
        jpnlSumPriceIncome.add(jLabel13, java.awt.BorderLayout.LINE_START);

        javax.swing.GroupLayout jPanel23Layout = new javax.swing.GroupLayout(jPanel23);
        jPanel23.setLayout(jPanel23Layout);
        jPanel23Layout.setHorizontalGroup(
                jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel23Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jPanel20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jpnlSumPriceIncome, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jpnlSumPriceSale, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 10, Short.MAX_VALUE)
                                .addComponent(jbtnCalculateTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap())
        );
        jPanel23Layout.setVerticalGroup(
                jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel23Layout.createSequentialGroup()
                                .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addGroup(jPanel23Layout.createSequentialGroup()
                                                .addGap(0, 0, Short.MAX_VALUE)
                                                .addComponent(jbtnCalculateTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(jPanel23Layout.createSequentialGroup()
                                                .addComponent(jPanel20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(jpnlSumPriceSale, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(5, 5, 5)
                                                .addComponent(jpnlSumPriceIncome, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel18Layout = new javax.swing.GroupLayout(jPanel18);
        jPanel18.setLayout(jPanel18Layout);
        jPanel18Layout.setHorizontalGroup(
                jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel18Layout.createSequentialGroup()
                                .addComponent(jPanel23, javax.swing.GroupLayout.PREFERRED_SIZE, 361, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 39, Short.MAX_VALUE))
        );
        jPanel18Layout.setVerticalGroup(
                jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jPanel23, javax.swing.GroupLayout.DEFAULT_SIZE, 105, Short.MAX_VALUE)
        );

        jPanel5.add(jPanel18, java.awt.BorderLayout.WEST);

        jPanel22.setPreferredSize(new java.awt.Dimension(531, 96));

        jbtnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/az/store/images/save.png"))); // NOI18N
        jbtnSave.setText("Yadda saxla");
        jbtnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnSaveActionPerformed(evt);
            }
        });

        jbtnDeleteAll.setIcon(new javax.swing.ImageIcon(getClass().getResource("/az/store/images/DeleteRed.png"))); // NOI18N
        jbtnDeleteAll.setText("Hamısını sil");
        jbtnDeleteAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnDeleteAllActionPerformed(evt);
            }
        });

        jbtnDeleteSelected.setIcon(new javax.swing.ImageIcon(getClass().getResource("/az/store/images/minus.png"))); // NOI18N
        jbtnDeleteSelected.setText("Seçilmişi sil");
        jbtnDeleteSelected.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnDeleteSelectedActionPerformed(evt);
            }
        });

        jbtnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/az/store/images/close.png"))); // NOI18N
        jbtnClose.setText("Bağla");
        jbtnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnCloseActionPerformed(evt);
            }
        });

        jbtnReport.setIcon(new javax.swing.ImageIcon(getClass().getResource("/az/store/images/print.png"))); // NOI18N
        jbtnReport.setText("Çapa ver");
        jbtnReport.setPreferredSize(new java.awt.Dimension(110, 25));
        jbtnReport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnReportActionPerformed(evt);
            }
        });

        jbtnEditSelected.setText("Seçilmişi dəyiş");
        jbtnEditSelected.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnEditSelectedActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel22Layout = new javax.swing.GroupLayout(jPanel22);
        jPanel22.setLayout(jPanel22Layout);
        jPanel22Layout.setHorizontalGroup(
                jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel22Layout.createSequentialGroup()
                                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel22Layout.createSequentialGroup()
                                                .addComponent(jbtnDeleteSelected)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jbtnDeleteAll))
                                        .addComponent(jbtnEditSelected, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(162, 162, 162)
                                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel22Layout.createSequentialGroup()
                                                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                        .addComponent(jbtnClose, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(jbtnSave, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                                .addGap(153, 153, 153))
                                        .addGroup(jPanel22Layout.createSequentialGroup()
                                                .addComponent(jbtnReport, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        jPanel22Layout.setVerticalGroup(
                jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel22Layout.createSequentialGroup()
                                .addGap(5, 5, 5)
                                .addComponent(jbtnReport, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jbtnSave)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jbtnClose)
                                .addGap(0, 4, Short.MAX_VALUE))
                        .addGroup(jPanel22Layout.createSequentialGroup()
                                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jbtnDeleteSelected)
                                        .addComponent(jbtnDeleteAll))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jbtnEditSelected)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel5.add(jPanel22, java.awt.BorderLayout.CENTER);

        getContentPane().add(jPanel5, java.awt.BorderLayout.PAGE_END);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jbtnReportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnReportActionPerformed
        if (check() == false) {
            return;
        }

        //umumi summani hesabla temp_nakladnoyda
        Double[] results = null;
        try {
            results = Inits.getSelectQueries().getSumPricesOfInvoice(currentInvoice.getId());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Sehv", JOptionPane.ERROR_MESSAGE);
        }
        if (results == null) {
            JOptionPane.showMessageDialog(null, "Ümumi summanı hesablaya bilmədi!");
            return;
        }

        //nakladnoya insert ela id sini gotur
        Invoice invoice = new Invoice();
        invoice.setId(currentInvoice.getId());
        invoice.setDate(currentInvoice.getDate());
        invoice.setClient(currentInvoice.getClient());
        invoice.setTotalPriceBuy(results[0]);
        invoice.setTotalPriceSale(results[1]);

        invoice.setDetaileds(new ArrayList<InvoiceDetailed>());
        for (Object obj : tmTemporaryInvoices.getRowObjects()) {
            InvoiceDetailed tempInvoice = (InvoiceDetailed) obj;
            invoice.getDetaileds().add(tempInvoice);
        }

        new InvoiceReport().createReport(invoice);
    }//GEN-LAST:event_jbtnReportActionPerformed

    private void jbtnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnCloseActionPerformed
        this.setVisible(false);
    }//GEN-LAST:event_jbtnCloseActionPerformed

    private void jbtnDeleteSelectedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnDeleteSelectedActionPerformed
        int rowIdx = jtblResult.getSelectedRow();
        if (rowIdx == -1) {
            JOptionPane.showMessageDialog(null, "Seçim edin!", "Diqqət", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        try {
            InvoiceDetailed tempInvoice = (InvoiceDetailed) tmTemporaryInvoices.getObjectAt(jtblResult.convertRowIndexToModel(rowIdx));
            String[] choices = {"Bəli", "Xeyr"};
            int response = JOptionPane.showOptionDialog(null, "Seçilmiş silinsinmi: " + tempInvoice.getId() + "?", "Diqqət", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, choices, "Xeyr");
            if (response != JOptionPane.YES_OPTION) {
                return;
            }

            Inits.getDeleteQueries().deleteInvoiceDetailed(tempInvoice.getId(), null);
            if (!currentInvoice.isTemp()) {
                Inits.getUpdateQueries().increaseProductCount(tempInvoice.getProduct().getId(), -getSelectedInvoiceType().getSign() * tempInvoice.getCount());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return;
        }

        refreshInvoice(null);
        setInvoiceDetailedEditing(false);
    }//GEN-LAST:event_jbtnDeleteSelectedActionPerformed

    private void jbtnDeleteAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnDeleteAllActionPerformed
        if (tmTemporaryInvoices.getRowCount() == 0) {
            JOptionPane.showMessageDialog(null, "Cədvəldə element yoxdur!");
            return;
        }

        String[] choices = {"Bəli", "Xeyr"};
        int response = JOptionPane.showOptionDialog(null, "Həqiqətən hamısı silinsinmi?", "Diqqət", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, choices, "Xeyr");
        if (response != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            Inits.getDeleteQueries().deleteInvoiceDetailed(null, currentInvoice.getId());
            if (!currentInvoice.isTemp()) {
                for (int i = 0; i < tmTemporaryInvoices.getRowCount(); i++) {
                    InvoiceDetailed invoiceDetailed = (InvoiceDetailed) tmTemporaryInvoices.getObjectAt(i);
                    Inits.getUpdateQueries().increaseProductCount(invoiceDetailed.getProduct().getId(),
                            -getSelectedInvoiceType().getSign() * invoiceDetailed.getCount());
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return;
        }

        refreshInvoice(null);
        setInvoiceDetailedEditing(false);
    }//GEN-LAST:event_jbtnDeleteAllActionPerformed

    private void jbtnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnSaveActionPerformed
        if (check() == false) {
            return;
        }

        String[] choices = {"Bəli", "Xeyr"};
        int response = JOptionPane.showOptionDialog(null, "Yadda saxlanılsınmı?", "Diqqət", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, choices, "Xeyr");
        if (response == JOptionPane.NO_OPTION) {
            return;
        }

        //umumi summani hesabla temp_nakladnoyda
        Double[] results = null;
        try {
            results = Inits.getSelectQueries().getSumPricesOfInvoice(currentInvoice.getId());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Sehv", JOptionPane.ERROR_MESSAGE);
        }
        if (results == null) {
            JOptionPane.showMessageDialog(null, "Ümumi summanı hesablaya bilmədi!");
            return;
        }

        try {
            Inits.getDb().getConnection().setAutoCommit(false);

            if (currentInvoice.isTemp()) {//update products count, prices
                int sign = getSelectedInvoiceType().getSign();

                for (Object obj : tmTemporaryInvoices.getRowObjects()) {
                    InvoiceDetailed invoiceDetailed = (InvoiceDetailed) obj;

                    if (invoiceDetailed.getCount() > 0) {
                        ArrayList<Product> products = Inits.getSelectQueries().getProducts(invoiceDetailed.getProduct().getId(), null, null);
                        if (products.size() > 0 && products.get(0) != null) {
                            Product product = products.get(0);
                            Inits.getUpdateQueries().updateProductPrice(product.getId(), invoiceDetailed.getPriceBuy(), invoiceDetailed.getPriceSale());
                            if (-sign * invoiceDetailed.getCount() <= product.getCount()) {
                                Inits.getUpdateQueries().increaseProductCount(product.getId(), sign * invoiceDetailed.getCount());
                            } else {
//                                JOptionPane.showMessageDialog(null, "Seçilmiş məhsul sayı istehsal olunmuş saydan çoxdur!");
                                throw new Exception("Seçilmiş məhsul sayı istehsal olunmuş saydan çoxdur!");
                            }
                        } else {
//                            JOptionPane.showMessageDialog(null, "İstehsal olunmuş məhsullardan məhsulun məlumatları alına bilmədi!");
                            throw new Exception("İstehsal olunmuş məhsullardan məhsulun məlumatları alına bilmədi!");
                        }
                    }
                }
            }

            currentInvoice.setDate(jdtInvoiceDate.getDate());
            currentInvoice.setTotalPriceBuy(results[0]);
            currentInvoice.setTotalPriceSale(results[1]);
            currentInvoice.setTemp(false);

            Inits.getUpdateQueries().updateInvoice(currentInvoice);

            Inits.getDb().getConnection().commit();
        } catch (Exception ex) {
            Inits.getDb().rollback();
            JOptionPane.showMessageDialog(null, ex.getMessage());
            ex.printStackTrace();
        } finally {
            Inits.getDb().setAutoCommit(true);
            currentInvoice = null;
            jtxtInvoiceId.setText("");
        }

        refreshInvoice(null);

//        jcbProductsItemStateChanged(null);
        jpnlProductSelect.setSelectedProduct(null);

        clearTextFields();
        this.setVisible(false);
    }//GEN-LAST:event_jbtnSaveActionPerformed

    private void jbtnCalculateTotalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnCalculateTotalActionPerformed
        calculateSums();
    }//GEN-LAST:event_jbtnCalculateTotalActionPerformed

    private void jbtnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnAddActionPerformed
        if (jcbClients.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(null, "Seçilməyib: " + jlbClient.getText());
            jcbClients.requestFocus();
            return;
        }

        if (jpnlProductSelect.getSelectedProduct() == null) {
            JOptionPane.showMessageDialog(null, "Seçilməyib: " + "Məhsul");
            jpnlProductSelect.requestFocus();
            return;
        }

        if (!Utils.isPositiveNumber(jtxtCount.getText())) {
            JOptionPane.showMessageDialog(null, "Seçilməyib: " + jlbCount.getText());
            jtxtCount.requestFocus();
            return;
        }

        if (jtxtPriceBuy.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Daxil edilməyib: " + jlbPriceBuy.getText());
            jtxtPriceBuy.requestFocus();
            return;
        }

        if (jtxtPriceSale.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Daxil edilməyib: " + jlbPriceSale.getText());
            jtxtPriceSale.requestFocus();
            return;
        }

        Product selProduct = jpnlProductSelect.getSelectedProduct();
        int sign = getSelectedInvoiceType().getSign();
        int selCount = Integer.parseInt(jtxtCount.getText()) * sign;
        int selCountInStore = Integer.parseInt(jtxtCountInStore.getText());
        double productCountInInvoice = (currentInvoice == null) ? 0d : sign * Inits.getSelectQueries().getProductCountInInvoice(selProduct.getId(), currentInvoice.getId());
//selCount: -2
//selCountInStore: 1
//productCountInInvoice: -1.0
//productCountInInvoice2: 0.0
//lastCount: -2.0

//selCount: -2
//selCountInStore: 1
//productCountInInvoice: -1.0
//productCountInInvoice2: -1.0
//lastCount: -3.0
//        System.out.println("selCount: " + selCount);
//        System.out.println("selCountInStore: " + selCountInStore);
//        System.out.println("productCountInInvoice: " + productCountInInvoice);
        if (isInvoiceDetailedEditing()) {
            if (currentInvoice != null && !currentInvoice.isTemp()) {
                productCountInInvoice = 0;
            }
            productCountInInvoice -= sign * currentInvoiceDetailed.getCount();
        }

//        System.out.println("productCountInInvoice2: " + productCountInInvoice);
//        System.out.println("lastCount: " + (selCount + productCountInInvoice));
        if (selCountInStore + selCount + productCountInInvoice < 0) {
            JOptionPane.showMessageDialog(null, "Daxil etdiyiniz say anbarda olandan çoxdur: " + Math.abs(selCount + productCountInInvoice) + " > " + selCountInStore);
            return;
        }

        try {
            Inits.getDb().getConnection().setAutoCommit(false);

            //Initiate Invoice: if null create, else update date, client, ...
            if (tmTemporaryInvoices.getRowCount() == 0) {
                if (currentInvoice == null) {
                    currentInvoice = new Invoice();
                    currentInvoice.setClient((CodeValue) jcbClients.getSelectedItem());
                    currentInvoice.setDate(jdtInvoiceDate.getDate());
                    currentInvoice.setInvoiceType((InvoiceType) jcbInvoiceTypes.getSelectedItem());
                    currentInvoice.setId(Inits.getInsertQueries().insertInvoice(currentInvoice));
                    currentInvoice.setTemp(true);
                } else {
                    currentInvoice.setClient((CodeValue) jcbClients.getSelectedItem());
                    currentInvoice.setInvoiceType((InvoiceType) jcbInvoiceTypes.getSelectedItem());
                    currentInvoice.setDate(jdtInvoiceDate.getDate());
//                    dialogInvoice.setTemp(true);
                    Inits.getUpdateQueries().updateInvoice(currentInvoice);
                }
            }

            InvoiceDetailed invoiceDetailed = new InvoiceDetailed();
            invoiceDetailed.setCount(Math.abs(selCount));
            invoiceDetailed.setPriceBuy(Double.parseDouble(jtxtPriceBuy.getText()));
            invoiceDetailed.setPriceSale(Double.parseDouble(jtxtPriceSale.getText()));
            invoiceDetailed.setProduct(jpnlProductSelect.getSelectedProduct());

            if (!isInvoiceDetailedEditing()) {//insert invoicedetailed
                Inits.getInsertQueries().insertInvoiceDetailed(currentInvoice.getId(), invoiceDetailed);
//                if(!currentInvoice.isTemp()){
//                    Inits.getUpdateQueries().increaseProductCount(invoiceDetailed.getProduct().getId(),
//                        sign * invoiceDetailed.getCount());
//                }
            } else {//update invoicedetailed
                invoiceDetailed.setId(currentInvoiceDetailed.getId());
                Inits.getUpdateQueries().updateInvoiceDetailed(invoiceDetailed);
                if (!currentInvoice.isTemp()) {
                    invoiceDetailed.setCount(invoiceDetailed.getCount() - currentInvoiceDetailed.getCount());
                }
            }

            if (!currentInvoice.isTemp()) {
                Inits.getUpdateQueries().increaseProductCount(invoiceDetailed.getProduct().getId(),
                        sign * invoiceDetailed.getCount());
            }

            Inits.getDb().getConnection().commit();
            refreshInvoice(currentInvoice.isTemp() ? null : currentInvoice);
        } catch (Exception ex) {
            Inits.getDb().rollback();
            JOptionPane.showMessageDialog(this, "Qaiməyə məhsulun əlavə olunması zamanı səhv baş verdi:\n" + ex.getMessage());
            ex.printStackTrace();
            return;
        } finally {
            Inits.getDb().setAutoCommit(true);
        }

        //refresh others
        jpnlProductSelect.setSelectedProduct(null);
        clearTextFields();
    }//GEN-LAST:event_jbtnAddActionPerformed

    private void jbtnEditSelectedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnEditSelectedActionPerformed
        int rowIdx = jtblResult.getSelectedRow();
        if (rowIdx == -1) {
            JOptionPane.showMessageDialog(null, "Seçim edin!", "Diqqət", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        jpnlProductSelect.setSelectedProduct(null);

        InvoiceDetailed invoiceDetailed = (InvoiceDetailed) tmTemporaryInvoices.getObjectAt(jtblResult.convertRowIndexToModel(rowIdx));
        jpnlProductSelect.setSelectedProduct(invoiceDetailed.getProduct());
        jtxtPriceBuy.setText(Utils.toString(invoiceDetailed.getPriceBuy()));
        jtxtPriceSale.setText(Utils.toString(invoiceDetailed.getPriceSale()));
        jtxtCount.setText(Utils.toString(invoiceDetailed.getCount()));

        currentInvoiceDetailed = invoiceDetailed;
        setInvoiceEditable(true);
        setInvoiceDetailedEditing(true);
    }//GEN-LAST:event_jbtnEditSelectedActionPerformed

    private void jtblResultMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtblResultMouseClicked
        if (evt.getButton() == MouseEvent.BUTTON1 && evt.getClickCount() == 2) {
            jbtnEditSelectedActionPerformed(null);
        }
    }//GEN-LAST:event_jtblResultMouseClicked

    private boolean check() {
        if (jcbInvoiceTypes.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(null, "Naməlum səhv. Xahiş olunur administratora müraciət edin!");
            return false;
        }

        if (jdtInvoiceDate.getDate() == null) {
            JOptionPane.showMessageDialog(null, "Tarixi daxil edin!");
            return false;
        }

        //yoxlaki cedvelde element var
        if (tmTemporaryInvoices.getRowCount() == 0) {
            JOptionPane.showMessageDialog(null, "Cədvəldə element yoxdur!");
            return false;
        }

        if (currentInvoice == null) {
            JOptionPane.showMessageDialog(null, "Qaimə yoxdur!");
            return false;
        }

        return true;
    }
    // End of variables declaration//GEN-END:variables
}
