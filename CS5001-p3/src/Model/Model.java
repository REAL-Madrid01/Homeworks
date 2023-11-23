package model;

import view.MainUI;
import view.NetworkService;

/**
 * The main class of the application that initiates and runs the drawing application.
 * It sets up the main user interface and starts the network service for communication
 * with the server, if needed.
 */
public class Model {

    /**
     * The main method that serves as the entry point for the application.
     * It creates the main frame of the application and starts the network service.
     *
     * @param args Command line arguments passed to the program (not used).
     */
    public static void main(String[] args) {
        // Invoke the application's GUI in the Event Dispatch Thread (EDT)
        javax.swing.SwingUtilities.invokeLater(() -> {
            // Initialize the network service
            NetworkService networkService = new NetworkService();
            MainUI frame = new MainUI();

            // Initialize and set up the main frame of the application
            frame.initUI(networkService);
            frame.setVisible(true);

            // Connect the main UI with the network service by using personal token and start it
            networkService.setMainUI(frame);
            networkService.run();
        });
    }
}
