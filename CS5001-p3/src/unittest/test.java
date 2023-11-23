package unittest;

import controller.DrawingPanel;
import model.Vector;
import view.MainUI;
import view.NetworkService;
import org.junit.Assert;
import org.junit.Before;

import java.awt.*;

public class Test {

    public DrawingPanel drawingpanel;
    public NetworkService networkService;
    public Vector vec1;
    public Vector vec2;
    public Vector vec3;
    public Vector vec4;
    public MainUI mainUI;

        /**
         * The method to set up the variables for test.
         */
        @Before
        public void setup() {
            vec1 = new Vector(20, 20, 40, 40, "LINE",
                    Color.BLACK, null,1,false,
                    "e7684fab-17ed-453e-a730-87e4d3381659",false,0);
            vec2 = new Vector(60,60, 80,80, "RECTANGLE",
                    Color.RED, null,2,false,
                    "e7684fab-17ed-453e-a730-87e4d3381660",false,0);
            vec3 = new Vector(100, 100, 120,120, "ELLIPSE",
                    Color.GREEN, null,1,false,
                    "e7684fab-17ed-453e-a730-87e4d3381661",false,0);
            vec4 = new Vector(140, 140, 160, 160, "TRIANGLE",
                    Color.GREEN, null,1,false,
                    "e7684fab-17ed-453e-a730-87e4d3381662",false,0);
            drawingpanel = new DrawingPanel(networkService);
            mainUI = new MainUI();
        }

        /**
         * The method to test if Vector and Model can be created.
         */
        @org.junit.Test
        public void testExist() {
            Assert.assertNotNull(vec1);
            Assert.assertNotNull(vec2);
            Assert.assertNotNull(vec3);
            Assert.assertNotNull(vec4);
            Assert.assertNotNull(drawingpanel);
        }

        /**
         * The method to test if addVector can work.
         */
        @org.junit.Test
        public void testDrawVectors() throws AWTException{

            Assert.assertTrue(drawingpanel.undoStack.size() == 0);

            drawingpanel.drawVector(vec1, networkService);
            Assert.assertTrue(drawingpanel.undoStack.size() == 1);
            drawingpanel.drawVector(vec2, networkService);
            Assert.assertTrue(drawingpanel.undoStack.size() == 2);
            drawingpanel.drawVector(vec3, networkService);
            Assert.assertTrue(drawingpanel.undoStack.size() == 3);
            drawingpanel.drawVector(vec4, networkService);
            Assert.assertTrue(drawingpanel.undoStack.size() == 4);

        }

    @org.junit.Test
    public void testEditVector() throws AWTException{

        drawingpanel.drawVector(vec1, networkService);
        drawingpanel.drawVector(vec2, networkService);
        drawingpanel.drawVector(vec3, networkService);
        drawingpanel.drawVector(vec4, networkService);

        Vector editVector;
        int width = Math.abs(vec2.x1 - vec2.x2);
        int height = Math.abs(vec2.y1 - vec2.y2);

        editVector = new Vector(vec2.x1+200, vec2.y1+200, vec2.x1+200+width,
                vec2.y1+200+height, vec2.shape, vec2.color, vec2.fillingColor,
                vec2.strokeWidth, vec2.shiftPressed, vec2.uniqueId, false, vec2.rotationAngle);

        vec2.isEditMode = true;
        drawingpanel.stackOperate(editVector, vec2);

        Assert.assertTrue(drawingpanel.undoStack.size() == 6);
        Assert.assertEquals(drawingpanel.undoStack.peek(), editVector);
        drawingpanel.undoStack.pop();
        Assert.assertEquals(drawingpanel.undoStack.peek(), vec2);

    }

        /**
         * The method to test if undo, redo can work.
         */
        @org.junit.Test
        public void testUndoRedoVector() {
            drawingpanel.drawVector(vec1, networkService);
            drawingpanel.drawVector(vec2, networkService);
            drawingpanel.drawVector(vec3, networkService);
            drawingpanel.drawVector(vec4, networkService);

            Vector editVector;
            editVector = new Vector(vec3.x1, vec3.y1, vec3.x2,
                    vec3.y2, vec3.shape, Color.BLUE, Color.PINK,
                    vec3.strokeWidth, vec3.shiftPressed, vec3.uniqueId, false, 20);

            vec3.isEditMode = true;
            drawingpanel.selectVec = vec3;
            drawingpanel.stackOperate(editVector, drawingpanel.selectVec);

            Assert.assertTrue(drawingpanel.redoStack.size() == 0);

            drawingpanel.undo();
            Assert.assertEquals(drawingpanel.undoStack.peek(), vec3);
            Assert.assertTrue(drawingpanel.redoStack.size() == 1);

            drawingpanel.undo();
            Assert.assertEquals(drawingpanel.undoStack.peek(), vec4);
            Assert.assertTrue(drawingpanel.redoStack.size() == 2);

            drawingpanel.undo();
            vec3.isEditMode = false;
            Assert.assertEquals(drawingpanel.undoStack.peek(), vec3);
            System.out.println(drawingpanel.redoStack.size());
            Assert.assertTrue(drawingpanel.redoStack.size() == 3);

            drawingpanel.redo();
            Assert.assertEquals(drawingpanel.undoStack.peek(), vec4);
            Assert.assertTrue(drawingpanel.redoStack.size() == 2);

            drawingpanel.redo();
            vec3.isEditMode = true;
            Assert.assertEquals(drawingpanel.undoStack.peek(), vec3);
            Assert.assertTrue(drawingpanel.redoStack.size() == 1);
        }

}
