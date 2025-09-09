# Bilingua Server

A Spring Boot REST API server for managing bilingual text paragraphs. This server provides endpoints for tracking reading position and retrieving bilingual text content.

## Features

- **Pointer Management**: Track and update reading position in bilingual texts
- **REST API**: Clean RESTful endpoints for text management
- **OpenAPI/Swagger**: Interactive API documentation
- **Spring Boot**: Built on robust Spring Boot framework
- **Maven**: Standard Maven project structure

## Getting Started

### Prerequisites

- Java 21 or higher
- Maven 3.6+ (or use the included Maven wrapper)

### Building the Project

```bash
# Using Maven wrapper (recommended)
./mvnw clean compile

# Or with system Maven
mvn clean compile
```

### Running the Application

```bash
# Using Maven wrapper
./mvnw spring-boot:run

# Or with system Maven
mvn spring-boot:run
```

The server will start on `http://localhost:8080` by default.

### Building a JAR

```bash
./mvnw clean package
```

The executable JAR will be created in the `target/` directory.

## API Documentation

Once the application is running, you can access the interactive API documentation at:

- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **OpenAPI Spec**: http://localhost:8080/v3/api-docs

## API Endpoints

### Pointer Management

- `GET /ptr` - Get current pointer position
- `POST /ptr?n={value}` - Set pointer position to a specific value

### Paragraph Retrieval

- `GET /pars` - Get bilingual paragraph pairs (see API docs for parameters)

## Project Structure

```
src/
├── main/
│   ├── java/
│   │   └── io/github/plotnik/bilingua_server/
│   │       ├── BilinguaServerApplication.java    # Main application class
│   │       ├── config/                           # Configuration classes
│   │       ├── controller/                       # REST controllers
│   │       ├── dto/                             # Data transfer objects
│   │       └── service/                         # Business logic services
│   └── resources/
│       ├── application.properties               # Application configuration
│       ├── static/                             # Static web resources
│       └── templates/                          # Template files
└── test/
    └── java/                                   # Test classes
```

## Configuration

The application can be configured through `src/main/resources/application.properties`. 

Key configuration options:
- Server port: `server.port=8080`
- Application context path: `server.servlet.context-path=/`

## Development

### Running Tests

```bash
./mvnw test
```

### Code Style

This project follows standard Java coding conventions. Please ensure your code:
- Uses proper indentation (4 spaces)
- Includes appropriate Javadoc comments
- Follows Spring Boot best practices

## Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Author

- **plotnik** - [GitHub Profile](https://github.com/plotnik)

## Support

If you encounter any issues or have questions, please [open an issue](https://github.com/plotnik/bilingua-server/issues) on GitHub.
