package com.bochkarev.restbackend.util;

import com.bochkarev.restbackend.domain.pets.Category;

import static com.bochkarev.restbackend.util.RandomGenerator.getRandomIndex;
import static com.bochkarev.restbackend.util.RandomGenerator.getRandomString;

public class CategoriesClient {
    public Category getCategoryCreateDto () {
        return fillUpdateDto(new Category());
    }

    private <T extends Category> T fillUpdateDto(final T updateDto) {
        updateDto.setId(getRandomIndex(0, 6454));
        updateDto.setName(getRandomString(12));
        return updateDto;
    }
}
