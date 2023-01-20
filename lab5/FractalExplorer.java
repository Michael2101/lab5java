package lab5;

import lab4.*;


import java.awt.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.event.*;
import java.awt.geom.Rectangle2D;
import java.io.IOException;


public class FractalExplorer {


    private final int displaySize;

    private JImageDisplay displayImage;
    private FractalGenerator fractalGenerator;

    private final Rectangle2D.Double complexPlaneRange;

    private JComboBox<FractalGenerator> fractalSelectorComboBox;

    public static void main(String[] args) {
        FractalExplorer fractalExplorer = new FractalExplorer(800);
        fractalExplorer.createAndShowGUI();
        fractalExplorer.drawFractal();
    }

    private FractalExplorer(int sizeDisplay) {
        this.displaySize = sizeDisplay;
        this.fractalGenerator = new Mandelbrot();
        this.complexPlaneRange = new Rectangle2D.Double(0, 0, 0, 0);
        fractalGenerator.getInitialRange(this.complexPlaneRange);
    }

    public void createAndShowGUI() {
     
        displayImage = new JImageDisplay(displaySize, displaySize);
        displayImage.addMouseListener(new EmphasizeActionListener());

        JButton buttonReset = new JButton("Reset display");
        buttonReset.addActionListener(new ResetActionListener());

        JButton buttonSave = new JButton("Save fractal");
        buttonSave.addActionListener(new SaveActionListener());

        JLabel label = new JLabel("Fractal:");
        fractalSelectorComboBox = new JComboBox<>();
        fractalSelectorComboBox.addItem(new Mandelbrot());
        fractalSelectorComboBox.addItem(new Tricorn());
        fractalSelectorComboBox.addItem(new BurningShip());
        fractalSelectorComboBox.addActionListener(new ComboBoxSelectItemActionListener());

        JPanel jPanelSelector = new JPanel();
        JPanel jPanelButtons = new JPanel();
        jPanelSelector.add(label, BorderLayout.CENTER);
        jPanelSelector.add(fractalSelectorComboBox, BorderLayout.CENTER);
        jPanelButtons.add(buttonReset, BorderLayout.CENTER);
        jPanelButtons.add(buttonSave, BorderLayout.CENTER);

        JFrame frame = new JFrame("fractal renderer");
        frame.setLayout(new BorderLayout());
        frame.add(displayImage, BorderLayout.CENTER);
        frame.add(jPanelSelector, BorderLayout.NORTH);
        frame.add(jPanelButtons, BorderLayout.SOUTH);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.setResizable(false);
    }


    private void drawFractal() {
        for (int x = 0; x < displaySize; x++) {
            for (int y = 0; y < displaySize; y++) {
                int count = fractalGenerator.numIterations(
                        FractalGenerator.getCoord(
                                complexPlaneRange.x,
                                complexPlaneRange.x + complexPlaneRange.width,
                                displaySize,
                                x
                        ),
                        FractalGenerator.getCoord(
                                complexPlaneRange.y,
                                complexPlaneRange.y + complexPlaneRange.width,
                                displaySize,
                                y
                        )
                );
                int rgbColor;
                if (count == -1) {
                    rgbColor = 0;
                } else {
                    float hue = 0.7f + (float) count / 200f;
                    rgbColor = Color.HSBtoRGB(hue, 1f, 1f);
                }
                displayImage.drawPixel(x, y, rgbColor);
            }
        }
        displayImage.repaint();
    }


    private class ResetActionListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            displayImage.clearImage();
            fractalGenerator.getInitialRange(complexPlaneRange);
            drawFractal();
        }
    }

   
    // Кнопка сохранения фрактала
    private class SaveActionListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            JFileChooser fileChooser = new JFileChooser();
            FileNameExtensionFilter fileFilter = new FileNameExtensionFilter("PNG Images", "png");
            fileChooser.setFileFilter(fileFilter);
            fileChooser.setAcceptAllFileFilterUsed(false);
            int t = fileChooser.showSaveDialog(displayImage);
            if (t == JFileChooser.APPROVE_OPTION) {
                try {
                    ImageIO.write(displayImage.picture, "png", fileChooser.getSelectedFile());
                } catch (Exception ee) {
                    JOptionPane.showMessageDialog(
                            displayImage,
                            ee.getMessage(),
                            "Error saving fractal",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        }
    }

    // Выбор фракталов
    private class ComboBoxSelectItemActionListener implements ActionListener {
        
        public void actionPerformed(ActionEvent e) {
            fractalGenerator = (FractalGenerator) fractalSelectorComboBox.getSelectedItem();
            fractalGenerator.getInitialRange(complexPlaneRange);
            drawFractal();
        }
    }

    private class EmphasizeActionListener extends MouseAdapter implements MouseListener {
        
        public void mouseClicked(MouseEvent e) {
            double x = FractalGenerator.getCoord(
                    complexPlaneRange.x,
                    complexPlaneRange.x + complexPlaneRange.width, displaySize, e.getX()
            );
            double y = FractalGenerator.getCoord(
                    complexPlaneRange.y,
                    complexPlaneRange.y + complexPlaneRange.width,
                    displaySize,
                    e.getY()
            );
            fractalGenerator.recenterAndZoomRange(complexPlaneRange, x, y, 0.5);
            drawFractal();
        }
    }
}