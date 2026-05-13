package com.example.homework.services;

import com.example.homework.models.Book;
import com.example.homework.models.BookRequest;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.*;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.vocabulary.RDF;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BookService {
    private static final String NS = "http://example.org/book-recommendation#";
    private static final String BOOK_URI = "http://example.org/book/";

    public static String activeRdfPath = null;

    private String getRdfPath() {
        if (activeRdfPath != null) return activeRdfPath;
        URL url = getClass().getClassLoader().getResource("xml/books.rdf");
        if (url == null) throw new RuntimeException("books.rdf not found on classpath");
        return url.getPath().replace("/target/classes/", "/src/main/resources/");
    }

    private String getXmlDir() {
        URL url = getClass().getClassLoader().getResource("xml/books.rdf");
        if (url == null) throw new RuntimeException("xml dir not found");
        return new java.io.File(url.getPath().replace("/target/classes/", "/src/main/resources/")).getParent();
    }

    private Model loadModel() {
        Model model = ModelFactory.createDefaultModel();
        RDFDataMgr.read(model, getRdfPath());
        return model;
    }

    private void saveModel(Model model) {
        try (FileOutputStream out = new FileOutputStream(getRdfPath())) {
            RDFDataMgr.write(out, model, Lang.RDFXML);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<Book> getAllBooks() {
        Model model = loadModel();
        String queryStr = "PREFIX ex: <" + NS + "> SELECT ?s ?title ?author ?level ?theme WHERE { ?s a ex:Book ; ex:title ?title ; ex:author ?author ; ex:readingLevel ?level . OPTIONAL { ?s ex:theme ?theme } }";
        Query query = QueryFactory.create(queryStr);
        QueryExecution qexec = QueryExecutionFactory.create(query, model);
        ResultSet rs = qexec.execSelect();

        List<Book> books = new ArrayList<>();

        while (rs.hasNext()) {
            QuerySolution sol = rs.nextSolution();
            String uri = sol.getResource("s").getURI();

            Book existing = books.stream()
                    .filter(b -> b.getId().equals(uri))
                    .findFirst()
                    .orElse(null);

            if (existing == null) {
                existing = new Book();
                existing.setId(uri);
                existing.setTitle(sol.getLiteral("title").getString());
                existing.setAuthor(sol.getLiteral("author").getString());
                existing.setReadingLevel(sol.getLiteral("level").getString());
                existing.setThemes(new ArrayList<>());
                books.add(existing);
            }

            if (sol.contains("theme")) {
                existing.getThemes().add(sol.getLiteral("theme").getString());
            }
        }

        return books;
    }

    public Book getBookById(String id) {
        Model model = loadModel();
        String queryStr = "PREFIX ex: <" + NS + "> SELECT ?title ?author ?level ?theme WHERE { <" + id + "> ex:title ?title ; ex:author ?author ; ex:readingLevel ?level . OPTIONAL { <" + id + "> ex:theme ?theme } }";
        Query query = QueryFactory.create(queryStr);
        QueryExecution qexec = QueryExecutionFactory.create(query, model);
        ResultSet rs = qexec.execSelect();
        Book book = null;
        List<String> themes = new ArrayList<>();

        while (rs.hasNext()) {
            QuerySolution sol = rs.nextSolution();
            if (book == null) {
                book = new Book();
                book.setId(id);
                book.setTitle(sol.getLiteral("title").getString());
                book.setAuthor(sol.getLiteral("author").getString());
                book.setReadingLevel(sol.getLiteral("level").getString());
            }

            if (sol.contains("theme")) {
                themes.add(sol.getLiteral("theme").getString());
            }
        }

        if (book != null) {
            book.setThemes(themes);
        }

        return book;
    }

    public void addBook(BookRequest req) {
        Model model = loadModel();
        String id = BOOK_URI + UUID.randomUUID();
        Resource book = model.createResource(id);
        Property title = model.createProperty(NS + "title");
        Property author = model.createProperty(NS + "author");
        Property level = model.createProperty(NS + "readingLevel");
        Property theme = model.createProperty(NS + "theme");
        book.addProperty(RDF.type, model.createResource(NS + "Book"));
        book.addProperty(title, req.getTitle() == null ? "" : req.getTitle());
        book.addProperty(author, req.getAuthor() == null ? "" : req.getAuthor());
        book.addProperty(level, req.getReadingLevel() == null ? "" : req.getReadingLevel());

        if (req.getThemes() != null && !req.getThemes().isEmpty()) {
            for (String t : req.getThemes()) {
                if (t != null && !t.trim().isEmpty()) {
                    book.addProperty(theme, t.trim());
                }
            }
        }

        saveModel(model);
    }

    public void updateBook(String id, BookRequest req) {
        Model model = loadModel();
        Resource book = model.getResource(id);
        Property title = model.createProperty(NS + "title");
        Property author = model.createProperty(NS + "author");
        Property level = model.createProperty(NS + "readingLevel");
        Property theme = model.createProperty(NS + "theme");
        model.removeAll(book, title, null);
        model.removeAll(book, author, null);
        model.removeAll(book, level, null);
        model.removeAll(book, theme, null);
        book.addProperty(RDF.type, model.createResource(NS + "Book"));
        book.addProperty(title, req.getTitle() == null ? "" : req.getTitle());
        book.addProperty(author, req.getAuthor() == null ? "" : req.getAuthor());
        book.addProperty(level, req.getReadingLevel() == null ? "" : req.getReadingLevel());

        if (req.getThemes() != null && !req.getThemes().isEmpty()) {
            for (String t : req.getThemes()) {
                if (t != null && !t.trim().isEmpty()) {
                    book.addProperty(theme, t.trim());
                }
            }
        }

        saveModel(model);
    }

    public void loadFromUpload(InputStream in, String filename) {
        Model uploaded = ModelFactory.createDefaultModel();
        RDFDataMgr.read(uploaded, in, Lang.RDFXML);
        activeRdfPath = getXmlDir() + java.io.File.separator + filename;
        saveModel(uploaded);
    }

    public void deleteBook(String id) {
        Model model = loadModel();
        Resource book = model.getResource(id);
        model.removeAll(book, null, (RDFNode) null);
        saveModel(model);
    }
}