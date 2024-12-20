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
		
		while(true) {
			Object object = readObject(socket);
			//object này đại diện cho message đc gửi từ client
			System.out.println("Read from client: " + object);
			
			if (object instanceof Messager) {
				//ép object đó về kiểu message
				Messager messager = (Messager)object;
				System.out.println("Message: " + messager.getText());
				switch (messager.getText()) {
					case "connect": {
						
						User user = (User)(messager.getObject());//lấy ra đc và ép về kiểu user
						//lấy ra đc socket của client kết nối
						Socket socket = Server.findSocketByUser(user);
						
						if (socket != null) {
							System.out.println("Connect success");
							//temp dau tien se la clien gui yeu cau ket noi
							temp = new Messager("Connect success", user);
							writeObject(temp, this.socket);
							//temp thu 2 se la client dc ket noi
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
						//System.out.println("đây là socket của client trên máy thật: "+socketClient);
						writeObject(temp, socketClient);
						temp = new Messager("notification", new String("Đã gửi yêu cầu chụp ảnh màn hình"));
						//System.out.println("đây là socket của client trên máy ảo: "+this.socket);
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
					//điều chỉnh độ sáng
					case "Client To Server: AdjustBrightness":
						userConnect = (User) messager.getObject2();
						socketClient = Server.findSocketByUser(userConnect);
						int bright = (int) messager.getObject();
						
						temp = new Messager("Server To Client: AdjustBrightness", bright);
						writeObject(temp, socketClient);
						break;
					//theo dõi màn hình 
					case "Client To Server: ScreenShare":
						//lấy ra user đc kết nối
						userConnect = (User) messager.getObject();
						//socket của user đc kết nối
						socketClient = Server.findSocketByUser(userConnect);
						//user của client kết nối
						User user = (User) messager.getObject2();
						temp = new Messager("Server To Client: ScreenShare", user);
						//socketClient này là của client đc kết nối
						writeObject(temp, socketClient);
						break;
						//nay dun de la, gof
					case "Client To Server: Screen":
						//lấy ra đc user của client đc kết nối
						userConnect = (User) messager.getObject();
						//lấy ra đc socket của client đc kết nối
						socketClient = Server.findSocketByUser(userConnect);
						//object2 này là imageData đc gửi từ client kết nối
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
						//tìm user để gửi tin nhắn
						User sender = Server.findUserBySocket(this.socket);
						//tìm user để nhận tin nhắn
						userConnect = (User) messager.getObject2();
						socketClient = Server.findSocketByUser(userConnect);
						temp = new Messager("Server To Client: Message", sender.getId() + ": " + messager.getObject());
						writeObject(temp, socketClient);
						break;
					case "File Transfer":
						 	User targetUser = (User) messager.getObject2();
						    byte[] fileContent = messager.getFileContent();
						    String fileName = messager.getFileName();

						    // Gửi file đến client đích
						    //ServerThread targetThread = getThreadForUser(targetUser);
						    
						    socketClient = Server.findSocketByUser(targetUser);
					        temp = new Messager("File Receive", messager.getObject2(), fileContent, fileName);
					        //targetThread.writeObject(fileMessage); 
					        writeObject(temp, socketClient);
				}
			}
		}
	}
	
	public void writeObject(Object object, Socket socket) {
		try {
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
			objectOutputStream.writeObject(object);
			System.out.println("WriteObject to Client: " + object);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public Object readObject(Socket socket) {
        Object object = null;
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            object = objectInputStream.readObject();
            System.out.println("ReadObject from Client: " + object);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return object;
    }
	private void closeConnection() {
	    try {
	        if (socket != null && !socket.isClosed()) {
	            socket.close();
	            System.out.println("Đã đóng kết nối socket.");
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
}
