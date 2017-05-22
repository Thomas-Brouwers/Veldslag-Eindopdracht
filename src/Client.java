

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

sButton buttons[]=new sButton[25];

public Client() {
    super("Veldslagje");
    this.setDefaultCloseOperation(3);
    this.setSize(1000, 1000);

    JPanel content = new JPanel(new BorderLayout());
    this.add(content);

    JPanel bar = new JPanel(new FlowLayout());
    content.add(bar, BorderLayout.PAGE_START);

    String[] playerStrings = {"Player 1", "Player 2"};
    JComboBox players = new JComboBox(playerStrings);
    players.setSelectedIndex(1);
    bar.add(players, BorderLayout.PAGE_START);

    this.setContentPane(content);
    JButton connect = new JButton("Connect");
    connect.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println(players.getSelectedItem().toString());
            //zend schip info
            started = true;
        }
    });
    bar.add(connect, BorderLayout.PAGE_START);

    JPanel battle = new JPanel(new GridLayout(5, 5));
    content.add(battle, BorderLayout.CENTER);

    for (int i = 0; i < 25; i++) {
        buttons[i] = new sButton(i);
        //buttons[i].addActionListener(this);

        battle.add(buttons[i]);
    }
/*
        ArrayList yList = new ArrayList();
        for (int x2=0; x2<5; x2++){
            ArrayList xList = new ArrayList();

            for

            yList.add(x2, xList);
        }
*/

    this.setVisible(true);
}

    public static void main(String[] args) {
        new Client();
    }

    class sButton extends JButton implements ActionListener {
    ImageIcon ship, emptyXY,hitShip;

    int position;
    byte isOccupied=0;
    //0=empty, 1=ship, 2=hitShip
    //JButton button = null;

    public sButton(int position){
        emptyXY = new ImageIcon("resource/grass128.png");
        this.position=position;
        this.addActionListener(this);
    }
        
        public int getPosition() {
            return position;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println(getPosition());
        }
    }
}