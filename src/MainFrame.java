import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.io.File;

public class MainFrame extends JFrame {
    private Container pane = getContentPane();
    private CardLayout cards = new CardLayout();

    protected Student[] students;
    protected Store store;
    protected int cardIndex = 0;

    protected UdpServer server;
    protected UdpClient client;


    public MainFrame(Student[] students, Store store, UdpServer server, UdpClient client) {
        setLayout(cards);
        setSize(920, 540);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("RunRun!");

        this.students = students;
        this.store = store;
        this.server = server;
        this.client = client;

        pane.add("Start", new StartFrame(this));
        pane.add("Game", new GameFrame(this));

        setVisible(true);
        startBGM();
        //changePanel(); //test
    }

    public void changePanel() {
        cards.next(pane);
        cardIndex = (cardIndex + 1) % pane.getComponentCount();
        System.out.println(cardIndex);
    }

    public void startBGM() {

        try {
            File soundFile = new File("img" + File.separator + "bgm.wav");
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);
            clip.start();
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (Exception error) {
            error.printStackTrace();
            JOptionPane.showMessageDialog(null, "Invalid file!");
        }

    }
}