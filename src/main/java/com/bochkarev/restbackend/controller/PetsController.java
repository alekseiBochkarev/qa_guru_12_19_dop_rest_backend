package com.bochkarev.restbackend.controller;

import com.bochkarev.restbackend.config.PetsServiceConfig;
import com.bochkarev.restbackend.domain.pets.Pet;
import com.bochkarev.restbackend.domain.pets.PetDto;
import com.bochkarev.restbackend.domain.pets.PetsMapper;
import io.swagger.annotations.ApiOperation;
import org.aeonbits.owner.ConfigFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.Map;

@RestController
public class PetsController {
    protected static PetsServiceConfig config() {
        return ConfigFactory.newInstance().create(PetsServiceConfig.class, System.getProperties());
    }

    RestTemplate restTemplate = new RestTemplate();

    @PostMapping("pet/add")
    @ApiOperation("Добавить pet")
    public PetDto addPet(@RequestBody Pet pet) {
        String petUrl = config().remote_host() + "/pet";
        HttpEntity<Pet> request = new HttpEntity<>(new Pet(pet));
        Pet petResult = restTemplate.exchange(petUrl, HttpMethod.POST, request, Pet.class).getBody();
       /* return PetDto.builder()
                .id(petResult.getId())
                .name(petResult.getName())
                .build();*/
        assert petResult != null;
        return PetsMapper.map(petResult);
    }

    @PutMapping("pet/update")
    @ApiOperation("update pet")
    public PetDto updatePet(@RequestBody Pet pet) {
        String petUrl = config().remote_host() + "/pet";
        HttpEntity<Pet> request = new HttpEntity<>(new Pet(pet));
        Pet petResult = restTemplate.exchange(petUrl, HttpMethod.PUT, request, Pet.class).getBody();
       /* return PetDto.builder()
                .id(petResult.getId())
                .name(petResult.getName())
                .build();*/
        assert petResult != null;
        return PetsMapper.map(petResult);
    }

    @GetMapping("pet/get/findByStatus")
    @ApiOperation("get pet by status")
    public HttpEntity<String>  getPet(@RequestParam String status) {
        String petUrl = UriComponentsBuilder.fromHttpUrl(config().remote_host() + "/pet/findByStatus")
                .queryParam("status", "{status}")
                .encode()
                .toUriString();
        Map<String, String> params = new HashMap<>();
        params.put("status", status);
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<?> request = new HttpEntity<>(headers);
        HttpEntity<String> response = restTemplate.exchange(petUrl, HttpMethod.GET, request, String.class, params);
       /* return PetDto.builder()
                .id(petResult.getId())
                .name(petResult.getName())
                .build();*/
        assert response != null;
        return response;
    }

}
