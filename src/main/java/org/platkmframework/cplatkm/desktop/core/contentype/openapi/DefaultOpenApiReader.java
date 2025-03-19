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
package org.platkmframework.cplatkm.desktop.core.contentype.openapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;

import java.io.InputStream;
import java.util.Map;

/**
 *   Author:
 *     Eduardo Iglesias
 *   Contributors:
 *   	Eduardo Iglesias - initial API and implementation
 */
public class DefaultOpenApiReader {

    public static Map<?,?> read() {
        try {
 
            InputStream inputStream = DefaultOpenApiReader.class.getResourceAsStream("/openapi/openapi_default.json");
            if (inputStream == null) throw new RuntimeException("Archivo openapi_default.json no encontrado en resources");
             
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(inputStream,  Map.class);
        } catch (IOException | RuntimeException e) {
            throw new RuntimeException("Error reading openapi_default.json", e);
        }
    }
}

