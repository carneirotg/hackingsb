package com.educative.hackingspringbootreactive;
import reactor.core.publisher.Mono;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.reactive.result.view.Rendering;


@Controller
public class HomeController {

    public static final String MY_CART = "My Cart";
    private final ItemRepository itemRepository;
    private final CartRepository cartRepository;
    private final CartService cartService;

    public HomeController(ItemRepository itemRepository, // 2
                          CartRepository cartRepository, CartService cartService) {
        this.cartService = cartService;
        this.itemRepository = itemRepository;
        this.cartRepository = cartRepository;
    }

    @GetMapping
    Mono<Rendering> home() { // 1
        return Mono.just(Rendering.view("home.html") // 2
            .modelAttribute("items",
                this.itemRepository.findAll()) // 3
            .modelAttribute("cart",
                this.cartRepository.findById(MY_CART) // 4
                    .defaultIfEmpty(new Cart(MY_CART)))
            .build());
    }

    @PostMapping("/add/{id}")
    Mono<String> addToCart(@PathVariable String id) {
        return this.cartService.addToCart(MY_CART, id)
            .thenReturn("redirect:/");
    }

}