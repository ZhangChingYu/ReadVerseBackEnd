package com.elec5620.readverseserver.services;

import com.elec5620.readverseserver.dto.FormalDto;
import com.elec5620.readverseserver.dto.PostBookDto;
import com.elec5620.readverseserver.models.Book;
import com.elec5620.readverseserver.models.User;
import com.elec5620.readverseserver.repositories.BookRepository;
import com.elec5620.readverseserver.repositories.UserRepository;
import com.elec5620.readverseserver.utils.FileHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PublisherManageBookService {
    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    @Autowired
    public PublisherManageBookService(UserRepository userRepository, BookRepository bookRepository) {
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
    }

    public FormalDto uploadNewBook(PostBookDto bookDto){
        FormalDto result = FormalDto.builder().build();
        Optional<User> user = userRepository.findUserById(bookDto.getPublisherId());
        if(user.isPresent()){
            if(user.get().getRole().equals("Publisher")){
                // check file format, only allowed .epub
                String mimeType = bookDto.getFile().getContentType();
                if ("application/epub+zip".equals(mimeType)){
                    System.out.println("Start inserting book into database.");
                    Book book = Book.builder()
                            .publisherId(bookDto.getPublisherId())
                            .author(bookDto.getAuthor())
                            .title(bookDto.getTitle())
                            .price(bookDto.getPrice())
                            .status(false)
                            .build();
                    Book insertResult = bookRepository.save(book);
                    // upload book
                    System.out.println("Insert Complete, Ready to upload.");
                    String filePath = FileHandler.uploadNewBook(bookDto.getPublisherId(), insertResult.getId(), bookDto.getFile());
                    // check file upload success or not
                    if(filePath != null){
                        // update book file path into database
                        book.setFilePath(filePath);
                        bookRepository.save(book);
                        System.out.println("E-Book Upload Complete.");
                        result.setStatus("200");
                        result.setMessage("E-Book Upload Complete");
                        result.setData(book);
                    } else {
                        result.setStatus("500");
                        result.setMessage("Failed to save file!");
                        // todo: remove the book data from database.
                    }
                }else {
                    result.setStatus("400");
                    result.setMessage("File format not allowed, only allowed .epub file.");
                }
            }else {
                result.setStatus("403");
                result.setMessage("User don't have the authority to access this function!");
            }
        } else{
            result.setStatus("404");
            result.setMessage("No such user exists!");
        }
        return result;
    }
}
