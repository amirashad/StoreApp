/*
 * PaymentDialog.java
 *
 * Created on Jan 10, 2012, 7:44:08 PM
 */
package az.store.payment;

import az.store.Inits;
import az.store.types.CodeType;
import az.store.types.CodeValue;
import az.util.components.ObjectTableModel;
import az.util.components.document.JTextFieldOnlyNumber;
import az.util.utils.Utils;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Rashad Amirjanov
 */
public class PaymentDialog extends javax.swing.JDialog {

    private static PaymentDialog paymentDialog = null;

    public static PaymentDialog instance() {
        if (paymentDialog == null) {
            paymentDialog = new PaymentDialog(null, true);
        }
        return paymentDialog;
    }

    private CodeType dialogCodeType = null;
    private final ObjectTableModel tmPayments = new ObjectTableModel(new String[]{
        "", "Tarix", "Məbləğ", "Qeyd"
    }) {

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
                    result = payment.getSumma() + " (AZN)";
                    break;
                case 3:
                    result = payment.getDescr();
                    break;
            }

            if (result == null) {
                result = "";
            }

            return result;
        }
    };

    /**
     * Creates new form PaymentDialog
     */
    public PaymentDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();

        jTblPayments.setModel(tmPayments);
        jtxtPaymentAmount.setDocument(new JTextFieldOnlyNumber());
        setLocationRelativeTo(null);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPnlParameters = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jlbCodeType = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jtxtPaymentAmount = new javax.swing.JTextField();
        jbtnAddPayment = new javax.swing.JButton();
        jCbCodeValue = new javax.swing.JComboBox();
        jDtDate = new az.util.components.JDateChooserEx();
        jLabel4 = new javax.swing.JLabel();
        jtxtDescr = new javax.swing.JTextField();
        jPnlTable = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTblPayments = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jbtnDelPayment = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jbtnClose = new javax.swing.JButton();

        jPnlParameters.setBorder(javax.swing.BorderFactory.createTitledBorder("Ödəniş üçün parametrlər"));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel1.setText("Tarix");

        jlbCodeType.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jlbCodeType.setText("Code value");

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel3.setText("Məbləğ (AZN ilə)");

        jtxtPaymentAmount.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        jbtnAddPayment.setIcon(new javax.swing.ImageIcon(getClass().getResource("/az/store/images/add.png"))); // NOI18N
        jbtnAddPayment.setText("Əlavə et");
        jbtnAddPayment.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnAddPaymentActionPerformed(evt);
            }
        });

        jCbCodeValue.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jCbCodeValue.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCbCodeValueItemStateChanged(evt);
            }
        });

        jDtDate.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jDtDatePropertyChange(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel4.setText("Qeyd");

        javax.swing.GroupLayout jPnlParametersLayout = new javax.swing.GroupLayout(jPnlParameters);
        jPnlParameters.setLayout(jPnlParametersLayout);
        jPnlParametersLayout.setHorizontalGroup(
            jPnlParametersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPnlParametersLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPnlParametersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 117, Short.MAX_VALUE))
                .addGap(94, 94, 94)
                .addGroup(jPnlParametersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jtxtPaymentAmount, javax.swing.GroupLayout.DEFAULT_SIZE, 110, Short.MAX_VALUE)
                    .addComponent(jDtDate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPnlParametersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jlbCodeType, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPnlParametersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPnlParametersLayout.createSequentialGroup()
                        .addComponent(jtxtDescr, javax.swing.GroupLayout.DEFAULT_SIZE, 123, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jbtnAddPayment))
                    .addComponent(jCbCodeValue, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jPnlParametersLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jLabel1, jLabel3});

        jPnlParametersLayout.setVerticalGroup(
            jPnlParametersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPnlParametersLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPnlParametersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPnlParametersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jlbCodeType)
                        .addComponent(jCbCodeValue, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel1)
                    .addComponent(jDtDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(7, 7, 7)
                .addGroup(jPnlParametersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jtxtPaymentAmount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(jbtnAddPayment)
                    .addComponent(jLabel4)
                    .addComponent(jtxtDescr, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        getContentPane().add(jPnlParameters, java.awt.BorderLayout.NORTH);

        jPnlTable.setBorder(javax.swing.BorderFactory.createTitledBorder("Bütün ödənişlər"));
        jPnlTable.setLayout(new java.awt.BorderLayout());

        jTblPayments.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane1.setViewportView(jTblPayments);

        jPnlTable.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        getContentPane().add(jPnlTable, java.awt.BorderLayout.CENTER);

        jPanel1.setLayout(new javax.swing.BoxLayout(jPanel1, javax.swing.BoxLayout.LINE_AXIS));

        jPanel2.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jbtnDelPayment.setIcon(new javax.swing.ImageIcon(getClass().getResource("/az/store/images/minus.png"))); // NOI18N
        jbtnDelPayment.setText("Seçilmiş ödənişi sil");
        jbtnDelPayment.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnDelPaymentActionPerformed(evt);
            }
        });
        jPanel2.add(jbtnDelPayment);

        jPanel1.add(jPanel2);

        jPanel3.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        jbtnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/az/store/images/close.png"))); // NOI18N
        jbtnClose.setText("Bağla");
        jbtnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnCloseActionPerformed(evt);
            }
        });
        jPanel3.add(jbtnClose);

        jPanel1.add(jPanel3);

        getContentPane().add(jPanel1, java.awt.BorderLayout.PAGE_END);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jbtnAddPaymentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnAddPaymentActionPerformed
        if (jCbCodeValue.getSelectedItem() == null
                || jtxtPaymentAmount.getText().isEmpty()
                || jDtDate.getDate() == null) {
            JOptionPane.showMessageDialog(this, "Xahiş olunur məlumatları tam daxil edin!");
            return;
        }

        Payment payment = new Payment();
        payment.setSumma(Utils.toDouble(jtxtPaymentAmount.getText()));
        payment.setDateTime(jDtDate.getDate());
        payment.setClient((CodeValue) jCbCodeValue.getSelectedItem());
        payment.setDescr(jtxtDescr.getText());

        try {
            Inits.getInsertQueries().insertPayment(payment);
            clearParameters();
            refreshPayments();
        } catch (SQLException ex) {
            Logger.getGlobal().log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this, "Daxiletmə zamanı səhv baş verdi");
        }

    }//GEN-LAST:event_jbtnAddPaymentActionPerformed

    private void jbtnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnCloseActionPerformed
        setVisible(false);
    }//GEN-LAST:event_jbtnCloseActionPerformed

    private void jbtnDelPaymentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnDelPaymentActionPerformed
        if (jTblPayments.getSelectedRow() == -1) {
            return;
        }

        Payment payment = (Payment) tmPayments.getObjectAt(jTblPayments.getSelectedRow());
        if (payment == null) {
            return;
        }

        int del = JOptionPane.showConfirmDialog(this, "<html><b>" + payment.toString() + "</b> silinsinmi?</html>",
                "", JOptionPane.YES_NO_OPTION);

        if (del == JOptionPane.YES_OPTION) {
            try {
                Inits.getDeleteQueries().deletePayment(payment);
            } catch (Exception ex) {
                Logger.getGlobal().log(Level.SEVERE, null, ex);
            }
            refreshPayments();
        }
    }//GEN-LAST:event_jbtnDelPaymentActionPerformed

    private void jDtDatePropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jDtDatePropertyChange
        refreshPayments();
    }//GEN-LAST:event_jDtDatePropertyChange

    private void jCbCodeValueItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCbCodeValueItemStateChanged
        refreshPayments();
    }//GEN-LAST:event_jCbCodeValueItemStateChanged

    public void showForm(CodeType codeType) {
        dialogCodeType = codeType;
        tmPayments.removeAll();

        refreshCodeValues();

        jlbCodeType.setText(dialogCodeType.getName());
        setTitle(dialogCodeType.getName()+"ə ödənişlər" );
        tmPayments.setColumnName(0, dialogCodeType.getName());

        setVisible(true);
    }

    private void refreshCodeValues() {
        jCbCodeValue.removeAllItems();

        ArrayList<CodeValue> codeValues = Inits.getSelectQueries().getCodeValues(dialogCodeType);
        for (CodeValue codeValue : codeValues) {
            if (codeValue.getId() != 0) {
                jCbCodeValue.addItem(codeValue);
            }
        }

//        CodeValue codeValueNewProductType = new CodeValue();
//        codeValueNewProductType.setId(-1);
//        codeValueNewProductType.setValueName("  [ Yeni məhsul tipi əlavə et... ]");
//        codeValueNewProductType.setCodeType(new CodeType(CodeType.PRODUCT_TYPE.getCode()));
//        codeValueNewProductType.setValid(0);
//        jCbProductType.addItem(codeValueNewProductType);
    }

    private void refreshPayments() {
        tmPayments.removeAll();
        Date date = jDtDate.getDate();
        System.out.println(date);
        if (date == null) {
            return;
        }

        ArrayList<Payment> payments = Inits.getSelectQueries().getPayments(date, date, dialogCodeType, null);
        for (Payment payment : payments) {
            tmPayments.addObject(payment);
        }

//        CodeValue codeValueNewProductType = new CodeValue();
//        codeValueNewProductType.setId(-1);
//        codeValueNewProductType.setValueName("  [ Yeni məhsul tipi əlavə et... ]");
//        codeValueNewProductType.setCodeType(new CodeType(CodeType.PRODUCT_TYPE.getCode()));
//        codeValueNewProductType.setValid(0);
//        jCbProductType.addItem(codeValueNewProductType);
    }

    private void clearParameters() {
//        jDtDate.setDate(new Date());
        jtxtPaymentAmount.setText("");
        jtxtDescr.setText("");
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox jCbCodeValue;
    private az.util.components.JDateChooserEx jDtDate;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPnlParameters;
    private javax.swing.JPanel jPnlTable;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTblPayments;
    private javax.swing.JButton jbtnAddPayment;
    private javax.swing.JButton jbtnClose;
    private javax.swing.JButton jbtnDelPayment;
    private javax.swing.JLabel jlbCodeType;
    private javax.swing.JTextField jtxtDescr;
    private javax.swing.JTextField jtxtPaymentAmount;
    // End of variables declaration//GEN-END:variables
}