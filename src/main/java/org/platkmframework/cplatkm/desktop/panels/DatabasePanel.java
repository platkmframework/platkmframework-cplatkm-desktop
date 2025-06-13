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
package org.platkmframework.cplatkm.desktop.panels;
 

import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.table.DefaultTableModel; 
import org.platkmframework.cplatkm.desktop.core.CPlatkmContentManager;
import org.platkmframework.cplatkm.processor.data.DatabaseData;
import org.platkmframework.cplatkm.processor.exception.CPlatkmException; 


/**
 *
 * @author Eduardo Iglesias 
 */
public final class DatabasePanel extends JPanel {

    private JTable table;
    private DefaultTableModel tableModel; 
    private JScrollPane scrollPaneMain;
    private DataBaseEditorPanel dataBaseEditorPanel;
    
    //private DataBaseJDialog dataBaseJDialog;

   public DatabasePanel(DataBaseEditorPanel dataBaseEditorPanel, JScrollPane scrollPaneMain) {
        this.scrollPaneMain = scrollPaneMain;
        this.dataBaseEditorPanel = dataBaseEditorPanel;
        setLayout(new BorderLayout());
        
        //dataBaseJDialog = new DataBaseJDialog(CPlatkmContentManager.getInstance().getMainFrame(), true);

        // Crear modelo de tabla
        String[] columnNames = { "Name", "Driver", "URL" };
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
                        editDatabase();
                    }
                }
            }
        });
        refreshTable();

        // Panel con botones CRUD
        JPanel buttonPanel = new JPanel();
        JButton btnCreate = new JButton("Create");
        JButton btnEdit = new JButton("Edit");
        JButton btnDelete = new JButton("Delete");

        // Asignar acciones a los botones
        btnCreate.addActionListener(e -> createDatabase());
        btnEdit.addActionListener(e -> editDatabase());
        btnDelete.addActionListener(e -> deleteDatabase());

        buttonPanel.add(btnCreate);
        buttonPanel.add(btnEdit);
        buttonPanel.add(btnDelete);

        // Agregar componentes al panel
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

 

    /**
     * Actualiza los datos mostrados en la tabla.
     */
    public void refreshTable() {
        tableModel.setRowCount(0);
        for (DatabaseData db : CPlatkmContentManager.getInstance().getCgenetatorConfig().getDatabases()) {
            Object[] rowData = {
                db.getName(),
                db.getDriver(),
                db.getUrl()
            };
            tableModel.addRow(rowData);
        }
    }

    /**
     * Crear un nuevo DatabaseItem.
     */
    private void createDatabase() {
        this.dataBaseEditorPanel.setData(null);
        this.scrollPaneMain.setViewportView(this.dataBaseEditorPanel);
    }

    /**
     * Editar el DatabaseItem seleccionado en la tabla.
     */
    private void editDatabase() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            DatabaseData current = CPlatkmContentManager.getInstance().getCgenetatorConfig().getDatabases().get(selectedRow);
            this.dataBaseEditorPanel.setData(current);
            this.scrollPaneMain.setViewportView(this.dataBaseEditorPanel);
 
        } else {
            JOptionPane.showMessageDialog(this,
                "You should select a record to update.",
                "Edit", JOptionPane.WARNING_MESSAGE);
        }
    }

    /**
     * Eliminar el DatabaseItem seleccionado en la tabla.
     */
    private void deleteDatabase() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            
            int response = JOptionPane.showConfirmDialog(null, 
                "Are you sure you want to delete this record?", "Delete Confirmation", 
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
             
            if (response == JOptionPane.YES_OPTION) {
                try {
                    CPlatkmContentManager.getInstance().getCgenetatorConfig().getDatabases().remove(selectedRow);
                    refreshTable();
                    CPlatkmContentManager.getInstance().refreshDataBaseSeparator();
                
                    CPlatkmContentManager.getInstance().updateConfigFile();
                } catch (CPlatkmException ex) {
                    Logger.getLogger(DatabasePanel.class.getName()).log(Level.SEVERE, null, ex);
                     JOptionPane.showMessageDialog(this,
                    ex.getMessage(),
                    "Edit", JOptionPane.WARNING_MESSAGE);
                }                
            }

        } else {
            JOptionPane.showMessageDialog(this,
                "You shuold select and element before remove.",
                "Delete", JOptionPane.WARNING_MESSAGE);
        }
    }

     
}

