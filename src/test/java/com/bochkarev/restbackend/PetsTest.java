package com.bochkarev.restbackend;

import com.bochkarev.restbackend.config.PetsServiceConfig;
import com.bochkarev.restbackend.domain.pets.Pet;
import com.bochkarev.restbackend.domain.pets.PetDto;
import com.bochkarev.restbackend.util.PetsClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import lombok.SneakyThrows;
import org.aeonbits.owner.ConfigFactory;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.with;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PetsTest {
    protected static PetsServiceConfig config() {
        return ConfigFactory.newInstance().create(PetsServiceConfig.class, System.getProperties());
    }
    private final ObjectMapper mapper = new ObjectMapper();
    private PetsClient petsClient = new PetsClient();

    static {
        RestAssured.baseURI = config().application_host() + config().application_port();
    }

    private RequestSpecification spec = with()
            .contentType(ContentType.JSON);

    @SneakyThrows
    private String buildRequestBodyForCreatePet(Pet pet) {
        return this.mapper.writeValueAsString(pet);
    }

    @Test
    void createPetAndCheck () {
        String path = "/pet/add";
        Pet pet = petsClient.getPetCreateDto();
        PetDto petDto = given()
                .spec(spec)
                .body(buildRequestBodyForCreatePet(pet))
                .when()
                .post(path)
                .then()
                .log().status()
                .log().body()
                .statusCode(201)
                .extract().as(PetDto.class);
        assertEquals(pet.getId(), petDto.getId());
        assertEquals(pet.getName(), petDto.getName());
    }
}
