package com.bochkarev.restbackend.controller;

import com.bochkarev.restbackend.domain.Author;
import com.bochkarev.restbackend.domain.BookInfo;
import com.bochkarev.restbackend.exception.DuplicateAuthorException;
import com.bochkarev.restbackend.exception.DuplicateBookException;
import com.bochkarev.restbackend.exception.NullAuthorException;
import com.bochkarev.restbackend.exception.NullBookException;
import com.bochkarev.restbackend.exception.FileNotFoundException;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class LibraryController {

    private Map<Integer, BookInfo> books = new HashMap<>();
    private Map<Integer, Author> authors = new HashMap<>();

    @GetMapping("book/getall")
    @ApiOperation("Получение всех книг")
    public List<BookInfo> getAllBooks () {
        return books.entrySet()
                .stream()
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());
    }

    @GetMapping("author/getall")
    @ApiOperation("Получение всех авторов")
    public List<Author> getAllAuthors () {
        return authors.entrySet()
                .stream()
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());
    }

    @GetMapping("author/getbooks")
    @ApiOperation("Получение всех книг автора")
    public List<BookInfo> getAuthorsBooks (@RequestBody Author author) {
        return books.entrySet()
                    .stream()
                    .filter(books -> books.getValue().getAuthor().getFirstName().equals(author.getFirstName())
                            & books.getValue().getAuthor().getSecondName().equals(author.getSecondName()))
                    .map(Map.Entry::getValue)
                    .collect(Collectors.toList());
    }

    @GetMapping("book/find")
    @ApiOperation("Поиск книги по автору")
    public BookInfo findBook (@RequestBody BookInfo bookInfo) throws FileNotFoundException {
        Optional<Integer> result = books.entrySet()
                .stream()
                .filter(entry -> bookInfo.equals(entry.getValue()))
                .map(Map.Entry::getKey)
                .findFirst();
        if (result.isPresent()) {
            return BookInfo.builder()
                    .bookName(books.get(result.get()).getBookName())
                    .author(books.get(result.get()).getAuthor())
                    .id(books.get(result.get()).getId())
                    .build();
        } else {
            throw new FileNotFoundException();
        }
    }

    @PostMapping("book/add")
    @ApiOperation("Добавить книгу")
    public BookInfo addBook (@RequestBody BookInfo bookInfo) throws DuplicateBookException, NullBookException {
        if (bookInfo.getBookName().equals("")) {
            throw new NullBookException();
        }
        else if(books.containsValue(bookInfo)){
            throw new DuplicateBookException();
        } else {
            books.put(books.size(), BookInfo.builder().bookName(bookInfo.getBookName())
                    .author(bookInfo.getAuthor()).build());
            if (bookInfo.getAuthor() != null) {
                if (!authors.containsValue(bookInfo.getAuthor())) {
                    authors.put(authors.size(), Author.builder().firstName(bookInfo.getAuthor().getFirstName())
                            .secondName(bookInfo.getAuthor().getSecondName()).build());
                }
            }
            return BookInfo.builder()
                    .bookName(bookInfo.getBookName())
                    .author(bookInfo.getAuthor())
                    .id(authors.size())
                    .build();
        }
    }

    @PostMapping("author/add")
    @ApiOperation("добавить автора")
    public Author addAuthor (@RequestBody Author author) throws NullAuthorException, DuplicateAuthorException {
        if(author.getFirstName().equals("") & author.getSecondName().equals("")) {
            throw new NullAuthorException();
        } else if (authors.containsValue(author)) {
            throw new DuplicateAuthorException();
        } else {
            authors.put(authors.size(), Author.builder().firstName(author.getFirstName())
                    .secondName(author.getSecondName()).build());
            return Author.builder()
                    .firstName(author.getFirstName())
                    .secondName(author.getSecondName())
                    .build();
        }
    }
}
