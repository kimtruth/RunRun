import java.net.InetAddress;
import java.net.UnknownHostException;

public class Student {
    private InetAddress serverAddress;
    private int money = 10000;
    private int score = 0;

    private int spriteIndex = 0;

    private int x = 0;

    private boolean isKeyPressed = false;

    Student() {
        try {
            serverAddress = InetAddress.getByName(InetAddress.getLocalHost().getHostAddress()); //자신의 IP
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public void setSpriteIndex(int spriteIndex) {
        this.spriteIndex = spriteIndex;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setServerAddress(InetAddress serverAddress) {
        this.serverAddress = serverAddress;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setIsKeyPressed(boolean isKeyPressed) {
        this.isKeyPressed = isKeyPressed;
    }

    public int getMoney() {
        return money;
    }

    public int getScore() {
        return score;
    }

    public InetAddress getServerAddress() {
        return serverAddress;
    }

    public int getX() {
        return x;
    }

    public boolean getIsKeyPressed() {
        return isKeyPressed;
    }

    public int getSpriteIndex() {
        return spriteIndex;
    }
}