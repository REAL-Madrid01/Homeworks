package view;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

import model.Vector;

/**
 * NetworkService class responsible for handling network-related operations in the drawing application.
 * This class manages network connections, sending and receiving drawing data to and from a server.
 */
public class NetworkService extends JSONUtils{
    public Socket socket;
    public InputStream is;
    public OutputStream os;
    public BufferedWriter writer;
    private String token = "ec60b474-56f2-4cf3-8894-4f181afbd9ba";
    public LinkedHashMap<String, Vector> vectorMap = new LinkedHashMap<>();
    public MainUI frame;

    /**
     * Constructor for NetworkService. Initializes network connections.
     */
    public NetworkService() {
        try {
            socket = new Socket("cs5001-p3.dynv6.net", 8080);
            is = socket.getInputStream();
            os = socket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Starts the network service, handling incoming data and continuously updating the vector map.
     */
    public void run() {
        new Thread(() -> {
            loginRequest();
            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
                while (true) {

                    String input = br.readLine();
                    vectorMap.clear();
//                    System.out.println(input);
                    vectorMap = getVectorMapFromServer(input);
                    if (vectorMap != null) {
                        for (Map.Entry<String, Vector> entry : vectorMap.entrySet()) {
                            System.out.println(vector2String(entry.getValue()));
                        }
                    }

                }
            } catch (Exception e) {}
        }).start();
    }

    /**
     * Sends a login request to the server using a predefined token.
     */
    public void loginRequest() {
        try {
            String responseBody = "{\"action\": \"login\", \"data\" :{\"token\":\"" + this.token + "\"}}\n";
            byte[] responseBodyBytes = responseBody.getBytes(StandardCharsets.UTF_8);
            os.write(responseBodyBytes);
            os.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Requests the current drawings from the server.
     */
    public void getDrawings() {
        try {
            String responseBody = "{\"action\": \"getDrawings\"}\n";
            System.out.println(responseBody);
            byte[] responseBodyBytes = responseBody.getBytes(StandardCharsets.UTF_8);
            os.write(responseBodyBytes);
            os.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sends a new drawing to the server.
     *
     * @param vector The Vector object to be added to the server.
     */
    public void addDrawings(Vector vector) {
        try {
            String responseBody = "{\"action\": \"addDrawing\", \"data\":"+vector2String(vector)+"}\n";
            System.out.println(responseBody);
            byte[] responseBodyBytes = responseBody.getBytes(StandardCharsets.UTF_8);
            os.write(responseBodyBytes);
            os.flush();
            getDrawings();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Updates an existing drawing on the server.
     *
     * @param vector The Vector object to be updated on the server.
     */
    public void updateDrawings(Vector vector) {
        try {
            String responseBody = "{\"action\": \"updateDrawing\", \"data\":"+vector2String(vector)+"}\n";
            System.out.println(responseBody);
            byte[] responseBodyBytes = responseBody.getBytes(StandardCharsets.UTF_8);
            os.write(responseBodyBytes);
            os.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Closes the network connection.
     */
    public void closeConnection() {
        try {
            if (os != null) os.close();
            if (is != null) is.close();
            if (socket != null && !socket.isClosed()) socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Updates the local vector map based on the data from the server.
     *
     * @param newMap The updated map of Vector objects received from the server.
     */
    public void updateVectorlMap(LinkedHashMap<String, Vector> newMap) {
        if (frame.drawingpanel != null) {
            frame.drawingpanel.setVectorMap(newMap);
        }
    }

    /**
     * Sets the MainUI instance for the network service.
     *
     * @param frame The MainUI instance to be set.
     */
    public void setMainUI(MainUI frame) {
        this.frame = frame;
        this.frame.setVisible(true);
    }

    /**
     * Compares two Vector objects to see if they match.
     *
     * @param vec1 The first Vector object.
     * @param vec2 The second Vector object.
     * @return true if the Vectors match, false otherwise.
     */
    public boolean matchVector(Vector vec1, Vector vec2) {

        if (vec1.x1 == vec2.x1 && vec1.x2 == vec2.x2) {
            return true;
        }
        return false;
    }

}




