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
 
    }

    private void createDateType() {
      
    }

    private void editDataType() {
 
    }

    private void deleteDataType() {
   
    }

    
    private CGOpenAPI showArtifactDialog(CGOpenAPI item) {
 
       return null;
    }
   
}
