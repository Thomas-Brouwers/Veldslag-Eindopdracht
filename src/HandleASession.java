import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Created by tmbro on 22-5-2017.
 */
public class HandleASession implements Runnable {

    private Socket player1;
    private Socket player2;

    // Create and initialize cells
    private char[][] cell = new char[3][3];

    private DataInputStream fromPlayer1;
    private DataOutputStream toPlayer1;
    private DataInputStream fromPlayer2;
    private DataOutputStream toPlayer2;

    // Continue to play
    private boolean continueToPlay = true;

    /**
     * Construct a thread
     */
    public HandleASession(Socket player1, Socket player2) {
        this.player1 = player1;
        this.player2 = player2;

        // Initialize cells
        for (int i = 0; i < 5; i++)
            for (int j = 0; j < 5; j++)
                cell[i][j] = ' ';
    }

    @Override
    public void run() {
        try {
            // Create data input and output streams
            DataInputStream fromPlayer1 = new DataInputStream(
                    player1.getInputStream());
            DataOutputStream toPlayer1 = new DataOutputStream(
                    player1.getOutputStream());
            DataInputStream fromPlayer2 = new DataInputStream(
                    player2.getInputStream());
            DataOutputStream toPlayer2 = new DataOutputStream(
                    player2.getOutputStream());

            // Write anything to notify player 1 to start
            // This is just to let player 1 know to start
            toPlayer1.writeInt(1);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
