package pl.cs50.network.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import pl.cs50.network.model.location.Location;
import pl.cs50.network.model.user.User;
import pl.cs50.network.model.post.Post;
import pl.cs50.network.model.post.PostMapper;
import pl.cs50.network.model.post.PostRequestDto;
import pl.cs50.network.model.post.PostResponseDto;
import pl.cs50.network.repostiory.PostRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final PostMapper postMapper;
    private final GeolocationService geolocationService;

    public List<PostResponseDto> getAll(Pageable paging) {
        return postRepository.findAll(paging)
                .stream()
                .map(postMapper::map)
                .toList();
    }

    public List<PostResponseDto> gotPostsFollowedByUser(User user, Pageable paging) {
        return postRepository.findAll(paging)
                .filter(p -> user.getFollowings().contains(p.getAuthor()))
                .map(postMapper::map)
                .toList();
    }

    public PostResponseDto createPost(PostRequestDto postRequestDto, User user, String ip) {

        Location location = geolocationService.getLocation(ip);
        System.out.println(location);
        Post postToSave = postMapper.map(postRequestDto, user, location);
        return saveAndMap(postToSave);
    }

    public PostResponseDto editPost(long id, PostRequestDto newPost, User user) {

        Post post = getPostById(id);

        if (post.getAuthor() != user) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You can edit only Your posts");
        }

        post.setText(newPost.getText());
        return saveAndMap(post);
    }

    public PostResponseDto like(long id, User user) {
        Post post = getPostById(id);
        post.addLike(user);
        return saveAndMap(post);
    }

    public PostResponseDto unlike(long id, User user) {
        Post post = getPostById(id);
        post.removeLike(user);
        return saveAndMap(post);
    }

    private PostResponseDto saveAndMap(Post post) {
        Post postSaved = postRepository.save(post);
        return postMapper.map(postSaved);

    }

    private Post getPostById(long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));
    }
}
