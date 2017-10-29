package az.store.product;

import az.store.Inits;
import az.store.main.CodeValueDialog;
import az.store.types.CodeType;
import az.store.types.CodeValue;
import az.util.components.document.JTextFieldOnlyNumber;
import az.util.utils.Utils;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ItemListener;
import java.util.ArrayList;

/**
 * @author Rashad Amirjanov
 */
public class ProductEditDialog extends javax.swing.JDialog {

    private static ProductEditDialog productEditDialog = null;
    private Product product;
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jBtnClose;
    private javax.swing.JButton jBtnSave;
    private javax.swing.JPanel jPnlClose;
    private javax.swing.JPanel jPnlParameters;
    private javax.swing.JComboBox jcbMeasures;
    private javax.swing.JComboBox jcbProducers;
    private javax.swing.JComboBox jcbProductTypes;
    private javax.swing.JComboBox jcbSourceTypes;
    private javax.swing.JLabel jlbProducers;
    private javax.swing.JLabel jlblMeasure;
    private javax.swing.JLabel jlblPriceBuy;
    private javax.swing.JLabel jlblPriceSale;
    private javax.swing.JLabel jlblProductCode;
    private javax.swing.JLabel jlblProductName;
    private javax.swing.JLabel jlblProductType;
    private javax.swing.JTextField jtxtPriceBuy;
    private javax.swing.JTextField jtxtPriceSale;
    private javax.swing.JTextField jtxtProductCode;
    private javax.swing.JTextField jtxtProductName;

    /**
     * Creates new form ProductEditDialog
     */
    public ProductEditDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();

        setSize(new Dimension(450, 260));
        setLocationRelativeTo(null);

        jtxtPriceBuy.setDocument(new JTextFieldOnlyNumber());
        jtxtPriceSale.setDocument(new JTextFieldOnlyNumber());
        jtxtProductCode.setDocument(new JTextFieldOnlyNumber());

//        if (Inits.isAppTypeProduction()) {
//            jlblPriceSale.setVisible(false);
//            jtxtPriceSale.setVisible(false);
////            jtblProducts.removeColumn(jtblProducts.getColumnModel().getColumn(5));
//        } else {
//            jlbProducers.setVisible(false);
//            jcbProducers.setVisible(false);
//        }
        ItemListener cbItemListener = new java.awt.event.ItemListener() {
            @Override
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                JComboBox cb = (JComboBox) evt.getSource();

                if (cb == null || cb.getSelectedItem() == null) {
                    return;
                }

                CodeValue selCodeValue = (CodeValue) cb.getSelectedItem();
                if (selCodeValue.isValid() == false && selCodeValue.getId() == -1) {
                    CodeValueDialog.instance().showForm(selCodeValue.getCodeType());

                    if (cb == jcbProductTypes) {
                        refreshProductTypes();
                    } else if (cb == jcbMeasures) {
                        refreshMeasures();
                    } else if (cb == jcbProducers) {
                        refreshProducers();
                    }
                }
            }
        };

        jcbProductTypes.addItemListener(cbItemListener);
        jcbMeasures.addItemListener(cbItemListener);
        jcbProducers.addItemListener(cbItemListener);

        jtxtPriceBuy.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                changed();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                changed();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                changed();
            }

            void changed() {
                jtxtPriceSale.setText(jtxtPriceBuy.getText());
            }
        });

        jcbSourceTypes.setVisible(false);
    }

    public static ProductEditDialog instance() {
        if (productEditDialog == null) {
            productEditDialog = new ProductEditDialog(null, true);
        }
        return productEditDialog;
    }

    public Product showForm(Product editProduct, CodeValue sourceType) {
        product = editProduct;

        clearParameters();
        refreshProductTypes();
        refreshMeasures();
        refreshProducers();
        refreshSourceTypes();

        setTitle("Yeni məhsul");

        if (product != null) {
            setTitle("Məhsulun məlumatlarının dəyişdirilməsi");

            jcbProductTypes.setSelectedItem(product.getType());
            jcbMeasures.setSelectedItem(product.getMeasure());
            jcbProducers.setSelectedItem(product.getProducer());
            jcbSourceTypes.setSelectedItem(product.getSourceType());

            jtxtProductCode.setText(product.getCode());
            jtxtProductName.setText(product.getName());
            jtxtPriceBuy.setText(Utils.toString(product.getPriceBuy()));
            jtxtPriceSale.setText(Utils.toString(product.getPriceSale()));
        } else {
            jcbSourceTypes.setSelectedItem(sourceType);
        }

        if (sourceType != null) {
            jtxtPriceBuy.setEnabled(!sourceType.equals(CodeValue.PST_PRODUCTION_PRODUCT));
            jtxtPriceSale.setEnabled(!(Inits.isAppTypeProduction() && sourceType.equals(CodeValue.PST_SIMPLE_PRODUCT)));
        }

        this.setVisible(true);

        return product;
    }

    private void refreshProductTypes() {
        jcbProductTypes.removeAllItems();

        ArrayList<CodeValue> codeValuesProductType = Inits.getSelectQueries().getCodeValues(CodeType.PRODUCT_TYPE);
        for (CodeValue codeValue : codeValuesProductType) {
            jcbProductTypes.addItem(codeValue);
        }

        CodeValue codeValueNewProductType = new CodeValue();
        codeValueNewProductType.setId(-1);
        codeValueNewProductType.setName("  [ Yeni məhsul tipi əlavə et... ]");
        codeValueNewProductType.setCodeType(CodeType.PRODUCT_TYPE);
        codeValueNewProductType.setValid(false);
        jcbProductTypes.addItem(codeValueNewProductType);
    }

    private void refreshMeasures() {
        jcbMeasures.removeAllItems();

        ArrayList<CodeValue> codeValuesMeasureUnit = Inits.getSelectQueries().getCodeValues(CodeType.PRODUCT_MEASURE_TYPE);
        for (CodeValue codeValue : codeValuesMeasureUnit) {
            jcbMeasures.addItem(codeValue);
        }

        CodeValue codeValueNewMeasureUnit = new CodeValue();
        codeValueNewMeasureUnit.setId(-1);
        codeValueNewMeasureUnit.setName("  [ Yeni ölçü vahidi əlavə et... ]");
        codeValueNewMeasureUnit.setCodeType(CodeType.PRODUCT_MEASURE_TYPE);
        codeValueNewMeasureUnit.setValid(false);
        jcbMeasures.addItem(codeValueNewMeasureUnit);
    }

    private void refreshProducers() {
        jcbProducers.removeAllItems();

        ArrayList<CodeValue> codeValuesProducers = Inits.getSelectQueries().getCodeValues(CodeType.PRODUCERS);
        for (CodeValue codeValue : codeValuesProducers) {
            jcbProducers.addItem(codeValue);
        }

        CodeValue codeValueNewProducer = new CodeValue();
        codeValueNewProducer.setId(-1);
        codeValueNewProducer.setName("  [ Yeni istehsalçı əlavə et... ]");
        codeValueNewProducer.setCodeType(CodeType.PRODUCERS);
        codeValueNewProducer.setValid(false);
        jcbProducers.addItem(codeValueNewProducer);
    }

    private void refreshSourceTypes() {
        jcbSourceTypes.removeAllItems();

        ArrayList<CodeValue> codeValuesProducers = Inits.getSelectQueries().getCodeValues(CodeType.PRODUCT_SOURCE_TYPE);
        for (CodeValue codeValue : codeValuesProducers) {
            jcbSourceTypes.addItem(codeValue);
        }
    }

    private void clearParameters() {
        jcbProductTypes.setSelectedItem(null);
        jcbMeasures.setSelectedItem(null);
        jcbProducers.setSelectedItem(null);

        jtxtProductName.setText("");
        jtxtProductCode.setText("");
        jtxtPriceBuy.setText(Utils.toString(0));
        jtxtPriceSale.setText(Utils.toString(0));
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPnlParameters = new javax.swing.JPanel();
        jlblProductType = new javax.swing.JLabel();
        jcbProductTypes = new javax.swing.JComboBox();
        jlblMeasure = new javax.swing.JLabel();
        jcbMeasures = new javax.swing.JComboBox();
        jlbProducers = new javax.swing.JLabel();
        jcbProducers = new javax.swing.JComboBox();
        jlblProductCode = new javax.swing.JLabel();
        jtxtProductCode = new javax.swing.JTextField();
        jlblProductName = new javax.swing.JLabel();
        jtxtProductName = new javax.swing.JTextField();
        jlblPriceBuy = new javax.swing.JLabel();
        jtxtPriceBuy = new javax.swing.JTextField();
        jlblPriceSale = new javax.swing.JLabel();
        jtxtPriceSale = new javax.swing.JTextField();
        jPnlClose = new javax.swing.JPanel();
        jBtnSave = new javax.swing.JButton();
        jBtnClose = new javax.swing.JButton();
        jcbSourceTypes = new javax.swing.JComboBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new javax.swing.BoxLayout(getContentPane(), javax.swing.BoxLayout.PAGE_AXIS));

        jPnlParameters.setBorder(javax.swing.BorderFactory.createTitledBorder("Məhsulun məlumatları"));
        jPnlParameters.setLayout(new java.awt.GridLayout(7, 2, 5, 5));

        jlblProductType.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jlblProductType.setText("Məhsulun tipi");
        jPnlParameters.add(jlblProductType);

        jcbProductTypes.setModel(new javax.swing.DefaultComboBoxModel(new String[]{"Item 1", "Item 2", "Item 3", "Item 4"}));
        jPnlParameters.add(jcbProductTypes);

        jlblMeasure.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jlblMeasure.setText("Məhsulun ölçü vahidi");
        jPnlParameters.add(jlblMeasure);

        jcbMeasures.setModel(new javax.swing.DefaultComboBoxModel(new String[]{"Item 1", "Item 2", "Item 3", "Item 4"}));
        jPnlParameters.add(jcbMeasures);

        jlbProducers.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jlbProducers.setText("İstehsalçı");
        jPnlParameters.add(jlbProducers);

        jcbProducers.setModel(new javax.swing.DefaultComboBoxModel(new String[]{"Item 1", "Item 2", "Item 3", "Item 4"}));
        jPnlParameters.add(jcbProducers);

        jlblProductCode.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jlblProductCode.setText("Məhsulun kodu");
        jPnlParameters.add(jlblProductCode);
        jPnlParameters.add(jtxtProductCode);

        jlblProductName.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jlblProductName.setText("Məhsulun adı");
        jPnlParameters.add(jlblProductName);
        jPnlParameters.add(jtxtProductName);

        jlblPriceBuy.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jlblPriceBuy.setText("Alış qiyməti");
        jPnlParameters.add(jlblPriceBuy);
        jPnlParameters.add(jtxtPriceBuy);

        jlblPriceSale.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jlblPriceSale.setText("Satış qiyməti");
        jPnlParameters.add(jlblPriceSale);
        jPnlParameters.add(jtxtPriceSale);

        getContentPane().add(jPnlParameters);

        jBtnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/az/store/images/ok.png"))); // NOI18N
        jBtnSave.setText("Yadda saxla");
        jBtnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnSaveActionPerformed(evt);
            }
        });
        jPnlClose.add(jBtnSave);

        jBtnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/az/store/images/close.png"))); // NOI18N
        jBtnClose.setText("İmtina");
        jBtnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnCloseActionPerformed(evt);
            }
        });
        jPnlClose.add(jBtnClose);

        jcbSourceTypes.setModel(new javax.swing.DefaultComboBoxModel(new String[]{"Item 1", "Item 2", "Item 3", "Item 4"}));
        jPnlClose.add(jcbSourceTypes);

        getContentPane().add(jPnlClose);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jBtnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnCloseActionPerformed
        setVisible(false);
    }//GEN-LAST:event_jBtnCloseActionPerformed

    private void jBtnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnSaveActionPerformed
        if (jtxtProductCode.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Xahiş olunur, Məhsulun kodunu daxil edin!");
            return;
        }
        if (jtxtProductName.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Xahiş olunur, Məhsulun adını daxil edin!");
            return;
        }

        if (product == null) {
            product = new Product();
        }

        product.setType((CodeValue) jcbProductTypes.getSelectedItem());
        product.setMeasure((CodeValue) jcbMeasures.getSelectedItem());
        product.setSourceType((CodeValue) jcbSourceTypes.getSelectedItem());
        product.setName(jtxtProductName.getText());
        product.setCode(jtxtProductCode.getText());
        product.setProducer((CodeValue) jcbProducers.getSelectedItem());
        try {
            product.setPriceBuy(Double.valueOf(jtxtPriceBuy.getText()));
        } catch (NumberFormatException ex) {
        }
        try {
            product.setPriceSale(Double.valueOf(jtxtPriceSale.getText()));
        } catch (NumberFormatException ex) {
        }

        try {
            if (product.getId() == null || product.getId() == 0) {
                product.setId(Inits.getInsertQueries().insertProduct(product));
            } else {
                Inits.getUpdateQueries().updateProduct(product);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Səhv baş verdi: \n" + ex.getMessage());
            return;
        }

        clearParameters();
        setVisible(false);
    }//GEN-LAST:event_jBtnSaveActionPerformed
    // End of variables declaration//GEN-END:variables
}
