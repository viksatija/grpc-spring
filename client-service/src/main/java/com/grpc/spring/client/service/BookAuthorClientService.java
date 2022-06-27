package com.grpc.spring.client.service;

import com.google.protobuf.Descriptors;
import com.grpc.spring.proto.TempDb;
import com.spring.grpc.Author;
import com.spring.grpc.Book;
import com.spring.grpc.BookAuthorServiceGrpc;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@Service
public class BookAuthorClientService {

    @GrpcClient("grpc-spring-service")
    private BookAuthorServiceGrpc.BookAuthorServiceBlockingStub synchronousClient;

    public Map<Descriptors.FieldDescriptor, Object> getAuthor(int authorId){
        Author authorRequest = Author.newBuilder().setAuthorId(authorId).build();
        Author response = synchronousClient.getAuthor(authorRequest);
        return response.getAllFields();
    }

    @GrpcClient("grpc-spring-service")
    private BookAuthorServiceGrpc.BookAuthorServiceStub asynchronousClient;

    public List<Map<Descriptors.FieldDescriptor, Object>> getBooksByAuthor(int authorId) throws InterruptedException {
        final CountDownLatch latch = new CountDownLatch(1);
        Author authorRequest = Author.newBuilder().setAuthorId(authorId).build();
        List<Map<Descriptors.FieldDescriptor, Object>> list = new ArrayList<>();
        asynchronousClient.getBooksByAuthor(authorRequest, new StreamObserver<Book>() {
            @Override
            public void onNext(Book value) {
                list.add(value.getAllFields());
            }

            @Override
            public void onError(Throwable t) {
                latch.countDown();
            }

            @Override
            public void onCompleted() {
                latch.countDown();
            }
        });

        boolean await = latch.await(1, TimeUnit.MINUTES);
        return await ? list : Collections.emptyList();
    }

    public Map<String, Map<Descriptors.FieldDescriptor, Object>> getExpensiveBooks() throws InterruptedException {
        final CountDownLatch latch = new CountDownLatch(1);
        final Map<String, Map<Descriptors.FieldDescriptor, Object>> map = new HashMap<>();
        StreamObserver<Book> response = asynchronousClient.getExpensiveBooks(new StreamObserver<Book>() {
            @Override
            public void onNext(Book value) {
                map.put("ExpBook", value.getAllFields());
            }

            @Override
            public void onError(Throwable t) {
                latch.countDown();
            }

            @Override
            public void onCompleted() {
                latch.countDown();
            }
        });
        TempDb.getBooks().forEach(response::onNext);
        response.onCompleted();
        boolean await = latch.await(1, TimeUnit.MINUTES);
        return await ? map : Collections.emptyMap();
    }

    public List<Map<Descriptors.FieldDescriptor, Object>> getBooksByAuthorGender(String gender) throws InterruptedException {
        final CountDownLatch latch = new CountDownLatch(1);
        final List<Map<Descriptors.FieldDescriptor, Object>> list = new ArrayList<>();
        StreamObserver<Book> observer = asynchronousClient.getBooksByAuthorGender(new StreamObserver<Book>() {
            @Override
            public void onNext(Book value) {
                list.add(value.getAllFields());
            }

            @Override
            public void onError(Throwable t) {
                latch.countDown();
            }

            @Override
            public void onCompleted() {
                latch.countDown();
            }


        });
        TempDb.getAuthors().stream().filter(author -> author.getGender().equalsIgnoreCase(gender))
                .forEach(author -> observer.onNext(Book.newBuilder().setAuthorId(author.getAuthorId()).build()));
        observer.onCompleted();

        boolean await = latch.await(1, TimeUnit.MINUTES);
        return await ? list : Collections.emptyList();
    }

}
