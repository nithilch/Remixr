import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Image;

import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.AbstractListModel;
import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.ListModel;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.Scanner;
import java.awt.event.ActionEvent;
import java.awt.Color;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class CreatePlaylist extends JFrame {

	private JPanel contentPane;
	private JList defaultSongs;
	private JList selectedSongs;
	private JScrollPane scrollPane;
	private JButton savePlaylistBtn;
	private JLabel remixrIconLabel;
	private JLabel lblNewLabel;
	private JLabel instructionsLabel;
	private JButton addBtn;
	private JButton removeBtn;

	final int PLAYLIST_LIMIT = 10;
	private int count = 0;
	private static String[] songs;
	String newSongs[] = new String[10];
	private Socket socket;

	DefaultListModel songNames;
	DefaultListModel defaultSongsList;
	DefaultListModel selectedSongsList = new DefaultListModel<>();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CreatePlaylist frame = new CreatePlaylist();
					frame.setVisible(true);
					frame.setTitle("Remixr");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public void loadData() {
		songNames = new DefaultListModel<>();

		for (int x = 0; x <= songs.length - 1; x++) {
			songNames.addElement(songs[x]);
		}
		defaultSongs.setModel(songNames);
	}

	public void createEvents() {

		addBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int rows = selectedSongs.getModel().getSize();

				if (defaultSongs.getSelectedIndex() < 0) {
					JOptionPane.showMessageDialog(null, "Please select a song to add to the playlist.");
					return;
				}

				if (rows < PLAYLIST_LIMIT) // limit of playlist
				{
					int toRemove = defaultSongs.getSelectedIndex();

					selectedSongsList.addElement(defaultSongs.getSelectedValue().toString());
					selectedSongs.setModel(selectedSongsList);

					defaultSongsList = (DefaultListModel) defaultSongs.getModel();
					defaultSongsList.removeElementAt(toRemove);

					System.out.println("default list: " + defaultSongsList);
					System.out.println("playlist: " + selectedSongsList);
				} else {
					JOptionPane.showMessageDialog(null, "You cannot select more than 10 songs.");
				}
			}
		});

		removeBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int rows = selectedSongs.getModel().getSize();

				if (selectedSongs.getSelectedIndex() < 0) {
					JOptionPane.showMessageDialog(null, "There are no songs to remove.");
					return;
				}

				if (rows > 0) {
					int toRemove = selectedSongs.getSelectedIndex();
					String picked = selectedSongs.getSelectedValue().toString();

					defaultSongsList = (DefaultListModel) defaultSongs.getModel();
					selectedSongsList = (DefaultListModel) selectedSongs.getModel();

					defaultSongsList.addElement(picked);
					defaultSongs.setModel(defaultSongsList);

					selectedSongsList.remove(toRemove);
					selectedSongs.setModel(selectedSongsList);

					System.out.println("default list: " + defaultSongsList);
					System.out.println("playlist: " + selectedSongsList);
				}
			}
		});

		savePlaylistBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int rows = selectedSongs.getModel().getSize();
				String userPlaylist;

				if (rows == 0) {
					JOptionPane.showMessageDialog(null, "Playlist is empty.");
				} else {
					String playlistName = JOptionPane.showInputDialog("Please enter the name of the playlist file:");
					try {
						File file = new File(".\\Playlists\\" + playlistName + ".txt");

						if (file.exists()) {
							int response = JOptionPane.showConfirmDialog(null,
									"File name " + playlistName
											+ ".txt already exists. Do you want to overwrite this file?",
									"Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
							if (response != JOptionPane.YES_OPTION) {
								return;
							}
						}
						BufferedWriter output = new BufferedWriter(new FileWriter(file));
						for (int x = 0; x <= rows - 1; x++) {
							userPlaylist = selectedSongsList.getElementAt(x).toString();
							System.out.println("Test: " + userPlaylist);
							output.write(userPlaylist);
							output.newLine();
						}
						output.close();
						System.out.println("Playlist saved at " + System.getProperty("user.dir") + "\\Playlists\\"
								+ playlistName + ".txt.");
						JOptionPane.showMessageDialog(null, "Playlist saved at " + System.getProperty("user.dir")
								+ "\\Playlists\\" + playlistName + ".txt.");

					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		});
	}

	public void initComponents() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 699, 501);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(255, 255, 255));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);

		JScrollPane songsScrollPane = new JScrollPane();

		addBtn = new JButton(">>");
		addBtn.setForeground(new Color(204, 102, 0));

		removeBtn = new JButton("<<");
		removeBtn.setForeground(new Color(204, 102, 0));

		JScrollPane selectedSongScrollPane = new JScrollPane();

		savePlaylistBtn = new JButton("Save Playlist");
		savePlaylistBtn.setForeground(new Color(204, 102, 0));
		savePlaylistBtn.setFont(new Font("Tahoma", Font.PLAIN, 14));

		remixrIconLabel = new JLabel("");
		try {
			Image remixrIconImg = new ImageIcon(this.getClass().getResource("Images/remixr.png")).getImage();
			remixrIconLabel.setIcon(new ImageIcon(remixrIconImg));
		} catch (Exception e) {
			e.printStackTrace();
		}

		JLabel songListLabel = new JLabel("Song List:");
		songListLabel.setFont(new Font("Tahoma", Font.BOLD, 14));

		lblNewLabel = new JLabel("Selected Songs:");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 14));

		instructionsLabel = new JLabel("<html><p>Instructions:</p>"
				+ "<p>- To add a song to the list, click a song on the left pane and click the '>>' button (maximum of 10).</p>"
				+ "<p>- To remove a song from the list, cling a song on the right pane and click the << button.</p>"
				+ "<p>- To save the playlist, click the Save Playlist button and name your playlist.</p></html>");
		instructionsLabel.setHorizontalAlignment(SwingConstants.LEFT);

		Image remixrIconImg = new ImageIcon(this.getClass().getResource("Images/remixr.png")).getImage();
		CreatePlaylist frame = CreatePlaylist.this;
		frame.setIconImage(remixrIconImg);

		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(gl_contentPane.createParallelGroup(Alignment.LEADING).addGroup(gl_contentPane
				.createSequentialGroup()
				.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING).addComponent(remixrIconLabel)
						.addGroup(gl_contentPane.createSequentialGroup().addContainerGap().addGroup(gl_contentPane
								.createParallelGroup(Alignment.LEADING)
								.addComponent(songListLabel, GroupLayout.PREFERRED_SIZE, 80, GroupLayout.PREFERRED_SIZE)
								.addGroup(gl_contentPane.createSequentialGroup()
										.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
												.addComponent(instructionsLabel, Alignment.LEADING, 0, 0,
														Short.MAX_VALUE)
												.addComponent(songsScrollPane, Alignment.LEADING,
														GroupLayout.PREFERRED_SIZE, 285, GroupLayout.PREFERRED_SIZE))
										.addPreferredGap(ComponentPlacement.RELATED)
										.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
												.addComponent(addBtn, GroupLayout.PREFERRED_SIZE, 49,
														GroupLayout.PREFERRED_SIZE)
												.addComponent(removeBtn))))
								.addPreferredGap(ComponentPlacement.RELATED)
								.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
										.addComponent(lblNewLabel)
										.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
												.addComponent(savePlaylistBtn).addComponent(selectedSongScrollPane,
														GroupLayout.PREFERRED_SIZE, 286, GroupLayout.PREFERRED_SIZE)))))
				.addGap(613)));
		gl_contentPane.setVerticalGroup(gl_contentPane.createParallelGroup(Alignment.LEADING).addGroup(gl_contentPane
				.createSequentialGroup()
				.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING).addGroup(gl_contentPane
						.createSequentialGroup()
						.addComponent(remixrIconLabel, GroupLayout.PREFERRED_SIZE, 66, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.UNRELATED)
						.addComponent(songListLabel, GroupLayout.PREFERRED_SIZE, 21, GroupLayout.PREFERRED_SIZE)
						.addGap(7))
						.addGroup(gl_contentPane.createSequentialGroup()
								.addComponent(lblNewLabel, GroupLayout.PREFERRED_SIZE, 21, GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(ComponentPlacement.RELATED)))
				.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup().addPreferredGap(ComponentPlacement.UNRELATED)
								.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
										.addGroup(gl_contentPane.createSequentialGroup().addComponent(addBtn).addGap(29)
												.addComponent(removeBtn).addGap(69))
										.addComponent(songsScrollPane, GroupLayout.PREFERRED_SIZE, 209,
												GroupLayout.PREFERRED_SIZE))
								.addPreferredGap(ComponentPlacement.RELATED).addComponent(instructionsLabel))
						.addGroup(gl_contentPane.createSequentialGroup()
								.addComponent(selectedSongScrollPane, GroupLayout.PREFERRED_SIZE, 209,
										GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(ComponentPlacement.RELATED).addComponent(savePlaylistBtn,
										GroupLayout.PREFERRED_SIZE, 36, GroupLayout.PREFERRED_SIZE)))
				.addContainerGap(34, Short.MAX_VALUE)));
		selectedSongs = new JList();
		selectedSongScrollPane.setViewportView(selectedSongs);
		selectedSongs.setBackground(new Color(255, 255, 153));
		selectedSongs.setForeground(new Color(0, 0, 0));

		defaultSongs = new JList();
		defaultSongs.setBackground(new Color(255, 255, 153));
		songsScrollPane.setViewportView(defaultSongs);
		contentPane.setLayout(gl_contentPane);
	}

	/**
	 * Create the frame.
	 */
	public CreatePlaylist() {

		try {
			socket = new Socket(RemixrClient.IP_ADDRESS, RemixrClient.PORT);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					getAvailableSongsFromServer();
				} catch (ClassNotFoundException | IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
		

		initComponents();
		loadData();
		createEvents();
	}

	// Socket for songs
	public void getAvailableSongsFromServer() throws UnknownHostException, IOException, ClassNotFoundException {
		System.out.println("Client: reading from " + RemixrClient.IP_ADDRESS + ":" + RemixrClient.PORT);
		BufferedWriter out = new BufferedWriter(new PrintWriter(socket.getOutputStream()));
		ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
		out.write(RemixrClient.CREATE_PLAYLIST);
		out.flush();
		System.out.println("Message " + RemixrClient.CREATE_PLAYLIST + " sent to server");
		songs = (String[]) in.readObject();
		System.out.println("Object received = " + songs.toString());

	}
}
