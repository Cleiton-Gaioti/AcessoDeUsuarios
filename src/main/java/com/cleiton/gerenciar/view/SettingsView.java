package com.cleiton.gerenciar.view;

import javax.swing.JButton;
import javax.swing.JRadioButton;

public class SettingsView extends javax.swing.JInternalFrame {

    public SettingsView() {
        initComponents();
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        radioButtonCSV = new javax.swing.JRadioButton();
        radioButtonJSON = new javax.swing.JRadioButton();
        radioButtonXML = new javax.swing.JRadioButton();
        btnApply = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();

        jLabel1.setText("Persistência de Log");

        radioButtonCSV.setText("CSV");

        radioButtonJSON.setText("JSON");

        radioButtonXML.setText("XML");

        btnApply.setText("Aplicar");

        btnCancel.setText("Cancelar");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(radioButtonCSV)
                            .addComponent(radioButtonJSON)
                            .addComponent(radioButtonXML)))
                    .addComponent(jLabel1))
                .addContainerGap(54, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnCancel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnApply)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(radioButtonCSV)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(radioButtonJSON)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(radioButtonXML)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 74, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnApply)
                    .addComponent(btnCancel))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnApply;
    private javax.swing.JButton btnCancel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JRadioButton radioButtonCSV;
    private javax.swing.JRadioButton radioButtonJSON;
    private javax.swing.JRadioButton radioButtonXML;
    // End of variables declaration//GEN-END:variables

    public JRadioButton getRadioButtonCSV() {
        return radioButtonCSV;
    }

    public JRadioButton getRadioButtonJSON() {
        return radioButtonJSON;
    }

    public JRadioButton getRadioButtonXML() {
        return radioButtonXML;
    }

    public JButton getBtnApply() {
        return btnApply;
    }

    public JButton getBtnCancel() {
        return btnCancel;
    }
}
