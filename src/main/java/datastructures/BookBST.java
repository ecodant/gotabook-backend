package datastructures;

import library.models.Book;

public class BookBST {
	private BookNode root;

	// Node class for BST
	public class BookNode {
		private Book book;
		private BookNode left, right;
		private int height; // For AVL balancing

		public BookNode(Book book) {
			this.book = book;
			this.left = null;
			this.right = null;
			this.height = 1; // Initial height
		}
	}

	// Constructor
	public BookBST() {
		this.root = null;
	}

	// Get height of node (for AVL)
	private int height(BookNode node) {
		if (node == null) {
			return 0;
		}
		return node.height;
	}

	// Get balance factor (for AVL)
	private int getBalance(BookNode node) {
		if (node == null) {
			return 0;
		}
		return height(node.left) - height(node.right);
	}

	// Update height of node
	private void updateHeight(BookNode node) {
		if (node != null) {
			node.height = Math.max(height(node.left), height(node.right)) + 1;
		}
	}

	// Right rotation
	private BookNode rightRotate(BookNode y) {
		BookNode x = y.left;
		BookNode T2 = x.right;

		// Perform rotation
		x.right = y;
		y.left = T2;

		// Update heights
		updateHeight(y);
		updateHeight(x);

		return x;
	}

	// Left rotation
	private BookNode leftRotate(BookNode x) {
		BookNode y = x.right;
		BookNode T2 = y.left;

		// Perform rotation
		y.left = x;
		x.right = T2;

		// Update heights
		updateHeight(x);
		updateHeight(y);

		return y;
	}

	// Insert a book
	public void insert(Book book) {
		root = insertNode(root, book);
	}

	// Insert helper method
	private BookNode insertNode(BookNode node, Book book) {
		// Standard BST insert
		if (node == null) {
			return new BookNode(book);
		}

		// Compare by title (can be changed to any other attribute)
		int compareResult = book.getTitle().compareToIgnoreCase(node.book.getTitle());

		if (compareResult < 0) {
			node.left = insertNode(node.left, book);
		} else if (compareResult > 0) {
			node.right = insertNode(node.right, book);
		} else {
			// Duplicate titles not allowed (can be modified as needed)
			return node;
		}

		// Update height
		updateHeight(node);

		// Get balance factor
		int balance = getBalance(node);

		// Left Left Case
		if (balance > 1 && book.getTitle().compareToIgnoreCase(node.left.book.getTitle()) < 0) {
			return rightRotate(node);
		}

		// Right Right Case
		if (balance < -1 && book.getTitle().compareToIgnoreCase(node.right.book.getTitle()) > 0) {
			return leftRotate(node);
		}

		// Left Right Case
		if (balance > 1 && book.getTitle().compareToIgnoreCase(node.left.book.getTitle()) > 0) {
			node.left = leftRotate(node.left);
			return rightRotate(node);
		}

		// Right Left Case
		if (balance < -1 && book.getTitle().compareToIgnoreCase(node.right.book.getTitle()) < 0) {
			node.right = rightRotate(node.right);
			return leftRotate(node);
		}

		return node;
	}

	// Search for a book by title
	public Book search(String title) {
		BookNode result = searchNode(root, title);
		return result != null ? result.book : null;
	}

	// Search helper method
	private BookNode searchNode(BookNode node, String title) {
		if (node == null || node.book.getTitle().equalsIgnoreCase(title)) {
			return node;
		}

		if (title.compareToIgnoreCase(node.book.getTitle()) < 0) {
			return searchNode(node.left, title);
		} else {
			return searchNode(node.right, title);
		}
	}

	// Delete a book
	public void delete(String title) {
		root = deleteNode(root, title);
	}

	// Delete helper method
	@SuppressWarnings("unused")
	private BookNode deleteNode(BookNode node, String title) {
		if (node == null) {
			return null;
		}

		int compareResult = title.compareToIgnoreCase(node.book.getTitle());

		if (compareResult < 0) {
			node.left = deleteNode(node.left, title);
		} else if (compareResult > 0) {
			node.right = deleteNode(node.right, title);
		} else {
			// Node with only one child or no child
			if (node.left == null) {
				return node.right;
			} else if (node.right == null) {
				return node.left;
			}

			// Node with two children: Get the inorder successor
			node.book = minValue(node.right).book;

			// Delete the inorder successor
			node.right = deleteNode(node.right, node.book.getTitle());
		}

		// If tree had only one node
		if (node == null) {
			return null;
		}

		// Update height
		updateHeight(node);

		// Get balance factor
		int balance = getBalance(node);

		// Left Left Case
		if (balance > 1 && getBalance(node.left) >= 0) {
			return rightRotate(node);
		}

		// Left Right Case
		if (balance > 1 && getBalance(node.left) < 0) {
			node.left = leftRotate(node.left);
			return rightRotate(node);
		}

		// Right Right Case
		if (balance < -1 && getBalance(node.right) <= 0) {
			return leftRotate(node);
		}

		// Right Left Case
		if (balance < -1 && getBalance(node.right) > 0) {
			node.right = rightRotate(node.right);
			return leftRotate(node);
		}

		return node;
	}

	// Find the node with minimum value
	private BookNode minValue(BookNode node) {
		BookNode current = node;

		while (current.left != null) {
			current = current.left;
		}

		return current;
	}

	// In-order traversal to get all books sorted by title
	public void inOrderTraversal(BookNode node, java.util.List<Book> result) {
		if (node != null) {
			inOrderTraversal(node.left, result);
			result.add(node.book);
			inOrderTraversal(node.right, result);
		}
	}

	// Get all books sorted by title
	public java.util.List<Book> getAllBooks() {
		java.util.List<Book> books = new java.util.ArrayList<>();
		inOrderTraversal(root, books);
		return books;
	}

	// Get root node
	public BookNode getRoot() {
		return root;
	}
}