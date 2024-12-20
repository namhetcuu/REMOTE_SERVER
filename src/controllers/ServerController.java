package controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import models.Server;
import views.ServerView;

public class ServerController extends Thread{
	public Server server;
	public ServerView serverView;
	public boolean isStart = false;
	
	public ServerController() {
		int portDefault = 6868;
		serverView = new ServerView();
		serverView.setIPv4(Server.getIPv4());
		serverView.portTf.setText(portDefault + "");
		
		serverView.startBt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int port = Integer.parseInt(serverView.portTf.getText());
				server = new Server(port);
				server.start();
				serverView.disableButtonConnect();
				isStart = true;
			}
		});
		//1280x800
		serverView.stopBt.addActionListener(new ActionListener() {
			
			
			public void actionPerformed(ActionEvent e) {
				
				if(server!=null&&isStart) {
					server.stopServer();
					isStart = false;
					serverView.enableButtonConnect();
				}
				
			}
			
		});
	}
	
	@Override
	public void run() {
		System.out.println("ok");
		while(true) {
            if (isStart) {
                String content = server.getContent();
                serverView.contentServer.setText(content);
            }
            System.out.print("");
		}
	}
	
	public static void main(String[] args) {
		ServerController controller = new ServerController();
		controller.start();
	}
}
