package views;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.JButton;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import java.awt.Font;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.ImageIcon;

public class ServerView extends JFrame {

	private JPanel contentPane;
	public JTextField portTf;
	public JButton startBt,stopBt,saveBt;
	
	public JTextArea contentServer;
	public JLabel ipv4;
	private final JLabel lblNewLabel = new JLabel("Status: Server stopped");

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ServerView frame = new ServerView();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public ServerView() {
		setTitle("Máy chủ");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 878, 576);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBackground(new Color(192, 192, 192));
		panel.setBounds(0, 0, 864, 529);
		contentPane.add(panel);
		panel.setLayout(null);
		
		ipv4 = new JLabel("o my gosh", SwingConstants.CENTER);
		ipv4.setForeground(new Color(0, 0, 0));
		ipv4.setFont(new Font("Roboto", Font.BOLD, 20));
		ipv4.setBounds(10, 10, 434, 20);
		panel.add(ipv4);
		
		JPanel startPanel = new JPanel();
		startPanel.setBackground(new Color(255, 255, 255));
		startPanel.setBounds(10, 41, 844, 62);
		panel.add(startPanel);
		startPanel.setLayout(null);
		
		JLabel lblNewLabel_1 = new JLabel("PORT");
		lblNewLabel_1.setForeground(new Color(0, 0, 0));
		lblNewLabel_1.setFont(new Font("Dialog", Font.BOLD, 20));
		lblNewLabel_1.setBounds(10, 12, 82, 32);
		startPanel.add(lblNewLabel_1);
		
		portTf = new JTextField();
		portTf.setBackground(new Color(255, 255, 255));
		portTf.setForeground(new Color(0, 0, 0));
		portTf.setFont(new Font("Dialog", Font.BOLD, 15));
		portTf.setBounds(84, 8, 165, 44);
		portTf.setHorizontalAlignment(JTextField.CENTER);
		startPanel.add(portTf);
		portTf.setColumns(10);
		
		startBt = new JButton("Start Server");
		startBt.setIcon(new ImageIcon(ServerView.class.getResource("/images/play.png")));
		startBt.setBackground(new Color(255, 255, 255));
		startBt.setForeground(new Color(0, 0, 0));
		startBt.setFont(new Font("Dialog", Font.BOLD, 15));
		startBt.setBounds(259, 9, 199, 41);
		startPanel.add(startBt);
		
		stopBt = new JButton("Stop Server");
		stopBt.setBackground(new Color(255, 255, 255));
		stopBt.setIcon(new ImageIcon(ServerView.class.getResource("/images/stop-button.png")));
		stopBt.setSelectedIcon(new ImageIcon(ServerView.class.getResource("/images/stopped.png")));
		stopBt.setFont(new Font("Tahoma", Font.BOLD, 15));
		stopBt.setBounds(468, 7, 191, 44);
		stopBt.setEnabled(false);
		startPanel.add(stopBt);
		
		saveBt = new JButton("Save Log");
		saveBt.setBackground(new Color(255, 255, 255));
		saveBt.setIcon(new ImageIcon(ServerView.class.getResource("/images/diskette.png")));
		saveBt.setFont(new Font("Tahoma", Font.BOLD, 15));
		saveBt.setBounds(669, 7, 165, 44);
		startPanel.add(saveBt);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBackground(new Color(255, 255, 255));
		panel_1.setBounds(10, 127, 844, 363);
		panel.add(panel_1);
		panel_1.setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 10, 824, 343);
		panel_1.add(scrollPane);
		
		contentServer = new JTextArea();
		contentServer.setBounds(10, 10, 824, 343);
		panel_1.add(contentServer);
		contentServer.setForeground(new Color(0, 0, 0));
		contentServer.setBackground(new Color(192, 192, 192));
		contentServer.setFont(new Font("Roboto", Font.PLAIN, 15));
		lblNewLabel.setForeground(new Color(0, 128, 0));
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 20));
		lblNewLabel.setBounds(10, 494, 272, 25);
		panel.add(lblNewLabel);
		setVisible(true);
		setLocationRelativeTo(null);
	}
	
	public void setIPv4(String text) {
		ipv4.setText(text);
	}
	
	public void disableButtonConnect() {
		lblNewLabel.setText("Status: Server is running");
		startBt.setEnabled(false);
		stopBt.setEnabled(true);
		
	}
	public void enableButtonConnect() {
		lblNewLabel.setText("Status: Server is stopped");
		startBt.setEnabled(true);
		stopBt.setEnabled(false);
	}
}
