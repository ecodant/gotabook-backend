# GotaBook API

This API is designed to manage a digital library, allowing users to borrow books from the library, rate them, connect with readers who share similar interests, and return books to the library. The system implements custom data structures including graphs, binary search trees, and priority queues to efficiently manage library operations.

## Features

### User Management

- User registration and authentication with role-based access (Reader/Administrator)
- Network of reader affinities based on similar book ratings
- Friend recommendations based on shared interests

### Book Management

- Complete book catalog
- Average book ratings
- Admin tools for adding, updating, and removing books

### Loan System

- Book borrowing and return processes
- Waitlist management using queues
- Loan history
- Book availability status updates

### Rating System

- Book rating (1-5 stars) and review functionality
- Calculation of average book ratings
- Analytics for most rated and highest-rated books
- Reader connectivity based on similar ratings

## Technologies Used

### Backend:

- **Framework:** Spring Boot
- **Database:** MongoDB
- **Authentication:** JWT-based authentication
- **API:** RESTful

### Custom Data Structures:

- **Binary Search Tree (BST):** For efficient book catalog management
- **Graph:** For representing reader affinity networks
- **Queue:** For managing book waitlists
- **Linked Lists:** For tracking loan history and ratings
- **Custom Array:** For store some data returned from the DataBase

## Endpoints

### User

- `POST /api/users/register` - Register a new user
- `POST /api/users/login` - Authenticate user and receive token
- `GET /api/users/{id}` - Get Data from a particular User

### Book

- `GET /api/books/` - Get all books
- `GET /api/books/{id}` - Get book by ID
- `GET /api/books/title/{title}` - Get book by Title
- `GET /api/books/search` - Search books by criteria
- `POST /api/books/` - Add new book (admin)
- `PUT /api/books/{id}` - Update book information (admin)
- `DELETE /api/books/{id}` - Remove book from catalog (admin)

### Loan

- `POST /api/loans/` - Borrow a book (Create a Loan)
- `PUT /api/loans/{id}/return` - Return a book
- `GET /api/loans/{id}` - Get loand by ID
- `GET /api/loans/user/{userId}` - Get user's loans
- `GET /api/loans/user/{userId}/active` - Get user's active loans
- `GET /api/loans/queue/{bookId}` - Get the waiting queue for a particular book

### Rating System

- `POST /api/ratings/` - Rate a book (Create a Rating)
- `GET /api/ratings/` - Get all ratings
- `PUT /api/ratings/{id}` - Update a rating
- `GET /api/ratings/{id}` - Get rating by id
- `GET /api/ratings/book/{id}` - Get book ratings
- `GET /api/ratings/user/{id}` - Get user ratings

### Messaging

- `POST /api/messages/` - Send a message
- `GET /api/messages/{id}` - Get message by id
- `GET /api/messages/sender/{senderId}` - Get message by Sender
- `GET /api/messages/receiver/{receiverId}` - Get message by Receiver

### Admin

- `GET /api/admin/stats` - Get system reports

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
