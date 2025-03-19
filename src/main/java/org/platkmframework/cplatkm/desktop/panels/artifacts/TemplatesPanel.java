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
package org.platkmframework.cplatkm.desktop.panels.artifacts;
 

import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
 
import org.platkmframework.cplatkm.desktop.core.CGeneratorContentManager;
import org.platkmframework.cplatkm.processor.data.Artifact;
import org.platkmframework.cplatkm.processor.data.Template;
import org.platkmframework.cplatkm.processor.exception.CGeneratorException;

public class TemplatesPanel extends JPanel {

    private Artifact artifact;
    
    private JTable table;
    private final DefaultTableModel tableModel; 
    private TemplateEditorJPanel templateEditorJPanel;
    private JScrollPane scrollPaneMain;

    public TemplatesPanel(TemplateEditorJPanel templateEditorJPanel, JScrollPane scrollPaneMain) {
        setLayout(new BorderLayout());
        this.templateEditorJPanel = templateEditorJPanel;
        this.scrollPaneMain = scrollPaneMain;
        // Datos iniciales simulados 

        // Crear el modelo de la tabla
        String[] columnNames = { "Name"  };
        tableModel = new DefaultTableModel(columnNames, 0){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Verifica si se hizo doble clic
                if (e.getClickCount() == 2) {
                    int fila = table.getSelectedRow();
                    if (fila >=0 ) {
                        editTemplate();
                    }
                }
            }
        });        

        // Panel de botones
        JPanel buttonPanel = new JPanel();
        JButton btnCreate = new JButton("Create");
        JButton btnEdit = new JButton("Edit");
        JButton btnDelete = new JButton("Remove");

        btnCreate.addActionListener(e -> createTemplate());
        btnEdit.addActionListener(e -> editTemplate());
        btnDelete.addActionListener(e -> deleteTemplate());

        buttonPanel.add(btnCreate);
        buttonPanel.add(btnEdit);
        buttonPanel.add(btnDelete);

        // Agregar la tabla y el panel de botones
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    public void refreshTable() {
        tableModel.setRowCount(0);
        for (Template art : artifact.getTemplates()) {
            Object[] rowData = {
                art.getLabel()
            };
            tableModel.addRow(rowData);
        }
    }

    private void createTemplate() {
        templateEditorJPanel.setData(artifact, null, 0);
        this.scrollPaneMain.setViewportView(this.templateEditorJPanel);
        /**    
        Template newItem = showArtifactDialog(null);
        if (newItem != null) {
            newItem.setId(Util.randomAlfaNumericString(255));
            newItem.setTemplatename(newItem.getLabel().strip() + "_" + Util.randomAlfaNumericString(10));
            try {
                
                CGeneratorContentManager.getInstance().createTemplateFile(artifact.getFoldername(), newItem.getTemplatename());
                artifact.getTemplates().add(newItem);
                refreshTable();
                CGeneratorContentManager.getInstance().templateAddNewNode(newItem, artifact);
                
                CGeneratorContentManager.getInstance().updateConfigFile();
            } catch (CGeneratorException ex) {
                Logger.getLogger(TemplatesPanel.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(this,
                ex.getMessage(),
                "Create", JOptionPane.WARNING_MESSAGE);
            }
        }*/
    }

    private void editTemplate() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            Template current = artifact.getTemplates().get(selectedRow);
            templateEditorJPanel.setData(artifact, current, selectedRow);
            this.scrollPaneMain.setViewportView(this.templateEditorJPanel);
            /**
            Template  updated = showArtifactDialog(current, );
            if (updated != null) {
                try {
                    
                    artifact.getTemplates().set(selectedRow, updated);
                    refreshTable();
                
                    CGeneratorContentManager.getInstance().templateUpdateNewNode(updated);
                
                    CGeneratorContentManager.getInstance().updateConfigFile();
                } catch (CGeneratorException ex) {
                    Logger.getLogger(TemplatesPanel.class.getName()).log(Level.SEVERE, null, ex);
                    JOptionPane.showMessageDialog(this,
                    ex.getMessage(),
                    "Edit", JOptionPane.WARNING_MESSAGE);
                }
            }*/
        }else {
            JOptionPane.showMessageDialog(this,
                "Select element to edit.",
                "Edit", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void deleteTemplate() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            
            int response = JOptionPane.showConfirmDialog(null, 
                "Are you sure you want to delete this record?", "Delete Confirmation", 
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
             
            if (response == JOptionPane.YES_OPTION) {    
                try {
                    
                    Template template = artifact.getTemplates().get(selectedRow);
                    artifact.getTemplates().remove(selectedRow);
                    refreshTable();
                    //CGeneratorConfigManager.getInstance().treeRemoveNodeFromParentType(TreeNodeTypes.TEMPLATE_SEPARATOR_TYPE, artifact.getId(), template.getId());
                    CGeneratorContentManager.getInstance().templateRemoveNewNode(template, artifact);
                    CGeneratorContentManager.getInstance().updateConfigFile();
                    
                } catch (CGeneratorException ex) {
                    Logger.getLogger(TemplatesPanel.class.getName()).log(Level.SEVERE, null, ex);
                    JOptionPane.showMessageDialog(this,
                    ex.getMessage(),
                    "Remove", JOptionPane.WARNING_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this,
                "Select an element to remove.",
                "Delete", JOptionPane.WARNING_MESSAGE);
        }
    }
 

    public Artifact getArtifact() {
        return artifact;
    }

    public void setArtifact(Artifact artifact) {
        this.artifact = artifact;
        refreshTable();
    }
   
}
