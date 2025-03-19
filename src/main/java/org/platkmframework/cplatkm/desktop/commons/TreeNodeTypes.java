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
package org.platkmframework.cplatkm.desktop.commons;

/**
 *   Author:
 *     Eduardo Iglesias
 *   Contributors:
 *   	Eduardo Iglesias - initial API and implementation
 */
public enum TreeNodeTypes {
    
    ROOT_TYPE, 
    DATABASE_TYPE, 
    DATABASE_SEPARATOR_TYPE, 
    DATATYPE, 
    DATATYPE_SEPARATOR_TYPE, 
    ARTIFACT_TYPE,
    ARTIFACT_SEPARATOR_TYPE, 
    TEMPLATE_SEPARATOR_TYPE, 
    TEMPLATE_TYPE, 
    RUN_CONFIGURATION_SEPARATOR_TYPE,
    RUN_CONFIGURATION_TYPE,
    GLOBAL_DATA_SEPARATOR_TYPE,
    GLOBAL_DATA_TYPE,
    OPEN_API_SEPARATOR_TYPE,
    OPEN_API_TYPE,
    OPEN_API_TAG_SEPARATOR_TYPE,
    OPEN_API_TAG_TYPE,
    OPEN_API_PATHS_TYPE,
    OPEN_API_PATHS_SEPARATOR_TYPE,
    OPEN_API_COMPONENT_SEPARATOR_TYPE,
    OPEN_API_COMPONENT_TYPE;

}
