// Nithil Chigullapally 300288453
// Joel Calo 300279569

import java.awt.EventQueue;
import java.awt.Image;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.Color;

public class RemixrClient extends JFrame {

	private JPanel welcomePane;
	private JButton btnCreatePlaylist;
	private JButton btnOpenPlaylist;
	private JLabel remixrLogoLabel;
	public static final int PORT = 6667;
	public static final int FILE_PORT = 7777;
	public static final String IP_ADDRESS = "127.0.0.1";
	public static final int EXIT = 0;
	public static final int CREATE_PLAYLIST = 1;
	public static final int OPEN_PLAYLIST = 2;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					RemixrClient frame = new RemixrClient();
					frame.setVisible(true);
					frame.setTitle("Remixr");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public void createEvents() {
		btnCreatePlaylist.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					CreatePlaylist cp = new CreatePlaylist();
					cp.setVisible(true);
				} catch (Exception es) {
					es.printStackTrace();
					JOptionPane.showMessageDialog(null, "Server is not active");
				}

			}
		});

		btnOpenPlaylist.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					OpenPlaylist op = new OpenPlaylist();
					op.setVisible(true);
				} catch (Exception es) {
					es.printStackTrace();
				}
				
			}
		});
	}

	public void initComponents() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 357, 326);
		welcomePane = new JPanel();
		welcomePane.setBackground(new Color(255, 255, 255));
		welcomePane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(welcomePane);

		btnCreatePlaylist = new JButton("Create Playlist");
		btnCreatePlaylist.setForeground(new Color(204, 102, 0));
		btnCreatePlaylist.setFont(new Font("Tahoma", Font.PLAIN, 18));

		btnOpenPlaylist = new JButton("Open Playlist");
		btnOpenPlaylist.setForeground(new Color(204, 102, 0));
		btnOpenPlaylist.setFont(new Font("Tahoma", Font.PLAIN, 18));
		
		remixrLogoLabel = new JLabel("");
		try {
			Image remixrLogoImg = new ImageIcon(this.getClass().getResource("Images/remixr_logo.png")).getImage();
			remixrLogoLabel.setIcon(new ImageIcon(remixrLogoImg));
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		Image remixrIconImg = new ImageIcon(this.getClass().getResource("Images/remixr.png")).getImage();
		RemixrClient frame = RemixrClient.this;
		frame.setIconImage(remixrIconImg);

		GroupLayout gl_welcomePane = new GroupLayout(welcomePane);
		gl_welcomePane.setHorizontalGroup(
			gl_welcomePane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_welcomePane.createSequentialGroup()
					.addComponent(remixrLogoLabel)
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
				.addGroup(gl_welcomePane.createSequentialGroup()
					.addGap(87)
					.addGroup(gl_welcomePane.createParallelGroup(Alignment.LEADING)
						.addComponent(btnOpenPlaylist, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 160, Short.MAX_VALUE)
						.addComponent(btnCreatePlaylist, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 160, Short.MAX_VALUE))
					.addGap(101))
		);
		gl_welcomePane.setVerticalGroup(
			gl_welcomePane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_welcomePane.createSequentialGroup()
					.addContainerGap()
					.addComponent(remixrLogoLabel, GroupLayout.PREFERRED_SIZE, 73, GroupLayout.PREFERRED_SIZE)
					.addGap(11)
					.addComponent(btnCreatePlaylist, GroupLayout.PREFERRED_SIZE, 46, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(btnOpenPlaylist, GroupLayout.PREFERRED_SIZE, 49, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(47, Short.MAX_VALUE))
		);
		welcomePane.setLayout(gl_welcomePane);
	}

	/**
	 * Create the frame.
	 */
	public RemixrClient() {
		initComponents();
		createEvents();
	}
	
	public static void sendExitCodeToServer() throws UnknownHostException, IOException, ClassNotFoundException {
		try (Socket socket = new Socket(IP_ADDRESS, PORT)) {
			BufferedWriter out = new BufferedWriter(new PrintWriter(socket.getOutputStream()));
			out.write(EXIT);
			out.flush();

		}
	}
}
