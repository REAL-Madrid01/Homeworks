# Vector Drawing Application

**Report for CS5001-p3 Assessment**  
**Author: Zhiqi Pu**

## Overview

The Vector Drawing Application is a Java-based vector graphic drawing tool, offering online and offline drawing, editing, and saving functionalities. This application allows users to create and modify basic graphic elements such as lines, rectangles, ellipses, and triangles. It supports color selection, undo/redo actions, and data synchronization with a server. Please note that local operation with the JAR file cannot establish a connection with the server. To view server communication, you should run the `Model.class` main function in the Model Package.

## Installation

To use this application, you need to run the provided JAR file on a computer with Java Runtime Environment (JRE) or execute the `Model.class` main function.

1. Ensure that Java Runtime Environment (JRE) is installed on your computer.
2. Open the file located at `..\out\artifactsCS5001-p3.jar` or run the following command in the command line to start the application:
   ```bash 
   java -jar artifactsCS5001.jar
   ```

## Feature Overview

This program implements the functionalities required in the assignment (server communication requires running `Model.class` instead of the JAR). Specific features include:

- Drawing straight lines (the program's frame coordinates range from 500 points, meeting the job requirements of 1000 points).
- Drawing rectangles/squares (can be an isosceles or ordinary triangle).
- Drawing ellipses/circles (press the Shift key for this).
- Drawing triangles.
- Undo/redo of every previously done operation (using undo/redo buttons).
- Selection of different border/line colors, border/line widths, fill colors for each shape (except for the line, using 'Color' and 'LineWidth' selectors).
- Rotation and resizing of shapes ('Rotate' and 'Reshape' selectors).
- Exporting the drawing as a JPEG file ('Save' button).
- Selecting a previously drawn object to change its location, color, or size ('Move' selector).
- Using networking to share drawings with other users (performing addDrawing and updateDrawing for each operation).

## Operating Instructions

Points to note:

- You will find a 'Color' button and a 'LineWidth' adjuster. This means all subsequent colors and widths are based on the current Color and LineWidth values. Please assign them before drawing, moving, filling, etc. The LineWidth range is 1-100, consistent with the requirements.
- The function to change the shape's border color shares a button with the move function ('Move'). As the assignment does not specify whether to change the internal filling color or the border color, the 'Fill' button here specifically deals with the internal filling color. The filling color also depends on the current color you select. This way, the program can draw shapes with different border and internal colors.
- The program is correct in local testing. If you find that clicking a button does not respond or retains the status of the previous button, it is speculated that the program did not successfully capture your current operation. Please click again, and you should see that the logic is executed correctly (e.g., if you fill a color and then move, using undo and redo).

## How to Use Features

1. **Drawing:** This requires you to use the mouse to drag and form two points, so the frame border range has been processed. Please don't worry about boundary abnormal value issues. When you want to draw circles and squares, please hold down the Shift key and then drag the mouse.
2. **Moving:** You need to drag the shape you want to move to any position inside (including the boundary).
3. **Rotating:** You need to hold the mouse inside (including the boundary) of the shape and rotate it. You will see the process of rotation, and the program will record the final shape saved when you release the mouse.
4. **Filling:** First, you need to select a color, then select a point in the shape you want to fill and slightly drag it (note that just clicking is not enough, you need to slightly draw a point area with the mouse to help the program identify the target shape).
5. **Scaling:** You need to select a point inside the shape and then drag it towards the center or outside of the shape. You will see the scaled shape. If the scaling is too large, it will exceed the boundary but will not affect the drawing (the program will automatically handle it).
6. **Changing Border Color:** You need to first select a color, then choose the 'Move' button, and then gently drag the shape. The color of the shape will change as expected.
7. **Changing Border Thickness:** You need to first adjust the size in 'LineWidth', then choose the 'Move' button

 and gently drag the shape. The thickness of the border will change as expected.
8. **Undo/Redo:** Click to withdraw the current operation or restore the previous operation.
9. **Save:** Click to select a path and save as CS5001-p3.jpeg image.
10. **Clean:** Clean Frame
11. **Server Communication:** When you run the `Model.class` and perform operations, you will see the addition of shapes or updates to the server after each operation in the console (based on the token, you will receive a server status:ok message initially).

## JUnit Testing

Open the `test.class` file in the Test Package and click to run. The test is based on automatic assignment and testing.
JUnit test includes four tests:

1. Testing whether initial shapes and canvas can be successfully built.
2. Testing whether normal drawing is possible.
3. Testing whether shapes can be edited (Move).
4. Testing whether undo and redo run correctly according to logic.

## Appendix

6 independent tests are located in the `testExamples` folder.