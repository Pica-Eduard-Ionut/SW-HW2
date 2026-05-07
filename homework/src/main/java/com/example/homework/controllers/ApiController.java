package com.example.homework.controllers;

import com.example.homework.models.Book;
import org.apache.jena.rdf.model.*;
import org.apache.jena.util.FileManager;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ApiController {

    @GetMapping("/books")
    public List<Book> getBooks() {
        List<Book> books = new ArrayList<>();
        String filename = "src/main/resources/xml/books.rdf";

        Model model = ModelFactory.createDefaultModel();
        InputStream in = FileManager.get().open(filename);
        if (in != null) {
            model.read(in, null);
            String ex = "http://example.org/book-recommendation#";
            Property titleProp = model.getProperty(ex + "title");
            Property authorProp = model.getProperty(ex + "author");
            Property levelProp = model.getProperty(ex + "readingLevel");
            Property themeProp = model.getProperty(ex + "theme");

            ResIterator iter = model.listSubjectsWithProperty(model.getProperty("http://www.w3.org/1999/02/22-rdf-syntax-ns#type"),
                    model.getResource(ex + "Book"));

            while (iter.hasNext()) {
                Resource res = iter.nextResource();
                Book book = new Book();
                if(res.hasProperty(titleProp)) book.setTitle(res.getProperty(titleProp).getString());
                if(res.hasProperty(authorProp)) book.setAuthor(res.getProperty(authorProp).getString());
                if(res.hasProperty(levelProp)) book.setReadingLevel(res.getProperty(levelProp).getString());

                List<String> themes = new ArrayList<>();
                StmtIterator themeIter = res.listProperties(themeProp);
                while(themeIter.hasNext()) {
                    themes.add(themeIter.nextStatement().getString());
                }
                book.setThemes(themes);
                books.add(book);
            }
        }
        return books;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            System.out.println("Am primit fișierul cu succes: " + file.getOriginalFilename());

            return ResponseEntity.ok("Fișierul a fost primit de Spring Boot!");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("A apărut o eroare la upload.");
        }
    }
}