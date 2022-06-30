package pl.cs50.network.controller;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import pl.cs50.network.util.HttpUtils;
import pl.cs50.network.service.PostService;
import pl.cs50.network.model.user.User;
import pl.cs50.network.model.post.PostRequestDto;
import pl.cs50.network.model.post.PostResponseDto;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {

    private final int POST_PER_PAGE = 5;
    private final PostService postService;
    private final Logger logger = LoggerFactory.getLogger(PostController.class);

    @GetMapping("/{id}")
    public ResponseEntity<?> getSinglePost(@PathVariable long id) {
        PostResponseDto post = postService.getSinglePost(id);
        return ResponseEntity.ok(post);
    }

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
    public ResponseEntity<?> createPost(@RequestBody PostRequestDto post,
                                        @AuthenticationPrincipal User user,
                                        HttpServletRequest request) {

        logger.info("server name: " + request.getServerName());
        logger.info("remote host: " + request.getRemoteHost());
        logger.info("real path: " + request.getRemoteAddr());
        logger.info("real path: " + request.getLocalAddr());
        logger.info("real path: " + request.getLocalName());
;

        String ip = HttpUtils.getRequestIP(request);
        PostResponseDto postSaved = postService.createPost(post, user, ip);
        return ResponseEntity.ok(postSaved);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deletePost(@PathVariable long id,
                                        @AuthenticationPrincipal User user) {

        postService.deletePost(id, user);

        return ResponseEntity.noContent().build();

    }

}
