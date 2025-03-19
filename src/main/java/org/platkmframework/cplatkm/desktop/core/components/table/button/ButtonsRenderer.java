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
 *  	Eduardo Iglesias - initial API and implementation
 * *****************************************************************************
 */
package org.platkmframework.cplatkm.desktop.core.components.table.button;

import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer; 

/**
 *
 * @author Eduardo Iglesias
 */
public class ButtonsRenderer implements TableCellRenderer {

    private final ButtonsPanel panel;
    
    public ButtonsRenderer(ButtonsPanel panel) {
        this.panel = panel;
    }

  @Override public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
    panel.setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
    return panel;
  }
}