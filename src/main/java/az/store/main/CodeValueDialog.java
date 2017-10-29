/*
 * CodeValueDialog.java
 *
 * Created on Jan 8, 2012, 3:06:45 PM
 */
package az.store.main;

import az.store.Inits;
import az.store.types.CodeType;
import az.store.types.CodeValue;
import az.store.types.PostavsikHesab;
import az.util.components.ObjectTableModel;

import javax.swing.*;
import java.awt.event.ItemEvent;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Rashad Amirjanov
 */
public class CodeValueDialog extends javax.swing.JDialog {

    static CodeValueDialog codeValueDialog = null;
    private ObjectTableModel codeValuesTableModel = new ObjectTableModel(new String[]{"Adı",
            "Əlavə məlumat"}) {
        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            CodeValue codeValue = (CodeValue) this.getRowObjects().get(rowIndex);

            Object result = null;
            switch (columnIndex) {
                case 0:
                    result = codeValue.getName();
                    break;
                case 1:
                    result = codeValue.getDescription();
                    break;
//            case 2:
//                result = codeValue.getCodeType().getTypeName();
//                break;
            }
            if (result == null) {
                result = "";
            }

            return result;
        }
    };
    ObjectTableModel objectTableModel = codeValuesTableModel;
    //    private ObjectTableModel postavsikClientTableModel = new ObjectTableModel(new String[]{"Adı",
//        "Əlavə məlumat", "Tarix", "Qalıq borc", "Qalıq borc (valyuta növü ilə)"}) {
//        @Override
//        public Object getValueAt(int rowIndex, int columnIndex) {
//            PostavsikHesab postavsikHesab = (PostavsikHesab) this.getRowObjects().get(rowIndex);
//
//            Object result = null;
//            switch (columnIndex) {
//                case 0:
//                    result = postavsikHesab.getClient().getName();
//                    break;
//                case 1:
//                    result = postavsikHesab.getClient().getDescription();
//                    break;
//                case 2:
//                    result = postavsikHesab.getDateFrom();
//                    break;
//                case 3:
//                    result = postavsikHesab.getSumma() + " (AZN)";
//                    break;
//                case 4:
//                    result = postavsikHesab.getCurrencyValue() + " (" + postavsikHesab.getCurrencyType().getName() + ")";
//                    break;
//            }
//            if (result == null) {
//                result = "";
//            }
//
//            return result;
//        }
//    };
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jBtnAdd;
    private javax.swing.JButton jBtnClose;
    private javax.swing.JButton jButton1;
    private javax.swing.JComboBox jCbCodeType;
    private javax.swing.JLabel jLblCodeName;
    private javax.swing.JLabel jLblDescription;
    private javax.swing.JLabel jLblPostavsikDtFrom;
    private javax.swing.JPanel jPnlClose;
    private javax.swing.JPanel jPnlEditSave;
    private javax.swing.JPanel jPnlParameters;
    private javax.swing.JPanel jPnlPostavsik;
    private javax.swing.JPanel jPnlSave;
    private javax.swing.JPanel jPnlTable;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTblCodeValues;
    private javax.swing.JTextField jTxtCodeName;
    private javax.swing.JTextField jTxtDescription;
    private javax.swing.JComboBox jcbPostavsikSummaCurrencyType;
    private az.util.components.JDateChooserEx jdtFrom;
    private javax.swing.JLabel jlbPostavsikSummaAZN;
    private javax.swing.JLabel jlbPostavsikSummaCurrency;
    private javax.swing.JLabel jlbPostavsikSummaCurrency1;
    private javax.swing.JPanel jpnlPostavsikSummaCurrency;
    private javax.swing.JTextField jtxtPostavsikSummaAZN;
    private javax.swing.JTextField jtxtPostavsikSummaCurrency;

    /**
     * Creates new form CodeValueDialog
     */
    public CodeValueDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();

        jCbCodeType.setVisible(false);

        jTblCodeValues.setModel(objectTableModel);

        setLocationRelativeTo(null);
    }

    public static CodeValueDialog instance() {
        if (codeValueDialog == null) {
            codeValueDialog = new CodeValueDialog(null, true);
        }
        return codeValueDialog;
    }

    public void showForm(CodeType selectedCodeType) {
        if (selectedCodeType == null) {
            return;
        }

        jCbCodeType.removeAllItems();
        clearParameters();

//        boolean isPostavsikOrClient = selectedCodeType.equals(CodeType.POSTAVSIK)
//                || selectedCodeType.equals(CodeType.CLIENTS);

//        jPnlPostavsik.setVisible(isPostavsikOrClient);
//        if (isPostavsikOrClient) {
//            objectTableModel = postavsikClientTableModel;
//            jTblCodeValues.setModel(objectTableModel);
//        } else {
//            objectTableModel = codeValuesTableModel;
//            jTblCodeValues.setModel(objectTableModel);
//        }

        ArrayList<CodeType> codeTypes = Inits.getSelectQueries().getAllCodeTypes();
        for (CodeType codeType : codeTypes) {
            jCbCodeType.addItem(codeType);
//            System.out.println(codeType.getId());
        }
//        System.err.println(selectedCodeType.getId());
        jCbCodeType.setSelectedItem(selectedCodeType);
        setTitle(((CodeType) jCbCodeType.getSelectedItem()).getName());

        jcbPostavsikSummaCurrencyType.removeAllItems();
        ArrayList<CodeValue> currencyTypes = Inits.getSelectQueries().getCodeValues(CodeType.CURRENCY_TYPE);
        for (CodeValue codeValue : currencyTypes) {
            jcbPostavsikSummaCurrencyType.addItem(codeValue);
        }

        if (currencyTypes.isEmpty()) {
            jpnlPostavsikSummaCurrency.setVisible(false);
            jcbPostavsikSummaCurrencyType.setVisible(false);
            jtxtPostavsikSummaCurrency.setVisible(false);

//            jTblCodeValues.getColumnModel().getColumn(4).setWidth(0);
            if (jTblCodeValues.getColumnCount() > 4) {
                jTblCodeValues.removeColumn(jTblCodeValues.getColumnModel().getColumn(4));
            }
        }

        refreshCodeValuesTable();

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

        jPnlEditSave = new javax.swing.JPanel();
        jPnlParameters = new javax.swing.JPanel();
        jLblCodeName = new javax.swing.JLabel();
        jTxtCodeName = new javax.swing.JTextField();
        jLblDescription = new javax.swing.JLabel();
        jTxtDescription = new javax.swing.JTextField();
        jPnlPostavsik = new javax.swing.JPanel();
        jLblPostavsikDtFrom = new javax.swing.JLabel();
        jdtFrom = new az.util.components.JDateChooserEx();
        jlbPostavsikSummaAZN = new javax.swing.JLabel();
        jtxtPostavsikSummaAZN = new javax.swing.JTextField();
        jpnlPostavsikSummaCurrency = new javax.swing.JPanel();
        jlbPostavsikSummaCurrency = new javax.swing.JLabel();
        jcbPostavsikSummaCurrencyType = new javax.swing.JComboBox();
        jlbPostavsikSummaCurrency1 = new javax.swing.JLabel();
        jtxtPostavsikSummaCurrency = new javax.swing.JTextField();
        jPnlSave = new javax.swing.JPanel();
        jCbCodeType = new javax.swing.JComboBox();
        jBtnAdd = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jPnlTable = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTblCodeValues = new javax.swing.JTable();
        jPnlClose = new javax.swing.JPanel();
        jBtnClose = new javax.swing.JButton();

        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentHidden(java.awt.event.ComponentEvent evt) {
                formComponentHidden(evt);
            }
        });

        jPnlEditSave.setBorder(javax.swing.BorderFactory.createTitledBorder("Yenisini daxil et"));

        jPnlParameters.setLayout(new java.awt.GridLayout(2, 2, 5, 5));

        jLblCodeName.setText("Adı");
        jPnlParameters.add(jLblCodeName);
        jPnlParameters.add(jTxtCodeName);

        jLblDescription.setText("Əlavə məlumat");
        jPnlParameters.add(jLblDescription);
        jPnlParameters.add(jTxtDescription);

        jPnlPostavsik.setLayout(new java.awt.GridLayout(3, 2, 5, 5));

        jLblPostavsikDtFrom.setText("Tarix");
        jPnlPostavsik.add(jLblPostavsikDtFrom);
        jPnlPostavsik.add(jdtFrom);

        jlbPostavsikSummaAZN.setText("Qalıq borc (AZN ilə)");
        jPnlPostavsik.add(jlbPostavsikSummaAZN);

        jtxtPostavsikSummaAZN.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jtxtPostavsikSummaAZNKeyPressed(evt);
            }

            public void keyReleased(java.awt.event.KeyEvent evt) {
                jtxtPostavsikSummaAZNKeyReleased(evt);
            }

            public void keyTyped(java.awt.event.KeyEvent evt) {
                jtxtPostavsikSummaAZNKeyTyped(evt);
            }
        });
        jPnlPostavsik.add(jtxtPostavsikSummaAZN);

        jpnlPostavsikSummaCurrency.setPreferredSize(new java.awt.Dimension(459, 22));

        jlbPostavsikSummaCurrency.setText("Qalıq borc (");

        jcbPostavsikSummaCurrencyType.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jcbPostavsikSummaCurrencyTypeItemStateChanged(evt);
            }
        });

        jlbPostavsikSummaCurrency1.setText("ilə)");

        javax.swing.GroupLayout jpnlPostavsikSummaCurrencyLayout = new javax.swing.GroupLayout(jpnlPostavsikSummaCurrency);
        jpnlPostavsikSummaCurrency.setLayout(jpnlPostavsikSummaCurrencyLayout);
        jpnlPostavsikSummaCurrencyLayout.setHorizontalGroup(
                jpnlPostavsikSummaCurrencyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpnlPostavsikSummaCurrencyLayout.createSequentialGroup()
                                .addComponent(jlbPostavsikSummaCurrency)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jcbPostavsikSummaCurrencyType, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jlbPostavsikSummaCurrency1)
                                .addContainerGap(69, Short.MAX_VALUE))
        );
        jpnlPostavsikSummaCurrencyLayout.setVerticalGroup(
                jpnlPostavsikSummaCurrencyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jpnlPostavsikSummaCurrencyLayout.createSequentialGroup()
                                .addGroup(jpnlPostavsikSummaCurrencyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jlbPostavsikSummaCurrency, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jcbPostavsikSummaCurrencyType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jlbPostavsikSummaCurrency1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 0, Short.MAX_VALUE))
        );

        jPnlPostavsik.add(jpnlPostavsikSummaCurrency);
        jPnlPostavsik.add(jtxtPostavsikSummaCurrency);

        jCbCodeType.setModel(new javax.swing.DefaultComboBoxModel(new String[]{"Item 1", "Item 2", "Item 3", "Item 4"}));
        jPnlSave.add(jCbCodeType);

        jBtnAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/az/store/images/add.png"))); // NOI18N
        jBtnAdd.setText("Əlavə et");
        jBtnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnAddActionPerformed(evt);
            }
        });
        jPnlSave.add(jBtnAdd);

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/az/store/images/edit.png"))); // NOI18N
        jButton1.setText("Seçilmişin adını dəyiş");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPnlSave.add(jButton1);

        javax.swing.GroupLayout jPnlEditSaveLayout = new javax.swing.GroupLayout(jPnlEditSave);
        jPnlEditSave.setLayout(jPnlEditSaveLayout);
        jPnlEditSaveLayout.setHorizontalGroup(
                jPnlEditSaveLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jPnlParameters, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPnlPostavsik, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 468, Short.MAX_VALUE)
                        .addComponent(jPnlSave, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPnlEditSaveLayout.setVerticalGroup(
                jPnlEditSaveLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPnlEditSaveLayout.createSequentialGroup()
                                .addComponent(jPnlParameters, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPnlPostavsik, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPnlSave, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        getContentPane().add(jPnlEditSave, java.awt.BorderLayout.PAGE_START);

        jPnlTable.setBorder(javax.swing.BorderFactory.createTitledBorder("Hamısı"));

        jTblCodeValues.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane1.setViewportView(jTblCodeValues);

        javax.swing.GroupLayout jPnlTableLayout = new javax.swing.GroupLayout(jPnlTable);
        jPnlTable.setLayout(jPnlTableLayout);
        jPnlTableLayout.setHorizontalGroup(
                jPnlTableLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPnlTableLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 448, Short.MAX_VALUE)
                                .addContainerGap())
        );
        jPnlTableLayout.setVerticalGroup(
                jPnlTableLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 276, Short.MAX_VALUE)
        );

        getContentPane().add(jPnlTable, java.awt.BorderLayout.CENTER);

        jBtnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/az/store/images/close.png"))); // NOI18N
        jBtnClose.setText("Bağla");
        jBtnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnCloseActionPerformed(evt);
            }
        });
        jPnlClose.add(jBtnClose);

        getContentPane().add(jPnlClose, java.awt.BorderLayout.SOUTH);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jBtnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnCloseActionPerformed
        setVisible(false);
    }//GEN-LAST:event_jBtnCloseActionPerformed

    private void jBtnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnAddActionPerformed
        if (jTxtCodeName.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Xahiş olunur, Adını daxil edin!");
            return;
        }

        CodeType selectedCodeType = (CodeType) jCbCodeType.getSelectedItem();
//        boolean isPostavsikOrClient = selectedCodeType.equals(CodeType.POSTAVSIK)
//                || selectedCodeType.equals(CodeType.CLIENTS);

        boolean isOtherCurrencyExists = jcbPostavsikSummaCurrencyType.getItemCount() > 0;

//        if (isPostavsikOrClient) {
//            if (jtxtPostavsikSummaAZN.getText().isEmpty()) {
//                JOptionPane.showMessageDialog(this, "Xahiş olunur, AZN ile qaliq borcu daxil edin!");
//                return;
//            }
//            if (jtxtPostavsikSummaCurrency.getText().isEmpty()) {
//                JOptionPane.showMessageDialog(this, "Xahiş olunur, Digər valyuta ilə qalıq borcu daxil edin!");
//                return;
//            }
//        }

        CodeValue codeValue = new CodeValue();
        codeValue.setCodeType(selectedCodeType);
        codeValue.setName(jTxtCodeName.getText());
        codeValue.setDescription(jTxtDescription.getText());

        {//transaction
            try {
                Inits.getDb().getConnection().setAutoCommit(false);
                int codeValueId = Inits.getInsertQueries().insertCodeValue(codeValue);

//                if (isPostavsikOrClient) {
//                    Logger.getGlobal().log(Level.INFO, "cv_id {0}", codeValueId);
//
//                    if (codeValueId != -1) {
//                        PostavsikHesab postavsikHesab = new PostavsikHesab();
//                        postavsikHesab.setClient(new CodeValue(codeValueId));
//                        postavsikHesab.setDateFrom(jdtFrom.getDate());
//                        try {
//                            postavsikHesab.setSumma(Double.parseDouble(jtxtPostavsikSummaAZN.getText()));
//                        } catch (NumberFormatException ex) {
//                            Logger.getGlobal().log(Level.WARNING, "", ex);
//                        }
//                        postavsikHesab.setCari((short) 1);
//                        if (isOtherCurrencyExists) {
//                            postavsikHesab.setCurrencyType((CodeValue) jcbPostavsikSummaCurrencyType.getSelectedItem());
//                            try {
//                                postavsikHesab.setCurrencyValue(Double.parseDouble(jtxtPostavsikSummaCurrency.getText()));
//                            } catch (NumberFormatException ex) {
//                                Logger.getGlobal().log(Level.WARNING, "", ex);
//                            }
//                        }
//                        Inits.getInsertQueries().insertPostavsikHesab(postavsikHesab);
//                    }
//                }
                Inits.getDb().getConnection().commit();
            } catch (SQLException ex) {
                Inits.getDb().rollback();
                Logger.getGlobal().log(Level.SEVERE, "", ex);
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Səhv", JOptionPane.ERROR_MESSAGE);
                return;
            } finally {
                Inits.getDb().setAutoCommit(true);
            }
        }

        clearParameters();
        refreshCodeValuesTable();
    }//GEN-LAST:event_jBtnAddActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        if (jTblCodeValues.getSelectedRow() == -1) {
            return;
        }

        Object object = objectTableModel.getObjectAt(jTblCodeValues.getSelectedRow());
        if (object == null) {
            return;
        }

        CodeValue codeValue = null;
        if (object instanceof CodeValue) {
            codeValue = (CodeValue) object;
        } else if (object instanceof PostavsikHesab) {
            PostavsikHesab ph = (PostavsikHesab) object;
            codeValue = ph.getClient();
        }

        if (codeValue == null) {
            return;
        }

        String string = JOptionPane.showInputDialog(this, "Xahiş olunur, yeni adı daxil edin!",
                codeValue.getName());
        if (string == null || string.equals(codeValue.getName())) {
            return;
        }

        codeValue.setName(string);
        try {
            Inits.getUpdateQueries().updateCodeValueName(codeValue);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        refreshCodeValuesTable();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void formComponentHidden(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentHidden
//        System.out.println("window hiddened");
//
//        Window window = getOwner();
//        if (window == null || !(window instanceof MainForm)) {
//            System.err.println("parent MainForm deyil!");
//            return;
//        }
//        CodeType selectedCodeType = (CodeType) jCbCodeType.getSelectedItem();

//        MainForm mainForm = (MainForm) window;
//        if (selectedCodeType.equals(CodeType.CLIENTS.getCodeType())) {
//            mainForm.refreshClients();
//        } else if (selectedCodeType.equals(CodeType.EXPENDITURES.getCodeType())) {
//            mainForm.refreshExpenditures();
//        } else if (selectedCodeType.equals(CodeType.POSTAVSIK.getCodeType())) {
//            mainForm.refreshPostavsik();
//        } else if (selectedCodeType.equals(CodeType.PRODUCT_TYPE.getCodeType())) {
//            mainForm.refreshProductTypes();
//        }
    }//GEN-LAST:event_formComponentHidden

    private void jcbPostavsikSummaCurrencyTypeItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jcbPostavsikSummaCurrencyTypeItemStateChanged
        if (evt.getStateChange() == ItemEvent.SELECTED) {
            CodeValue currencyType = (CodeValue) jcbPostavsikSummaCurrencyType.getSelectedItem();
            if (currencyType == null || currencyType.getName().equals("AZN")) {
                jtxtPostavsikSummaCurrency.setEnabled(false);
                jtxtPostavsikSummaCurrency.setText(jtxtPostavsikSummaAZN.getText());
            } else {
                jtxtPostavsikSummaCurrency.setEnabled(true);
                jtxtPostavsikSummaCurrency.setText("");
            }
        }
    }//GEN-LAST:event_jcbPostavsikSummaCurrencyTypeItemStateChanged

    private void jtxtPostavsikSummaAZNKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtPostavsikSummaAZNKeyPressed
    }//GEN-LAST:event_jtxtPostavsikSummaAZNKeyPressed

    private void jtxtPostavsikSummaAZNKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtPostavsikSummaAZNKeyTyped
    }//GEN-LAST:event_jtxtPostavsikSummaAZNKeyTyped

    private void jtxtPostavsikSummaAZNKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtPostavsikSummaAZNKeyReleased
        CodeValue currencyType = (CodeValue) jcbPostavsikSummaCurrencyType.getSelectedItem();
        if (currencyType == null || currencyType.getName().equals("AZN")) {
            jtxtPostavsikSummaCurrency.setEnabled(false);
            jtxtPostavsikSummaCurrency.setText(jtxtPostavsikSummaAZN.getText());
        } else {
//        jtxtPostavsikSummaCurrency.setEnabled(true);
        }
    }//GEN-LAST:event_jtxtPostavsikSummaAZNKeyReleased

    private void refreshCodeValuesTable() {
        objectTableModel.removeAll();

//        CodeType selectedCodeType = (CodeType) jCbCodeType.getSelectedItem();

//        boolean isPostavsikOrClient = selectedCodeType.equals(CodeType.POSTAVSIK)
//                || selectedCodeType.equals(CodeType.CLIENTS);

//        if (!isPostavsikOrClient) {
        CodeType codeType = (CodeType) jCbCodeType.getSelectedItem();
        ArrayList<CodeValue> codeValues = Inits.getSelectQueries().getCodeValues(codeType);
        for (CodeValue codeValue : codeValues) {
            objectTableModel.addObject(codeValue);
        }
//        } else {
//            CodeType codeType = (CodeType) jCbCodeType.getSelectedItem();
//            ArrayList<PostavsikHesab> postavsikHesabs = Inits.getSelectQueries().getPostavsikHesabs(codeType, null);
//            for (PostavsikHesab postavsikHesab : postavsikHesabs) {
//                objectTableModel.addObject(postavsikHesab);
//            }
//        }
    }

    private void clearParameters() {
        jTxtCodeName.setText("");
        jTxtDescription.setText("");
        jtxtPostavsikSummaAZN.setText("");
        jtxtPostavsikSummaCurrency.setText("");
        jdtFrom.setDate(new Date());
    }
    // End of variables declaration//GEN-END:variables
}
