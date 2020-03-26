package com.learnreactivespring.controller;

import com.learnreactivespring.domain.Item;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class ItemClientController {

    WebClient webClient = WebClient.create("http://localhost:8080");

    @GetMapping("/client/retrieve")
    public Flux<Item> getAllItemsRetrieve(){
        System.out.println("fefe");
        return webClient.get().uri("/v1/items")
                .retrieve()
                .bodyToFlux(Item.class)
                .log("get all item by retrieve");
    }

    @GetMapping("/client/exchange")
    public Flux<Item> getAllItemsExchange(){
        return webClient.get().uri("/v1/items")
                .exchange()
                .flatMapMany(clientResponse -> clientResponse.bodyToFlux(Item.class))
                .log("get all items by exchange");
    }

    @GetMapping("/client/retrieve/singleItem")
    public Mono<Item> getSingleItemRetrieve(){
        String id = "ABC";
        return webClient.get().uri("/v1/items/{id}", id)
                .retrieve()
                .bodyToMono(Item.class)
                .log("get single item by id using retrieve method");
    }
    @GetMapping("/client/exchange/singleItem")
    public Mono<Item> getSingleItemExchange(){
        String id = "ABC";
        return webClient.get().uri("/v1/items/{id}",id)
                .exchange()
                .flatMap(clientResponse -> clientResponse.bodyToMono(Item.class))
                .log("getting single item by id using exchange");
    }

    @PostMapping("/client/retrieve/createItem")
    public Mono<Item> createItemRetrieve(@RequestBody Item item){
        return  webClient.post().uri("/v1/create")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(item),Item.class)
                .retrieve()
                .bodyToMono(Item.class)
                .log("create item by retrieve");
    }


    @PostMapping("/client/exchange/createItem")
    public Mono<Item> createItemExchange(@RequestBody Item item){
        return  webClient.post().uri("/v1/create")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(item),Item.class)
                .exchange()
                .flatMap(clientResponse -> clientResponse.bodyToMono(Item.class))
                .log("create item by exchange");
    }

    @PutMapping("/client/retrieve/updateItem/{id}")
    public Mono<Item> updateItemRetrieve(@PathVariable String id,@RequestBody Item updatedItem){
        return webClient.put().uri("/v1/items/{id}",id)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(updatedItem),Item.class)
                .retrieve()
                .bodyToMono(Item.class)
                .log("item updated with retrieve method");
    }

    @PutMapping("/client/exchange/updateItem/{id}")
    public Mono<Item> updateItemExchange(@PathVariable String id, @RequestBody Item updatedItem){
        return webClient.put().uri("/v1/items/{id}",id)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(updatedItem),Item.class)
                .exchange()
                .flatMap(clientResponse -> {
                    System.out.println(clientResponse.statusCode());
                    return clientResponse.bodyToMono(Item.class);})
                .log( "item updated with retrieve method");
    }
}
