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
package org.platkmframework.cplatkm;

import org.platkmframework.cplatkm.desktop.MainJFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Eduardo Iglesias
 */
public class Application {
    
    private static final Logger logger = LoggerFactory.getLogger(MainJFrame.class);

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) { 
       if(args != null && args.length >0 && ("-v".equals(args[0]) || "-version".equals(args[0]))){
           logger.info("version: " + Application.class.getPackage().getImplementationVersion());
       }else{
           MainJFrame.main(args);
       }
    }
    
}
