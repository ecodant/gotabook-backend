package library.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import library.services.StatsService;

@RestController
@RequestMapping("/api/stats")
public class StatsController {

    private final StatsService statsService;

    @Autowired
    public StatsController(StatsService statsService) {
        this.statsService = statsService;
    }

    //User loan statistics
    @GetMapping("/user-loans/{userId}")
    public ResponseEntity<String> getUserLoanStats(@PathVariable String userId) {
        String statsText = statsService.formatUserLoanStatsAsText(userId);
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_PLAIN);
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=user_loan_stats.txt");
        
        return ResponseEntity.ok()
                .headers(headers)
                .body(statsText);
    }

    //Most rated books
    @GetMapping("/most-rated-books")
    public ResponseEntity<String> getMostRatedBooks(@RequestParam(defaultValue = "10") int limit) {
        String statsText = statsService.formatMostRatedBooksAsText(limit);
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_PLAIN);
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=most_rated_books.txt");
        
        return ResponseEntity.ok()
                .headers(headers)
                .body(statsText);
    }

    //Readers with most friends
    @GetMapping("/readers-with-most-friends")
    public ResponseEntity<String> getReadersWithMostFriends(@RequestParam(defaultValue = "10") int limit) {
        String statsText = statsService.formatReadersWithMostFriendsAsText(limit);
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_PLAIN);
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=readers_with_most_friends.txt");
        
        return ResponseEntity.ok()
                .headers(headers)
                .body(statsText);
    }

    //Shortest path between two users
    @GetMapping("/path/{userId1}/{userId2}")
    public ResponseEntity<String> getShortestPath(
            @PathVariable String userId1, 
            @PathVariable String userId2) {
        
        String statsText = statsService.formatShortestPathAsText(userId1, userId2);
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_PLAIN);
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=shortest_path.txt");
        
        return ResponseEntity.ok()
                .headers(headers)
                .body(statsText);
    }
    
   
    @GetMapping("/refresh-graph")
    public ResponseEntity<String> refreshAffinityGraph() {
        statsService.refreshAffinityGraph();
        return ResponseEntity.ok("Affinity graph refreshed successfully.");
    }
}
