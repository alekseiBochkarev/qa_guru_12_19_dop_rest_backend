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
public class PetNew {
    private Long id;
    private Category category;
    private String name;
    private List<String> photoUrls;
    private List<Tag> tags;
    @JsonProperty("status")
    private String status;

    public PetNew(PetNew pet) {
        this.id = pet.getId();
        this.category = pet.getCategory();
        this.name = pet.getName();
        this.photoUrls = pet.getPhotoUrls();
        this.tags = pet.getTags();
        this.status = pet.getStatus();
    }

}
