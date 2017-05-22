import java.io.*;
import java.net.*;
import java.util.Date;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class Server extends Application {
    @Override // Override the start method in the Application class
    public void start(Stage primaryStage) {
        // Text area for displaying contents
        TextArea ta = new TextArea();

        // Create a scene and place it in the stage
        Scene scene = new Scene(new ScrollPane(ta), 450, 200);
        primaryStage.setTitle("Server"); // Set the stage title
        primaryStage.setScene(scene); // Place the scene in the stage
        primaryStage.show(); // Display the stage

        new Thread( () -> {
            try {
                // Create a server socket
                ServerSocket serverSocket = new ServerSocket(8000);
                Platform.runLater(() ->
                        ta.appendText("Server started at " + new Date() + '\n'));

                while (true) {
                    Platform.runLater(() -> ta.appendText(new Date() +
                            ": Wait for players to join session " + '\n'));

                // Listen for a connection request
                Socket player1 = serverSocket.accept();

                    Platform.runLater(() -> {
                        ta.appendText(new Date() + ": Player 1 joined session "
                                 + '\n');
                        ta.appendText("Player 1's IP address" +
                                player1.getInetAddress().getHostAddress() + '\n');
                    });

                        new DataOutputStream(
                                player1.getOutputStream()).writeUTF("player 1");

                Socket player2 = serverSocket.accept();
                        Platform.runLater(() -> {
                            ta.appendText(new Date() + ": Player 2 joined session "
                                    + '\n');
                            ta.appendText("Player 2's IP address" +
                                    player2.getInetAddress().getHostAddress() + '\n');
                        });

                            new DataOutputStream(
                                    player2.getOutputStream()).writeUTF("player 2");
                            Platform.runLater(() ->
                                    ta.appendText(new Date() +
                                            ": Start a thread for session "  + '\n'));

                            // Launch a new thread for this session of two players
                            new Thread(new HandleASession(player1, player2)).start();
                        }
                    }
      catch(IOException ex) {
                        ex.printStackTrace();
                    }
                }).start();
            }



    /**
     * The main method is only needed for the IDE with limited
     * JavaFX support. Not needed for running from the command line.
     */
    public static void main(String[] args) {
        launch(args);
    }
}