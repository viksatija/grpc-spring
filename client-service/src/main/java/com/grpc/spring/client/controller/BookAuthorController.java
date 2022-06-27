package com.grpc.spring.client.controller;

import com.google.protobuf.Descriptors;
import com.grpc.spring.client.service.BookAuthorClientService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
public class BookAuthorController {
        BookAuthorClientService bookAuthorClientService;

        @GetMapping("/author/{authorId}")
        public Map<Descriptors.FieldDescriptor, Object> getAuthor(@PathVariable String authorId){
            try {
                System.out.println("authorId = " + authorId);
                return bookAuthorClientService.getAuthor(Integer.parseInt(authorId));
            }
            catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }

    @GetMapping("/books/{authorId}")
    public List<Map<Descriptors.FieldDescriptor, Object>> getBooksByAuthor(@PathVariable String authorId){
        try {
            System.out.println("authorId = " + authorId);
            return bookAuthorClientService.getBooksByAuthor(Integer.parseInt(authorId));
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @GetMapping("/expbook")
    public Map<String,Map<Descriptors.FieldDescriptor, Object>> getExpensiveBooks(){
        try {
            return bookAuthorClientService.getExpensiveBooks();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @GetMapping("/book/author/{gender}")
    public List<Map<Descriptors.FieldDescriptor, Object>> getExpensiveBooks(@PathVariable String gender){
        try {
            return bookAuthorClientService.getBooksByAuthorGender(gender);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
