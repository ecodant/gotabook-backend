package datastructures;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

import org.bson.types.ObjectId;

public class ReaderAffinityGraph {
	// Adjacency list representation
	private Map<ObjectId, List<Edge>> adjacencyList;

	// Edge class
	public static class Edge {
		private ObjectId destination;
		private double weight; // Similarity weight

		public Edge(ObjectId destination, double weight) {
			this.destination = destination;
			this.weight = weight;
		}

		public ObjectId getDestination() {
			return destination;
		}

		public double getWeight() {
			return weight;
		}
	}

	// Constructor
	public ReaderAffinityGraph() {
		this.adjacencyList = new HashMap<>();
	}

	// Add a user to the graph
	public void addUser(ObjectId userId) {
		if (!adjacencyList.containsKey(userId)) {
			adjacencyList.put(userId, new ArrayList<>());
		}
	}

	// Add a connection between users with a similarity weight
	public void addConnection(ObjectId userId1, ObjectId userId2, double similarityWeight) {
		// Add users if they don't exist
		addUser(userId1);
		addUser(userId2);

		// Add edges in both directions (undirected graph)
		adjacencyList.get(userId1).add(new Edge(userId2, similarityWeight));
		adjacencyList.get(userId2).add(new Edge(userId1, similarityWeight));
	}

	// Remove a connection between users
	public void removeConnection(ObjectId userId1, ObjectId userId2) {
		if (adjacencyList.containsKey(userId1)) {
			adjacencyList.get(userId1).removeIf(edge -> edge.getDestination().equals(userId2));
		}
		if (adjacencyList.containsKey(userId2)) {
			adjacencyList.get(userId2).removeIf(edge -> edge.getDestination().equals(userId1));
		}
	}

	// Get all connections for a user
	public List<Edge> getConnections(ObjectId userId) {
		return adjacencyList.getOrDefault(userId, new ArrayList<>());
	}

	// Check if two users are connected
	public boolean areConnected(ObjectId userId1, ObjectId userId2) {
		if (!adjacencyList.containsKey(userId1) || !adjacencyList.containsKey(userId2)) {
			return false;
		}

		for (Edge edge : adjacencyList.get(userId1)) {
			if (edge.getDestination().equals(userId2)) {
				return true;
			}
		}
		return false;
	}

	// Find shortest path between two users (Dijkstra's algorithm)
	public List<ObjectId> findShortestPath(ObjectId start, ObjectId end) {
		// Priority queue for Dijkstra's algorithm
		PriorityQueue<Node> pq = new PriorityQueue<>(Comparator.comparingDouble(Node::getDistance));

		// Distance map
		Map<ObjectId, Double> distance = new HashMap<>();

		// Previous node map for path reconstruction
		Map<ObjectId, ObjectId> previous = new HashMap<>();

		// Initialize distances
		for (ObjectId userId : adjacencyList.keySet()) {
			distance.put(userId, Double.POSITIVE_INFINITY);
			previous.put(userId, null);
		}

		// Start node has distance 0
		distance.put(start, 0.0);
		pq.add(new Node(start, 0.0));

		// Process nodes
		while (!pq.isEmpty()) {
			Node current = pq.poll();
			ObjectId currentId = current.getUserId();

			// If we reached the end
			if (currentId.equals(end)) {
				break;
			}

			// If we've found a longer path
			if (current.getDistance() > distance.get(currentId)) {
				continue;
			}

			// Process neighbors
			for (Edge edge : adjacencyList.get(currentId)) {
				ObjectId neighborId = edge.getDestination();
				double newDistance = distance.get(currentId) + edge.getWeight();

				// If we found a shorter path
				if (newDistance < distance.get(neighborId)) {
					distance.put(neighborId, newDistance);
					previous.put(neighborId, currentId);
					pq.add(new Node(neighborId, newDistance));
				}
			}
		}

		// Reconstruct path
		List<ObjectId> path = new ArrayList<>();
		ObjectId current = end;

		// If no path exists
		if (previous.get(end) == null && !start.equals(end)) {
			return path; // Empty path
		}

		// Build path from end to start
		while (current != null) {
			path.add(current);
			current = previous.get(current);
		}

		// Reverse to get path from start to end
		Collections.reverse(path);
		return path;
	}

	// Node class for Dijkstra's algorithm
	private static class Node {
		private ObjectId userId;
		private double distance;

		public Node(ObjectId userId, double distance) {
			this.userId = userId;
			this.distance = distance;
		}

		public ObjectId getUserId() {
			return userId;
		}

		public double getDistance() {
			return distance;
		}
	}

	// Find all users within n connections (breadth-first search)
	public Set<ObjectId> findUsersWithinDistance(ObjectId start, int maxDistance) {
		Set<ObjectId> visited = new HashSet<>();
		Queue<ObjectId> queue = (Queue<ObjectId>) new LinkedListSimple();
		Map<ObjectId, Integer> distances = new HashMap<>();

		// Start with the given user
		queue.add(start);
		visited.add(start);
		distances.put(start, 0);

		while (!queue.isEmpty()) {
			ObjectId current = queue.poll();
			int currentDistance = distances.get(current);

			// If we've reached the maximum distance
			if (currentDistance >= maxDistance) {
				continue;
			}

			// Process neighbors
			for (Edge edge : adjacencyList.get(current)) {
				ObjectId neighbor = edge.getDestination();

				if (!visited.contains(neighbor)) {
					visited.add(neighbor);
					queue.add(neighbor);
					distances.put(neighbor, currentDistance + 1);
				}
			}
		}

		// Remove the start user
		visited.remove(start);
		return visited;
	}

	// Detect clusters using breadth-first search
	public List<Set<ObjectId>> detectClusters() {
		List<Set<ObjectId>> clusters = new ArrayList<>();
		Set<ObjectId> visited = new HashSet<>();

		// Process all users
		for (ObjectId userId : adjacencyList.keySet()) {
			if (!visited.contains(userId)) {
				// Find all connected users
				Set<ObjectId> cluster = new HashSet<>();
				Queue<ObjectId> queue = (Queue<ObjectId>) new LinkedListSimple();

				queue.add(userId);
				visited.add(userId);
				cluster.add(userId);

				while (!queue.isEmpty()) {
					ObjectId current = queue.poll();

					for (Edge edge : adjacencyList.get(current)) {
						ObjectId neighbor = edge.getDestination();

						if (!visited.contains(neighbor)) {
							visited.add(neighbor);
							queue.add(neighbor);
							cluster.add(neighbor);
						}
					}
				}

				clusters.add(cluster);
			}
		}

		return clusters;
	}

	// Get all users in the graph
	public Set<ObjectId> getAllUsers() {
		return adjacencyList.keySet();
	}

	// Get the number of connections for a user
	public int getConnectionCount(ObjectId userId) {
		if (!adjacencyList.containsKey(userId)) {
			return 0;
		}
		return adjacencyList.get(userId).size();
	}

	// Get users with most connections
	public List<ObjectId> getUsersWithMostConnections(int limit) {
		return adjacencyList.entrySet().stream()
				.sorted((a, b) -> Integer.compare(b.getValue().size(), a.getValue().size())).limit(limit)
				.map(Map.Entry::getKey).collect(java.util.stream.Collectors.toList());
	}
}