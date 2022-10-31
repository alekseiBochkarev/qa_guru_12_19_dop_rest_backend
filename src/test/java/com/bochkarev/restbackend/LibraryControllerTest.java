package com.bochkarev.restbackend;

import com.bochkarev.restbackend.config.PetsServiceConfig;
import com.bochkarev.restbackend.domain.Author;
import com.bochkarev.restbackend.domain.BookInfo;
import com.bochkarev.restbackend.domain.pets.Pet;
import com.bochkarev.restbackend.domain.pets.PetDto;
import com.bochkarev.restbackend.domain.pets.PetsMapper;
import com.bochkarev.restbackend.tools.Tools;
import com.bochkarev.restbackend.util.PetsClient;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import net.bytebuddy.utility.RandomString;
import org.aeonbits.owner.ConfigFactory;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;


import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.with;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class LibraryControllerTest {
    protected static PetsServiceConfig config() {
        return ConfigFactory.newInstance().create(PetsServiceConfig.class, System.getProperties());
    }

    static {
        RestAssured.baseURI = config().application_host() + ":" + config().application_port();
    }
    private PetsClient petsClient = new PetsClient();

    private RequestSpecification spec = with()
            .basePath("/")
            .contentType(ContentType.JSON);

    @Test
    void checkCanAddAuthor() {
        String first_name = Tools.getRandomString(10, true, false);
        String second_name = Tools.getRandomString(10, true, false);
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
        String first_name = Tools.getRandomString(10, true, false);
        String second_name = Tools.getRandomString(10, true, false);
        HashMap bodyParams = new HashMap();
        bodyParams.put("first_name", first_name);
        bodyParams.put("second_name", second_name);
        String path = "/author/add";
        step("add author first time", () -> {
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

    @Test
    void checkCanAddBook() {
        String first_name = Tools.getRandomString(10, true, false);
        String second_name = Tools.getRandomString(10, true, false);
        String book_name = Tools.getRandomString(10, true, true);
        HashMap bodyParams = bodyForBook(first_name, second_name, book_name);
        String path = "/book/add";
        step("add book first time", () -> {
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
        });
    }

    private HashMap bodyForBook (String first_name, String second_name, String book_name) {
        BookInfo book = new BookInfo();
        book.setBookName(book_name);
        Author author = new Author();
        author.setFirstName(first_name);
        author.setSecondName(second_name);
        book.setAuthor(author);
        HashMap bodyParams = new HashMap();
        bodyParams.put("author", author);
        bodyParams.put("book_name", book_name);
        return bodyParams;
    }


    @Test
    void cannotAddBookTwice () {
        String first_name = Tools.getRandomString(10, true, false);
        String second_name = Tools.getRandomString(10, true, false);
        String book_name = Tools.getRandomString(10, true, true);
        HashMap bodyParams = bodyForBook(first_name, second_name, book_name);
        String path = "/book/add";
        step("add book first time", () -> {
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
        });
        step("try add book again", () -> {
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

    @Test
    void canFindBook () {
        String first_name = Tools.getRandomString(10, true, false);
        String second_name = Tools.getRandomString(10, true, false);
        String book_name = Tools.getRandomString(10, true, true);
        HashMap bodyParams = bodyForBook(first_name, second_name, book_name);
        step("add book", ()-> {
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
        });
        step("find book", () -> {
            String path = "/book/find";
            BookInfo bookInfo = given()
                    .spec(spec)
                    .body(bodyParams)
                    .when()
                    .get(path)
                    .then()
                    .statusCode(200)
                    .log().body()
                    .extract().as(BookInfo.class);
            assertEquals(first_name, bookInfo.getAuthor().getFirstName());
            assertEquals(second_name, bookInfo.getAuthor().getSecondName());
            assertEquals(book_name, bookInfo.getBookName());
        });
    }


}
