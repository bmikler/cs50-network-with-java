package pl.cs50.network.model.post;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mockito;
import pl.cs50.network.model.location.Location;
import pl.cs50.network.model.user.User;
import pl.cs50.network.util.TimeCounter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PostMapperTest {

    private PostMapper postMapper;
    private TimeCounter timeCounter;

    @BeforeAll
    public void init() {
        timeCounter = Mockito.mock(TimeCounter.class);
        postMapper = new PostMapper(timeCounter);
    }

    @Test
    public void mapRequestToPost() {

        LocalDateTime date = LocalDateTime.now();
        Location location = new Location("Poland", "Kraków");

        PostRequestDto postRequestDto = new PostRequestDto("testText");
        User user = new User("testName", "testPassword", new ArrayList<>(), new HashSet<>(), new HashSet<>(), true, false);

        Mockito.when(timeCounter.getTime()).thenReturn(date);

        Post expected = new Post(date, "testText", user, location);
        Post actual = postMapper.map(postRequestDto, user, location);

        Assertions.assertEquals(expected, actual);

    }

    @Test
    public void mapPostToResponse() {

        LocalDateTime date = LocalDateTime.now();
        Location location = new Location("Poland", "Kraków");
        User user = new User("testName", "testPassword", new ArrayList<>(), new HashSet<>(), new HashSet<>(), true, false);

        Post post = new Post(date, "testText", user, location);

        PostResponseDto expected = new PostResponseDto(0L, date, "testText", "testName", "Poland: Kraków");
        PostResponseDto actual = postMapper.map(post);

        Assertions.assertEquals(expected, actual);

    }


}