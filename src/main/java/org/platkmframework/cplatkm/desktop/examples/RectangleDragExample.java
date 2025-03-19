/*
 * Copyright 2025 eigta.
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
package org.platkmframework.cplatkm.desktop.examples;

/**
 *
 * @author eigta
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class RectangleDragExample extends JFrame {

    private Rectangle rectangle; // The rectangle shape
    private Point offset;        // Offset between the mouse point and the top-left corner of the rectangle

    public RectangleDragExample() {
        super("Drag Rectangle Example");

        // Initialize rectangle with starting position and size
        rectangle = new Rectangle(50, 50, 100, 100);
        offset = new Point();

        // Create a custom panel to draw the rectangle
        MyPanel panel = new MyPanel();
        panel.setBackground(Color.WHITE);

        // Add mouse listener to detect press and release events
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                // Check if the mouse press is inside the rectangle
                if (rectangle.contains(e.getPoint())) {
                    // Calculate the offset between the mouse location and rectangle's top-left corner
                    offset.x = e.getX() - rectangle.x;
                    offset.y = e.getY() - rectangle.y;
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                // Reset the offset when the mouse is released
                offset.x = 0;
                offset.y = 0;
            }
        });

        // Add mouse motion listener to detect dragging
        panel.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                // Only update rectangle's position if dragging started inside the rectangle
                if (offset.x != 0 || offset.y != 0) {
                    rectangle.x = e.getX() - offset.x;
                    rectangle.y = e.getY() - offset.y;
                    panel.repaint(); // Redraw the panel with the rectangle in new position
                }
            }
        });

        add(panel);
        setSize(400, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the window
    }

    // Custom JPanel to draw the rectangle
    class MyPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.setColor(Color.RED);
            g.fillRect(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new RectangleDragExample().setVisible(true);
        });
    }
}
