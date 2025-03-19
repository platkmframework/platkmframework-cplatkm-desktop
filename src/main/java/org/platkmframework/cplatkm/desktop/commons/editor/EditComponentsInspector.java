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
package org.platkmframework.cplatkm.desktop.commons.editor;

import java.util.List;
import javax.swing.JButton;

/**
 *
 * @author Eduardo Iglesias 
 */
public class EditComponentsInspector {
    
    private final List<ComponentInspector> components;
    private final JButton appyJButton;

    public EditComponentsInspector(List<ComponentInspector> components, JButton appyJButton) {
        this.components = components;
        this.appyJButton = appyJButton;
        initCheckerListener();
    }

    private void initCheckerListener() {
        Runnable editStatus = () -> appyJButton.setEnabled(this.components.stream().anyMatch(c->c.isValueUpdated()));
        for (int i = 0; i < components.size(); i++) {
            ComponentInspector componentChecker = components.get(i);
            componentChecker.setRunnable(editStatus);
        }
    }
    
    public void reset(){
        for (int i = 0; i < components.size(); i++) {
            components.get(i).resetLastValue();
        }
        appyJButton.setEnabled(false);
    }

}
