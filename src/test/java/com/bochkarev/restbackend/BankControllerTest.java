package com.bochkarev.restbackend;

import com.bochkarev.restbackend.domain.UserInfo;
import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.Test;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.restassured.RestAssured.with;

public class BankControllerTest {

    static {
        RestAssured.baseURI = "http://localhost:8080";
    }

    private RequestSpecification spec = with()
            .basePath("/user");

    @Test
    void bankControllerTest () {
        UserInfo[] userInfos = spec.get("/getall")
                .then()
                .statusCode(200)
                .extract()
                .response()
                .as(UserInfo[].class);
        UserInfo dima = Stream.of(userInfos)
                .filter(userInfo -> userInfo.getUserName().equals("Dima"))
                .peek(userInfo -> System.out.println(userInfo.getUserName()))
                //.map(userInfo -> userInfo.toString())
                //.collect(Collectors.toList());
                .findFirst()
                .orElse(new UserInfo());

        Stream.of(userInfos)
                .filter(userInfo -> userInfo.getUserName().equals("Dima"))
                .peek(userInfo -> System.out.println(userInfo.getUserName()))
                .map(userInfo -> userInfo.toString())
                .collect(Collectors.toList());
    }
}
