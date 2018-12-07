
// Nithil Chigullapally 300288453
// Joel Calo 300279569

import java.net.*;
import java.util.Arrays;
import java.io.*;

public class RemixrServer {

	private static final int PORT = 6667;
	private static final int FILE_PORT = 7777; // filetesting
	public static AVLTree<String> musicTree = new AVLTree<String>();
	public static String relativeFile = ".\\wav_files\\";
	public static String wav = ".wav";
	public static String[] songs;
	private static final int EXIT = 0;
	private static final int CREATE_PLAYLIST = 1;
	private static final int OPEN_PLAYLIST = 2;

	public static void main(String[] args) {
		loadData();
		new Thread(new Runnable() {
			ServerSocket serverSocket;
			ServerSocket fileSocket; // filetesting

			@Override
			public void run() {
				try {
					serverSocket = new ServerSocket(PORT);
					System.out.println("Port: " + PORT + " is open.");
					Socket client = serverSocket.accept();
					System.out.println("Client Connected...");
					BufferedWriter out = new BufferedWriter(new PrintWriter(client.getOutputStream()));// to send
																										// integer or
																										// string
					BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
					ObjectOutputStream objectOut = new ObjectOutputStream(client.getOutputStream()); // to send array
					// File Streams
					FileInputStream fs;
					OutputStream os;

					System.out.println("Server Ready!");

					while (true) {
						int inputMessage = in.read();
						switch (inputMessage) {
						case CREATE_PLAYLIST:
							System.out.println("CREATE PLAYLIST INITIATED");
							objectOut.writeObject(songs);
							System.out.println("Server: " + songs);
							objectOut.flush();
							break;
						case OPEN_PLAYLIST:
							System.out.println("OPEN PLAYLIST INITIATED");
							String songPath;
							String songName = in.readLine();
							System.out.println("Received " + songName);
							songPath = musicTree.search(relativeFile + songName + wav);
							out.write(songName + "\n");
							out.flush();

							fileSocket = new ServerSocket(FILE_PORT); // filetesting
							System.out.println("Port: " + FILE_PORT + " is open."); // filetesting
							Socket file_client = fileSocket.accept(); // filetesting

							fs = new FileInputStream(songPath);

							os = file_client.getOutputStream();

							stream(fs, os);
							fileSocket.close();
							System.out.println("Port: " + FILE_PORT + " is closed.");// filetesting

							System.out.println("Sending completed");
							break;
						}

						if (inputMessage == EXIT) {
							break; // break for while loop
						}
					}
					objectOut.close();
					in.close();
					client.close();
					System.out.println("Client Disconnected");
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}).start();

	}

	public static void loadData() {
		File folder = new File(".\\wav_files");
		File[] files = folder.listFiles();

		for (File file : files) {
			if (file.isFile()) {
				musicTree.insert(file.getPath());
			}
		}

		songs = musicTree.toStringArray();
		System.out.println(Arrays.toString(songs));

		for (int i = 0; i < songs.length; i++) {
			if (songs[i] != null) {
				String temp1 = songs[i].replace(relativeFile, "");
				String temp2 = temp1.replace(wav, "");
				songs[i] = temp2;
			}
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
