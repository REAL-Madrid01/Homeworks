package View;

import javax.json.*;
import java.io.StringReader;
import java.util.*;
import java.awt.Color;
import Model.Vector;

/**
 * Utility class for handling JSON operations related to vector objects.
 * Includes methods for converting vectors to and from JSON format.
 */
public class JSONUtils {

    public JSONUtils(){

    }

    /**
     * Parses a JSON string from the server and converts it into a LinkedHashMap of Vectors.
     *
     * @param input The JSON string to be parsed.
     * @return A LinkedHashMap where each key is a unique ID and each value is a corresponding Vector object.
     */
    public LinkedHashMap<String, Vector> getVectorMapFromServer(String input) {

        LinkedHashMap<String, Vector> vectorMap = new LinkedHashMap<>();

        JsonReader jsonReader = Json.createReader(new StringReader(input));
        try {
            JsonObject jsonObject = jsonReader.readObject();

            if (jsonObject.containsKey("result")) {
                String result = jsonObject.getString("result");
                if (result.equals("error")) {
                    System.out.println("server show error:" + jsonObject.getString("message"));
                    return null;
                } else {
                    System.out.println("communicate status:" + result);
                    return null;
                }
            }
        } catch (javax.json.stream.JsonParsingException e) {

            System.out.println("entering handling getDrawing");
            JsonArray jsonArray = jsonReader.readArray();
            for (JsonObject jsonObject : jsonArray.getValuesAs(JsonObject.class)) {

                String shape = jsonObject.getString("type");
                String uniqueId = jsonObject.getString("id");
                int x1 = (int) jsonObject.getJsonNumber("x").doubleValue();
                int y1 = (int) jsonObject.getJsonNumber("y").doubleValue();
                Vector vec = null;

                JsonObject propertiesObject = jsonObject.getJsonObject("properties");

                if (shape.equals("line")) {

                    int x2 = (int) jsonObject.getJsonNumber("x2").doubleValue();
                    int y2 = (int) jsonObject.getJsonNumber("y2").doubleValue();
                    int strokeWidth = propertiesObject.getInt("lineWidth");
                    String color = propertiesObject.getString("lineColor");
                    vec = new Vector(x1, y1, x2, y2, "LINE", transfromColor(color), transfromColor(color),
                            strokeWidth, false, uniqueId, false, 0.0);
                } else if (shape.equals("triangle")) {

                    int x2 = (int) jsonObject.getJsonNumber("x2").doubleValue();
                    int y2 = (int) jsonObject.getJsonNumber("y2").doubleValue();
                    int strokeWidth = propertiesObject.getInt("borderWidth");
                    Double rotationAngle = Double.valueOf(propertiesObject.getInt("borderWidth"));
                    String color = propertiesObject.getString("borderColor");
                    String fillingColor = propertiesObject.getString("fillColor");
                    vec = new Vector(x1, y1, x2, y2, "TRIANGLE", transfromColor(color), transfromColor(fillingColor),
                            strokeWidth, false, uniqueId, false, rotationAngle);
                } else if (shape.equals("ellipse")) {

                    int x2 = (int) jsonObject.getJsonNumber("width").doubleValue() + x1;
                    int y2 = (int) jsonObject.getJsonNumber("height").doubleValue() + y1;
                    int strokeWidth = propertiesObject.getInt("borderWidth");
                    Double rotationAngle = Double.valueOf(propertiesObject.getInt("borderWidth"));
                    String color = propertiesObject.getString("borderColor");
                    String fillingColor = propertiesObject.getString("fillColor");
                    vec = new Vector(x1, y1, x2, y2, "ELLIPSE", transfromColor(color), transfromColor(fillingColor),
                            strokeWidth, false, uniqueId, false, rotationAngle);
                } else if (shape.equals("rectangle")) {

                    int x2 = (int) jsonObject.getJsonNumber("width").doubleValue() + x1;
                    int y2 = (int) jsonObject.getJsonNumber("height").doubleValue() + y1;
                    int strokeWidth = propertiesObject.getInt("borderWidth");
                    Double rotationAngle = Double.valueOf(propertiesObject.getInt("borderWidth"));
                    String color = propertiesObject.getString("borderColor");
                    String fillingColor = propertiesObject.getString("fillColor");
                    vec = new Vector(x1, y1, x2, y2, "RECTANGLE", transfromColor(color), transfromColor(fillingColor),
                            strokeWidth, false, uniqueId, false, rotationAngle);
                }

                vectorMap.put(uniqueId, vec);
            }
        } finally {
            jsonReader.close();
        }
        return vectorMap;
    }

    /**
     * Converts a Vector object into a JSON-formatted string.
     *
     * @param vector The Vector object to be converted.
     * @return A JSON-formatted string representing the Vector object.
     */
    public static String vector2String(Vector vector) {

        StringBuilder json = new StringBuilder();

        switch (vector.shape) {
            case "RECTANGLE":
                json.append("{");
                json.append("\"id\"" + ":").append("\""+vector.uniqueId+"\"").append(",");
                json.append("\"type\"" + ":").append("\"rectangle\"").append(",");
                json.append("\"x\"" + ":").append((float)vector.x1).append(",");
                json.append("\"y\"" + ":").append((float)vector.y1).append(",");
                json.append("\"properties\"" + ":").append("{");
                json.append("\"width\"" + ":").append((float)Math.abs(vector.x2-vector.x1)).append(",");
                json.append("\"height\"" + ":").append((float)Math.abs(vector.y2-vector.y1)).append(",");
                json.append("\"rotation\"" + ":").append((int)(Math.abs(vector.rotationAngle))).append(",");
                json.append("\"borderColor\"" + ":").append("\""+transformColorToString(vector.color)+"\"").append(",");
                json.append("\"borderWidth\"" + ":").append(vector.strokeWidth).append(",");
                json.append("\"fillColor\"" + ":").append("\""+transformColorToString(vector.fillingColor)+"\"");
                json.append("}}");
                break;
            case "ELLIPSE":
                json.append("{");
                json.append("\"id\"" + ":").append("\""+vector.uniqueId+"\"").append(",");
                json.append("\"type\"" + ":").append("\"ellipse\"").append(",");
                json.append("\"x\"" + ":").append((float)vector.x1).append(",");
                json.append("\"y\"" + ":").append((float)vector.y1).append(",");
                json.append("\"properties\"" + ":").append("{");
                json.append("\"width\"" + ":").append((float)Math.abs(vector.x2-vector.x1)).append(",");
                json.append("\"height\"" + ":").append((float)Math.abs(vector.y2-vector.y1)).append(",");
                json.append("\"rotation\"" + ":").append((int)(Math.abs(vector.rotationAngle))).append(",");
                json.append("\"borderColor\"" + ":").append("\""+transformColorToString(vector.color)+"\"").append(",");
                json.append("\"borderWidth\"" + ":").append(vector.strokeWidth).append(",");
                json.append("\"fillColor\"" + ":").append("\""+transformColorToString(vector.fillingColor)+"\"");
                json.append("}}");
                break;
            case "TRIANGLE":
                json.append("{");
                json.append("\"id\"" + ":").append("\""+vector.uniqueId+"\"").append(",");
                json.append("\"type\"" + ":").append("\"triangle\"").append(",");
                json.append("\"x\"" + ":").append((float)vector.x1).append(",");
                json.append("\"y\"" + ":").append((float)vector.y1).append(",");
                json.append("\"properties\"" + ":").append("{");
                json.append("\"x2\"" + ":").append((float)vector.x2).append(",");
                json.append("\"y2\"" + ":").append((float)vector.y1).append(",");
                json.append("\"x3\"" + ":").append((float)(vector.x1+vector.x2)/2).append(",");
                json.append("\"y3\"" + ":").append((float)vector.y2).append(",");
                json.append("\"rotation\"" + ":").append((int)(Math.abs(vector.rotationAngle))).append(",");
                json.append("\"borderColor\"" + ":").append("\""+transformColorToString(vector.color)+"\"").append(",");
                json.append("\"borderWidth\"" + ":").append(vector.strokeWidth).append(",");
                json.append("\"fillColor\"" + ":").append("\""+transformColorToString(vector.fillingColor)+"\"");
                json.append("}}");
                break;
            case "LINE":
                json.append("{");
                json.append("\"id\"" + ":").append("\""+vector.uniqueId+"\"").append(",");
                json.append("\"type\"" + ":").append("\"line\"").append(",");
                json.append("\"x\"" + ":").append((float)vector.x1).append(",");
                json.append("\"y\"" + ":").append((float)vector.y1).append(",");
                json.append("\"properties\"" + ":").append("{");
                json.append("\"x2\"" + ":").append((float)vector.x2).append(",");
                json.append("\"y2\"" + ":").append((float)vector.y2).append(",");
                json.append("\"lineColor\"" + ":").append("\""+transformColorToString(vector.color)+"\"").append(",");
                json.append("\"lineWidth\"" + ":").append(vector.strokeWidth);
                json.append("}}");
                break;
        }

        return json.toString();
    }

    /**
     * Converts a string representation of a color into an actual Color object.
     *
     * @param color The string representation of the color.
     * @return The Color object corresponding to the provided string.
     */
    public Color transfromColor(String color) {
        Color colorRGB;
        switch (color.toLowerCase()) {
            case "red":
                colorRGB = Color.RED;
                break;
            case "green":
                colorRGB = Color.GREEN;
                break;
            case "blue":
                colorRGB = Color.BLUE;
                break;
            case "yellow":
                colorRGB = Color.YELLOW;
                break;
            case "black":
                colorRGB = Color.BLACK;
                break;
            case "white":
                colorRGB = Color.WHITE;
                break;
            case "cyan":
                colorRGB = Color.CYAN;
                break;
            case "magenta":
                colorRGB = Color.MAGENTA;
                break;
            case "orange":
                colorRGB = Color.ORANGE;
                break;
            case "pink":
                colorRGB = Color.PINK;
                break;
            case "gray":
                colorRGB = Color.GRAY;
                break;
            case "darkgray":
                colorRGB = Color.DARK_GRAY;
                break;
            case "lightgray":
                colorRGB = Color.LIGHT_GRAY;
                break;
            default:
                colorRGB = null;
        }
        return colorRGB;
    }

    /**
     * Converts a Color object into a string representation, finding the closest match among standard colors.
     *
     * @param color The Color object to be converted.
     * @return A string representing the closest standard color to the provided Color object.
     */
    public static String transformColorToString(Color color) {

        String closestColorName = null;

        if (color == null) {
            closestColorName = "black";
            return closestColorName;
        }

        Map<String, Color> colors = new HashMap<>();
        colors.put("red", Color.RED);
        colors.put("green", Color.GREEN);
        colors.put("blue", Color.BLUE);
        colors.put("yellow", Color.YELLOW);
        colors.put("black", Color.BLACK);
        colors.put("white", Color.WHITE);
        colors.put("cyan", Color.CYAN);
        colors.put("magenta", Color.MAGENTA);
        colors.put("orange", Color.ORANGE);
        colors.put("pink", Color.PINK);
        colors.put("gray", Color.GRAY);
        colors.put("darkGray", Color.DARK_GRAY);
        colors.put("lightGray", Color.LIGHT_GRAY);

        double minDistance = Double.MAX_VALUE;

        for (Map.Entry<String, Color> entry : colors.entrySet()) {
            double distance = colorDistance(color, entry.getValue());
            if (distance < minDistance) {
                minDistance = distance;
                closestColorName = entry.getKey();
            }
        }

        return closestColorName;
    }

    /**
     * Calculates the distance between two Color objects in RGB space.
     * Used to determine the closest standard color to a given color.
     *
     * @param c1 The first Color object.
     * @param c2 The second Color object.
     * @return The distance between the two colors in RGB space.
     */
    private static double colorDistance(Color c1, Color c2) {
        int red = c1.getRed() - c2.getRed();
        int green = c1.getGreen() - c2.getGreen();
        int blue = c1.getBlue() - c2.getBlue();
        return Math.sqrt(red * red + green * green + blue * blue);
    }
}