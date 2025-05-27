package library.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import datastructures.AffinityGraph;
import library.models.Book;
import library.models.Loan;
import library.models.Role;
import library.models.User;
import library.repositories.BookRepository;
import library.repositories.LoanRepository;
import library.repositories.RatingRepository;
import library.repositories.UserRepository;

@Service
public class StatsService {

    private final UserRepository userRepository;
    private final LoanRepository loanRepository;
    private final BookRepository bookRepository;
    private final RatingRepository ratingRepository;
    private final AffinityGraph affinityGraph;

    @Autowired
    public StatsService(
            UserRepository userRepository, 
            LoanRepository loanRepository,
            BookRepository bookRepository,
            RatingRepository ratingRepository) {
        this.userRepository = userRepository;
        this.loanRepository = loanRepository;
        this.bookRepository = bookRepository;
        this.ratingRepository = ratingRepository;
        this.affinityGraph = new AffinityGraph();
        initializeAffinityGraph();
    }
    
    private void initializeAffinityGraph() {
        // Load all users into the affinity graph
        List<User> allUsers = userRepository.findAll();
        affinityGraph.buildGraphFromUsers(allUsers);
    }
    
    // Refreshes the graph with latest user data
    public void refreshAffinityGraph() {
        List<User> allUsers = userRepository.findAll();
        affinityGraph.buildGraphFromUsers(allUsers);
    }

    // 1. Get loan statistics for a specific user
    public Map<String, Object> getUserLoanStats(String userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            return Collections.emptyMap();
        }
        
        User user = userOpt.get();
        List<Loan> userLoans = loanRepository.findByUserId(userId);
        
        // Count active, waiting, and returned loans
        long activeLoans = userLoans.stream()
                .filter(loan -> loan.getStatus() == Loan.LoanStatus.ACTIVE)
                .count();
        
        long waitingLoans = userLoans.stream()
                .filter(loan -> loan.getStatus() == Loan.LoanStatus.WAITING)
                .count();
        
        long returnedLoans = userLoans.stream()
                .filter(loan -> loan.getStatus() == Loan.LoanStatus.RETURNED)
                .count();
        
        // Get the most recent loans (up to 5)
        List<Loan> recentLoans = userLoans.stream()
                .sorted(Comparator.comparing(Loan::getLoanDate).reversed())
                .limit(5)
                .collect(Collectors.toList());
        
        Map<String, Object> stats = new HashMap<>();
        stats.put("username", user.getUsername());
        stats.put("totalLoans", userLoans.size());
        stats.put("activeLoans", activeLoans);
        stats.put("waitingLoans", waitingLoans);
        stats.put("returnedLoans", returnedLoans);
        stats.put("recentLoans", recentLoans);
        
        return stats;
    }
    
    // 2. Get most rated books in the system
    public List<Map<String, Object>> getMostRatedBooks(int limit) {
        List<Book> allBooks = bookRepository.findAll();
        
        // Calculate rating count for each book
        List<Map<String, Object>> bookStats = new ArrayList<>();
        
        for (Book book : allBooks) {
            long ratingCount = ratingRepository.countByBookId(book.getId());
            if (ratingCount > 0) {
                Map<String, Object> bookStat = new HashMap<>();
                bookStat.put("book", book);
                bookStat.put("ratingCount", ratingCount);
                bookStats.add(bookStat);
            }
        }
        
        // Sort by rating count (descending)
        bookStats.sort((a, b) -> Long.compare(
                (Long) b.get("ratingCount"), 
                (Long) a.get("ratingCount")));
        
        // Return top N books
        return bookStats.stream()
                .limit(limit)
                .collect(Collectors.toList());
    }
    
    // 3. Get readers with most friends
    public List<Map<String, Object>> getReadersWithMostFriends(int limit) {
        // Get all users with READER role
        List<User> readers = userRepository.findByRole(Role.READER);
        
        // Calculate friend count for each reader
        List<Map<String, Object>> readerStats = new ArrayList<>();
        
        for (User reader : readers) {
            int friendCount = reader.getFriends() != null ? reader.getFriends().size() : 0;
            
            Map<String, Object> readerStat = new HashMap<>();
            readerStat.put("user", reader);
            readerStat.put("friendCount", friendCount);
            readerStats.add(readerStat);
        }
        
        // Sort by friend count (descending)
        readerStats.sort((a, b) -> Integer.compare(
                (Integer) b.get("friendCount"), 
                (Integer) a.get("friendCount")));
        
        // Return top N readers
        return readerStats.stream()
                .limit(limit)
                .collect(Collectors.toList());
    }
    
    // 4. Find shortest path between two users
    public Map<String, Object> getShortestPathBetweenUsers(String userId1, String userId2) {
        Map<String, Object> result = new HashMap<>();
        
        Optional<User> user1Opt = userRepository.findById(userId1);
        Optional<User> user2Opt = userRepository.findById(userId2);
        
        if (user1Opt.isEmpty() || user2Opt.isEmpty()) {
            result.put("error", "One or both users not found");
            return result;
        }
        
        User user1 = user1Opt.get();
        User user2 = user2Opt.get();
        
        // Refresh the graph to ensure we have the latest data
        refreshAffinityGraph();
        
        // Get the shortest path
        List<String> pathUserIds = affinityGraph.findShortestPath(userId1, userId2);
        
        if (pathUserIds.isEmpty()) {
            result.put("connected", false);
            result.put("message", "No connection path exists between these users");
            return result;
        }
        
        // Convert user IDs in path to user details
        List<Map<String, String>> pathDetails = new ArrayList<>();
        for (String id : pathUserIds) {
            Optional<User> userOpt = userRepository.findById(id);
            if (userOpt.isPresent()) {
                User user = userOpt.get();
                Map<String, String> userDetail = new HashMap<>();
                userDetail.put("id", user.getId());
                userDetail.put("username", user.getUsername());
                pathDetails.add(userDetail);
            }
        }
        
        result.put("connected", true);
        result.put("sourceUser", user1.getUsername());
        result.put("targetUser", user2.getUsername());
        result.put("pathLength", pathUserIds.size() - 1); // Number of connections = nodes - 1
        result.put("path", pathDetails);
        
        return result;
    }
    
    // Format the loan stats as text
    public String formatUserLoanStatsAsText(String userId) {
        Map<String, Object> stats = getUserLoanStats(userId);
        if (stats.isEmpty()) {
            return "User not found.";
        }
        
        StringBuilder sb = new StringBuilder();
        sb.append("LOAN STATISTICS FOR USER: ").append(stats.get("username")).append("\n");
        sb.append("========================================\n\n");
        sb.append("Total loans: ").append(stats.get("totalLoans")).append("\n");
        sb.append("Active loans: ").append(stats.get("activeLoans")).append("\n");
        sb.append("Waiting loans: ").append(stats.get("waitingLoans")).append("\n");
        sb.append("Returned loans: ").append(stats.get("returnedLoans")).append("\n\n");
        
        sb.append("RECENT LOANS:\n");
        sb.append("----------------------------------------\n");
        
        @SuppressWarnings("unchecked")
        List<Loan> recentLoans = (List<Loan>) stats.get("recentLoans");
        
        if (recentLoans.isEmpty()) {
            sb.append("No recent loans found.\n");
        } else {
            for (Loan loan : recentLoans) {
                sb.append("Book: ").append(loan.getBookTitle()).append("\n");
                sb.append("Status: ").append(loan.getStatus()).append("\n");
                sb.append("Loan date: ").append(loan.getLoanDate()).append("\n");
                if (loan.getReturnDate() != null) {
                    sb.append("Return date: ").append(loan.getReturnDate()).append("\n");
                }
                sb.append("----------------------------------------\n");
            }
        }
        
        return sb.toString();
    }
    
    // Format most rated books as text
    public String formatMostRatedBooksAsText(int limit) {
        List<Map<String, Object>> bookStats = getMostRatedBooks(limit);
        
        StringBuilder sb = new StringBuilder();
        sb.append("MOST RATED BOOKS\n");
        sb.append("========================================\n\n");
        
        if (bookStats.isEmpty()) {
            sb.append("No rated books found.\n");
            return sb.toString();
        }
        
        int rank = 1;
        for (Map<String, Object> stat : bookStats) {
            Book book = (Book) stat.get("book");
            long ratingCount = (Long) stat.get("ratingCount");
            
            sb.append(rank++).append(". ").append(book.getTitle()).append("\n");
            sb.append("   Author: ").append(book.getAuthor()).append("\n");
            sb.append("   Average rating: ").append(String.format("%.1f", book.getAverageRating())).append("\n");
            sb.append("   Number of ratings: ").append(ratingCount).append("\n");
            sb.append("----------------------------------------\n");
        }
        
        return sb.toString();
    }
    
    // Format readers with most friends as text
    public String formatReadersWithMostFriendsAsText(int limit) {
        List<Map<String, Object>> readerStats = getReadersWithMostFriends(limit);
        
        StringBuilder sb = new StringBuilder();
        sb.append("READERS WITH MOST FRIENDS\n");
        sb.append("========================================\n\n");
        
        if (readerStats.isEmpty()) {
            sb.append("No readers found.\n");
            return sb.toString();
        }
        
        int rank = 1;
        for (Map<String, Object> stat : readerStats) {
            User user = (User) stat.get("user");
            int friendCount = (Integer) stat.get("friendCount");
            
            sb.append(rank++).append(". ").append(user.getUsername()).append("\n");
            sb.append("   Friend count: ").append(friendCount).append("\n");
            sb.append("   Registration date: ").append(user.getRegistrationDate()).append("\n");
            sb.append("----------------------------------------\n");
        }
        
        return sb.toString();
    }
    
    // Format shortest path between users as text
    public String formatShortestPathAsText(String userId1, String userId2) {
        Map<String, Object> pathResult = getShortestPathBetweenUsers(userId1, userId2);
        
        StringBuilder sb = new StringBuilder();
        sb.append("SHORTEST CONNECTION PATH\n");
        sb.append("========================================\n\n");
        
        if (pathResult.containsKey("error")) {
            sb.append("Error: ").append(pathResult.get("error")).append("\n");
            return sb.toString();
        }
        
        boolean connected = (Boolean) pathResult.get("connected");
        if (!connected) {
            sb.append(pathResult.get("message")).append("\n");
            return sb.toString();
        }
        
        sb.append("From: ").append(pathResult.get("sourceUser")).append("\n");
        sb.append("To: ").append(pathResult.get("targetUser")).append("\n");
        sb.append("Degrees of separation: ").append(pathResult.get("pathLength")).append("\n\n");
        
        sb.append("CONNECTION PATH:\n");
        sb.append("----------------------------------------\n");
        
        @SuppressWarnings("unchecked")
        List<Map<String, String>> path = (List<Map<String, String>>) pathResult.get("path");
        
        for (int i = 0; i < path.size(); i++) {
            Map<String, String> user = path.get(i);
            sb.append(i + 1).append(". ").append(user.get("username"));
            
            if (i < path.size() - 1) {
                sb.append(" → ");
            }
            
            // Start a new line every 3 users for readability
            if ((i + 1) % 3 == 0 && i < path.size() - 1) {
                sb.append("\n   ");
            }
        }
        
        sb.append("\n\n");
        sb.append("This path represents the shortest connection between these users.\n");
        
        return sb.toString();
    }
}
