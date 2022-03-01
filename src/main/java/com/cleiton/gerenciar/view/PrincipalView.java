
package com.cleiton.gerenciar.view;

import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JTextPane;

public class PrincipalView extends javax.swing.JFrame {

    public PrincipalView() {
        initComponents();
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        desktop = new javax.swing.JDesktopPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtUser = new javax.swing.JTextPane();
        btnNotifications = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        txtUser.setEditable(false);
        jScrollPane1.setViewportView(txtUser);

        btnNotifications.setText("0 notificações");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(desktop)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 476, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnNotifications, javax.swing.GroupLayout.DEFAULT_SIZE, 146, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(desktop, javax.swing.GroupLayout.DEFAULT_SIZE, 306, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane1)
                    .addComponent(btnNotifications, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnNotifications;
    private javax.swing.JDesktopPane desktop;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextPane txtUser;
    // End of variables declaration//GEN-END:variables

    public JDesktopPane getDesktop() {
        return desktop;
    }

    public JButton getBtnNotifications() {
        return btnNotifications;
    }

    public JTextPane getTxtUser() {
        return txtUser;
    }
}
