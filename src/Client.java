import java.io.*;
import java.net.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Client extends Application {
    // IO streams
    DataOutputStream toServer = null;
    DataInputStream fromServer = null;
    DataOutputStream toServer2 = null;
    DataInputStream fromServer2 = null;
    int[][] locations = new int[5][5];
    int player = 1;
    String hit;
    private Label lblTitle = new Label();
    private boolean myTurn = false;
    private boolean continueToPlay = true;
    private boolean waiting = true;

    // Create and initialize a status label
    private Label lblStatus = new Label();


    @Override // Override the start method in the Application class
    public void start(Stage primaryStage) {



        BorderPane borderPane = new BorderPane();
        borderPane.setTop(lblTitle);
        borderPane.setBottom(lblStatus);

        // Create a scene and place it in the stage
        Scene scene = new Scene(borderPane, 450, 200);
        primaryStage.setTitle("Client"); // Set the stage title
        primaryStage.setScene(scene); // Place the scene in the stage
        primaryStage.show(); // Display the stage

        connectToServer();
    }
    private void connectToServer() {
        try {
            // Create a socket to connect to the server
            Socket socket = new Socket("localhost", 8000);

            // Create an input stream to receive data from the server
            fromServer = new DataInputStream(socket.getInputStream());

            // Create an output stream to send data to the server
            toServer = new DataOutputStream(socket.getOutputStream());
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        new Thread(() -> {
            try {
                // Get notification from the server
                String player = fromServer.readUTF();

                // Am I player 1 or 2?

                    if (player.equals("player 2")) {
                        Platform.runLater(() -> {
                            lblTitle.setText("Player 2 with token 'O'");
                            lblStatus.setText("Waiting for player 1 to move");
                        });
                    }
                 else {
                    Platform.runLater(() -> {
                        lblTitle.setText("Player 1 with token 'X'");
                        lblStatus.setText("Waiting for player 2 to join");
                    });

                    // Receive startup notification from the server
                    fromServer.readInt(); // Whatever read is ignored

                    // The other player has joined
                    Platform.runLater(() ->
                            lblStatus.setText("Player 2 has joined. I start first"));

                    // It is my turn
                    myTurn = true;
                }

                // Continue to play
//                while (continueToPlay) {
//                    if (player.equals("player 1")) {
//                        waitForPlayerAction(); // Wait for player 1 to move
//                        sendMove(); // Send the move to the server
//                        receiveInfoFromServer(); // Receive info from the server
//                    }
//                    else if (player.equals("player 2")) {
//                        receiveInfoFromServer(); // Receive info from the server
//                        waitForPlayerAction(); // Wait for player 2 to move
//                        sendMove(); // Send player 2's move to the server
//                    }
//                }
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }
        }).start();
    }
    /** Wait for the player to mark a cell */
//    private void waitForPlayerAction() throws InterruptedException {
//        while (waiting) {
//            Thread.sleep(100);
//        }
//
//        waiting = true;
//    }
//
//    private void receiveInfoFromServer() throws IOException {
//
//    }








    /**
     * The main method is only needed for the IDE with limited
     * JavaFX support. Not needed for running from the command line.
     */
    public static void main(String[] args) {
        launch(args);
    }
}