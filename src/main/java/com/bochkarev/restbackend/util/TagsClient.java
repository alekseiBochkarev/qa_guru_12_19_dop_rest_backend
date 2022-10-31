package com.bochkarev.restbackend.util;

import com.bochkarev.restbackend.domain.pets.Category;
import com.bochkarev.restbackend.domain.pets.Tag;

import static com.bochkarev.restbackend.util.RandomGenerator.getRandomIndex;
import static com.bochkarev.restbackend.util.RandomGenerator.getRandomString;

public class TagsClient {
    public Tag getTagCreateDto () {
        return fillUpdateDto(new Tag());
    }

    private <T extends Tag> T fillUpdateDto(final T updateDto) {
        updateDto.setId(getRandomIndex(0, 7454));
        updateDto.setName(getRandomString(8));
        return updateDto;
    }
}
