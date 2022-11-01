package com.bochkarev.restbackend;

import com.bochkarev.restbackend.config.PetsServiceConfig;
import com.bochkarev.restbackend.domain.pets.Pet;
import com.bochkarev.restbackend.domain.pets.PetDto;
import com.bochkarev.restbackend.domain.pets.PetsMapper;
import com.bochkarev.restbackend.util.PetsClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.extension.responsetemplating.ResponseTemplateTransformer;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.aeonbits.owner.ConfigFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.with;
import static org.junit.jupiter.api.Assertions.assertEquals;

//@WireMockTest(httpPort = 9997)
public class PetsTest {
    protected static PetsServiceConfig config() {
        return ConfigFactory.newInstance().create(PetsServiceConfig.class, System.getProperties());
    }

    private final ObjectMapper mapper = new ObjectMapper();
    private PetsClient petsClient = new PetsClient();

    public WireMockServer wireMockServer;


    static {
        RestAssured.baseURI = config().application_host() + ":" + config().application_port();
    }

    private RequestSpecification spec = with()
            .contentType(ContentType.JSON);

    @BeforeEach
    public void setUp() throws JsonProcessingException {
        wireMockServer = new WireMockServer(options().port(config().wiremock_port()).extensions(new ResponseTemplateTransformer(true)));
        wireMockServer.start();
        createMockStub();
    }


    @AfterEach
    public void tearDown() {
        wireMockServer.stop();
    }


    private String buildRequestBodyForCreatePet(Pet pet) throws JsonProcessingException {
        return this.mapper.writeValueAsString(pet);
    }

    public void createMockStub() throws JsonProcessingException {
        wireMockServer.stubFor(post(urlEqualTo("/pet"))
                .withHeader("Content-Type", matching("application/json"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody("{{{request.body}}}")));
                        //.withBodyFile("json/request.json")));
                        //.withBody("{{{jsonPath request.body '$'}}}")));
    }

    @Test
    void createPetAndCheck() throws JsonProcessingException {
        String path = "/pet/add";
        Pet pet = petsClient.getPetCreateDto();
        System.out.println(pet);
        PetDto petDto = given()
                .spec(spec)
                .body(buildRequestBodyForCreatePet(pet))
                .when()
                .post(path)
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .extract().as(PetDto.class);
        assertEquals(pet.getId(), petDto.getId());
        assertEquals(pet.getName(), petDto.getName());
    }

    @Test
    void templateTest() {
        RestTemplate restTemplate = new RestTemplate();
        Pet pet = petsClient.getPetCreateDto();
        String petUrl = config().remote_host() + "/pet";
        HttpEntity<Pet> request = new HttpEntity<>(new Pet(pet));
        Pet petResult = restTemplate.exchange(petUrl, HttpMethod.POST, request, Pet.class).getBody();
        System.out.println(petResult);
        PetDto petDto = PetsMapper.map(petResult);
        System.out.println(petDto);
    }

}
