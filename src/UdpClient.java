import javax.swing.*;
import java.io.IOException;
import java.net.*;

/*
You! RunRun?을 보낸뒤 Yes! RunRun!이 응답되면 존재하는 IP
추가적으로 게임 요청은 Please Connect!!로 하며 OK or NO로 응답한다.
 */

public class UdpClient {
    private DatagramSocket datagramSocket;
    private byte[] msg;

    private DatagramPacket outPacket;
    private DatagramPacket inPacket;

    public UdpClient() {
        try {
            datagramSocket = new DatagramSocket();
            msg = new byte[100];
            inPacket = new DatagramPacket(msg, msg.length);
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }


    public boolean send(InetAddress serverAddress, String msg) { //패킷 전송 성공 여부
        try {
            System.out.println("[Client] Output : " + msg);
            this.msg = msg.getBytes();
            this.outPacket = new DatagramPacket(this.msg, this.msg.length, serverAddress, 7777);

            datagramSocket.send(this.outPacket);     // 전송

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean connectServerAddress(InetAddress serverAddress) {
        try {
            datagramSocket.setSoTimeout(10000);

            if (!send(serverAddress, "Please Connect!!")) //연결 실패시
                return false;
            datagramSocket.receive(inPacket);   // 수신
            String input = new String(inPacket.getData(), 0, inPacket.getLength());

            if (input.equals("OK")) {
                if (!send(serverAddress, "Game Start!")) //연결 실패시
                    return false;
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean checkServerAddress(InetAddress serverAddress) {

        try {
            datagramSocket.setSoTimeout(1000);
            if (!send(serverAddress, "You! RunRun?")) //연결 실패시
                return false;
            datagramSocket.receive(inPacket);   // 수신

            String input = new String(inPacket.getData(), 0, inPacket.getLength());

            System.out.println("[Client] Input : " + input);
            if (input.equals("Yes! RunRun!")) {
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }
}