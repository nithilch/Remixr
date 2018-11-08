import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class WelcomeScreen extends JFrame {

	private JPanel welcomePane;
	private JButton btnCreatePlaylist;
	private JButton btnOpenPlaylist;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					WelcomeScreen frame = new WelcomeScreen();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public void createEvents()
	{
		btnCreatePlaylist.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseClicked(MouseEvent e)
			{
				CreatePlaylist cp = new CreatePlaylist();
				cp.setVisible(true);
			}
		});
		
		btnOpenPlaylist.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseClicked(MouseEvent e)
			{
				OpenPlaylist op = new OpenPlaylist();
				op.setVisible(true);
			}
		});
	}
	
	public void initComponents()
	{
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		welcomePane = new JPanel();
		welcomePane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(welcomePane);
		
		btnCreatePlaylist = new JButton("Create Playlist");
		
		btnOpenPlaylist = new JButton("Open Playlist");
		
		GroupLayout gl_welcomePane = new GroupLayout(welcomePane);
		gl_welcomePane.setHorizontalGroup(
			gl_welcomePane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_welcomePane.createSequentialGroup()
					.addGap(141)
					.addGroup(gl_welcomePane.createParallelGroup(Alignment.TRAILING, false)
						.addComponent(btnOpenPlaylist, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(btnCreatePlaylist, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
					.addContainerGap(146, Short.MAX_VALUE))
		);
		gl_welcomePane.setVerticalGroup(
			gl_welcomePane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_welcomePane.createSequentialGroup()
					.addGap(38)
					.addComponent(btnCreatePlaylist)
					.addGap(51)
					.addComponent(btnOpenPlaylist)
					.addContainerGap(87, Short.MAX_VALUE))
		);
		welcomePane.setLayout(gl_welcomePane);
	}

	/**
	 * Create the frame.
	 */
	public WelcomeScreen()
	{
		initComponents();
		createEvents();
	}
}
