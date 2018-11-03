
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
		
	}
}

