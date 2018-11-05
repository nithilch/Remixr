import java.io.File;

public class Demo {
	
	public static void main(String[] args) {
		MLinkedList<File> musicList = new MLinkedList<File>();
		
		File folder = new File(args[0]);
		File[] files = folder.listFiles();
		
		for (File file : files){
			if(file.isFile()) {
				musicList.addLast(file);
			}
		}
		System.out.println(musicList);
	}
}
