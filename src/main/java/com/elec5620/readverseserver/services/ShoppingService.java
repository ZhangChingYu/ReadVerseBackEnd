package com.elec5620.readverseserver.services;

import com.elec5620.readverseserver.dto.AddCartDto;
import com.elec5620.readverseserver.dto.CartItemDto;
import com.elec5620.readverseserver.dto.FormalDto;
import com.elec5620.readverseserver.models.Book;
import com.elec5620.readverseserver.models.Cart;
import com.elec5620.readverseserver.models.User;
import com.elec5620.readverseserver.repositories.BookRepository;
import com.elec5620.readverseserver.repositories.CartRepository;
import com.elec5620.readverseserver.repositories.UserRepository;
import com.elec5620.readverseserver.utils.FileHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ShoppingService {
    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    @Autowired
    public ShoppingService(CartRepository cartRepository, UserRepository userRepository, BookRepository bookRepository) {
        this.cartRepository = cartRepository;
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
    }

    public FormalDto addCart(AddCartDto cartItem){
        FormalDto response = FormalDto.builder().build();
        if(cartItem == null){
            response.setStatus(400);
            response.setMessage("Cart Item cannot be empty or null!");
        } else if(cartItem.getUserId() == null){
            response.setStatus(400);
            response.setMessage("User cannot be empty or null!");
        } else if(cartItem.getBookId() == null){
            response.setStatus(400);
            response.setMessage("Book cannot be empty or null!");
        } else {
            Optional<User> user = userRepository.findUserById(cartItem.getUserId());
            Optional<Book> book = bookRepository.findById(cartItem.getBookId());
            if(user.isPresent()){
                if(!user.get().getRole().equals("Customer")){
                    response.setStatus(401);
                    response.setMessage("User has not authority accessing this function!");
                } else {
                    if(book.isPresent()){
                        Cart cart = Cart.builder()
                                .bookId(cartItem.getBookId())
                                .customerId(cartItem.getUserId())
                                .build();
                        Cart check = cartRepository.save(cart);
                        response.setStatus(201);
                        response.setMessage("Cart Item Added");
                        response.setData(check);
                    }else {
                        response.setStatus(404);
                        response.setMessage("No such book exists!");
                    }
                }
            }else{
                response.setStatus(404);
                response.setMessage("No such user exists!");
            }
        }
        return response;
    }

    public FormalDto removeCart(Long cartId){
        FormalDto response = FormalDto.builder().build();
        cartRepository.deleteById(cartId);
        if (!cartRepository.findCartById(cartId).isPresent()) {
            response.setStatus(204);
            response.setMessage("Cart Item Delete Success.");
        } else {
            response.setStatus(500);
            response.setMessage("Server Error, Please Try Again Later...");
        }
        return response;
    }

    public FormalDto getAllCartItems(Long customerId){
        FormalDto response = FormalDto.builder().build();
        Optional<User> user = userRepository.findUserById(customerId);
        if(user.isPresent()){
            if(user.get().getRole().equals("Customer")){
                response.setStatus(200);
                List<Cart> carts = cartRepository.findCartsByCustomerId(customerId);
                if(carts.isEmpty()){
                    response.setMessage("Cart is empty.");
                } else{
                    List<CartItemDto> cartItems = new ArrayList<>();
                    for(Cart cart : carts){
                        Optional<Book> book = bookRepository.findById(cart.getBookId());
                        if(book.isPresent()){
                            CartItemDto cartItem = CartItemDto.builder()
                                    .id(cart.getId())
                                    .title(book.get().getTitle())
                                    .price(book.get().getPrice())
                                    .bookId(cart.getBookId())
                                    .coverImage(FileHandler.coverImageBase64(book.get().getPublisherId(), book.get().getId()))
                                    .build();
                            cartItems.add(cartItem);
                        }
                    }
                    response.setMessage("Cart Items Found.");
                    response.setData(cartItems);
                }
            } else {
                response.setStatus(403);
                response.setMessage("User Has no authority accessing this function!");
            }
        } else {
            response.setStatus(400);
            response.setMessage("No such user exists!");
        }
        return response;
    }
}
