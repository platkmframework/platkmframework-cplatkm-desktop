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
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeCellRenderer;
import org.platkmframework.cplatkm.desktop.tree.CGTreeNodeBase;

/**
 *
 * @author eigta
 */
    public class CheckBoxNodeRenderer extends JPanel implements TreeCellRenderer {
        private JCheckBox checkBox;
        private JLabel label;
        
        public CheckBoxNodeRenderer() {
            super(new FlowLayout(FlowLayout.LEFT, 0, 0));
            checkBox = new JCheckBox();
            label = new JLabel();
            add(checkBox);
            add(label);
            setOpaque(false);
        }
        
        @Override
        public Component getTreeCellRendererComponent(JTree tree, Object value,
                                                      boolean selected, boolean expanded,
                                                      boolean leaf, int row, boolean hasFocus) {
            if (value instanceof DefaultMutableTreeNode) {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
                if (node instanceof CGTreeNodeBase) {
                    CGTreeNodeBase cbNode = (CGTreeNodeBase) node;
                    checkBox.setSelected(cbNode.isSelected());
                } 
                label.setText(node.toString());
            }
            
            // Opcional: Cambiar colores en función de la selección
            if (selected) {
                setBackground(UIManager.getColor("Tree.selectionBackground"));
                setForeground(UIManager.getColor("Tree.selectionForeground"));
            } else {
                setBackground(UIManager.getColor("Tree.textBackground"));
                setForeground(UIManager.getColor("Tree.textForeground"));
            }
            
            return this;
        }
    }
