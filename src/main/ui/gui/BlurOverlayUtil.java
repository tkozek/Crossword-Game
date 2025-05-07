package ui.gui;

import javax.swing.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;

public class BlurOverlayUtil {

    private static final Font MESSAGE_FONT = new Font("SansSerif", Font.BOLD, 22);
    private static final int INFO_TABS_WIDTH = 175;
    private static final int MOVE_SUMMARY_PADDING = 25;

    public static void showBlurOverlay(JFrame frame, String message) {
        try {
            JRootPane root = frame.getRootPane();
            Point contentLocation = root.getLocationOnScreen();
            Dimension contentSize = root.getSize();
            contentSize.setSize(contentSize.width + 8, contentSize.height +8);
            Rectangle contentBounds = new Rectangle(contentLocation, contentSize);               

            BufferedImage screenshot = new Robot().createScreenCapture(contentBounds);
            BufferedImage blurred = blurImage(screenshot, 6); // Adjust blur level here

            // Create glass pane
            JPanel glass = new JPanel() {
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    g.drawImage(blurred, 0, 0, getWidth(), getHeight(), null);
                    
                    // Optional: draw a dark overlay on top of blur
                    // g.setColor(new Color(0, 0, 0, 80));
                    // g.fillRect(0, 0, getWidth(), getHeight());
                }
            };

            glass.setLayout(new GridBagLayout());
            glass.setOpaque(false);
            String toDisplay = message + "\n\nClick anywhere to continue";
            JTextPane textPane = new JTextPane();
            textPane.setText(toDisplay);
            textPane.setFont(MESSAGE_FONT);
            textPane.setOpaque(false);
           // int height = getTextboxHeight(glass, toDisplay, INFO_TABS_WIDTH + 100, MESSAGE_FONT);
            // textArea.setLineWrap(true);
            // textArea.setWrapStyleWord(true);
            
            textPane.setEditable(false);
            textPane.setFocusable(false); 
            StyledDocument documentStyle = textPane.getStyledDocument();
            SimpleAttributeSet centerAttribute = new SimpleAttributeSet();
            StyleConstants.setAlignment(centerAttribute, StyleConstants.ALIGN_CENTER);
            documentStyle.setParagraphAttributes(0, documentStyle.getLength(), centerAttribute, false);
            
            textPane.setPreferredSize(new Dimension(frame.getWidth() * 2 / 3, frame.getHeight() / 2));
            textPane.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
        
            // glass.add(label);
            glass.add(textPane);

            glass.addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                        hideBlurOverlay(frame);
                    }
                }
            });
            
            glass.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    hideBlurOverlay(frame);
                }
            });

            glass.setFocusable(true);
            frame.setGlassPane(glass);
            glass.setVisible(true);
            glass.requestFocusInWindow();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // // EFFECTS: returns textbox height required based on length of input text and font used
    // private static int getTextboxHeight(JComponent component, String text, int containerWidth, Font font) {
    //     FontMetrics metric = component.getFontMetrics(font);
    //     int width = metric.charWidth('A');
    //     int h = metric.getHeight();
    //     double textBoxHeight = (text.length() * width * h / containerWidth);
    //     return Math.max(h - 5, (int) textBoxHeight);
    // }

    public static void hideBlurOverlay(JFrame frame) {
        Component glass = frame.getGlassPane();
        for (KeyListener kl : glass.getKeyListeners()) {
            glass.removeKeyListener(kl);
        }
        for (MouseListener ml : glass.getMouseListeners()) {
            glass.removeMouseListener(ml);
        }
        glass.setVisible(false);
    }

    private static BufferedImage blurImage(BufferedImage image, int radius) {
        int size = radius * 2 + 1;
        float[] data = new float[size * size];
        float value = 1.0f / (size * size);

        for (int i = 0; i < data.length; i++) {
            data[i] = value;
        }

        Kernel kernel = new Kernel(size, size, data);
        ConvolveOp op = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);
        return op.filter(image, null);
    }
}
