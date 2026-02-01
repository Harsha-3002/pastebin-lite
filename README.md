# Pastebin Lite

A simple pastebin application that allows users to create and share text pastes with optional expiration times and view limits.

## Features

- Create text pastes with optional constraints:
  - Time-based expiry (TTL in seconds)
  - View count limit
- Share pastes via unique URLs
- RESTful API for programmatic access
- Clean, responsive web interface

## Tech Stack

### Backend
- **Framework:** Spring Boot 4.0.2
- **Language:** Java 21
- **Persistence:** H2 Database (local), MySQL (production)
- **Build Tool:** Maven

### Frontend
- **Framework:** React 18 with Vite
- **Routing:** React Router
- **HTTP Client:** Axios
- **Styling:** Plain CSS

## Persistence Layer

This application uses **JPA (Java Persistence API)** with:
- **H2 Database** for local development (file-based storage at `./data/pastebindb`)
- **MySQL** for production deployment

The database schema is automatically created and updated via Hibernate's `ddl-auto=update` setting.

## Running Locally

### Prerequisites
- Java 21+
- Node.js 20.19+ / 22.12+
- Maven (included via wrapper)

### Backend Setup

1. Navigate to backend directory:
```bash
   cd backend
```

2. Run the application:
```bash
   ./mvnw spring-boot:run
```
   (On Windows: `mvnw.cmd spring-boot:run`)

3. Backend will start on `http://localhost:8081`

### Frontend Setup

1. Navigate to frontend directory:
```bash
   cd pastebin-frontend
```

2. Install dependencies:
```bash
   npm install
```

3. Start development server:
```bash
   npm run dev
```

4. Frontend will start on `http://localhost:5173`

## API Endpoints

### Health Check
```
GET /api/healthz
```
Returns: `{"ok": true}`

### Create Paste
```
POST /api/pastes
Content-Type: application/json

{
  "content": "Your text here",
  "ttl_seconds": 60,      // Optional
  "max_views": 5          // Optional
}
```

### Get Paste (API)
```
GET /api/pastes/:id
```
Returns paste with remaining_views and expires_at

### View Paste (HTML)
```
GET /p/:id
```
Returns HTML page with paste content

## Design Decisions

1. **Dual View Endpoints:** 
   - `/api/pastes/:id` for API access (JSON, increments view count)
   - `/p/:id` for HTML viewing (for automated tests and direct browser access)

2. **View Counting:** 
   - Only API endpoint increments view count to avoid double-counting

3. **Expiry Logic:**
   - Both TTL and max_views are optional
   - If both are set, paste expires when EITHER constraint is met
   - Deterministic time testing supported via `x-test-now-ms` header when `TEST_MODE=1`

4. **XSS Prevention:**
   - All paste content is HTML-escaped before rendering

5. **CORS Configuration:**
   - Backend allows requests from frontend origins

## Environment Variables

### Backend (Production)
- `DATABASE_URL` - MySQL connection string
- `DATABASE_USERNAME` - Database username
- `DATABASE_PASSWORD` - Database password
- `BASE_URL` - Deployed backend URL
- `TEST_MODE` - Enable deterministic time testing (default: false)

### Frontend (Production)
- `VITE_API_URL` - Backend API URL

## Building for Production

### Backend
```bash
cd backend
./mvnw clean package
java -jar target/pastebin-lite-1.0.0.jar
```

### Frontend
```bash
cd pastebin-frontend
npm run build
# Deploy the 'dist' folder
```

## Project Structure
```
pastebin-lite/
├── backend/              # Spring Boot backend
│   ├── src/
│   └── pom.xml
├── pastebin-frontend/    # React frontend
│   ├── src/
│   └── package.json
└── README.md
```

## License

Educational project.
