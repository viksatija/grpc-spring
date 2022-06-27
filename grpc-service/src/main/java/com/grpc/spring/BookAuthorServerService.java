package com.grpc.spring;

import com.grpc.spring.proto.TempDb;
import com.spring.grpc.*;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

import java.util.ArrayList;
import java.util.List;

@GrpcService
public class BookAuthorServerService extends BookAuthorServiceGrpc.BookAuthorServiceImplBase {

    @Override
    public void getAuthor(Author request, StreamObserver<Author> responseObserver) {;
        TempDb.getAuthors()
                .stream()
                .filter(author -> author.getAuthorId() == request.getAuthorId())
                .findFirst().ifPresent((responseObserver::onNext));
        responseObserver.onCompleted();
    }

    @Override
    public void getBooksByAuthor(Author request, StreamObserver<Book> responseObserver) {
        TempDb.getBooks().stream()
                .filter(book -> book.getAuthorId() == request.getAuthorId())
                .forEach(responseObserver::onNext); // this will not wait for each to complete . here we are streaming data in real time.
        responseObserver.onCompleted();
    }

    @Override
    public StreamObserver<Book> getExpensiveBooks(StreamObserver<Book> responseObserver) {
        return new StreamObserver<Book>() {
            Book expensiveBook = null;
            float price = 0;
            @Override
            public void onNext(Book value) {
                System.out.println(value.getPrice());
                if(value.getPrice() >= price){
                    System.out.println(value.getPrice());
                    price = value.getPrice();
                    expensiveBook = value;
                }
            }

            @Override
            public void onError(Throwable t) {
                responseObserver.onError(t);
            }

            @Override
            public void onCompleted() {
                responseObserver.onNext(expensiveBook);
                responseObserver.onCompleted();
            }
        };
    }

    @Override
    public StreamObserver<Book> getBooksByAuthorGender(StreamObserver<Book> responseObserver) {
        return new StreamObserver<Book>() {
            List<Book> list = new ArrayList<>();
            @Override
            public void onNext(Book value) {
                TempDb.getBooks().stream().filter(book -> book.getAuthorId() == value.getAuthorId()).forEach(list::add);
            }

            @Override
            public void onError(Throwable t) {
                responseObserver.onError(t);
            }

            @Override
            public void onCompleted() {
                list.forEach(responseObserver::onNext);
                responseObserver.onCompleted();
            }
        };
    }

    @Override
    public void getAllBooks(Empty request, StreamObserver<BookList> responseObserver) {
        BookList obj = BookList.newBuilder()
                .addBook(Book.newBuilder().setBookId(1).setAuthorId(1).setPrice(100).setTitle("Test Title 1").setPages(100).build())
                .addBook(Book.newBuilder().setBookId(1).setAuthorId(2).setPrice(400).setTitle("Test Title 2").setPages(200).build())
                .addBook(Book.newBuilder().setBookId(1).setAuthorId(3).setPrice(600).setTitle("Test Title 3").setPages(300).build())
                .build();
        responseObserver.onNext(obj);
        responseObserver.onCompleted();
    }
}
