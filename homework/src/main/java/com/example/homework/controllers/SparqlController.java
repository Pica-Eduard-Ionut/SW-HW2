package com.example.homework.controllers;

import org.apache.jena.query.*;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/sparql")
public class SparqlController {

    private static final String NS = "http://example.org/book-recommendation#";
    private static final String PREFIX = "PREFIX ex: <" + NS + ">\n";

    private static final Map<String, String> QUERIES = new LinkedHashMap<>();

    static {
        QUERIES.put("1. Get all books",
            PREFIX + "SELECT ?title WHERE { ?book ex:title ?title . }");

        QUERIES.put("2. Get all Science Fiction books",
            PREFIX + "SELECT ?title WHERE { ?book ex:title ?title . ?book ex:hasTheme ex:ScienceFiction . }");

        QUERIES.put("3. Get all Beginner books",
            PREFIX + "SELECT ?title WHERE { ?book ex:title ?title . ?book ex:suitableFor ex:Beginner . }");

        QUERIES.put("4. Get all authors",
            PREFIX + "SELECT ?authorName WHERE { ?book ex:writtenBy ?author . ?author ex:name ?authorName . }");

        QUERIES.put("5. Get books and their themes",
            PREFIX + "SELECT ?title ?themeName WHERE { ?book ex:title ?title . ?book ex:hasTheme ?theme . ?theme ex:name ?themeName . }");
    }

    @GetMapping(produces = "text/html;charset=UTF-8")
    public String runAll() throws Exception {
        Model model = ModelFactory.createDefaultModel();
        model.read(new ClassPathResource("xml/ontology.owl").getInputStream(), null);

        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html><html><head><meta charset='UTF-8'>")
            .append("<title>SPARQL Queries - OWL Ontology</title>")
            .append("<style>")
            .append("body { font-family: Arial, sans-serif; padding: 30px; background: #f5f5f5; }")
            .append("h1 { color: #333; border-bottom: 2px solid #007bff; padding-bottom: 10px; }")
            .append("h2 { color: #007bff; margin-top: 30px; }")
            .append("pre { background: #2b2b2b; color: #f8f8f2; padding: 15px; border-radius: 6px; }")
            .append("table { border-collapse: collapse; width: 100%; background: white; border-radius: 6px; overflow: hidden; box-shadow: 0 1px 4px rgba(0,0,0,0.1); }")
            .append("th { background: #007bff; color: white; padding: 10px 16px; text-align: left; }")
            .append("td { padding: 9px 16px; border-bottom: 1px solid #eee; }")
            .append("tr:last-child td { border-bottom: none; }")
            .append(".section { background: white; padding: 20px; margin-bottom: 20px; border-radius: 8px; box-shadow: 0 1px 4px rgba(0,0,0,0.1); }")
            .append("</style></head><body>")
            .append("<h1>SPARQL Queries on OWL Ontology</h1>")
            .append("<p>Ontology: <code>http://example.org/book-recommendation#</code></p>");

        for (Map.Entry<String, String> entry : QUERIES.entrySet()) {
            html.append("<div class='section'>")
                .append("<h2>Query ").append(entry.getKey()).append("</h2>")
                .append("<pre>").append(entry.getValue()).append("</pre>");

            try (QueryExecution qe = QueryExecutionFactory.create(
                    QueryFactory.create(entry.getValue()), model)) {
                ResultSet rs = qe.execSelect();
                List<String> vars = rs.getResultVars();

                html.append("<table><thead><tr>");
                for (String v : vars) html.append("<th>?").append(v).append("</th>");
                html.append("</tr></thead><tbody>");

                int count = 0;
                while (rs.hasNext()) {
                    QuerySolution sol = rs.nextSolution();
                    html.append("<tr>");
                    for (String v : vars) {
                        String val = sol.get(v) != null ? sol.get(v).toString() : "";
                        if (val.contains("#")) val = val.substring(val.lastIndexOf('#') + 1);
                        html.append("<td>").append(val).append("</td>");
                    }
                    html.append("</tr>");
                    count++;
                }

                if (count == 0) html.append("<tr><td colspan='").append(vars.size()).append("'><i>No results</i></td></tr>");
                html.append("</tbody></table>");
            }

            html.append("</div>");
        }

        html.append("</body></html>");
        return html.toString();
    }
}
