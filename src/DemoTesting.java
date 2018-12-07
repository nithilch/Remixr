// Nithil Chigullapally 300288453
// Joel Calo 300279569

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class DemoTesting {

	public static AVLTree<String> musicTree = new AVLTree<String>();

	public static void main(String[] args) throws IOException {

		File folder = new File(".\\wav_files");
		File[] files = folder.listFiles();

		for (File file : files) {
			if (file.isFile()) {
				musicTree.insert(file.getPath());
			}
		}


		musicTree.printInOrder();
		System.out.println(musicTree.size());

		String[] songs = musicTree.toStringArray();
		System.out.println(Arrays.toString(songs));

	}
}
