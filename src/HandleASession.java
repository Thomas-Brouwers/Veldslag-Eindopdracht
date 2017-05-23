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


        private boolean continueToPlay = true;


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

                toPlayer1.writeUTF("Zet jouw soldaten neer");
                toPlayer2.writeUTF("Zet jouw soldaten neer");

                for(int i = 0; i< 25; i++)
                    isOccupiedPlayer1[i] = fromPlayer1.readInt();

                for(int j = 0; j< 25; j++)
                    isOccupiedPlayer2[j] = fromPlayer2.readInt();

                toPlayer1.writeInt(1);

                while(true)
                {
                    int movePlayer1 = fromPlayer1.readInt();
                    if(isOccupiedPlayer2[movePlayer1] == 0)
                        isOccupiedPlayer2[movePlayer1] = 3;
                    else if(isOccupiedPlayer2[movePlayer1] == 1)
                        isOccupiedPlayer2[movePlayer1] = 2;
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


