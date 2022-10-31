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
                "https://www.google.com/url?sa=i&url=https%3A%2F%2Fwww.istockphoto.com%2Fphotos%2Fshark&psig=AOvVaw1fcXANt0Qm8Pp8m755bOdL&ust=1667284204418000&source=images&cd=vfe&ved=0CAwQjRxqFwoTCJCzrefrifsCFQAAAAAdAAAAABAP",
                "https://www.google.com/url?sa=i&url=https%3A%2F%2Fpixabay.com%2Fimages%2Fsearch%2Fdog%2F&psig=AOvVaw0pCnPurvF0JBGfmYNZxaBq&ust=1667285495048000&source=images&cd=vfe&ved=0CAwQjRxqFwoTCJDx4M7wifsCFQAAAAAdAAAAABAE")));
        updateDto.setCategory(categoriesClient.getCategoryCreateDto());
        updateDto.setTags(new ArrayList<>((Arrays.asList(tagsClient.getTagCreateDto(), tagsClient
                .getTagCreateDto()))));
        return updateDto;
    }
}
