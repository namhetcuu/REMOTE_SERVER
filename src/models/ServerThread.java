package models;

import java.io.*;
import java.net.*;
import java.net.Socket;

import javax.swing.JOptionPane;

public class ServerThread extends Thread{
	public Socket socket;
	
	public ServerThread(Socket socket) {
		this.socket = socket;
	}

	@Override
	public void run() {
		Messager temp = null;
		Socket socketClient = null;
		User userConnect = null;
		try {
			while(true) {
				Object object = readObject(socket);
				if (object == null) {
	                System.out.println("Client đã ngắt kết nối.");
	                break; // Thoát vòng lặp nếu client ngắt kết nối
	            }
				System.out.println("Read from client: " + object);
				
				
				if (object instanceof Messager) {
					Messager messager = (Messager)object;
					System.out.println("Message: " + messager.getText());
					switch (messager.getText()) {
						case "connect": {
							User user = (User)(messager.getObject());
							Socket socket = Server.findSocketByUser(user);
							if (socket != null) {
								System.out.println("Connect success");
								temp = new Messager("Connect success", user);
								writeObject(temp, this.socket);
								temp = new Messager("notification", new String("Có một kết nối mới"));
								writeObject(temp, socket);
							} else {
								System.out.println("Connect fail");
								temp = new Messager("notification", new String("Sai ID hoặc pass"));
								writeObject(temp, this.socket);
							}
							break;
						}
						case "Screen Capture": {
							userConnect = (User) messager.getObject();
							socketClient = Server.findSocketByUser(userConnect);
							temp = new Messager("Server To Client: Screen Capture");
							writeObject(temp, socketClient);
							temp = new Messager("notification", new String("Đã gửi yêu cầu chụp ảnh màn hình"));
							writeObject(temp, this.socket);
							break;
						}
						
						case "Client To Server: Change Desktop Background": {
							System.out.println("Client To Server: Change Desktop Background");
							byte[] imageData = (byte[]) messager.getObject();
							userConnect = (User) messager.getObject2();
							socketClient = Server.findSocketByUser(userConnect);
							
							temp = new Messager("Server To Client: Change Desktop Background", imageData);
							writeObject(temp, socketClient);
							
							temp = new Messager("notification", new String("Đã gửi yêu cầu đổi hình nền"));
							writeObject(temp, this.socket);
							break;
						}
						case "Client To Server: AdjustBrightness":
							userConnect = (User) messager.getObject2();
							socketClient = Server.findSocketByUser(userConnect);
							int bright = (int) messager.getObject();
							
							temp = new Messager("Server To Client: AdjustBrightness", bright);
							writeObject(temp, socketClient);
							break;
						case "Client To Server: ScreenShare":
							userConnect = (User) messager.getObject();
							socketClient = Server.findSocketByUser(userConnect);
							User user = (User) messager.getObject2();
							temp = new Messager("Server To Client: ScreenShare", user);
							writeObject(temp, socketClient);
							break;
						case "Client To Server: Screen":
							userConnect = (User) messager.getObject();
							socketClient = Server.findSocketByUser(userConnect);
							temp = new Messager("Server To Client: Screen", messager.getObject2());
							writeObject(temp, socketClient);
							break;
						case "Client To Server: KeyPressed":
							userConnect = (User) messager.getObject2();
							socketClient = Server.findSocketByUser(userConnect);
							temp = new Messager("Server To Client: KeyPressed", messager.getObject());
							writeObject(temp, socketClient);
							break;
						case "Client To Server: Mouse":
							userConnect = (User) messager.getObject();
							socketClient = Server.findSocketByUser(userConnect);
							temp = new Messager("Server To Client: Mouse", messager.getObject2());
							writeObject(temp, socketClient);
							break;
						case "Client To Server: MouseClick":
							userConnect = (User) messager.getObject();
							socketClient = Server.findSocketByUser(userConnect);
							temp = new Messager("Server To Client: MouseClick");
							writeObject(temp, socketClient);
							break;
						case "Client To Server: Message":
							User user2 = Server.findUserBySocket(this.socket);
							userConnect = (User) messager.getObject2();
							socketClient = Server.findSocketByUser(userConnect);
							temp = new Messager("Server To Client: Message", user2.getId() + ": " + messager.getObject());
							writeObject(temp, socketClient);
							break;
					}
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Lỗi trong ServerThread: " + e.getMessage());
		} finally {
			closeSocket();
		}
		
	}
	
//	public void writeObject(Object object, Socket socket) {
//		try {
//			ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
//			objectOutputStream.writeObject(object);
//			System.out.println("WriteObject to Client: " + object);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
//	
//	public Object readObject(Socket socket) {
//        Object object = null;
//        try {
//            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
//            object = objectInputStream.readObject();
//            System.out.println("ReadObject from Client: " + object);
//        } catch (IOException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        } catch (ClassNotFoundException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//        return object;
//    }
	public void writeObject(Object object, Socket socket) {
        if (socket == null || socket.isClosed()) {
            System.out.println("Socket đã đóng, không thể ghi dữ liệu.");
            return;
        }

        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream())) {
            objectOutputStream.writeObject(object);
            System.out.println("WriteObject to Client: " + object);
        } catch (IOException e) {
            System.out.println("Lỗi khi ghi dữ liệu: " + e.getMessage());
        }
    }

    public Object readObject(Socket socket) {
        if (socket == null || socket.isClosed()) {
            System.out.println("Socket đã đóng, không thể đọc dữ liệu.");
            return null;
        }

        try (ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream())) {
            Object object = objectInputStream.readObject();
            System.out.println("ReadObject from Client: " + object);
            return object;
        } catch (IOException e) {
            System.out.println("Lỗi khi đọc dữ liệu: " + e.getMessage());
            return null;
        } catch (ClassNotFoundException e) {
            System.out.println("Lỗi: Không tìm thấy class " + e.getMessage());
            return null;
        }
    }

    private void closeSocket() {
        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
                System.out.println("Đã đóng kết nối với client.");
            }
        } catch (IOException e) {
            System.out.println("Không thể đóng socket: " + e.getMessage());
        }
    }
}
