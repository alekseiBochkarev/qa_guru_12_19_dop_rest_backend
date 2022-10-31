package com.bochkarev.restbackend.domain.pets;

public class PetsMapper {
    public static PetDto map(Pet pet) {
        PetDto petDto = new PetDto();
        petDto.setId(pet.getId());
        petDto.setName(pet.getName());
        return petDto;
    }
}
