package Model;

import java.awt.*;

/**
 * Represents a vector graphic. This class contains basic properties for drawing a shape,
 * such as coordinates, shape type, color, fill color, stroke width, shift key status, unique identifier, and rotation angle.
 * Supported shapes include line, rectangle, ellipse, and triangle.
 */
public class Vector {

    // Fields related to coordinates and drawing properties
    public int x1;
    public int y1;
    public int x2;
    public int y2;
    public String shape;
    public Color color = null;
    public Color fillingColor = null;
    public int strokeWidth;
    public boolean shiftPressed = false;
    public String uniqueId = null;
    public boolean isEditMode = false;
    public double rotationAngle = 0;

    // Constants defining supported shape types
    public static final String LINE = "LINE";
    public static final String RECTANGLE = "RECTANGLE";
    public static final String ELLIPSE = "ELLIPSE";
    public static final String TRIANGLE = "TRIANGLE";

    /**
     * Constructs a new vector graphic with specified properties.
     *
     * @param x1           The x-coordinate of the starting point.
     * @param y1           The y-coordinate of the starting point.
     * @param x2           The x-coordinate of the ending point.
     * @param y2           The y-coordinate of the ending point.
     * @param shape        The type of shape to be drawn.
     * @param color        The color of the shape's outline.
     * @param fillingColor The color used to fill the shape.
     * @param strokeWidth  The width of the shape's outline.
     * @param shiftPressed Indicates whether the shift key is pressed.
     * @param uniqueId     A unique identifier for the shape.
     * @param isEditMode   Indicates whether the shape is in edit mode.
     * @param rotationAngle The rotation angle of the shape in radians.
     */
    public Vector(int x1, int y1, int x2, int y2, String shape, Color color, Color fillingColor, int strokeWidth, boolean shiftPressed, String uniqueId, Boolean isEditMode, double rotationAngle) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.shape = shape;
        this.color = color;
        this.fillingColor = fillingColor;
        this.strokeWidth = strokeWidth;
        this.shiftPressed = shiftPressed;
        this.uniqueId = uniqueId;
        this.isEditMode = isEditMode;
        this.rotationAngle = rotationAngle;
    }

    /**
     * Draws the vector graphic on the given graphics context.
     *
     * @param graphic The graphics context on which the vector graphic is drawn.
     */
    public void drawVector(Graphics graphic) {

        Graphics2D g2d = (Graphics2D) graphic.create();
        int width = Math.abs(x2 - x1);
        int height = Math.abs(y2 - y1);

        if (shiftPressed) {
            int size = Math.min(width, height);
            width = size;
            height = size;
        }

        if(fillingColor != null) {
            g2d.setColor(fillingColor);
            fillShape(g2d, width, height);
        }

        g2d.setColor(color);
        g2d.setStroke(new BasicStroke(this.strokeWidth));
        drawShape(g2d, width, height);
    }

    /**
     * Determines if the vector's graphical representation intersects with the specified rectangle.
     *
     * @param rect The rectangle to test for intersection.
     * @return true if the vector intersects with the rectangle, false otherwise.
     */
    public boolean intersects(Rectangle rect) {

        Rectangle bounds = getBounds();
        return rect.intersects(bounds);
    }

    /**
     * Gets the bounding rectangle of the vector's graphical representation.
     *
     * @return A Rectangle object representing the bounds of the vector.
     */
    public Rectangle getBounds() {

        return new Rectangle(Math.min(x1, x2), Math.min(y1, y2),
                Math.abs(x1 - x2), Math.abs(y1 - y2));
    }

    /**
     * Fills the shape represented by the vector with the current filling color.
     *
     * @param g2d   The Graphics2D object used for drawing.
     * @param width  The width of the shape.
     * @param height The height of the shape.
     */
    private void fillShape(Graphics2D g2d, int width, int height) {
        switch (shape) {

            case RECTANGLE:
                g2d.fillRect(Math.min(x1, x2), Math.min(y1, y2), width, height);
                break;
            case ELLIPSE:
                g2d.fillOval(Math.min(x1, x2), Math.min(y1, y2), width, height);
                break;
            case TRIANGLE:
                int[] xPoints = {x1, x2, (x1 + x2) / 2};
                int[] yPoints = {y1, y1, y2};
                int nPoints = 3;
                g2d.fillPolygon(xPoints, yPoints, nPoints);
                break;
        }
    }

    /**
     * Draws the outline of the shape represented by the vector.
     *
     * @param g2d   The Graphics2D object used for drawing.
     * @param width  The width of the shape.
     * @param height The height of the shape.
     */
    private void drawShape(Graphics2D g2d, int width, int height) {
        switch (shape) {

            case LINE:
                g2d.drawLine(x1, y1, x2, y2);
                break;
            case RECTANGLE:
                g2d.drawRect(Math.min(x1, x2), Math.min(y1, y2), width, height);
                break;
            case ELLIPSE:
                g2d.drawOval(Math.min(x1, x2), Math.min(y1, y2), width, height);
                break;
            case TRIANGLE:
                int[] xPoints = {x1, x2, (x1 + x2) / 2};
                int[] yPoints = {y1, y1, y2};
                int nPoints = 3;
                g2d.drawPolygon(xPoints, yPoints, nPoints);
                break;
        }
    }
}
