/*
 * LoginWindow.java
 *
 * Created on 29 November 2008, 18:20
 */
package az.store.main;

import az.store.Inits;
import az.store.configs.Config;
import az.store.types.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * @author amirjanov
 */
public class LoginDialog extends javax.swing.JDialog {

    private User user = null;
    private int attempts = 0;
    private KeyListener escKeyListener = new KeyAdapter() {

        @Override
        public void keyPressed(KeyEvent evt) {
            if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
                System.exit(0);
            }
        }
    };
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btCancel;
    private javax.swing.JButton btOk;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jpUserName;
    private javax.swing.JPasswordField pfPassword;
    private javax.swing.JTextField tfUserName;

    /**
     * Creates new form LoginWindow
     */
    public LoginDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        setModal(modal);
        setAlwaysOnTop(true);
        setLocationRelativeTo(null);

        tfUserName.addKeyListener(escKeyListener);
        pfPassword.addKeyListener(escKeyListener);
        btOk.addKeyListener(escKeyListener);
        btCancel.addKeyListener(escKeyListener);

        tfUserName.setText(Config.instance(Inits.CONFIG_FILE_MAIN).getString(Inits.CONFIG_KEY_DEFAULTUSER));
        pfPassword.setText(Config.instance(Inits.CONFIG_FILE_MAIN).getString(Inits.CONFIG_KEY_DEFAULTPASS));
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel3 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        pfPassword = new javax.swing.JPasswordField();
        jpUserName = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        tfUserName = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        btOk = new javax.swing.JButton();
        btCancel = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("İstifadəçi adını və şifrəsini daxil edin");
        setResizable(false);

        jPanel2.setOpaque(false);
        jPanel2.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 10, 1));

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Şifrə:");
        jLabel2.setPreferredSize(new java.awt.Dimension(200, 15));
        jPanel2.add(jLabel2);

        pfPassword.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        pfPassword.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        pfPassword.setOpaque(false);
        pfPassword.setPreferredSize(new java.awt.Dimension(200, 21));
        pfPassword.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                pfPasswordFocusGained(evt);
            }

            public void focusLost(java.awt.event.FocusEvent evt) {
                pfPasswordFocusLost(evt);
            }
        });
        pfPassword.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                pfPasswordKeyPressed(evt);
            }
        });
        jPanel2.add(pfPassword);

        jpUserName.setOpaque(false);
        jpUserName.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 10, 1));

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setLabelFor(tfUserName);
        jLabel1.setText("İstifadəçi adı:");
        jLabel1.setPreferredSize(new java.awt.Dimension(200, 15));
        jpUserName.add(jLabel1);

        tfUserName.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        tfUserName.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        tfUserName.setOpaque(false);
        tfUserName.setPreferredSize(new java.awt.Dimension(200, 21));
        tfUserName.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                tfUserNameFocusGained(evt);
            }

            public void focusLost(java.awt.event.FocusEvent evt) {
                tfUserNameFocusLost(evt);
            }
        });
        tfUserName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tfUserNameKeyPressed(evt);
            }
        });
        jpUserName.add(tfUserName);

        jPanel1.setOpaque(false);

        btOk.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btOk.setIcon(new javax.swing.ImageIcon(getClass().getResource("/az/store/images/ok.png"))); // NOI18N
        btOk.setText("Daxil ol");
        btOk.setOpaque(false);
        btOk.setPreferredSize(new java.awt.Dimension(110, 27));
        btOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btOkActionPerformed(evt);
            }
        });
        jPanel1.add(btOk);

        btCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/az/store/images/close.png"))); // NOI18N
        btCancel.setText("İmtina et");
        btCancel.setPreferredSize(new java.awt.Dimension(110, 27));
        btCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btCancelActionPerformed(evt);
            }
        });
        jPanel1.add(btCancel);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
                jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 370, Short.MAX_VALUE)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                                .addGap(59, 59, 59)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                                        .addComponent(jpUserName, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                                .addGap(64, 64, 64))
        );
        jPanel3Layout.setVerticalGroup(
                jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jpUserName, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tfUserNameFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tfUserNameFocusGained
        tfUserName.setOpaque(true);
    }//GEN-LAST:event_tfUserNameFocusGained

    private void tfUserNameFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tfUserNameFocusLost
        tfUserName.setOpaque(false);
    }//GEN-LAST:event_tfUserNameFocusLost

    private void pfPasswordFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_pfPasswordFocusGained
        pfPassword.setOpaque(true);
    }//GEN-LAST:event_pfPasswordFocusGained

    private void pfPasswordFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_pfPasswordFocusLost
        pfPassword.setOpaque(false);
    }//GEN-LAST:event_pfPasswordFocusLost

    private void btCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btCancelActionPerformed
        closeApplication();
    }//GEN-LAST:event_btCancelActionPerformed

    private void btOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btOkActionPerformed
        check();
    }//GEN-LAST:event_btOkActionPerformed

    private void check() {
        String userName = tfUserName.getText();
        String passWord = new String(pfPassword.getPassword());

        user = Inits.getSelectQueries().checkUser(userName, passWord);
        if (user != null) {
            setVisible(false);
        } else {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
            }
            Toolkit.getDefaultToolkit().beep();
            JOptionPane.showMessageDialog(this, "İstifadəçi adı və ya şifrəsi düzgün deyil",
                    "Səhv", JOptionPane.ERROR_MESSAGE);

            attempts++;
            if (attempts == 3) {
                closeApplication();
            }

            tfUserName.setText("");
            pfPassword.setText("");
            tfUserName.requestFocus();
        }
    }

    public User checkUser() {
        setVisible(true);
        return user;
    }

    private void closeApplication() {
        System.exit(0);
    }

    private void pfPasswordKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_pfPasswordKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            check();
        }
    }//GEN-LAST:event_pfPasswordKeyPressed

    private void tfUserNameKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tfUserNameKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            pfPassword.requestFocus();
        }
    }//GEN-LAST:event_tfUserNameKeyPressed
    // End of variables declaration//GEN-END:variables
}