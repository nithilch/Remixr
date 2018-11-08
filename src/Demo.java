import java.io.File;

public class Demo {
	
	public static void main(String[] args) {
		MLinkedList<File> musicList = new MLinkedList<File>();
		
		File folder = new File("D:\\DOUGLAS COLLEGE\\2018 5th Semester\\Data Structures & Algorithms CSIS 3475-001\\Project");
		File[] files = folder.listFiles();
		
		for (File file : files){
			if(file.isFile()) {
				musicList.addLast(file);
			}
		}
		System.out.println(musicList);
		MergeSort.sort(musicList);
	}
}
