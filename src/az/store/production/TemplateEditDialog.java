package az.store.production;

import az.store.Inits;
import az.store.main.CodeValueDialog;
import az.store.product.Product;
import az.store.product.ProductSelectionListener;
import az.store.types.CodeType;
import az.store.types.CodeValue;
import az.util.components.ObjectTableModel;
import az.util.utils.Utils;
import java.awt.Color;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

/**
 *
 * @author Rashad Amirjanov
 */
public class TemplateEditDialog extends javax.swing.JDialog {

    private ArrayList<Expenditure> deletedProductExpenditures = new ArrayList<>();

    private ObjectTableModel tmExpendituresAsCodeValue = new ObjectTableModel(new String[]{"Xammal və ya xərc",
        "Xərc (AZN-lə)", "Qeyd"}) {

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            Expenditure expenditure = (Expenditure) this.getRowObjects().get(rowIndex);

            Object result = "";
            switch (columnIndex) {
                case 0:
                    result = expenditure.toString();
                    break;
                case 1:
                    result = expenditure.getPrice();
                    break;
                case 2:
                    result = expenditure.getDescription();
                    break;
            }

            if (result == null) {
                result = "";
            }

            return result;
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return (columnIndex == 1 || columnIndex == 2);
        }

        @Override
        public void setValueAt(Object value, int row, int col) {
            Expenditure expenditure = (Expenditure) this.getRowObjects().get(row);

            switch (col) {
                case 1:
                    expenditure.setPrice((double) value);
                    break;
                case 2:
                    expenditure.setDescription((String) value);
                    break;
            }
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
                    result = Double.class;
                    break;
                case 2:
                    result = String.class;
                    break;
            }
            return result;
        }

    };
    private ObjectTableModel tmExpendituresAsProduct = new ObjectTableModel(new String[]{"Sadə məhsul",
        "Sayı", "Qeyd", "Qiyməti", "Anbarda sayı"}) {

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            Expenditure expenditure = (Expenditure) this.getRowObjects().get(rowIndex);

            Object result = "";
            switch (columnIndex) {
                case 0:
                    result = expenditure.toString();
                    break;
                case 1:
                    result = expenditure.getPrice();
                    break;
                case 2:
                    result = expenditure.getDescription();
                    break;
                case 3:
                    result = expenditure.getObjectAsProduct().getPriceBuy();
                    break;
                case 4:
                    result = expenditure.getObjectAsProduct().getCount();
                    break;
            }

            if (result == null) {
                result = "";
            }

            return result;
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return (columnIndex == 1 || columnIndex == 2);
        }

        public void setValueAt(Object value, int row, int col) {
            Expenditure expenditure = (Expenditure) this.getRowObjects().get(row);

            switch (col) {
                case 1:
                    expenditure.setPrice((double) value);
                    break;
                case 2:
                    expenditure.setDescription((String) value);
                    break;
            }
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
                    result = Double.class;
                    break;
                case 2:
                    result = String.class;
                    break;
                case 3:
                    result = String.class;
                    break;
                case 4:
                    result = String.class;
                    break;
            }
            return result;
        }

    };

    /**
     * Creates new form TemplateEditDialog
     */
    public TemplateEditDialog(java.awt.Dialog parent, boolean modal) {
        super(parent, modal);
        initComponents();

        jtblExpendituresAsCodeValue.setModel(tmExpendituresAsCodeValue);
        jtblExpendituresAsProduct.setModel(tmExpendituresAsProduct);

        TableModelListener tableModelListener = new TableModelListener() {
            public void tableChanged(TableModelEvent e) {
                recalculateProductionPrice();
            }
        };

        tmExpendituresAsCodeValue.addTableModelListener(tableModelListener);
        tmExpendituresAsProduct.addTableModelListener(tableModelListener);

        jspnrProductionCount.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                recalculateProductionPrice();
            }
        });

        jtxtProductionPrice.setEditable(false);
        jtxtTotalProductionPrice.setEditable(false);

        jtxtProductionPrice.setBackground(Color.lightGray);
        jtxtTotalProductionPrice.setBackground(Color.lightGray);

        pspProductionProduct.getProductSourceTypesComboBox().setVisible(false);
        pspProductionProduct.getProductTypesComboBox().setVisible(false);
        pspProductionProduct.getProductTypesLabel().setVisible(false);
        pspProductionProduct.addProductSelectionListener(new ProductSelectionListener() {
            @Override
            public void productSourceTypeChanged() {
            }

            @Override
            public void productTypeChanged() {
            }

            @Override
            public void productSelected() {
                jspnrProductionCount.setModel(new SpinnerNumberModel(0, 0, 0, 1));
                tmExpendituresAsCodeValue.removeAll();
                tmExpendituresAsProduct.removeAll();

                if (pspProductionProduct.getSelectedProduct() == null) {
                    return;
                }

                Product productionProduct = pspProductionProduct.getSelectedProduct();
                if (productionProduct == null) {
                    return;
                }

                refreshExpenditures(productionProduct.getId());
                productionProduct.setExpenditures(Inits.getSelectQueries().getProductExpenditures(productionProduct.getId()));

                int min = 0;
                for (int i = 0; i < tmExpendituresAsProduct.getRowCount(); i++) {
                    Expenditure exp = (Expenditure) tmExpendituresAsProduct.getObjectAt(i);
                    if (i == 0) {
                        min = (int) (exp.getObjectAsProduct().getCount() / exp.getPrice());
                        Product p = exp.getObjectAsProduct();
                        if (!p.isValid()) {
                            JOptionPane.showMessageDialog(null, "<html><b>"
                                    + p.toString() + "</b>\n"
                                    + "Xahiş olunur, şablonda bu məhsulu başqası ilə əvəzləyin, ya da silin.\n"
                                    + "Əks halda səhv baş verəcək.", "Sadə məhsul silinib", JOptionPane.WARNING_MESSAGE);
                            System.out.println("p is deleted: " + p.getId() + p.getName());
                        }
                        continue;
                    }
                    min = Math.min(min, (int) (exp.getObjectAsProduct().getCount() / exp.getPrice()));
                }

                jspnrProductionCount.setModel(new SpinnerNumberModel(0, 0, min, 1));
            }
        });

        pspSimpleProduct.getProductSourceTypesComboBox().setVisible(false);
        pspSimpleProduct.getProductTypesComboBox().setVisible(false);
        pspSimpleProduct.getProductTypesLabel().setVisible(false);
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
        pspProductionProduct = new az.store.product.ProductSelectPanel();
        jpnlExpendituresAsProduct = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jtblExpendituresAsProduct = new javax.swing.JTable();
        jpnlSimpleProducts = new javax.swing.JPanel();
        pspSimpleProduct = new az.store.product.ProductSelectPanel();
        jbtnAddSimpleProduct = new javax.swing.JButton();
        jbtnDelSimpleProduct = new javax.swing.JButton();
        jpnlExpendituresAsCodeValue = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jtblExpendituresAsCodeValue = new javax.swing.JTable();
        jpnlExpenditures = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jcbExpenditures = new javax.swing.JComboBox();
        jbtnAddExpenditure = new javax.swing.JButton();
        jbtnDelExpenditure = new javax.swing.JButton();
        jpnlInfo = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jtxtProductionPrice = new javax.swing.JTextField();
        jpnlInfoProduction = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jspnrProductionCount = new javax.swing.JSpinner();
        jLabel1 = new javax.swing.JLabel();
        jtxtTotalProductionPrice = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jbtnOkProduction = new javax.swing.JButton();
        jbtnSaveTemplate = new javax.swing.JButton();
        jbtnCancel = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPnlParameters.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "İstehsal məhsulu", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N
        jPnlParameters.setLayout(new javax.swing.BoxLayout(jPnlParameters, javax.swing.BoxLayout.PAGE_AXIS));
        jPnlParameters.add(pspProductionProduct);

        jpnlExpendituresAsProduct.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Sadə məshullar", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N

        jtblExpendituresAsProduct.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane2.setViewportView(jtblExpendituresAsProduct);

        jpnlSimpleProducts.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 2, 2));
        jpnlSimpleProducts.add(pspSimpleProduct);

        jbtnAddSimpleProduct.setIcon(new javax.swing.ImageIcon(getClass().getResource("/az/store/images/add.png"))); // NOI18N
        jbtnAddSimpleProduct.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnAddSimpleProductActionPerformed(evt);
            }
        });
        jpnlSimpleProducts.add(jbtnAddSimpleProduct);

        jbtnDelSimpleProduct.setIcon(new javax.swing.ImageIcon(getClass().getResource("/az/store/images/minus.png"))); // NOI18N
        jbtnDelSimpleProduct.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnDelSimpleProductActionPerformed(evt);
            }
        });
        jpnlSimpleProducts.add(jbtnDelSimpleProduct);

        javax.swing.GroupLayout jpnlExpendituresAsProductLayout = new javax.swing.GroupLayout(jpnlExpendituresAsProduct);
        jpnlExpendituresAsProduct.setLayout(jpnlExpendituresAsProductLayout);
        jpnlExpendituresAsProductLayout.setHorizontalGroup(
            jpnlExpendituresAsProductLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2)
            .addComponent(jpnlSimpleProducts, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jpnlExpendituresAsProductLayout.setVerticalGroup(
            jpnlExpendituresAsProductLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpnlExpendituresAsProductLayout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpnlSimpleProducts, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jpnlExpendituresAsCodeValue.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Xammallar və xərclər", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N

        jtblExpendituresAsCodeValue.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane1.setViewportView(jtblExpendituresAsCodeValue);

        jpnlExpenditures.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 2, 2));

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel2.setText("Xərc");
        jLabel2.setPreferredSize(new java.awt.Dimension(100, 14));
        jpnlExpenditures.add(jLabel2);

        jcbExpenditures.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jcbExpenditures.setPreferredSize(new java.awt.Dimension(300, 20));
        jcbExpenditures.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jcbExpendituresItemStateChanged(evt);
            }
        });
        jpnlExpenditures.add(jcbExpenditures);

        jbtnAddExpenditure.setIcon(new javax.swing.ImageIcon(getClass().getResource("/az/store/images/add.png"))); // NOI18N
        jbtnAddExpenditure.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnAddExpenditureActionPerformed(evt);
            }
        });
        jpnlExpenditures.add(jbtnAddExpenditure);

        jbtnDelExpenditure.setIcon(new javax.swing.ImageIcon(getClass().getResource("/az/store/images/minus.png"))); // NOI18N
        jbtnDelExpenditure.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnDelExpenditureActionPerformed(evt);
            }
        });
        jpnlExpenditures.add(jbtnDelExpenditure);

        javax.swing.GroupLayout jpnlExpendituresAsCodeValueLayout = new javax.swing.GroupLayout(jpnlExpendituresAsCodeValue);
        jpnlExpendituresAsCodeValue.setLayout(jpnlExpendituresAsCodeValueLayout);
        jpnlExpendituresAsCodeValueLayout.setHorizontalGroup(
            jpnlExpendituresAsCodeValueLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
            .addComponent(jpnlExpenditures, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jpnlExpendituresAsCodeValueLayout.setVerticalGroup(
            jpnlExpendituresAsCodeValueLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpnlExpendituresAsCodeValueLayout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 198, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpnlExpenditures, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jpnlInfo.setBorder(javax.swing.BorderFactory.createTitledBorder("İstehsal qiymətləri"));
        jpnlInfo.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 20, 5));

        jLabel9.setText("İstehsal qiyməti");
        jpnlInfo.add(jLabel9);

        jtxtProductionPrice.setPreferredSize(new java.awt.Dimension(80, 20));
        jpnlInfo.add(jtxtProductionPrice);

        jpnlInfoProduction.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 10, 5));

        jLabel10.setText("İstehsal sayı");
        jpnlInfoProduction.add(jLabel10);

        jspnrProductionCount.setPreferredSize(new java.awt.Dimension(50, 20));
        jpnlInfoProduction.add(jspnrProductionCount);

        jLabel1.setText("Toplam istehsal qiyməti");
        jpnlInfoProduction.add(jLabel1);

        jtxtTotalProductionPrice.setPreferredSize(new java.awt.Dimension(80, 20));
        jpnlInfoProduction.add(jtxtTotalProductionPrice);

        jpnlInfo.add(jpnlInfoProduction);

        jbtnOkProduction.setText("İstehsal et");
        jbtnOkProduction.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnOkProductionActionPerformed(evt);
            }
        });
        jPanel2.add(jbtnOkProduction);

        jbtnSaveTemplate.setText("Ok");
        jbtnSaveTemplate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnSaveTemplateActionPerformed(evt);
            }
        });
        jPanel2.add(jbtnSaveTemplate);

        jbtnCancel.setText("İmtina");
        jbtnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnCancelActionPerformed(evt);
            }
        });
        jPanel2.add(jbtnCancel);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
            .addComponent(jpnlExpendituresAsProduct, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jpnlExpendituresAsCodeValue, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jpnlInfo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPnlParameters, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPnlParameters, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpnlExpendituresAsProduct, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpnlExpendituresAsCodeValue, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpnlInfo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    public void showForm(Product productionProduct, boolean template) {
        deletedProductExpenditures.clear();
        refreshExpenditures(productionProduct != null ? productionProduct.getId() : 0);
        refreshExpendituresCodeValues();
        pspProductionProduct.init();

        pspSimpleProduct.init();
        pspSimpleProduct.setSelectedProductSourceType(CodeValue.PST_SIMPLE_PRODUCT);

        setLocationRelativeTo(null);
        if (template) {
            setTitle(productionProduct != null ? "Şablonun dəyişdirilməsi" : "Yeni şablonun daxil edilməsi");
        } else {
            setTitle("Məhsulun istehsal olunması");
        }

        pspProductionProduct.setSelectedProduct(productionProduct);
        pspProductionProduct.setEnabled(productionProduct == null);

        jpnlSimpleProducts.setVisible(template);
        jpnlExpenditures.setVisible(template);
        jbtnSaveTemplate.setVisible(template);

        jpnlInfoProduction.setVisible(!template);
        jbtnOkProduction.setVisible(!template);

        this.setVisible(true);
    }

    private void refreshExpenditures(int productId) {
        tmExpendituresAsCodeValue.removeAll();
        tmExpendituresAsProduct.removeAll();

        ArrayList<Expenditure> expenditures = Inits.getSelectQueries().getProductExpenditures(productId);
        for (Expenditure expenditure : expenditures) {
            if (expenditure.isCodeValue()) {
                tmExpendituresAsCodeValue.addObject(expenditure);
            } else {
                tmExpendituresAsProduct.addObject(expenditure);
            }
        }
    }

    private void refreshExpendituresCodeValues() {
        jcbExpenditures.removeAllItems();
        ArrayList<CodeValue> allExpenditures = Inits.getSelectQueries().getCodeValues(CodeType.EXPENDITURES);
        for (CodeValue expenditure : allExpenditures) {
            jcbExpenditures.addItem(expenditure);
        }

        CodeValue codeValueNewExpenditure = new CodeValue();
        codeValueNewExpenditure.setId(-1);
        codeValueNewExpenditure.setName("  [ Yeni xərc əlavə et... ]");
        codeValueNewExpenditure.setCodeType(CodeType.EXPENDITURES);
        codeValueNewExpenditure.setValid(false);
        jcbExpenditures.addItem(codeValueNewExpenditure);
    }

    private void recalculateProductionPrice() {
        double productionPrice = 0;
        for (int i = 0; i < tmExpendituresAsProduct.getRowCount(); i++) {
            Expenditure exp = (Expenditure) tmExpendituresAsProduct.getObjectAt(i);
            double count = exp.getPrice();
            double price = exp.getObjectAsProduct().getPriceBuy();
            productionPrice += count * price;
        }
        for (int i = 0; i < tmExpendituresAsCodeValue.getRowCount(); i++) {
            productionPrice += ((Expenditure) tmExpendituresAsCodeValue.getObjectAt(i)).getPrice();
        }
        double totalProductionPrice = productionPrice * (int) jspnrProductionCount.getValue();
        jtxtProductionPrice.setText(Utils.toString(productionPrice));
        jtxtTotalProductionPrice.setText(Utils.toString(totalProductionPrice));
    }

    private void jcbExpendituresItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jcbExpendituresItemStateChanged
        if (jcbExpenditures.getSelectedItem() == null) {
            return;
        }

        CodeValue selCodeValue = (CodeValue) jcbExpenditures.getSelectedItem();
        if (selCodeValue.isValid() == false && selCodeValue.getId() == -1) {
            CodeValueDialog.instance().showForm(selCodeValue.getCodeType());

            refreshExpendituresCodeValues();
        } else {
//            jtxtPrice.setText();
        }
    }//GEN-LAST:event_jcbExpendituresItemStateChanged

    private void jbtnAddExpenditureActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnAddExpenditureActionPerformed
        Expenditure newExpenditure = new Expenditure();
        newExpenditure.setObject(jcbExpenditures.getSelectedItem());

        if (tmExpendituresAsCodeValue.getRowObjects().contains(newExpenditure)) {
            JOptionPane.showMessageDialog(this, "Bu xərc artıq siyahıda var!");
            return;
        }

        tmExpendituresAsCodeValue.addObject(newExpenditure);
    }//GEN-LAST:event_jbtnAddExpenditureActionPerformed

    private void jbtnDelExpenditureActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnDelExpenditureActionPerformed
        if (jtblExpendituresAsCodeValue.getSelectedRow() == -1) {
            return;
        }

        Product productionProduct = pspProductionProduct.getSelectedProduct();
        if (productionProduct != null) {
            Expenditure expenditure = (Expenditure) tmExpendituresAsCodeValue.getObjectAt(jtblExpendituresAsCodeValue.getSelectedRow());
            if (expenditure != null && productionProduct.getExpenditures().contains(expenditure)) {
                deletedProductExpenditures.add(expenditure);
            }
        }

        tmExpendituresAsCodeValue.removeRow(jtblExpendituresAsCodeValue.getSelectedRow());
    }//GEN-LAST:event_jbtnDelExpenditureActionPerformed

    private void jbtnSaveTemplateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnSaveTemplateActionPerformed
        if (jtblExpendituresAsCodeValue.isEditing()) {
            jtblExpendituresAsCodeValue.getCellEditor().stopCellEditing();
        }
        if (jtblExpendituresAsProduct.isEditing()) {
            jtblExpendituresAsProduct.getCellEditor().stopCellEditing();
        }

        Product productionProduct = pspProductionProduct.getSelectedProduct();
        if (productionProduct == null) {
            JOptionPane.showMessageDialog(this, "Xahiş olunur istehsal məhsulunu seçəsiniz!");
            return;
        }
        if (tmExpendituresAsProduct.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Xahiş olunur sadə məhsulu seçəsiniz!");
            return;
        }

        Inits.getDb().setAutoCommit(false);

        try {
            //delete template's deleted expenditures
            for (Expenditure expenditure : deletedProductExpenditures) {
                Logger.getGlobal().log(Level.INFO, "delete exp: pid={0}, eid={1}",
                        new Object[]{productionProduct.getId(), expenditure.getObject()});
                Inits.getDeleteQueries().deleteProductExpenditure(productionProduct.getId(), expenditure);
            }

            //insert new product expenditures or update existings
            for (Object obj : tmExpendituresAsProduct.getRowObjects()) {
                Expenditure expenditure = (Expenditure) obj;
                Product p = Inits.getSelectQueries().getProduct(expenditure.getObjectAsProduct().getId());
                if (p != null && p.isValid()) {
                    Inits.getUpdateQueries().upsertProductExpenditure(productionProduct.getId(), expenditure);
                } else {
                    JOptionPane.showMessageDialog(null, "<html><b>"
                            + expenditure.getObjectAsProduct().toString() + "</b>\n"
                            + "Xahiş olunur, şablonda bu məhsulu başqası ilə əvəzləyin, ya da silin.\n"
                            + "Əks halda səhv baş verəcək.", "Sadə məhsul silinib", JOptionPane.WARNING_MESSAGE);
                    throw new Exception("Şablondakı sadə məhsul silinib: " + expenditure.getObjectAsProduct().toString());
                }
            }

            //insert new codevalue expenditures or update existings
            for (Object obj : tmExpendituresAsCodeValue.getRowObjects()) {
                Expenditure expenditure = (Expenditure) obj;
                Inits.getUpdateQueries().upsertProductExpenditure(productionProduct.getId(), expenditure);
            }

            Inits.getDb().commit();
        } catch (Exception ex) {
            Inits.getDb().rollback();
            JOptionPane.showMessageDialog(this, "Şablonun daxil olunması zamanı səhv baş verdi!");
            Logger.getGlobal().log(Level.SEVERE, null, ex);
            return;
        }

        Inits.getDb().setAutoCommit(true);

        setVisible(false);
    }//GEN-LAST:event_jbtnSaveTemplateActionPerformed

    private void jbtnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnCancelActionPerformed
        setVisible(false);
    }//GEN-LAST:event_jbtnCancelActionPerformed

    private void jbtnAddSimpleProductActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnAddSimpleProductActionPerformed
        Product simpleProduct = pspSimpleProduct.getSelectedProduct();
        if (simpleProduct == null) {
            return;
        }

        Expenditure newExpenditure = new Expenditure();
        newExpenditure.setObject(simpleProduct);
        newExpenditure.setPrice(1);

        if (tmExpendituresAsProduct.getRowObjects().contains(newExpenditure)) {
            JOptionPane.showMessageDialog(this, "Bu sadə məhsul artıq siyahıda var!");
            return;
        }

        tmExpendituresAsProduct.addObject(newExpenditure);
    }//GEN-LAST:event_jbtnAddSimpleProductActionPerformed

    private void jbtnDelSimpleProductActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnDelSimpleProductActionPerformed
        if (jtblExpendituresAsProduct.getSelectedRow() == -1) {
            return;
        }

        Product productionProduct = pspProductionProduct.getSelectedProduct();
        if (productionProduct != null) {
            Expenditure expenditure = (Expenditure) tmExpendituresAsProduct.getObjectAt(jtblExpendituresAsProduct.getSelectedRow());
            if (expenditure != null && productionProduct.getExpenditures().contains(expenditure)) {
                deletedProductExpenditures.add(expenditure);
            }
        }

        tmExpendituresAsProduct.removeRow(jtblExpendituresAsProduct.getSelectedRow());
    }//GEN-LAST:event_jbtnDelSimpleProductActionPerformed

    private void jbtnOkProductionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnOkProductionActionPerformed
        if (tmExpendituresAsProduct.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Xahiş olunur şablon üçün sadə məhsul daxil edəsiniz!");
            return;
        }
        if (tmExpendituresAsCodeValue.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Xahiş olunur şablon üçün xərclər daxil edəsiniz!");
            return;
        }

        Product productionProduct = pspProductionProduct.getSelectedProduct();
        int productionCount = (int) jspnrProductionCount.getValue();
        double productionPrice = Utils.toDouble(jtxtProductionPrice.getText());

        if (productionProduct == null) {
            JOptionPane.showMessageDialog(this, "İstehsal məhsulu boşdur, xahiş olunur şablonu redaktə edin!");
            return;
        }

        if (productionCount == 0 || productionPrice == 0) {
            JOptionPane.showMessageDialog(this, "Xahiş olunur istehsal məlumatlarını tam daxil edəsiniz!");
            return;
        }

        Production production = new Production();
        production.setProductionProduct(productionProduct);
        production.setProductionPrice(productionPrice);
        production.setCount(productionCount);

        Inits.getDb().setAutoCommit(false);

        try {
            production.setId(Inits.getInsertQueries().insertProduction(production));

            ArrayList<Expenditure> expendituresAsProduct = productionProduct.getProductExpenditures();
            for (Expenditure e : expendituresAsProduct) {
                Product p = Inits.getSelectQueries().getProduct(e.getObjectAsProduct().getId());
                if (p != null && p.isValid()) {
                    Inits.getUpdateQueries().increaseProductCount(e.getObjectAsProduct().getId(), -productionCount * e.getPrice());
                } else {
                    JOptionPane.showMessageDialog(null, "<html><b>"
                            + e.getObjectAsProduct().toString() + "</b>\n"
                            + "Xahiş olunur, şablonda bu məhsulu başqası ilə əvəzləyin, ya da silin.\n"
                            + "Əks halda səhv baş verəcək.", "Sadə məhsul silinib", JOptionPane.WARNING_MESSAGE);
                    throw new Exception("Şablondakı sadə məhsul silinib: " + e.getObjectAsProduct().toString());
                }

            }
            Inits.getUpdateQueries().increaseProductCount(productionProduct.getId(), productionCount);
            Inits.getUpdateQueries().updateProductPrice(productionProduct.getId(), productionPrice, null);

            //insert expenditures
            //            for (Object obj : tmExpendituresAsCodeValue.getRowObjects()) {
            //                Expenditure expenditure = (Expenditure) obj;
            //                Inits.getUpdateQueries().upsertProductionExpenditure(production.getId(), expenditure);
            //            }
            Inits.getDb().commit();
        } catch (Exception ex) {
            Inits.getDb().rollback();
            JOptionPane.showMessageDialog(this, "Şablonun daxil olunması zamanı səhv baş verdi!");
            ex.printStackTrace();
            return;
        }

        Inits.getDb().setAutoCommit(true);

        setVisible(false);
    }//GEN-LAST:event_jbtnOkProductionActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPnlParameters;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JButton jbtnAddExpenditure;
    private javax.swing.JButton jbtnAddSimpleProduct;
    private javax.swing.JButton jbtnCancel;
    private javax.swing.JButton jbtnDelExpenditure;
    private javax.swing.JButton jbtnDelSimpleProduct;
    private javax.swing.JButton jbtnOkProduction;
    private javax.swing.JButton jbtnSaveTemplate;
    private javax.swing.JComboBox jcbExpenditures;
    private javax.swing.JPanel jpnlExpenditures;
    private javax.swing.JPanel jpnlExpendituresAsCodeValue;
    private javax.swing.JPanel jpnlExpendituresAsProduct;
    private javax.swing.JPanel jpnlInfo;
    private javax.swing.JPanel jpnlInfoProduction;
    private javax.swing.JPanel jpnlSimpleProducts;
    private javax.swing.JSpinner jspnrProductionCount;
    private javax.swing.JTable jtblExpendituresAsCodeValue;
    private javax.swing.JTable jtblExpendituresAsProduct;
    private javax.swing.JTextField jtxtProductionPrice;
    private javax.swing.JTextField jtxtTotalProductionPrice;
    private az.store.product.ProductSelectPanel pspProductionProduct;
    private az.store.product.ProductSelectPanel pspSimpleProduct;
    // End of variables declaration//GEN-END:variables
}
