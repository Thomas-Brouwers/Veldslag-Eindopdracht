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
    @Override
    public void start(Stage primaryStage) {

        TextArea ta = new TextArea();


        Scene scene = new Scene(new ScrollPane(ta), 450, 200);
        primaryStage.setTitle("Server");
        primaryStage.setScene(scene);
        primaryStage.show();

        new Thread( () -> {
            try {

                ServerSocket serverSocket = new ServerSocket(8000);
                Platform.runLater(() ->
                        ta.appendText("Server started at " + new Date() + '\n'));

                while (true) {
                    Platform.runLater(() -> ta.appendText(new Date() +
                            ": Wait for players to join session " + '\n'));


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


                    new Thread(new HandleASession(player1, player2)).start();
                }
            }
            catch(IOException ex) {
                ex.printStackTrace();
            }
        }).start();
    }




    public static void main(String[] args) {
        launch(args);
    }
}