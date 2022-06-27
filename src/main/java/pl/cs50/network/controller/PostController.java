package pl.cs50.network.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import pl.cs50.network.repostiory.UserRepository;
import pl.cs50.network.service.PostService;
import pl.cs50.network.model.User.User;
import pl.cs50.network.model.post.Post;
import pl.cs50.network.model.post.PostRequestDto;
import pl.cs50.network.model.post.PostResponseDto;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final UserRepository userRepository;

    @GetMapping
    public List<PostResponseDto> getAll() {
        return postService.getAll();
    }

    @PostMapping
    public ResponseEntity<?> createPost(@RequestBody PostRequestDto post, @AuthenticationPrincipal User user) {

        PostResponseDto postSaved = postService.createPost(post, user);

        return ResponseEntity.ok(postSaved);

    }

}
