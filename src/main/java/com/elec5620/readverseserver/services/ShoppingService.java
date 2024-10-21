package com.elec5620.readverseserver.services;

import com.elec5620.readverseserver.dto.AddCartDto;
import com.elec5620.readverseserver.dto.CartItemDto;
import com.elec5620.readverseserver.dto.FormalDto;
import com.elec5620.readverseserver.dto.PlaceOrderDto;
import com.elec5620.readverseserver.models.Book;
import com.elec5620.readverseserver.models.Cart;
import com.elec5620.readverseserver.models.Order;
import com.elec5620.readverseserver.models.User;
import com.elec5620.readverseserver.repositories.BookRepository;
import com.elec5620.readverseserver.repositories.CartRepository;
import com.elec5620.readverseserver.repositories.OrderRepository;
import com.elec5620.readverseserver.repositories.UserRepository;
import com.elec5620.readverseserver.utils.FileHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class ShoppingService {
    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final OrderRepository orderRepository;
    @Autowired
    public ShoppingService(CartRepository cartRepository, UserRepository userRepository, BookRepository bookRepository, OrderRepository orderRepository) {
        this.cartRepository = cartRepository;
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
        this.orderRepository = orderRepository;
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

    public FormalDto placeOrder(List<PlaceOrderDto> newOrders){
        FormalDto response = FormalDto.builder().build();
        if(newOrders == null){
            response.setStatus(400);
            response.setMessage("Order cannot be empty or null!");
        } else if(newOrders.isEmpty()){
            response.setStatus(400);
            response.setMessage("Order cannot be empty or null!");
        } else {
            List<Order> orders = new ArrayList<>();
            for(PlaceOrderDto newOrder : newOrders){
                Optional<Book> book = bookRepository.findById(newOrder.getBookId());
                if(book.isPresent()){
                    if(book.get().getStatus()){
                        Order order = Order.builder()
                                .bookId(book.get().getId())
                                .customerId(newOrder.getCustomerId())
                                .date(new Date())
                                .price(newOrder.getPrice())
                                .paymentMethod(newOrder.getPaymentMethod())
                                .status("Pending")
                                .orderNumber(generateOrderNumber())
                                .build();
                        Order result = orderRepository.save(order);
                        System.out.println(result);
                        orders.add(result);
                    }
                }
            }
            if(orders.size() == 0){
                response.setStatus(202);
                response.setMessage("Books are not on shelf right now.");
            }else {
                response.setStatus(201);
                response.setMessage("Order Placed.");
            }
            response.setData(orders);
        }
        return response;
    }

    private String generateOrderNumber(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHssSSS");
        String localDate = LocalDateTime.now().format(formatter);
        Random random = new Random();
        int randomNumber = random.nextInt(1000);
        String randomNumerics;
        if (randomNumber < 10){
            randomNumerics = "00"+randomNumber;
        } else if (randomNumber < 100) {
            randomNumerics = "0"+randomNumber;
        } else{
            randomNumerics = String.valueOf(randomNumber);
        }
        return localDate+randomNumerics;
    }
}
