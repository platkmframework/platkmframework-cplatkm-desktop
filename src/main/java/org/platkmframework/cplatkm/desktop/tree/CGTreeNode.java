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

import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author Eduardo Iglesias
 */
public final class CGTreeNode extends CGTreeNodeBase{

    private String id;
    private String type;
    private String name;
    
    public CGTreeNode() {
        super();
    }

    public CGTreeNode(Object userObject, boolean allowsChildren) {
        super(userObject, allowsChildren);
    }
    
    public CGTreeNode(Object userObject ) {
        super(userObject);
    }
    
     public CGTreeNode(Object userObject, String type ) {
        super(userObject);
         setType(type);
    }
     
    public CGTreeNode(Object userObject, String type, String name, String id  ) {
        super(userObject);
        setType(type);
        setName(name);
        setId(id);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    
    @Override
    public String toString() {
        if(userObject != null && (userObject instanceof String) ) return getUserObject().toString();
        if(StringUtils.isBlank(name)) return getType();
        return getName();
    }   
}
