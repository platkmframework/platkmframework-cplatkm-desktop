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
package org.platkmframework.cplatkm.desktop.tree;

import javax.swing.tree.DefaultMutableTreeNode;

/**
 *
 * @author Eduardo Iglesias
 */
public abstract class CGTreeNodeBase extends DefaultMutableTreeNode{
    
    private String parentId;
    private boolean selected = false;

    public CGTreeNodeBase() {
    }
    
    public CGTreeNodeBase(Object userObject) {
        super(userObject);
    }

    public CGTreeNodeBase(Object userObject, boolean allowsChildren) {
        super(userObject, allowsChildren);
    }    
    
    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    
}
