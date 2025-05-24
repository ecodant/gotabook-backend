package datastructures;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
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
        
        // Add friendship connections if they exist
        if (user.getFriends() != null) {
            for (String friendId : user.getFriends()) {
                // Create connections (with weight 1.0 for direct friendship)
                createConnection(userId, friendId, 1.0);
            }
        }
    }

    // Build graph from a collection of users
    public void buildGraphFromUsers(Collection<User> userCollection) {
        // First add all users to the graph
        for (User user : userCollection) {
            String userId = user.getId();
            users.put(userId, user);
            
            if (!adjacencyList.containsKey(userId)) {
                adjacencyList.put(userId, new HashMap<>());
            }
        }
        
        // Then create all the friendship connections
        for (User user : userCollection) {
            String userId = user.getId();
            if (user.getFriends() != null) {
                for (String friendId : user.getFriends()) {
                    createConnection(userId, friendId, 1.0);
                }
            }
        }
    }

    // Create or update a connection between users
    public void createConnection(String userId1, String userId2, double weight) {
        // Ensure both users exist
        if (!adjacencyList.containsKey(userId1)) {
            adjacencyList.put(userId1, new HashMap<>());
        }
        if (!adjacencyList.containsKey(userId2)) {
            adjacencyList.put(userId2, new HashMap<>());
        }

        // Create bidirectional connection with weight
        adjacencyList.get(userId1).put(userId2, weight);
        adjacencyList.get(userId2).put(userId1, weight);
    }

    // Get the weight/distance between two directly connected users
    public double getConnectionWeight(String userId1, String userId2) {
        if (!adjacencyList.containsKey(userId1) || !adjacencyList.get(userId1).containsKey(userId2)) {
            return Double.POSITIVE_INFINITY; // No direct connection
        }
        return adjacencyList.get(userId1).get(userId2);
    }

    // Find the shortest path between two users using Dijkstra's algorithm
    public List<String> findShortestPath(String sourceUserId, String targetUserId) {
        if (!adjacencyList.containsKey(sourceUserId) || !adjacencyList.containsKey(targetUserId)) {
            return Collections.emptyList(); // One of the users doesn't exist
        }
        
        if (sourceUserId.equals(targetUserId)) {
            List<String> selfPath = new ArrayList<>();
            selfPath.add(sourceUserId);
            return selfPath; // Path to self
        }

        // Distance from source to all nodes
        Map<String, Double> distances = new HashMap<>();
        // Previous node in optimal path
        Map<String, String> previousNodes = new HashMap<>();
        // Set of nodes to be evaluated
        Set<String> unvisitedNodes = new HashSet<>();

        // Initialize distances and add all nodes to unvisited set
        for (String nodeId : adjacencyList.keySet()) {
            distances.put(nodeId, Double.POSITIVE_INFINITY);
            unvisitedNodes.add(nodeId);
        }
        // Distance from source to itself is 0
        distances.put(sourceUserId, 0.0);

        // Priority queue to get the node with minimum distance
        PriorityQueue<NodeDistance> priorityQueue = new PriorityQueue<>();
        priorityQueue.add(new NodeDistance(sourceUserId, 0.0));

        while (!priorityQueue.isEmpty()) {
            NodeDistance current = priorityQueue.poll();
            String currentNodeId = current.nodeId;
            
            // If we've reached the target, we can stop
            if (currentNodeId.equals(targetUserId)) {
                break;
            }
            
            // Skip if this node has already been processed or if the distance is outdated
            if (!unvisitedNodes.contains(currentNodeId) || current.distance > distances.get(currentNodeId)) {
                continue;
            }
            
            // Mark the current node as visited
            unvisitedNodes.remove(currentNodeId);
            
            // Check all neighbors of current node
            for (Map.Entry<String, Double> neighborEntry : adjacencyList.get(currentNodeId).entrySet()) {
                String neighborId = neighborEntry.getKey();
                
                // Skip if neighbor has been visited
                if (!unvisitedNodes.contains(neighborId)) {
                    continue;
                }
                
                double edgeWeight = neighborEntry.getValue();
                double distanceThroughCurrent = distances.get(currentNodeId) + edgeWeight;
                
                // If we found a better path
                if (distanceThroughCurrent < distances.get(neighborId)) {
                    distances.put(neighborId, distanceThroughCurrent);
                    previousNodes.put(neighborId, currentNodeId);
                    priorityQueue.add(new NodeDistance(neighborId, distanceThroughCurrent));
                }
            }
        }
        
        // If no path was found
        if (!previousNodes.containsKey(targetUserId)) {
            return Collections.emptyList();
        }
        
        // Reconstruct the path
        LinkedList<String> path = new LinkedList<>();
        String current = targetUserId;
        
        while (current != null) {
            path.addFirst(current);
            current = previousNodes.get(current);
        }
        
        return path;
    }
    
    // Helper class for Dijkstra's algorithm
    private static class NodeDistance implements Comparable<NodeDistance> {
        String nodeId;
        double distance;
        
        public NodeDistance(String nodeId, double distance) {
            this.nodeId = nodeId;
            this.distance = distance;
        }
        
        @Override
        public int compareTo(NodeDistance other) {
            return Double.compare(this.distance, other.distance);
        }
    }

    // Find similar users to a given user (direct friends)
    public List<User> findDirectFriends(String userId) {
        if (!adjacencyList.containsKey(userId)) {
            return new ArrayList<>();
        }

        List<User> friends = new ArrayList<>();
        for (String friendId : adjacencyList.get(userId).keySet()) {
            if (users.containsKey(friendId)) {
                friends.add(users.get(friendId));
            }
        }
        
        return friends;
    }

    // Find "friends of friends" (users 2 hops away)
    public List<User> findFriendsOfFriends(String userId) {
        if (!adjacencyList.containsKey(userId)) {
            return new ArrayList<>();
        }

        // Direct connections (friends)
        Set<String> directConnections = adjacencyList.get(userId).keySet();
        Set<String> friendsOfFriends = new HashSet<>();

        // For each direct connection
        for (String friendId : directConnections) {
            // Get their friends
            if (adjacencyList.containsKey(friendId)) {
                friendsOfFriends.addAll(adjacencyList.get(friendId).keySet());
            }
        }
        
        // Remove the original user and direct friends
        friendsOfFriends.remove(userId);
        friendsOfFriends.removeAll(directConnections);
        
        // Convert to list of users
        List<User> result = new ArrayList<>();
        for (String fofId : friendsOfFriends) {
            if (users.containsKey(fofId)) {
                result.add(users.get(fofId));
            }
        }

        return result;
    }

    // Suggest potential friends based on shortest path
    public List<Map.Entry<User, Integer>> suggestPotentialFriends(String userId, int maxDistance) {
        if (!adjacencyList.containsKey(userId)) {
            return new ArrayList<>();
        }
        
        // Map to store user and their distance from source
        Map<String, Integer> distances = new HashMap<>();
        
        // Breadth-first search to find distances
        Set<String> visited = new HashSet<>();
        LinkedList<String> queue = new LinkedList<>();
        
        visited.add(userId);
        queue.add(userId);
        distances.put(userId, 0);
        
        while (!queue.isEmpty()) {
            String currentId = queue.poll();
            int currentDistance = distances.get(currentId);
            
            // If we've reached the max distance, don't explore further
            if (currentDistance >= maxDistance) {
                continue;
            }
            
            // Explore neighbors
            for (String neighborId : adjacencyList.get(currentId).keySet()) {
                if (!visited.contains(neighborId)) {
                    visited.add(neighborId);
                    queue.add(neighborId);
                    distances.put(neighborId, currentDistance + 1);
                }
            }
        }
        
        // Create result list (excluding direct friends and self)
        List<Map.Entry<User, Integer>> suggestions = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : distances.entrySet()) {
            String suggestionId = entry.getKey();
            int distance = entry.getValue();
            
            // Skip self and direct friends
            if (suggestionId.equals(userId) || distance <= 1) {
                continue;
            }
            
            if (users.containsKey(suggestionId)) {
                suggestions.add(Map.entry(users.get(suggestionId), distance));
            }
        }
        
        suggestions.sort((a, b) -> Integer.compare(a.getValue(), b.getValue()));
        
        return suggestions;
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