import java.util.NoSuchElementException;


public class MLinkedList<T> {

	// NODE CLASS AND CONSTRUCTOR
	class Node {
		T value;
		Node prev, next;

		Node(T value, Node prev, Node next) {
			this.value = value;
			this.prev = prev;
			this.next = next;
		}

		Node(T val) {
			this(val, null, null);
		}

	}

	Node first, last;

	public MLinkedList() {
		first = null;
		last = null;
	}
	// CHECKS IF THE LINKEDLIST IS EMPTY

	public boolean isEmpty() {
		return first == null;
	}

	// CHECKS THE SIZE OF THE LINKEDLIST

	public int size() {
		int count = 0;
		Node tempNode = first;

		while (tempNode != null) {
			count++;
			tempNode = tempNode.next;
		}
		return count;
	}

	public void addFirst(T val) {
		Node temp = new Node(val, null, first);
		if (first != null) {
			first.prev = temp;
		}
		first = temp;
		if (last == null) {
			last = temp;
		}
	}

	public void addLast(T val) {
		Node temp = new Node(val, last, null);
		if (last != null) {
			last.next = temp;
		}
		last = temp;
		if (first == null) {
			first = temp;
		}
	}

	public T removeFirst() {
		if (isEmpty())
			throw new NoSuchElementException();
		Node tmp = first;
		first = first.next;
		first.prev = null;
		return tmp.value;
	}

	public T removeLast() {
		if (isEmpty())
			throw new NoSuchElementException();
		Node tmp = last;
		last = last.prev;
		last.next = null;
		return tmp.value;

	}

	@Override
	public String toString() {
		StringBuilder strBuilder = new StringBuilder();
		
	      Node p = first;
	      while (p != null)
	      {
	         strBuilder.append(p.value + "\n"); 
	         p = p.next;
	      }      
	      return strBuilder.toString(); 
	}
	
	
}
