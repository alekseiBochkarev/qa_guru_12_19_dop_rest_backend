package com.bochkarev.restbackend;

import com.bochkarev.restbackend.domain.Author;
import com.bochkarev.restbackend.domain.BookInfo;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import net.bytebuddy.utility.RandomString;
import org.junit.jupiter.api.Test;

import java.util.HashMap;


import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.with;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class LibraryControllerTest {

    static {
        RestAssured.baseURI = "http://localhost:8080";
    }

    private RequestSpecification spec = with()
            .basePath("/")
            .contentType(ContentType.JSON);

    @Test
    void checkCanAddAuthor() {
        String first_name = "Lev";
        String second_name = "Tolstoy";
        HashMap bodyParams = new HashMap();
        bodyParams.put("first_name", first_name);
        bodyParams.put("second_name", second_name);
        String path = "/author/add";
        Author author = given()
                .spec(spec)
                .body(bodyParams)
                .when()
                .post(path)
                .then()
                .statusCode(200)
                .log().body()
                .extract().as(Author.class);
        assertEquals(first_name, author.getFirstName());
        assertEquals(second_name, author.getSecondName());
    }

    @Test
    void cannotAddAuthorTwice () {
        String first_name = String.valueOf(new RandomString(10));
        String second_name = String.valueOf(new RandomString(10));
        HashMap bodyParams = new HashMap();
        bodyParams.put("first_name", first_name);
        bodyParams.put("second_name", second_name);
        step("add author first time", () -> {
            String path = "/author/add";
            Author author = given()
                    .spec(spec)
                    .body(bodyParams)
                    .when()
                    .post(path)
                    .then()
                    .statusCode(200)
                    .log().body()
                    .extract().as(Author.class);
            assertEquals(first_name, author.getFirstName());
            assertEquals(second_name, author.getSecondName());
        });
        step("try add author ones again", () -> {
            String path = "/author/add";
            given()
                    .spec(spec)
                    .body(bodyParams)
                    .when()
                    .post(path)
                    .then()
                    .statusCode(423)
                    .log().body();
        });
    }
/*
    @Test
    void checkCanAddBook() {
        String first_name = "Lev";
        String second_name = "Tolstoy";
        String book_name = "My book";
        HashMap bodyParams = new HashMap();
        bodyParams.put("author.first_name", first_name);
        bodyParams.put("author.second_name", second_name);
        bodyParams.put("book_name", book_name);
        String path = "/book/add";
        BookInfo bookInfo = given()
                .spec(spec)
                .body(bodyParams)
                .when()
                .post(path)
                .then()
                .statusCode(200)
                .log().body()
                .extract().as(BookInfo.class);
        assertEquals(first_name, bookInfo.getAuthor().getFirstName());
        assertEquals(second_name, bookInfo.getAuthor().getSecondName());
        assertEquals(book_name, bookInfo.getBookName());
    }

 */
}
