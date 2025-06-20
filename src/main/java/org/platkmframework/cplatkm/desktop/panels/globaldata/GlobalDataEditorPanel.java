/*
 * Copyright 2025 Eduardo Iglesias.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.platkmframework.cplatkm.desktop.panels.globaldata;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import java.awt.Color;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JColorChooser;
import javax.swing.JOptionPane;
import org.apache.commons.lang3.StringUtils;
import org.platkmframework.cplatkm.desktop.commons.editor.ComponentInspector;
import org.platkmframework.cplatkm.desktop.commons.editor.EditComponentsInspector;
import org.platkmframework.cplatkm.desktop.commons.editor.JTextEditorComponentInspector;
import org.platkmframework.cplatkm.desktop.commons.editor.JTextPaneComponentInspector;
import org.platkmframework.cplatkm.desktop.core.CPlatkmContentManager;
import org.platkmframework.cplatkm.processor.data.GlobalData;
import org.platkmframework.cplatkm.processor.exception.CPlatkmException;
import org.platkmframework.util.Util;

/**
 *
 * @author Eduardo Iglesias
 */
public class GlobalDataEditorPanel extends javax.swing.JPanel {

    public  GlobalData  globalData;
    private EditComponentsInspector editComponentsInspector;
      
    /**
     * Creates new form GlobalDataEditorPanel
     */
    public GlobalDataEditorPanel() {
        initComponents();
        initEditorComponetInspector();
        
        increaseFontSizeButton.addActionListener(e -> {
            Font currentFont = textPane.getFont();
            textPane.setFont(new Font(currentFont.getName(), currentFont.getStyle(), currentFont.getSize() + 2));
        });

        decreaseFontSizeButton.addActionListener(e -> {
            Font currentFont = textPane.getFont();
            textPane.setFont(new Font(currentFont.getName(), currentFont.getStyle(), Math.max(currentFont.getSize() - 2, 8)));
        });
        
        colorButton.addActionListener(e -> {
            Color selectedColor = JColorChooser.showDialog(CPlatkmContentManager.getInstance().getMainFrame(), "Elige un color", Color.BLACK);
            if (selectedColor != null) {
                textPane.setForeground(selectedColor);
            }
        });
        
        String[] fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        fontComboBox.removeAllItems();
        if(fonts != null && fonts.length>0)
            for (String font : fonts) {
                fontComboBox.addItem(font);
            }
            
        fontComboBox.addActionListener(e -> {
            String selectedFont = (String) fontComboBox.getSelectedItem();
            Font currentFont = textPane.getFont();
            textPane.setFont(new Font(selectedFont, currentFont.getStyle(), currentFont.getSize()));
        });
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        txtName = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtCode = new javax.swing.JTextField();
        btnCheckBox = new javax.swing.JCheckBox();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        textPane = new javax.swing.JTextPane();
        jToolBar1 = new javax.swing.JToolBar();
        decreaseFontSizeButton = new javax.swing.JButton();
        increaseFontSizeButton = new javax.swing.JButton();
        colorButton = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        fontComboBox = new javax.swing.JComboBox<>();
        btnApply = new javax.swing.JButton();

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel2.setText("Name");

        jLabel3.setText("Code");

        btnCheckBox.setText("Test");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(txtCode)
                            .addComponent(txtName))
                        .addGap(15, 15, 15))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(btnCheckBox)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(9, 9, 9)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtCode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnCheckBox)
                .addContainerGap(7, Short.MAX_VALUE))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jScrollPane1.setViewportView(textPane);

        jToolBar1.setRollover(true);

        decreaseFontSizeButton.setText("A-");
        decreaseFontSizeButton.setFocusable(false);
        decreaseFontSizeButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        decreaseFontSizeButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(decreaseFontSizeButton);

        increaseFontSizeButton.setText("A+");
        increaseFontSizeButton.setFocusable(false);
        increaseFontSizeButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        increaseFontSizeButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(increaseFontSizeButton);

        colorButton.setText("Color");
        colorButton.setFocusable(false);
        colorButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        colorButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(colorButton);
        jToolBar1.add(jSeparator1);

        jToolBar1.add(fontComboBox);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, 633, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 388, Short.MAX_VALUE)
                .addContainerGap())
        );

        btnApply.setText("Apply");
        btnApply.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnApplyActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnApply, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnApply)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnApplyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnApplyActionPerformed
        try {

            if(StringUtils.isBlank(txtName.getText())){
                JOptionPane.showMessageDialog(this,
                    "Name should not be empty",
                    "Edit", JOptionPane.WARNING_MESSAGE);

                return;
            }
            ObjectMapper objectMapper = new ObjectMapper();
            this.globalData.setCode(txtCode.getText());
            this.globalData.setName(txtName.getText());
            this.globalData.setTest(btnCheckBox.isSelected());
            this.globalData.setData(objectMapper.readValue(textPane.getText(), Map.class));
            
            if(StringUtils.isBlank(this.globalData.getId())){
                
                try {
                    this.globalData.setId(Util.randomAlfaNumericString(255)); 
                    CPlatkmContentManager.getInstance().getCgenetatorConfig().getGlobalDatas().add(this.globalData);
                    CPlatkmContentManager.getInstance().refreshGlobalDataSeparator();  
                    CPlatkmContentManager.getInstance().updateConfigFile();
                    editComponentsInspector.reset(); 

                } catch (CPlatkmException ex) {
                    Logger.getLogger(GlobalDataPanel.class.getName()).log(Level.SEVERE, null, ex);
                    JOptionPane.showMessageDialog(this,
                    ex.getMessage(),
                    "Create", JOptionPane.WARNING_MESSAGE);
                }
            
            }else{
                try {
                        for (int i = 0; i < CPlatkmContentManager.getInstance().getCgenetatorConfig().getGlobalDatas().size(); i++) {
                            if (CPlatkmContentManager.getInstance().getCgenetatorConfig().getGlobalDatas().get(i).getId().equals(this.globalData.getId())) {
                                CPlatkmContentManager.getInstance().getCgenetatorConfig().getGlobalDatas().set(i, this.globalData);
                                break;
                            }
                        }
                        CPlatkmContentManager.getInstance().refreshGlobalDataSeparator();
                        CPlatkmContentManager.getInstance().updateConfigFile();
                        
                        editComponentsInspector.reset(); 
                        
                } catch (CPlatkmException ex) {
                    Logger.getLogger(GlobalDataPanel.class.getName()).log(Level.SEVERE, null, ex);
                    JOptionPane.showMessageDialog(this,
                    ex.getMessage(),
                    "Edit", JOptionPane.WARNING_MESSAGE);
                }            
            }

        } catch (JsonProcessingException ex) {
            Logger.getLogger(GlobalDataEditorPanel.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this,
                "Json error",
                "Edit", JOptionPane.ERROR_MESSAGE);
        } 
        
        
    }//GEN-LAST:event_btnApplyActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnApply;
    private javax.swing.JCheckBox btnCheckBox;
    private javax.swing.JButton colorButton;
    private javax.swing.JButton decreaseFontSizeButton;
    private javax.swing.JComboBox<String> fontComboBox;
    private javax.swing.JButton increaseFontSizeButton;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JTextPane textPane;
    private javax.swing.JTextField txtCode;
    private javax.swing.JTextField txtName;
    // End of variables declaration//GEN-END:variables


    public void setData(GlobalData pglobalData) {
        
        this.globalData = pglobalData;
        if(this.globalData == null) this.globalData = new GlobalData();
        txtCode.setText(this.globalData.getCode());
        txtName.setText(this.globalData.getName());
        btnCheckBox.setSelected(this.globalData.isTest());
        
        try {
            ObjectWriter writer = new ObjectMapper().writerWithDefaultPrettyPrinter();
            textPane.setText(writer.writeValueAsString(this.globalData.getData()));
        } catch (JsonProcessingException ex) {
            Logger.getLogger(GlobalDataEditorPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        editComponentsInspector.reset(); 

    }

    public GlobalData getGlobalData() {
        return globalData;
    }
    
     private void initEditorComponetInspector() {
        List<ComponentInspector> components = new ArrayList<>();
        
        components.add(new JTextEditorComponentInspector(txtName, null)); 
        components.add(new JTextEditorComponentInspector(txtCode, null));  
        components.add(new JTextPaneComponentInspector(textPane, null));  
        
        editComponentsInspector = new EditComponentsInspector(components, btnApply);
    }  
}
