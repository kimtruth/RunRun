import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class StartFrame extends JPanel {
    private MainFrame F;
    private ImageIcon bgImg = new ImageIcon("img" + File.separator + "Main.jpg");
    private JLabel bg = new JLabel(bgImg);

    private JLabel myIp = new JLabel();
    private JTextField inputOtherIp = new JTextField();

    private JButton gameStart = new JButton();

    void makeUI() {
        setSize(960, 540);
        setLayout(null);

        Student st1 = new Student();

        myIp.setText(st1.getServerAddress().toString().replace("/", ""));

        bg.setLocation(0, 0);
        bg.setSize(960, 540);

        myIp.setLocation(420, 257);
        myIp.setForeground(Color.WHITE);
        myIp.setFont(new Font("sans-serif", Font.PLAIN, 20));
        myIp.setSize(300, 20);

        inputOtherIp.setLocation(420, 385);
        inputOtherIp.setFont(new Font("sans-serif", Font.PLAIN, 20));
        inputOtherIp.setForeground(Color.WHITE);
        inputOtherIp.setOpaque(false);
        inputOtherIp.setBorder(null);
        inputOtherIp.setSize(300, 20);
        inputOtherIp.setCaretColor(Color.WHITE);

        gameStart.setLocation(250, 455);
        gameStart.setSize(460, 50);
        gameStart.setOpaque(false);
        gameStart.setContentAreaFilled(false);
        gameStart.setBorderPainted(false);
        gameStart.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    void makeEvent() {
        gameStart.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    System.out.println(inputOtherIp.getText());
                    InetAddress otherIp = InetAddress.getByName(inputOtherIp.getText());

                    if (F.client.checkServerAddress(otherIp)) {
                        if (F.client.connectServerAddress(otherIp)) {
                            F.students[1].setServerAddress(otherIp);
                            F.changePanel();
                        } else {
                            JOptionPane.showMessageDialog(null, "입력하신 IP주소가 잘못되었습니다.");
                        }
                    } else {
                        msgbox("IP주소를 확인해주세요");
                    }
                } catch (UnknownHostException e1) {
                    e1.printStackTrace();
                    msgbox("IP주소를 확인해주세요");
                }
            }
        });
    }

    public StartFrame(MainFrame f) {
        F = f;
        makeUI();
        makeEvent();

        F.server.setIsOpen(true);
        F.server.listen();

        add(myIp);
        add(inputOtherIp);
        add(gameStart);

        add(bg);
        setVisible(true);


        msgbox("이용 방법\n" +
                "1. 2인용 게임입니다\n" +
                "2. 둘 중 한명이 상대방의 IP주소를 입력한뒤 시작버튼을 누릅니다\n" +
                "3. 게임 시작과 동시에 스페이스바를 연타합니다\n" +
                "4. 마지막 관문은 매점 할머니께 빠르게 주문을 하면됩니다.\n" +
                "5. 즐겜!");
        ifConnected();
    }

    private void ifConnected() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    if (F.server.getInputData().equals("Game Start!")) {
                        F.students[1].setServerAddress(F.server.getInPacket().getAddress());
                        F.changePanel();
                        break;
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

    private void msgbox(String s) {
        JOptionPane.getRootFrame().dispose();
        JOptionPane.showMessageDialog(null, s);
    }
}