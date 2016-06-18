import javax.swing.*;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class UdpServer {
    private DatagramSocket socket;
    private DatagramPacket inPacket, outPacket;

    private byte[] inMsg;
    private byte[] outMsg;

    private boolean isOpen;

    private String inputData = "";

    public UdpServer() {
        try {
            socket = new DatagramSocket(7777);
            inMsg = new byte[100];
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    public void setIsOpen(boolean isOpen) {
        this.isOpen = isOpen;
    }

    public String getInputData() {
        return inputData;
    }

    void listen() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("Listen!");
                while (isOpen) {
                    // 패킷 생성
                    inPacket = new DatagramPacket(inMsg, inMsg.length);

                    // 패킷 수신
                    try {
                        socket.receive(inPacket);

                        System.out.println("패킷 수신 완료");

                        // 수신한 패킷으로부터 client의 IP주소와 Port주소를 얻는다
                        InetAddress address = inPacket.getAddress();
                        int port = inPacket.getPort();
                        System.out.println(address + " : " + port);

                        String input = new String(inPacket.getData(), 0, inPacket.getLength());
                        System.out.println("[Server] Input : " + input);
                        inputData = input; // 입력값을 저장.

                        String msg = "";
                        if (input.equals("You! RunRun?")) { // 존재하는 IP인가?
                            msg = "Yes! RunRun!";
                        } else if (input.equals("Please Connect!!")) {// 게임 연결 시도시
                            int request = JOptionPane.showConfirmDialog(null, address + "님이 게임신청 하였습니다. 수락하시겠습니까?", "연결 요청", JOptionPane.YES_NO_OPTION);
                            if (request == JOptionPane.YES_OPTION) {
                                msg = "OK";
                            } else {
                                msg = "NO";
                            }
                        } else if (input.equals("You Lose!")) {
                            isOpen = false; // 게임에서 지면 그만 listen
                        }
                        System.out.println("[Server] Output : " + msg);
                        outMsg = msg.getBytes();

                        // 패킷을 생성해서 client에게 전송
                        outPacket = new DatagramPacket(outMsg, outMsg.length, address, port);
                        socket.send(outPacket);

                        System.out.println("전송 완료 ");
                        Thread.sleep(100);
                    } catch (IOException  InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    public DatagramPacket getInPacket() {
        return inPacket;
    }
}