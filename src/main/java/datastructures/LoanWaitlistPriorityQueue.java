package datastructures;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.types.ObjectId;

import library.models.WaitlistEntry;

public class LoanWaitlistPriorityQueue {
	// Map to store waitlists for each book
	private Map<ObjectId, MinHeap> bookWaitlists;

	// Min Heap class for priority queue
	public class MinHeap {
		private List<WaitlistEntry> heap;

		public MinHeap() {
			heap = new ArrayList<>();
		}

		// Get parent index
		private int parent(int i) {
			return (i - 1) / 2;
		}

		// Get left child index
		private int leftChild(int i) {
			return 2 * i + 1;
		}

		// Get right child index
		private int rightChild(int i) {
			return 2 * i + 2;
		}

		// Swap two elements
		private void swap(int i, int j) {
			WaitlistEntry temp = heap.get(i);
			heap.set(i, heap.get(j));
			heap.set(j, temp);
		}

		// Insert a new entry
		public void insert(WaitlistEntry entry) {
			heap.add(entry);
			int current = heap.size() - 1;

			// Heapify up
			while (current > 0 && heap.get(current).getPriority() < heap.get(parent(current)).getPriority()) {
				swap(current, parent(current));
				current = parent(current);
			}
		}

		// Extract the highest priority entry
		public WaitlistEntry extractMin() {
			if (heap.isEmpty()) {
				return null;
			}

			WaitlistEntry min = heap.get(0);

			// Move the last item to the root and heapify down
			heap.set(0, heap.get(heap.size() - 1));
			heap.remove(heap.size() - 1);
			heapifyDown(0);

			return min;
		}

		// Heapify down
		private void heapifyDown(int i) {
			int smallest = i;
			int left = leftChild(i);
			int right = rightChild(i);
			int size = heap.size();

			if (left < size && heap.get(left).getPriority() < heap.get(smallest).getPriority()) {
				smallest = left;
			}

			if (right < size && heap.get(right).getPriority() < heap.get(smallest).getPriority()) {
				smallest = right;
			}

			if (smallest != i) {
				swap(i, smallest);
				heapifyDown(smallest);
			}
		}

		// Get all entries in the waitlist
		public List<WaitlistEntry> getAllEntries() {
			return new ArrayList<>(heap);
		}

		// Check if the heap is empty
		public boolean isEmpty() {
			return heap.isEmpty();
		}

		// Get size of the heap
		public int size() {
			return heap.size();
		}
	}

	// Constructor
	public LoanWaitlistPriorityQueue() {
		bookWaitlists = new HashMap<>();
	}

	// Add a new waitlist entry
	public void addEntry(WaitlistEntry entry) {
		if (!bookWaitlists.containsKey(entry.getBookId())) {
			bookWaitlists.put(entry.getBookId(), new MinHeap());
		}
		bookWaitlists.get(entry.getBookId()).insert(entry);
	}

	// Get the next user in line for a specific book
	public WaitlistEntry getNextWaitingUser(ObjectId bookId) {
		if (!bookWaitlists.containsKey(bookId) || bookWaitlists.get(bookId).isEmpty()) {
			return null;
		}
		return bookWaitlists.get(bookId).extractMin();
	}

	// Get all users waiting for a specific book
	public List<WaitlistEntry> getWaitlistForBook(ObjectId bookId) {
		if (!bookWaitlists.containsKey(bookId)) {
			return new ArrayList<>();
		}
		return bookWaitlists.get(bookId).getAllEntries();
	}

	// Remove a specific user from a book's waitlist
	public void removeUserFromWaitlist(ObjectId bookId, ObjectId userId) {
		if (!bookWaitlists.containsKey(bookId)) {
			return;
		}

		MinHeap heap = bookWaitlists.get(bookId);
		List<WaitlistEntry> entries = new ArrayList<>();

		// Extract all entries
		while (!heap.isEmpty()) {
			entries.add(heap.extractMin());
		}

		// Filter out the specified user
		entries.removeIf(entry -> entry.getUserId().equals(userId));

		// Reinsert all remaining entries
		for (WaitlistEntry entry : entries) {
			heap.insert(entry);
		}
	}
}
