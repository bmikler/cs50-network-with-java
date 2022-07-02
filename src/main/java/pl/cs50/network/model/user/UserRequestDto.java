package pl.cs50.network.model.user;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
public class UserRequestDto {

    @NotBlank(message = "Username can`t be empty")
    private String username;
    @NotBlank(message = "Password can`t be empty")
    private String password;

}
