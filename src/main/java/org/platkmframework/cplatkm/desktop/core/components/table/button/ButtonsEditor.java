/**
 * ****************************************************************************
 *  Copyright(c) 2025 the original author Eduardo Iglesias.
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
package org.platkmframework.cplatkm.desktop.core.components.table.button;

import java.awt.Component;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.AbstractCellEditor;
import javax.swing.ButtonModel;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor; 

/**
 *
 * @author eigta
 */
public class ButtonsEditor extends AbstractCellEditor implements TableCellEditor {
  private ButtonsPanel panel;
  private final JTable table;
  
  private final class EditingStopHandler extends MouseAdapter implements ActionListener {
    @Override public void mousePressed(MouseEvent e) {
      Object o = e.getSource();
      if (o instanceof TableCellEditor) {
        actionPerformed(new ActionEvent(o, ActionEvent.ACTION_PERFORMED, ""));
      } else if (o instanceof JButton) {
        // DEBUG:
        // view button click ->
        // control key down + edit button(same cell) press ->
        // remain selection color
        ButtonModel m = ((JButton) e.getComponent()).getModel();
        if (m.isPressed() && table.isRowSelected(table.getEditingRow()) && e.isControlDown()) {
          panel.setBackground(table.getBackground());
        }
      }
    }

    @SuppressWarnings("PMD.LambdaCanBeMethodReference")
    @Override public void actionPerformed(ActionEvent e) {
      EventQueue.invokeLater(() -> fireEditingStopped());
      // https://bugs.openjdk.org/browse/JDK-8138667
      // java.lang.IllegalAccessError: tried to access method (for a protected method)
      // Fix Version/s: 9
      // EventQueue.invokeLater(ButtonsEditor.this::fireEditingStopped);
    }
  }

   public ButtonsEditor(JTable table, ButtonsPanel panel) {
    super();
    this.table = table;
    this.panel = panel;
    List<JButton> list = panel.getButtons();
//    list.get(0).setAction(new ViewAction(table));
//    list.get(1).setAction(new ViewAction(table));

    EditingStopHandler handler = new EditingStopHandler();
    for (JButton b : list) {
      b.addMouseListener(handler);
      b.addActionListener(handler);
    }
    panel.addMouseListener(handler);
  }

  @Override public Component getTableCellEditorComponent(JTable tbl, Object value, boolean isSelected, int row, int column) {
    panel.setBackground(tbl.getSelectionBackground());
    return panel;
  }

  @Override public Object getCellEditorValue() {
    return "";
  }
  


}
