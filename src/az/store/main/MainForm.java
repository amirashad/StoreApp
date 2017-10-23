/*
 * MainForm.java
 *
 * Created on Jan 8, 2012, 12:23:52 PM
 */
package az.store.main;

import az.store.product.CurrentStoreSearchPanel;
import az.dev.java.sia.SingleInstanceApplication;
import az.store.Inits;
import az.store.client.ClientEditDialog;
import az.store.invoice.InvoiceDetailedSearchPanel;
import az.store.invoice.InvoiceDialog;
import az.store.invoice.InvoiceSearchPanel;
import az.store.invoice.InvoiceType;
import az.store.payment.PaymentDialog;
import az.store.product.ProductDialog;
import az.store.production.AdvancedProductionSearchPanel;
import az.store.production.ProductionsSearchPanel;
import az.store.production.TemplateEditDialog;
import az.store.report.ReportDialog;
import az.store.types.CodeType;
import az.store.types.CodeValue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

/**
 *
 * @author Rashad Amirjanov
 */
public final class MainForm extends javax.swing.JFrame {

    /**
     * Creates new form MainForm
     */
    public MainForm() {
        initComponents();

        setSize(1200, 600);
        setLocationRelativeTo(null);
        setIconImage(new ImageIcon(getClass().getResource("/az/store/images/store.png")).getImage());
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        ActionListener actionNewCodeValueListener = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                CodeType codeType = new CodeType();
                codeType.setId(Integer.valueOf(e.getActionCommand()));
                CodeValueDialog.instance().showForm(codeType);
            }
        };

        jmenuNewExpenditure.setActionCommand(String.valueOf(CodeType.EXPENDITURES.getId()));
        jmenuNewPostavsik.setActionCommand(String.valueOf(CodeType.POSTAVSIK.getId()));
        jmenuNewProductMeasure.setActionCommand(String.valueOf(CodeType.PRODUCT_MEASURE_TYPE.getId()));
        jmenuNewProductType.setActionCommand(String.valueOf(CodeType.PRODUCT_TYPE.getId()));
        jmenuNewClient.setActionCommand(String.valueOf(CodeType.CLIENTS.getId()));
        jmenuNewWorker.setActionCommand(String.valueOf(CodeType.WORKERS.getId()));
        jmenuNewCurrency.setActionCommand(String.valueOf(CodeType.CURRENCY_TYPE.getId()));

        jmenuNewExpenditure.addActionListener(actionNewCodeValueListener);
        jmenuNewPostavsik.addActionListener(actionNewCodeValueListener);
        jmenuNewProductMeasure.addActionListener(actionNewCodeValueListener);
        jmenuNewProductType.addActionListener(actionNewCodeValueListener);
        jmenuNewClient.addActionListener(actionNewCodeValueListener);
        jmenuNewWorker.addActionListener(actionNewCodeValueListener);
        jmenuNewCurrency.addActionListener(actionNewCodeValueListener);

        refreshInvoiceTypes();

        initProduction();
    }

    public void refreshInvoiceTypes() {
        ActionListener actionNewInvoiceListener = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                JMenuItem item = (JMenuItem) e.getSource();
                InvoiceDialog.instance().showForm(Integer.valueOf(e.getActionCommand()), item.getText(), null);
            }
        };

        ArrayList<InvoiceType> invoiceTypes = Inits.getSelectQueries().getAllInvoiceTypes();
        for (InvoiceType invoiceType : invoiceTypes) {
            JMenuItem jmenuInvoiceType = jTopMenuInvoices.add(new JMenuItem(invoiceType.getDescription()));
            jmenuInvoiceType.setActionCommand(String.valueOf(invoiceType.getId()));
            jmenuInvoiceType.addActionListener(actionNewInvoiceListener);
        }
        jMenuBar1.remove(jTopMenuProducts);
    }

    private void initProduction() {
        if (!Inits.isAppTypeProduction()) {
            return;
        }

        jTopMenuNews.remove(jmenuNewCurrency);
        jTopMenuNews.remove(jmenuNewWorker);

        jTopMenuProducts.remove(jmenuExpenditureAsProduct);
        jTopMenuProducts.remove(jmenuPriceIncrease);
        jTopMenuProducts.remove(jmenuPriceDecrease);
        jTopMenuProducts.remove(jmenuNakladnoyForm2);

        jTopMenuPayments.remove(jmenuPaymentPostavsik);
        jTopMenuPayments.remove(jmenuExpenditureAsMoney);

//        jMenuBar1.remove(jTopMenuPayments);
//        jMenuBar1.remove(jTopMenuReports);
        jTopMenuReports.remove(jmenuPostavsikAndClientReport);
        jMenuBar1.remove(jTopMenuExit);

        jTabbedPane1.addTab("Cari anbar", new CurrentStoreSearchPanel());
        jTabbedPane1.addTab("Məhsullar (Alış/Satış)", new InvoiceDetailedSearchPanel());
        jTabbedPane1.addTab("Qaimələr", new InvoiceSearchPanel());
        jTabbedPane1.addTab("İstehsallar", new ProductionsSearchPanel());
        jTabbedPane1.addTab("Ətraflı istehsalat hesabatı", new AdvancedProductionSearchPanel());

        JMenuItem jmenuNewProducer = new JMenuItem("Yeni İstehsalçı");
        jTopMenuNews.add(jmenuNewProducer);

        jmenuNewProducer.setActionCommand(String.valueOf(CodeType.PRODUCERS.getId()));
        jmenuNewProducer.addActionListener(jmenuNewClient.getActionListeners()[0]);
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
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jMenuBar1 = new javax.swing.JMenuBar();
        jTopMenuNews = new javax.swing.JMenu();
        jmenuNewProduct = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        jmenuNewPostavsik = new javax.swing.JMenuItem();
        jmenuNewClient = new javax.swing.JMenuItem();
        jmenuClients = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JPopupMenu.Separator();
        jmenuNewProductType = new javax.swing.JMenuItem();
        jmenuNewProductMeasure = new javax.swing.JMenuItem();
        jmenuNewExpenditure = new javax.swing.JMenuItem();
        jmenuNewWorker = new javax.swing.JMenuItem();
        jmenuNewCurrency = new javax.swing.JMenuItem();
        jTopMenuProducts = new javax.swing.JMenu();
        jmenuExpenditureAsProduct = new javax.swing.JMenuItem();
        jmenuPriceIncrease = new javax.swing.JMenuItem();
        jmenuPriceDecrease = new javax.swing.JMenuItem();
        jmenuNakladnoy = new javax.swing.JMenuItem();
        jmenuNakladnoyForm2 = new javax.swing.JMenuItem();
        jTopMenuInvoices = new javax.swing.JMenu();
        jTopMenuPayments = new javax.swing.JMenu();
        jmenuPaymentPostavsik = new javax.swing.JMenuItem();
        jmenuPaymentClient = new javax.swing.JMenuItem();
        jmenuExpenditureAsMoney = new javax.swing.JMenuItem();
        jTopMenuProduction = new javax.swing.JMenu();
        jmenuNewTemplate = new javax.swing.JMenuItem();
        jmenuNewProduction = new javax.swing.JMenuItem();
        jTopMenuReports = new javax.swing.JMenu();
        jmenuProductReport = new javax.swing.JMenuItem();
        jmenuPaymentReport = new javax.swing.JMenuItem();
        jmenuPostavsikAndClientReport = new javax.swing.JMenuItem();
        jTopMenuExit = new javax.swing.JMenu();
        jmenuBackup = new javax.swing.JMenuItem();
        jmenuExit = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("ANBAR");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });
        getContentPane().add(jTabbedPane1, java.awt.BorderLayout.CENTER);

        jTopMenuNews.setText("Yenilər");

        jmenuNewProduct.setText("Yeni Məhsul");
        jmenuNewProduct.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmenuNewProductActionPerformed(evt);
            }
        });
        jTopMenuNews.add(jmenuNewProduct);
        jTopMenuNews.add(jSeparator1);

        jmenuNewPostavsik.setText("Yeni Məhsul gətirən");
        jTopMenuNews.add(jmenuNewPostavsik);

        jmenuNewClient.setText("Yeni Satış məntəqəsi");
        jTopMenuNews.add(jmenuNewClient);

        jmenuClients.setText("Müştərilər / Satış məntəqələri");
        jmenuClients.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmenuClientsActionPerformed(evt);
            }
        });
        jTopMenuNews.add(jmenuClients);
        jTopMenuNews.add(jSeparator2);

        jmenuNewProductType.setText("Yeni Məhsul tipi");
        jTopMenuNews.add(jmenuNewProductType);

        jmenuNewProductMeasure.setText("Yeni Ölçü vahidi");
        jTopMenuNews.add(jmenuNewProductMeasure);

        jmenuNewExpenditure.setText("Yeni Xərc tipi");
        jTopMenuNews.add(jmenuNewExpenditure);

        jmenuNewWorker.setText("Yeni İşçi");
        jTopMenuNews.add(jmenuNewWorker);

        jmenuNewCurrency.setText("Yeni Valyuta növü");
        jTopMenuNews.add(jmenuNewCurrency);

        jMenuBar1.add(jTopMenuNews);

        jTopMenuProducts.setText("Məhsullar");

        jmenuExpenditureAsProduct.setText("Xərclər (Məhsul şəklində)");
        jmenuExpenditureAsProduct.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmenuExpenditureAsProductActionPerformed(evt);
            }
        });
        jTopMenuProducts.add(jmenuExpenditureAsProduct);

        jmenuPriceIncrease.setText("Qiymətlərin artması");
        jTopMenuProducts.add(jmenuPriceIncrease);

        jmenuPriceDecrease.setText("Qiymətlərin azalması");
        jTopMenuProducts.add(jmenuPriceDecrease);

        jmenuNakladnoy.setText("Qaimələr");
        jmenuNakladnoy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmenuNakladnoyActionPerformed(evt);
            }
        });
        jTopMenuProducts.add(jmenuNakladnoy);

        jmenuNakladnoyForm2.setText("Qaimələr (Forma 2)");
        jmenuNakladnoyForm2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmenuNakladnoyForm2ActionPerformed(evt);
            }
        });
        jTopMenuProducts.add(jmenuNakladnoyForm2);

        jMenuBar1.add(jTopMenuProducts);

        jTopMenuInvoices.setText("Qaimələr");
        jMenuBar1.add(jTopMenuInvoices);

        jTopMenuPayments.setText("Ödənişlər");

        jmenuPaymentPostavsik.setText("Məhsul gətirənlərə ödənişlər");
        jmenuPaymentPostavsik.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmenuPaymentPostavsikActionPerformed(evt);
            }
        });
        jTopMenuPayments.add(jmenuPaymentPostavsik);

        jmenuPaymentClient.setText("Müştərilərin ödənişləri");
        jmenuPaymentClient.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmenuPaymentClientActionPerformed(evt);
            }
        });
        jTopMenuPayments.add(jmenuPaymentClient);

        jmenuExpenditureAsMoney.setText("Xərclər (Pul şəklində)");
        jmenuExpenditureAsMoney.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmenuExpenditureAsMoneyActionPerformed(evt);
            }
        });
        jTopMenuPayments.add(jmenuExpenditureAsMoney);

        jMenuBar1.add(jTopMenuPayments);

        jTopMenuProduction.setText("İstehsalat");

        jmenuNewTemplate.setText("Şablonlar");
        jmenuNewTemplate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmenuNewTemplateActionPerformed(evt);
            }
        });
        jTopMenuProduction.add(jmenuNewTemplate);

        jmenuNewProduction.setText("Yeni məhsulun istehsal olunması");
        jmenuNewProduction.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmenuNewProductionActionPerformed(evt);
            }
        });
        jTopMenuProduction.add(jmenuNewProduction);

        jMenuBar1.add(jTopMenuProduction);

        jTopMenuReports.setText("Hesabat");

        jmenuProductReport.setText("Dövriyyə hesabatı");
        jmenuProductReport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmenuProductReportActionPerformed(evt);
            }
        });
        jTopMenuReports.add(jmenuProductReport);

        jmenuPaymentReport.setText("Ödənişlərin hesabatı");
        jmenuPaymentReport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmenuPaymentReportActionPerformed(evt);
            }
        });
        jTopMenuReports.add(jmenuPaymentReport);

        jmenuPostavsikAndClientReport.setText("Məhsul gətirən və Satış məntəqələrinin hesabatı");
        jmenuPostavsikAndClientReport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmenuPostavsikAndClientReportActionPerformed(evt);
            }
        });
        jTopMenuReports.add(jmenuPostavsikAndClientReport);

        jMenuBar1.add(jTopMenuReports);

        jTopMenuExit.setText("Çıxış");

        jmenuBackup.setText("Arxivini götür");
        jmenuBackup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmenuBackupActionPerformed(evt);
            }
        });
        jTopMenuExit.add(jmenuBackup);

        jmenuExit.setText("Çıxış");
        jmenuExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmenuExitActionPerformed(evt);
            }
        });
        jTopMenuExit.add(jmenuExit);

        jMenuBar1.add(jTopMenuExit);

        setJMenuBar(jMenuBar1);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jmenuNewProductActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmenuNewProductActionPerformed
        ProductDialog.instance().showForm(CodeValue.PST_SIMPLE_PRODUCT);
    }//GEN-LAST:event_jmenuNewProductActionPerformed

    private void jmenuExpenditureAsProductActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmenuExpenditureAsProductActionPerformed
//        productEntryDialog.showForm(CodeType.EXPENDITURES, false, jmenuExpenditureAsProduct.getText());
//        invoiceDialog.showForm(InvoiceType.ESTIMATE, "Qalıq", null);
    }//GEN-LAST:event_jmenuExpenditureAsProductActionPerformed

    private void jmenuPaymentPostavsikActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmenuPaymentPostavsikActionPerformed
        PaymentDialog.instance().showForm(CodeType.POSTAVSIK);
    }//GEN-LAST:event_jmenuPaymentPostavsikActionPerformed

    private void jmenuPaymentClientActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmenuPaymentClientActionPerformed
        PaymentDialog.instance().showForm(CodeType.CLIENTS);
    }//GEN-LAST:event_jmenuPaymentClientActionPerformed

    private void jmenuExpenditureAsMoneyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmenuExpenditureAsMoneyActionPerformed
        PaymentDialog.instance().showForm(CodeType.EXPENDITURES);
    }//GEN-LAST:event_jmenuExpenditureAsMoneyActionPerformed

    private void jmenuExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmenuExitActionPerformed
        exit();
    }//GEN-LAST:event_jmenuExitActionPerformed

    private void jmenuNakladnoyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmenuNakladnoyActionPerformed
//        nakladnoyDialog.showForm();
    }//GEN-LAST:event_jmenuNakladnoyActionPerformed

    private void jmenuPostavsikAndClientReportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmenuPostavsikAndClientReportActionPerformed
        ReportDialog.instance().showForm(1);
    }//GEN-LAST:event_jmenuPostavsikAndClientReportActionPerformed

    private void exit() {
        Inits.getDb().closeDB();
        System.exit(0);
    }

    public static void export(String execfileName, String parameters, String outFileName) {
        try {

            String[] commands = {"cmd", "/c", "start", "\"DummyTitle\"", execfileName, parameters, outFileName};
            Process p = Runtime.getRuntime().exec(commands);
            p.waitFor();
            System.out.println("Done.");

        } catch (Exception ex) {
            ex.printStackTrace();
//            Logger.getLogger(CRDFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void jmenuBackupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmenuBackupActionPerformed
        File tmp = null;
        try {
            tmp = File.createTempFile("exportAnb", ".bat");
            tmp.deleteOnExit();
            FileWriter fileWriter = new FileWriter(tmp);
            BufferedWriter writer = new BufferedWriter(fileWriter);
            writer.write("@echo off \n");
            writer.write("exp %1 file=%2");
            writer.close();
            fileWriter.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Fayl create olunmadi" + ex.getMessage());
        }

        try {
            SimpleDateFormat df = new SimpleDateFormat("dd_MM_yyyy_HH_mm_SS");
            String userPassSid = Inits.getDb().getUser() + "/"
                    + Inits.getDb().getPassword() + "@" + Inits.getDb().getDbName();

            export(tmp.getAbsolutePath(), userPassSid, df.format(new Date()) + ".dmp");
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }//GEN-LAST:event_jmenuBackupActionPerformed

    private void jmenuProductReportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmenuProductReportActionPerformed
        ReportDialog.instance().showForm(1);
    }//GEN-LAST:event_jmenuProductReportActionPerformed

    private void jmenuPaymentReportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmenuPaymentReportActionPerformed
        ReportDialog.instance().showForm(0);
    }//GEN-LAST:event_jmenuPaymentReportActionPerformed

    private void jmenuNakladnoyForm2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmenuNakladnoyForm2ActionPerformed
//        productsNakladnoyDialog.showForm();
    }//GEN-LAST:event_jmenuNakladnoyForm2ActionPerformed

    private void jmenuNewTemplateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmenuNewTemplateActionPerformed
        ProductDialog templateDialog = new ProductDialog(this, true);
        templateDialog.showForm(CodeValue.PST_PRODUCTION_PRODUCT);
    }//GEN-LAST:event_jmenuNewTemplateActionPerformed

    private void jmenuNewProductionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmenuNewProductionActionPerformed
//        ProductionEntryDialog productionEntryDialog = new ProductionEntryDialog(null, true);
//        productionEntryDialog.showForm();
        TemplateEditDialog templateEditDialog = new TemplateEditDialog(null, true);
        templateEditDialog.showForm(null, false);
    }//GEN-LAST:event_jmenuNewProductionActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        exit();
    }//GEN-LAST:event_formWindowClosing

    private void jmenuClientsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmenuClientsActionPerformed
        ClientEditDialog.instance().showForm(null);
    }//GEN-LAST:event_jmenuClientsActionPerformed

    static MainForm mainForm = null;

    public static void main(String args[]) {
        /*
         * Set the Nimbus look and feel
         */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /*
         * If Nimbus (introduced in Java SE 6) is not available, stay with the
         * default look and feel. For details see
         * http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /*
         * Create and display the form
         */
        java.awt.EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
//                boolean res = false;
//                try {
//                    storesystem.JavaLibSSystem jlss = new storesystem.JavaLibSSystem();
//                    res = jlss.load();
//                } catch (Throwable t) {
////                    ex.printStackTrace();
//                    System.err.println("Error: license error 0001!");
//                }
//                res = true;
//                if (res) {
                SingleInstanceApplication sia = new SingleInstanceApplication() {

                    @Override
                    public void newInstanceOpened() {
                        System.out.println("second instance opening!");

                        if (mainForm != null) {
                            if (mainForm.getExtendedState() == JFrame.ICONIFIED) {
                                mainForm.setExtendedState(JFrame.NORMAL);
                            }
                            mainForm.setAlwaysOnTop(true);
                            mainForm.setAlwaysOnTop(false);
                            mainForm.setVisible(true);

//                            JOptionPane.showMessageDialog(mainForm, "Proqram artıq açıqdır!", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                };

                Inits.initLogger();
                Inits.initConfig();
                Inits.initLookAndFeel();
                Inits.initDb();
                Inits.checkUser();

                mainForm = new MainForm();
                mainForm.setVisible(true);
//                } else {
//                    System.err.println("Error: license error 0002!");
//                }
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JPopupMenu.Separator jSeparator2;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JMenu jTopMenuExit;
    private javax.swing.JMenu jTopMenuInvoices;
    private javax.swing.JMenu jTopMenuNews;
    private javax.swing.JMenu jTopMenuPayments;
    private javax.swing.JMenu jTopMenuProduction;
    private javax.swing.JMenu jTopMenuProducts;
    private javax.swing.JMenu jTopMenuReports;
    private javax.swing.JMenuItem jmenuBackup;
    private javax.swing.JMenuItem jmenuClients;
    private javax.swing.JMenuItem jmenuExit;
    private javax.swing.JMenuItem jmenuExpenditureAsMoney;
    private javax.swing.JMenuItem jmenuExpenditureAsProduct;
    private javax.swing.JMenuItem jmenuNakladnoy;
    private javax.swing.JMenuItem jmenuNakladnoyForm2;
    private javax.swing.JMenuItem jmenuNewClient;
    private javax.swing.JMenuItem jmenuNewCurrency;
    private javax.swing.JMenuItem jmenuNewExpenditure;
    private javax.swing.JMenuItem jmenuNewPostavsik;
    private javax.swing.JMenuItem jmenuNewProduct;
    private javax.swing.JMenuItem jmenuNewProductMeasure;
    private javax.swing.JMenuItem jmenuNewProductType;
    private javax.swing.JMenuItem jmenuNewProduction;
    private javax.swing.JMenuItem jmenuNewTemplate;
    private javax.swing.JMenuItem jmenuNewWorker;
    private javax.swing.JMenuItem jmenuPaymentClient;
    private javax.swing.JMenuItem jmenuPaymentPostavsik;
    private javax.swing.JMenuItem jmenuPaymentReport;
    private javax.swing.JMenuItem jmenuPostavsikAndClientReport;
    private javax.swing.JMenuItem jmenuPriceDecrease;
    private javax.swing.JMenuItem jmenuPriceIncrease;
    private javax.swing.JMenuItem jmenuProductReport;
    // End of variables declaration//GEN-END:variables
}
