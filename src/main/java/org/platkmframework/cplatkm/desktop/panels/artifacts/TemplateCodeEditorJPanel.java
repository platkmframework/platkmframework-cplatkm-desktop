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
package org.platkmframework.cplatkm.desktop.panels.artifacts;

import java.awt.Color;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JColorChooser;
import javax.swing.JOptionPane;  
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import javax.swing.undo.UndoManager;
import org.platkmframework.cplatkm.desktop.core.CGeneratorContentManager;
import org.platkmframework.cplatkm.processor.data.Template;
import org.platkmframework.cplatkm.processor.exception.CGeneratorException;

/**
 *
 * @author Eduardo Iglesias
 */
public class TemplateCodeEditorJPanel extends javax.swing.JPanel {

    private Template template;
    private String artifactFolder;
    /**
     * Creates new form templateCodeEditorJPanel
     */
    public TemplateCodeEditorJPanel() {
        initComponents();
        
        UndoManager undoManager = new UndoManager();
        StyledDocument styledDoc = textPane.getStyledDocument();
        styledDoc.addUndoableEditListener(undoManager);

        SimpleAttributeSet keywordStyle = new SimpleAttributeSet();
        StyleConstants.setForeground(keywordStyle, Color.BLUE);
        
        String keywords = "\\b(set|foreach|if|end|else|elseif)\\b";
        Pattern keywordPattern = Pattern.compile(keywords, Pattern.CASE_INSENSITIVE);
            
        textPane.getDocument().addDocumentListener(new DocumentListener() {
                private void applyHighlighting(DocumentEvent e) {
                    SwingUtilities.invokeLater(() -> {
                        try {
                            int start = e.getOffset();
                            int length = e.getLength();
                            String text = styledDoc.getText(0, styledDoc.getLength());
                            
                            // Obtener el rango afectado
                            int startWord = Math.max(0, text.lastIndexOf(" ", start));
                            int endWord = Math.min(text.length(), text.indexOf(" ", start + length));
                            if (endWord == -1) endWord = text.length();

                            Matcher matcher = keywordPattern.matcher(text.substring(startWord, endWord));

                            // Restablecer estilos en el rango afectado
                            styledDoc.setCharacterAttributes(startWord, endWord - startWord, new SimpleAttributeSet(), true);

                            while (matcher.find()) {
                                int keywordStart = startWord + matcher.start();
                                int keywordEnd = startWord + matcher.end();
                                styledDoc.setCharacterAttributes(keywordStart, keywordEnd - keywordStart, keywordStyle, true);
                            }
                        } catch (BadLocationException ex) {
                            ex.printStackTrace();
                        }
                    });
                }

                @Override
                public void insertUpdate(DocumentEvent e) {
                    applyHighlighting(e);
                }

                @Override
                public void removeUpdate(DocumentEvent e) {
                    applyHighlighting(e);
                }

                @Override
                public void changedUpdate(DocumentEvent e) {
                }
            });
        
        
        increaseFontSizeButton.addActionListener(e -> {
                Font currentFont = textPane.getFont();
                textPane.setFont(new Font(currentFont.getName(), currentFont.getStyle(), currentFont.getSize() + 2));
            });

        decreaseFontSizeButton.addActionListener(e -> {
            Font currentFont = textPane.getFont();
            textPane.setFont(new Font(currentFont.getName(), currentFont.getStyle(), Math.max(currentFont.getSize() - 2, 8)));
        });
        
        colorButton.addActionListener(e -> {
            Color selectedColor = JColorChooser.showDialog(CGeneratorContentManager.getInstance().getMainFrame(), "Elige un color", Color.BLACK);
            if (selectedColor != null) {
                textPane.setForeground(selectedColor);
            }
        });
        
        undoButton.addActionListener(e -> {
               if (undoManager.canUndo()) {
                   undoManager.undo();
               }
           });

        redoButton.addActionListener(e -> {
            if (undoManager.canRedo()) {
                undoManager.redo();
            }
        });
        
        String[] fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        fontComboBox.removeAllItems();
        if(fonts != null && fonts.length>0)
            for (String font : fonts) {
                fontComboBox.addItem(font);
        }
            
        fontComboBox.addActionListener(e -> {
            String selectedFont = (String) fontComboBox.getSelectedItem();
            Font currentFont = textPane.getFont();
            textPane.setFont(new Font(selectedFont, currentFont.getStyle(), currentFont.getSize()));
        });
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jToolBar1 = new javax.swing.JToolBar();
        btnGuardar = new javax.swing.JButton();
        jSeparator3 = new javax.swing.JToolBar.Separator();
        undoButton = new javax.swing.JButton();
        redoButton = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JToolBar.Separator();
        decreaseFontSizeButton = new javax.swing.JButton();
        increaseFontSizeButton = new javax.swing.JButton();
        colorButton = new javax.swing.JButton();
        fontComboBox = new javax.swing.JComboBox<>();
        jScrollPane2 = new javax.swing.JScrollPane();
        textPane = new javax.swing.JTextPane();

        jToolBar1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        btnGuardar.setText("Save");
        btnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarActionPerformed(evt);
            }
        });
        jToolBar1.add(btnGuardar);
        jToolBar1.add(jSeparator3);

        undoButton.setText("Undo");
        undoButton.setFocusable(false);
        undoButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        undoButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(undoButton);

        redoButton.setText("Redo");
        redoButton.setFocusable(false);
        redoButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        redoButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(redoButton);
        jToolBar1.add(jSeparator2);

        decreaseFontSizeButton.setText("A-");
        decreaseFontSizeButton.setFocusable(false);
        decreaseFontSizeButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        decreaseFontSizeButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(decreaseFontSizeButton);

        increaseFontSizeButton.setText("A+");
        increaseFontSizeButton.setFocusable(false);
        increaseFontSizeButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        increaseFontSizeButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(increaseFontSizeButton);

        colorButton.setText("Color");
        colorButton.setFocusable(false);
        colorButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        colorButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(colorButton);

        jToolBar1.add(fontComboBox);

        jScrollPane2.setViewportView(textPane);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jToolBar1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 562, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane2)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 539, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
        try {
            CGeneratorContentManager.getInstance().saveTemplateContent(artifactFolder, this.template.getTemplatename(), textPane.getText());
        } catch (CGeneratorException ex) {
            Logger.getLogger(TemplateCodeEditorJPanel.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this,
                ex.getMessage(),
                "Editor", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_btnGuardarActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton colorButton;
    private javax.swing.JButton decreaseFontSizeButton;
    private javax.swing.JComboBox<String> fontComboBox;
    private javax.swing.JButton increaseFontSizeButton;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JToolBar.Separator jSeparator2;
    private javax.swing.JToolBar.Separator jSeparator3;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JButton redoButton;
    private javax.swing.JTextPane textPane;
    private javax.swing.JButton undoButton;
    // End of variables declaration//GEN-END:variables

    public void setTemplate(String artifactFolder, Template template) throws CGeneratorException {
        this.template  = template;
        this.artifactFolder = artifactFolder;
        textPane.setText(CGeneratorContentManager.getInstance().getTemplateContent(artifactFolder, this.template.getTemplatename()));
    }
}
