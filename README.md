# Semantic Web Library Management System

## Team
- **Raul-Anton Jac** — Frontend & RDF Graph Visualization
- **Eduard Ionuț Pică** — RDF + Apache Jena + Backend Logic
- **Tudor-Neculai Balba** — Ontology + SPARQL + AI/RAG Chatbot

**Group:** 1241 EA

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
- ChromaDB / FAISS
- OpenAI API or Google AI Studio
- XML Chunking
- Embeddings Generation
- Semantic Search

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
- Created OWL ontology using Protégé
- Exported ontology and GraphDB visualizations
- Wrote SPARQL queries
- Built vector database ingestion pipeline
- Implemented embeddings generation
- Integrated LLM APIs
- Developed semantic retrieval and context injection

## Main Features Implemented
- OWL ontology for recommendation system
- Ontology classes and properties
- SPARQL query examples
- Vector database pipeline
- Embedding generation workflow
- RAG retrieval system
- Semantic chatbot search by author/theme

## Deliverables
- `.owl` ontology file
- GraphDB screenshots
- `sparql_owl.txt`
- Chatbot backend
- Vector DB integration

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
project-root/
│
├── backend/
│   ├── rdf/
│   ├── services/
│   ├── controllers/
│   └── sparql/
│
├── frontend/
│   ├── templates/
│   ├── static/
│   └── chatbot/
│
├── ontology/
│   ├── library.owl
│   └── sparql_owl.txt
│
├── rag/
│   ├── embeddings/
│   ├── vector-db/
│   └── retrieval/
│
└── books.rdf