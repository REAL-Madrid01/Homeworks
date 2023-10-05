
# TextAlignment Project

## Description
The primary focus of this project is to process a text file and align the text based on specified parameters using various alignment strategies.

## Core Classes

### TextAlignment
Processes a text file and aligns the text according to specified parameters.

### AlignOperation (Interface)
Defines a contract for classes that can print aligned text.

### LeftAlign
Implements the `AlignOperation` interface to left-align text.

### RightAlign
Implements the `AlignOperation` interface to right-align text.

### CentreAlign
Implements the `AlignOperation` interface to centre-align text.

### JustifyAlign
Implements the `AlignOperation` interface to justify-align text, adjusting the text to fit within a specified line length.

### FileUtil
A utility class for reading text files.

## Usage

To run the `TextAlignment` class, provide the following arguments:
1. `filename`: The path to the file you wish to process.
2. `alignmentType`: The alignment method (options: "left", "right", "centre", "justify").
3. `lineLength`: The desired line length after alignment.

Example:
```
java TextAlignment sample.txt left 80
```

## Output
Regular string printed to the console.

## Author Information
- **Author**: Zhiqi Pu
- **Student ID**: 230028496
- **Version**: 1.0
- **Date**: 1/10/2023

## Change Log
### Version 1.0 (1/10/2023)
- Initial release with basic text alignment functionalities.
