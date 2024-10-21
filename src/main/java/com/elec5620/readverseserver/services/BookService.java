package com.elec5620.readverseserver.services;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.elec5620.readverseserver.dto.FormalDto;
import com.elec5620.readverseserver.models.Book;
import com.elec5620.readverseserver.repositories.BookRepository;

@Service
public class BookService {
    private final BookRepository bookRepository;

    @Autowired
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public FormalDto getBooks() {
        List<Book> books = bookRepository.findAll();
        FormalDto result = FormalDto.builder().status(200).build();
        if(books.isEmpty()) {
            result.setMessage("No books in database");
            result.setData(null);
        } else {
            result.setMessage("Load book successfully");
            result.setData(books);
        }
        return result;
    }

    public FormalDto getBooksByKeyword(String keyword) {
        List<Book> books = bookRepository.findBooksByKeyword(keyword);

        FormalDto result = FormalDto.builder().status(200).build();
        if (books.isEmpty()) {
            result.setMessage("No books found");
            result.setData(null);
        } else {
            result.setMessage("Books retrieved successfully");
            result.setData(books);
        }
        return result;
    }

    public FormalDto getBooksByCategories(List<Long> categoryId) {
        List<Book> books = bookRepository.findBooksByCategories(categoryId);

        FormalDto result = FormalDto.builder().status(200).build();
        if (books.isEmpty()) {
            result.setMessage("No books under selected category(s)");
            result.setData(null);
        } else {
            result.setMessage("Books retrieved successfully");
            result.setData(books);
        }
    return result;
    }
}
