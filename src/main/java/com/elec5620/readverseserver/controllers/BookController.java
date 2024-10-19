package com.elec5620.readverseserver.controllers;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.elec5620.readverseserver.dto.BookDto;
import com.elec5620.readverseserver.dto.FormalDto;
import com.elec5620.readverseserver.dto.PostBookDto;
import com.elec5620.readverseserver.services.BookService;

@RestController
public class BookController {
    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/books")
    public ResponseEntity<FormalDto> getBooks(@RequestParam("publisherId") Long publisherId) {
        FormalDto respond = bookService.getBooks(publisherId);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<>(respond, headers, respond.getStatus());
    }

    @PostMapping("/books/new")
    public ResponseEntity<FormalDto> addBook(
            @RequestParam("publisherId") Long publisherId, 
            @RequestParam("author") String author, 
            @RequestParam("price") Double price, 
            @RequestParam("title") String title, 
            @RequestParam("file")MultipartFile file) {

        PostBookDto book = PostBookDto.builder()
            .publisherId(publisherId)
            .author(author)
            .price(price)
            .title(title)
            .file(file)
            .build();

        FormalDto respond = bookService.addBook(book);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<>(respond, headers, respond.getStatus());
    }

    @PutMapping("/books/edit")
    public ResponseEntity<FormalDto> editBook(
            @RequestParam("bookId") Long bookId,
            @RequestParam("publisherId") Long publisherId, 
            @RequestParam("author") String author, 
            @RequestParam("price") Double price, 
            @RequestParam("title") String title, 
            @RequestParam("file")MultipartFile file) {
        
        BookDto book = BookDto.builder()
            .bookId(bookId)
            .publisherId(publisherId)
            .author(author)
            .price(price)
            .title(title)
            .file(file)
            .build();

        FormalDto respond = bookService.editBook(book);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<>(respond, headers, respond.getStatus());
    }

    @DeleteMapping("/books/delete")
    public ResponseEntity<FormalDto> deleteBook(@RequestParam("bookId") Long bookId, 
                                                @RequestParam("publisherId") Long publisherId){
        
        FormalDto respond = bookService.deleteBook(bookId, publisherId);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<>(respond, headers, respond.getStatus());
    }

    @GetMapping("/books/search")
    public ResponseEntity<FormalDto> searchBook(@RequestParam("publisherId") Long publisherId, 
                                                @RequestParam("keyword") String keyword) {
        FormalDto respond = bookService.searchBook(publisherId, keyword);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<>(respond, headers, respond.getStatus());
    }

    /*
    @PostMapping("/test")
    public ResponseEntity<String> testEndpoint() {
        return ResponseEntity.ok("Controller is working");
    }
    */
}