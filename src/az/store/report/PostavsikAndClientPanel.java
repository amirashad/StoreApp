package az.store.report;

import az.store.Inits;
import az.store.invoice.Invoice;
import az.store.invoice.InvoiceType;
import az.store.types.CodeType;
import az.store.types.CodeValue;
import az.store.payment.Payment;
import az.store.types.PostavsikHesab;
import az.util.components.ExcelWriter;
import az.util.components.ObjectTableModel;
import az.util.utils.Utils;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import jxl.write.WriteException;

/**
 *
 * @author Rashad Amirjanov
 */
public class PostavsikAndClientPanel extends javax.swing.JPanel {

    ObjectTableModel tmPostavsikHesab = new ObjectTableModel(new String[]{
        "ID", "", "İlkin borc tarixi", "İlkin borc (AZN)", "İlkin borc (Valyuta)",
        "Ödəniş (AZN)", "Ödəniş (Valyuta)",
        "Götürülən məhsul (AZN)", "Götürülən məhsul (Valyuta)",
        "Qaytarılan məhsul (AZN)", "Qaytarılan məhsul (Valyuta)",
        "Tarixə kimi", "Son borc (AZN)", "Son borc (Valyuta)"}) {

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            PostavsikHesab postavsikHesab = (PostavsikHesab) this.getRowObjects().get(rowIndex);

            Object result = null;
            switch (columnIndex) {
                case 0:
                    result = postavsikHesab.getId();
                    break;
                case 1:
                    result = postavsikHesab.getClient().getName();
                    break;
                case 2:
                    result = Utils.toStringDate(postavsikHesab.getDateFrom());
                    break;
                case 3:
                    result = postavsikHesab.getSumma();
                    break;
                case 4:
                    result = postavsikHesab.getCurrencyValue() + " (" + postavsikHesab.getCurrencyType().getName() + ")";
                    break;
                case 5:
                    result = postavsikHesab.getPayment();
                    break;
                case 6:
                    result = postavsikHesab.getPaymentCurrency() + " (" + postavsikHesab.getCurrencyType().getName() + ")";
                    break;
                case 7:
                    result = postavsikHesab.getProductPrixod();
                    break;
                case 8:
                    result = postavsikHesab.getProductPrixodCurrency() + " (" + postavsikHesab.getCurrencyType().getName() + ")";
                    break;
                case 9:
                    result = postavsikHesab.getProductVazvrat();
                    break;
                case 10:
                    result = postavsikHesab.getProductVazvratCurrency() + " (" + postavsikHesab.getCurrencyType().getName() + ")";
                    break;
                case 11:
                    result = Utils.toStringDate(postavsikHesab.getDateTo());
                    break;
                case 12:
                    result = Utils.toString(postavsikHesab.getLastSumma());
                    break;
                case 13:
                    result = Utils.toString(postavsikHesab.getLastSummaCurrency()) + " (" + postavsikHesab.getCurrencyType().getName() + ")";
                    break;
            }
            if (result == null) {
                result = "";
            }

            return result;
        }
    };
//    NakladnoyDetailedDialog nakladnoyDetailedDialog = new NakladnoyDetailedDialog(null, true);
    ItemListener radioButoonListener = new ItemListener() {

        @Override
        public void itemStateChanged(ItemEvent e) {
            jcbCodeValue.removeAllItems();

            if (e.getStateChange() == ItemEvent.SELECTED) {
                CodeType codeType = new CodeType(Integer.parseInt(((JRadioButton) e.getItem()).getActionCommand()));

                ArrayList<CodeValue> codeValues = Inits.getSelectQueries().getCodeValues(codeType);
                jcbCodeValue.addItem(new CodeValue());
                for (CodeValue codeValue : codeValues) {
                    if (codeValue.getId() == 0) {
                        continue;
                    }

                    jcbCodeValue.addItem(codeValue);
                }
            }
        }
    };

    private class PaymentsTableModel extends ObjectTableModel {

        public PaymentsTableModel() {
            super(new String[]{"Tipi", "Tarix", "Məbləğ", "Məbləğ (valyuta növü ilə)"});
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            Payment payment = (Payment) this.getRowObjects().get(rowIndex);

            Object result = null;
            switch (columnIndex) {
                case 0:
                    result = payment.getClient().getName();
                    break;
                case 1:
                    result = Utils.toStringDate(payment.getDateTime());
                    break;
                case 2:
                    result = payment.getSumma();
                    break;
                case 3:
//                    result = payment.getCurrencySumma() + " (" + payment.getCurrencyType().getName() + ")";
                    break;
            }
            if (result == null) {
                result = "";
            }

            return result;
        }
    };

    private class InvoicesTableModel extends ObjectTableModel {

        public InvoicesTableModel() {
            super(new String[]{"Nömrə", CodeType.POSTAVSIK.getName(),
                "Tarix", "Alış qiyməti", "Satış qiyməti", "Tip"});
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            Invoice invoice = (Invoice) this.getRowObjects().get(rowIndex);

            Object result = null;
            switch (columnIndex) {
                case 0:
                    result = invoice.getId();
                    break;
                case 1:
                    result = invoice.getClient().toString();
                    break;
                case 2:
                    result = Utils.toStringDate(invoice.getDate());
                    break;
                case 3:
                    result = invoice.getTotalPriceBuy();
                    break;
                case 4:
                    result = invoice.getTotalPriceSale();
                    break;
                case 7:
                    result = invoice.getInvoiceType().toString();
                    break;
            }
            if (result == null) {
                result = "";
            }

            return result;
        }
    };
    PaymentsTableModel tmPayments = new PaymentsTableModel();
    InvoicesTableModel tmInvoices = new InvoicesTableModel();
    InvoicesTableModel tmVazvrat = new InvoicesTableModel();

    /**
     * Creates new form PostavsikAndClientPanel
     */
    public PostavsikAndClientPanel() {
        initComponents();

        jtbPostavsikHesab.setModel(tmPostavsikHesab);

        jrbPostavsik.setActionCommand(String.valueOf(CodeType.POSTAVSIK.getId()));
        jrbClient.setActionCommand(String.valueOf(CodeType.CLIENTS.getId()));

        jrbPostavsik.addItemListener(radioButoonListener);
        jrbClient.addItemListener(radioButoonListener);

        jtbPayments.setModel(tmPayments);
        jtbInInvoices.setModel(tmInvoices);
        jtbOutInvoices.setModel(tmVazvrat);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jbuttonGroupCodeType = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jrbPostavsik = new javax.swing.JRadioButton();
        jrbClient = new javax.swing.JRadioButton();
        jcbCodeValue = new javax.swing.JComboBox();
        jLabel2 = new javax.swing.JLabel();
        jdtTo = new az.util.components.JDateChooserEx();
        jbtSearch = new javax.swing.JButton();
        jdtFrom = new az.util.components.JDateChooserEx();
        jLabel3 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jtbPostavsikHesab = new javax.swing.JTable();
        jbtPrint = new javax.swing.JButton();
        jbtDetailed = new javax.swing.JButton();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jScrollPane2 = new javax.swing.JScrollPane();
        jtbPayments = new javax.swing.JTable();
        jScrollPane4 = new javax.swing.JScrollPane();
        jtbOutInvoices = new javax.swing.JTable();
        jScrollPane3 = new javax.swing.JScrollPane();
        jtbInInvoices = new javax.swing.JTable();

        setPreferredSize(new java.awt.Dimension(888, 600));

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Axtarış"));

        jPanel2.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jbuttonGroupCodeType.add(jrbPostavsik);
        jrbPostavsik.setText("Məhsul gətirən");
        jPanel2.add(jrbPostavsik);

        jbuttonGroupCodeType.add(jrbClient);
        jrbClient.setSelected(true);
        jrbClient.setText("Klient");
        jPanel2.add(jrbClient);

        jcbCodeValue.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jcbCodeValue.setPreferredSize(new java.awt.Dimension(200, 20));
        jPanel2.add(jcbCodeValue);

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel2.setText("Tarixə");

        jbtSearch.setIcon(new javax.swing.ImageIcon(getClass().getResource("/az/store/images/Search.png"))); // NOI18N
        jbtSearch.setText("Göstər");
        jbtSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtSearchActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel3.setText("Tarixdən");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jdtFrom, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 19, Short.MAX_VALUE)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jdtTo, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addComponent(jbtSearch)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jbtSearch)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jdtTo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel2)
                        .addComponent(jdtFrom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel3)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Nəticə"));
        jPanel3.setToolTipText("");

        jtbPostavsikHesab.setModel(new javax.swing.table.DefaultTableModel(
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
        jtbPostavsikHesab.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jtbPostavsikHesabMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jtbPostavsikHesab);

        jbtPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/az/store/images/excel.png"))); // NOI18N
        jbtPrint.setText("Çapa ver");
        jbtPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtPrintActionPerformed(evt);
            }
        });

        jbtDetailed.setText("Seçilmişə ətraflı bax");
        jbtDetailed.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtDetailedActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 821, Short.MAX_VALUE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jbtPrint)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jbtDetailed)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 220, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jbtPrint)
                    .addComponent(jbtDetailed)))
        );

        jTabbedPane1.setPreferredSize(new java.awt.Dimension(457, 200));

        jtbPayments.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane2.setViewportView(jtbPayments);

        jTabbedPane1.addTab("Ödənişlər", jScrollPane2);

        jtbOutInvoices.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane4.setViewportView(jtbOutInvoices);

        jTabbedPane1.addTab("Qaytarılan", jScrollPane4);

        jtbInInvoices.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane3.setViewportView(jtbInInvoices);

        jTabbedPane1.addTab("Mədaxil", jScrollPane3);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(195, 195, 195))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jbtSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtSearchActionPerformed
        tmPostavsikHesab.removeAll();

        if (jbuttonGroupCodeType.getSelection() == null || jdtFrom.getDate() == null || jdtTo.getDate() == null) {
            JOptionPane.showMessageDialog(this, "Xahiş olunur, məlumatları tam daxil edin!");
            return;
        }
        tmPostavsikHesab.setColumnName(1, getSelectedCodeType());

        CodeType codeType = new CodeType();
        CodeValue codeValue = (CodeValue) jcbCodeValue.getSelectedItem();
        codeType.setId(Integer.parseInt(jbuttonGroupCodeType.getSelection().getActionCommand()));

        ArrayList<PostavsikHesab> postavsikHesabs = null;
        if (jcbCodeValue.getSelectedItem() == null || codeValue == null || codeValue.getId() == null) {
            postavsikHesabs = Inits.getSelectQueries().getPostavsikHesabs(codeType, null);
        } else {
            postavsikHesabs = Inits.getSelectQueries().getPostavsikHesabs(null, codeValue);
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(jdtFrom.getDate());
        calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) - 1);
        Date dateFromMinus = calendar.getTime();

        boolean byPriceBuy = false;
        if (jrbClient.isSelected()) {
            byPriceBuy = false;
        } else if (jrbPostavsik.isSelected()) {
            byPriceBuy = true;
        }

        for (PostavsikHesab postavsikHesab : postavsikHesabs) {

            postavsikHesab.setDateTo(dateFromMinus);
            CodeValue cv = new CodeValue(postavsikHesab.getClient().getId());

            Double[] payments;
            try {
                payments = Inits.getSelectQueries().getPostavsikPaymentsSUM(
                        cv, postavsikHesab.getDateFrom(), postavsikHesab.getDateTo());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Sehv", JOptionPane.ERROR_MESSAGE);
                return;
            }
            postavsikHesab.setPayment(payments[0]);
            postavsikHesab.setPaymentCurrency(payments[1]);

            Double[] prixod;
            try {
                prixod = Inits.getSelectQueries().getPostavsikPrixodsSUM(
                        cv, postavsikHesab.getDateFrom(), postavsikHesab.getDateTo(), byPriceBuy);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Sehv", JOptionPane.ERROR_MESSAGE);
                return;
            }
            postavsikHesab.setProductPrixod(prixod[0]);
            postavsikHesab.setProductPrixodCurrency(prixod[1]);

            Double[] vazvrat;
            try {
                vazvrat = Inits.getSelectQueries().getPostavsikVazvratsSUM(
                        cv, postavsikHesab.getDateFrom(), postavsikHesab.getDateTo());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Sehv", JOptionPane.ERROR_MESSAGE);
                return;
            }
            postavsikHesab.setProductVazvrat(vazvrat[0]);
            postavsikHesab.setProductVazvratCurrency(vazvrat[1]);

            postavsikHesab.setLastSumma(postavsikHesab.getSumma() + postavsikHesab.getProductPrixod()
                    - postavsikHesab.getPayment() - postavsikHesab.getProductVazvrat());
            postavsikHesab.setLastSummaCurrency(postavsikHesab.getCurrencyValue() + postavsikHesab.getProductPrixodCurrency()
                    - postavsikHesab.getPaymentCurrency() - postavsikHesab.getProductVazvratCurrency());

            tmPostavsikHesab.addObject(postavsikHesab);
        }

        for (PostavsikHesab postavsikHesabEx : postavsikHesabs) {
            PostavsikHesab newPostavsikHesabEx = new PostavsikHesab();
            newPostavsikHesabEx.setId(postavsikHesabEx.getId());
            newPostavsikHesabEx.setClient(postavsikHesabEx.getClient());
            newPostavsikHesabEx.setDateFrom(jdtFrom.getDate());
            newPostavsikHesabEx.setSumma(postavsikHesabEx.getLastSumma());
            newPostavsikHesabEx.setCurrencyValue(postavsikHesabEx.getLastSummaCurrency());
            newPostavsikHesabEx.setCari(postavsikHesabEx.getCari());
            newPostavsikHesabEx.setDateTo(jdtTo.getDate());
//            newPostavsikHesabEx.setValueName(postavsikHesabEx.getValueName());
//            newPostavsikHesabEx.setDescription(postavsikHesabEx.getDescription());
//TODO            newPostavsikHesabEx.setCvPartID(postavsikHesabEx.getCvPartID());
            newPostavsikHesabEx.setCurrencyType(postavsikHesabEx.getCurrencyType());
            CodeValue cv = postavsikHesabEx.getClient();

            Double[] payments;
            try {
                payments = Inits.getSelectQueries().getPostavsikPaymentsSUM(
                        cv, newPostavsikHesabEx.getDateFrom(), newPostavsikHesabEx.getDateTo());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Sehv", JOptionPane.ERROR_MESSAGE);
                return;
            }
            newPostavsikHesabEx.setPayment(payments[0]);
            newPostavsikHesabEx.setPaymentCurrency(payments[1]);

            Double[] prixod;
            try {
                prixod = Inits.getSelectQueries().getPostavsikPrixodsSUM(
                        cv, newPostavsikHesabEx.getDateFrom(), newPostavsikHesabEx.getDateTo(), byPriceBuy);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Sehv", JOptionPane.ERROR_MESSAGE);
                return;
            }
            newPostavsikHesabEx.setProductPrixod(prixod[0]);
            newPostavsikHesabEx.setProductPrixodCurrency(prixod[1]);

            Double[] vazvrat;
            try {
                vazvrat = Inits.getSelectQueries().getPostavsikVazvratsSUM(
                        cv, newPostavsikHesabEx.getDateFrom(), newPostavsikHesabEx.getDateTo());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Sehv", JOptionPane.ERROR_MESSAGE);
                return;
            }
            newPostavsikHesabEx.setProductVazvrat(vazvrat[0]);
            newPostavsikHesabEx.setProductVazvratCurrency(vazvrat[1]);

            newPostavsikHesabEx.setLastSumma(newPostavsikHesabEx.getSumma() + newPostavsikHesabEx.getProductPrixod()
                    - newPostavsikHesabEx.getPayment() - newPostavsikHesabEx.getProductVazvrat());
            newPostavsikHesabEx.setLastSummaCurrency(newPostavsikHesabEx.getCurrencyValue() + newPostavsikHesabEx.getProductPrixodCurrency()
                    - newPostavsikHesabEx.getPaymentCurrency() - newPostavsikHesabEx.getProductVazvratCurrency());

            tmPostavsikHesab.addObject(newPostavsikHesabEx);
        }

    }//GEN-LAST:event_jbtSearchActionPerformed

    private void jtbPostavsikHesabMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtbPostavsikHesabMouseClicked
        if (evt.getButton() == MouseEvent.BUTTON1 && evt.getClickCount() == 2) {
            jbtDetailedActionPerformed(null);
        }
    }//GEN-LAST:event_jtbPostavsikHesabMouseClicked

    private void jbtPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtPrintActionPerformed
        try {
            ExcelWriter.viewAsExcelFile(new ObjectTableModel[]{tmPostavsikHesab},
                    new String[]{
                        "Nakladnoy"
                    });
        } catch (IOException | WriteException ex) {
            ex.printStackTrace();
        }
    }//GEN-LAST:event_jbtPrintActionPerformed

    private void jbtDetailedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtDetailedActionPerformed
        tmPayments.removeAll();
        tmInvoices.removeAll();
        tmVazvrat.removeAll();

        if (jbuttonGroupCodeType.getSelection() == null) {
            return;
        }

        if (jtbPostavsikHesab.getSelectedRow() == -1) {
            return;
        }

        PostavsikHesab postavsikHesabEx = (PostavsikHesab) tmPostavsikHesab.getObjectAt(jtbPostavsikHesab.getSelectedRow());

        if (postavsikHesabEx == null) {
            return;
        }

        CodeType codeType = new CodeType();
        codeType.setId(Integer.parseInt(jbuttonGroupCodeType.getSelection().getActionCommand()));
        codeType.setName(getSelectedCodeType());

        CodeValue client = postavsikHesabEx.getClient();
        client.setCodeType(codeType);

        ArrayList<Payment> payments = Inits.getSelectQueries().getPayments(postavsikHesabEx.getDateFrom(), postavsikHesabEx.getDateTo(), null, client);
        for (Payment vPayment : payments) {
            tmPayments.addObject(vPayment);
        }

        ArrayList<InvoiceType> allInvoiceTypes = Inits.getSelectQueries().getAllInvoiceTypes();
//        ArrayList<InvoiceType> inInvoiceTypes = new ArrayList<InvoiceType>();
//        for (InvoiceType aInvoiceType : allInvoiceTypes) {
//            if()
//        }

        ArrayList<Invoice> inInvoices = Inits.getSelectQueries().getInvoices(
                null,
                client.getId(),
                false,
                postavsikHesabEx.getDateFrom(),
                postavsikHesabEx.getDateTo(), allInvoiceTypes);
        for (Invoice invoice : inInvoices) {
            tmInvoices.addObject(invoice);
        }
//
//        ArrayList<VNakladnoy> nakladnoysVazvrat = Inits.getSelectQueries().getNakladnoys(codeType,
//                cv, postavsikHesabEx.getHesabPostavsikDateFrom(), postavsikHesabEx.getHesabPostavsikDateTo(), 2);
//        for (VNakladnoy vNakladnoy : nakladnoysVazvrat) {
//            tmVazvrat.addObject(vNakladnoy);
//        }
        //        nakladnoyDetailedDialog.showForm(postavsikHesab, codeType);
    }//GEN-LAST:event_jbtDetailedActionPerformed
//    
//    if (tmPostavsikHesab.getRowCount() == 0) { return; }
//    
//    boolean isPostavsikHesabAcceptable = true; {//eger sechilmish tarix ilkin
//    tarixden kichikdirse mesaj chixar ki olmaz. for (int i = 0; i <
//    tmPostavsikHesab.getRowCount(); i++) { VPostavsikHesabEx postavsikHesabEx
//    = (VPostavsikHesabEx) tmPostavsikHesab.getObjectAt(i);
//    
//    if (postavsikHesabEx == null) { return; }
//    
//    if
//    (postavsikHesabEx.getHesabPostavsikDateFrom().after(postavsikHesabEx.getLastDate()))
//    { isPostavsikHesabAcceptable = false; break; } } }
//    
//    if (!isPostavsikHesabAcceptable) { JOptionPane.showMessageDialog(this,
//    "Seçilmiş tarix ilkin borc tarixindən kiçikdir. " + "Xahiş olunur, Tarixi
//    düzgün seçin. " + "Seçilmiş tarix ilkin borc tarixindən böyük
//    olmalıdır.", "Səhv", JOptionPane.ERROR_MESSAGE); return; }
//    
//    if (JOptionPane.showConfirmDialog(this, "Qəbul olunsunmu?") !=
//    JOptionPane.YES_OPTION) { return; }
//    
//    for (int i = 0; i < tmPostavsikHesab.getRowCount(); i++) {
//    VPostavsikHesabEx postavsikHesabEx = (VPostavsikHesabEx)
//    tmPostavsikHesab.getObjectAt(i);
//    
//    if (postavsikHesabEx == null) { return; }
//    
//    CodeType codeType = new CodeType();
//    codeType.setId(Integer.parseInt(jbuttonGroupCodeType.getSelection().getActionCommand()));
//    codeType.setTypeName(getSelectedCodeType());
//    
//    CodeValue cv = new CodeValue((int)
//    postavsikHesabEx.getHesabPostavsikId()); cv.setCodeType(codeType);
//    
//    try { boolean update =
//    Initialization.getUpdateQueries().updatePostavsikHesab(postavsikHesabEx);
//    if (update == false) { JOptionPane.showMessageDialog(this, "Əməliyyat
//    zamanı səhv baş verdi.", "Səhv", JOptionPane.ERROR_MESSAGE); } else {
//    JOptionPane.showMessageDialog(this, "Əməliyyat uğurlu oldu.", "Məlumat",
//    JOptionPane.INFORMATION_MESSAGE); } } catch (Exception ex) {
//    JOptionPane.showMessageDialog(this, "Əməliyyat zamanı səhv baş verdi.",
//    "Səhv", JOptionPane.ERROR_MESSAGE);
//    
//    ex.printStackTrace(); return; } }
//    
//    jbtSearchActionPerformed(evt);
//    

    public void showForm() {

        clearDialog();

//        setVisible(true);
    }

    private String getSelectedCodeType() {
        if (jrbClient.isSelected()) {
            return jrbClient.getText();
        } else if (jrbPostavsik.isSelected()) {
            return jrbPostavsik.getText();
        } else {
            return "";
        }
    }

    private void clearDialog() {
        jdtTo.setDate(null);
        tmPostavsikHesab.removeAll();
        jrbPostavsik.setSelected(true);
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JButton jbtDetailed;
    private javax.swing.JButton jbtPrint;
    private javax.swing.JButton jbtSearch;
    private javax.swing.ButtonGroup jbuttonGroupCodeType;
    private javax.swing.JComboBox jcbCodeValue;
    private az.util.components.JDateChooserEx jdtFrom;
    private az.util.components.JDateChooserEx jdtTo;
    private javax.swing.JRadioButton jrbClient;
    private javax.swing.JRadioButton jrbPostavsik;
    private javax.swing.JTable jtbInInvoices;
    private javax.swing.JTable jtbOutInvoices;
    private javax.swing.JTable jtbPayments;
    private javax.swing.JTable jtbPostavsikHesab;
    // End of variables declaration//GEN-END:variables
}
