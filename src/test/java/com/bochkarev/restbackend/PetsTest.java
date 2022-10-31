package com.bochkarev.restbackend;

import com.bochkarev.restbackend.config.PetsServiceConfig;
import com.bochkarev.restbackend.domain.pets.Pet;
import com.bochkarev.restbackend.domain.pets.PetDto;
import com.bochkarev.restbackend.util.PetsClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.extension.responsetemplating.ResponseTemplateTransformer;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.aeonbits.owner.ConfigFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.with;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PetsTest {
    protected static PetsServiceConfig config() {
        return ConfigFactory.newInstance().create(PetsServiceConfig.class, System.getProperties());
    }
    private final ObjectMapper mapper = new ObjectMapper();
    private PetsClient petsClient = new PetsClient();
    private WireMockServer wireMockServer;

    static {
        RestAssured.baseURI = config().application_host() + ":" + config().application_port();
    }

    private RequestSpecification spec = with()
            .contentType(ContentType.JSON);

    @BeforeAll
    public void SetUp() {
        wireMockServer = new WireMockServer(options().port(config().wiremock_port()).extensions(new ResponseTemplateTransformer(true)));
        wireMockServer.start();
        createMockStub();
    }

    @AfterAll
    public void tearDown() {
        wireMockServer.stop();
    }


    private String buildRequestBodyForCreatePet(Pet pet) throws JsonProcessingException {
        return this.mapper.writeValueAsString(pet);
    }

    @Step
    public void createMockStub() {
        wireMockServer.stubFor(post(urlPathEqualTo("/pet"))
                .willReturn(aResponse()
                        .withBody("{{request.body}}")
                        .withStatus(200)));

    }

    @Test
    void createPetAndCheck() throws JsonProcessingException {
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
