

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;


public class Client extends JFrame {
private int width = 5;
private int height = 5;
private boolean started = false;
private int limit = 5;
private int[] soldiers = new int[limit];
private int index;
private String logtxt="";
JLabel toplabel;
JLabel logHeader2;
//srvr var
private DataInputStream fromServer;
private DataOutputStream toServer;
private String host = "localhost";
private boolean continueToPlay = false;
private boolean waiting = true;
private boolean myTurn = false;
private boolean waitServer = false;
private boolean playerTurn=true;
private String player;
private String status= "";

sButton buttons[]=new sButton[25];

public Client() {
    //Initiating Frame
    super("Veldslagje");
    this.setDefaultCloseOperation(3);
    Dimension size = new Dimension(450,400);
    this.setSize(size);
    this.setPreferredSize(size);

    Runnable sound = new Runnable() {
        @Override
        public void run() {
            Sound marsch = new Sound();
        }
    };

    new Thread(sound).start();

    //Content Borderlayout
    JPanel content = new JPanel(new BorderLayout());
    this.add(content);

    //Initiating Top bar
    JPanel bar = new JPanel(new FlowLayout());
    content.add(bar, BorderLayout.PAGE_START);

    //Creating Connect button
    this.setContentPane(content);
    JButton readybtn = new JButton("Ready");
    readybtn.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {

            for (int c = 0; c<25; c++){
                try{
                    if(started==false) sendMove(buttons[c].getIsOccupied());
                } catch (IOException ex){
                    ex.printStackTrace();
                }
                //System.out.println(buttons[c].getPosition() + "I"+ buttons[c].getIsOccupied());
            }
            continueToPlay = true;
            started = true;

        }
    });
    bar.add(readybtn, BorderLayout.NORTH);

    //Creating reset button
    JButton reset = new JButton("Reset Selection");
    reset.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if(started==false){
                soldiers=new int[limit];
                index=0;
                for (int q=0; q<25 ;q++){
                    buttons[q].setImage(0);
                    buttons[q].setOccupiedMod(0);
                }
            }
        }
    });
    bar.add(reset, BorderLayout.NORTH);

    //Creating top label
    toplabel = new JLabel("Zet je soldaten neer!");
    bar.add(toplabel);

    //Creating button grid
    JPanel battle = new JPanel(new GridLayout(5, 5));
    content.add(battle, BorderLayout.CENTER);

    for (int i = 0; i < 25; i++) {
        buttons[i] = new sButton(i);
        //buttons[i].addActionListener(this);

        battle.add(buttons[i]);
    }

    //Creating sidepanel with JLabel enInf
    JPanel sidePanel = new JPanel(new BorderLayout());
    Dimension sPd = new Dimension(125,300);
    sidePanel.setPreferredSize(sPd);
    content.add(sidePanel,BorderLayout.EAST);

    JLabel logHeader = new JLabel("Soldaten geraakt op:");
    sidePanel.add(logHeader,BorderLayout.NORTH);

    logHeader2 = new JLabel("Nog niks");
    sidePanel.add(logHeader2, BorderLayout.CENTER);
    this.pack();

    this.setResizable(false);

    this.setVisible(true);

    connectToServer();


}

    private void connectToServer() {
        try {
            // Create a socket to connect to the server
            Socket socket = new Socket(host, 8000);

            // Create an input stream to receive data from the server
            fromServer = new DataInputStream(socket.getInputStream());

            // Create an output stream to send data to the server
            toServer = new DataOutputStream(socket.getOutputStream());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        new Thread(() -> {
            try {

                player = fromServer.readUTF();
                if (player.equals("player 1")) {
                    fromServer.readInt();
                    myTurn = true;
                    toplabel.setText("Je bent aan de beurt!");
                } else if(player.equals("player 2"))
                {
                    fromServer.readInt();
                }
                    while (continueToPlay) {
                        if (player.equals("player 1")) {
                            waitForPlayerAction(); // Wait for player 1 to move
                            fromServer.readBoolean();// Send the move to the server
                            toplabel.setText("De tegenstander is aan de beurt.");
                            receiveInfoFromServer(); // Receive info from the server
                        } else if (player.equals("player 2")) {
                            receiveInfoFromServer(); // Receive info from the server
                            waitForPlayerAction(); // Wait for player 2 to move
                            fromServer.readBoolean(); // Send player 2's move to the server
                            toplabel.setText("De tegenstander is aan de beurt.");
                        }
                    }

                } catch(Exception ex){
                    ex.printStackTrace();
                }

        }).start();

}

    public boolean isContinueToPlay() {
        return continueToPlay;
    }

    public boolean isStarted() {
        return started;
    }

    private void waitForPlayerAction() throws InterruptedException {
        waiting = true;
        while (waiting) { Thread.sleep(100);
        }

    }

    private void sendMove(int position) throws IOException {
    if(myTurn || !started) {
        toServer.writeInt(position); // Send the selected row
        myTurn = false;
    }
    }

    private void sendZet(int position) throws IOException {
        toServer.writeInt(position); // Send the selected row
    }

    private void receiveInfoFromServer() throws IOException{
    status = fromServer.readUTF();
        System.out.println(status);
    if (status.equals("je hebt gewonnen")){
        toplabel.setText("Je hebt gewonnen!");
        continueToPlay=false;
        System.out.println("Gewonnen?");
    }else if(status.equals("je hebt verloren")){
        toplabel.setText("Je hebt verloren!");
        continueToPlay=false;
        System.out.println("Verloren?");
    } else if(status.equals("Het is een gewone zet")){
        receiveMove(fromServer.readInt());
        myTurn = true;
        System.out.println("nieuwe beurt");
        toplabel.setText("Je bent aan de beurt!");
    }

    }

    /*public void hitOrNot(int coord){
        try {
            String answer = fromServer.readUTF();
            if (answer.equals("Hit")){logHeader2.setText(""+coord);};
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/

    public void receiveMove(int move){
    if(buttons[move].getIsOccupied()==0){
        buttons[move].setOccupied(3);
        buttons[move].setImage(3);
    }else if(buttons[move].getIsOccupied()==1){
        buttons[move].setOccupied(2);
        buttons[move].setImage(2);
    }
    }


    public static void main(String[] args) {
        new Client();
    }

    public void soldierSelect(int position){
        if(index<limit&&started==false&&buttons[position].isOccupied==0){
        soldiers[index]=position;
        buttons[position].setOccupied(1);
        index++;}
    }

    /*public int coordinatePressed(int position){
        return position;
    }*/




    class sButton extends JButton implements ActionListener {
        ImageIcon ship, emptyXY,hitShip;

        int position;
        int isOccupied=0;
        //0=empty, 1=sol, 2=hitSol
        //JButton button = null;


        //Custom Button
        public sButton(int position){
            this.position=position;
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
            if(myTurn) {
                try {
                    sendMove(this.getPosition());
                    //hitOrNot(this.getPosition());
                    waiting = false;
                    //System.out.println("andere beurt");
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
                    //waiting = true;


                /*while(started){
                try {
                    if(fromServer.readBoolean()==true&&started){
                        sendMove(this.getPosition());
                        playerTurn = false;
                        break;
                    }else if(fromServer.readBoolean()==false&&started){
                        sendMove(this.getPosition());
                        playerTurn=true;
                        System.out.println("WAKE ME UP INSIDE");
                        break;
                    }} catch (IOException e1) {
                e1.printStackTrace();
            }}*/}

        public int getIsOccupied() {
            return isOccupied;
        }

        public void setOccupied(int state){
            isOccupied = state;
            setImage(isOccupied);
            repaint();
        }

        public void setOccupiedMod (int occ){
            isOccupied = occ;
        }
    }

}