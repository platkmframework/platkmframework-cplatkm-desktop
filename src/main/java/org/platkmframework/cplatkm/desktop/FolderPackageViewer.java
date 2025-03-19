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
package org.platkmframework.cplatkm.desktop;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

public class FolderPackageViewer {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Folder Package Viewer");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 600);

            JPanel panel = new JPanel(new BorderLayout());

            // Modelo de tabla
            DefaultTableModel tableModel = new DefaultTableModel(new Object[]{"Ruta", "Nombre del Paquete"}, 0);
            JTable table = new JTable(tableModel);
            JScrollPane scrollPane = new JScrollPane(table);

            // Panel para seleccionar directorio
            JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            JButton selectFolderButton = new JButton("Seleccionar Carpeta");
            topPanel.add(selectFolderButton);

            // Botón para crear nuevo paquete
            JButton createPackageButton = new JButton("Crear Nuevo Paquete");
            createPackageButton.setEnabled(false);
            topPanel.add(createPackageButton);

            panel.add(topPanel, BorderLayout.NORTH);
            panel.add(scrollPane, BorderLayout.CENTER);

            // Acción para seleccionar directorio
            selectFolderButton.addActionListener(e -> {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                int result = fileChooser.showOpenDialog(frame);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFolder = fileChooser.getSelectedFile();
                    tableModel.setRowCount(0); // Limpiar tabla

                    // Listar todas las subcarpetas recursivamente a partir de la carpeta seleccionada
                    listSubfolders(selectedFolder, tableModel, selectedFolder.getAbsolutePath());

                    createPackageButton.setEnabled(true);
                }
            });

            // Acción para crear nuevo paquete
            createPackageButton.addActionListener(e -> {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    String selectedPath = (String) tableModel.getValueAt(selectedRow, 0);
                    String packageName = JOptionPane.showInputDialog(frame, "Ingrese el nombre del nuevo paquete:", "Nuevo Paquete", JOptionPane.PLAIN_MESSAGE);

                    if (packageName != null && !packageName.trim().isEmpty()) {
                        File newPackage = new File(selectedPath + File.separator + packageName);
                        if (newPackage.mkdir()) {
                            JOptionPane.showMessageDialog(frame, "Paquete creado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                            tableModel.addRow(new Object[]{newPackage.getAbsolutePath().replace(selectedPath, ""), packageName});
                        } else {
                            JOptionPane.showMessageDialog(frame, "No se pudo crear el paquete.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(frame, "Seleccione un paquete primero.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                }
            });

            // Acción para doble clic en una fila
            table.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (e.getClickCount() == 2) {
                        int selectedRow = table.getSelectedRow();
                        if (selectedRow != -1) {
                            String relativePath = (String) tableModel.getValueAt(selectedRow, 0);
                            String packageName = (String) tableModel.getValueAt(selectedRow, 1);
                            JOptionPane.showMessageDialog(frame, "Seleccionaste: " + packageName + "\nRuta relativa: " + relativePath, "Información de la Carpeta", JOptionPane.INFORMATION_MESSAGE);
                        }
                    }
                }
            });

            frame.add(panel);
            frame.setVisible(true);
        });
    }

    private static void listSubfolders(File folder, DefaultTableModel tableModel, String basePath) {
        File[] files = folder.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    String relativePath = file.getAbsolutePath().replace(basePath + File.separator, "");
                    tableModel.addRow(new Object[]{relativePath, file.getName()});
                    listSubfolders(file, tableModel, basePath); // Llamada recursiva
                }
            }
        }
    }
}
