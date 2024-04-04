Library Management System

OVERVIEW:
The Library Management System is a web-based application designed to streamline library operations, enabling librarians to manage library resources efficiently and patrons to borrow and return books seamlessly. This system utilizes PostgreSQL for database management and JWT-based authentication for secure access to various endpoints.

FEATURES:
User Signup and Login:
Librarians and patrons can sign up for accounts with separate endpoints.
JWT tokens are generated upon successful login to authorize access to endpoints.

LIBRARIAN MANAGEMENT:
Librarians have exclusive access to endpoints for adding, updating, and deleting books to maintain library integrity.
They can also create, update, or delete patron accounts.

PATRON BOOK BORROWING:
Patrons can borrow books from the library.
Borrow requests from patrons require librarian approval.
Librarians can approve or reject patron borrow requests.

LOGGING AND PERFORMANCE METRICS:
AOP (Aspect-Oriented Programming) is implemented for logging method calls, exceptions, and performance metrics.
Logging includes book additions, updates, and patron transactions.
Data Integrity:

@Transaction annotation ensures data integrity during critical operations.

AUTHENTICATION:
The application uses JWT (JSON Web Token) for authentication, JWT SECRET KEY(SHA 256-bit key).

INSTALLATION:
To run the application locally, follow these steps:
Clone the repository.
Set up PostgreSQL and configure the database.
Set the JWT secret key as an environment variable.
Build and run the application.

USAGE:
API Endpoints: Interact with the application using the provided API endpoints.
Librarian Actions: Librarians have access to endpoints for managing books and patron accounts.
Patron Actions: Patrons can borrow books from the library.

TESTING:
Unit tests are written for core functionalities, including user management, book management, and borrowing services. The tests ensure proper functionality and have good coverage.

CONTACT:
For any questions or further assistance, please contact:
Email: decabimsoftware@gmail.com
