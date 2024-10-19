package com.elec5620.readverseserver.services;

import com.elec5620.readverseserver.dto.BookDetailDto;
import com.elec5620.readverseserver.dto.FormalDto;
import com.elec5620.readverseserver.dto.PostBookDto;
import com.elec5620.readverseserver.dto.ProductDto;
import com.elec5620.readverseserver.models.Book;
import com.elec5620.readverseserver.models.User;
import com.elec5620.readverseserver.repositories.BookRepository;
import com.elec5620.readverseserver.repositories.UserRepository;
import com.elec5620.readverseserver.utils.FileHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

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
                            .timestamp(new Date())
                            .build();
                    Book insertResult = bookRepository.save(book);
                    // upload book
                    System.out.println("Insert Complete, Ready to upload.");
                    String filePath = FileHandler.uploadNewBook(bookDto.getPublisherId(), insertResult.getId(), bookDto.getFile());
                    // check file upload success or not
                    if(filePath != null){
                        System.out.println("E-Book Upload Complete.");
                        // update book file path into database
                        HashMap<String, String> ebookInfo = FileHandler.getBookUploadInfo(bookDto.getPublisherId(), book.getId());
                        book.setAuthor(ebookInfo.get("authors"));
                        book.setTitle(ebookInfo.get("title"));
                        bookRepository.save(book);
                        System.out.println("E-Book Data Update Complete");
                        result.setStatus(200);
                        result.setMessage("E-Book Upload Complete");
                        result.setData(book);
                    } else {
                        result.setStatus(500);
                        result.setMessage("Failed to save file!");
                        // remove the book data from database.
                        bookRepository.delete(book);
                    }
                }else {
                    result.setStatus(400);
                    result.setMessage("File format not allowed, only allowed .epub file.");
                }
            }else {
                result.setStatus(403);
                result.setMessage("User don't have the authority to access this function!");
            }
        } else{
            result.setStatus(404);
            result.setMessage("No such user exists!");
        }
        return result;
    }

    public FormalDto getAllBooks(Long publisherId){
        FormalDto response = FormalDto.builder().build();
        if(publisherId == null){
            response.setStatus(400);
            response.setMessage("User Id cannot be empty or null!");
        } else if (userRepository.findUserById(publisherId).isPresent()) {
            response.setStatus(200);
            response.setMessage("Books found.");
            List<Book> books = bookRepository.findBooksByPublisherId(publisherId);
            if(books.isEmpty()){
                response.setData(books);
            } else {
                // pack ebooks into ProductDto
                List<ProductDto> products = new ArrayList<>();
                for(Book book : books){
                    ProductDto product = ProductDto.builder()
                            .id(book.getId())
                            .title(book.getTitle())
                            .price(book.getPrice())
                            .uploadDate(book.getTimestamp())
                            .coverImg(FileHandler.coverImageBase64(publisherId, book.getId()))
                            .build();
                    products.add(product);
                }
                response.setData(products);
            }
        } else {
            response.setStatus(404);
            response.setMessage("User not found or User is not authorized!");
        }
        return response;
    }

    public FormalDto getBookDetail(Long publisherId, Long bookId){
        FormalDto response = FormalDto.builder().build();
        if(publisherId==null || bookId==null){
            response.setStatus(400);
            response.setMessage("Publisher and Book ids can not be empty or null!");
        } else {
            Optional<Book> book = bookRepository.findBookByIdAndPublisherId(bookId, publisherId);
            if(book.isPresent()){
                response.setStatus(200);
                response.setMessage("Book found.");
                BookDetailDto bookDetail = BookDetailDto.builder()
                        .id(bookId)
                        .publisherId(publisherId)
                        .uploadDate(book.get().getTimestamp())
                        .author(book.get().getAuthor())
                        .price(book.get().getPrice())
                        .title(book.get().getTitle())
                        .onShelf(book.get().getStatus())
                        .chapters(FileHandler.getChapters(publisherId, bookId))
                        .coverImage(FileHandler.coverImageBase64(publisherId, bookId))
                        .summary(FileHandler.getBookSummary(publisherId, bookId))
                        .build();
                response.setData(bookDetail);
            }else{
                response.setStatus(404);
                response.setMessage("No such book exists!");
            }
        }
        return response;
    }

}
