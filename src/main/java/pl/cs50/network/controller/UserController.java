package pl.cs50.network.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import pl.cs50.network.model.user.User;
import pl.cs50.network.model.user.UserRequestDto;
import pl.cs50.network.model.user.UserResponseDto;
import pl.cs50.network.service.UserService;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/profile/{id}")
    public ResponseEntity<?> getUser(@PathVariable long id) {
        return ResponseEntity.ok(userService.findById(id));
    }

    @PostMapping("/profile/{id}")
    public ResponseEntity<?> followUser(@PathVariable long id, @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(userService.followUser(id, user));
    }

    @DeleteMapping("/profile/{id}")
    public ResponseEntity<?> unfollowUser(@PathVariable long id, @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(userService.unfollowUser(id, user));
    }

    @PostMapping("/register")
    public ResponseEntity<?> createUser(@RequestBody UserRequestDto userRequestDto) {
        System.out.println(userRequestDto);
        UserResponseDto userSaved = userService.save(userRequestDto);
        return ResponseEntity.ok(userSaved);
    }

}
