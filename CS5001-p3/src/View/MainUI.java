package View;

import Controller.DrawingPanel;

import javax.swing.*;
import java.awt.*;

/**
 * Main UI class for the vector drawing application. This class is responsible for
 * creating and displaying the main window of the application.
 */
public class MainUI extends JFrame {

    public DrawingPanel drawingpanel;
    public NetworkService networkService;

    /**
     * Constructor for MainUI. Initializes the frame with title, size, and default close operation.
     */
    public MainUI() {

        setTitle("Vector Drawing Application");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    /**
     * Initializes the user interface components and layout.
     * Sets up the drawing panel and control buttons for drawing operations.
     *
     * @param networkService The NetworkService instance for handling network operations.
     */
    public void initUI(NetworkService networkService) {

        JPanel panel = new JPanel(new GridLayout(0, 7));
        this.add(panel, BorderLayout.NORTH);

        drawingpanel = new DrawingPanel(networkService);
        this.add(drawingpanel, BorderLayout.CENTER);


        String[] buttons = {"LINE", "RECTANGLE", "ELLIPSE", "TRIANGLE"};
        for (String button : buttons) {
            JButton btn = new JButton(button);
            panel.add(btn);
            btn.addActionListener(e -> {
                drawingpanel.setCurrentShapeType(button);
            });
        }
        JButton undoButton = new JButton("Undo");
        JButton redoButton = new JButton("Redo");

        panel.add(undoButton);
        panel.add(redoButton);

        undoButton.addActionListener(e -> {
            drawingpanel.undo();
        });

        redoButton.addActionListener(e -> {
            drawingpanel.redo();
        });

        JButton colorChooserButton = new JButton("Color");
        colorChooserButton.addActionListener(e -> {
            Color chosenColor = JColorChooser.showDialog(this, "Choose a color", null);
            if (chosenColor != null && !drawingpanel.isFillMode()) {
                drawingpanel.setDrawingColor(chosenColor);
            } else if (chosenColor != null && drawingpanel.isFillMode()) {
                drawingpanel.setFillingColor(chosenColor);
            }
        });
        panel.add(colorChooserButton);

        JCheckBox moveCheckBox = new JCheckBox("Move");
        JCheckBox fillCheckBox = new JCheckBox("Fill");
        JCheckBox rotateCheckBox = new JCheckBox("Rotate");
        JCheckBox reshapeCheckBox = new JCheckBox("Reshape");

        moveCheckBox.addActionListener(e -> {
            if (moveCheckBox.isSelected()) {
                fillCheckBox.setSelected(false);
                rotateCheckBox.setSelected(false);
                reshapeCheckBox.setSelected(false);
                drawingpanel.setMoveMode(true);
                drawingpanel.setFillMode(false);
                drawingpanel.setRotateMode(false);
                drawingpanel.setReshapeMode(false);
            } else {
                drawingpanel.setMoveMode(false);
            }
        });
        panel.add(moveCheckBox);

        fillCheckBox.addActionListener(e -> {
            if (fillCheckBox.isSelected()) {
                rotateCheckBox.setSelected(false);
                reshapeCheckBox.setSelected(false);
                moveCheckBox.setSelected(false);
                drawingpanel.setFillMode(true);
                drawingpanel.setMoveMode(false);
                drawingpanel.setRotateMode(false);
                drawingpanel.setReshapeMode(false);
            } else {
                drawingpanel.setFillMode(false);
            }
        });
        panel.add(fillCheckBox);

        rotateCheckBox.addActionListener(e -> {
            if (rotateCheckBox.isSelected()) {
                fillCheckBox.setSelected(false);
                reshapeCheckBox.setSelected(false);
                moveCheckBox.setSelected(false);
                drawingpanel.setRotateMode(true);
                drawingpanel.setMoveMode(false);
                drawingpanel.setFillMode(false);
                drawingpanel.setReshapeMode(false);
            } else {
                drawingpanel.setRotateMode(false);
            }
        });
        panel.add(rotateCheckBox);

        reshapeCheckBox.addActionListener(e -> {
            if (reshapeCheckBox.isSelected()) {
                fillCheckBox.setSelected(false);
                rotateCheckBox.setSelected(false);
                moveCheckBox.setSelected(false);
                drawingpanel.setReshapeMode(true);
                drawingpanel.setMoveMode(false);
                drawingpanel.setFillMode(false);
                drawingpanel.setRotateMode(false);
            } else {
                drawingpanel.setReshapeMode(false);
            }
        });
        panel.add(reshapeCheckBox);

        JSpinner strokeWidthSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));
        strokeWidthSpinner.addChangeListener(e -> {
            drawingpanel.setStrokeWidth((int) strokeWidthSpinner.getValue());
        });

        panel.add(new JLabel("lineWidth"));
        panel.add(strokeWidthSpinner);

        JButton saveFrame = new JButton("Save");

        panel.add(saveFrame);

        saveFrame.addActionListener(e -> {
            drawingpanel.saveFrame(this);
        });
    }

}
