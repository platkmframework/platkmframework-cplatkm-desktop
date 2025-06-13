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
 

import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.table.DefaultTableModel; 
import org.platkmframework.cplatkm.desktop.core.CPlatkmContentManager;
import org.platkmframework.cplatkm.desktop.panels.datatype.DataTypePanel;
import org.platkmframework.cplatkm.processor.data.RunConfiguration;
import org.platkmframework.cplatkm.processor.exception.CPlatkmException;

/**
 *   Author:
 *     Eduardo Iglesias
 *   Contributors:
 *   	Eduardo Iglesias - initial API and implementation
 */
public final class RunConfigurationPanel extends JPanel {

    private JTable table;
    private final DefaultTableModel tableModel; 
    private RunConfigurationEditorPanel runConfigurationEditorPanel;
    private JScrollPane scrollPaneMain;
    

    public RunConfigurationPanel(RunConfigurationEditorPanel runConfigurationEditorPanel, JScrollPane scrollPaneMain) {
        setLayout(new BorderLayout());
        this.runConfigurationEditorPanel = runConfigurationEditorPanel;
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
                        editRunConfiguration();
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

        btnCreate.addActionListener(e -> createRunconfiguration());
        btnEdit.addActionListener(e -> editRunConfiguration());
        btnDelete.addActionListener(e -> deleteRunConfiguration());

        buttonPanel.add(btnCreate);
        buttonPanel.add(btnEdit);
        buttonPanel.add(btnDelete);

        // Agregar la tabla y el panel de botones
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    public void refreshTable() {
        tableModel.setRowCount(0);
        for (RunConfiguration art : CPlatkmContentManager.getInstance().getCgenetatorConfig().getRunConfigurations()) {
            Object[] rowData = {
                art.getName()
            };
            tableModel.addRow(rowData);
        }
    }

    private void createRunconfiguration() {
        runConfigurationEditorPanel.setData(null);
        scrollPaneMain.setViewportView(runConfigurationEditorPanel);
        refreshTable();
    }

    private void editRunConfiguration() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            RunConfiguration  runConfiguration = CPlatkmContentManager.getInstance().getCgenetatorConfig().getRunConfigurations().get(selectedRow);
            if(runConfiguration != null){
                runConfigurationEditorPanel.setData(runConfiguration);
                scrollPaneMain.setViewportView(runConfigurationEditorPanel);
                refreshTable();
            }
        }
    }

    private void deleteRunConfiguration() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            
            int response = JOptionPane.showConfirmDialog(null, 
                "Are you sure you want to delete this record?", "Delete Confirmation", 
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
             
            if (response == JOptionPane.YES_OPTION) {    
                try {
                    CPlatkmContentManager.getInstance().getCgenetatorConfig().getRunConfigurations().remove(selectedRow);
                    CPlatkmContentManager.getInstance().refreshRunConfigurationSeparator();
                    refreshTable();
                
                    CPlatkmContentManager.getInstance().updateConfigFile();
                } catch (CPlatkmException ex) {
                    Logger.getLogger(DataTypePanel.class.getName()).log(Level.SEVERE, null, ex);
                    JOptionPane.showMessageDialog(this,
                    ex.getMessage(),
                    "Edit", JOptionPane.WARNING_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this,
                "You should select a record to delete.",
                "Remove", JOptionPane.WARNING_MESSAGE);
        }
    } 
}
