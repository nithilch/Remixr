// Nithil Chigullapally 300288453
// Joel Calo 300279569

import java.awt.EventQueue;
import java.awt.Image;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.Vector;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.imageio.ImageIO;
import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.LayoutStyle.ComponentPlacement;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JScrollPane;
import javax.swing.JList;
import javax.swing.JOptionPane;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Color;
import javax.swing.JLabel;
import java.awt.Font;

public class OpenPlaylist extends JFrame {

	private JButton playBtn;
	private JButton stopBtn;
	private JButton pauseBtn;
	private JList playlistFilesList;
	private JList fileContentsList;
	private JLabel remixrIconLabel;
	private JPanel contentPane;
	private JLabel playlistListLabel;
	private JButton delPlaylistBtn;
	private JLabel songListLabel;
	private JLabel instructionsLabel;
	private JScrollPane songListScrollPane;
	private JScrollPane fileContentsScrollPane;
	private JLabel songDetailsLabel;
	private JLabel genreLabel;
	private JLabel yearReleasedLabel;

	DefaultListModel songNames;
	DefaultListModel playlistFiles;

	String[] fileNames;
	String[] col;
	private static final int OPEN_PLAYLIST = 2;
	static long clipTime;

	private Socket socket;

	private boolean isPlaying = false;
	private boolean isPause = false;
	private boolean newSongClicked = false;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					OpenPlaylist frame = new OpenPlaylist();
					frame.setVisible(true);
					frame.setTitle("Remixr");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public void createEvents() {

		// Action for Play button
		playBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				isPlaying = true;
				isPause = false;
				if (isPlaying == false || newSongClicked == false) {
					PlayMusic.play();
				} else {
					PlayMusic.stop();
					PlayMusic.setClipTimetoZero();
					PlayMusic.play();
				}
			}
		});

		// Action for Pause button
		pauseBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				newSongClicked = false;
				if (!isPause) {
					isPlaying = false;
					isPause = true;
					PlayMusic.pause();
				}
			}
		});

		// Action for Stop button
		stopBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				isPlaying = false;
				isPause = false;
				PlayMusic.stop();
			}
		});

		// Action when a song from a playlist is selected in the right JList
		playlistFilesList.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				System.out.println(playlistFilesList.getSelectedValue().toString());
				String selectedFileName = playlistFilesList.getSelectedValue().toString();

				try {
					File selectedFile = new File(
							System.getProperty("user.dir") + "\\Playlists\\" + selectedFileName + ".txt");
					FileReader fr = new FileReader(selectedFile);
					BufferedReader br = new BufferedReader(fr);
					Vector<String> lines = new Vector<String>();

					String row;
					DefaultListModel fileEntries = new DefaultListModel();
					while ((row = br.readLine()) != null) {
						fileEntries.addElement(row);
					}
					br.close();
					fr.close();
					fileContentsList.setModel(fileEntries);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		// Action when a playlist file is selected in the left JList
		fileContentsList.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				newSongClicked = true;

				String song = fileContentsList.getSelectedValue().toString();
				new Thread(new Runnable() {
					String songOut = "";

					@Override
					public void run() {
						try {
							songOut = getSongFileFromServer(song);
							System.out.println(songOut);
						} catch (IOException e) {

							e.printStackTrace();
						}
					}
				}).start();

			}
		});

		delPlaylistBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String selectedFileName = playlistFilesList.getSelectedValue().toString();

				File selectedFile = new File(
						System.getProperty("user.dir") + "\\Playlists\\" + selectedFileName + ".txt");
				if (selectedFile.exists()) {
					int response = JOptionPane.showConfirmDialog(null, "Confirm delete " + selectedFileName + "?",
							"Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
					if (response == JOptionPane.YES_OPTION) {
						System.out.println("Deleted Playlist: " + selectedFile.delete());
						loadData();
					} else {
						return;
					}
				}

			}
		});
	}

	public void loadData() {

		File folder = new File(System.getProperty("user.dir") + "\\playlists\\");

		if (folder.isDirectory()) {
			fileNames = folder.list();
		} else {
			JOptionPane.showMessageDialog(null,
					"The Playlists folder does not exist in " + System.getProperty("user.dir"));
		}

		playlistFiles = new DefaultListModel<>();

		System.out.println(fileNames.length);

		for (int x = 0; x < fileNames.length; x++) {
			if (fileNames[x] != null) {
				String temp = fileNames[x].replace(".txt", "");
				fileNames[x] = temp;
			}
		}

		for (int x = 0; x < fileNames.length; x++) {
			playlistFiles.addElement(fileNames[x]);
		}
		playlistFilesList.setModel(playlistFiles);
	}

	public void initComponents() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 705, 501);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(255, 255, 255));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);

		playBtn = new JButton();
		try {
			Image playImg = ImageIO.read(getClass().getResource("Images/play.png"));
			playBtn.setIcon(new ImageIcon(playImg));
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}

		pauseBtn = new JButton();
		try {
			Image pauseImg = ImageIO.read(getClass().getResource("Images/pause.png"));
			pauseBtn.setIcon(new ImageIcon(pauseImg));
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}

		stopBtn = new JButton("");
		try {
			Image stopImg = ImageIO.read(getClass().getResource("Images/stop.png"));
			stopBtn.setIcon(new ImageIcon(stopImg));
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}

		songListScrollPane = new JScrollPane();
		fileContentsScrollPane = new JScrollPane();

		remixrIconLabel = new JLabel("");
		try {
			Image remixrIconImg = new ImageIcon(this.getClass().getResource("Images/remixr.png")).getImage();
			remixrIconLabel.setIcon(new ImageIcon(remixrIconImg));
		} catch (Exception e) {
			e.printStackTrace();
		}

		playlistListLabel = new JLabel("Playlists:");
		playlistListLabel.setFont(new Font("Tahoma", Font.BOLD, 14));

		delPlaylistBtn = new JButton("Delete Playlist");
		delPlaylistBtn.setForeground(new Color(204, 102, 0));
		delPlaylistBtn.setFont(new Font("Tahoma", Font.PLAIN, 14));

		songListLabel = new JLabel("Playlist Songs:");
		songListLabel.setFont(new Font("Tahoma", Font.BOLD, 14));

		instructionsLabel = new JLabel("<html><p>Instructions:</p>"
				+ "<p>- Click on a playlist from the left pane and click the song you want to play on the right pane.</>"
				+ "<p>- To delete a playlist, click on the playlist name and click on the Delete Playlist button.</></html>");

		Image remixrIconImg = new ImageIcon(this.getClass().getResource("Images/remixr.png")).getImage();
		OpenPlaylist frame = OpenPlaylist.this;
		frame.setIconImage(remixrIconImg);

		songDetailsLabel = new JLabel("Song Details:");
		songDetailsLabel.setFont(new Font("Tahoma", Font.BOLD, 14));

		genreLabel = new JLabel("Genre: ");
		genreLabel.setFont(new Font("Tahoma", Font.PLAIN, 12));

		yearReleasedLabel = new JLabel("Year Released: ");
		yearReleasedLabel.setFont(new Font("Tahoma", Font.PLAIN, 12));

		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(gl_contentPane.createParallelGroup(Alignment.LEADING).addGroup(gl_contentPane
				.createSequentialGroup()
				.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addComponent(remixrIconLabel, GroupLayout.PREFERRED_SIZE, 68, GroupLayout.PREFERRED_SIZE)
						.addGroup(gl_contentPane.createSequentialGroup().addContainerGap().addGroup(gl_contentPane
								.createParallelGroup(Alignment.LEADING, false)
								.addComponent(delPlaylistBtn, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE,
										Short.MAX_VALUE)
								.addComponent(songListScrollPane, GroupLayout.DEFAULT_SIZE, 148, Short.MAX_VALUE)
								.addComponent(playlistListLabel))
								.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING).addGroup(gl_contentPane
										.createSequentialGroup()
										.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
												.addGroup(gl_contentPane.createSequentialGroup()
														.addComponent(playBtn, GroupLayout.PREFERRED_SIZE, 73,
																GroupLayout.PREFERRED_SIZE)
														.addPreferredGap(ComponentPlacement.RELATED, 25,
																Short.MAX_VALUE)
														.addComponent(pauseBtn, GroupLayout.PREFERRED_SIZE, 79,
																GroupLayout.PREFERRED_SIZE)
														.addGap(26).addComponent(stopBtn, GroupLayout.PREFERRED_SIZE,
																84, GroupLayout.PREFERRED_SIZE))
												.addComponent(fileContentsScrollPane, GroupLayout.PREFERRED_SIZE, 287,
														GroupLayout.PREFERRED_SIZE))
										.addPreferredGap(ComponentPlacement.UNRELATED)
										.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING, false)
												.addComponent(yearReleasedLabel, GroupLayout.DEFAULT_SIZE,
														GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
												.addComponent(genreLabel, GroupLayout.DEFAULT_SIZE,
														GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
										.addGap(106))
										.addGroup(gl_contentPane.createSequentialGroup().addComponent(songListLabel)
												.addGap(198).addComponent(songDetailsLabel))))
						.addGroup(gl_contentPane.createSequentialGroup().addContainerGap()
								.addComponent(instructionsLabel, GroupLayout.DEFAULT_SIZE, 765, Short.MAX_VALUE)))
				.addContainerGap()));
		gl_contentPane.setVerticalGroup(gl_contentPane.createParallelGroup(Alignment.LEADING).addGroup(gl_contentPane
				.createSequentialGroup()
				.addComponent(remixrIconLabel, GroupLayout.PREFERRED_SIZE, 66, GroupLayout.PREFERRED_SIZE)
				.addPreferredGap(ComponentPlacement.UNRELATED)
				.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(playlistListLabel, GroupLayout.PREFERRED_SIZE, 24, GroupLayout.PREFERRED_SIZE)
						.addComponent(songListLabel, GroupLayout.PREFERRED_SIZE, 20, GroupLayout.PREFERRED_SIZE)
						.addComponent(songDetailsLabel))
				.addPreferredGap(ComponentPlacement.RELATED)
				.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addComponent(songListScrollPane, GroupLayout.PREFERRED_SIZE, 205, GroupLayout.PREFERRED_SIZE)
						.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
								.addComponent(fileContentsScrollPane, GroupLayout.PREFERRED_SIZE, 205,
										GroupLayout.PREFERRED_SIZE)
								.addGroup(gl_contentPane.createSequentialGroup().addComponent(genreLabel).addGap(10)
										.addComponent(yearReleasedLabel))))
				.addPreferredGap(ComponentPlacement.RELATED)
				.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addComponent(stopBtn, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(delPlaylistBtn, GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)
						.addComponent(pauseBtn, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(playBtn, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
				.addPreferredGap(ComponentPlacement.RELATED).addComponent(instructionsLabel).addGap(46)));
		fileContentsList = new JList();
		fileContentsScrollPane.setViewportView(fileContentsList);
		fileContentsList.setBackground(new Color(255, 255, 153));

		playlistFilesList = new JList();
		playlistFilesList.setBackground(new Color(255, 255, 153));
		songListScrollPane.setViewportView(playlistFilesList);
		contentPane.setLayout(gl_contentPane);
	}

	/**
	 * Create the frame.
	 */
	public OpenPlaylist() {

		try {
			socket = new Socket(RemixrClient.IP_ADDRESS, RemixrClient.PORT);
		} catch (IOException e) {
			e.printStackTrace();
		}
		initComponents(); // Loads all the screen elements into the JFrame
		loadData(); // Loads all the playlist files in the into the left JList.
		createEvents(); // Houses all the events when a user clicks the JLists or the buttons in the
						// screen.
	}

	public String getSongFileFromServer(String song) throws UnknownHostException, IOException {

		String songReceived;
		BufferedWriter out = new BufferedWriter(new PrintWriter(socket.getOutputStream()));
		BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

		out.write(RemixrClient.OPEN_PLAYLIST);
		out.flush();
		System.out.println("Message " + RemixrClient.OPEN_PLAYLIST + " sent to server");
		out.write(song + "\n");
		out.flush();
		System.out.println(song + " sent to server");
		songReceived = in.readLine();
		System.out.println("SONG RECEIVED: " + songReceived);

		Socket file_socket = new Socket(RemixrClient.IP_ADDRESS, RemixrClient.FILE_PORT);

		InputStream is = file_socket.getInputStream();
		File output = new File(".\\outputs\\songToPlay.wav");
		FileOutputStream fo;
		if (output.exists()) {
			System.out.println("true : file exits");
			Boolean deleted = output.delete();
			System.out.println("Deleted: " + deleted);
			fo = new FileOutputStream(output);
		} else {
			System.out.println("false :  file doesn't exit");
			fo = new FileOutputStream(output);
		}

		stream(is, fo);
		file_socket.close();
		readDetails(songReceived);
		return songReceived;

	}

	private void readDetails(String songReceived) {
		try {
			File file = new File(System.getProperty("user.dir") + "\\song_details.txt");
			Scanner sc = new Scanner(file);

			while (sc.hasNextLine()) {
				col = sc.nextLine().toString().split(",");
				String genre = col[1];
				String date = col[2];
				if (col[0].equals(songReceived)) {
					System.out.println("Genre " + genre + " Date: " + date);
					genreLabel.setText("Genre: " + genre);
					yearReleasedLabel.setText("Year Released: " + date);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void stream(InputStream in, OutputStream out) throws IOException {
		byte[] buf = new byte[8192];
		int bytesRead = 0;

		try {

			while (-1 != (bytesRead = in.read(buf, 0, buf.length))) {
				out.write(buf, 0, bytesRead);
			}
		} catch (IOException e) {
			System.out.println("Error with streaming op: " + e.getMessage());
			throw (e);
		} finally {

			in.close();
			out.flush();
			out.close();

		}
	}
}
