package pl.cs50.network.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.cs50.network.model.User.UserRequestDto;
import pl.cs50.network.model.User.UserResponseDto;
import pl.cs50.network.service.UserService;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/profile")

    @PostMapping("/register")
    public ResponseEntity<?> createUser(@RequestBody UserRequestDto userRequestDto) {

        System.out.println(userRequestDto);
        UserResponseDto userSaved = userService.save(userRequestDto);
        return ResponseEntity.ok(userSaved);
    }

}
