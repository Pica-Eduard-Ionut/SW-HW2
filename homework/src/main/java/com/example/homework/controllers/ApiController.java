package com.example.homework.controllers;

import com.example.homework.models.Book;
import com.example.homework.models.BookRequest;
import com.example.homework.services.BookService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/books")
@CrossOrigin("*")
public class ApiController {

    private final BookService service = new BookService();

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
        return ResponseEntity.ok("Book added");
    }

    @PutMapping
    public ResponseEntity<String> updateBook(@RequestParam String id, @RequestBody BookRequest req) {
        service.updateBook(id, req);
        return ResponseEntity.ok("Book updated");
    }

    @DeleteMapping
    public ResponseEntity<String> deleteBook(@RequestParam String id) {
        service.deleteBook(id);
        return ResponseEntity.ok("Book deleted");
    }

    @PostMapping("/upload")
    public ResponseEntity<String> upload(@RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok("Uploaded: " + file.getOriginalFilename());
    }
}