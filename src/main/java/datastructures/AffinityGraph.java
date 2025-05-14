package datastructures;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import library.models.User;

public class AffinityGraph {
	// Adjacency list representation
	private Map<String, Map<String, Double>> adjacencyList;
	private Map<String, User> users;

	public AffinityGraph() {
		adjacencyList = new HashMap<>();
		users = new HashMap<>();
	}

	// Add a user to the graph
	public void addUser(User user) {
		String userId = user.getId();
		users.put(userId, user);

		// If user doesn't have connections yet, initialize empty map
		if (!adjacencyList.containsKey(userId)) {
			adjacencyList.put(userId, new HashMap<>());
		}
	}

	// Create or update a connection between users
	public void createConnection(String userId1, String userId2, double similarityScore) {
		// Ensure both users exist
		if (!adjacencyList.containsKey(userId1)) {
			adjacencyList.put(userId1, new HashMap<>());
		}
		if (!adjacencyList.containsKey(userId2)) {
			adjacencyList.put(userId2, new HashMap<>());
		}

		// Create bidirectional connection with similarity score
		adjacencyList.get(userId1).put(userId2, similarityScore);
		adjacencyList.get(userId2).put(userId1, similarityScore);
	}

	// Get the similarity score between two users
	public double getSimilarity(String userId1, String userId2) {
		if (!adjacencyList.containsKey(userId1) || !adjacencyList.get(userId1).containsKey(userId2)) {
			return 0.0; // No connection
		}
		return adjacencyList.get(userId1).get(userId2);
	}

	// Find similar readers to a given user
	public List<User> findSimilarReaders(String userId, int limit) {
		if (!adjacencyList.containsKey(userId)) {
			return new ArrayList<>();
		}

		// Get all connections
		Map<String, Double> connections = adjacencyList.get(userId);

		// Convert to list for sorting
		List<Map.Entry<String, Double>> connectionsList = new ArrayList<>(connections.entrySet());

		// Sort by similarity score (descending)
		connectionsList.sort((a, b) -> Double.compare(b.getValue(), a.getValue()));

		// Get top connections
		List<User> result = new ArrayList<>();
		for (int i = 0; i < Math.min(limit, connectionsList.size()); i++) {
			String connectedUserId = connectionsList.get(i).getKey();
			result.add(users.get(connectedUserId));
		}

		return result;
	}

	// Find "friends of friends" (users 2 hops away)
	public List<User> findFriendsOfFriends(String userId, int limit) {
		if (!adjacencyList.containsKey(userId)) {
			return new ArrayList<>();
		}

		// Direct connections (friends)
		Set<String> directConnections = adjacencyList.get(userId).keySet();

		// Track friends of friends with their similarity scores
		Map<String, Double> friendsOfFriends = new HashMap<>();

		// For each direct connection
		for (String friendId : directConnections) {
			// Get their friends
			Map<String, Double> friendsConnections = adjacencyList.get(friendId);

			// For each friend of friend
			for (Map.Entry<String, Double> entry : friendsConnections.entrySet()) {
				String fofId = entry.getKey();
				double fofScore = entry.getValue();

				// Skip the original user and direct connections
				if (fofId.equals(userId) || directConnections.contains(fofId)) {
					continue;
				}

				// Calculate a weighted score (can be adjusted based on your needs)
				double weightedScore = fofScore * adjacencyList.get(userId).get(friendId);

				// Update score if higher
				if (!friendsOfFriends.containsKey(fofId) || friendsOfFriends.get(fofId) < weightedScore) {
					friendsOfFriends.put(fofId, weightedScore);
				}
			}
		}

		// Convert to list for sorting
		List<Map.Entry<String, Double>> fofList = new ArrayList<>(friendsOfFriends.entrySet());

		// Sort by similarity score (descending)
		fofList.sort((a, b) -> Double.compare(b.getValue(), a.getValue()));

		// Get top results
		List<User> result = new ArrayList<>();
		for (int i = 0; i < Math.min(limit, fofList.size()); i++) {
			String fofId = fofList.get(i).getKey();
			result.add(users.get(fofId));
		}

		return result;
	}

	// Get all connections for a user
	public Map<String, Double> getUserConnections(String userId) {
		if (!adjacencyList.containsKey(userId)) {
			return new HashMap<>();
		}
		return new HashMap<>(adjacencyList.get(userId));
	}

	// Remove a connection between users
	public void removeConnection(String userId1, String userId2) {
		if (adjacencyList.containsKey(userId1)) {
			adjacencyList.get(userId1).remove(userId2);
		}
		if (adjacencyList.containsKey(userId2)) {
			adjacencyList.get(userId2).remove(userId1);
		}
	}

	// Remove user from graph
	public void removeUser(String userId) {
		// Remove user from users map
		users.remove(userId);

		// Remove all connections to this user
		for (Map<String, Double> connections : adjacencyList.values()) {
			connections.remove(userId);
		}

		// Remove user's adjacency list
		adjacencyList.remove(userId);
	}

	// Get all users in the graph
	public Collection<User> getAllUsers() {
		return users.values();
	}

	// Get total number of connections in the graph
	public int getTotalConnections() {
		int total = 0;
		for (Map<String, Double> connections : adjacencyList.values()) {
			total += connections.size();
		}
		// Each connection is counted twice (once for each user)
		return total / 2;
	}
}