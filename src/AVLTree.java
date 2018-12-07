// Nithil Chigullapally 300288453
// Joel Calo 300279569

class AVLTree<T extends Comparable<T>> {

	public AVLTree() {
		this.root = null;
	}

	// node class where each node has left and right nodes
	class Node<T> {
		T data;
		int height;
		Node<T> left, right;

		Node(T data) {
			this.data = data;
			this.height = 1;
		}
	}

	// root of the tree
	Node<T> root;

	// returns height of the tree
	private int height(Node<T> node) {
		if (node == null)
			return 0;

		return node.height;
	}

	// returns max between two data values
	private int max(int a, int b) {
		return (a > b) ? a : b;
	}

	// rotate the subtree right
	private Node<T> rotateRight(Node<T> b) {
		Node<T> a = b.left;
		Node<T> l = a.right;

		a.right = b;
		b.left = l;

		b.height = max(height(b.left), height(b.right)) + 1;
		a.height = max(height(a.left), height(a.right)) + 1;

		return a;

	}

	// rotate the subtree left
	private Node<T> rotateLeft(Node<T> a) {
		Node<T> b = a.right;
		Node<T> l = b.left;

		b.left = a;
		a.right = l;

		a.height = max(height(a.left), height(a.right)) + 1;
		b.height = max(height(b.left), height(b.right)) + 1;

		return b;

	}

	// returns balance factor
	private int getBalance(Node<T> node) {
		if (node == null)
			return 0;

		return height(node.left) - height(node.right);
	}

	private Node<T> insert(Node<T> node, T data) {

		// Base case: if root is null, create a new node and add data to the root node
		if (node == null) {
			Node<T> newNode = new Node<T>(data);
			return newNode;
		}

		// Recursion down the tree: inserts in the BinarySearchTree by comparing data
		// using compareTo
		if (data.compareTo(node.data) > 0) {
			node.right = insert(node.right, data);
		} else if (data.compareTo(node.data) < 0) {
			node.left = insert(node.left, data);
		} else {
			return node;
		}

		// updating height
		node.height = 1 + (max(height(node.left), height(node.right)));

		// check if node became unbalanced
		int balance = getBalance(node);

		// if node is unbalanced, four cases to consider
		// LEFT LEFT CASE
		if (balance > 1 && data.compareTo(node.left.data) < 0) {
			return rotateRight(node);
		}
		// RIGHT RIGHT CASE
		if (balance < -1 && data.compareTo(node.right.data) > 0) {
			return rotateLeft(node);
		}

		// LEFT RIGHT CASE
		if (balance > 1 && data.compareTo(node.left.data) > 0) {
			node.left = rotateLeft(node.left);
			return rotateRight(node);
		}
		// RIGHT LEFT CASE
		if (balance < -1 && data.compareTo(node.right.data) < 0) {
			node.right = rotateRight(node.right);
			return rotateLeft(node);
		}

		return node;

	}

	// adds a node to the binary search tree by calling the other insert function
	public void insert(T data) {
		root = insert(root, data);
	}

	private Node<T> delete(Node<T> node, T data) {

		// Base case: if the root node is null, then nothing to delete
		if (node == null) {
			return null;
		}

		// Recursion down the tree: to find the data to delete
		if (data.compareTo(node.data) > 0) {
			node.left = delete(node.left, data);
		} else if (data.compareTo(node.data) < 0) {
			node.right = delete(node.right, data);
		}

		// if data is same as node.data, then this data needs to be deleted
		else {

			// node has only one child or no child
			if (node.left == null)
				return node.right;
			else if (node.right == null)
				return node.left;

			node.data = findMin(node.right);

			node.right = delete(node.right, node.data);
		}

		// updates height of current node
		root.height = max(height(root.left), height(root.right)) + 1;

		// check whether this node became unbalanced
		int balance = getBalance(root);

		// If this node becomes unbalanced, there are 4 cases

		// LEFT LEFT CASE
		if (balance > 1 && getBalance(root.left) >= 0)
			return rotateRight(root);

		// LEFT RIGHT CASE
		if (balance > 1 && getBalance(root.left) < 0) {
			root.left = rotateLeft(root.left);
			return rotateRight(root);
		}

		// RIGHT RIGHT CASE
		if (balance < -1 && getBalance(root.right) <= 0)
			return rotateLeft(root);

		// RIGHT LEFT CASE
		if (balance < -1 && getBalance(root.right) > 0) {
			root.right = rotateRight(root.right);
			return rotateLeft(root);
		}

		return node;

	}

	private T findMin(Node<T> node) {
		T minData = node.data;
		while (root.left != null) {
			minData = node.left.data;
			node = node.left;
		}
		return minData;
	}

	private T findMax(Node<T> node) {
		T maxData = node.data;
		while (root.right != null) {
			maxData = node.right.data;
			node = node.right;
		}
		return maxData;

	}

	public void delete(T data) {
		root = delete(root, data);
	}

	private T search(Node<T> node, T data) {
		if (node == null) {
			return null;
		}

		else if (data.compareTo(node.data) > 0)
			return search(node.right, data);
		else if (data.compareTo(node.data) < 0)
			return search(node.left, data);

		else {
			return node.data;
		}
	}

	public T search(T data) {
		return search(root, data);
	}

	private void printInOrder(Node<T> node) { // in-order transversal
		if (node == null)
			return;

		printInOrder(node.left);
		System.out.println(node.data + " ");
		printInOrder(node.right);
	}

	public void printInOrder() {
		printInOrder(root);
	}

	private void printPreOrder(Node<T> node) { // pre-order transversal
		if (node == null)
			return;

		System.out.println(node.data + "");
		printPreOrder(node.left);
		printPreOrder(node.right);
	}

	public void printPreOrder() {
		printPreOrder(root);
	}

	public int size() {
		return size(root);
	}

	private int size(Node<T> node) {
		if (node == null)
			return 0;
		else {
			return size(node.left) + 1 + size(node.right);
		}
	}

	public String[] toStringArray() {
		String[] arr = new String[size()];
		int index = 0;
		toStringArrayInOrder(root, arr, index);
		return arr;
	}

	private int toStringArrayInOrder(Node<T> node, String[] arr, int index) { // InOrder

		if (node.left != null) {
			index = toStringArrayInOrder(node.left, arr, index);
		}

		arr[index++] = node.data.toString();

		if (node.right != null) {
			index = toStringArrayInOrder(node.right, arr, index);
		}

		return index;
	}

	private int toStringArrayPreOrder(Node<T> node, String[] arr, int index) { // PreOrder

		if (node.left != null) {
			index = toStringArrayPreOrder(node.left, arr, index);
		}

		if (node.right != null) {
			index = toStringArrayPreOrder(node.right, arr, index);
		}
		
		arr[index] = node.data.toString();

		return index + 1;
	}

}
