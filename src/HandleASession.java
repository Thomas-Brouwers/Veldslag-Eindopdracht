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
    private int[] isOccupiedPlayer1 = new int[25];
    private int[] isOccupiedPlayer2 = new int[25];

    private DataInputStream fromPlayer1;
    private DataOutputStream toPlayer1;
    private DataInputStream fromPlayer2;
    private DataOutputStream toPlayer2;



    public HandleASession(Socket player1, Socket player2) {
        this.player1 = player1;
        this.player2 = player2;
    }

    @Override
    public void run() {
        try {

            DataInputStream fromPlayer1 = new DataInputStream(
                    player1.getInputStream());
            DataOutputStream toPlayer1 = new DataOutputStream(
                    player1.getOutputStream());
            DataInputStream fromPlayer2 = new DataInputStream(
                    player2.getInputStream());
            DataOutputStream toPlayer2 = new DataOutputStream(
                    player2.getOutputStream());


            for (int i = 0; i < 25; i++) {
                isOccupiedPlayer1[i] = fromPlayer1.readInt();
                System.out.println(i + "I" + isOccupiedPlayer1[i]);
            }
            System.out.println(" ");
            for (int j = 0; j < 25; j++) {
                isOccupiedPlayer2[j] = fromPlayer2.readInt();
                System.out.println(j + "J" + isOccupiedPlayer2[j]);
            }
            toPlayer1.writeInt(1);

            while (true) {
                int movePlayer1 = fromPlayer1.readInt();
                toPlayer1.writeInt(1);
                System.out.println("move from player 1: "+movePlayer1);
                if (isOccupiedPlayer2[movePlayer1] == 0)
                    isOccupiedPlayer2[movePlayer1] = 3;
                else if (isOccupiedPlayer2[movePlayer1] == 1)
                    isOccupiedPlayer2[movePlayer1] = 2;

                if (isWon("player 1")) {
                    toPlayer1.writeUTF("je hebt gewonnen");
                    toPlayer2.writeUTF("je hebt verloren");
                    sendMove(toPlayer2, movePlayer1);
                    break; // Break the loop
                } else {
                    // Notify player 2 to take the turn
                    toPlayer2.writeInt(2);

                    // Send player 1's selected row and column to player 2
                    sendMove(toPlayer2, movePlayer1);
                }
                int movePlayer2 = fromPlayer2.readInt();
                toPlayer2.writeInt(1);
                System.out.println("move from player 2: "+movePlayer2);
                if (isOccupiedPlayer1[movePlayer2] == 0)
                    isOccupiedPlayer1[movePlayer2] = 3;
                else if (isOccupiedPlayer1[movePlayer2] == 1)
                    isOccupiedPlayer1[movePlayer2] = 2;

                // Check if Player 2 wins
                if (isWon("player 2")) {
                    toPlayer1.writeUTF("je hebt verloren");
                    toPlayer2.writeUTF("je hebt gewonnen");
                    sendMove(toPlayer1, movePlayer2);
                    break;
                } else {
                    // Notify player 1 to take the turn
                    toPlayer1.writeInt(1);

                    // Send player 2's selected row and column to player 1
                    sendMove(toPlayer1, movePlayer2);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Send the move to other player
     */
    private void sendMove(DataOutputStream out, int move)
            throws IOException {
        out.writeInt(move); // Send button index

    }

    private boolean isWon(String player) {
        if (player.equals("player 1")) {
            for (int i = 0; i < 25; i++) {
                if (isOccupiedPlayer2[i] == 1) {
                    return false;
                }
            }
            return true;
        } else if (player.equals("player 2")) {
            for (int i = 0; i < 25; i++) {
                if (isOccupiedPlayer1[i] == 1) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }
}


