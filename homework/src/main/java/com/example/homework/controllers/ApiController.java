package com.example.homework.controllers;

import com.example.homework.models.Book;
import com.example.homework.models.BookRequest;
import com.example.homework.services.BookService;
import com.example.homework.services.RagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/books")
@CrossOrigin("*")
public class ApiController {

    private final BookService service = new BookService();

    @Autowired
    private RagService ragService;

    @GetMapping
    public List<Book> getAllBooks() { return service.getAllBooks(); }

    @GetMapping("/details")
    public ResponseEntity<Book> getBook(@RequestParam String id) {
        Book book = service.getBookById(id);
        return book == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(book);
    }

    @PostMapping
    public ResponseEntity<String> addBook(@RequestBody BookRequest req) {
        service.addBook(req);
        ragService.rebuild();
        return ResponseEntity.ok("Book added");
    }

    @PutMapping
    public ResponseEntity<String> updateBook(@RequestParam String id, @RequestBody BookRequest req) {
        service.updateBook(id, req);
        ragService.rebuild();
        return ResponseEntity.ok("Book updated");
    }

    @DeleteMapping
    public ResponseEntity<String> deleteBook(@RequestParam String id) {
        service.deleteBook(id);
        ragService.rebuild();
        return ResponseEntity.ok("Book deleted");
    }

    @PostMapping("/upload")
    public ResponseEntity<String> upload(@RequestParam("file") MultipartFile file) {
        try {
            service.loadFromUpload(file.getInputStream(), file.getOriginalFilename());
            ragService.rebuild();
            return ResponseEntity.ok("Uploaded: " + file.getOriginalFilename());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to load RDF: " + e.getMessage());
        }
    }
}