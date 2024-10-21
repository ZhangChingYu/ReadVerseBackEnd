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
import com.elec5620.readverseserver.services.ProductService;

@RestController
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/products")
    public ResponseEntity<FormalDto> getBooks(@RequestParam("publisherId") Long publisherId) {
        FormalDto respond = productService.getProduct(publisherId);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<>(respond, headers, respond.getStatus());
    }

    @PostMapping("/products/add")
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

        FormalDto respond = productService.addProduct(book);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<>(respond, headers, respond.getStatus());
    }

    @PutMapping("/products/edit")
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

        FormalDto respond = productService.editProduct(book);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<>(respond, headers, respond.getStatus());
    }

    @DeleteMapping("/products/delete")
    public ResponseEntity<FormalDto> deleteBook(@RequestParam("bookId") Long bookId, 
                                                @RequestParam("publisherId") Long publisherId){
        
        FormalDto respond = productService.deleteProduct(bookId, publisherId);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<>(respond, headers, respond.getStatus());
    }

    @GetMapping("/products/search")
    public ResponseEntity<FormalDto> searchBook(@RequestParam("publisherId") Long publisherId, 
                                                @RequestParam("keyword") String keyword) {
        FormalDto respond = productService.searchProduct(publisherId, keyword);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<>(respond, headers, respond.getStatus());
    }


}