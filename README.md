# Eye Sense Backend

Spring Boot application for image processing with AWS S3 integration and AI analysis.

## Docker Usage

### Prerequisites
- Docker
- Docker Compose

### Building and Running with Docker

1. **Build the application JAR first:**
   ```bash
   mvn clean package -DskipTests
   ```

2. **Build and run with Docker Compose:**
   ```bash
   docker-compose up --build
   ```

3. **Or build and run with Docker directly:**
   ```bash
   # Build the image
   docker build -t eyesense-backend:latest .
   
   # Run the container
   docker run -p 8090:8090 eyesense-backend:latest
   ```

### Configuration

The application can be configured using environment variables. Copy `.env.example` to `.env` and set your AWS credentials:

```bash
cp .env.example .env
```

Edit `.env` with your AWS credentials:
```
AWS_ACCESS_KEY_ID=your-access-key-id
AWS_SECRET_ACCESS_KEY=your-secret-access-key
AWS_REGION=us-east-1
AWS_S3_BUCKET=your-bucket-name
AI_API_BASE_URL=http://your-ai-api:8000
```

### Docker Compose Configuration

The `docker-compose.yml` includes:
- Port mapping (8090:8090)
- Environment variables configuration
- Health checks
- Memory limits
- Restart policy

### Accessing the Application

Once running, the application will be available at:
- Application: http://localhost:8090
- H2 Console: http://localhost:8090/h2-console (if enabled)

### Environment Variables

| Variable | Description | Default |
|----------|-------------|---------|
| `AWS_REGION` | AWS region | us-east-1 |
| `AWS_ACCESS_KEY_ID` | AWS access key | - |
| `AWS_SECRET_ACCESS_KEY` | AWS secret key | - |
| `AWS_S3_BUCKET` | S3 bucket name | eyesense-demo-ml-api |
| `AI_API_BASE_URL` | AI API endpoint | http://localhost:8000 |
| `SPRING_PROFILES_ACTIVE` | Spring profile | docker |
