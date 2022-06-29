package pl.cs50.network.controller;

import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import pl.cs50.network.model.post.PostResponseDto;
import pl.cs50.network.model.user.User;
import pl.cs50.network.service.PostService;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class LikeController {

    private final PostService postService;

    @PostMapping("/{id}/like")
    public ResponseEntity<?> like (@PathVariable long id,
                                  @AuthenticationPrincipal User user) {

        PostResponseDto post = postService.like(id, user);
        return ResponseEntity.ok(post);

    }

    @DeleteMapping("/{id}/like")
    public ResponseEntity<?> unlike (@PathVariable long id,
                                  @AuthenticationPrincipal User user) {

        PostResponseDto post = postService.unlike(id, user);
        return ResponseEntity.ok(post);

    }

}
