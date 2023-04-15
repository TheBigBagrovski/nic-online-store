package nic.project.onlinestore.dto;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class CategoryDTO {

    @NotBlank
    @Size(min = 2, message = "Минимум 2 символа")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
