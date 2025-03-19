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
package org.platkmframework.cplatkm.desktop.core;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Eduardo Iglesias
 */
public class CGeneratorSettings {
    
    private List<String> recents;
    private String lastExportPath;
    private String lastImportPath;
    

    public List<String> getRecents() {
        if(recents == null) recents = new ArrayList<>();
        return recents;
    }

    public void setRecents(List<String> recents) {
        this.recents = recents;
    }

    public String getLastExportPath() {
        return lastExportPath;
    }

    public void setLastExportPath(String lastExportPath) {
        this.lastExportPath = lastExportPath;
    }

    public String getLastImportPath() {
        return lastImportPath;
    }

    public void setLastImportPath(String lastImportPath) {
        this.lastImportPath = lastImportPath;
    }
    
    
}
