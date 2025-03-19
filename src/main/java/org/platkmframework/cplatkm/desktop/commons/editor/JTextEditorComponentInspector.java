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
package org.platkmframework.cplatkm.desktop.commons.editor;

import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 *
 * @author Eduardo Iglesias
 */
public class JTextEditorComponentInspector extends ComponentInspector<JTextField> {

    public JTextEditorComponentInspector(JTextField jComponent, Object lastComponentValue) {
        super(jComponent, lastComponentValue);
        
        jComponent.getDocument().addDocumentListener(new DocumentListener() {
             @Override
             public void insertUpdate(DocumentEvent e) {isValueChanged();}
             @Override
             public void removeUpdate(DocumentEvent e) {isValueChanged();}
             @Override
             public void changedUpdate(DocumentEvent e) {isValueChanged();}
        });  
    }

    @Override
    public Object getComponentValue(){
        return jComponent.getText();
    }
    
    public void isValueChanged(){
        this.valueUpdated = !jComponent.getText().equals(lastComponentValue);
        runnable.run();
    }
    
}
