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

import org.platkmframework.cplatkm.desktop.commons.TreeNodeTypes;
import org.apache.commons.lang3.StringUtils;
import org.platkmframework.cplatkm.desktop.tree.CGTreeNode;
import org.platkmframework.cplatkm.desktop.tree.CGTreeNodeBase;
import org.platkmframework.cplatkm.processor.data.Artifact;
import org.platkmframework.cplatkm.processor.data.CGenetatorConfig;
import org.platkmframework.cplatkm.processor.data.DataTypeMapping;
import org.platkmframework.cplatkm.processor.data.DatabaseData;
import org.platkmframework.cplatkm.processor.data.GlobalData;
import org.platkmframework.cplatkm.processor.data.OpenApiImported;
import org.platkmframework.cplatkm.processor.data.RunConfiguration;
import org.platkmframework.cplatkm.processor.data.Template;
import org.platkmframework.cplatkm.processor.data.openapi.CGComponents;
import org.platkmframework.cplatkm.processor.data.openapi.CGOpenAPI;
import org.platkmframework.cplatkm.processor.data.openapi.CGPaths;
import org.platkmframework.cplatkm.processor.data.openapi.CGTags;

/**
 *
 * @author Eduardo Iglesias
 */
public class MainTreeCreator {
    
    private static final TreeNodeCreator treeNodeCreatorDefault = (Object userObject, String nodeType, String name, String id) -> {
            return new CGTreeNode(userObject, nodeType, name, id );
    };
    
    public static MainTreeCreator getInstance() {
        return MainTreeCreatorHolder.INSTANCE;
    }
 
    private static class MainTreeCreatorHolder {
        private static final MainTreeCreator INSTANCE = new MainTreeCreator();
    }
        
    public CGTreeNodeBase create(CGenetatorConfig cGenetatorConfig){
        return create(cGenetatorConfig, treeNodeCreatorDefault);
    }
    
    public CGTreeNodeBase create(CGenetatorConfig cGenetatorConfig, TreeNodeCreator treeNodeCreator){
        
        CGTreeNodeBase treeNodeRoot = treeNodeCreator.create("Projects", TreeNodeTypes.ROOT_TYPE.name(), null, null); //   new CGTreeNode("Projects", TreeNodeTypes.ROOT_TYPE.name() );
        CGTreeNodeBase treeNodeDatabase = treeNodeCreator.create("DataBases Connections", TreeNodeTypes.DATABASE_SEPARATOR_TYPE.name(), null, null);//new CGTreeNode("DataBases", TreeNodeTypes.DATABASE_SEPARATOR_TYPE.name());
        CGTreeNodeBase treeNodeDataType = treeNodeCreator.create("DataType Mapping", TreeNodeTypes.DATATYPE_SEPARATOR_TYPE.name(), null, null);//new CGTreeNode("DataType Mapping", TreeNodeTypes.DATATYPE_SEPARATOR_TYPE.name());
        CGTreeNodeBase treeNodeArtifacts = treeNodeCreator.create("Artifacts",TreeNodeTypes.ARTIFACT_SEPARATOR_TYPE.name(), null, null);
        CGTreeNodeBase treeNodeGlobalDataConfig = treeNodeCreator.create("Global Data", TreeNodeTypes.GLOBAL_DATA_SEPARATOR_TYPE.name(), null, null);//new CGTreeNode("Global Data", TreeNodeTypes.GLOBAL_DATA_SEPARATOR_TYPE.name());
        CGTreeNodeBase treeNodeOpenAPI = treeNodeCreator.create("OpenAPI", TreeNodeTypes.OPEN_API_SEPARATOR_TYPE.name(), null, null); //new CGTreeNode("Run Configuration", TreeNodeTypes.RUN_CONFIGURATION_SEPARATOR_TYPE.name());
        CGTreeNodeBase treeNodeRunConfig = treeNodeCreator.create("Run Configuration", TreeNodeTypes.RUN_CONFIGURATION_SEPARATOR_TYPE.name(), null, null); //new CGTreeNode("Run Configuration", TreeNodeTypes.RUN_CONFIGURATION_SEPARATOR_TYPE.name());
        
        CGTreeNodeBase treeNode;
        for (DatabaseData database : cGenetatorConfig.getDatabases()) {
            treeNodeDatabase.add(createNodeDataBase(database, treeNodeCreator));
        }
       
        for (DataTypeMapping dataTypeMapping : cGenetatorConfig.getDatatypes()) {
            treeNodeDataType.add(createNodeDataTypeMapping(dataTypeMapping, treeNodeCreator));
        }
        
        for (Artifact artifact : cGenetatorConfig.getArtifacts() ) {
            treeNode = createNodeArtifact(artifact, treeNodeCreator); //   treeNodeCreator.create(artifact, TreeNodeTypes.ARTIFACT_TYPE.name(), artifact.getLabel(), artifact.getId());//new CGTreeNode(artifact, TreeNodeTypes.ARTIFACT_TYPE.name(), artifact.getLabel(), artifact.getId());
            treeNodeArtifacts.add(treeNode);
        }
        
        for (RunConfiguration runConfiguration : cGenetatorConfig.getRunConfigurations()) {
            treeNodeRunConfig.add(createNodeRunConfiguration(runConfiguration, treeNodeCreator));
        }
        
        for (GlobalData globalData : cGenetatorConfig.getGlobalDatas()) {
            treeNodeGlobalDataConfig.add(createNodeGlobalData(globalData, treeNodeCreator));
        }
        
        for (OpenApiImported cgOpenAPI : cGenetatorConfig.getOpenAPIs()) {
            treeNodeOpenAPI.add(createOpenAPI(cgOpenAPI, treeNodeCreator));
        }
        
        treeNodeRoot.add(treeNodeDatabase);
        treeNodeRoot.add(treeNodeDataType); 
        treeNodeRoot.add(treeNodeArtifacts);
        treeNodeRoot.add(treeNodeGlobalDataConfig); 
        treeNodeRoot.add(treeNodeOpenAPI); 
        treeNodeRoot.add(treeNodeRunConfig);
        
        return treeNodeRoot;
    }
    
    public CGTreeNodeBase createNodeDataBase(DatabaseData database, TreeNodeCreator treeNodeCreator){
        return treeNodeCreator.create(database, TreeNodeTypes.DATABASE_TYPE.name(), database.getName(), database.getId());//new CGTreeNode(database, TreeNodeTypes.DATABASE_TYPE.name(), database.getName(), database.getId());
    }
    
     public CGTreeNodeBase createNodeDataBase(DatabaseData database){
        return createNodeDataBase(database, treeNodeCreatorDefault);
    }

    public  CGTreeNodeBase createNodeDataTypeMapping(DataTypeMapping dataTypeMapping, TreeNodeCreator treeNodeCreator) {
        return treeNodeCreator.create(dataTypeMapping, TreeNodeTypes.DATATYPE.name(), dataTypeMapping.getName(), dataTypeMapping.getId());
    }
    
    public  CGTreeNodeBase createNodeDataTypeMapping(DataTypeMapping dataTypeMapping) {
        return createNodeDataTypeMapping(dataTypeMapping, treeNodeCreatorDefault);
    }

    public CGTreeNodeBase createNodeArtifact(Artifact artifact, TreeNodeCreator treeNodeCreator) {
        CGTreeNodeBase cgTreeNodeBaseArt = treeNodeCreator.create(artifact, TreeNodeTypes.ARTIFACT_TYPE.name(), artifact.getLabel(), artifact.getId());
        
        CGTreeNodeBase treeNodeTemplateSeparator = treeNodeCreator.create("Templates", TreeNodeTypes.TEMPLATE_SEPARATOR_TYPE.name(),null, null);//new CGTreeNode("Templates", TreeNodeTypes.TEMPLATE_SEPARATOR_TYPE.name());
        treeNodeTemplateSeparator.setParentId(artifact.getId());
        CGTreeNodeBase treeNodeTemplate;
        
        for (Template template :  artifact.getTemplates()) {
            treeNodeTemplate = createNodeTemplate(template, treeNodeCreator); // treeNodeCreator.create(template, TreeNodeTypes.TEMPLATE_TYPE.name() , template.getLabel(), template.getId());//new CGTreeNode(template, TreeNodeTypes.TEMPLATE_TYPE.name() , template.getLabel(), template.getId());
            treeNodeTemplate.setParentId(artifact.getId());
            treeNodeTemplateSeparator.add(treeNodeTemplate); 
        }
        cgTreeNodeBaseArt.add(treeNodeTemplateSeparator);
        
        return cgTreeNodeBaseArt;
    }
    
    public CGTreeNodeBase createNodeArtifact(Artifact artifact) {
        return createNodeArtifact(artifact, treeNodeCreatorDefault);
    }

    public CGTreeNodeBase createNodeTemplate(Template template, TreeNodeCreator treeNodeCreator) {
        return treeNodeCreator.create(template, TreeNodeTypes.TEMPLATE_TYPE.name() , template.getLabel(), template.getId());
    }
    
    public CGTreeNodeBase createNodeTemplate(Template template) {
        return createNodeTemplate(template, treeNodeCreatorDefault);
    }

    public CGTreeNodeBase createNodeRunConfiguration(RunConfiguration runConfiguration, TreeNodeCreator treeNodeCreator) {
       return treeNodeCreator.create(runConfiguration, TreeNodeTypes.RUN_CONFIGURATION_TYPE.name(), runConfiguration.getName(), runConfiguration.getId()); 
    }
    
    public CGTreeNodeBase createNodeRunConfiguration(RunConfiguration runConfiguration) {
       return createNodeRunConfiguration(runConfiguration, treeNodeCreatorDefault);
    }

    public CGTreeNodeBase createNodeGlobalData(GlobalData globalData, TreeNodeCreator treeNodeCreator) {
        return treeNodeCreator.create(globalData, TreeNodeTypes.GLOBAL_DATA_TYPE.name(), globalData.getName(), globalData.getId());
    }
    
    public CGTreeNodeBase createNodeGlobalData(GlobalData globalData) {
        return createNodeGlobalData(globalData, treeNodeCreatorDefault);
    }
    
    public CGTreeNodeBase createOpenAPI(OpenApiImported cgOpenAPI, TreeNodeCreator treeNodeCreator) {
        return treeNodeCreator.create(cgOpenAPI, TreeNodeTypes.OPEN_API_TYPE.name(), cgOpenAPI.getName(), cgOpenAPI.getId());
    }
    
   public CGTreeNodeBase createOpenAPI(OpenApiImported cgOpenAPI) {
        return createOpenAPI(cgOpenAPI, treeNodeCreatorDefault);
    }
    
    /**
     public CGTreeNodeBase createOpenAPI(CGOpenAPI cgOpenAPI, TreeNodeCreator treeNodeCreator) {
        
        CGTreeNodeBase cgTreeNodeOpenAPI = treeNodeCreator.create(cgOpenAPI, TreeNodeTypes.OPEN_API_TYPE.name(), cgOpenAPI.getInfo().getTitle(), cgOpenAPI.getId());
     
        CGTreeNodeBase treeNodeCGComponents;
        
        CGTreeNodeBase treeNodeTagsSeparator = treeNodeCreator.create("Tags", TreeNodeTypes.OPEN_API_TAG_SEPARATOR_TYPE.name(),null, null);//new CGTreeNode("Templates", TreeNodeTypes.TEMPLATE_SEPARATOR_TYPE.name());
        treeNodeTagsSeparator.setParentId(cgOpenAPI.getId());
        
        addCGTAgsToSepartor(cgOpenAPI, treeNodeTagsSeparator);

        cgTreeNodeOpenAPI.add(treeNodeTagsSeparator);
        
        CGTreeNodeBase treeNodeComponentsSeparator = treeNodeCreator.create("Components", TreeNodeTypes.OPEN_API_COMPONENT_SEPARATOR_TYPE.name(),null, null);//new CGTreeNode("Templates", TreeNodeTypes.TEMPLATE_SEPARATOR_TYPE.name());
        treeNodeComponentsSeparator.setParentId(cgOpenAPI.getId());
        cgTreeNodeOpenAPI.add(treeNodeComponentsSeparator);
        for (CGComponents cgComponents :  cgOpenAPI.getComponents()) {
            treeNodeCGComponents = createNodeOpenApiComponents(cgComponents, treeNodeCreator); 
            treeNodeComponentsSeparator.add(treeNodeCGComponents);
        }
        
        return cgTreeNodeOpenAPI;
    }
  
    public CGTreeNodeBase createOpenAPI(CGOpenAPI cgOpenAPI) {
        return createOpenAPI(cgOpenAPI, treeNodeCreatorDefault);
    }
      */
    public CGTreeNodeBase createNodeOpenApiTags(CGTags cgTags, TreeNodeCreator treeNodeCreator, String parentId) {
       CGTreeNodeBase cgTagsNodeBase = treeNodeCreator.create(cgTags, TreeNodeTypes.OPEN_API_TAG_TYPE.name(), cgTags.getName(), cgTags.getId());
       cgTagsNodeBase.setParentId(parentId);
       return cgTagsNodeBase;
    }
    
    public CGTreeNodeBase createNodeOpenApiTags(CGTags cgTags, String parentId) {
       return createNodeOpenApiTags(cgTags, treeNodeCreatorDefault, parentId);
    }
   
    public void addCGTAgsToSepartor(CGOpenAPI cgOpenAPI, CGTreeNodeBase treeNodeTagsSeparator) {
        addCGTAgsToSepartor(cgOpenAPI, treeNodeTagsSeparator, treeNodeCreatorDefault);
    }
    
    public void addCGTAgsToSepartor(CGOpenAPI cgOpenAPI, CGTreeNodeBase treeNodeTagsSeparator, TreeNodeCreator treeNodeCreator) {
        CGTreeNodeBase treeNodeApiTags;
        CGTreeNodeBase treeNodeCGPaths;
        
        for (CGTags cgTags :  cgOpenAPI.getTags()) {
            treeNodeApiTags = createNodeOpenApiTags(cgTags, treeNodeCreator, cgOpenAPI.getId()); // treeNodeCreator.create(template, TreeNodeTypes.TEMPLATE_TYPE.name() , template.getLabel(), template.getId());//new CGTreeNode(template, TreeNodeTypes.TEMPLATE_TYPE.name() , template.getLabel(), template.getId());
            
            CGTreeNodeBase treeNodePathSeparator = treeNodeCreator.create("Paths", TreeNodeTypes.OPEN_API_PATHS_SEPARATOR_TYPE.name(),null, null);
            treeNodePathSeparator.setParentId(cgTags.getId());     
            treeNodeApiTags.add(treeNodePathSeparator);
            
            for (CGPaths cgPaths :  cgTags.getPaths()) {
                treeNodeCGPaths = createNodeOpenApiPaths(cgPaths, treeNodeCreator); 
                treeNodeCGPaths.setParentId(cgOpenAPI.getId());
                treeNodePathSeparator.add(treeNodeCGPaths);
            }
            treeNodeTagsSeparator.add(treeNodeApiTags);
        }
    }
    
    public void addNewTags(CGTags cgTags,CGOpenAPI cgOpenAPI, CGTreeNodeBase treeNodeTagsSeparator) {
      
        CGTreeNodeBase treeNodeApiTags = createNodeOpenApiTags(cgTags, treeNodeCreatorDefault, cgOpenAPI.getId()); // treeNodeCreator.create(template, TreeNodeTypes.TEMPLATE_TYPE.name() , template.getLabel(), template.getId());//new CGTreeNode(template, TreeNodeTypes.TEMPLATE_TYPE.name() , template.getLabel(), template.getId());

        CGTreeNodeBase treeNodePathSeparator = treeNodeCreatorDefault.create("Paths", TreeNodeTypes.OPEN_API_PATHS_SEPARATOR_TYPE.name(),null, null);
        treeNodePathSeparator.setParentId(cgTags.getId());     
        treeNodeApiTags.add(treeNodePathSeparator);
 
        treeNodeTagsSeparator.add(treeNodeApiTags);
        
    }
    
    public CGTreeNodeBase createNodeOpenApiPaths(CGPaths cgPaths, TreeNodeCreator treeNodeCreator) {
        CGTreeNodeBase cgTagsNodeBase = treeNodeCreator.create(cgPaths, TreeNodeTypes.OPEN_API_PATHS_TYPE.name(), getPathsLabel(cgPaths), cgPaths.getId());
        return cgTagsNodeBase;
    }
    
    public CGTreeNodeBase createNodeOpenApiPaths(CGPaths cgPaths) {
        return createNodeOpenApiPaths(cgPaths, treeNodeCreatorDefault);
    }
    
    public String getPathsLabel(CGPaths cgPaths) {
        return StringUtils.isNotBlank(cgPaths.getSummary())?cgPaths.getSummary(): cgPaths.getPath();
    }
    
    public CGTreeNodeBase createNodeOpenApiComponents(CGComponents cgComponents, TreeNodeCreator treeNodeCreator) {
        return treeNodeCreator.create(cgComponents, TreeNodeTypes.OPEN_API_COMPONENT_TYPE.name(), " (" + cgComponents.getGroupType()+ ") " + cgComponents.getName(), cgComponents.getId());
    }
    
    public CGTreeNodeBase createNodeOpenApiComponents(CGComponents cgComponents) {
        return createNodeOpenApiComponents(cgComponents, treeNodeCreatorDefault);
    }
        
}
