

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;


public class Client extends JFrame {
private int width = 5;
private int height = 5;
private boolean started = false;
private int[] soldiers = new int[5];
private int index;

sButton buttons[]=new sButton[25];

public Client() {
    //Initiating Frame
    super("Veldslagje");
    this.setDefaultCloseOperation(3);
    Dimension size = new Dimension(350,400);
    this.setSize(size);
    this.setPreferredSize(size);
    //Content Borderlayout
    JPanel content = new JPanel(new BorderLayout());
    this.add(content);
    //Initiating Top bar
    JPanel bar = new JPanel(new FlowLayout());
    content.add(bar, BorderLayout.PAGE_START);
    /*//Creating top bar ComboBox
    String[] playerStrings = {"Player 1", "Player 2"};
    JComboBox players = new JComboBox(playerStrings);
    players.setSelectedIndex(1);
    bar.add(players, BorderLayout.PAGE_START);*/
    //Creating Connect button
    this.setContentPane(content);
    JButton readybtn = new JButton("Ready");
    readybtn.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            //System.out.println(players.getSelectedItem().toString());
            //zend schip info
            System.out.println(" 1: "+soldiers[0]+" 2: "+soldiers[1]+" 3: "+soldiers[2]+" 4: "+soldiers[3]+" 5: "+soldiers[4]);
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
                soldiers=new int[5];
                index=0;
                for (int q=0; q<25 ;q++){
                    buttons[q].setImage(0);
                    buttons[q].setOccupiedMod(0);
                }
            }
        }
    });
    bar.add(reset, BorderLayout.NORTH);
    //Creating button grid
    JPanel battle = new JPanel(new GridLayout(5, 5));
    content.add(battle, BorderLayout.CENTER);

    for (int i = 0; i < 25; i++) {
        buttons[i] = new sButton(i);
        //buttons[i].addActionListener(this);

        battle.add(buttons[i]);
    }

    this.pack();

    this.setResizable(false);

    this.setVisible(true);
}

    public static void main(String[] args) {
        new Client();
    }

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
                    Image hitSol = ImageIO.read(getClass().getResource("resource/sol.png"));
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
            CoordinatePressed(getPosition());

        }

        public void setOccupied(int state){
            isOccupied = state;
            setImage(isOccupied);
            System.out.println("Pressed "+isOccupied);
            repaint();
        }

        public void setOccupiedMod (int occ){
            isOccupied = occ;
        }
    }



    public void CoordinatePressed(int position){
        if(index<5&&started==false&&buttons[position].isOccupied==0){
        soldiers[index]=position;
        buttons[position].setOccupied(1);
        index++;}
    }
}