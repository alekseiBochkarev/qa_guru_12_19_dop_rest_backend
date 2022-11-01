package com.bochkarev.restbackend.util;

import com.bochkarev.restbackend.domain.pets.Pet;

import java.util.ArrayList;
import java.util.Arrays;

import static com.bochkarev.restbackend.util.RandomGenerator.getRandomIndex;
import static com.bochkarev.restbackend.util.RandomGenerator.getRandomString;

public class PetsClient {
    CategoriesClient categoriesClient = new CategoriesClient();
    TagsClient tagsClient = new TagsClient();

    public Pet getPetCreateDto () {
        return fillUpdateDto(new Pet());
    }

    private <T extends Pet> T fillUpdateDto(final T updateDto) {
        updateDto.setId(getRandomIndex(0, 654987124));
        updateDto.setName(getRandomString(10));
        updateDto.setStatus(getRandomString(7));
        updateDto.setPhotoUrls(new ArrayList<>(Arrays.asList(
                "ggadgadsf",
                "aahsdfasdhadfsfasdfha")));
        updateDto.setCategory(categoriesClient.getCategoryCreateDto());
        updateDto.setTags(new ArrayList<>((Arrays.asList(tagsClient.getTagCreateDto(), tagsClient
                .getTagCreateDto()))));
        return updateDto;
    }
}
