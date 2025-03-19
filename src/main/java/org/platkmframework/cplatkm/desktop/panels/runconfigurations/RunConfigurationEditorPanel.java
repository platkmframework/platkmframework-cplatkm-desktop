/*
 * Copyright 2025 Eduardo Iglesias.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.platkmframework.cplatkm.desktop.panels.runconfigurations;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import java.awt.HeadlessException;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import org.apache.commons.lang3.StringUtils;
import org.apache.velocity.exception.MethodInvocationException;
import org.platkmframework.cplatkm.desktop.commons.ObjectSelection;
import org.platkmframework.cplatkm.desktop.commons.editor.ComponentInspector;
import org.platkmframework.cplatkm.desktop.commons.editor.EditComponentsInspector;
import org.platkmframework.cplatkm.desktop.commons.editor.JComboBoxComponentInspector;
import org.platkmframework.cplatkm.desktop.commons.editor.JTableEditorComponentInspector;
import org.platkmframework.cplatkm.desktop.commons.editor.JTextEditorComponentInspector;
import org.platkmframework.cplatkm.desktop.commons.editor.JTextPaneComponentInspector;
import org.platkmframework.cplatkm.desktop.core.CGeneratorContentManager;
import org.platkmframework.cplatkm.desktop.core.components.table.checkbox.CheckBoxRenderer;
import org.platkmframework.cplatkm.processor.CodeGenerationRunner;
import org.platkmframework.cplatkm.processor.data.Artifact;
import org.platkmframework.cplatkm.processor.data.DataTypeMapping;
import org.platkmframework.cplatkm.processor.data.DataTypes;
import org.platkmframework.cplatkm.processor.data.GlobalData;
import org.platkmframework.cplatkm.processor.data.OpenApiImported;
import org.platkmframework.cplatkm.processor.data.RunConfigArtifact;
import org.platkmframework.cplatkm.processor.data.RunConfigData;
import org.platkmframework.cplatkm.processor.data.RunConfigTemplate;
import org.platkmframework.cplatkm.processor.data.RunConfiguration;
import org.platkmframework.cplatkm.processor.data.Template;
import org.platkmframework.cplatkm.processor.exception.CGeneratorException;
import org.platkmframework.util.Util;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Eduardo Iglesias
 */
public class RunConfigurationEditorPanel extends javax.swing.JPanel {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(RunConfigurationEditorPanel.class);
    
    private RunConfiguration runConfiguration;
    private boolean udpdated;
    private JComboBox<String> comboBoxTag;
    JFileChooser fileChooser;
    
    DefaultTableModel tableDataModel;
    DefaultTableModel artifactModel;
    
    RunConfigPropertiesEditorDialog templateEditorDialog;
    RunConfigArtifactTemplatesDialog runConfigArtifactTemplatesDialog;
    RunConfigAddDataDialog runConfigAddDataDialog;
    
     private EditComponentsInspector editComponentsInspector;
    /**
     * Creates new form RunConfigurationEditorPanel
     */
    public RunConfigurationEditorPanel() {
        initComponents();
        initEditorComponetInspector();
        
        templateEditorDialog             = new RunConfigPropertiesEditorDialog(CGeneratorContentManager.getInstance().getMainFrame(),true);
        runConfigArtifactTemplatesDialog = new RunConfigArtifactTemplatesDialog(CGeneratorContentManager.getInstance().getMainFrame(), true);
        runConfigAddDataDialog     = new RunConfigAddDataDialog(CGeneratorContentManager.getInstance().getMainFrame(), true);
        
        this.comboBoxTag = new JComboBox<>();
        fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        
        cmbDataTypeMapping.setRenderer((JList<? extends DataTypeMapping> list, DataTypeMapping value, int index, boolean isSelected, boolean cellHasFocus) -> {
            JLabel label = new JLabel();
            if(value != null) label.setText(value.getName());
            return label;
        });        
   
        cmbArtifiact.setRenderer((JList<? extends Artifact> list, Artifact value, int index, boolean isSelected, boolean cellHasFocus) -> {
            JLabel label = new JLabel();
            if(value != null) label.setText(value.getLabel());
            return label;
        });
        
        tableDataModel = new DefaultTableModel(new String[] {"Tags", "Code", "Name", "Active"}, 0){
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                // Especificar que la columna 1 tendrá Boolean para los checkboxes
                if (columnIndex == 3) {
                    return Boolean.class;
                }
                return super.getColumnClass(columnIndex);
            }
            @Override
            public boolean isCellEditable(int row, int column) {
                return column==3;
            }
        };
        tableData.setModel(tableDataModel);
        // Asegurarse de que los checkboxes se rendericen correctamente
        tableData.getColumnModel().getColumn(3).setCellRenderer(new CheckBoxRenderer());
        tableData.getColumnModel().getColumn(3).setCellEditor(new DefaultCellEditor(new JCheckBox()));
        tableDataModel.addTableModelListener((TableModelEvent e) -> {
            // Verificar si el evento es de actualización (EDICIÓN)
            if (e.getType() == TableModelEvent.UPDATE) {
                int row = e.getFirstRow(); // Fila donde ocurrió el cambio
                int column = e.getColumn(); // Columna donde ocurrió el cambio
                
                // Verificar si la columna es la del checkbox
                if (column == 3) {
                    Boolean isChecked = (Boolean) tableData.getValueAt(row, column);
                    runConfiguration.getDatas().get(row).setActive(isChecked);
                }
            }
        });
        
        artifactModel =  new DefaultTableModel(new String[] {"Name", "Description", "Active"}, 0) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                // Especificar que la columna 1 tendrá Boolean para los checkboxes
                if (columnIndex == 2) {
                    return Boolean.class;
                }
                return super.getColumnClass(columnIndex);
            }
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 2;
            }
        };
        tbArtifactData.setModel(artifactModel);
        // Asegurarse de que los checkboxes se rendericen correctamente  
        tbArtifactData.getColumnModel().getColumn(2).setCellRenderer(new CheckBoxRenderer());
        tbArtifactData.getColumnModel().getColumn(2).setCellEditor(new DefaultCellEditor(new JCheckBox()));
        artifactModel.addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                // Verificar si el evento es de actualización (EDICIÓN)
                if (e.getType() == TableModelEvent.UPDATE) {
                    int row = e.getFirstRow(); // Fila donde ocurrió el cambio
                    int column = e.getColumn(); // Columna donde ocurrió el cambio

                    // Verificar si la columna es la del checkbox
                    if (column == 2) {
                        Boolean isChecked = (Boolean) tbArtifactData.getValueAt(row, column);
                        runConfiguration.getArtifacts().get(row).setActive(isChecked); 
                    }
                }
            }
        });
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
        txtName = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        txtDescription = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtRootPath = new javax.swing.JTextField();
        btnRootPath = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        jPanel9 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        cmbArtifiact = new javax.swing.JComboBox<>();
        btnAddArtifact = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbArtifactData = new javax.swing.JTable();
        btnArtTemplates = new javax.swing.JButton();
        btnArtifactProperties = new javax.swing.JButton();
        btnRemoveArtifact = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();
        cmbDataTypeMapping = new javax.swing.JComboBox<>();
        jPanel10 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tableData = new javax.swing.JTable();
        btnAddData = new javax.swing.JButton();
        btnRemoveData = new javax.swing.JButton();
        jPanel11 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        txtPanelAdditionalData = new javax.swing.JTextPane();
        jPanel3 = new javax.swing.JPanel();
        btnRun = new javax.swing.JButton();
        btnApply = new javax.swing.JButton();

        jLabel1.setText("Name");

        jLabel2.setText("Description");

        jLabel3.setText("Root Path");

        btnRootPath.setText("...");
        btnRootPath.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRootPathActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 62, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(txtName)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtDescription))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(txtRootPath)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnRootPath, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(txtDescription, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtRootPath, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnRootPath))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel4.setText("Artifacts");

        btnAddArtifact.setText("Add");
        btnAddArtifact.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddArtifactActionPerformed(evt);
            }
        });

        tbArtifactData.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Name", "Description", "Active"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.Boolean.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tbArtifactData);

        btnArtTemplates.setText("Templates");
        btnArtTemplates.setToolTipText("");
        btnArtTemplates.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnArtTemplatesActionPerformed(evt);
            }
        });

        btnArtifactProperties.setText("Properties");
        btnArtifactProperties.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnArtifactPropertiesActionPerformed(evt);
            }
        });

        btnRemoveArtifact.setText("Remove");
        btnRemoveArtifact.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRemoveArtifactActionPerformed(evt);
            }
        });

        jLabel9.setText("Types Mapping");

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(btnArtTemplates)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnArtifactProperties)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnRemoveArtifact, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 550, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(32, 32, 32)
                                .addComponent(cmbArtifiact, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnAddArtifact))))
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmbDataTypeMapping, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(cmbDataTypeMapping, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(cmbArtifiact, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnAddArtifact))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 273, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnRemoveArtifact)
                    .addComponent(btnArtifactProperties)
                    .addComponent(btnArtTemplates))
                .addContainerGap())
        );

        jTabbedPane2.addTab("Run", jPanel9);

        tableData.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Group", "Code", "Name", "Active"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Boolean.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane2.setViewportView(tableData);

        btnAddData.setText("Add Data...");
        btnAddData.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddDataActionPerformed(evt);
            }
        });

        btnRemoveData.setText("Remove");
        btnRemoveData.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRemoveDataActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 544, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnAddData)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnRemoveData, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 330, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnRemoveData)
                    .addComponent(btnAddData))
                .addContainerGap())
        );

        jTabbedPane2.addTab("Data", jPanel10);

        jScrollPane3.setViewportView(txtPanelAdditionalData);

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 550, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 359, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane2.addTab("Additional Data", jPanel11);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane2)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane2, javax.swing.GroupLayout.Alignment.TRAILING)
        );

        btnRun.setText("Run");
        btnRun.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRunActionPerformed(evt);
            }
        });

        btnApply.setText("Apply");
        btnApply.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnApplyActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnRun, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnApply, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnApply)
                    .addComponent(btnRun))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(15, 15, 15)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnRootPathActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRootPathActionPerformed

        fileChooser.setCurrentDirectory(new File(this.runConfiguration.getRootPath()));
        int result = fileChooser.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            txtRootPath.setText(selectedFile.getAbsolutePath());
        }
    }//GEN-LAST:event_btnRootPathActionPerformed

    private void btnAddArtifactActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddArtifactActionPerformed
        Artifact artifact = (Artifact) cmbArtifiact.getSelectedItem();
        RunConfigArtifact runConfigArtifact = new RunConfigArtifact();
        runConfigArtifact.getProperties().putAll(artifact.getProperties());
        runConfigArtifact.setArtifactId(artifact.getId());
        RunConfigTemplate runtConfigTemplate;
        for( Template template :artifact.getTemplates()){
            runtConfigTemplate = new RunConfigTemplate();
            runtConfigTemplate.setId(template.getId());
            runtConfigTemplate.setActive(true);
            runtConfigTemplate.getProperties().putAll(template.getProperties());
            runConfigArtifact.getTemplates().add(runtConfigTemplate);
        }

        this.runConfiguration.getArtifacts().add(runConfigArtifact);
        refreshArtifact();
    }//GEN-LAST:event_btnAddArtifactActionPerformed

    private void btnRemoveArtifactActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemoveArtifactActionPerformed
        int selectedRow = tbArtifactData.getSelectedRow();
        if (selectedRow >= 0) {
            this.runConfiguration.getArtifacts().remove(selectedRow);
            refreshArtifact();
        }
    }//GEN-LAST:event_btnRemoveArtifactActionPerformed

    private void btnArtifactPropertiesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnArtifactPropertiesActionPerformed
        int selectedRow = tbArtifactData.getSelectedRow();
        if (selectedRow >= 0) {
            templateEditorDialog.setLocationRelativeTo(CGeneratorContentManager.getInstance().getMainFrame());
            templateEditorDialog.setData(this.runConfiguration.getArtifacts().get(selectedRow).getProperties());
            templateEditorDialog.setVisible(true);

            if(templateEditorDialog.updated){
                this.runConfiguration.getArtifacts().get(selectedRow).getProperties().clear();
                this.runConfiguration.getArtifacts().get(selectedRow).getProperties().putAll(templateEditorDialog.mapData);

            }
        }
    }//GEN-LAST:event_btnArtifactPropertiesActionPerformed

    private void btnArtTemplatesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnArtTemplatesActionPerformed
        int selectedRow = tbArtifactData.getSelectedRow();
        if (selectedRow >= 0) {
            runConfigArtifactTemplatesDialog.setLocationRelativeTo(CGeneratorContentManager.getInstance().getMainFrame());
            runConfigArtifactTemplatesDialog.setData(this.runConfiguration.getRootPath(), this.runConfiguration.getArtifacts().get(selectedRow));
            runConfigArtifactTemplatesDialog.setVisible(true);

        }
    }//GEN-LAST:event_btnArtTemplatesActionPerformed

    private void btnRemoveDataActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemoveDataActionPerformed
        int selectedRow = tableData.getSelectedRow();
        if (selectedRow >= 0) {
            this.runConfiguration.getDatas().remove(selectedRow);
            refreshTableData();
        }
    }//GEN-LAST:event_btnRemoveDataActionPerformed

    private void btnAddDataActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddDataActionPerformed
        runConfigAddDataDialog.setData();
        runConfigAddDataDialog.setVisible(true);
        if(runConfigAddDataDialog.isUpdated() && !runConfigAddDataDialog.getSelectedTables().isEmpty()){
            
            RunConfigData runConfigData;
            for (ObjectSelection objectSelection : runConfigAddDataDialog.getSelectedTables()) {
                if(objectSelection.isSelected()){
                    runConfigData = new RunConfigData();
                    runConfigData.setId(Util.randomString(255));
                    runConfigData.setCode("");
                    runConfigData.setType(objectSelection.getType());
                    runConfigData.setName(objectSelection.getName());
                    runConfigData.setRefId( objectSelection.getId());
                    this.runConfiguration.getDatas().add(runConfigData);
                }
            }
           
            refreshTableData();
        }

    }//GEN-LAST:event_btnAddDataActionPerformed

    private void btnRunActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRunActionPerformed
        try {
            CodeGenerationRunner.run(CGeneratorContentManager.getInstance().getCurrentWorkSpace().getAbsolutePath(),
                CGeneratorContentManager.getInstance().getCgenetatorConfig(),
                runConfiguration);

            JOptionPane.showMessageDialog(this,
                "code generated successfully" ,
                "Generation Process", JOptionPane.INFORMATION_MESSAGE);

        } catch (HeadlessException | MethodInvocationException | CGeneratorException ex) {
            Logger.getLogger(RunConfigurationEditorPanel.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this,
                ex.getMessage(),
                "Error", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_btnRunActionPerformed

    private void btnApplyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnApplyActionPerformed
        if(StringUtils.isBlank(txtName.getText())){
            JOptionPane.showMessageDialog(this,
                "Artifact name should not be empty",
                "Edit", JOptionPane.WARNING_MESSAGE);

            return;
        }

        try {

            this.runConfiguration.setName(txtName.getText().trim());
            this.runConfiguration.setDescription(txtDescription.getText());
            this.runConfiguration.setRootPath(txtRootPath.getText());

            ObjectMapper objectMapper = new ObjectMapper();
            this.runConfiguration.setAdditionalData(objectMapper.readValue(txtPanelAdditionalData.getText(), Map.class));

            this.runConfiguration.setDataTypeMappingId(cmbDataTypeMapping.getSelectedItem() != null? ((org.platkmframework.cplatkm.processor.data.DataTypeMapping)cmbDataTypeMapping.getSelectedItem()).getId():"");

            if(StringUtils.isNotBlank(runConfiguration.getId())){

                try {

                    for (int i = 0; i < CGeneratorContentManager.getInstance().getCgenetatorConfig().getRunConfigurations().size(); i++) {
                        if (CGeneratorContentManager.getInstance().getCgenetatorConfig().getRunConfigurations().get(i).getId().equals(this.runConfiguration.getId())){
                            CGeneratorContentManager.getInstance().getCgenetatorConfig().getRunConfigurations().set(i, this.runConfiguration);
                            break;
                        }
                    }

                    CGeneratorContentManager.getInstance().updateConfigFile();
                    setUdpdated(true);
                    CGeneratorContentManager.getInstance().refreshRunConfigurationSeparator();
                    
                    JOptionPane.showMessageDialog(this,
                    "Saved",
                    "Run Configuration", JOptionPane.INFORMATION_MESSAGE);
                    
                } catch (CGeneratorException ex) {
                    Logger.getLogger(RunConfigurationPanel.class.getName()).log(Level.SEVERE, null, ex);
                    JOptionPane.showMessageDialog(this,
                        ex.getMessage(),
                        "Edit", JOptionPane.WARNING_MESSAGE);
                }
            }else{

                runConfiguration.setId(Util.randomAlfaNumericString(255));
                CGeneratorContentManager.getInstance().getCgenetatorConfig().getRunConfigurations().add(this.runConfiguration);
                CGeneratorContentManager.getInstance().updateConfigFile();
                setUdpdated(true);
                CGeneratorContentManager.getInstance().refreshRunConfigurationSeparator();

                JOptionPane.showMessageDialog(this,
                    "Saved",
                    "Run Configuration", JOptionPane.INFORMATION_MESSAGE);

            }

        } catch (CGeneratorException | JsonProcessingException ex) {
            Logger.getLogger(RunConfigPropertiesEditorDialog.class.getName()).log(Level.SEVERE, null, ex);

            JOptionPane.showMessageDialog(this,
                "Json error",
                "Edit", JOptionPane.ERROR_MESSAGE);
        }

        //setVisible(false);
    }//GEN-LAST:event_btnApplyActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAddArtifact;
    private javax.swing.JButton btnAddData;
    private javax.swing.JButton btnApply;
    private javax.swing.JButton btnArtTemplates;
    private javax.swing.JButton btnArtifactProperties;
    private javax.swing.JButton btnRemoveArtifact;
    private javax.swing.JButton btnRemoveData;
    private javax.swing.JButton btnRootPath;
    private javax.swing.JButton btnRun;
    private javax.swing.JComboBox<org.platkmframework.cplatkm.processor.data.Artifact> cmbArtifiact;
    private javax.swing.JComboBox<org.platkmframework.cplatkm.processor.data.DataTypeMapping> cmbDataTypeMapping;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JTable tableData;
    private javax.swing.JTable tbArtifactData;
    private javax.swing.JTextField txtDescription;
    private javax.swing.JTextField txtName;
    private javax.swing.JTextPane txtPanelAdditionalData;
    private javax.swing.JTextField txtRootPath;
    // End of variables declaration//GEN-END:variables


    public void setData(RunConfiguration runConfiguration) {
        
        this.udpdated = false;
        if(runConfiguration == null){
            runConfiguration = new RunConfiguration();
        }
        this.runConfiguration  = runConfiguration;
        this.comboBoxTag.removeAllItems();
        this.cmbArtifiact.removeAllItems();
        this.cmbDataTypeMapping.removeAllItems();
        
        txtName.setText(this.runConfiguration.getName());
        txtDescription.setText(this.runConfiguration.getDescription());
        txtRootPath.setText(this.runConfiguration.getRootPath());
        
        
        try {
            ObjectWriter writer = new ObjectMapper().writerWithDefaultPrettyPrinter();
            String formattedJson = writer.writeValueAsString( this.runConfiguration.getAdditionalData());
            txtPanelAdditionalData.setText(formattedJson);
        } catch (JsonProcessingException ex) {
            Logger.getLogger(RunConfigurationEditorPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        final List<RunConfigTemplate> newTemplates = new ArrayList<>(); 
        
        for (Artifact artifact : CGeneratorContentManager.getInstance().getCgenetatorConfig().getArtifacts()) {
            cmbArtifiact.addItem(artifact);
            
            runConfiguration.getArtifacts().forEach((a->{
                
                if(a.getArtifactId().equals(artifact.getId())){
                    artifact.getTemplates().forEach((t)->{
                        if(!a.getTemplates().stream().filter(rt-> rt.getId().equals(t.getId())).findFirst().isPresent()){
                         newTemplates.add(new RunConfigTemplate(t.getId(), t.getProperties(), false));
                        }
                    });

                    a.getTemplates().addAll(newTemplates); 
                    newTemplates.clear();  
                }
            }));
        } 
        
        DataTypeMapping dataTypeMappingSelected = null;
        for (DataTypeMapping dataTypeMapping : CGeneratorContentManager.getInstance().getCgenetatorConfig().getDatatypes()) {
            if(dataTypeMapping.getId().equals(runConfiguration.getDataTypeMappingId()))
                dataTypeMappingSelected = dataTypeMapping;
            cmbDataTypeMapping.addItem(dataTypeMapping);
        }
        
        cmbDataTypeMapping.setSelectedItem(dataTypeMappingSelected);
        
        refreshTableData();
        refreshArtifact();
     
    }

    public boolean isUdpdated() {
        return udpdated;
    }

    public void setUdpdated(boolean udpdated) {
        this.udpdated = udpdated;
    }
    
     public RunConfiguration getItem() {
        return runConfiguration;
    }

    public void setItem(RunConfiguration runConfiguration) {
        this.runConfiguration = runConfiguration;
    }
    
     private void refreshTableData() {
        GlobalData globalData;
        tableDataModel.setRowCount(0);
        for (RunConfigData runConfigData : this.runConfiguration.getDatas()) {
            
            if(StringUtils.isNotBlank(runConfigData.getRefId())){
                if(DataTypes.GLOBAL_DATA.name().equals(runConfigData.getType())){
                    globalData = CGeneratorContentManager.getInstance().getCgenetatorConfig().getGlobalDatas().stream().filter(a->a.getId().equals(runConfigData.getRefId())).findFirst().orElse(null);

                    Object[] rowData = {
                        runConfigData.getTags(),
                        runConfigData.getCode(),
                        globalData.getName(), 
                        runConfigData.isActive()
                    };
                    tableDataModel.addRow(rowData);
                }else if(DataTypes.OPENAPI.name().equals(runConfigData.getType())){
                    OpenApiImported openApiImported = CGeneratorContentManager.getInstance().getCgenetatorConfig().getOpenAPIs().stream().filter(a->a.getId().equals(runConfigData.getRefId())).findFirst().orElse(null);

                    Object[] rowData = {
                        runConfigData.getTags(),
                        runConfigData.getCode(),
                        ((Map<?,?>)openApiImported.getData().get("info")).get("title").toString(),
                        runConfigData.isActive()
                    };
                    tableDataModel.addRow(rowData);
                }
            }else{
                Object[] rowData = {
                    runConfigData.getTags(),
                    runConfigData.getCode(),
                    runConfigData.getName(), 
                    runConfigData.isActive()
                };
                tableDataModel.addRow(rowData);
            }
            
        }
    }
     
    private void refreshArtifact() {
        artifactModel.setRowCount(0);
        Artifact artifact;
        for (RunConfigArtifact runConfigArtifact : this.runConfiguration.getArtifacts()) {
            artifact = CGeneratorContentManager.getInstance().getCgenetatorConfig().getArtifacts().stream().filter(a->a.getId().equals(runConfigArtifact.getArtifactId())).findFirst().orElse(null);
            if(artifact != null){ 
                Object[] rowData = {
                    artifact.getLabel(),
                    artifact.getDescription(), 
                    runConfigArtifact.isActive() 
                };
                artifactModel.addRow(rowData);
            }
        }
    }
    
    private void initEditorComponetInspector() {
        List<ComponentInspector> components = new ArrayList<>();
        
        components.add(new JTextEditorComponentInspector(txtName, null)); 
        components.add(new JTextEditorComponentInspector(txtDescription, null)); 
        components.add(new JTextEditorComponentInspector(txtRootPath, null)); 
        components.add(new JComboBoxComponentInspector(cmbArtifiact, null));  
        components.add(new JTableEditorComponentInspector(tbArtifactData, null));  
        components.add(new JTableEditorComponentInspector(tableData, null));  
        
        components.add(new JTextPaneComponentInspector(txtPanelAdditionalData, null));  
        
        editComponentsInspector = new EditComponentsInspector(components, btnApply);
    }  

}
