import woodland.ConnectionHandler;

import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;
import java.util.Random;

/**
 * The GameServerMain class is the entry point for the server application.
 * It sets up a server on a specified port, listens for incoming connections,
 * and handles each connection using the ConnectionHandler class.
 */
public class GameServerMain {

	/**
	 * The main method starts the server.
	 * It requires two command-line arguments: the port number and the seed for random number generation.
	 *
	 * @param args Command line arguments, where args[0] is the port number and args[1] is the seed.
	 */
	public static void main(String[] args) {
//		long seed = Long.parseLong(args[1]);
//		int port = Integer.parseInt(args[0]);
		int port = 8080;
		Random random = new Random();
		long seed = 50;
		System.out.println("port input is: " + port);
				try {
					ServerSocket serverSocket = new ServerSocket(port);
//					while(true) {
						Socket socket = serverSocket.accept();
						System.out.println("Server connection on port " + port + " with " + socket.getInetAddress());
						ConnectionHandler connectionhandler = new ConnectionHandler(socket, seed);
						connectionhandler.start();
//					}
				} catch (IOException e) {
					System.out.println("Server exception on port " + port + ": " + e.getMessage());
				}
			}
		}
