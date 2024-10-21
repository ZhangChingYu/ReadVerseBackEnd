package com.elec5620.readverseserver.controllers;

import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.elec5620.readverseserver.dto.FormalDto;
import com.elec5620.readverseserver.services.BookService;
import com.elec5620.readverseserver.services.CategoryService;

@RestController
public class BookController {
    private final BookService bookService;
    private final CategoryService categoryService;

    public BookController(BookService bookService, CategoryService categoryService) {
        this.bookService = bookService;
        this.categoryService = categoryService;
    }

    @GetMapping("/books") 
    public ResponseEntity<FormalDto> getBooks() {
        FormalDto respond = bookService.getBooks();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<>(respond, headers, respond.getStatus());
    }

    @GetMapping("/categories")
    public ResponseEntity<FormalDto> getCategories() {
        FormalDto respond = categoryService.getAllCategories();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<>(respond, headers, respond.getStatus());
    }

    @GetMapping("/books/search") 
    public ResponseEntity<FormalDto> searchBook(@RequestParam("keyword") String keyword) {
        FormalDto respond = bookService.getBooksByKeyword(keyword);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<>(respond, headers, respond.getStatus());
    }

    @GetMapping("/books/category")
    public ResponseEntity<FormalDto> categoryBook(@RequestParam("categoryId") List<Long> categoryId) {
        FormalDto respond = bookService.getBooksByCategories(categoryId);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<>(respond, headers, respond.getStatus());
    }

}