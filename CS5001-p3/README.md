# Vector Drawing Application

Report for CS5001-p3 Assessment
author: Zhiqi Pu

## 概述

Vector Drawing Application 是一个基于 Java 的向量图形绘制工具，提供了在线和离线绘图、编辑和保存功能。该应用程序允许用户创建和修改基本的图形元素（如线、矩形、椭圆和三角形），并支持颜色选择、撤销/重做操作以及与服务器的数据同步。请注意当在本地使用JAR操作时无法与服务器建立联系，如果您想查看与服务器的交流，您应该运行Model Package下的Model.class主函数.

## 安装

要使用此应用程序，您需要在具有 Java 运行时环境的计算机上运行提供的 JAR 文件，或是运行Model.class主函数.

1. 确保您的计算机上安装了 Java 运行时环境（JRE）。
2. 打开位于..\out\artifactsCS5001-p3.jar 文件或在命令行中运行以下命令来启动应用程序：
   ```bash
   java -jar artifactsCS5001.jar
   ```

## 功能介绍

本程序实现了作业要求的功能(服务器交流方面需要运行Model.class而不是JAR),具体的功能如下:
•  Drawing straight lines (Based on the 500 coordinate points of the program frame, the 1000 left table meets the job requirements)
• Drawing rectangles/squares (Can be an isosceles or ordinary triangle)
• Drawing ellipses/circles (using Shift key plz)
• Drawing triangles
• Undo/redo every operation did previously (undo/ redo button)
• Different border/line color, border/line width, fill colors for each shape (except for line, 'Color' and 'LineWidth' selectbox)
• Rotation and resize of the shapes ('Rotate' and 'Reshape' selectbox)
• Exporting the drawing as a jpeg file ('Save' button)
• Select a previously drawn object and change its location, color or size ('Move' selectbox)
• Use networking to sharing drawings with other users (conduct addDrawing and updateDrawing for each operation)

## 操作说明

需要注意的点:
•  您会发现Color按钮与LineWidth调节器，这意味着所有下一步的颜色与boardwidth/linewidth基于当前的Color与LineWidth的值，因此如果您想改变改变颜色或变框粗细的话请在绘图或移动、填充等前先为其赋值, LineWidth的范围为1-100与要求保持一致.
•  改变图形边框颜色的功能与move功能共用一个按钮('Move'), 由于要求中未指定改变的是内部填充的颜色还是边框颜色，因此这里使用'Fill' button专门处理内部填充的颜色，filling填充的颜色也取决于当前您选择的color，这样本程序可以绘制不同颜色的边框与内部颜色.
•  程序在本地测试是正确的，如果您遇到了点击按钮却没有做出反应或保持上一个按钮的状态时，推测是程序运行间隔未能成功捕捉到您的当前操作，请重复点击即可，您应该看到逻辑执行是正确的(例如您填充颜色并移动后使用undo 与 redo).

## 功能使用

1. 绘图: 这需要您使用鼠标拖拽前后形成的两个点，因此frame边框范围做了处理，请无需担心边界异常值问题，当您想绘制圆形与正方形时请按住Shift key再拖拽鼠标
2. 移动: 您需要在图形内部(包括边界)拖拽您想要移动的图形到任意位置.
3. 旋转: 您需要在图形内部(包括边界)按住鼠标并旋转，您会看到旋转的过程，程序会记录您释放鼠标时最后图形保存.
4. 填充: 您首先需要选定颜色，然后在您想要填充的图形中选中一个点并稍微拖拽(注意只有点击是不行的，需要稍微用鼠标画出点区域帮助程序识别目标图形)
5. 缩放: 您需要在图形的内部选定一点然后往图形中心或外部拖拽，您会看到缩放的图形，缩放过大时会超出边界但是不影响画图(程序会自动处理).
6. 改变边框颜色: 您需要首先选定颜色，然后选择Move按钮，然后在轻轻拖拽图形，图形颜色变会预期的改变.
7. 改变边框粗细: 您需要首先在LineWidth中调整大小，然后选择Move按钮并轻轻拖拽图形，边框粗细会预期的改变.
8. uodo/redo: 点击回撤当前的操作或恢复上一步的操作.
9. Save: 点击选择路径保存为CS5001-p3.jpeg图片
10. 服务器通信，当您运行Model.class执行操作时，您会在控制台看到每次操作后对服务器的添加形状或更新 (基于token当一开始成功连接，会返回服务器status:ok的信息)

## JUnit test

打开Test Package下test.class文件，点击运行, test是基于自动赋值和测试的.
JUnit test包含四个测试:

1. 测试能否成功构建初始形状与画布
2. 测试是否能够正常画图
3. 测试能否对图形进行编辑操作(Move)
4. 测试undo与redo是否正常按逻辑运行

## 附录
6个独立测试位于testExamples文件夹内