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

    private int btnnbr;



    public HandleASession(Socket player1, Socket player2) {
        this.player1 = player1;
        this.player2 = player2;
    }

    @Override
    public void run() {
        try {

            fromPlayer1 = new DataInputStream(
                    player1.getInputStream());
            toPlayer1 = new DataOutputStream(
                    player1.getOutputStream());
            fromPlayer2 = new DataInputStream(
                    player2.getInputStream());
            toPlayer2 = new DataOutputStream(
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
            toPlayer2.writeInt(2);

            while (true) {
                int movePlayer1 = fromPlayer1.readInt();
                if(isOccupiedPlayer2[movePlayer1]==1) {
                    System.out.println("Detected hit to player 2");
                    toPlayer1.writeUTF("Hit");
                } else {toPlayer1.writeUTF("Miss");}


                System.out.println("move from player 1: "+movePlayer1);
                toPlayer1.writeBoolean(true);
                if (isOccupiedPlayer2[movePlayer1] == 0)
                    isOccupiedPlayer2[movePlayer1] = 3;
                else if (isOccupiedPlayer2[movePlayer1] == 1)
                    isOccupiedPlayer2[movePlayer1] = 2;
                btnnbr = 0;
                if (isWon("player 1")) {
                    System.out.println("eind");
                    toPlayer1.writeUTF("je hebt gewonnen");
                    toPlayer2.writeUTF("je hebt verloren");
                    break;
                } else {
                    toPlayer2.writeUTF("Het is een gewone zet");
                    sendMove(toPlayer2, movePlayer1);
                }
                int movePlayer2 = fromPlayer2.readInt();

                if(isOccupiedPlayer1[movePlayer2]==1) {
                    System.out.println("Detected hit to player 1");
                    toPlayer2.writeUTF("Hit");
                } else {toPlayer2.writeUTF("Miss");}

                toPlayer2.writeBoolean(true);
                System.out.println("move from player 2: "+movePlayer2);
                if (isOccupiedPlayer1[movePlayer2] == 0)
                    isOccupiedPlayer1[movePlayer2] = 3;
                else if (isOccupiedPlayer1[movePlayer2] == 1)
                    isOccupiedPlayer1[movePlayer2] = 2;
                btnnbr = 0;
                if (isWon("player 2")) {
                    System.out.println("eind");
                    toPlayer1.writeUTF("je hebt verloren");
                    toPlayer2.writeUTF("je hebt gewonnen");
                    break;
                } else {
                    toPlayer1.writeUTF("Het is een gewone zet");
                    sendMove(toPlayer1, movePlayer2);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendMove(DataOutputStream out, int move)
            throws IOException {
        out.writeInt(move);
    }

    private boolean isWon(String player) {
        if (player.equals("player 1") ) {
            if(btnnbr < 25) {
                if (isOccupiedPlayer2[btnnbr] == 1) {
                    return false;
                } else {
                    System.out.println("speler 1 button " + btnnbr + "status" + isOccupiedPlayer2[btnnbr]);
                    btnnbr++;
                    return isWon(player);
                }
            } else {
                return true;
            }

        } else if (player.equals("player 2")) {
            if(btnnbr<25) {
                if (isOccupiedPlayer1[btnnbr] == 1) {
                    return false;
                } else {
                    System.out.println("speler 2 button " + btnnbr + "status" + isOccupiedPlayer1[btnnbr]);
                    btnnbr++;
                    return isWon(player);
                }
            } else {
                return true;
            }
        }
        return false;
    }
}


