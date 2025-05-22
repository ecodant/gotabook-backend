package datastructures;

public class LinkedListSimple<T> {
	private Node<T> head;
	private int size;

	// Node class
	public static class Node<T> {
		private T data;
		private Node<T> next;

		public Node(T data) {
			this.data = data;
			this.next = null;
		}

		public T getData() {
			return data;
		}

		public Node<T> getNext() {
			return next;
		}
	}


	public LinkedListSimple() {
		this.head = null;
		this.size = 0;
	}

	// Add element to the end
	public void add(T data) {
		Node<T> newNode = new Node<>(data);

		if (head == null) {
			head = newNode;
		} else {
			Node<T> current = head;
			while (current.next != null) {
				current = current.next;
			}
			current.next = newNode;
		}
		size++;
	}

	// Add element at the beginning
	public void addFirst(T data) {
		Node<T> newNode = new Node<>(data);
		newNode.next = head;
		head = newNode;
		size++;
	}


	public boolean remove(T data) {
		if (head == null) {
			return false;
		}

		if (head.data.equals(data)) {
			head = head.next;
			size--;
			return true;
		}

		Node<T> current = head;
		while (current.next != null && !current.next.data.equals(data)) {
			current = current.next;
		}

		if (current.next != null) {
			current.next = current.next.next;
			size--;
			return true;
		}

		return false;
	}

	// Get element at index
	public T get(int index) {
		if (index < 0 || index >= size) {
			throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
		}

		Node<T> current = head;
		for (int i = 0; i < index; i++) {
			current = current.next;
		}

		return current.data;
	}


	public int size() {
		return size;
	}


	public boolean isEmpty() {
		return size == 0;
	}

	// Convert to list
	public java.util.List<T> toList() {
		java.util.List<T> list = new java.util.ArrayList<>();
		Node<T> current = head;

		while (current != null) {
			list.add(current.data);
			current = current.next;
		}

		return list;
	}

	public Node<T> getHead() {
		return head;
	}
}
