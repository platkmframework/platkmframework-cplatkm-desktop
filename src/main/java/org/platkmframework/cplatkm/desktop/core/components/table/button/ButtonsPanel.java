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
package org.platkmframework.cplatkm.desktop.core.components.table.button;

import java.awt.Component;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JPanel; 

/**
 *
 * @author eigta
 */
public class ButtonsPanel extends JPanel {
  private final List<JButton> buttons;

  public ButtonsPanel(List<JButton> buttons) {
    super();
    this.buttons = buttons;
    for (JButton b : this.buttons) {
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
