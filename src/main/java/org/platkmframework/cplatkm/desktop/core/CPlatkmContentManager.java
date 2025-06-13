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
 *  	Eduardo Iglesias Taylor - initial API and implementation
 * *****************************************************************************
 */
package org.platkmframework.cplatkm.desktop.core;

import org.platkmframework.cplatkm.desktop.commons.TreeNodeTypes;
import com.google.gson.reflect.TypeToken;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException; 
import java.sql.Connection; 
import java.sql.DriverManager;
import java.sql.SQLException; 
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import javax.swing.JFrame;
import javax.swing.tree.DefaultTreeModel;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils; 
import org.platkmframework.cplatkm.desktop.core.contentype.ContentType;
import org.platkmframework.cplatkm.desktop.tree.CGTreeNode;
import org.platkmframework.databasereader.core.DatabaseReader;
import org.platkmframework.databasereader.model.Table;
import org.platkmframework.cplatkm.processor.data.Artifact;
import org.platkmframework.cplatkm.processor.data.CGenetatorConfig;
import org.platkmframework.cplatkm.processor.data.DataTypeMapping;
import org.platkmframework.cplatkm.processor.data.DatabaseData; 
import org.platkmframework.cplatkm.processor.data.GlobalData;
import org.platkmframework.cplatkm.processor.data.OpenApiImported;
import org.platkmframework.cplatkm.processor.data.RunConfiguration;
import org.platkmframework.cplatkm.processor.data.Template;
import org.platkmframework.cplatkm.processor.data.openapi.CGOpenAPI;
import org.platkmframework.cplatkm.processor.data.openapi.CGPaths;
import org.platkmframework.cplatkm.processor.data.openapi.CGTags;
import org.platkmframework.cplatkm.processor.exception.CPlatkmException;
import org.platkmframework.util.JsonException;
import org.platkmframework.util.JsonUtil;
import org.platkmframework.util.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Eduardo Iglesias
 */
public class CPlatkmContentManager {
    
    private static final String C_PLATKMFRAMEWORK_FOLDER = ".platkmframework";
    private static final String C_CGENERATOR = "cgenerator";
    private static final String C_REPOSITORY = "repository";
    
    private static final Logger logger = LoggerFactory.getLogger(CPlatkmContentManager.class);
    
    boolean active = false;
    CGenetatorConfig cgenetatorConfig;
    File cliConfigurationFile;
    private CGeneratorSettings cGeneratorSettings;
    
    javax.swing.JFrame mainFrame;
    DefaultTreeModel defaultTreeModel;
    private File currentWorkSpace = null;
    
    private List<ContentType> contentTypes;
    
    private Map<?,?> defaultOpenApi;
    
    
    private CPlatkmContentManager() {
        this.cgenetatorConfig = new CGenetatorConfig();
    }
    
    public static CPlatkmContentManager getInstance() {
        return CGeneratorConfigManagerHolder.INSTANCE;
    }


 
    private static class CGeneratorConfigManagerHolder {
        private static final CPlatkmContentManager INSTANCE = new CPlatkmContentManager();
    }


    public void addGlobalData(String id, String code, String name, Map<String, Object> data) {
        cgenetatorConfig.getGlobalDatas().add(new GlobalData(id, code, name, new HashMap<>(data)));
    }

    public void updateGlobalData(String globalDataRefId, String code, String name, Map<String, Object> data) {
        for (int i = 0; i < cgenetatorConfig.getGlobalDatas().size(); i++) {
            if(cgenetatorConfig.getGlobalDatas().get(i).getId().equals(globalDataRefId)){
                cgenetatorConfig.getGlobalDatas().get(i).setCode(code);
                cgenetatorConfig.getGlobalDatas().get(i).setName(name);
                cgenetatorConfig.getGlobalDatas().get(i).setData(data);
                break;
            }
        }
    }

    public void refreshGlobalDataSeparator() {
        CGTreeNode node = searchNodeByType((CGTreeNode)defaultTreeModel.getRoot(), TreeNodeTypes.GLOBAL_DATA_SEPARATOR_TYPE.name(), null, null);
        node.removeAllChildren();
        for (GlobalData globalData : CPlatkmContentManager.getInstance().getCgenetatorConfig().getGlobalDatas()) {
            node.add(MainTreeCreator.getInstance().createNodeGlobalData(globalData));
        }
        defaultTreeModel.nodeStructureChanged(node);
    }
    
    public void refreshOpenAPISeparator() {
        CGTreeNode node = searchNodeByType((CGTreeNode)defaultTreeModel.getRoot(), TreeNodeTypes.OPEN_API_SEPARATOR_TYPE.name(), null, null);
        node.removeAllChildren();
        for (OpenApiImported openApiImported : CPlatkmContentManager.getInstance().getCgenetatorConfig().getOpenAPIs()) {
            node.add(MainTreeCreator.getInstance().createOpenAPI(openApiImported));
        }
        defaultTreeModel.nodeStructureChanged(node);      
    }
    
    public void refreshOpenAPITagsSeparator(CGOpenAPI cgOpenApiParent) {
        CGTreeNode node = searchNodeByType((CGTreeNode)defaultTreeModel.getRoot(), TreeNodeTypes.OPEN_API_TAG_SEPARATOR_TYPE.name(), null, cgOpenApiParent.getId());
        node.removeAllChildren();
        defaultTreeModel.nodeStructureChanged(node);      
    }
    
    public void refreshDataBaseSeparator() {
        CGTreeNode node = searchNodeByType((CGTreeNode)defaultTreeModel.getRoot(), TreeNodeTypes.DATABASE_SEPARATOR_TYPE.name(), null, null);
        node.removeAllChildren();
        for (DatabaseData databaseData : CPlatkmContentManager.getInstance().getCgenetatorConfig().getDatabases()) {
            node.add(MainTreeCreator.getInstance().createNodeDataBase(databaseData));
        }
        defaultTreeModel.nodeStructureChanged(node);
    } 
    
    public void refreshDataTypeSeparator() {
        CGTreeNode node = searchNodeByType((CGTreeNode)defaultTreeModel.getRoot(), TreeNodeTypes.DATATYPE_SEPARATOR_TYPE.name(), null, null);
        node.removeAllChildren();
        for (DataTypeMapping dataTypeMapping : CPlatkmContentManager.getInstance().getCgenetatorConfig().getDatatypes()) {
            node.add(MainTreeCreator.getInstance().createNodeDataTypeMapping(dataTypeMapping));
        }
        defaultTreeModel.nodeStructureChanged(node);
    } 

    public void artifactAddNewNode(Artifact artifact) {
        CGTreeNode node = searchNodeByType((CGTreeNode)defaultTreeModel.getRoot(), TreeNodeTypes.ARTIFACT_SEPARATOR_TYPE.name(), null, null);
        node.add(MainTreeCreator.getInstance().createNodeArtifact(artifact));
        defaultTreeModel.nodeStructureChanged(node);
    } 

    public void artifactUpdateNewNode(Artifact artifact) {
        CGTreeNode node = searchNodeByType((CGTreeNode)defaultTreeModel.getRoot(), TreeNodeTypes.ARTIFACT_TYPE.name(), artifact.getId(), null);
        node.setUserObject(artifact);
        node.setName(artifact.getLabel());
        defaultTreeModel.nodeStructureChanged(node);
    } 
    
    public void artifactRemoveNewNode(Artifact artifact) {
        treeRemoveNodeFromParentType(TreeNodeTypes.ARTIFACT_SEPARATOR_TYPE, null, artifact.getId());
    }   
    
    public void templateAddNewNode(Template template, Artifact artifact) {
        CGTreeNode node = searchNodeByType((CGTreeNode)defaultTreeModel.getRoot(), TreeNodeTypes.TEMPLATE_SEPARATOR_TYPE.name(), null, artifact.getId());
        node.add(MainTreeCreator.getInstance().createNodeTemplate(template));
        defaultTreeModel.nodeStructureChanged(node);
    } 
    
    public void templateUpdateNewNode(Template template) {
        CGTreeNode node = searchNodeByType((CGTreeNode)defaultTreeModel.getRoot(), TreeNodeTypes.TEMPLATE_TYPE.name(), template.getId(), null);
        node.setUserObject(template);
        node.setName(template.getLabel());
        defaultTreeModel.nodeStructureChanged(node);
    } 
    
    public void templateRemoveNewNode(Template template, Artifact artifact) {
        treeRemoveNodeFromParentType(TreeNodeTypes.TEMPLATE_SEPARATOR_TYPE, artifact.getId(), template.getId());
    }
    
    public void tagsAddNewNode(CGTags cgTags, CGOpenAPI cgOpenAPI) {
        CGTreeNode treeNodeTagsSeparator = searchNodeByType((CGTreeNode)defaultTreeModel.getRoot(), TreeNodeTypes.OPEN_API_TAG_SEPARATOR_TYPE.name(), null, cgOpenAPI.getId());
        MainTreeCreator.getInstance().addNewTags(cgTags, cgOpenAPI, treeNodeTagsSeparator);
        defaultTreeModel.nodeStructureChanged(treeNodeTagsSeparator);
    } 
    
    public void tagsUpdateNewNode(CGTags cgTags) {
        CGTreeNode node = searchNodeByType((CGTreeNode)defaultTreeModel.getRoot(), TreeNodeTypes.OPEN_API_TAG_TYPE.name(), cgTags.getId(), null);
        node.setUserObject(cgTags);
        node.setName(cgTags.getName());
        defaultTreeModel.nodeStructureChanged(node);
    } 
    
    public void tagsRemoveNewNode(CGTags cgTags, CGOpenAPI cgOpenAPI) {
        treeRemoveNodeFromParentType(TreeNodeTypes.OPEN_API_TAG_SEPARATOR_TYPE, cgOpenAPI.getId(), cgTags.getId());
    }
    
    public void pathsRemoveNewNode(CGPaths cgPaths, CGTags cgTagsFound) {
        treeRemoveNodeFromParentType(TreeNodeTypes.OPEN_API_PATHS_SEPARATOR_TYPE, cgTagsFound.getId(), cgPaths.getId());
    }
    
    public void refreshRunConfigurationSeparator() {
        CGTreeNode node = searchNodeByType((CGTreeNode)defaultTreeModel.getRoot(), TreeNodeTypes.RUN_CONFIGURATION_SEPARATOR_TYPE.name(), null, null);
        node.removeAllChildren();
        for (RunConfiguration runConfiguration : CPlatkmContentManager.getInstance().getCgenetatorConfig().getRunConfigurations()) {
            node.add(MainTreeCreator.getInstance().createNodeRunConfiguration(runConfiguration));
        }
        defaultTreeModel.nodeStructureChanged(node);
    } 
     
    public void createArtifactFolder(String newFoldername) throws CPlatkmException {

        try {
            
            File cgeneratorFolder = new File(currentWorkSpace.getAbsolutePath() + File.separator + newFoldername);
            if(!cgeneratorFolder.exists()){
                FileUtils.forceMkdir(cgeneratorFolder);
            }
            
        } catch (IOException ex) {
            logger.error(ex.getMessage());
            throw new CPlatkmException(ex.getMessage()); 
        }
    }

    public void createTemplateFile(String artifactFolder, String templatename) throws CPlatkmException{
 
         try {
            File fileTemplate = new File(currentWorkSpace.getAbsolutePath() + File.separator +
                artifactFolder + File.separator + templatename );
            FileUtils.write(fileTemplate, "", "UTF-8");
        } catch (IOException ex) {
            logger.error(ex.getMessage());
            throw new CPlatkmException(ex.getMessage()); 
        }
    }

    public String getTemplateContent(String artifactFolder, String templatename) throws CPlatkmException {
 
        try {
            
            File fileTemplate = new File(currentWorkSpace.getAbsolutePath() + File.separator +
                artifactFolder + File.separator + templatename );
            return FileUtils.readFileToString(fileTemplate, "UTF-8");
            
        } catch (IOException ex) {
            logger.error(ex.getMessage());
            throw new CPlatkmException(ex.getMessage()); 
        }
    }

    public void saveTemplateContent(String artifactFolder, String templatename, String text) throws CPlatkmException {
 
         try {
            
            File fileTemplate = new File(currentWorkSpace.getAbsolutePath() + 
                File.separator + artifactFolder + File.separator + templatename );
            FileUtils.writeStringToFile(fileTemplate, text, "UTF-8");
        } catch (IOException ex) {
            logger.error(ex.getMessage());
            throw new CPlatkmException(ex.getMessage()); 
        }
    }

    public List<Table> loadTables(DatabaseData databaseData) throws CPlatkmException {
                
        try {
            Class.forName(databaseData.getDriver());
            Connection con = DriverManager.getConnection(databaseData.getUrl(),
                    databaseData.getUser(),
                    databaseData.getPassword());
            
            DatabaseReader databaseReader = new DatabaseReader(con, Util.stringToList(databaseData.getExcludedTables(), ","));
            return databaseReader.readTables(databaseData.getCatalog(), databaseData.getSchema(), "%",  new String[] {"TABLE", "VIEW"});
        
        } catch (ClassNotFoundException | IllegalAccessException | IllegalArgumentException | InstantiationException | NoSuchMethodException | SecurityException | InvocationTargetException | SQLException ex) {
            logger.error(ex.getMessage());
            throw new CPlatkmException(ex.getMessage()); 
        }
    }
  
    public void treeAddFNodeFromParentTypeAndId(TreeNodeTypes treeNodeTypes, CGTreeNode childNode, String id) {
        CGTreeNode node = searchNodeByType((CGTreeNode)defaultTreeModel.getRoot(), treeNodeTypes.name(), id, null);
        node.add(childNode);
        defaultTreeModel.nodeStructureChanged(node);
    }

    public void treeAddFNodeFromChildTypeAndParentId(TreeNodeTypes treeNodeTypes, CGTreeNode childNode, String parentId) {
        CGTreeNode node = searchNodeByType((CGTreeNode)defaultTreeModel.getRoot(), treeNodeTypes.name(), null, parentId);
        node.add(childNode);
        defaultTreeModel.nodeStructureChanged(node);
    }
    
    public void treeAddFNodeFromParentType(TreeNodeTypes treeNodeTypes, CGTreeNode childNode) {
        CGTreeNode node = searchNodeByType((CGTreeNode)defaultTreeModel.getRoot(), treeNodeTypes.name(), null, null);
        node.add(childNode);
        defaultTreeModel.nodeStructureChanged(node);
    }

    public void treeUpdateNode(TreeNodeTypes treeNodeTypes, String id, String name,  Object object) {
        CGTreeNode node = searchNodeByType((CGTreeNode)defaultTreeModel.getRoot(), treeNodeTypes.name(), id, null);
        node.setUserObject(object);
        node.setName(name);
        defaultTreeModel.nodeStructureChanged(node);
    }

    public void treeRemoveNodeFromParentType(TreeNodeTypes treeNodeTypes, String parentId, String id) {
        CGTreeNode node = searchNodeByType((CGTreeNode)defaultTreeModel.getRoot(), treeNodeTypes.name(), null, parentId);
        if(node != null){
            CGTreeNode cGTreeNodeFound;
            for (int i = 0; i < node.getChildCount(); i++) {
                 cGTreeNodeFound = (CGTreeNode) node.getChildAt(i);
                if(id.equals(cGTreeNodeFound.getId())){
                    node.remove(i);
                    break;
                }
            }
            defaultTreeModel.nodeStructureChanged(node);      
        }

    }

    public void loadConfigFile(String workSpaceFolder) throws CPlatkmException{
        
        try {
            
            TypeToken<CGenetatorConfig> type = new TypeToken<CGenetatorConfig>() {};
            this.cliConfigurationFile = new File( getRepositoryPath() + File.separator + workSpaceFolder + File.separator + "configuration");
            this.cgenetatorConfig = JsonUtil.jsonToObjectTypeReference(FileUtils.readFileToString(cliConfigurationFile, "UTF-8"), type);
            
            this.currentWorkSpace = new File( getRepositoryPath() + File.separator + workSpaceFolder);
            this.active = true;
        } catch (JsonException | IOException ex) {
            java.util.logging.Logger.getLogger(CPlatkmContentManager.class.getName()).log(Level.SEVERE, null, ex);
            throw new CPlatkmException("Error cargando el fichero de configuración");
        }
  
    }
    
    public void updateConfigFile() throws CPlatkmException{
    
        try {
            String strJson = JsonUtil.objectToJson(cgenetatorConfig);
            FileUtils.write(cliConfigurationFile, strJson, "UTF-8");
        } catch (IOException | JsonException ex) {
            java.util.logging.Logger.getLogger(CPlatkmContentManager.class.getName()).log(Level.SEVERE, null, ex);
            throw new CPlatkmException("Error guardando el fichero de configuración");
        }
        
    }
    
    private static CGTreeNode searchNodeByType(CGTreeNode cGTreeNode, String type, String id, String parentId) {
        
        if (cGTreeNode.getType().equals(type) && 
                (StringUtils.isBlank(id) || id.equals(cGTreeNode.getId())) && 
                (StringUtils.isBlank(parentId) || parentId.equals(cGTreeNode.getParentId()))){
            return cGTreeNode;
        }
        CGTreeNode child;
        CGTreeNode result;
        for (int i = 0; i < cGTreeNode.getChildCount(); i++) {
            child = (CGTreeNode) cGTreeNode.getChildAt(i);
            result = searchNodeByType(child, type, id, parentId);
            if (result != null) {
                return result;
            }
        }

        return null;
    }
    
    public String getRepositoryPath(){
        String userHomePath = System.getProperty("user.home");
        return userHomePath + File.separator + C_PLATKMFRAMEWORK_FOLDER + File.separator + C_REPOSITORY;
    }
    
    public List<String> getWorkSpaceFolders(){
        String userHomePath = System.getProperty("user.home"); 
        File repositoryFolder = new File(userHomePath + File.separator + C_PLATKMFRAMEWORK_FOLDER + File.separator + C_REPOSITORY);
           
        List<String> workSpaceList = new ArrayList<>();
                
        File file;
        for (File listFile : repositoryFolder.listFiles()) {
            file = listFile;
            if(file.exists() && file.isDirectory() && !file.isHidden())
                workSpaceList.add(file.getName());
        }
        
        return workSpaceList;
    }
    
    public void loadSetting(){
        
        
        try {
            String userHomePath = System.getProperty("user.home"); 
            File cgeneratorSettingFolder = new File(userHomePath + File.separator + C_PLATKMFRAMEWORK_FOLDER + File.separator + C_CGENERATOR);
            if(!cgeneratorSettingFolder.exists() || !cgeneratorSettingFolder.isDirectory()){
                FileUtils.forceMkdir(cgeneratorSettingFolder);
                cGeneratorSettings = new CGeneratorSettings();
                String strJson = JsonUtil.objectToJson(cGeneratorSettings);
                FileUtils.write(new File(cgeneratorSettingFolder.getAbsolutePath() + File.separator + "settings"), strJson, "UTF-8");
            }else{
                TypeToken<CGeneratorSettings> type = new TypeToken<CGeneratorSettings>() {};
                File settingsFile = new File(userHomePath + File.separator + C_PLATKMFRAMEWORK_FOLDER + File.separator + C_CGENERATOR + File.separator + "settings");
                this.cGeneratorSettings = JsonUtil.jsonToObjectTypeReference(FileUtils.readFileToString(settingsFile, "UTF-8"), type);
            }         

        } catch (IOException | JsonException ex) {
            java.util.logging.Logger.getLogger(CPlatkmContentManager.class.getName()).log(Level.SEVERE, null, ex);
            cGeneratorSettings = new CGeneratorSettings();
        }  
        
        try {
            String userHomePath = System.getProperty("user.home"); 
            File repositoryFolder = new File(userHomePath + File.separator + C_PLATKMFRAMEWORK_FOLDER + File.separator + C_REPOSITORY);
            if(!repositoryFolder.exists() || !repositoryFolder.isDirectory()){
                FileUtils.forceMkdir(repositoryFolder);
            }        

        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(CPlatkmContentManager.class.getName()).log(Level.SEVERE, null, ex);
        }  
     
    }
 
    public void saveSetting(){
        try {
            String userHomePath = System.getProperty("user.home");
            String strJson = JsonUtil.objectToJson(cGeneratorSettings);
            FileUtils.write(new File(userHomePath + File.separator + C_PLATKMFRAMEWORK_FOLDER + File.separator + C_CGENERATOR + File.separator + "settings"), strJson, "UTF-8");
        } catch (IOException | JsonException ex) {
            java.util.logging.Logger.getLogger(CPlatkmContentManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
 
    
    public void createWorkSpace(String newWorkSpaceName) throws CPlatkmException{
        try {
            this.currentWorkSpace = new File( this.getRepositoryPath() + File.separator + newWorkSpaceName);
            this.cliConfigurationFile = new File(this.currentWorkSpace + File.separator + "configuration");
            this.cgenetatorConfig = new CGenetatorConfig();
            this.cgenetatorConfig.setName(newWorkSpaceName);
            String strJson = JsonUtil.objectToJson(this.cgenetatorConfig );
            FileUtils.write(cliConfigurationFile, strJson, "UTF-8");
            
            this.active = true;
            
        } catch (IOException | JsonException ex) {
            java.util.logging.Logger.getLogger(CPlatkmContentManager.class.getName()).log(Level.SEVERE, null, ex);
            throw new CPlatkmException("Error creando el workspace");
        }   
    }
 
    public JFrame getMainFrame() {
        return mainFrame;
    }

    public void setMainFrame(JFrame mainFrame) {
        this.mainFrame = mainFrame;
    }
    
    public void setTreModel(DefaultTreeModel defaultTreeModel) {
        this.defaultTreeModel = defaultTreeModel;
    }    
    
    public CGenetatorConfig getCgenetatorConfig() {
        return cgenetatorConfig;
    }

    public void setCgenetatorConfig(CGenetatorConfig cgenetatorConfig) {
        this.cgenetatorConfig = cgenetatorConfig;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public File getCurrentWorkSpace() {
        return currentWorkSpace;
    }

 
    public CGeneratorSettings getcGeneratorSettings() {
        return cGeneratorSettings;
    }
 

    public List<ContentType> getContentTypes() {
        return contentTypes;
    }

    public void setContentTypes(List<ContentType> contentTypes) {
        this.contentTypes = contentTypes;
    }

    public Map<?, ?> getDefaultOpenApi() {
        return defaultOpenApi;
    }
    
    public void setDefaultOpenApi(Map<?, ?> defaultOpenApi) {
        this.defaultOpenApi = defaultOpenApi;
    }
    
    
    
}
