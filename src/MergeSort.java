import java.io.File;

public class MergeSort <T>{

	static MLinkedList<File> mList;
	
	static Node head;
	
	static class Node
	{
		int data;
		Node prev;
		Node next;
		
		Node(int d)
		{
			data = d;
			prev = null;
			next = null;
		}
	}
	public static void sort(MLinkedList<File> musicList) {
		/*mList = musicList;
		
		System.out.println("First: " + mList.first.toString());
		System.out.println("Last: " + mList.last.toString());
		
		mergeSort(mList, 0, mList.size());*/
		
		Node aHead = new Node(0);
		Node dHead = new Node(0);
		splitList(aHead, dHead);
	}

	/*private static void mergeSort(MLinkedList<File> mList, int start, int end) {
		
		if(end - start < 2)
		{
			return;
		}
		
		int mid = (start + end) / 2;
		mergeSort(mList, start, mid);
		mergeSort(mList, mid, end);
		merge(mList, start, mid, end);
	}

	private static void merge(MLinkedList<File> mList, int start, int mid, int end) {
		
	}*/
	
	private static void splitList(Node aHead, Node dHead)
	{
		Node asc = aHead;
		Node desc = dHead;
		Node curr = head;
		
	}
}
