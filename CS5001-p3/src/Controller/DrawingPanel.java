package controller;

import model.Vector;
import view.JSONUtils;
import view.NetworkService;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * The DrawingPanel class provides the main drawing area of the application.
 * It handles user interactions for drawing and editing shapes, managing undo/redo operations,
 * and interacting with the network service for saving and updating drawings.
 */
public class DrawingPanel extends JPanel {

    public Point startPoint;
    public Point endPoint;
    public Point selectionStart;
    public Point selectionEnd;
    public Point centrePoint = new Point();
    public String currentShapeType = "LINE";
    public Color drawingColor = null;
    public Color fillingColor = null;
    public int strokeWidth;
    public HashMap<String, Vector> vectorMap = new LinkedHashMap<>();
    public Stack<Vector> undoStack = new Stack<>();
    public Stack<Vector> redoStack = new Stack<>();
    public boolean shiftPressed = false;
    public boolean isMoveMode = false;
    public boolean isFillMode = false;
    public boolean isRotateMode = false;
    public boolean isReshapeMode = false;
    public Vector vec = null;
    public Vector selectVec = null;
    public boolean firstEdit = true;
    public boolean isRotatingShow = true;
    public boolean isReshapeShow = true;
    public Rectangle selectionRect;
    public double rotationAngle = 0;
    public double reshapeSize = 1.0;
    public NetworkService networkService;


    /**
     * Constructor that initializes the DrawingPanel with a given network service.
     * Sets up mouse event handling for drawing and editing operations.
     *
     * @param networkService The network service to be used for saving and updating drawings.
     */
    public DrawingPanel(NetworkService networkService) {

        this.networkService = networkService;

        MouseAdapter mouseAdapter = new MouseAdapter() {

            /**
             * Handles mouse press events. Sets the starting point for drawing or selecting a vector.
             * Determines if shift is pressed for constrained drawing.
             *
             * @param e The MouseEvent that triggered the method.
             */
            @Override
            public void mousePressed(MouseEvent e) {

                if (isMoveMode || isFillMode || isRotateMode || isReshapeMode) {
                    selectionStart = e.getPoint();
                    selectionEnd = null;
                    selectionRect = null;
                } else {
                    startPoint = e.getPoint();
                    setShiftPressed((e.getModifiersEx() & InputEvent.SHIFT_DOWN_MASK) == InputEvent.SHIFT_DOWN_MASK);
                }
            }

            /**
             * Handles mouse drag events. If in rotate or reshape mode, updates the rotation angle or
             * size of the selected vector. Triggers a repaint to show changes in real-time.
             *
             * @param e The MouseEvent that triggered the method.
             */
            @Override
            public void mouseDragged(MouseEvent e) {

                if (isRotateMode) {

                   isRotatingShow = true;

                   selectionRect = new Rectangle(selectionStart.x, selectionStart.y, 1, 1);
                   selectVec = selectVectorsInRect(selectionRect);

                    if (selectVec != null) {
                        getRotationCenter(selectVec);
                        if (isRotateMode && selectionStart != null) {
                            Point currentPoint = e.getPoint();
                            rotationAngle = calculateRotationAngle(centrePoint, currentPoint);
                            repaint();
                        }
                    }
                } else if (isReshapeMode) {

                    isReshapeShow = true;

                    selectionRect = new Rectangle(selectionStart.x, selectionStart.y, 1, 1);
                    selectVec = selectVectorsInRect(selectionRect);

                    if (selectVec != null) {
                        getRotationCenter(selectVec);
                        if (isReshapeMode && selectionStart != null) {
                            Point currentPoint = e.getPoint();
                            reshapeSize = calculateReshapeSize(centrePoint, currentPoint, selectionStart);
                            repaint();
                        }
                    }
                }
            }

            /**
             * Handles mouse release events. Completes the drawing or editing of a vector and updates
             * the vector map. In edit modes, applies changes and updates undo/redo stacks. Triggers
             * a repaint to reflect changes.
             *
             * @param e The MouseEvent that triggered the method.
             */
            @Override
            public void mouseReleased(MouseEvent e) {

                endPoint = e.getPoint();
                selectionEnd = e.getPoint();

                if ((isMoveMode || isFillMode || isRotateMode || isReshapeMode) && selectionStart != null) {

                    selectionRect = new Rectangle(Math.min(selectionStart.x, selectionEnd.x),
                            Math.min(selectionStart.y, selectionEnd.y),
                            Math.abs(selectionEnd.x - selectionStart.x),
                            Math.abs(selectionEnd.y - selectionStart.y));

                    selectVec = selectVectorsInRect(selectionRect);

                    if (selectVec != null) {

                        if (!undoStack.peek().uniqueId.equals(selectVec.uniqueId) || firstEdit) {
                            selectVec.isEditMode = true;
                            firstEdit = false;
                        }

                        if (isMoveMode) {

                            int width = Math.abs(selectVec.x1 - selectVec.x2);
                            int height = Math.abs(selectVec.y1 - selectVec.y2);

                            vec = new Vector(selectionEnd.x, selectionEnd.y, selectionEnd.x+width,
                                    selectionEnd.y+height, selectVec.shape, drawingColor, selectVec.fillingColor,
                                    strokeWidth, selectVec.shiftPressed, selectVec.uniqueId, false, selectVec.rotationAngle);

                            networkService.updateDrawings(vec);

                            stackOperate(vec, selectVec);
                            repaint();
                        } else if (isFillMode) {

                            vec = new Vector(selectVec.x1, selectVec.y1, selectVec.x2,
                                    selectVec.y2, selectVec.shape, selectVec.color, fillingColor,
                                    strokeWidth, selectVec.shiftPressed, selectVec.uniqueId, false, selectVec.rotationAngle);

                            networkService.updateDrawings(vec);

                            stackOperate(vec, selectVec);
                            repaint();
                        } else if (isRotateMode) {

                            isRotatingShow = false;

                            vec = new Vector(selectVec.x1, selectVec.y1, selectVec.x2,
                                    selectVec.y2, selectVec.shape, selectVec.color, selectVec.fillingColor,
                                    selectVec.strokeWidth, selectVec.shiftPressed, selectVec.uniqueId, false, rotationAngle);

                            networkService.updateDrawings(vec);

                            stackOperate(vec, selectVec);
                            repaint();
                        } else if (isReshapeMode) {

                            isReshapeShow = false;

                            vec = new Vector((int)(selectVec.x1 / reshapeSize), (int)(selectVec.y1 / reshapeSize), (int)(selectVec.x2*reshapeSize),
                                    (int)(selectVec.y2*reshapeSize), selectVec.shape, selectVec.color, selectVec.fillingColor,
                                    selectVec.strokeWidth, selectVec.shiftPressed, selectVec.uniqueId, false, selectVec.rotationAngle);

                            networkService.updateDrawings(vec);

                            stackOperate(vec, selectVec);
                            repaint();
                        }
                    }
                    selectionRect = null;

                } else if (!isMoveMode && !isFillMode && !isRotateMode && !isReshapeMode && startPoint != null && endPoint != null) {

                    String uniqueId = UUID.randomUUID().toString();

                    vec = new Vector(startPoint.x, startPoint.y, endPoint.x, endPoint.y,
                                        currentShapeType, drawingColor,null, strokeWidth, shiftPressed, uniqueId, false, 0);

                    drawVector(vec, networkService);
                    repaint();
                }
            }
        };
        addMouseListener(mouseAdapter);
        addMouseMotionListener(mouseAdapter);
    }

    /**
     * Overrides the paintComponent method to draw the vectors on the panel.
     * Handles the drawing of shapes with current transformations like rotation and resizing.
     *
     * @param g The Graphics object to be painted.
     */
    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);

        for (Vector vector : vectorMap.values()) {
            if (selectVec != null && vector.uniqueId.equals(selectVec.uniqueId) && isRotateMode && isRotatingShow) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.rotate(rotationAngle, (vector.x1+vector.x2)/2, (vector.y1+vector.y2)/2);
                vector.drawVector(g2d);
            } else if (selectVec != null && vector.uniqueId.equals(selectVec.uniqueId) && isReshapeMode && isReshapeShow) {
                Graphics2D g2d = (Graphics2D) g.create();
                int storex1 = vector.x1;
                int storey1 = vector.y1;
                int storex2 = vector.x2;
                int storey2 = vector.y2;
                vector.x1 = (int)( vector.x1 / reshapeSize);
                vector.y1 = (int)( vector.y1 / reshapeSize);
                vector.x2 = (int)( vector.x2 * reshapeSize);
                vector.y2 = (int)( vector.y2 * reshapeSize);
                g2d.rotate(vector.rotationAngle, (vector.x1+vector.x2)/2, (vector.y1+vector.y2)/2);
                vector.drawVector(g2d);
                vector.x1 = storex1;
                vector.y1 = storey1;
                vector.x2 = storex2;
                vector.y2 = storey2;
            } else {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.rotate(vector.rotationAngle, (vector.x1+vector.x2)/2, (vector.y1+vector.y2)/2);
                vector.drawVector(g2d);
            }
        }
    }

    public void setCurrentShapeType(String shapeType) {
        this.currentShapeType = shapeType;
    }

    public void setDrawingColor(Color color) {
        this.drawingColor = color;
    }

    public void setStrokeWidth(int width) {
        this.strokeWidth = width;
    }

    public void setShiftPressed(boolean shiftPressed) {
        this.shiftPressed = shiftPressed;
    }

    /**
     * Performs the undo operation. Removes the last vector from the current drawing
     * or reverses the last edit operation. Updates the vector map and repaints the panel.
     */
    public void undo() {
        if (!undoStack.isEmpty()) {

            Vector vec = undoStack.pop();
            if (!vec.isEditMode) {
                vectorMap.remove(vec.uniqueId);
                if (selectVec != null) {
                    if (vec.uniqueId.equals(selectVec.uniqueId)) {
                        vec.isEditMode = !(vec.isEditMode);
                    }
                }
                redoStack.push(vec);
            } else if (vec.isEditMode) {
                vec.isEditMode = !(vec.isEditMode);
                vectorMap.put(vec.uniqueId, vec);
                redoStack.push(vec);
            }
        }
        if ((isMoveMode || isFillMode || isRotateMode || isReshapeMode) && undoStack.size() >= 1) {
            Vector vec1 = undoStack.peek();
            vectorMap.put(vec1.uniqueId, vec1);
        }
        repaint();
    }

    /**
     * Performs the redo operation. Re-applies the last undone vector drawing or edit operation.
     * Updates the vector map and repaints the panel.
     */
    public void redo() {
        if (!redoStack.isEmpty()) {
            Vector vec = redoStack.pop();
            undoStack.push(vec);
            vectorMap.put(vec.uniqueId, vec);
            repaint();
        }
    }

    /**
     * Operates on the vector stack for undo and redo functionality.
     * Adds the newly created or edited vector to the stack and updates the vector map.
     *
     * @param vec The new or edited vector.
     * @param selectVec The previously selected vector before editing.
     */
    public void stackOperate(Vector vec, Vector selectVec) {
        vectorMap.put(vec.uniqueId, vec);

        if (selectVec != null && selectVec.isEditMode) {
            undoStack.push(selectVec);
        }

        undoStack.push(vec);
        redoStack.clear();
    }

    public boolean isMoveMode() {
        return isMoveMode;
    }

    public void setMoveMode(boolean isMoveMode) {
        this.isMoveMode = isMoveMode;
    }

    public boolean isFillMode() {
        return isFillMode;
    }

    public void setFillMode(boolean fillMode) {
        this.isFillMode = fillMode;
    }

    public boolean isRotateMode() {
        return isRotateMode;
    }

    public void setRotateMode (boolean rotateMode) {
        this.isRotateMode = rotateMode;
    }

    public boolean isReshapeMode() {
        return isReshapeMode;
    }

    public void setReshapeMode(boolean reshapeMode) {
        this.isReshapeMode = reshapeMode;
    }

    public void setFillingColor(Color fillingColor) {
        this.fillingColor = fillingColor;
    }

    /**
     * Selects vectors within a specified rectangular area.
     * Used in edit modes to identify which vector is being interacted with.
     *
     * @param rect The selection rectangle.
     * @return The selected Vector, if any.
     */
    private Vector selectVectorsInRect(Rectangle rect) {
        Vector vec = null;
        for (Vector vector : vectorMap.values()) {
            if (vector.intersects(rect)) {
                vec = vector;
                break;
            }
        }
        return vec;
    }

    /**
     * Calculates the center point of the selected vector.
     * Used in rotate and reshape modes to determine the center of transformation.
     *
     * @param selectVec The selected Vector.
     */
    public void getRotationCenter(Vector selectVec) {
        centrePoint.x = (selectVec.x1 + selectVec.x2) / 2;
        centrePoint.y = (selectVec.y1 + selectVec.y2) / 2;
    }

    /**
     * Calculates the rotation angle based on the current and center points.
     * Used in rotate mode to determine the angle of rotation.
     *
     * @param center The center point for rotation.
     * @param current The current point of the mouse.
     * @return The calculated rotation angle.
     */
    public double calculateRotationAngle(Point center, Point current) {
        double angle2 = Math.atan2(current.y - center.y, current.x - center.x);
        return angle2 ;
    }

    /**
     * Calculates the size factor for reshaping based on the current and start points.
     * Used in reshape mode to scale the size of the vector.
     *
     * @param center The center point for resizing.
     * @param current The current point of the mouse.
     * @param selectionStart The starting point of the reshape action.
     * @return The size factor for reshaping.
     */
    public double calculateReshapeSize(Point center, Point current, Point selectionStart) {
        double distance1 = Math.pow((selectionStart.x-center.x)^2 + (selectionStart.y-center.y)^2, 0.5);
        double distance2 = Math.pow((current.x-center.x)^2 + (current.y-center.y)^2, 0.5);
        return distance2/distance1;
    }

    /**
     * Saves the current frame as an image to the chosen directory.
     * Presents a file chooser to the user and saves the image if confirmed.
     *
     * @param jframe The JFrame containing the content to be saved.
     */
    public void saveFrame(JFrame jframe) {
        Container content = jframe.getContentPane();
        BufferedImage img = new BufferedImage(jframe.getWidth(), jframe.getHeight(),BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = img.createGraphics();
        content.printAll(g2d);

        JFileChooser fileChooser = new JFileChooser(FileSystemView.getFileSystemView().getDefaultDirectory());
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        int returnValue = fileChooser.showSaveDialog(null);

        if (returnValue == JFileChooser.APPROVE_OPTION) {

            String fileFolder = fileChooser.getSelectedFile().getAbsolutePath();
            File f = new File(fileFolder + "/CS5001-p3.jpg");

            try {
                ImageIO.write(img, "jpg", f);
                System.out.println("save success");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        g2d.dispose();
    }

    /**
     * clean Frame by clear vectorMap.
     */
    public void cleanFrame() {
        this.vectorMap.clear();
        this.undoStack.clear();
        this.redoStack.clear();
        this.selectVec = null;
        repaint();
    }

    /**
     * Updates the vector map with a new map, usually received from the network service.
     * Clears the current map and repaints the panel with the new vectors.
     *
     * @param newMap The new map of vectors to be displayed.
     */
    public void setVectorMap(HashMap<String, Vector> newMap) {
        vectorMap.clear();
        vectorMap.putAll(newMap);
        repaint();
    }

    /**
     * Handles drawing a vector onto the panel and updating the network service.
     * Adds the vector to the map, updates the network service, and manages undo stack.
     *
     * @param vec The vector to be drawn.
     * @param networkService The network service for updating drawings.
     */
    public void drawVector(Vector vec, NetworkService networkService) {

        vectorMap.put(vec.uniqueId, vec);

        if (networkService != null) {
            networkService.addDrawings(vec);
        }

        setShiftPressed(false);

        undoStack.push(vec);
        redoStack.clear();
    }

}


