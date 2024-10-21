package com.elec5620.readverseserver.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.elec5620.readverseserver.dto.BookDto;
import com.elec5620.readverseserver.dto.FormalDto;
import com.elec5620.readverseserver.dto.PostBookDto;
import com.elec5620.readverseserver.models.Book;
import com.elec5620.readverseserver.models.User;
import com.elec5620.readverseserver.repositories.BookRepository;
import com.elec5620.readverseserver.repositories.UserRepository;
import com.elec5620.readverseserver.utils.FileHandler;

@Service
public class BookService {
    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    @Autowired
    public BookService(BookRepository bookRepository, UserRepository userRepository) {
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
    }


    // tested:
    // 1. user not found
    // 2. user is not a publisher 
    public FormalDto checkPublisher(Long publisherId) {
        FormalDto result = FormalDto.builder().build();
        Optional<User> user = userRepository.findById(publisherId);

        if (user.isEmpty()) {
            result.setStatus(404);
            result.setMessage("User not found");
            return result;
        }
        if (!user.get().getRole().equals("P")) {
            result.setStatus(403);
            result.setMessage("User not allowed");
            return result;
        }
        return null;
    }

    // tested: 
    // 1. file uploaded is not epub
    public boolean checkFileType(MultipartFile file) {
        if (file == null || file.isEmpty()) return false;
        String mineType = file.getContentType();
        return "application/epub+zip".equals(mineType); 
    }
    
    // tested:
    // 1. publisher has no book
    // 2. publisher has book(s)
    public FormalDto getBooks(Long publisherId) {
        FormalDto result = checkPublisher(publisherId);
        if (result != null) return result;

        List<Book> books = bookRepository.findBooksByPublisherId(publisherId);
        result = FormalDto.builder().status(200).build();
        if(books.isEmpty()) {
            result.setMessage("No books found for this publisher");
            result.setData(null);
        } else {
            result.setMessage("Load book successfully");
            result.setData(books);
        }
        return result;
    }
    
    // tested:
    // 1. no book found to be deleted
    // 2. user is deleteing someone else's book
    // 3. book found and deleted
    public FormalDto deleteBook(Long bookId, Long publisherId) {
        FormalDto result = checkPublisher(publisherId);
        if (result != null) return result;

        Optional<Book> book = bookRepository.findById(bookId);
        result = FormalDto.builder().build();
        if (book.isEmpty()) {
            result.setStatus(404);
            result.setMessage("Book not found");
            return result;
        }
        if (!book.get().getPublisherId().equals(publisherId)) {
            result.setStatus(403);
            result.setMessage("User not allow");
            return result;
        }

        bookRepository.delete(book.get());
        FileHandler.deleteFile(book.get().getFilePath());
        result.setStatus(200);
        result.setMessage("Book deleted");
        return result;
    }
    
    // tested:
    // 1. no search result match the query
    // 2. has matching
    // 3. keyword is null, return all books
    public FormalDto searchBook(Long publisherId, String keyword) {
        FormalDto result = checkPublisher(publisherId);
        if (result != null) return result;

        result = FormalDto.builder().status(200).build();
        if (!keyword.isEmpty()) {
            List<Book> books = bookRepository.findBooksByPublisherIdAndKeyword(publisherId, keyword);
            if (books.isEmpty()) {
                result.setMessage("No books found");
                result.setData(null);
            } else {
                result.setMessage("Books retrieved successfully");
                result.setData(books);
            }
        } else {
            List<Book> books = bookRepository.findBooksByPublisherId(publisherId);
            result.setMessage("All books retrieved successfully");
            result.setData(books);
        }
        return result;
    }


    public FormalDto addBook(PostBookDto bookDto) {
        FormalDto result = FormalDto.builder().build();

        if (!checkFileType(bookDto.getFile())) {
            result.setStatus(400);
            result.setMessage("File not allowed");
            return result;
        }

        Book book = Book.builder()
            .publisherId(bookDto.getPublisherId())
            .author(bookDto.getAuthor())
            .title(bookDto.getTitle())
            .price(bookDto.getPrice())
            .status(false)
            .build();
    
        Book saveBook = bookRepository.save(book);
        String filePath = FileHandler.uploadNewBook(bookDto.getPublisherId(), saveBook.getId(), bookDto.getFile());
        if (filePath == null) {
            result.setStatus(500);
            result.setMessage("Fail to upload file");
            return result;
        }
        book.setFilePath(filePath);
        bookRepository.save(book);
        result.setStatus(200);
        result.setMessage("E-book Upload Complete");
        result.setData(book);
        return result;    
    }


    public FormalDto editBook(BookDto bookDto) {
        FormalDto result = FormalDto.builder().build();
        Optional<Book> existingBook = bookRepository.findById(bookDto.getBookId());

        if (existingBook.isEmpty()) {
            result.setStatus(404);
            result.setMessage("Book not found");
            return result;
        }

        if (!existingBook.get().getPublisherId().equals(bookDto.getPublisherId())) {
            result.setStatus(403);
            result.setMessage("User not allow");
            return result;
        }

        Book book = existingBook.get();
        // System.out.println(bookDto.getTitle());
        book.setTitle(bookDto.getTitle());
        book.setAuthor(bookDto.getAuthor());
        book.setPrice(bookDto.getPrice());

        if (bookDto.getFile() != null && checkFileType(bookDto.getFile())) {
            String filePath = FileHandler.uploadNewBook(bookDto.getPublisherId(), book.getId(), bookDto.getFile());
            if (filePath == null) {
                result.setStatus(500);
                result.setMessage("Fail to upload file");
                return result;
            }
            book.setFilePath(filePath);
        }
        bookRepository.save(book);
        result.setStatus(200);
        result.setMessage("Book updated successfully");
        result.setData(book);
        return result;
    } 

}