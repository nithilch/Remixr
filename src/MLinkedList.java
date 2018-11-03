
public class MLinkedList<T> {
	
	private class Node<T>{
		T value;
		Node prev, next;
		
		public Node(T value, Node prev, Node next) {
			this.value = value;
			this.prev = prev;
			this.next = next;
		}
		
		public Node(T value) {
			this(value, null, null);
		}
		
		Node first = null;
		Node last = null;
		
		
		//CHECKS IF THE LINKEDLIST IS EMPTY
		
		public boolean isEmpty() {
			return first == null;
		}
		
		
		//CHECKS THE SIZE OF THE LINKEDLIST
		
		public int checkListSize() {
			int count = 0;
			Node tempNode = first;
			
			while(tempNode != null) {
				count++;
				tempNode = tempNode.next;
			}
			return count;
		}
		
		
		//ADDNODE METHODS
		
		public void addNode(int index, T val) {
			if(index < 0 || index > checkListSize()) {
				System.out.println("Index " + index + " is out of bounds");
			}
			
			if(index == 0)
			{
				first = new Node(val);
				if(last == null) {
				last = first;
				}
			}
		}
		
		public void addNode(T val) {
			
		}
	}
}

