/**
 * ****************************************************************************
 *  Copyright(c) 2025 the original author Eduardo Iglesias Taylor.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  	 https://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 *  Contributors:
 *  	Eduardo Iglesias Taylor - initial API and implementation
 * *****************************************************************************
 */
package org.platkmframework.cplatkm.desktop.panels.runconfigurations;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import org.platkmframework.cplatkm.desktop.core.CGeneratorContentManager; 
import org.platkmframework.cplatkm.desktop.core.components.table.button.BasicButtonAction;
import org.platkmframework.cplatkm.desktop.core.components.table.button.ButtonsEditor;
import org.platkmframework.cplatkm.desktop.core.components.table.button.ButtonsPanel;
import org.platkmframework.cplatkm.desktop.core.components.table.button.ButtonsRenderer;
import org.platkmframework.cplatkm.desktop.core.components.table.checkbox.CheckBoxRenderer;
import org.platkmframework.cplatkm.processor.data.Artifact;
import org.platkmframework.cplatkm.processor.data.RunConfigArtifact;
import org.platkmframework.cplatkm.processor.data.RunConfigTemplate;
import org.platkmframework.cplatkm.processor.data.Template;

/**
 *
 * @author Eduardo Iglesias
 */
public class RunConfigArtifactTemplatesDialog extends javax.swing.JDialog {

    DefaultTableModel tableDataModel;
    RunConfigArtifact runConfigArtifact;
    public boolean updated;
    String rootPath;
    RunConfigPropertiesEditorDialog templateEditorDialog;
    RunConfigTemplatePathJDialog runConfigTemplatePathJDialog;
    
    /**
     * Creates new form RunConfigArtifTemplates
     */
    public RunConfigArtifactTemplatesDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
       setLocationRelativeTo(null);
       
        templateEditorDialog = new RunConfigPropertiesEditorDialog(CGeneratorContentManager.getInstance().getMainFrame(),true);
        runConfigTemplatePathJDialog = new RunConfigTemplatePathJDialog(CGeneratorContentManager.getInstance().getMainFrame(),true);
        
        tableDataModel = new DefaultTableModel(new String[] {"Name", "Description", "Active", "Apply Only With Tags","Properties", "Relative Path"}, 0){
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                // Especificar que la columna 1 tendrá Boolean para los checkboxes
                if (columnIndex == 2 || columnIndex == 3) {
                    return Boolean.class;
                }
                return super.getColumnClass(columnIndex);
            }
            
            @Override
            public boolean isCellEditable(int row, int column) {
                return column >= 2;
            }
        };
        tableData.setRowHeight(26);
        tableData.setAutoCreateRowSorter(true);
        
        tableData.setModel(tableDataModel);
        tableData.getColumnModel().getColumn(2).setCellRenderer(new CheckBoxRenderer());
        tableData.getColumnModel().getColumn(2).setCellEditor(new DefaultCellEditor(new JCheckBox()));
        tableData.getColumnModel().getColumn(3).setCellRenderer(new CheckBoxRenderer());
        tableData.getColumnModel().getColumn(3).setCellEditor(new DefaultCellEditor(new JCheckBox()));
        
        List<JButton> buttons = new ArrayList<>();
        JButton buton = new JButton("...");
        buton.setAction(new BasicButtonAction("...") {
           
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = tableData.getSelectedRow();
                if (selectedRow >=0) {
                    templateEditorDialog.setLocationRelativeTo(CGeneratorContentManager.getInstance().getMainFrame());
                    templateEditorDialog.setData(runConfigArtifact.getTemplates().get(selectedRow).getProperties());
                    templateEditorDialog.setVisible(true);  

                    if(templateEditorDialog.updated){
                        runConfigArtifact.getTemplates().get(selectedRow).getProperties().clear();
                        runConfigArtifact.getTemplates().get(selectedRow).getProperties().putAll(templateEditorDialog.mapData);
                    }
                }
            }
            
        });
        buttons.add(buton);
        ButtonsPanel ButtonsPanel = new  ButtonsPanel(buttons);
        tableData.getColumnModel().getColumn(4).setCellRenderer(new ButtonsRenderer(ButtonsPanel));
        tableData.getColumnModel().getColumn(4).setCellEditor(new ButtonsEditor(this.tableData, ButtonsPanel));
        
        
        buttons = new ArrayList<>();
        buton = new JButton("...");
        buton.setAction(new BasicButtonAction("...") {
           
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = tableData.getSelectedRow();
                if (selectedRow >=0) {
                    runConfigTemplatePathJDialog.setLocationRelativeTo(CGeneratorContentManager.getInstance().getMainFrame());
                    runConfigTemplatePathJDialog.setData(rootPath, runConfigArtifact.getTemplates().get(selectedRow).getRelativePath());
                    runConfigTemplatePathJDialog.setVisible(true);  

                    if(runConfigTemplatePathJDialog.updated){
                        runConfigArtifact.getTemplates().get(selectedRow).setRelativePath(runConfigTemplatePathJDialog.relativePath); 
                    }
                }
                //
            }
            
        });
        buttons.add(buton);
        ButtonsPanel = new  ButtonsPanel(buttons);
        tableData.getColumnModel().getColumn(5).setCellRenderer(new ButtonsRenderer(ButtonsPanel));
        tableData.getColumnModel().getColumn(5).setCellEditor(new ButtonsEditor(this.tableData, ButtonsPanel) );
        
        tableDataModel.addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                // Verificar si el evento es de actualización (EDICIÓN)
                if (e.getType() == TableModelEvent.UPDATE) {
                    int row = e.getFirstRow(); // Fila donde ocurrió el cambio
                    int column = e.getColumn(); // Columna donde ocurrió el cambio

                    // Verificar si la columna es la del checkbox
                    if (column == 2) {
                        Boolean isChecked = (Boolean) tableData.getValueAt(row, column);
                        runConfigArtifact.getTemplates().get(row).setActive(isChecked); 
                    }else if (column == 3) {
                        Boolean isChecked = (Boolean) tableData.getValueAt(row, column);
                        runConfigArtifact.getTemplates().get(row).setApplyOnlyWithTags(isChecked); 
                    }
                }
            }
        });
        
  /**      tableData.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) { // Detectar doble clic
                    int selectedRow = tableData.getSelectedRow();
                    if (selectedRow >=0) {
                        templateEditorDialog.setData(runConfigArtifact.getTemplates().get(selectedRow).getProperties());
                        templateEditorDialog.setVisible(true);  

                        if(templateEditorDialog.updated){
                            runConfigArtifact.getTemplates().get(selectedRow).getProperties().clear();
                            runConfigArtifact.getTemplates().get(selectedRow).getProperties().putAll(templateEditorDialog.mapData);
                        }
                    }
                }
            } 
        }); */
   
}
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        lblArtifactName = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tableData = new javax.swing.JTable();
        btnClose = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Artifact Templates");

        jLabel1.setText("Artifact");

        lblArtifactName.setText("<Artifact name>");

        tableData.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null}
            },
            new String [] {
                "Name", "Description", "Active", "Apply Only With Tags", "Properties", "Relative Path"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.Boolean.class, java.lang.Boolean.class, java.lang.Object.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, true, true, true, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tableData);

        btnClose.setText("Close");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 659, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblArtifactName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnClose, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(lblArtifactName))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 209, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnClose)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        this.updated = false;
        setVisible(false);
    }//GEN-LAST:event_btnCloseActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(RunConfigArtifactTemplatesDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(RunConfigArtifactTemplatesDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(RunConfigArtifactTemplatesDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(RunConfigArtifactTemplatesDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                RunConfigArtifactTemplatesDialog dialog = new RunConfigArtifactTemplatesDialog(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnClose;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblArtifactName;
    private javax.swing.JTable tableData;
    // End of variables declaration//GEN-END:variables


    public void setData(String rootPath, RunConfigArtifact runConfigArtifact){
        this.updated = false;
        this.rootPath = rootPath;
        this.runConfigArtifact = runConfigArtifact; 
        refreshTableData(); 
    }

     private void refreshTableData() {
        tableDataModel.setRowCount(0);
        Template template;
        Artifact artifact = CGeneratorContentManager.getInstance().getCgenetatorConfig().getArtifacts().stream().filter(a->a.getId().equals(runConfigArtifact.getArtifactId())).findFirst().orElse(null);
        lblArtifactName.setText(artifact.getLabel());
        
        for (RunConfigTemplate runtConfigTemplate : this.runConfigArtifact.getTemplates()) {
           
            template = artifact.getTemplates().stream().filter(t-> t.getId().equals(runtConfigTemplate.getId())).findFirst().orElse(null);
            
            Object[] rowData = {
                template.getLabel(),
                template.getDescription(), 
                runtConfigTemplate.isActive(),
                runtConfigTemplate.isApplyOnlyWithTags(),
                "",
                ""
            };
            tableDataModel.addRow(rowData);
        }
    }
}
