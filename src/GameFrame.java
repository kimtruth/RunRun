import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

public class GameFrame extends JPanel {
    private MainFrame F;

    private ImageIcon bgImg = new ImageIcon("img" + File.separator + "runBG.jpg");
    private JLabel bg = new JLabel(bgImg);

    private ImageIcon barImg = new ImageIcon("img" + File.separator + "runBar.gif");
    private JLabel myRoad = new JLabel(barImg);
    private JLabel otherRoad = new JLabel(barImg);

    private ImageIcon miku = new ImageIcon("img" + File.separator + "0.png");

    private JLabel me = new JLabel(miku);
    private JLabel other = new JLabel(miku);

    private Student studentMe;
    private Student studentOther;

    private boolean GameOver = false;

    private JLabel myScore = new JLabel("0");
    private JLabel otherScore = new JLabel("0");

    private void makeUI() {
        setBackground(Color.GRAY);
        setLayout(null);

        bg.setLocation(0, 0);
        bg.setSize(960, 540);

        myRoad.setLocation(0, 190);
        myRoad.setSize(920, 83);

        me.setLocation(F.students[0].getX(), 150);
        me.setSize(200, 113);

        myScore.setLocation(760, 138);
        myScore.setForeground(Color.WHITE);
        myScore.setFont(new Font("sans-serif", Font.BOLD, 50));
        myScore.setSize(300, 50);

        otherRoad.setLocation(0, 360);
        otherRoad.setSize(920, 83);

        otherScore.setLocation(760, 308);
        otherScore.setForeground(Color.WHITE);
        otherScore.setFont(new Font("sans-serif", Font.BOLD, 50));
        otherScore.setSize(300, 50);

        other.setLocation(F.students[1].getX(), 320);
        other.setSize(200, 113);
    }

    private void makeEvent() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    // System.out.println(F.cardIndex);
                    if (F.cardIndex == 1) { //now showing
                        F.setFocusable(true);
                        F.requestFocus();
                        F.addKeyListener(new MyKeyListener());
                        System.out.println("MyKeyListener");
                        break;
                    }
                    try {
                        Thread.sleep(1000 / 20);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                F.client.send(studentOther.getServerAddress(), "0,0"); //Start!
            }
        }).start();

    }

    public GameFrame(MainFrame f) {
        F = f;

        studentMe = F.students[0];
        studentOther = F.students[1];

        makeUI();
        makeEvent();
        add(me);
        add(myScore);
        add(myRoad);

        add(other);
        add(otherScore);
        add(otherRoad);

        add(bg);
        Draw();
    }

    void Update() {
        studentMe.setSpriteIndex((studentMe.getSpriteIndex() + 1) % 20);
        studentMe.setX(studentMe.getX() + 10);
        studentMe.setScore(studentMe.getX()); // score == x좌표

        F.client.send(studentOther.getServerAddress(), studentMe.getX() + "," + studentMe.getSpriteIndex());
        System.out.println(studentMe.getX());
    }

    void Check() {
        if (me.getX() + me.getWidth() > this.getWidth()) {
            while (!GameOver) {
                String input = JOptionPane.showInputDialog(null, F.store.getWantToBuy());
                try {
                    if (input.equals(F.store.getWantToBuy())) {
                        GameOver = true;
                        if (!F.server.getInputData().equals("You Lose!")) {
                            F.client.send(studentOther.getServerAddress(), "You Lose!");

                            msgbox(F.store.getMent());
                            myScore.setText("Win!!");
                            otherScore.setText("Lose..");

                            F.server.setIsOpen(false);
                        } else {
                            msgbox(F.store.getMent());
                            otherScore.setText("Win!!");
                            myScore.setText("Lose..");
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else if (F.server.getInputData().equals("You Lose!")) {
            msgbox(F.store.getMent());
            otherScore.setText("Win!!");
            myScore.setText("Lose..");
        }
    }

    void Draw() {
        System.out.println("DRAW");
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (!GameOver) {

                    String[] otherData = F.server.getInputData().split(",");
                    if (otherData.length == 2) {


                        studentOther.setSpriteIndex(Integer.parseInt(otherData[1]));
                        studentOther.setX(Integer.parseInt(otherData[0]));
                        studentOther.setScore(studentOther.getX()); // score == x좌표

                        if (me.getX() != studentMe.getX()) {
                            me.setLocation(studentMe.getX(), me.getY());
                            me.setIcon(new ImageIcon("img" + File.separator + studentMe.getSpriteIndex() + ".png"));
                            myScore.setText(String.valueOf(studentMe.getScore()));
                        }
                        if (other.getX() != studentOther.getX()) {
                            other.setLocation(studentOther.getX(), other.getY());
                            other.setIcon(new ImageIcon("img" + File.separator + studentOther.getSpriteIndex() + ".png"));
                            otherScore.setText(String.valueOf(studentOther.getScore()));

                            System.out.println(String.valueOf(studentOther.getScore()));
                        }
                    }
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    class MyKeyListener extends KeyAdapter {
        public void keyPressed(KeyEvent e) {
            int keyCode = e.getKeyCode();
            if (keyCode == KeyEvent.VK_SPACE && !F.students[0].getIsKeyPressed()) {
                System.out.println("press");
                F.students[0].setIsKeyPressed(true);
                Update();
                Check();
            }
        }

        public void keyReleased(KeyEvent e) {
            F.students[0].setIsKeyPressed(false);
        }
    }

    private void msgbox(String s) {
        JOptionPane.getRootFrame().dispose();
        JOptionPane.showMessageDialog(null, s);
    }
}
