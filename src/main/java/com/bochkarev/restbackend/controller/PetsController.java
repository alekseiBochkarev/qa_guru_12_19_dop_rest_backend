package com.bochkarev.restbackend.controller;

import com.bochkarev.restbackend.config.PetsServiceConfig;
import com.bochkarev.restbackend.domain.pets.Pet;
import com.bochkarev.restbackend.domain.pets.PetDto;
import com.bochkarev.restbackend.domain.pets.PetsMapper;
import io.swagger.annotations.ApiOperation;
import org.aeonbits.owner.ConfigFactory;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class PetsController {
    protected static PetsServiceConfig config() {
        return ConfigFactory.newInstance().create(PetsServiceConfig.class, System.getProperties());
    }
    RestTemplate restTemplate = new RestTemplate();

    @PostMapping("pet/add")
    @ApiOperation("Добавить pet")
    public PetDto addPet(Pet pet) {
        String petUrl = config().remote_host() + "/pet";
        HttpEntity<Pet> request = new HttpEntity<>(pet);
        Pet petResult = restTemplate.postForObject(petUrl, request, Pet.class);
        return PetsMapper.map(petResult);
    }
}
