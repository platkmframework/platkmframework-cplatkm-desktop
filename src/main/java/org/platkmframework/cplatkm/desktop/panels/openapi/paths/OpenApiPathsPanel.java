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
package org.platkmframework.cplatkm.desktop.panels.openapi.paths;
 

import org.platkmframework.cplatkm.desktop.panels.openapi.tags.OpenTagsDialog;
import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
 
import org.platkmframework.cplatkm.desktop.core.CPlatkmContentManager;
import org.platkmframework.cplatkm.processor.data.openapi.CGOpenAPI;
import org.platkmframework.cplatkm.processor.data.openapi.CGPaths;
import org.platkmframework.cplatkm.processor.data.openapi.CGTags;
import org.platkmframework.cplatkm.processor.exception.CPlatkmException;

public class OpenApiPathsPanel extends JPanel {

    private CGTags cgTagsFound;
    private CGOpenAPI cgOpenAPIFound;
    
    private JTable table;
    private DefaultTableModel tableModel; 
    private OpenTagsDialog openTagsDialog;

    public OpenApiPathsPanel() {
        setLayout(new BorderLayout());
        openTagsDialog = new OpenTagsDialog(null, true);
        // Datos iniciales simulados 

        // Crear el modelo de la tabla
        String[] columnNames = { "Summary"  };
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
                      //  editTags();
                    }
                }
            }
        });        

        // Panel de botones
        JPanel buttonPanel = new JPanel();
        //JButton btnCreate = new JButton("Create");
        //JButton btnEdit = new JButton("Edit");
        JButton btnDelete = new JButton("Remove");

        //btnCreate.addActionListener(e -> createCGPaths());
        //btnEdit.addActionListener(e -> editPaths());
        btnDelete.addActionListener(e -> deletePaths());

        //buttonPanel.add(btnCreate);
        //buttonPanel.add(btnEdit);
        buttonPanel.add(btnDelete);

        // Agregar la tabla y el panel de botones
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    public void refreshTable() {
        refreshTableFromtable(this.cgOpenAPIFound.getId(), this.cgTagsFound.getId());
    }
    
    public void refreshTableFromtable(String openApiParentId, String tagParentId) {
 
    }

    
    private void deletePaths() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            
            int response = JOptionPane.showConfirmDialog(null, 
                "Are you sure you want to delete this record?", "Delete Confirmation", 
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
             
            if (response == JOptionPane.YES_OPTION) {    
                try {
                    
                    CGPaths cgPaths = cgTagsFound.getPaths().get(selectedRow);
                    cgTagsFound.getPaths().remove(selectedRow);
                    refreshTable();
                    CPlatkmContentManager.getInstance().pathsRemoveNewNode(cgPaths, cgTagsFound);
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
   
}
