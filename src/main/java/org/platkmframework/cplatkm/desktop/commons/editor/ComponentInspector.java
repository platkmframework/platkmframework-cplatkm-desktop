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
 *  	Eduardo Iglesias - initial API and implementation
 * *****************************************************************************
 */
package org.platkmframework.cplatkm.desktop.commons.editor;

import javax.swing.JComponent;

/**
 *
 * @author Eduardo Iglesias
 * @param <E> E
 */
public abstract class ComponentInspector<E extends JComponent> {

    protected E jComponent;
    protected Object lastComponentValue;
    protected boolean valueUpdated;
    protected Runnable runnable;

    public ComponentInspector(E jComponent, Object lastComponentValue) {
        this.jComponent = jComponent; 
        this.lastComponentValue = lastComponentValue;
    }

    public abstract Object getComponentValue();

    public boolean isValueUpdated() {
        return valueUpdated;
    }

    protected void setValueUpdated(boolean valueUpdated) {
        this.valueUpdated = valueUpdated;
    }

    public void setRunnable(Runnable runnable) {
        this.runnable = runnable;
    }

    public void resetLastValue() {
        this.lastComponentValue = getComponentValue();
        this.valueUpdated = false;
    }
   
}
