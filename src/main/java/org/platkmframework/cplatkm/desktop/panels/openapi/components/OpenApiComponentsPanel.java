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
package org.platkmframework.cplatkm.desktop.panels.openapi.components;
 
  
 
import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent; 
import javax.swing.*;
import javax.swing.table.DefaultTableModel;  
import org.platkmframework.cplatkm.desktop.panels.openapi.OpenApiDialog;
import org.platkmframework.cplatkm.processor.data.openapi.CGOpenAPI; 
/**
 *   Author:
 *     Eduardo Iglesias
 *   Contributors:
 *   	Eduardo Iglesias - initial API and implementation
 */
public final class OpenApiComponentsPanel extends JPanel {

    private JTable table;
    private DefaultTableModel tableModel; 
    private OpenApiDialog openApiDialog = null;

    public OpenApiComponentsPanel() {
        setLayout(new BorderLayout());
    
        // Datos iniciales simulados 

        // Crear el modelo de la tabla
        String[] columnNames = { "Title"  };
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
                        editDataType();
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

        btnCreate.addActionListener(e -> createDateType());
        btnEdit.addActionListener(e -> editDataType());
        btnDelete.addActionListener(e -> deleteDataType());

        buttonPanel.add(btnCreate);
        buttonPanel.add(btnEdit);
        buttonPanel.add(btnDelete);

        // Agregar la tabla y el panel de botones
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    public void refreshTable() {
    /**    tableModel.setRowCount(0);
        for (CGOpenAPI gd : CGeneratorContentManager.getInstance().getCgenetatorConfig().getOpenAPIs()) {
            Object[] rowData = {
                gd.getInfo().getTitle()
            };
            tableModel.addRow(rowData);
        }*/
    }

    private void createDateType() {
      /**  CGOpenAPI newItem = showArtifactDialog(null);
        if (newItem != null) {
            newItem.setId(Util.randomAlfaNumericString(255)); 
            try {
            
                CGeneratorContentManager.getInstance().getCgenetatorConfig().getOpenAPIs().add(newItem);
                refreshTable();
                CGeneratorContentManager.getInstance().refreshGlobalDataSeparator();  
                
                CGeneratorContentManager.getInstance().updateConfigFile();
                
            } catch (CGeneratorException ex) {
                Logger.getLogger(OpenApiComponentsPanel.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(this,
                ex.getMessage(),
                "Create", JOptionPane.WARNING_MESSAGE);
            }
        }*/
    }

    private void editDataType() {
       /** int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            CGOpenAPI  current = CGeneratorContentManager.getInstance().getCgenetatorConfig().getOpenAPIs().get(selectedRow);
            CGOpenAPI  updated = showArtifactDialog(current);
            if (updated != null) {
                try {
                    CGeneratorContentManager.getInstance().getCgenetatorConfig().getOpenAPIs().set(selectedRow, updated);
                    refreshTable();
                    CGeneratorContentManager.getInstance().refreshGlobalDataSeparator();
                
                    CGeneratorContentManager.getInstance().updateConfigFile();
                } catch (CGeneratorException ex) {
                    Logger.getLogger(OpenApiComponentsPanel.class.getName()).log(Level.SEVERE, null, ex);
                    JOptionPane.showMessageDialog(this,
                    ex.getMessage(),
                    "Edit", JOptionPane.WARNING_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this,
                "You should select a record to update.",
                "Edit", JOptionPane.WARNING_MESSAGE);
        }*/
    }

    private void deleteDataType() {
   /**     int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            
            int response = JOptionPane.showConfirmDialog(null, 
                "Are you sure you want to delete this record?", "Delete Confirmation", 
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
             
            if (response == JOptionPane.YES_OPTION) { 
                try {
                    CGeneratorConfigManager.getInstance().getCgenetatorConfig().getOpenAPIs().remove(selectedRow);
                    refreshTable();
                    CGeneratorConfigManager.getInstance().refreshOpenAPISeparator();

                    CGeneratorConfigManager.getInstance().updateConfigFile();
                } catch (CGeneratorException ex) {
                    Logger.getLogger(OpenApiComponentsPanel.class.getName()).log(Level.SEVERE, null, ex);
                    JOptionPane.showMessageDialog(this,
                    ex.getMessage(),
                    "Delete Conformation", JOptionPane.WARNING_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this,
                "You should select a record to  delete.",
                "Delete", JOptionPane.WARNING_MESSAGE);
        }*/
    }

    
    private CGOpenAPI showArtifactDialog(CGOpenAPI item) {
       /**   if(openApiDialog == null)  
            openApiDialog = new OpenApiDialog( CGeneratorConfigManager.getInstance().getMainFrame(), true);
        
        openApiDialog.setLocationRelativeTo(CGeneratorConfigManager.getInstance().getMainFrame());    
        openApiDialog.setData(item);
        openApiDialog.setVisible(true);
        
        if(openApiDialog.isUpdated()) 
            return openApiDialog.getCGOpenAPI(); 
        else 
            return null;*/
       return null;
    }
   
}
