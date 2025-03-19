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
import org.platkmframework.cplatkm.desktop.core.CGeneratorContentManager;
import org.platkmframework.cplatkm.processor.data.DatabaseData;
import org.platkmframework.cplatkm.processor.exception.CGeneratorException; 


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
        
        //dataBaseJDialog = new DataBaseJDialog(CGeneratorContentManager.getInstance().getMainFrame(), true);

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
        for (DatabaseData db : CGeneratorContentManager.getInstance().getCgenetatorConfig().getDatabases()) {
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
        /** 
        DatabaseData dbItem = showDatabaseDialog(null);
        if (dbItem != null) {
            try {
                 
                dbItem.setId(Util.generateId(255));
                CGeneratorContentManager.getInstance().getCgenetatorConfig().getDatabases().add(dbItem);
                refreshTable();
                
                CGeneratorContentManager.getInstance().refreshDataBaseSeparator();
                
                CGeneratorContentManager.getInstance().updateConfigFile();
            } catch (CGeneratorException ex) {
                Logger.getLogger(DatabasePanel.class.getName()).log(Level.SEVERE, null, ex);
                 JOptionPane.showMessageDialog(this,
                ex.getMessage(),
                "Edit", JOptionPane.WARNING_MESSAGE);
            }
        }*/
    }

    /**
     * Editar el DatabaseItem seleccionado en la tabla.
     */
    private void editDatabase() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            DatabaseData current = CGeneratorContentManager.getInstance().getCgenetatorConfig().getDatabases().get(selectedRow);
            this.dataBaseEditorPanel.setData(current);
            this.scrollPaneMain.setViewportView(this.dataBaseEditorPanel);
            /**
             * DatabaseData updated = showDatabaseDialog(current);
            if (updated != null) {
                try {
                    // Reemplaza el antiguo por el nuevo
                    CGeneratorContentManager.getInstance().getCgenetatorConfig().getDatabases().set(selectedRow, updated);
                    refreshTable();
                    CGeneratorContentManager.getInstance().refreshDataBaseSeparator();
                    CGeneratorContentManager.getInstance().updateConfigFile();
                } catch (CGeneratorException ex) {
                    Logger.getLogger(DatabasePanel.class.getName()).log(Level.SEVERE, null, ex);
                    
                    JOptionPane.showMessageDialog(this,
                ex.getMessage(),
                "Edit", JOptionPane.WARNING_MESSAGE);
                }
            }*/
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
                    CGeneratorContentManager.getInstance().getCgenetatorConfig().getDatabases().remove(selectedRow);
                    refreshTable();
                    CGeneratorContentManager.getInstance().refreshDataBaseSeparator();
                
                    CGeneratorContentManager.getInstance().updateConfigFile();
                } catch (CGeneratorException ex) {
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

    /**
     * Muestra un diálogo para crear/editar un DatabaseItem.
     * @param item Si es null, se crea uno nuevo; si no, se edita el existente.
     * @return El DatabaseItem creado/editado o null si se cancela la operación.
     
    private DatabaseData showDatabaseDialog(DatabaseData item) {
     
        //dataBaseJDialog.setLocationRelativeTo(CGeneratorContentManager.getInstance().getMainFrame()); 
       dataBaseJDialog.setData(item); 
        dataBaseJDialog.setVisible(true);
        if(dataBaseJDialog.updated) return dataBaseJDialog.item; else return null;
        
      
        
//        JTextField nameField = new JTextField(20);
//        JTextField driverField = new JTextField(20);
//        JTextField urlField = new JTextField(20);
//        JTextField userField = new JTextField(20);
//        JTextField passwordField = new JTextField(20);
//        JTextField catalogField = new JTextField(20);
//        JTextField schemaField = new JTextField(20);
//        JTextField excludedFieldsField = new JTextField(20);
//        JTextField excludedTableField = new JTextField(20);
/*        
        if (item != null) {
            nameField.setText(item.getName());
            driverField.setText(item.getDriver());
            urlField.setText(item.getUrl());
            userField.setText(item.getUser());
            passwordField.setText(item.getPassword());
            catalogField.setText(item.getCatalog());
            schemaField.setText(item.getSchema());
            excludedFieldsField.setText(item.getExcludedFields());
            excludedTableField.setText(item.getExcludedTables());
        }

 
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(new JLabel("Name:"));
        panel.add(nameField);
        
        panel.add(new JLabel("Driver:"));
        panel.add(driverField);
        
        panel.add(new JLabel("URL:"));
        panel.add(urlField);
        
        panel.add(new JLabel("User:"));
        panel.add(userField);
        
        panel.add(new JLabel("Password:"));
        panel.add(passwordField);
        
        panel.add(new JLabel("Catalog:"));
        panel.add(catalogField);
        
        panel.add(new JLabel("Schema:"));
        panel.add(schemaField);
        
        panel.add(new JLabel("Excluded Fields:"));
        panel.add(excludedFieldsField);
        
        panel.add(new JLabel("Excluded Tables:"));
        panel.add(excludedTableField);

        int result = JOptionPane.showConfirmDialog(
            this,
            panel,
            (item == null ? "Create Database" : "Edit  Database"),
            JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.PLAIN_MESSAGE
        );

        if (result == JOptionPane.OK_OPTION) {
            
            if(StringUtils.isBlank(nameField.getText().trim())){
                return null;
            }else{
                if (item == null) {
                    // Crear nuevo
                    item = new DatabaseData();  
                }  

                // Actualizar existente
                item.setName(nameField.getText().trim());
                item.setDriver(driverField.getText().trim());
                item.setUrl(urlField.getText().trim());
                item.setUser(userField.getText().trim());
                item.setPassword(passwordField.getText().trim());   
                item.setCatalog(catalogField.getText().trim()); 
                item.setSchema(schemaField.getText().trim());
                item.setExcludedFields(excludedFieldsField.getText().trim());
                item.setExcludedTables(excludedTableField.getText().trim());

                return item;
            }
        }
        return null; // Se canceló

       
    } */
}

