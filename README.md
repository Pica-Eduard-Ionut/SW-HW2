# Semantic Web Library Management System

## Team
- **Raul-Anton Jac** — Frontend & RDF Graph Visualization
- **Eduard Ionuț Pică** — RDF + Apache Jena + Backend Logic
- **Tudor-Neculai Balba** — Ontology + SPARQL + AI/RAG Chatbot

**Group:** 1241 EA

**GitHub Repository:** https://github.com/Pica-Eduard-Ionut/SW-HW2

> **Note:** The Google AI Studio API key is not committed to the repository. It must be set as an environment variable `GOOGLE_AI_KEY` before running the application. In IntelliJ: Run → Edit Configurations → Environment variables → add `GOOGLE_AI_KEY=your_key_here`. The `.env.local` file (if used) is ignored by git.

---

# Project Overview

This project is a **Semantic Web Library Management System** developed using:

- Java Spring Boot
- Apache Jena
- RDF/XML
- OWL Ontology
- SPARQL
- Thymeleaf
- AI/RAG Chatbot
- ChromaDB / FAISS
- OpenAI API / Google AI Studio

The application allows users to manage books stored in RDF format, visualize RDF graphs, execute SPARQL queries, and interact with an intelligent chatbot powered by Retrieval-Augmented Generation (RAG).

---

# Technologies Used

## Backend
- Java Spring Boot
- Apache Jena
- REST API
- RDF/XML Processing
- SPARQL Query Engine

## Frontend
- Thymeleaf Templates
- HTML/CSS/JavaScript
- RDF Graph Visualization
- Floating Chatbot Widget

## AI / RAG
- Google AI Studio (Gemini) — LLM for chat responses
- TF-IDF Vector Store (Java, in-memory) — document vectorization + cosine similarity search
- RDF/XML chunking into book text documents
- Retrieval-Augmented Generation (RAG) pipeline
- Context-aware conversation starters

---

# Team Contributions

---

# Eduard Ionuț Pică
### RDF + Apache Jena + Backend Logic

## Responsibilities
- Created RDF/XML schema and initial dataset
- Implemented RDF parsing and writing
- Developed backend CRUD logic using Apache Jena
- Integrated SPARQL queries into the application
- Built REST API endpoints
- Connected RDF storage with frontend components

## Main Features Implemented
- RDF/XML dataset for books and users
- JENA parser and RDF reader services
- Book creation and editing functionality
- SPARQL-based book listing
- Book details retrieval endpoint
- RDF utility refactoring

## Deliverables
- `books.rdf`
- Backend RDF services
- CRUD operations
- SPARQL integration

---

# Raul-Anton Jac
### Frontend + RDF Graph Visualization + UI

## Responsibilities
- Built frontend web pages
- Implemented RDF/XML upload feature
- Developed RDF graph visualization
- Created books listing and details pages
- Implemented floating chatbot UI
- Added context-aware conversation starters
- Improved responsive design and styling

## Main Features Implemented
- RDF upload page
- Graph visualization integration
- Books listing interface
- Individual book details page
- Floating chatbot widget
- Responsive frontend styling

## Deliverables
- UI pages
- RDF graph rendering
- Upload forms
- Chatbot frontend components

---

# Tudor-Neculai Balba
### Ontology + SPARQL + AI/RAG Chatbot

## Responsibilities
- Built the OWL ontology in Protégé — classes, object properties, named individuals
- Wrote the 5 SPARQL queries and ran them on the ontology
- Implemented the RAG chatbot backend: books from RDF get chunked into text, vectorized with TF-IDF, and stored in-memory; incoming queries are matched via cosine similarity and the top results are injected as context into the Gemini prompt
- Hooked up Google AI Studio (Gemini) as the LLM — auto-discovers the available model at startup
- Made the chatbot starters dynamic and context-aware based on what page you're on

## Main Features Implemented
- OWL ontology with classes: Book, User, Author, Theme, ReadingLevel and object properties: hasTheme, suitableFor, writtenBy, prefersTheme, hasReadingLevel
- 5 SPARQL queries executed on the ontology (see `sparql_owl.txt`)
- `RagService.java` — reads books from RDF at startup, builds TF-IDF vocabulary, stores vectors in-memory, exposes cosine similarity search
- `GoogleAiClient.java` — auto-discovers available Gemini model, calls generateContent API
- `ChatController.java` — `POST /api/chat` (RAG-enhanced responses), `GET /api/chat/starters` (context-aware starters)
- Semantic search by author and theme (e.g. "Frank Herbert + Science Fiction" → Dune)
- Chatbot starters change based on current page (books list vs book detail vs home)

## Deliverables
- `ontology.owl`
- GraphDB/Protégé screenshots
- `sparql_owl.txt`
- `ChatController.java`, `RagService.java`, `GoogleAiClient.java`
- `VectorEntry.java`, `ChatRequest.java`, `ChatResponse.java`

---

# Application Features

- RDF/XML book storage
- Apache Jena RDF processing
- SPARQL querying
- OWL ontology support
- RDF graph visualization
- REST API integration
- Book management system
- AI-powered semantic chatbot
- Context-aware recommendations
- Vector similarity search

---

# Project Structure

```bash
homework/
└── src/main/
    ├── java/com/example/homework/
    │   ├── controllers/
    │   │   ├── ApiController.java       # Book CRUD endpoints
    │   │   └── ChatController.java      # RAG chat endpoints
    │   ├── services/
    │   │   ├── BookService.java         # Jena RDF CRUD
    │   │   ├── RagService.java          # TF-IDF vector store + retrieval
    │   │   └── GoogleAiClient.java      # Gemini LLM integration
    │   └── models/
    │       ├── Book.java
    │       ├── VectorEntry.java
    │       ├── ChatRequest.java
    │       └── ChatResponse.java
    └── resources/
        ├── static/
        │   ├── index.html               # RDF upload page
        │   ├── books.html               # Books list + add/edit
        │   ├── book-details.html        # Book details page
        │   ├── graph.html               # RDF graph visualization
        │   └── js/app.js                # Frontend + chatbot widget
        └── xml/
            ├── books.rdf                # RDF/XML dataset
            ├── ontology.owl             # OWL ontology
            └── sparql_owl.txt           # 5 SPARQL queries
```