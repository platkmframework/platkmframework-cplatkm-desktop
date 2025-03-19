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
import org.platkmframework.cplatkm.processor.exception.CGeneratorException;

/**
 *   Author:
 *     Eduardo Iglesias
 *   Contributors:
 *   	Eduardo Iglesias - initial API and implementation
 */
public final class ArtifactPanel extends JPanel {

    private JTable table;
    private final DefaultTableModel tableModel; 
    private ArtifactEditorPanel artifactEditorPanel;
    private JScrollPane scrollPaneMain;

    public ArtifactPanel(ArtifactEditorPanel artifactEditorPanel, JScrollPane scrollPaneMain) {
        setLayout(new BorderLayout());
        this.artifactEditorPanel = artifactEditorPanel;
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
                        editArtifact();
                    }
                }
            }
        });
        refreshTable();

        // Panel de botones
        JPanel buttonPanel = new JPanel();
        JButton btnCreate = new JButton("Create");
        JButton btnEdit = new JButton("Edit");
        JButton btnDelete = new JButton("Delete");

        btnCreate.addActionListener(e -> createArtifact());
        btnEdit.addActionListener(e -> editArtifact());
        btnDelete.addActionListener(e -> deleteArtifact());

        buttonPanel.add(btnCreate);
        buttonPanel.add(btnEdit);
        buttonPanel.add(btnDelete);

        // Agregar la tabla y el panel de botones
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    public void refreshTable() {
        tableModel.setRowCount(0);
        for (Artifact art : CGeneratorContentManager.getInstance().getCgenetatorConfig().getArtifacts()) {
            Object[] rowData = {
                art.getLabel()
            };
            tableModel.addRow(rowData);
        }
    }

    private void createArtifact() {
        this.artifactEditorPanel.setData(null);
        this.scrollPaneMain.setViewportView(this.artifactEditorPanel);
    /**    
        Artifact newItem = showArtifactDialog(null);
        if (newItem != null) {
            newItem.setId(Util.randomAlfaNumericString(255));
            newItem.setFoldername(newItem.getLabel().strip() + "_" + Util.randomAlfaNumericString(10).replaceAll(" ", "_"));
            try {
                
                CGeneratorContentManager.getInstance().createArtifactFolder(newItem.getFoldername());
                CGeneratorContentManager.getInstance().getCgenetatorConfig().getArtifacts().add(newItem);
                refreshTable();
                CGeneratorContentManager.getInstance().artifactAddNewNode(newItem);
                
                /**
                CGTreeNode treeNodeTemplateSeparator = new CGTreeNode("Templates", TreeNodeTypes.TEMPLATE_SEPARATOR_TYPE.name());
                treeNodeTemplateSeparator.setParentId(newItem.getId());
                CGTreeNode treeNodeArtNew  = new CGTreeNode(newItem, TreeNodeTypes.ARTIFACT_TYPE.name(), newItem.getLabel(), newItem.getId());
                treeNodeArtNew.add(treeNodeTemplateSeparator);  
                
                CGeneratorConfigManager.getInstance().treeAddFNodeFromParentType(TreeNodeTypes.ARTIFACT_SEPARATOR_TYPE, treeNodeArtNew);
                *
                
                CGeneratorContentManager.getInstance().updateConfigFile();
            } catch (CGeneratorException ex) {
                Logger.getLogger(ArtifactPanel.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(this,
                ex.getMessage(),
                "Create", JOptionPane.WARNING_MESSAGE);
            }
        }*/
    }

    private void editArtifact() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            Artifact current = CGeneratorContentManager.getInstance().getCgenetatorConfig().getArtifacts().get(selectedRow);
            this.artifactEditorPanel.setData(current);
            this.scrollPaneMain.setViewportView(this.artifactEditorPanel);
        
            /**        Artifact  updated = showArtifactDialog(current);
            if (updated != null) {
                
                try {
                    CGeneratorContentManager.getInstance().getCgenetatorConfig().getArtifacts().set(selectedRow, updated);
                    refreshTable();
                    CGeneratorContentManager.getInstance().artifactUpdateNewNode(artifactDialog.item);
                
                    CGeneratorContentManager.getInstance().updateConfigFile();
                } catch (CGeneratorException ex) {
                    Logger.getLogger(ArtifactPanel.class.getName()).log(Level.SEVERE, null, ex);
                    JOptionPane.showMessageDialog(this,
                    ex.getMessage(),
                    "Edit", JOptionPane.WARNING_MESSAGE);
                }
            }
            */
        } else {
            JOptionPane.showMessageDialog(this,
                "You should select the record to update.",
                "Edit", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void deleteArtifact() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            
            int response = JOptionPane.showConfirmDialog(null, 
                "Are you sure you want to delete this record?", "Delete Confirmation", 
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
             
            if (response == JOptionPane.YES_OPTION) {
                
                try {
                    Artifact artifact = CGeneratorContentManager.getInstance().getCgenetatorConfig().getArtifacts().get(selectedRow);
                    CGeneratorContentManager.getInstance().getCgenetatorConfig().getArtifacts().remove(selectedRow);
                    refreshTable();
                    CGeneratorContentManager.getInstance().artifactRemoveNewNode(artifact);

                    CGeneratorContentManager.getInstance().updateConfigFile();
                    
                } catch (CGeneratorException ex) {
                    Logger.getLogger(ArtifactPanel.class.getName()).log(Level.SEVERE, null, ex);
                    JOptionPane.showMessageDialog(this,
                    ex.getMessage(),
                    "Remove", JOptionPane.WARNING_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this,
                "You shoul select the record to remove.",
                "Delete", JOptionPane.WARNING_MESSAGE);
        }
    }

    /**
     * Muestra un diálogo para crear/editar un ArtifactItem.
     * @param item Si es null, se crea uno nuevo; si no, se edita el existente.
     * @return El ArtifactItem creado/editado o null si se canceló.
   
    private Artifact  showArtifactDialog(Artifact item) {
        if(artifactDialog == null)  
            artifactDialog = new ArtifactDialog( CGeneratorContentManager.getInstance().getMainFrame(), true);
        
         artifactDialog.setLocationRelativeTo(CGeneratorContentManager.getInstance().getMainFrame());    
        artifactDialog.setData(item);
        artifactDialog.setVisible(true);
        
        if(artifactDialog.isUdpdated()) 
            return artifactDialog.getItem(); 
        else 
        return null;
    }
     */
}
