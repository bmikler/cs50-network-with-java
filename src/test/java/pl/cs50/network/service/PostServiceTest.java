package pl.cs50.network.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mockito;
import org.springframework.data.domain.*;
import pl.cs50.network.model.post.Post;
import pl.cs50.network.model.post.PostMapper;
import pl.cs50.network.model.post.PostResponseDto;
import pl.cs50.network.model.location.Location;
import pl.cs50.network.util.TimeCounter;
import pl.cs50.network.model.user.User;
import pl.cs50.network.repostiory.PostRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PostServiceTest {

    private PostService postService;
    private PostRepository postRepository;
    private PostMapper postMapper;
    private TimeCounter timeCounter;
    private LocalDateTime time;
    private Pageable paging;
    private GeolocationService geolocationService;

    @BeforeAll
    public void init() {
        paging = PageRequest.of(1, 5, Sort.by("timestamp").descending());
        time = LocalDateTime.now();
        timeCounter = Mockito.mock(TimeCounter.class);
        postMapper = new PostMapper(timeCounter);
        postRepository = Mockito.mock(PostRepository.class);
        geolocationService = Mockito.mock(GeolocationService.class);
        postService = new PostService(postRepository, postMapper, geolocationService);
    }

    @Test
    public void getAllPostsDBEmpty() {
        Mockito.when(postRepository.findAll(paging)).thenReturn(Page.empty());
        Mockito.when(timeCounter.getTime()).thenReturn(time);

        List<PostResponseDto> expected = Collections.emptyList();
        List<PostResponseDto> actual = postService.getAll(paging);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void getAllPostsDBWithPosts() {

        User author = new User("testName", "testPassword", new ArrayList<>(), new HashSet<>(), new HashSet<>(), true, false);
        Location location = new Location("Poland", "Kraków");

        List<Post> posts = List.of(
                new Post(time.plusHours(1), "def", author, location),
                new Post(time, "testText", author, location)
        );
        Page<Post> page = new PageImpl<>(posts);

        Mockito.when(postRepository.findAll(paging)).thenReturn(page);
        Mockito.when(timeCounter.getTime()).thenReturn(time);

        List<PostResponseDto> expected = List.of(
                new PostResponseDto(0L, time.plusHours(1), "def", "testName", "Poland: Kraków", 0),
                new PostResponseDto(0L, time, "testText", "testName", "Poland: Kraków", 0)
        );

        List<PostResponseDto> actual = postService.getAll(paging);

        Assertions.assertEquals(expected, actual);

    }

    //TODO postServiceTest
    //getFollowed
    //createPost

}