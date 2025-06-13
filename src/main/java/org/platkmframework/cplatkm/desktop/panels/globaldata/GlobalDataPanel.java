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
package org.platkmframework.cplatkm.desktop.panels.globaldata;
 
  
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.table.DefaultTableModel; 
import org.platkmframework.cplatkm.desktop.core.CPlatkmContentManager;
import org.platkmframework.databasereader.model.Table;
import org.platkmframework.cplatkm.processor.data.GlobalData;
import org.platkmframework.cplatkm.processor.exception.CPlatkmException;
import org.platkmframework.util.JsonException;
import org.platkmframework.util.JsonUtil;
import org.platkmframework.util.Util;

/**
 *   Author:
 *     Eduardo Iglesias
 *   Contributors:
 *   	Eduardo Iglesias - initial API and implementation
 */
public final class GlobalDataPanel extends JPanel {

    private JTable table;
    private final DefaultTableModel tableModel; 
    GlobalDataEditorPanel globalDataEditorPanel = null;
    private JScrollPane scrollPaneMain;
    private GlobalDataAddDataBaseObjectDialog  globalDataAddDataBaseObjectDialog;

    public GlobalDataPanel(GlobalDataEditorPanel globalDataEditorPanel, JScrollPane scrollPaneMain) {
        setLayout(new BorderLayout());
        this.globalDataEditorPanel = globalDataEditorPanel;
        this.scrollPaneMain = scrollPaneMain;
        this.globalDataAddDataBaseObjectDialog = new GlobalDataAddDataBaseObjectDialog(CPlatkmContentManager.getInstance().getMainFrame(), true);
    
        // Datos iniciales simulados 

        // Crear el modelo de la tabla
        String[] columnNames = { "Name" , "Code"  };
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
                        editGlobaData();
                    }
                }
            }
        });
        refreshTable();

        // Panel de botones
        JPanel buttonPanel = new JPanel();
        JButton btnAddDbObject = new JButton("Add from Database");
        JButton btnCreate = new JButton("Create");
        JButton btnEdit = new JButton("Edit");
        JButton btnDelete = new JButton("Delete");

        btnAddDbObject.addActionListener(e -> addDataBaseObjects());
        btnCreate.addActionListener(e -> createGlobalData());
        btnEdit.addActionListener(e -> editGlobaData());
        btnDelete.addActionListener(e -> deleteDataType());

        buttonPanel.add(btnAddDbObject);
        buttonPanel.add(btnCreate);
        buttonPanel.add(btnEdit);
        buttonPanel.add(btnDelete);

        // Agregar la tabla y el panel de botones
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    public void refreshTable() {
        tableModel.setRowCount(0);
        for (GlobalData gd : CPlatkmContentManager.getInstance().getCgenetatorConfig().getGlobalDatas()) {
            Object[] rowData = {
                gd.getName(),
                gd.getCode()
            };
            tableModel.addRow(rowData);
        }
    }

    private void createGlobalData() {
        
        globalDataEditorPanel.setData(null);
        this.scrollPaneMain.setViewportView(this.globalDataEditorPanel);
 
    }

    private void editGlobaData() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            GlobalData current = CPlatkmContentManager.getInstance().getCgenetatorConfig().getGlobalDatas().get(selectedRow);
            
            globalDataEditorPanel.setData(current);
            this.scrollPaneMain.setViewportView(this.globalDataEditorPanel);
 
        } else {
            JOptionPane.showMessageDialog(this,
                "You should select a record to update.",
                "Edit", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void deleteDataType() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            
            int response = JOptionPane.showConfirmDialog(null, 
                "Are you sure you want to delete this record?", "Delete Confirmation", 
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
             
            if (response == JOptionPane.YES_OPTION) { 
                try {
                    //GlobalData  current = CPlatkmContentManager.getInstance().getCgenetatorConfig().getGlobalDatas().get(selectedRow);
                    CPlatkmContentManager.getInstance().getCgenetatorConfig().getGlobalDatas().remove(selectedRow);
                    refreshTable();
                    CPlatkmContentManager.getInstance().refreshGlobalDataSeparator();

                    CPlatkmContentManager.getInstance().updateConfigFile();
                } catch (CPlatkmException ex) {
                    Logger.getLogger(GlobalDataPanel.class.getName()).log(Level.SEVERE, null, ex);
                    JOptionPane.showMessageDialog(this,
                    ex.getMessage(),
                    "Delete Conformation", JOptionPane.WARNING_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this,
                "You should select a record to  delete.",
                "Delete", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    private void addDataBaseObjects(){
        
        globalDataAddDataBaseObjectDialog.setLocationRelativeTo(CPlatkmContentManager.getInstance().getMainFrame());
        globalDataAddDataBaseObjectDialog.setData();
        globalDataAddDataBaseObjectDialog.setVisible(true);
        if(globalDataAddDataBaseObjectDialog.isUpdate() && !globalDataAddDataBaseObjectDialog.getSelectedTables().isEmpty()){
        
            try {
                
                ObjectMapper objectMapper = new ObjectMapper();
                GlobalData globalData;
                for (Table table1 : globalDataAddDataBaseObjectDialog.getSelectedTables()) {
                    
                    globalData = new GlobalData();
                    globalData.setId(Util.randomAlfaNumericString(255)); 
                    globalData.setName(table1.getName());
                    String json = JsonUtil.objectToJson(table1);
                    globalData.setData(objectMapper.readValue(json, Map.class));
            
                    CPlatkmContentManager.getInstance().getCgenetatorConfig().getGlobalDatas().add(globalData);
                }
                
                CPlatkmContentManager.getInstance().refreshGlobalDataSeparator();  
                CPlatkmContentManager.getInstance().updateConfigFile();
                
                refreshTable();

            } catch (JsonProcessingException | JsonException | CPlatkmException ex) {
                Logger.getLogger(GlobalDataPanel.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(this,
                ex.getMessage(),
                "Create", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

 
   
}
