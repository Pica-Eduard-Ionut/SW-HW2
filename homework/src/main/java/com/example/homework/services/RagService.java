package com.example.homework.services;

import com.example.homework.models.Book;
import com.example.homework.models.VectorEntry;
import jakarta.annotation.PostConstruct;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RagService {

    private static final String NS = "http://example.org/book-recommendation#";

    private final List<VectorEntry> store = new ArrayList<>();
    private List<String> vocabulary = new ArrayList<>();
    private final Map<String, Double> idf = new HashMap<>();

    @PostConstruct
    public void init() {
        try {
            Model model = ModelFactory.createDefaultModel();
            model.read(new ClassPathResource("xml/books.rdf").getInputStream(), null);

            String queryStr = "PREFIX ex: <" + NS + "> " +
                "SELECT ?s ?title ?author ?level ?theme WHERE { " +
                "?s a ex:Book ; ex:title ?title ; ex:author ?author ; ex:readingLevel ?level . " +
                "OPTIONAL { ?s ex:theme ?theme } }";

            Map<String, Book> bookMap = new LinkedHashMap<>();
            try (QueryExecution qe = QueryExecutionFactory.create(QueryFactory.create(queryStr), model)) {
                ResultSet rs = qe.execSelect();
                while (rs.hasNext()) {
                    QuerySolution sol = rs.nextSolution();
                    String uri = sol.getResource("s").getURI();
                    if (!bookMap.containsKey(uri)) {
                        Book b = new Book();
                        b.setId(uri);
                        b.setTitle(sol.getLiteral("title").getString());
                        b.setAuthor(sol.getLiteral("author").getString());
                        b.setReadingLevel(sol.getLiteral("level").getString());
                        b.setThemes(new ArrayList<>());
                        bookMap.put(uri, b);
                    }
                    if (sol.contains("theme")) {
                        bookMap.get(uri).getThemes().add(sol.getLiteral("theme").getString());
                    }
                }
            }

            System.out.println("[RagService] Loaded " + bookMap.size() + " books from RDF");

            List<String> chunks = new ArrayList<>();
            List<String> ids   = new ArrayList<>(bookMap.keySet());
            for (Book book : bookMap.values()) {
                chunks.add(buildChunk(book));
            }

            buildVocabularyAndIdf(chunks);

            for (int i = 0; i < chunks.size(); i++) {
                store.add(new VectorEntry(ids.get(i), chunks.get(i), tfidf(chunks.get(i))));
            }

            System.out.println("[RagService] Vector store ready: " + store.size() + " entries");
            store.forEach(e -> System.out.println("  -> " + e.getText()));

        } catch (Exception e) {
            System.err.println("[RagService] Init failed: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public List<String> search(String query, int topK) {
        if (store.isEmpty()) return List.of("No books in database.");
        float[] queryVec = tfidf(query);
        return store.stream()
            .sorted(Comparator.comparingDouble(e -> -cosine(e.getEmbedding(), queryVec)))
            .limit(topK)
            .map(VectorEntry::getText)
            .collect(Collectors.toList());
    }

    private void buildVocabularyAndIdf(List<String> docs) {
        Set<String> allTerms = new LinkedHashSet<>();
        for (String doc : docs) Collections.addAll(allTerms, tokenize(doc));
        vocabulary = new ArrayList<>(allTerms);

        int n = docs.size();
        for (String term : vocabulary) {
            long df = docs.stream().filter(d -> d.toLowerCase().contains(term)).count();
            idf.put(term, Math.log((double)(n + 1) / (df + 1)) + 1.0);
        }
    }

    private float[] tfidf(String text) {
        String[] tokens = tokenize(text);
        Map<String, Long> tf = new HashMap<>();
        for (String t : tokens) tf.merge(t, 1L, Long::sum);

        float[] vec = new float[vocabulary.size()];
        for (int i = 0; i < vocabulary.size(); i++) {
            String term = vocabulary.get(i);
            double termFreq = tf.getOrDefault(term, 0L) / (double) Math.max(tokens.length, 1);
            vec[i] = (float)(termFreq * idf.getOrDefault(term, 1.0));
        }
        return vec;
    }

    private String[] tokenize(String text) {
        return text.toLowerCase().replaceAll("[^a-z0-9 ]", " ").trim().split("\\s+");
    }

    private String buildChunk(Book book) {
        return String.format("Book: %s. Author: %s. Themes: %s. Reading Level: %s.",
            book.getTitle(), book.getAuthor(),
            String.join(", ", book.getThemes()), book.getReadingLevel());
    }

    private float cosine(float[] a, float[] b) {
        float dot = 0, na = 0, nb = 0;
        for (int i = 0; i < a.length; i++) {
            dot += a[i] * b[i];
            na += a[i] * a[i];
            nb += b[i] * b[i];
        }
        if (na == 0 || nb == 0) return 0;
        return dot / (float)(Math.sqrt(na) * Math.sqrt(nb));
    }
}
