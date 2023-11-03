package woodland;
import woodland.Animals.Animal;
import woodland.Spells.Spell;

import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.StringReader;

/**
 * ConnectionHandler is responsible for managing the connection between a client and the server.
 * It extends the Thread class, allowing it to run in its own thread and handle client-server communication asynchronously.
 */
public class ConnectionHandler extends Thread {

    public final Socket connect;
    public InputStream is;
    public OutputStream os;
    public long seed;;
    public Game game;
    public String action = "Move";

    /**
     * Constructs a new ConnectionHandler for handling client-server communication.
     *
     * @param socket The TCP/IP socket through which the server and client are connected.
     * @param seed   The seed used for random number generation within the game.
     */
    public ConnectionHandler(Socket socket, long seed) {
        this.seed = seed;
        this.connect = socket;
        try {
            is = connect.getInputStream();
            os = connect.getOutputStream();
        } catch (IOException ioe) {
            System.out.println("ConnectionHandler initial with error: " + ioe.getMessage());
        }
    }

    /**
     * Validates the client's request and determines the type of HTTP method being used.
     *
     * @param input1 The first line of the HTTP request, which should contain the HTTP method.
     * @param input2 The second line of the HTTP request, which may contain additional information.
     * @return A string representing the type of request (e.g., "GET/game", "POST/reset").
     * @throws Exception If the request method is not implemented or the input is invalid.
     */
    public String isValidRequest(String input1, String input2) throws Exception {
        // figure out if the client close the Conn
        String method = null;
        if (input1 == null || input1.equals("null")) {
            throw  new Exception("input invalid");
        }
        String methodJudge = input1;
        String methodAction = input2;
        if (methodJudge.contains("GET")) {
            if (methodAction.contains("game")) {
                method = "GET/game";
            } else {
                method = "GET/";
            }
        } else if (methodJudge.contains("POST")) {
            if (methodAction.contains("game")) {
                method = "POST/game";
            } else if (methodAction.contains("reset")) {
                method = "POST/reset";
            }
        } else if (methodJudge.contains("OPTIONS")) {
            method = "OPTIONS/";
        } else {
            throw new Exception("Sorry, method not implemented");
        }
        return method;
    }

    /**
     * The main run method of the thread. It handles client requests and sends appropriate responses.
     */
    public void run() {
        this.game = new Game(this.seed);
        System.out.println("-------start a new ConnectionHandler thread----------");
        // this connection run successfully, so add 1 connect.
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            while (true) {
                String method;
                // get the data or request from client by using BufferReader
                String input = br.readLine();
                if (input == null) {
                    System.out.println("Connection closed by client.");
                    break;
                } else {
                    String[] inputs = input.split(" ");
                    method = isValidRequest(inputs[0], inputs[1]);
                }
                Map<String, String> headers = new HashMap<>();
                String line;
                int contentLength = 0;
                try {
                    while (!(line = br.readLine()).isEmpty()) {
                        int colonPos = line.indexOf(":");
                        if (colonPos != -1) {
                            String headerName = line.substring(0, colonPos).trim();
                            String headerValue = line.substring(colonPos + 1).trim();
                            headers.put(headerName, headerValue);
                            if ("Content-Length".equalsIgnoreCase(headerName)) {
                                contentLength = Integer.parseInt(headerValue.trim());
                            }
                        }
                    }
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
                char[] body = new char[contentLength];
                if (contentLength > 0) {
                    br.read(body, 0, contentLength);
                }
                System.out.println("Method used is:" + method);
                switch (method) {
                    case "GET/":
                        os.write(LinkGetContent());
                        os.flush();
                        break;
                    case "GET/game":
                        os.write(getGameContent());
                        os.flush();
                        break;
                    case "POST/game":
                        os.write(postGameContent(game, body));
                        os.flush();
                        break;
                    case "POST/reset":
                        os.write(postResetContent());
                        os.flush();
                        break;
                    case "OPTIONS/":
                        os.write(LinkGetContent());
                        os.flush();
                        break;
                    default:
                        os.write(returnFor501());
                }
            }
        } catch (Exception e) {
            // exit with closing all Stream objects
            System.out.println("ConnectionHandler run with error:" + e.getMessage());
            try {
                if (os != null) os.close();
                if (is != null) is.close();
                if (connect != null && !connect.isClosed()) connect.close();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    /**
     * Generates a standard HTTP response content for a GET request to the root path.
     *
     * @return A byte array containing the HTTP response.
     * @throws IOException If there is an error writing the response.
     */
    public byte[] LinkGetContent() throws IOException {
        String responseBody = "{\"status\": \"ok\"}";
        byte[] responseBodyBytes = responseBody.getBytes(StandardCharsets.UTF_8);
        String httpResponse = "HTTP/1.1 200 OK\r\n" +
                "Content-Type: application/json\r\n" +
                "Access-Control-Allow-Origin: *\r\n" +
                "Access-Control-Allow-Methods: *\r\n" +
                "Access-Control-Allow-Headers: *\r\n" +
                "Access-Control-Max-Age: 86400\r\n" +
                "Content-Length: " + responseBodyBytes.length + "\r\n" +
                "\r\n" + responseBody;
        return httpResponse.getBytes(StandardCharsets.UTF_8);
    }

    /**
     * Generates a standard HTTP response content for an OPTIONS request.
     *
     * @return A byte array containing the HTTP response.
     * @throws IOException If there is an error writing the response.
     */
    public byte[] LinkOptionsContent() throws IOException {
        String httpResponse = "HTTP/1.1 200 OK\r\n" +
                "Access-Control-Allow-Origin: *\r\n" +
                "Access-Control-Allow-Methods: *\r\n" +
                "Access-Control-Allow-Headers: *\r\n" +
                "Access-Control-Max-Age: 86400\r\n" +
                "\r\n";
        return httpResponse.getBytes(StandardCharsets.UTF_8);
    }

    /**
     * Handles a GET request to retrieve the current state of the game.
     *
     * @return A byte array containing the HTTP response with the game state in JSON format.
     * @throws IOException If there is an error writing the response.
     */
    public byte[] getGameContent() throws IOException {
        String jsonResponse = ToJson.ToJson(this.game, this.action);
        String httpResponse = "HTTP/1.1 200 OK\r\n" +
                "Content-Type: application/json\r\n" +
                "Access-Control-Allow-Origin: *\r\n" +
                "Access-Control-Allow-Methods: *\r\n" +
                "Access-Control-Allow-Headers: *\r\n" +
                "Access-Control-Max-Age: 86400\r\n" +
                "Content-Length: " + jsonResponse.getBytes(StandardCharsets.UTF_8).length + "\r\n" +
                "\r\n" + jsonResponse;
        byte[] responseBytes = httpResponse.getBytes(StandardCharsets.UTF_8);
        return responseBytes;
    }

    /**
     * Handles a POST request to update the game state based on the client's action.
     *
     * @param game The game instance to be updated.
     * @param body The body of the POST request containing the action details.
     * @return A byte array containing the HTTP response with the new game state.
     * @throws IOException If there is an error writing the response.
     */
    public byte[] postGameContent(Game game, char[] body) throws IOException {
        String requestBody = new String(body);
        JsonReader jsonReader = Json.createReader(new StringReader(requestBody));
        JsonObject jsonObject = jsonReader.readObject();
        jsonReader.close();
        String action = jsonObject.getString("action");
        String animalName = "";
        int newRow = 0;
        int newCol = 0;
        String spell = "";
        if (action.contains("move")) {
            animalName = jsonObject.getString("animal");
            JsonObject toSquare = jsonObject.getJsonObject("toSquare");
            newRow = toSquare.getInt("row");
            newCol = toSquare.getInt("col");
        } else if (action.contains("spell")) {
            animalName = jsonObject.getString("animal");
            spell = jsonObject.getString("spell");
        }
        System.out.println("requestBody is " + requestBody);
        if (action.contains("move") && !game.gameOver() && !game.gameWin()) {
            for (Map.Entry<String, Animal> animal : game.animals.entrySet()) {
                if (animal.getKey().equals(animalName)) {
                    int oldRow = animal.getValue().getSquare().getRow();
                    int oldCol = animal.getValue().getSquare().getCol();
                    this.action = game.moveAnimal(animal.getValue(), oldRow, oldCol, newRow, newCol, this.action);
                    game.gameOver();
                    game.gameWin();
                }
            }
        } else if (action.contains("spell") && !game.gameOver() && !game.gameWin()) {
            for (Map.Entry<String, Animal> animal : game.animals.entrySet()) {
                if (animal.getKey().equals(animalName)) {
                    for (Map.Entry<Integer, Spell> entry : animal.getValue().spells.entrySet()) {
                        if (entry.getValue().spellName.contains(spell)) {
                            game.spellAnimal(animal.getValue(), entry.getValue());
                            animal.getValue().updateSpell(animal.getValue(), entry.getValue(), false);
                            this.action = "Move";
                        }
                    }
                }
            }
        }
        return getGameContent();
    }

    /**
     * Handles a POST request to reset the game to its initial state.
     *
     * @return A byte array containing the HTTP response with the reset game state.
     */
    private byte[] postResetContent() {
        this.game.clearMap();
        this.action = "Move";
        this.game = new Game(seed);
        String jsonResponse = ToJson.ToJson(this.game, this.action);
        String httpResponse = "HTTP/1.1 200 OK\r\n" +
                "Content-Type: application/json\r\n" +
                "Access-Control-Allow-Origin: *\r\n" +
                "Access-Control-Allow-Methods: *\r\n" +
                "Access-Control-Allow-Headers: *\r\n" +
                "Access-Control-Max-Age: 86400\r\n" +
                "Content-Length: " + jsonResponse.getBytes(StandardCharsets.UTF_8).length + "\r\n" +
                "\r\n" + jsonResponse;
        byte[] responseBytes = httpResponse.getBytes(StandardCharsets.UTF_8);
        return responseBytes;
    }

    /**
     * Generates a response for a HTTP 501 Not Implemented error.
     *
     * @return A byte array containing the HTTP 501 response.
     * @throws IOException If there is an error writing the response.
     */
    private byte[] returnFor501() throws IOException {
        byte[] content = "501 Not Implemented".getBytes();

        String feedbackString = "HTTP/1.1 501 Not Implemented\r\nContent-Type: text/html\r\nContent-Length: "
                + content.length + "\r\n\r\n";

        System.out.println(feedbackString);
        return content;
    }

}