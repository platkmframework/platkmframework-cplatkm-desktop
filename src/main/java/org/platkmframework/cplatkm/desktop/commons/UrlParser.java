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
package org.platkmframework.cplatkm.desktop.commons;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.platkmframework.cplatkm.processor.data.openapi.CGParameters;

/**
 *   Author:
 *     Eduardo Iglesias
 *   Contributors:
 *   	Eduardo Iglesias - initial API and implementation
 */
public class UrlParser {
    
    public static List<CGParameters> extractPathVariables(String urlBase) {
        List<CGParameters> pathVariables = new ArrayList<>();

        // Extraer los nombres de las variables de la URL base
        Matcher nameMatcher = Pattern.compile("\\{(\\w+)}").matcher(urlBase);
        while (nameMatcher.find()) {
            pathVariables.add(new CGParameters(nameMatcher.group(1), ParamTypes.path.name()));
        }

        return pathVariables;
    }

    // Método para extraer Query Parameters de una URL
    public static List<CGParameters> extractQueryParameters(String url) {
        List<CGParameters> queryParams = new ArrayList<>();
        try {
            //URI uri = new URI(url);
            //String query = uri.getQuery(); // Obtiene la parte después de "?"
            String[] urlSplit = url.split("\\?");
            if (urlSplit != null && urlSplit.length > 1) {
                 String query = urlSplit[1];
                String[] pairs = query.split("&");
                for (String pair : pairs) {
                    String[] keyValue = pair.split("=");
                    String key = URLDecoder.decode(keyValue[0], StandardCharsets.UTF_8);
                    String value = keyValue.length > 1 ? URLDecoder.decode(keyValue[1], StandardCharsets.UTF_8) : "";
                    queryParams.add(new CGParameters(key, value, ParamTypes.query.name()));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return queryParams;
    }    

    public static List<CGParameters>  extract(String path) {
        List<CGParameters> queryParams = extractQueryParameters(path);
        queryParams.addAll(extractPathVariables(path));
        return queryParams;
    }
}
