package pl.cs50.network.model.post;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class PostRequestDto {

    @NotBlank
    private String text;

}
