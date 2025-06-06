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
package org.platkmframework.cplatkm.desktop.test;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

public final class MainPanel extends JPanel {
  private MainPanel() {
    super(new BorderLayout());
    JTable table = new JTable(makeModel()) {
      @Override public void updateUI() {
        super.updateUI();
        setRowHeight(36);
        setAutoCreateRowSorter(true);
        TableColumn column = getColumnModel().getColumn(1);
        column.setCellRenderer(new ButtonsRenderer());
        column.setCellEditor(new ButtonsEditor(this));
      }
    };
    add(new JScrollPane(table));
    setBorder(BorderFactory.createTitledBorder("Multiple Buttons in a Table Cell"));
    setPreferredSize(new Dimension(320, 240));
  }

  private static TableModel makeModel() {
    String empty = "";
    String[] columnNames = {"String", "Button"};
    Object[][] data = {
        {"AAA", empty}, {"CCC", empty}, {"BBB", empty}, {"ZZZ", empty}
    };
    return new DefaultTableModel(data, columnNames) ;
  }

  public static void main(String[] args) {
    EventQueue.invokeLater(MainPanel::createAndShowGui);
  }

  private static void createAndShowGui() {
    try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    } catch (UnsupportedLookAndFeelException ignored) {
      Toolkit.getDefaultToolkit().beep();
    } catch (ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
      ex.printStackTrace();
      return;
    }
    JFrame frame = new JFrame("@title@");
    frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    frame.getContentPane().add(new MainPanel());
    frame.pack();
    frame.setLocationRelativeTo(null);
    frame.setVisible(true);
  }
}

 

class ButtonsPanel extends JPanel {
  private final List<JButton> buttons = Arrays.asList(new JButton("view"), new JButton("edit"));

  protected ButtonsPanel() {
    super();
    for (JButton b : buttons) {
      b.setFocusable(false);
      b.setRolloverEnabled(false);
      add(b);
    }
  }

  @Override public final Component add(Component comp) {
    return super.add(comp);
  }

  @Override public void updateUI() {
    super.updateUI();
    setOpaque(true);
  }

  protected List<JButton> getButtons() {
    return buttons;
  }
}

class ButtonsRenderer implements TableCellRenderer {
  private final ButtonsPanel panel = new ButtonsPanel() {
    @Override public void updateUI() {
      super.updateUI();
      setName("Table.cellRenderer");
    }
  };

  @Override public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
    panel.setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
    return panel;
  }
}

class ViewAction extends AbstractAction {
  private final JTable table;

  protected ViewAction(JTable table) {
    super("view");
    this.table = table;
  }

  @Override public void actionPerformed(ActionEvent e) {
    JOptionPane.showMessageDialog(table, "Viewing");
  }
}

class EditAction extends AbstractAction {
  private final JTable table;

  protected EditAction(JTable table) {
    super("edit");
    this.table = table;
  }

  @Override public void actionPerformed(ActionEvent e) {
    // Object o = table.getModel().getValueAt(table.getSelectedRow(), 0);
    int row = table.convertRowIndexToModel(table.getEditingRow());
    Object o = table.getModel().getValueAt(row, 0);
    JOptionPane.showMessageDialog(table, "Editing: " + o);
  }
}

// delegation pattern
class ButtonsEditor extends AbstractCellEditor implements TableCellEditor {
  private final ButtonsPanel panel = new ButtonsPanel();
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

  protected ButtonsEditor(JTable table) {
    super();
    this.table = table;
    List<JButton> list = panel.getButtons();
    list.get(0).setAction(new ViewAction(table));
    list.get(1).setAction(new EditAction(table));

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
 