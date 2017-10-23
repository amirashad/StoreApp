/*
 * CodeValueDialog.java
 *
 * Created on Jan 8, 2012, 3:06:45 PM
 */
package az.store.client;

import az.store.Inits;
import az.util.components.ObjectTableModel;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.JOptionPane;

/**
 *
 * @author Rashad Amirjanov
 */
public class ClientEditDialog extends javax.swing.JDialog {

    static ClientEditDialog clientEditDialog = null;

    public static ClientEditDialog instance() {
        if (clientEditDialog == null) {
            clientEditDialog = new ClientEditDialog(null, true);
        }
        return clientEditDialog;
    }

    public enum ClientState {
        ADDED, CHANGED, DELETED
    }

    private HashMap<Client, ClientState> changedClientsMap = new HashMap<>();

    private void setClientState(Client client, ClientState state) {
        if (!changedClientsMap.containsKey(client)) {
            changedClientsMap.put(client, state);
        } else {
            changedClientsMap.replace(client, state);
        }
    }

    private ObjectTableModel tmClients = new ObjectTableModel(new String[]{
        "Adı", "Müştəri", "Satış məntəqəsi"}) {

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            Client client = (Client) this.getRowObjects().get(rowIndex);

            Object result = null;
            switch (columnIndex) {
                case 0:
                    result = client.getName();
                    break;
                case 1:
                    result = client.isClient();
                    break;
                case 2:
                    result = client.isProvider();
                    break;
            }
            if (result == null) {
                result = "";
            }

            return result;
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return true;//(columnIndex == 1 || columnIndex == 2);
        }

        @Override
        public void setValueAt(Object value, int row, int col) {
            Client client = (Client) this.getRowObjects().get(row);

            switch (col) {
                case 0:
                    client.setName((String) value);
                    break;
                case 1:
                    client.setClient((Boolean) value);
                    break;
                case 2:
                    client.setProvider((Boolean) value);
                    break;
            }

            setClientState(client, ClientState.CHANGED);

            fireTableDataChanged();
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            Class<?> result = null;

            switch (columnIndex) {
                case 0:
                    result = String.class;
                    break;
                case 1:
                    result = Boolean.class;
                    break;
                case 2:
                    result = Boolean.class;
                    break;
            }

            return result;
        }
    };

    /**
     * Creates new form CodeValueDialog
     */
    public ClientEditDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();

        jtblClients.setModel(tmClients);

        setLocationRelativeTo(null);
    }

    public void showForm(Client selectedClient) {
        refreshClients();
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

        jPnlTable = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jtblClients = new javax.swing.JTable();
        jPnlClose = new javax.swing.JPanel();
        jbtnAddExpenditure = new javax.swing.JButton();
        jbtnDelExpenditure = new javax.swing.JButton();
        jPnlSave = new javax.swing.JPanel();
        jbtnSaveTemplate = new javax.swing.JButton();
        jBtnClose = new javax.swing.JButton();

        setTitle("Müştərilər / Satış məntəqələri");

        jtblClients.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane1.setViewportView(jtblClients);

        javax.swing.GroupLayout jPnlTableLayout = new javax.swing.GroupLayout(jPnlTable);
        jPnlTable.setLayout(jPnlTableLayout);
        jPnlTableLayout.setHorizontalGroup(
            jPnlTableLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPnlTableLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 460, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPnlTableLayout.setVerticalGroup(
            jPnlTableLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPnlTableLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 466, Short.MAX_VALUE)
                .addContainerGap())
        );

        getContentPane().add(jPnlTable, java.awt.BorderLayout.CENTER);

        jbtnAddExpenditure.setIcon(new javax.swing.ImageIcon(getClass().getResource("/az/store/images/add.png"))); // NOI18N
        jbtnAddExpenditure.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnAddExpenditureActionPerformed(evt);
            }
        });
        jPnlClose.add(jbtnAddExpenditure);

        jbtnDelExpenditure.setIcon(new javax.swing.ImageIcon(getClass().getResource("/az/store/images/minus.png"))); // NOI18N
        jbtnDelExpenditure.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnDelExpenditureActionPerformed(evt);
            }
        });
        jPnlClose.add(jbtnDelExpenditure);

        jPnlSave.setMinimumSize(new java.awt.Dimension(100, 10));
        jPnlSave.setPreferredSize(new java.awt.Dimension(100, 10));
        jPnlClose.add(jPnlSave);

        jbtnSaveTemplate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/az/store/images/ok.png"))); // NOI18N
        jbtnSaveTemplate.setText("Yadda saxla");
        jbtnSaveTemplate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnSaveTemplateActionPerformed(evt);
            }
        });
        jPnlClose.add(jbtnSaveTemplate);

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

    private void refreshClients() {
        tmClients.removeAll();
        ArrayList<Client> clients = Inits.getSelectQueries().getClients(null, null, null);
        clients.forEach((client) -> {
            tmClients.addObject(client);
        });
    }


    private void jBtnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnCloseActionPerformed
        setVisible(false);
    }//GEN-LAST:event_jBtnCloseActionPerformed

    private void jbtnAddExpenditureActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnAddExpenditureActionPerformed
        String name = JOptionPane.showInputDialog("Müştəri / Satış məntəqəsinin adı");
        if (name == null || name.trim().isEmpty()) {
            return;
        }

        Client client = new Client();
        client.setName(name);

        if (tmClients.getRowObjects().contains(client)) {
            JOptionPane.showMessageDialog(this, "Bu Müştəri / Satış məntəqəsi artıq siyahıda var!");
            return;
        }

        tmClients.addObject(client);
        setClientState(client, ClientState.ADDED);
    }//GEN-LAST:event_jbtnAddExpenditureActionPerformed

    private void jbtnDelExpenditureActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnDelExpenditureActionPerformed
        if (jtblClients.getSelectedRow() == -1) {
            return;
        }

        String[] choices = {"Bəli", "Xeyr"};
        int response = JOptionPane.showOptionDialog(null, "Həqiqətən silinsinmi?", "Diqqət", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, choices, "Xeyr");
        if (response != JOptionPane.YES_OPTION) {
            return;
        }

        Client client = (Client) tmClients.getObjectAt(jtblClients.getSelectedRow());
        if (client != null) {// && client.getId() > 0
            setClientState(client, ClientState.DELETED);
        }

        tmClients.removeRow(jtblClients.getSelectedRow());
    }//GEN-LAST:event_jbtnDelExpenditureActionPerformed

    private void jbtnSaveTemplateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnSaveTemplateActionPerformed
        if (jtblClients.isEditing()) {
            jtblClients.getCellEditor().stopCellEditing();
        }

        changedClientsMap.forEach((client, state) -> {
            System.out.println(client.toString() + ", " + state);
        });

//        Client productionProduct = pspProductionProduct.getSelectedProduct();
//        if (productionProduct == null) {
//            JOptionPane.showMessageDialog(this, "Xahiş olunur istehsal məhsulunu seçəsiniz!");
//            return;
//        }
//        if (tmExpendituresAsProduct.getRowCount() == 0) {
//            JOptionPane.showMessageDialog(this, "Xahiş olunur sadə məhsulu seçəsiniz!");
//            return;
//        }
//
//        Inits.getDb().setAutoCommit(false);
//
//        try {
//            //delete template's deleted expenditures
//            for (Expenditure expenditure : deletedProductExpenditures) {
//                Logger.getGlobal().log(Level.INFO, "delete exp: pid={0}, eid={1}",
//                        new Object[]{productionProduct.getId(), expenditure.getObject()});
//                Inits.getDeleteQueries().deleteProductExpenditure(productionProduct.getId(), expenditure);
//            }
//
//            //insert new product expenditures or update existings
//            for (Object obj : tmExpendituresAsProduct.getRowObjects()) {
//                Expenditure expenditure = (Expenditure) obj;
//                Product p = Inits.getSelectQueries().getProduct(expenditure.getObjectAsProduct().getId());
//                if (p != null && p.isValid()) {
//                    Inits.getUpdateQueries().upsertProductExpenditure(productionProduct.getId(), expenditure);
//                } else {
//                    JOptionPane.showMessageDialog(null, "<html><b>"
//                            + expenditure.getObjectAsProduct().toString() + "</b>\n"
//                            + "Xahiş olunur, şablonda bu məhsulu başqası ilə əvəzləyin, ya da silin.\n"
//                            + "Əks halda səhv baş verəcək.", "Sadə məhsul silinib", JOptionPane.WARNING_MESSAGE);
//                    throw new Exception("Şablondakı sadə məhsul silinib: " + expenditure.getObjectAsProduct().toString());
//                }
//            }
//
//            //insert new codevalue expenditures or update existings
//            for (Object obj : tmExpendituresAsCodeValue.getRowObjects()) {
//                Expenditure expenditure = (Expenditure) obj;
//                Inits.getUpdateQueries().upsertProductExpenditure(productionProduct.getId(), expenditure);
//            }
//
//            Inits.getDb().commit();
//        } catch (Exception ex) {
//            Inits.getDb().rollback();
//            JOptionPane.showMessageDialog(this, "Şablonun daxil olunması zamanı səhv baş verdi!");
//            Logger.getGlobal().log(Level.SEVERE, null, ex);
//            return;
//        }
//
//        Inits.getDb().setAutoCommit(true);
//
//        setVisible(false);
    }//GEN-LAST:event_jbtnSaveTemplateActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jBtnClose;
    private javax.swing.JPanel jPnlClose;
    private javax.swing.JPanel jPnlSave;
    private javax.swing.JPanel jPnlTable;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton jbtnAddExpenditure;
    private javax.swing.JButton jbtnDelExpenditure;
    private javax.swing.JButton jbtnSaveTemplate;
    private javax.swing.JTable jtblClients;
    // End of variables declaration//GEN-END:variables
}
