public class Main {
    public static void main(String[] args) {
        Student[] student = new Student[2];
        student[0] = new Student();
        student[1] = new Student();

        Store store = new Store();

        UdpServer server = new UdpServer();
        UdpClient client = new UdpClient();

        MainFrame main = new MainFrame(student, store, server, client);

    }
}
