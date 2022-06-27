package pl.cs50.network.model.User;



import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.cs50.network.model.post.Post;
import pl.cs50.network.model.post.PostMapper;
import pl.cs50.network.model.post.PostResponseDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserMapperTest {

    private PasswordEncoder passwordEncoder;
    private PostMapper postMapper;
    private UserMapper userMapper;

    private User testUser;

    @BeforeAll
    public void init() {
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        postMapper = Mockito.mock(PostMapper.class);
        userMapper = new UserMapper(passwordEncoder, postMapper);
    }

    @BeforeEach
    public void initTestUser() {
        testUser = new User("testUser", "hashedPassword", new ArrayList<>(), new HashSet<>(), new HashSet<>(), true, false);
    }

    @Test
    public void mapRequestToUser() {

        UserRequestDto userRequestDto = new UserRequestDto("testUser", "testPassword");

        Mockito.when(passwordEncoder.encode("testPassword")).thenReturn("hashedPassword");

        User expected =  testUser;
        User actual = userMapper.map(userRequestDto);

        Assertions.assertEquals(expected, actual);

    }

    @Test
    public void mapUserWithoutDataToResponse() {

        UserResponseDto expected = new UserResponseDto(0, "testUser", Collections.emptyList(), 0, 0);
        UserResponseDto actual = userMapper.map(testUser);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void mapUserWithPostsToResponse() {

        LocalDateTime date = LocalDateTime.now();


        Post post1 = new Post(date, "abc", testUser);
        Post post2 = new Post(date, "def", testUser);
        testUser.addPost(post1);
        testUser.addPost(post2);

        Mockito.when(postMapper.map(post1)).thenReturn(new PostResponseDto(0, date, "abc", "testUser"));
        Mockito.when(postMapper.map(post2)).thenReturn(new PostResponseDto(0, date, "def", "testUser"));

        UserResponseDto expected = new UserResponseDto(0,
                "testUser",
                List.of(new PostResponseDto(0, date, "abc", "testUser"), new PostResponseDto(0, date, "def", "testUser")),
                0,
                0);

        UserResponseDto actual = userMapper.map(testUser);

        Assertions.assertEquals(expected, actual);

    }

    @Test
    public void mapUserWithFollowingsToResponse() {

        testUser.addFollowing(new User("a","a",new ArrayList<>(), new HashSet<>(), new HashSet<>(),true, false));
        testUser.addFollowing(new User("b","b",new ArrayList<>(), new HashSet<>(), new HashSet<>(),true, false));;

        UserResponseDto expected = new UserResponseDto(0, "testUser", Collections.emptyList(), 2, 0);
        UserResponseDto actual = userMapper.map(testUser);

        assertEquals(expected, actual);

    }

    @Test
    public void mapUserWithFollowersToResponse() {

        testUser.addFollower(new User("a","a",new ArrayList<>(), new HashSet<>(), new HashSet<>(),true, false));
        testUser.addFollower(new User("b","b",new ArrayList<>(), new HashSet<>(), new HashSet<>(),true, false));

        UserResponseDto expected = new UserResponseDto(0, "testUser", Collections.emptyList(), 0, 2);
        UserResponseDto actual = userMapper.map(testUser);

        assertEquals(expected, actual);


    }

}