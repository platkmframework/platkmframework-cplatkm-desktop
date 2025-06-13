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
package org.platkmframework.cplatkm.desktop.panels.openapi.tags;
 

import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
 
import org.platkmframework.cplatkm.desktop.core.CPlatkmContentManager;
import org.platkmframework.cplatkm.processor.data.openapi.CGOpenAPI;
import org.platkmframework.cplatkm.processor.data.openapi.CGTags;
import org.platkmframework.cplatkm.processor.exception.CPlatkmException;
import org.platkmframework.util.Util;

public class OpenTagsPanel extends JPanel {

    private CGOpenAPI cgOpenAPI;
    
    private JTable table;
    private DefaultTableModel tableModel; 
    private OpenTagsDialog openTagsDialog;

    public OpenTagsPanel() {
        setLayout(new BorderLayout());
        openTagsDialog = new OpenTagsDialog(null, true);
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
                        editTags();
                    }
                }
            }
        });        

        // Panel de botones
        JPanel buttonPanel = new JPanel();
        JButton btnCreate = new JButton("Create");
        JButton btnEdit = new JButton("Edit");
        JButton btnDelete = new JButton("Remove");

        btnCreate.addActionListener(e -> createTags());
        btnEdit.addActionListener(e -> editTags());
        btnDelete.addActionListener(e -> deleteTags());

        buttonPanel.add(btnCreate);
        buttonPanel.add(btnEdit);
        buttonPanel.add(btnDelete);

        // Agregar la tabla y el panel de botones
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    public void refreshTable() {
         refreshTableFromtable(this.cgOpenAPI.getId());
    }
    
    public void refreshTableFromtable(String parentId) {
 
    }

    private void createTags() {
        CGTags newItem = showArtifactDialog(null);
        if (newItem != null) {
            newItem.setId(Util.randomAlfaNumericString(255)); 
            try {
                cgOpenAPI.getTags().add(newItem);
                refreshTable();
                CPlatkmContentManager.getInstance().tagsAddNewNode(newItem, cgOpenAPI);
                
                CPlatkmContentManager.getInstance().updateConfigFile();
            } catch (CPlatkmException ex) {
                Logger.getLogger(OpenTagsPanel.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(this,
                ex.getMessage(),
                "Create", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    private void editTags() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            CGTags  current = cgOpenAPI.getTags().get(selectedRow);
            CGTags  updated = showArtifactDialog(current);
            if (updated != null) {
                try {
                    
                    cgOpenAPI.getTags().set(selectedRow, updated);
                    refreshTable();
                
                    CPlatkmContentManager.getInstance().tagsUpdateNewNode(updated);
                
                    CPlatkmContentManager.getInstance().updateConfigFile();
                } catch (CPlatkmException ex) {
                    Logger.getLogger(OpenTagsPanel.class.getName()).log(Level.SEVERE, null, ex);
                    JOptionPane.showMessageDialog(this,
                    ex.getMessage(),
                    "Edit", JOptionPane.WARNING_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this,
                "Select element to edit.",
                "Edit", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void deleteTags() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            
            int response = JOptionPane.showConfirmDialog(null, 
                "Are you sure you want to delete this record?", "Delete Confirmation", 
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
             
            if (response == JOptionPane.YES_OPTION) {    
                try {
                    
                    CGTags cgTags = cgOpenAPI.getTags().get(selectedRow);
                    cgOpenAPI.getTags().remove(selectedRow);
                    refreshTable();
                    CPlatkmContentManager.getInstance().tagsRemoveNewNode(cgTags, cgOpenAPI);
                    CPlatkmContentManager.getInstance().updateConfigFile();
                    
                } catch (CPlatkmException ex) {
                    Logger.getLogger(JOptionPane.class.getName()).log(Level.SEVERE, null, ex);
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

    /**
     * Muestra un diálogo para crear/editar un ArtifactItem.
     * @param item Si es null, se crea uno nuevo; si no, se edita el existente.
     * @return El ArtifactItem creado/editado o null si se canceló.
     */
    private CGTags  showArtifactDialog(CGTags item) {
        
        openTagsDialog.setData(item);
        openTagsDialog.setLocationRelativeTo(CPlatkmContentManager.getInstance().getMainFrame());
        openTagsDialog.setVisible(true);
        
        if(openTagsDialog.isUdpdated()) 
            return openTagsDialog.getItem(); 
        else 
        return null;
    
    }

    public CGOpenAPI getCgOpenAPI() {
        return cgOpenAPI;
    }

    public void setCgOpenAPI(CGOpenAPI cgOpenAPI) {
        this.cgOpenAPI = cgOpenAPI;
        refreshTable();
    }
 
   
}
