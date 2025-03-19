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
package org.platkmframework.cplatkm.desktop;

import java.util.logging.Level;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.tree.TreePath; 
 
import org.platkmframework.cplatkm.desktop.core.CGeneratorContentManager;
import org.platkmframework.cplatkm.desktop.tree.CGTreeNode;
import org.platkmframework.cplatkm.desktop.panels.artifacts.ArtifactPanel;
import org.platkmframework.cplatkm.desktop.panels.DatabasePanel;
import org.platkmframework.cplatkm.desktop.panels.runconfigurations.RunConfigurationPanel;
import org.platkmframework.cplatkm.desktop.panels.artifacts.TemplateCodeEditorJPanel;
import org.platkmframework.cplatkm.desktop.panels.artifacts.TemplatesPanel; 
import org.platkmframework.cplatkm.desktop.panels.datatype.DataTypePanel;
import org.platkmframework.cplatkm.processor.exception.CGeneratorException;
import org.platkmframework.util.JsonUtil;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import com.formdev.flatlaf.FlatLightLaf;
import com.google.gson.reflect.TypeToken;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.platkmframework.cplatkm.desktop.core.MainTreeCreator;
import org.platkmframework.cplatkm.desktop.commons.TreeNodeTypes;
import org.platkmframework.cplatkm.desktop.core.contentype.ContentTypeReader;
import org.platkmframework.cplatkm.desktop.core.contentype.openapi.DefaultOpenApiReader;
import org.platkmframework.cplatkm.desktop.importexport.ExportImportEnum;
import org.platkmframework.cplatkm.desktop.importexport.ExportJDialog;
import org.platkmframework.cplatkm.desktop.panels.DataBaseEditorPanel;
import org.platkmframework.cplatkm.desktop.panels.artifacts.ArtifactEditorPanel;
import org.platkmframework.cplatkm.desktop.panels.artifacts.TemplateEditorJPanel;
import org.platkmframework.cplatkm.desktop.panels.datatype.DataTypeEditorJPanel; 
import org.platkmframework.cplatkm.desktop.panels.globaldata.GlobalDataEditorPanel;
import org.platkmframework.cplatkm.desktop.panels.globaldata.GlobalDataPanel;
import org.platkmframework.cplatkm.desktop.panels.openapi.OpenAPIsPanel;
import org.platkmframework.cplatkm.desktop.panels.openapi.OpenApiEditorPanel;
import org.platkmframework.cplatkm.desktop.panels.runconfigurations.RunConfigurationEditorPanel;
import org.platkmframework.cplatkm.desktop.tree.CGTreeNodeBase;
import org.platkmframework.cplatkm.processor.data.Artifact;
import org.platkmframework.cplatkm.processor.data.CGenetatorConfig;
import org.platkmframework.cplatkm.processor.data.DataTypeMapping;
import org.platkmframework.cplatkm.processor.data.DatabaseData;
import org.platkmframework.cplatkm.processor.data.GlobalData;
import org.platkmframework.cplatkm.processor.data.OpenApiImported;
import org.platkmframework.cplatkm.processor.data.RunConfiguration;
import org.platkmframework.cplatkm.processor.data.Template;
import org.platkmframework.cplatkm.processor.data.openapi.CGOpenAPI;
import org.platkmframework.cplatkm.processor.openapi.OpenApiReader;
import org.platkmframework.util.JsonException;
import org.platkmframework.util.Util;

/**
 *   Author:
 *     Eduardo Iglesias
 *   Contributors:
 *   	Eduardo Iglesias - initial API and implementation
 */
public class MainJFrame extends javax.swing.JFrame {

    private static final Logger logger = LoggerFactory.getLogger(MainJFrame.class);
    
    JFileChooser fileChooser;
    JFileChooser fileChooserWorkSpace;
      
    private final DatabasePanel databasePanel;
    DataBaseEditorPanel  dataBaseEditorPanel;
    
    private final ArtifactPanel artifactPanel;
    ArtifactEditorPanel artifactEditorPanel;
     
    private final DataTypePanel dataTypePanel;
    private final DataTypeEditorJPanel dataTypeEditorJPanel;
    
    private final TemplateEditorJPanel templateEditorJPanel;
    private final TemplatesPanel templatesPanel; 
    private final TemplateCodeEditorJPanel templateCodeEditorJPanel;
    
    private final RunConfigurationEditorPanel runConfigurationEditorPanel;
    private final RunConfigurationPanel runConfigurationPanel;
    
    
    private DefaultTreeModel defaultTreeModel;
   
    private final ExportJDialog exportJDialog; 
    private final PropertiesJDialog propertiesJDialog;
    
    private final GlobalDataEditorPanel globalDataEditorPanel;
    private final GlobalDataPanel globalDataPanel;
    
    private final OpenApiEditorPanel openApiEditorPanel;
    private final OpenAPIsPanel  openAPIsPanel;
    
    /**private final OpenTagsPanel openTagsPanel;
    private final OpenTagsDialog openTagsDialog;
   
    
    private final OpenApiPathsPanel openApiPathsPanel;
    private final OpenApiPathsEditorPanel openApiPathsEditorPanel;
     */
    
    /**
     * Creates new form NewJFrame
     */
    public MainJFrame() {
        initComponents();
        treeProject.setToggleClickCount(0);
        setLocationRelativeTo(null);
        JsonUtil.init("dd-MM-yyyy:HH:mm:ss","dd-MM-yyyy","HH:mm:ss");
        ImageIcon icon = new ImageIcon(MainJFrame.class.getResource("/images/cgenerator_small.png"));
        setIconImage(icon.getImage());
        
        CGeneratorContentManager.getInstance().setContentTypes(ContentTypeReader.read()); 
        CGeneratorContentManager.getInstance().setDefaultOpenApi(DefaultOpenApiReader.read());
        
        fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        
        fileChooserWorkSpace = new JFileChooser();
        fileChooserWorkSpace.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
          
        dataBaseEditorPanel = new DataBaseEditorPanel();
        databasePanel       = new DatabasePanel(dataBaseEditorPanel, scrollPaneMain);
        
        artifactEditorPanel = new ArtifactEditorPanel();
        artifactPanel = new ArtifactPanel(artifactEditorPanel, scrollPaneMain);
        
        dataTypeEditorJPanel = new DataTypeEditorJPanel();
        dataTypePanel = new DataTypePanel(dataTypeEditorJPanel, scrollPaneMain);
        
        templateEditorJPanel = new TemplateEditorJPanel();
        templatesPanel = new TemplatesPanel(templateEditorJPanel, scrollPaneMain);
        templateCodeEditorJPanel = new TemplateCodeEditorJPanel();
        
        runConfigurationEditorPanel = new RunConfigurationEditorPanel();
        runConfigurationPanel = new RunConfigurationPanel(runConfigurationEditorPanel, scrollPaneMain);
        
        globalDataEditorPanel = new GlobalDataEditorPanel();
        globalDataPanel = new GlobalDataPanel(globalDataEditorPanel, scrollPaneMain);
        
        propertiesJDialog = new PropertiesJDialog(CGeneratorContentManager.getInstance().getMainFrame(), true);
        
        exportJDialog = new ExportJDialog(CGeneratorContentManager.getInstance().getMainFrame(), true);
        
        openApiEditorPanel = new OpenApiEditorPanel();
        openAPIsPanel = new OpenAPIsPanel(openApiEditorPanel, scrollPaneMain);
        
        /**openTagsDialog = new OpenTagsDialog(CGeneratorContentManager.getInstance().getMainFrame(), true);
        openTagsPanel = new OpenTagsPanel();
        
        openApiPathsPanel = new OpenApiPathsPanel();
        openApiPathsEditorPanel = new OpenApiPathsEditorPanel();
        */
        
        loadInitConfiguration(); 
        
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jSeparator2 = new javax.swing.JSeparator();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        jSplitPane1 = new javax.swing.JSplitPane();
        jScrollPane3 = new javax.swing.JScrollPane();
        treeProject = new javax.swing.JTree();
        scrollPaneMain = new javax.swing.JScrollPane();
        jMenuBar1 = new javax.swing.JMenuBar();
        menuFile = new javax.swing.JMenu();
        subMenuNewWorkSpace = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        menuOpenWorkSpace = new javax.swing.JMenuItem();
        jSeparator3 = new javax.swing.JPopupMenu.Separator();
        menuProperties = new javax.swing.JMenuItem();
        jSeparator4 = new javax.swing.JPopupMenu.Separator();
        menuSwitchWorkspace = new javax.swing.JMenu();
        jMenu2 = new javax.swing.JMenu();
        subMenuImport = new javax.swing.JMenuItem();
        subMenuExport = new javax.swing.JMenuItem();
        jSeparator5 = new javax.swing.JPopupMenu.Separator();
        subMenuImportOpenApi = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("CGenerator");

        jSplitPane1.setDividerLocation(140);

        javax.swing.tree.DefaultMutableTreeNode treeNode1 = new javax.swing.tree.DefaultMutableTreeNode("CGenerator");
        javax.swing.tree.DefaultMutableTreeNode treeNode2 = new javax.swing.tree.DefaultMutableTreeNode("Databases");
        javax.swing.tree.DefaultMutableTreeNode treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("db1");
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("db2");
        treeNode2.add(treeNode3);
        treeNode1.add(treeNode2);
        treeNode2 = new javax.swing.tree.DefaultMutableTreeNode("Artifacts");
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("artifact1");
        javax.swing.tree.DefaultMutableTreeNode treeNode4 = new javax.swing.tree.DefaultMutableTreeNode("template1");
        treeNode3.add(treeNode4);
        treeNode4 = new javax.swing.tree.DefaultMutableTreeNode("template2");
        treeNode3.add(treeNode4);
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("artifact2");
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("artifact3");
        treeNode4 = new javax.swing.tree.DefaultMutableTreeNode("template31");
        treeNode3.add(treeNode4);
        treeNode4 = new javax.swing.tree.DefaultMutableTreeNode("template32");
        treeNode3.add(treeNode4);
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("artifact4");
        treeNode2.add(treeNode3);
        treeNode1.add(treeNode2);
        treeNode2 = new javax.swing.tree.DefaultMutableTreeNode("Run Configurations");
        treeNode1.add(treeNode2);
        treeProject.setModel(new javax.swing.tree.DefaultTreeModel(treeNode1));
        treeProject.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                treeProjectMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(treeProject);

        jSplitPane1.setLeftComponent(jScrollPane3);
        jSplitPane1.setRightComponent(scrollPaneMain);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 826, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 470, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Configurations", jPanel2);

        menuFile.setText("File");

        subMenuNewWorkSpace.setText("New Workspace");
        subMenuNewWorkSpace.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                subMenuNewWorkSpaceActionPerformed(evt);
            }
        });
        menuFile.add(subMenuNewWorkSpace);
        menuFile.add(jSeparator1);

        menuOpenWorkSpace.setText("Open Workspace");
        menuOpenWorkSpace.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuOpenWorkSpaceActionPerformed(evt);
            }
        });
        menuFile.add(menuOpenWorkSpace);
        menuFile.add(jSeparator3);

        menuProperties.setText("Properties");
        menuProperties.setEnabled(false);
        menuProperties.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuPropertiesActionPerformed(evt);
            }
        });
        menuFile.add(menuProperties);
        menuFile.add(jSeparator4);

        menuSwitchWorkspace.setText("Switch Workspace");
        menuFile.add(menuSwitchWorkspace);

        jMenuBar1.add(menuFile);

        jMenu2.setText("Operations");

        subMenuImport.setText("Import...");
        subMenuImport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                subMenuImportActionPerformed(evt);
            }
        });
        jMenu2.add(subMenuImport);

        subMenuExport.setText("Export...");
        subMenuExport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                subMenuExportActionPerformed(evt);
            }
        });
        jMenu2.add(subMenuExport);
        jMenu2.add(jSeparator5);

        subMenuImportOpenApi.setText("Import OpenAPI...");
        subMenuImportOpenApi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                subMenuImportOpenApiActionPerformed(evt);
            }
        });
        jMenu2.add(subMenuImportOpenApi);

        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void treeProjectMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_treeProjectMouseClicked
       
        if(CGeneratorContentManager.getInstance().isActive()){
            // Obtener la ubicación del clic
            int x = evt.getX();
            int y = evt.getY();
        
            if (evt.getClickCount() == 2) {
                TreePath path = treeProject.getPathForLocation(evt.getX(), evt.getY());
                if (path != null) {
                    DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
                    if (!node.isLeaf()) {
                        evt.consume(); // Evitar la acción predeterminada
                    }
                    CGTreeNode nodoSeleccionado = (CGTreeNode) path.getLastPathComponent();

                    if(TreeNodeTypes.DATABASE_TYPE.name().equals(nodoSeleccionado.getType())){
                       if(nodoSeleccionado.getUserObject() instanceof DatabaseData databaseData){
                           DatabaseData databaseDataFound =    CGeneratorContentManager.getInstance().
                                   getCgenetatorConfig().
                                   getDatabases().stream().filter(d-> d.getId().equals(databaseData.getId())).findFirst().orElse(null);

                        if(databaseDataFound != null){
                            //dataBaseJDialog.setLocationRelativeTo(CGeneratorContentManager.getInstance().getMainFrame()); 
                            dataBaseEditorPanel.setData(databaseDataFound);
                            scrollPaneMain.setViewportView(dataBaseEditorPanel);  
                           
                        }
                       }
                    }else if(TreeNodeTypes.DATATYPE.name().equals(nodoSeleccionado.getType())){
                        if(nodoSeleccionado.getUserObject() instanceof DataTypeMapping dataTypeMapping){
                            DataTypeMapping dataTypeMappingFound =  CGeneratorContentManager.getInstance().
                                   getCgenetatorConfig().
                                   getDatatypes().stream().filter(dt-> dt.getId().equals(dataTypeMapping.getId())).findFirst().orElse(null);
                            
                            dataTypeEditorJPanel.setData(dataTypeMappingFound);
                            scrollPaneMain.setViewportView(dataTypeEditorJPanel); 
 
                        }
                    }else if(TreeNodeTypes.ARTIFACT_TYPE.name().equals(nodoSeleccionado.getType())){
                        if(nodoSeleccionado.getUserObject() instanceof Artifact  artifact){
                            Artifact artifactFound =  CGeneratorContentManager.getInstance().
                                   getCgenetatorConfig().
                                   getArtifacts().stream().filter(db-> db.getId().equals(artifact.getId())).findFirst().orElse(null);

                            artifactEditorPanel.setData(artifactFound);
                            scrollPaneMain.setViewportView(artifactEditorPanel);
                             
                        }
                    }else if(TreeNodeTypes.RUN_CONFIGURATION_TYPE.name().equals(nodoSeleccionado.getType())){
                        if(nodoSeleccionado.getUserObject() instanceof RunConfiguration runConfiguration){
                            RunConfiguration runConfigurationFound =  CGeneratorContentManager.getInstance().
                                   getCgenetatorConfig().
                                   getRunConfigurations().stream().filter(rc-> rc.getId().equals(runConfiguration.getId())).findFirst().orElse(null);
                            
                            
                            runConfigurationEditorPanel.setData(runConfigurationFound);
                            scrollPaneMain.setViewportView(runConfigurationEditorPanel);
                            
                             
                        }
                    }else if(TreeNodeTypes.GLOBAL_DATA_TYPE.name().equals(nodoSeleccionado.getType())){
                        if(nodoSeleccionado.getUserObject() instanceof GlobalData globalData){
                            GlobalData globalDataMappingFound =  CGeneratorContentManager.getInstance().
                                   getCgenetatorConfig().getGlobalDatas()
                                    .stream().filter(dt-> dt.getId().equals(globalData.getId())).findFirst().orElse(null);

                            
                            globalDataEditorPanel.setData(globalDataMappingFound);
                            scrollPaneMain.setViewportView(globalDataEditorPanel);
                            
                        }
                    }else if(TreeNodeTypes.OPEN_API_TYPE.name().equals(nodoSeleccionado.getType())){
                        if(nodoSeleccionado.getUserObject() instanceof OpenApiImported openApiImported){
                            OpenApiImported openApiImportedFound =  CGeneratorContentManager.getInstance().
                                   getCgenetatorConfig().getOpenAPIs()
                                    .stream().filter(dt-> dt.getId().equals(openApiImported.getId())).findFirst().orElse(null);
                            
                            
                            openApiEditorPanel.setData(openApiImportedFound);
                            scrollPaneMain.setViewportView(openApiEditorPanel);
                            /*

                            openApiDialog.setLocationRelativeTo(CGeneratorContentManager.getInstance().getMainFrame());    
                            openApiDialog.setData(cgOpenAPIFound);
                            openApiDialog.setVisible(true);

                            if(openApiDialog.isUdpdated()){

                                for (int i = 0; i < CGeneratorContentManager.getInstance().getCgenetatorConfig().getOpenAPIs().size(); i++) {
                                    if (CGeneratorContentManager.getInstance().getCgenetatorConfig().getOpenAPIs().get(i).getId().equals(openApiDialog.getItem().getId())) {
                                        CGeneratorContentManager.getInstance().getCgenetatorConfig().getOpenAPIs().set(i, openApiDialog.getItem());
                                        break;
                                    }
                                }

                                try {
                                    CGeneratorContentManager.getInstance().refreshOpenAPISeparator();
                                    openAPIsPanel.refreshTable();
                                    CGeneratorContentManager.getInstance().updateConfigFile();
                                } catch (CGeneratorException ex) {
                                    java.util.logging.Logger.getLogger(MainJFrame.class.getName()).log(Level.SEVERE, null, ex);
                                    JOptionPane.showMessageDialog(this,
                                    ex.getMessage(),
                                    "Edit", JOptionPane.WARNING_MESSAGE);
                                }
                            }*/
                        }
                    }/*else if(TreeNodeTypes.OPEN_API_TAG_TYPE.name().equals(nodoSeleccionado.getType())){
                        if(nodoSeleccionado.getUserObject() instanceof CGTags cgTags){
                             
                                    
                            OpenApiImported openApiImported = CGeneratorContentManager.getInstance().
                                   getCgenetatorConfig().getOpenAPIs()
                                    .stream().filter(dt-> dt.getId().equals(nodoSeleccionado.getParentId())).findFirst().orElse(null);

                            CGTags cgTagFound = cgOpenAPI.getTags().stream().filter(t-> t.getId().equals(cgTags.getId())).findFirst().orElse(null);
                            openTagsDialog.setLocationRelativeTo(CGeneratorContentManager.getInstance().getMainFrame());    
                            openTagsDialog.setData(cgTagFound);
                            openTagsDialog.setVisible(true);

                            if(openTagsDialog.isUdpdated()){

                                for (int i = 0; i < CGeneratorContentManager.getInstance().getCgenetatorConfig().getOpenAPIs().size(); i++) {
                                    if (CGeneratorContentManager.getInstance().getCgenetatorConfig().getOpenAPIs().get(i).getId().equals(cgOpenAPI.getId())) {
                                        for (int j = 0; j < CGeneratorContentManager.getInstance().getCgenetatorConfig().getOpenAPIs().get(i).getTags().size(); j++) {
                                            if(CGeneratorContentManager.getInstance().getCgenetatorConfig().getOpenAPIs().get(i).getTags().get(j).getId().equals(openTagsDialog.getItem().getId())) {
                                                CGeneratorContentManager.getInstance().getCgenetatorConfig().getOpenAPIs().get(i).getTags().set(j, openTagsDialog.getItem());
                                                break;
                                            }
                                        } 
                                        break; 
                                    }
                                }

                                try {
                                    CGeneratorContentManager.getInstance().refreshOpenAPITagsSeparator(cgOpenAPI);
                                    openTagsPanel.refreshTable();
                                    CGeneratorContentManager.getInstance().updateConfigFile();
                                } catch (CGeneratorException ex) {
                                    java.util.logging.Logger.getLogger(MainJFrame.class.getName()).log(Level.SEVERE, null, ex);
                                    JOptionPane.showMessageDialog(this,
                                    ex.getMessage(),
                                    "Edit", JOptionPane.WARNING_MESSAGE);
                                }
                            }
                        }
                    }*/
                }
            }else{
                // Obtener la fila en la ubicación del clic
                 int fila = treeProject.getRowForLocation(x, y);
                 TreePath ruta = treeProject.getPathForLocation(x, y);

                 if (ruta != null) { 
                     // Obtener el nodo seleccionado
                     CGTreeNode nodoSeleccionado = (CGTreeNode) ruta.getLastPathComponent();
                     String type = nodoSeleccionado.getType();

                     if(TreeNodeTypes.DATABASE_SEPARATOR_TYPE.name().equals(type)){
                        databasePanel.refreshTable();
                        scrollPaneMain.setViewportView(databasePanel);                    
                     } else if(TreeNodeTypes.ARTIFACT_SEPARATOR_TYPE.name().equals(type)){
                        artifactPanel.refreshTable();
                         scrollPaneMain.setViewportView(artifactPanel);
                     }else if(TreeNodeTypes.DATATYPE_SEPARATOR_TYPE.name().equals(type)){
                        dataTypePanel.refreshTable();
                        scrollPaneMain.setViewportView(dataTypePanel);
                     }else if(TreeNodeTypes.TEMPLATE_SEPARATOR_TYPE.name().equals(type)){

                         Artifact artifact = CGeneratorContentManager.getInstance().getCgenetatorConfig().
                                 getArtifacts().
                                 stream().filter(a-> a.getId().equals(nodoSeleccionado.getParentId())).findFirst().orElse(null);
                        
                        templatesPanel.setArtifact(artifact);
                        templatesPanel.refreshTable();
                        scrollPaneMain.setViewportView(templatesPanel);

                    } else if(TreeNodeTypes.RUN_CONFIGURATION_SEPARATOR_TYPE.name().equals(type)){
                        runConfigurationPanel.refreshTable();
                        scrollPaneMain.setViewportView(runConfigurationPanel);                 
                    }else if(TreeNodeTypes.TEMPLATE_TYPE.name().equals(type)){
                        try {
                            Artifact artifact = CGeneratorContentManager.getInstance().getCgenetatorConfig().
                                    getArtifacts().
                                    stream().filter(a-> a.getId().equals(((CGTreeNode)nodoSeleccionado.getParent()).getParentId())).findFirst().orElse(null);

                           templateCodeEditorJPanel.setTemplate(artifact.getFoldername(),(Template)nodoSeleccionado.getUserObject());
                           scrollPaneMain.setViewportView(templateCodeEditorJPanel);

                        } catch (CGeneratorException ex) {
                            logger.error(ex.getMessage());
                            JOptionPane.showMessageDialog(this,
                                    ex.getMessage(),
                                    "Editor", JOptionPane.WARNING_MESSAGE);
                        }
                    }else if(TreeNodeTypes.GLOBAL_DATA_SEPARATOR_TYPE.name().equals(type)){
                        globalDataPanel.refreshTable();
                        scrollPaneMain.setViewportView(globalDataPanel);                    
                    }else if(TreeNodeTypes.OPEN_API_SEPARATOR_TYPE.name().equals(type)){
                        openAPIsPanel.refreshTable();
                        scrollPaneMain.setViewportView(openAPIsPanel);                    
                    }
                    /*else if(TreeNodeTypes.OPEN_API_TAG_SEPARATOR_TYPE.name().equals(type)){
                        openTagsPanel.refreshTableFromtable(nodoSeleccionado.getParentId());
                        scrollPaneMain.setViewportView(openTagsPanel);                    
                    }else if(TreeNodeTypes.OPEN_API_PATHS_SEPARATOR_TYPE.name().equals(type)){
                        openApiPathsPanel.refreshTableFromtable(((CGTreeNode)nodoSeleccionado.getParent()).getParentId(), nodoSeleccionado.getParentId());
                        scrollPaneMain.setViewportView(openApiPathsPanel);                    
                    }else if(TreeNodeTypes.OPEN_API_PATHS_TYPE.name().equals(type)){
                        //openApiPathsPanel.refreshTableFromtable(((CGTreeNode)nodoSeleccionado.getParent()).getParentId(), nodoSeleccionado.getParentId());
                        openApiPathsEditorPanel.refreshData(nodoSeleccionado.getParentId(),
                                (CGPaths)nodoSeleccionado.getUserObject());
                        scrollPaneMain.setViewportView(openApiPathsEditorPanel);                    
                    }*/
                }        
            }
        }
        
           
 
    }//GEN-LAST:event_treeProjectMouseClicked

    private void subMenuImportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_subMenuImportActionPerformed
       
        if(StringUtils.isNotBlank(CGeneratorContentManager.getInstance().getcGeneratorSettings().getLastImportPath())){
            File defaultDirectory = new File(CGeneratorContentManager.getInstance().getcGeneratorSettings().getLastImportPath());
            fileChooser.setCurrentDirectory(defaultDirectory);
        }
        int result = fileChooser.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            if(selectedFile.isFile() && selectedFile.exists()){
                exportJDialog.setLocationRelativeTo(CGeneratorContentManager.getInstance().getMainFrame());
                    try {
                        
                        TypeToken<CGenetatorConfig> type = new TypeToken<CGenetatorConfig>() {};
                        CGenetatorConfig cgenetatorConfig = JsonUtil.jsonToObjectTypeReference(FileUtils.readFileToString(selectedFile, "UTF-8"), type);

                        exportJDialog.openAction(ExportImportEnum.IMPORT, cgenetatorConfig, selectedFile);
                        exportJDialog.setVisible(true);
                        if(exportJDialog.processDone){
                            loadTreeConfiguration();
                        }
                        
                    } catch (JsonException | IOException ex) {
                        java.util.logging.Logger.getLogger(MainJFrame.class.getName()).log(Level.SEVERE, null, ex);
                        JOptionPane.showMessageDialog(this,
                                ex.getMessage(),
                                "Import", JOptionPane.ERROR_MESSAGE);
                    } 
                    // load usado el load de manager o algo parecido y pasar el cgenerator config como parametro
                    //       -recuerda hay un ejemplo de arbol con checkbox 
                    // load usado el load de manager o algo parecido y pasar el cgenerator config como parametro
                    //       -recuerda hay un ejemplo de arbol con checkbox
                
            }
        }
    }//GEN-LAST:event_subMenuImportActionPerformed

    private void subMenuExportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_subMenuExportActionPerformed
        exportJDialog.setLocationRelativeTo(CGeneratorContentManager.getInstance().getMainFrame());
        
        File defaultDirectory; 
         if(StringUtils.isNotBlank(CGeneratorContentManager.getInstance().getcGeneratorSettings().getLastExportPath())){
            defaultDirectory = new File(CGeneratorContentManager.getInstance().getcGeneratorSettings().getLastExportPath());
            fileChooser.setCurrentDirectory(defaultDirectory);
        }else{
            defaultDirectory = new File("");
        }
         
        exportJDialog.openAction(ExportImportEnum.EXPORT, CGeneratorContentManager.getInstance().getCgenetatorConfig(), defaultDirectory);
        exportJDialog.setVisible(true);
    }//GEN-LAST:event_subMenuExportActionPerformed

    private void menuOpenWorkSpaceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuOpenWorkSpaceActionPerformed

        try {
            fileChooserWorkSpace.setDialogTitle("Open WorkSpace");
            int result = fileChooserWorkSpace.showOpenDialog(null);
            if (result == JFileChooser.APPROVE_OPTION) {
                loadConfigurations(fileChooserWorkSpace.getSelectedFile());
                setTitle("CGenerator - " + CGeneratorContentManager.getInstance().getCurrentWorkSpace().getName());
                menuProperties.setEnabled(true);
                
                if(!CGeneratorContentManager.getInstance().getcGeneratorSettings().getRecents().contains(fileChooserWorkSpace.getSelectedFile().getAbsolutePath())){
                    addRecent(fileChooserWorkSpace.getSelectedFile());
                }                
            }      
        }catch(HeadlessException | CGeneratorException e){
            JOptionPane.showMessageDialog(this,
            e.getMessage(),
            "Open WorkSpace", JOptionPane.ERROR_MESSAGE);
        }
 
    }//GEN-LAST:event_menuOpenWorkSpaceActionPerformed
 
    private void subMenuNewWorkSpaceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_subMenuNewWorkSpaceActionPerformed
        fileChooserWorkSpace.setDialogTitle("Create Workspace");
        int result = fileChooserWorkSpace.showSaveDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            if(!fileChooserWorkSpace.getSelectedFile().isDirectory()){
                JOptionPane.showMessageDialog(this,
            "The path is not a directory",
            "Create WorkSpace", JOptionPane.WARNING_MESSAGE);
            }else if(fileChooserWorkSpace.getSelectedFile().list().length != 0){
                JOptionPane.showMessageDialog(this,
            "The directory should be empty",
            "Create WorkSpace", JOptionPane.WARNING_MESSAGE);
            }else{
                try {
                    CGeneratorContentManager.getInstance().createWorkSpace(fileChooserWorkSpace.getSelectedFile());
                    loadTreeConfiguration();
                    setTitle("CGenerator - " + CGeneratorContentManager.getInstance().getCurrentWorkSpace().getName());
                    menuProperties.setEnabled(true);
                    scrollPaneMain.setViewportView(null);
                    
                    if(!CGeneratorContentManager.getInstance().getcGeneratorSettings().getRecents().contains(fileChooserWorkSpace.getSelectedFile().getAbsolutePath())){
                        addRecent(fileChooserWorkSpace.getSelectedFile());
                    }
                    
                } catch (CGeneratorException ex) {
                    java.util.logging.Logger.getLogger(MainJFrame.class.getName()).log(Level.SEVERE, null, ex);

                    JOptionPane.showMessageDialog(null,
                            ex.getMessage(),
                            "Create WorkSpace", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }//GEN-LAST:event_subMenuNewWorkSpaceActionPerformed

    private void menuPropertiesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuPropertiesActionPerformed
        propertiesJDialog.init();
        propertiesJDialog.setVisible(true);
    }//GEN-LAST:event_menuPropertiesActionPerformed

    private void subMenuImportOpenApiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_subMenuImportOpenApiActionPerformed
        
        if(StringUtils.isNotBlank(CGeneratorContentManager.getInstance().getcGeneratorSettings().getLastImportPath())){
            File defaultDirectory = new File(CGeneratorContentManager.getInstance().getcGeneratorSettings().getLastImportPath());
            fileChooser.setCurrentDirectory(defaultDirectory);
        }
        
        int result = fileChooser.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            if(selectedFile.isFile() && selectedFile.exists()){
                try {
                    Map cgOpenAPI = OpenApiReader.readFile(selectedFile);
                    
                    String title = "Default title";
                    Map mapInfo =(Map) cgOpenAPI.get("info");
                    if(mapInfo == null){
                       mapInfo = new HashMap<>();
                       mapInfo.put("title", title);
                       cgOpenAPI.put("info", mapInfo);
                    }else if(!mapInfo.containsKey("title")){
                        mapInfo.put("title", "Default title");
                    }else{
                        title = mapInfo.get("title").toString();
                    }
                    OpenApiImported openApiImported = new OpenApiImported();
                    openApiImported.setId(Util.generateId());
                    openApiImported.setName(title);
                    openApiImported.setData(cgOpenAPI);
                    CGeneratorContentManager.getInstance().getCgenetatorConfig().getOpenAPIs().add(openApiImported);
                    CGeneratorContentManager.getInstance().updateConfigFile();
                    
                    loadTreeConfiguration();
                    
                    CGeneratorContentManager.getInstance().getcGeneratorSettings().setLastImportPath(selectedFile.getParent());
                    CGeneratorContentManager.getInstance().saveSetting();
                        
                } catch (Exception ex) {
                    java.util.logging.Logger.getLogger(MainJFrame.class.getName()).log(Level.SEVERE, null, ex);
                    JOptionPane.showMessageDialog(null,
                            ex.getMessage(),
                            "Import OpenAPI", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }//GEN-LAST:event_subMenuImportOpenApiActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
     /**   try {
            
      
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
  
              //  UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
             
              
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(NewJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(NewJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(NewJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(NewJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        * **/
        //</editor-fold>

        try {
            UIManager.setLookAndFeel( new FlatLightLaf() );
        } catch( Exception ex ) {
            System.err.println( "Failed to initialize LaF" );
        }

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                MainJFrame newJFrame = new MainJFrame();
                CGeneratorContentManager.getInstance().setMainFrame(newJFrame);
                newJFrame.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JPopupMenu.Separator jSeparator3;
    private javax.swing.JPopupMenu.Separator jSeparator4;
    private javax.swing.JPopupMenu.Separator jSeparator5;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JMenu menuFile;
    private javax.swing.JMenuItem menuOpenWorkSpace;
    private javax.swing.JMenuItem menuProperties;
    private javax.swing.JMenu menuSwitchWorkspace;
    private javax.swing.JScrollPane scrollPaneMain;
    private javax.swing.JMenuItem subMenuExport;
    private javax.swing.JMenuItem subMenuImport;
    private javax.swing.JMenuItem subMenuImportOpenApi;
    private javax.swing.JMenuItem subMenuNewWorkSpace;
    private javax.swing.JTree treeProject;
    // End of variables declaration//GEN-END:variables

    
    private void loadInitConfiguration() {
        
        CGTreeNode treeNodeRoot      = new CGTreeNode("Projects", TreeNodeTypes.ROOT_TYPE.name() );
        CGTreeNode treeNodeDatabase  = new CGTreeNode("DataBase Connectios", TreeNodeTypes.DATABASE_SEPARATOR_TYPE.name());
        CGTreeNode treeNodeDataType  = new CGTreeNode("DataType Mapping", TreeNodeTypes.DATATYPE_SEPARATOR_TYPE.name());
        CGTreeNode treeNodeArtifacts = new CGTreeNode("Artifacts",TreeNodeTypes.ARTIFACT_SEPARATOR_TYPE.name());
        CGTreeNode treeNodeGlobalDataConfig = new CGTreeNode("Global Data", TreeNodeTypes.GLOBAL_DATA_SEPARATOR_TYPE.name());
        CGTreeNodeBase treeNodeOpenAPI = new CGTreeNode("OpenAPI", TreeNodeTypes.OPEN_API_SEPARATOR_TYPE.name()); //new CGTreeNode("Run Configuration", TreeNodeTypes.RUN_CONFIGURATION_SEPARATOR_TYPE.name());
        CGTreeNode treeNodeRunConfig = new CGTreeNode("Run Configuration", TreeNodeTypes.RUN_CONFIGURATION_SEPARATOR_TYPE.name());
        
        treeNodeRoot.add(treeNodeDatabase);
        treeNodeRoot.add(treeNodeDataType); 
        treeNodeRoot.add(treeNodeArtifacts);
         treeNodeRoot.add(treeNodeGlobalDataConfig);
          treeNodeRoot.add(treeNodeOpenAPI);
        treeNodeRoot.add(treeNodeRunConfig);
       
        defaultTreeModel = new DefaultTreeModel(treeNodeRoot);
        treeProject.setModel(defaultTreeModel);   
        CGeneratorContentManager.getInstance().setTreModel(defaultTreeModel);
        
        treeProject.setEnabled(CGeneratorContentManager.getInstance().isActive());
        subMenuExport.setEnabled(CGeneratorContentManager.getInstance().isActive());
        subMenuImport.setEnabled(CGeneratorContentManager.getInstance().isActive());
        subMenuImportOpenApi.setEnabled(CGeneratorContentManager.getInstance().isActive());
                
        CGeneratorContentManager.getInstance().loadSetting();
        JMenuItem JMenuItem;
        for (String recent : CGeneratorContentManager.getInstance().getcGeneratorSettings().getRecents()) {
            JMenuItem = new JMenuItem(recent);
            JMenuItem.addActionListener((ActionEvent e) -> {
                try {
                    loadConfigurations(new File(recent));
                } catch (CGeneratorException ex) {
                    java.util.logging.Logger.getLogger(MainJFrame.class.getName()).log(Level.SEVERE, null, ex);

                    JOptionPane.showMessageDialog(null,
                            ex.getMessage(),
                            "Open WorkSpace", JOptionPane.ERROR_MESSAGE);
                }
            });
            menuSwitchWorkspace.add(JMenuItem);
        }
    }
    

    /**
     * loadTreeConfiguration
     */
    private void loadTreeConfiguration() {
        defaultTreeModel = new DefaultTreeModel(MainTreeCreator.getInstance().create(CGeneratorContentManager.getInstance().getCgenetatorConfig()));
        treeProject.setModel(defaultTreeModel);
        CGeneratorContentManager.getInstance().setTreModel(defaultTreeModel);
        treeProject.setEnabled(CGeneratorContentManager.getInstance().isActive());
        subMenuExport.setEnabled(CGeneratorContentManager.getInstance().isActive());
        subMenuImport.setEnabled(CGeneratorContentManager.getInstance().isActive());
        subMenuImportOpenApi.setEnabled(CGeneratorContentManager.getInstance().isActive());
    }

    /**
     * 
     * @param cgenetatorConfig 
     */
    private void loadConfigurations(File workSpaceFolder ) throws CGeneratorException{
        CGeneratorContentManager.getInstance().loadConfigFile(workSpaceFolder);
        loadTreeConfiguration();
        scrollPaneMain.setViewportView(null);
        setTitle("CGenerator - " + CGeneratorContentManager.getInstance().getCurrentWorkSpace().getName());
        menuProperties.setEnabled(true);
    }

    private void addRecent(File selectedFile) {
        CGeneratorContentManager.getInstance().getcGeneratorSettings().getRecents().add(selectedFile.getAbsolutePath());
        CGeneratorContentManager.getInstance().saveSetting();
        JMenuItem JMenuItem = new JMenuItem(selectedFile.getAbsolutePath());
        menuSwitchWorkspace.add(JMenuItem);
        JMenuItem.addActionListener((ActionEvent e) -> {
            try {
                loadConfigurations(new File(selectedFile.getAbsolutePath()));
            } catch (CGeneratorException ex) {
                java.util.logging.Logger.getLogger(MainJFrame.class.getName()).log(Level.SEVERE, null, ex);

                JOptionPane.showMessageDialog(null,
                        ex.getMessage(),
                        "Open Workspace", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    
}
