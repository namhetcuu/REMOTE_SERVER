package models;

import java.io.*;
import java.net.*;
import java.time.LocalTime;
import java.util.HashMap;

public class Server extends Thread {
    private int port;
    private ServerSocket serverSocket = null;
    private static StringBuilder content = new StringBuilder();
    public static HashMap<User, Socket> users = new HashMap<>();
    private boolean running = true;

    public Server(int port) {
        this.port = port;
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Máy chủ đã khởi động ở cổng " + port);
            addContent("Máy chủ đã khởi động ở cổng " + port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (running && !serverSocket.isClosed()) {
            try {
                Socket socket = serverSocket.accept();
                System.out.println("Client kết nối: " + socket);
                addContent("Client kết nối: " + socket);

                User user = new User();
                users.put(user, socket);
                writeObject(user, socket);

                ServerThread serverThread = new ServerThread(socket);
                serverThread.start();
            } catch (SocketException e) {
                if (!running) {
                    System.out.println("Server socket đã đóng.");
                } else {
                    e.printStackTrace();
                }
                break;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void stopServer() {
        try {
            running = false;
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
                addContent("Server đã dừng.");
            }
            System.out.println("Server đã dừng.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeObject(Object object, Socket socket) {
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectOutputStream.writeObject(object);
            System.out.println("Đã gửi đối tượng tới Client: " + object);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Object readObject(Socket socket) {
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            return objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getIPv4() {
        try {
            InetAddress myIP = InetAddress.getLocalHost();
            return myIP.getHostAddress();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Không xác định.";
    }

    public static String getCurrentTimeAsString() {
        LocalTime currentTime = LocalTime.now();
        return String.format("[%02d:%02d:%02d]", currentTime.getHour(), currentTime.getMinute(), currentTime.getSecond());
    }

    public static synchronized void addContent(String text) {
        String timestampedText = getCurrentTimeAsString() + " " + text;
        if (content.length() > 0) {
            content.append("\n____________________________________________________________\n");
        }
        content.append(timestampedText);
       // System.out.println(timestampedText); // Ghi log ra console
    }

    public static Socket findSocketByUser(User user) {
        return users.get(user);
    }

    public static User findUserBySocket(Socket socket) {
        for (User user : users.keySet()) {
            if (users.get(user).equals(socket)) {
                return user;
            }
        }
        return null;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getContent() {
        return content.toString();
    }

    public void clearContent() {
        synchronized (content) {
            content.setLength(0); // Xóa nội dung cũ
        }
    }

    public static void main(String[] args) {
        Server server = new Server(6868);
        server.start();

        // Thêm ví dụ dừng server sau 30 giây
        try {
            Thread.sleep(30000); // Chờ 30 giây
            server.stopServer();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
