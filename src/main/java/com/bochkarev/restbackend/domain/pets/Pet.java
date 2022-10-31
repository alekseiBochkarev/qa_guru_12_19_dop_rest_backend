package com.bochkarev.restbackend.domain.pets;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class Pet {
    private int id;
    private Category category;
    private String name;
    private List<String> photoUrls;
    private List<Tag> tags;
    @JsonProperty("status")
    private String status;

    public Pet(Pet pet) {
        this.id = pet.getId();
        this.category = pet.getCategory();
        this.name = pet.getName();
        this.photoUrls = pet.getPhotoUrls();
        this.tags = pet.getTags();
        this.status = pet.getStatus();
    }

}
