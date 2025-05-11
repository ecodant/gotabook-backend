# GotaBook API

This API is designed to manage a digital library, allowing users to borrow books from the library, rate them, connect with readers who share similar interests, and return books to the library. The system implements custom data structures including graphs, binary search trees, and priority queues to efficiently manage library operations.

## Features

### User Management

- User registration and authentication with role-based access (Reader/Administrator)
- User profile management
- Network of reader affinities based on similar book ratings
- Friend recommendations based on shared interests

### Book Management

- Complete book catalog with search functionality (by title, author, category)
- Book metadata including title, author, year, category, and availability status
- Average book ratings and reviews
- Admin tools for adding, updating, and removing books

### Loan System

- Book borrowing and return processes
- Waitlist management for popular books using priority queues
- Loan history tracking
- Book availability status updates

### Rating System

- Book rating (1-5 stars) and review functionality
- Calculation of average book ratings
- Analytics for most rated and highest-rated books
- Reader connectivity based on similar ratings

### Reader Connectivity

- Graph-based network of readers with similar interests
- "Friends of friends" suggestions
- Shortest path algorithms to discover reader connections
- Identification of reader affinity clusters

### Messaging System

- Direct messaging between connected readers
- Message history and conversation tracking

## Technologies Used

### Backend:

- **Framework:** Spring Boot
- **Database:** MongoDB
- **Authentication:** JWT-based authentication
- **API:** RESTful

### Custom Data Structures:

- **Binary Search Tree (BST):** For efficient book catalog management
- **Graph:** For representing reader affinity networks
- **Priority Queue:** For managing book waitlists
- **Linked Lists:** For tracking loan history and ratings

## Endpoints

### Authentication

- `POST /api/auth/register` - Register a new user
- `POST /api/auth/login` - Authenticate user and receive token

### User Management

- `GET /api/users/profile` - Get user profile
- `PUT /api/users/profile` - Update user profile
- `GET /api/users/recommendations` - Get reader recommendations

### Book Management

- `GET /api/books` - Get all books
- `GET /api/books/{id}` - Get book by ID
- `GET /api/books/search` - Search books by criteria
- `POST /api/books` - Add new book (admin)
- `PUT /api/books/{id}` - Update book information (admin)
- `DELETE /api/books/{id}` - Remove book from catalog (admin)

### Loan Management

- `POST /api/loans` - Borrow a book
- `PUT /api/loans/{id}/return` - Return a book
- `GET /api/loans/user` - Get user's active loans
- `GET /api/loans/history` - Get user's loan history
- `GET /api/loans/waitlist` - Check waitlist status

### Rating System

- `POST /api/ratings` - Rate a book
- `PUT /api/ratings/{id}` - Update a rating
- `GET /api/ratings/book/{id}` - Get book ratings
- `GET /api/books/top-rated` - Get top-rated books

### Reader Affinity Graph

- `GET /api/graph/connections` - Get reader connections
- `GET /api/graph/path/{userId}` - Find path to another reader
- `GET /api/graph/suggestions` - Get reader suggestions

### Messaging

- `POST /api/messages` - Send a message
- `GET /api/messages/inbox` - Get received messages
- `GET /api/messages/conversation/{userId}` - Get conversation history

### Admin Functions

- `GET /api/admin/stats` - Get system statistics
- `GET /api/admin/users` - Manage users (admin)
- `GET /api/admin/graph` - View complete affinity graph (admin)

## Getting Started

### Installation

1. Clone the repository

   ```bash
   git clone https://github.com/ecodant/gotabook-backend.git
   ```

2. Configure MongoDB connection in `.env` with the correct attributes, use the `.env.model` as a reference

3. Build the backend

   ```bash
   mvn clean install
   ```

4. Run the application
