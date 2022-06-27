package pl.cs50.network.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import pl.cs50.network.service.PostService;
import pl.cs50.network.model.user.User;
import pl.cs50.network.model.post.PostRequestDto;
import pl.cs50.network.model.post.PostResponseDto;
import java.util.List;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {

    private final int POST_PER_PAGE = 5;
    private final PostService postService;

    @GetMapping
    public List<PostResponseDto> getPosts(@RequestParam(required = false) boolean followed,
                                          @RequestParam(required = false, defaultValue = "0") int page,
                                          @AuthenticationPrincipal User user) {

        Pageable paging = PageRequest.of(page, POST_PER_PAGE, Sort.by("timestamp").descending());

        if (followed) {
            return postService.gotPostsFollowedByUser(user, paging);
        }
        return postService.getAll(paging);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editPost(@PathVariable long id,
                                      @RequestBody PostRequestDto postRequestDto,
                                      @AuthenticationPrincipal User user) {
        PostResponseDto post = postService.editPost(id, postRequestDto, user);
        return ResponseEntity.ok(post);
    }

    @PostMapping
    public ResponseEntity<?> createPost(@RequestBody PostRequestDto post, @AuthenticationPrincipal User user) {
        PostResponseDto postSaved = postService.createPost(post, user);
        return ResponseEntity.ok(postSaved);
    }

}
