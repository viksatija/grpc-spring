package com.grpc.spring.proto;

import com.spring.grpc.Author;
import com.spring.grpc.Book;

import java.util.ArrayList;
import java.util.List;

public class TempDb {

    public static List<Author> getAuthors(){
        return new ArrayList<Author>() {
            {
                add(Author.newBuilder().setAuthorId(1).setBookId(1).setFirstName("fTest").setLastName("lTest").setGender("Male").build());
                add(Author.newBuilder().setAuthorId(2).setBookId(2).setFirstName("fTest2").setLastName("lTest2").setGender("Female").build());
                add(Author.newBuilder().setAuthorId(3).setBookId(3).setFirstName("fTest3").setLastName("lTest3").setGender("Male").build());
                add(Author.newBuilder().setAuthorId(4).setBookId(4).setFirstName("fTest4").setLastName("lTest4").setGender("Male").build());
            }
        };
    }

    public static List<Book> getBooks(){
        return  new ArrayList<Book>(){
            {
                add(Book.newBuilder().setBookId(1).setAuthorId(1).setPrice(100).setTitle("Test Title").setPages(100).build());
                add(Book.newBuilder().setBookId(2).setAuthorId(2).setPrice(200).setTitle("Test Title 3").setPages(200).build());
                add(Book.newBuilder().setBookId(3).setAuthorId(3).setPrice(300).setTitle("Test Title 3").setPages(300).build());
                add(Book.newBuilder().setBookId(4).setAuthorId(4).setPrice(400).setTitle("Test Title 4").setPages(400).build());

                add(Book.newBuilder().setBookId(5).setAuthorId(1).setPrice(100).setTitle("Test Title 5").setPages(100).build());
                add(Book.newBuilder().setBookId(6).setAuthorId(1).setPrice(200).setTitle("Test Title 6").setPages(200).build());
                add(Book.newBuilder().setBookId(7).setAuthorId(2).setPrice(300).setTitle("Test Title 7").setPages(300).build());
                add(Book.newBuilder().setBookId(8).setAuthorId(2).setPrice(400).setTitle("Test Title 8").setPages(400).build());
            }
        };
    }
}
