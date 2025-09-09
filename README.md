# Bilingua Server

A Spring Boot REST API application for comparing and managing bilingual texts in different languages. The application reads markdown files and provides REST endpoints for paragraph-by-paragraph comparison and navigation.

## Overview

Bilingua Server enables side-by-side comparison of texts in two different languages by:
- Reading two markdown files specified in configuration
- Breaking down texts into paragraphs for comparison
- Maintaining a pointer to track the current reading position
- Providing REST APIs for navigation and text retrieval
- Supporting text editing and persistence back to files

## Features

- **Bilingual Text Comparison**: Compare texts paragraph by paragraph
- **Markdown File Support**: Reads texts from markdown files in Obsidian folders
- **Position Tracking**: Maintains current reading position with persistent storage
- **REST API**: Clean RESTful endpoints for text navigation and editing
- **Text Editing**: Modify paragraphs and save changes back to source files
- **OpenAPI/Swagger**: Interactive API documentation
- **Configuration-Based**: Uses properties file for flexible setup

## Configuration

The application reads configuration from `bi.properties` file located in the `bilingua` folder in your user home directory.

### Configuration File Location
```
~/Documents/pi/bilingua/bi.properties
```

### Required Properties
```properties
book=path/to/your/obsidian/folder
left_name=relative/path/to/first/language/file.md
right_name=relative/path/to/second/language/file.md
```

### Example Configuration
```properties
book=/Users/username/Documents/MyObsidianVault
left_name=books/novel_english.md
right_name=books/novel_russian.md
```

## Position Persistence

The application maintains a `ptr.txt` file in the `~/bilingua/` folder to store the current paragraph position. If this file doesn't exist, the position defaults to 0.

## Getting Started

### Prerequisites

- Java 21 or higher
- Maven 3.6+ (or use the included Maven wrapper)
- Obsidian vault or folder with markdown files
- Configuration file setup (see Configuration section)

### Building the Project

```bash
./mvnw clean compile
```

### Running the Application

```bash
./mvnw spring-boot:run
```

The server will start on `http://localhost:8080` by default.

### Building a JAR

```bash
./mvnw clean package
```

The WAR file will be created in the `target/` directory.

## API Documentation

Once the application is running, you can access the interactive API documentation at:

- **Swagger UI**: http://localhost:8080/swagger-ui/index.html
- **OpenAPI Spec**: http://localhost:8080/v3/api-docs

## API Endpoints

### Pointer Management

- **GET /ptr** - Get current pointer position
  - Returns: Current paragraph position as integer

- **POST /ptr?n={value}** - Set pointer position
  - Parameters: `n` - New position (must be >= 0)
  - Sets the current paragraph position to the specified value

### Paragraph Retrieval

- **GET /pars?shift={shift}** - Get bilingual paragraph pair
  - Parameters: `shift` - Offset from current position (default: 0)
  - Returns: JSON object with `left` and `right` paragraph texts
  - Example response:
    ```json
    {
      "left": "English paragraph text",
      "right": "Russian paragraph text"
    }
    ```

### Text Editing

- **POST /save** - Save modified paragraph texts
  - Body: JSON with `left` and/or `right` text content
  - Example request:
    ```json
    {
      "left": "Updated English text",
      "right": "Updated Russian text"
    }
    ```
  - If text is modified, the corresponding markdown file is updated and paragraph lists are reloaded

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

## Usage Workflow

1. **Setup Configuration**: Create `~/bilingua/bi.properties` with your markdown file paths
2. **Start Application**: Run the Spring Boot application
3. **Navigate Texts**: Use `/ptr` endpoints to move through paragraphs
4. **Compare Paragraphs**: Use `/pars` endpoint to retrieve text at current or offset positions
5. **Edit Content**: Use `/save` endpoint to modify and persist paragraph changes
6. **Track Progress**: Position is automatically saved to `~/bilingua/ptr.txt`

## Development

### Running Tests

```bash
./mvnw test
```

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
