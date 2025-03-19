/*
 * Copyright 2025 eigta.
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
package org.platkmframework.cplatkm.desktop.importexport;

import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.AbstractCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeCellEditor;
import org.platkmframework.cplatkm.desktop.tree.CGTreeNodeBase;

/**
 *
 * @author eigta
 */
public   class CheckBoxNodeEditor extends AbstractCellEditor implements TreeCellEditor {
        private JPanel panel;
        private JCheckBox checkBox;
        private JLabel label;
        private CGTreeNodeBase currentNode;
    
        public CheckBoxNodeEditor() {
            panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
            checkBox = new JCheckBox();
            label = new JLabel();
            panel.add(checkBox);
            panel.add(label);
            
            // Al hacer clic en el checkbox se actualiza el modelo
            checkBox.addActionListener(new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (currentNode != null) {
                        currentNode.setSelected(checkBox.isSelected());
                        System.out.println(checkBox.isSelected());
                        
                        //@TODO 
                        //ver si tiene el objecto y de que tipo, para eliminar o adicioar a la nueva configuracion
                    }
                    // Finaliza la edición
                    fireEditingStopped();
                }
            });
        }
        
        @Override
        public Component getTreeCellEditorComponent(JTree tree, Object value,
                                                    boolean selected, boolean expanded,
                                                    boolean leaf, int row) {
            if (value instanceof DefaultMutableTreeNode) {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
                if (node instanceof CGTreeNodeBase) {
                    currentNode = (CGTreeNodeBase) node;
                    checkBox.setSelected(currentNode.isSelected());
                }
                label.setText(currentNode.toString());
            }
            return panel;
        }
        
        @Override
        public Object getCellEditorValue() {
            return currentNode.getUserObject();
        }
    }