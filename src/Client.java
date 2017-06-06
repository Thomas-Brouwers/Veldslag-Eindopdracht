

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;


public class Client extends JFrame {
    private boolean started = false;
    private int limit = 5;
    private int[] soldiers = new int[limit];
    private int index;
    JLabel toplabel;
    JLabel logHeader2;
    private DataInputStream fromServer;
    private DataOutputStream toServer;
    private String host = "localhost";
    private boolean continueToPlay = false;
    private boolean waiting = true;
    private boolean myTurn = false;
    private String player;
    private String status = "";
    private ArrayList<Integer> used = new ArrayList<Integer>();

    sButton buttons[] = new sButton[25];

    public Client() {
        super("Veldslagje");
        this.setDefaultCloseOperation(3);
        Dimension size = new Dimension(450, 400);
        this.setSize(size);
        this.setPreferredSize(size);

        Runnable sound = () -> {
            Sound marsch = new Sound();
        };

        new Thread(sound).start();

        JPanel content = new JPanel(new BorderLayout());
        this.add(content);

        JPanel bar = new JPanel(new FlowLayout());
        content.add(bar, BorderLayout.PAGE_START);

        this.setContentPane(content);
        JButton readybtn = new JButton("Ready");
        readybtn.addActionListener(e -> {

            for (int c = 0; c < 25; c++) {
                try {
                    if (started == false) sendMove(buttons[c].getIsOccupied());
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
            continueToPlay = true;
            started = true;

        });
        bar.add(readybtn, BorderLayout.NORTH);

        JButton reset = new JButton("Reset Selection");
        reset.addActionListener(e -> {
            if (started == false) {
                soldiers = new int[limit];
                index = 0;
                for (int q = 0; q < 25; q++) {
                    buttons[q].setImage(0);
                    buttons[q].setOccupiedMod(0);
                }
            }
        });
        bar.add(reset, BorderLayout.NORTH);

        toplabel = new JLabel("Zet je soldaten neer!");
        bar.add(toplabel);

        JPanel battle = new JPanel(new GridLayout(5, 5));
        content.add(battle, BorderLayout.CENTER);

        for (int i = 0; i < 25; i++) {
            buttons[i] = new sButton(i);
            battle.add(buttons[i]);
        }

        JPanel sidePanel = new JPanel(new BorderLayout());
        Dimension sPd = new Dimension(125, 300);
        sidePanel.setPreferredSize(sPd);
        content.add(sidePanel, BorderLayout.EAST);

        JLabel logHeader = new JLabel("Log:");
        sidePanel.add(logHeader, BorderLayout.NORTH);

        logHeader2 = new JLabel("");
        sidePanel.add(logHeader2, BorderLayout.CENTER);
        this.pack();

        this.setResizable(false);

        this.setVisible(true);

        connectToServer();


    }

    private void connectToServer() {
        try {
            Socket socket = new Socket(host, 8000);

            fromServer = new DataInputStream(socket.getInputStream());

            toServer = new DataOutputStream(socket.getOutputStream());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        new Thread(() -> {
            try {

                player = fromServer.readUTF();
                if (player.equals("player 1")) {
                    fromServer.readInt();
                    toplabel.setText("Je bent aan de beurt!");
                    myTurn = true;
                } else if (player.equals("player 2")) {
                    fromServer.readInt();
                }
                while (continueToPlay) {
                    if (player.equals("player 1")) {
                        waitForPlayerAction();
                        readHit();
                        fromServer.readBoolean();
                        toplabel.setText("De tegenstander is aan de beurt.");
                        receiveInfoFromServer();
                    } else if (player.equals("player 2")) {
                        receiveInfoFromServer();
                        waitForPlayerAction();
                        readHit();
                        fromServer.readBoolean();
                        toplabel.setText("De tegenstander is aan de beurt.");
                    }
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }).start();

    }

    public void readHit() {
        try {
            String hit = fromServer.readUTF();
            if (hit.equals("Hit")) {
                logHeader2.setText("De vijand is geraakt!");
            } else {
                logHeader2.setText("Je hebt gemist.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void waitForPlayerAction() throws InterruptedException {
        waiting = true;
        while (waiting) {
            Thread.sleep(100);
        }
    }

    private void sendMove(int position) throws IOException {
        if (myTurn || !started) {
            toServer.writeInt(position);
            myTurn = false;
        }
    }


    private void receiveInfoFromServer() throws IOException {
        status = fromServer.readUTF();
        System.out.println(status);
        if (status.equals("je hebt gewonnen")) {
            toplabel.setText("Je hebt gewonnen!");
            continueToPlay = false;
            System.out.println("Gewonnen?");
        } else if (status.equals("je hebt verloren")) {
            toplabel.setText("Je hebt verloren!");
            continueToPlay = false;
            System.out.println("Verloren?");
        } else if (status.equals("Het is een gewone zet")) {
            receiveMove(fromServer.readInt());
            myTurn = true;
            System.out.println("nieuwe beurt");
            toplabel.setText("Je bent aan de beurt!");
        }

    }

    public void receiveMove(int move) {
        if (buttons[move].getIsOccupied() == 0) {
            buttons[move].setOccupied(3);
            buttons[move].setImage(3);
        } else if (buttons[move].getIsOccupied() == 1) {
            buttons[move].setOccupied(2);
            buttons[move].setImage(2);
        }
    }


    public static void main(String[] args) {
        new Client();
    }

    public void soldierSelect(int position) {
        if (index < limit && started == false && buttons[position].isOccupied == 0) {
            soldiers[index] = position;
            buttons[position].setOccupied(1);
            index++;
        }
    }


    class sButton extends JButton implements ActionListener {

        int position;
        int isOccupied = 0;

        public sButton(int position) {
            this.position = position;
            this.addActionListener(this);
            setImage(isOccupied);
        }

        public void setImage(int isOccupied) {
            switch (isOccupied) {
                case 0:
                    try {
                        Image grs = ImageIO.read(getClass().getResource("resource/gras.png"));
                        setIcon(new ImageIcon(grs));
                    } catch (Exception ex) {
                        System.out.println(ex);
                    }
                    break;
                case 1:
                    try {
                        Image sol = ImageIO.read(getClass().getResource("resource/sol.png"));
                        setIcon(new ImageIcon(sol));
                    } catch (Exception ex) {
                        System.out.println(ex);
                    }
                    break;
                case 2:
                    try {
                        Image hitSol = ImageIO.read(getClass().getResource("resource/hitSol.png"));
                        setIcon(new ImageIcon(hitSol));
                    } catch (Exception ex) {
                        System.out.println(ex);
                    }
                    break;
                case 3:
                    try {
                        Image hitSol = ImageIO.read(getClass().getResource("resource/hitGras.png"));
                        setIcon(new ImageIcon(hitSol));
                    } catch (Exception ex) {
                        System.out.println(ex);
                    }
                    break;
            }
        }

        public int getPosition() {
            return position;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println(getPosition());
            soldierSelect(getPosition());
            if (myTurn) {
                try {
                    if (used.contains(this.getPosition())) {
                        toplabel.setText("Je hebt hier al geschoten.");
                    } else {
                        sendMove(this.getPosition());
                        waiting = false;
                        used.add(this.getPosition());
                    }
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }

        public int getIsOccupied() {
            return isOccupied;
        }

        public void setOccupied(int state) {
            isOccupied = state;
            setImage(isOccupied);
            repaint();
        }

        public void setOccupiedMod(int occ) {
            isOccupied = occ;
        }
    }

}