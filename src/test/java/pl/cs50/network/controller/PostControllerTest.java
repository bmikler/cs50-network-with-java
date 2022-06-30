package pl.cs50.network.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import pl.cs50.network.model.location.Location;
import pl.cs50.network.model.post.Post;
import pl.cs50.network.model.post.PostResponseDto;
import pl.cs50.network.model.user.User;
import pl.cs50.network.repostiory.PostRepository;
import pl.cs50.network.repostiory.UserRepository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    @WithMockUser
    @Transactional
    public void shouldGetSinglePostOK() throws Exception {

        User user = userRepository.save(new User("test", "test", new ArrayList<>(), new HashSet<>(), new HashSet<>(), true, false));
        Post expectedPost = postRepository.save(new Post(500, LocalDateTime.now(), "abc", new Location("Poland", "Kraków"), user, new HashSet<>()));

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/posts/" + expectedPost.getId()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andReturn();

        PostResponseDto actualPost = objectMapper.readValue(result.getResponse().getContentAsString(), PostResponseDto.class);

        assertThat(actualPost).isNotNull();
        assertThat(actualPost.getId()).isEqualTo(expectedPost.getId());
        assertThat(actualPost.getAuthor()).isEqualTo("test");
    }

    @Test
    @WithMockUser
    public void shouldGetSinglePostNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/posts/10000"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is(404));
    }

    @Test
    @WithMockUser
    public void shouldGetAllPosts() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/posts"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray());
    }

    @Test
    @WithMockUser
    @Transactional
    public void shouldGetOnlyPostsOfFollowedUsers() throws Exception {

        User followedUser = userRepository.save(new User("followed", "test", new ArrayList<>(), new HashSet<>(), new HashSet<>(), true, false));

        postRepository.save(new Post(LocalDateTime.now(), "abc", followedUser, new Location("Poland", "Kraków")));
        postRepository.save(new Post(LocalDateTime.now(), "def", followedUser, new Location("Poland", "Kraków")));

        User follower = new User("follower", "test", new ArrayList<>(), new HashSet<>(), new HashSet<>(), true, false);
        follower.addFollowing(followedUser);

        User savedFollower = userRepository.save(follower);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/posts?followed=true").with(user(savedFollower)))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andReturn();

        List<PostResponseDto> posts = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<List<PostResponseDto>>() {
        });

        assertEquals(2, posts.size());
        assertEquals("followed", posts.get(0).getAuthor());
        assertEquals("followed", posts.get(1).getAuthor());
    }

    @Test
    @Transactional
    public void editPostOk() throws Exception {

        User user = userRepository.save(new User("test", "test", new ArrayList<>(), new HashSet<>(), new HashSet<>(), true, false));
        Post post = postRepository.save(new Post(LocalDateTime.now(), "abc", user, new Location("Poland", "Krakow")));

        int postsBefore = postRepository.findAll().size();

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put("/posts/" + post.getId()).with(user(user))
                        .contentType(MediaType.APPLICATION_JSON).content("{\"text\":\"edited\"}"))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andReturn();

        PostResponseDto editedPostResponse = objectMapper.readValue(result.getResponse().getContentAsString(), PostResponseDto.class);

        int postsAfter = postRepository.findAll().size();

        assertEquals(postsBefore, postsAfter);
        assertEquals(post.getId(), editedPostResponse.getId());
        assertEquals("edited", editedPostResponse.getText());

    }

    @Test
    @WithMockUser
    public void editPostPostNotFound() throws Exception {

        int postNumber = postRepository.findAll().size();

        mockMvc.perform(MockMvcRequestBuilders.put("/posts/" + postNumber + 1)
                        .contentType(MediaType.APPLICATION_JSON).content("{\"text\":\"edited\"}"))
                .andExpect(MockMvcResultMatchers.status().is(404));

    }

    @Test
    @Transactional
    public void editPostUserUnauthorized() throws Exception {

        User user = userRepository.save(new User("user", "test", new ArrayList<>(), new HashSet<>(), new HashSet<>(), true, false));
        User author = userRepository.save(new User("author", "test", new ArrayList<>(), new HashSet<>(), new HashSet<>(), true, false));
        Post post = postRepository.save(new Post(LocalDateTime.now(), "abc", author, new Location("Poland", "Krakow")));


        mockMvc.perform(MockMvcRequestBuilders.put("/posts/" + post.getId()).with(user(user))
                        .contentType(MediaType.APPLICATION_JSON).content("{\"text\":\"edited\"}"))
                .andExpect(MockMvcResultMatchers.status().is(401));
    }

    @Test
    @Transactional
    public void createPost() throws Exception {

        User user = userRepository.save(new User("user", "test", new ArrayList<>(), new HashSet<>(), new HashSet<>(), true, false));

        int postNumberBefore = postRepository.findAll().size();

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/posts").with(user(user))
                        .contentType(MediaType.APPLICATION_JSON).content("{\"text\":\"newPost\"}"))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andReturn();

        PostResponseDto postResponseDto = objectMapper.readValue(result.getResponse().getContentAsString(), PostResponseDto.class);

        int postNumberAfter = postRepository.findAll().size();

        assertEquals(postNumberBefore + 1, postNumberAfter);
        assertEquals("user", postResponseDto.getAuthor());
        assertEquals("newPost", postResponseDto.getText());
    }


    @SneakyThrows
    @Test
    @Transactional
    public void deletePostOk() {

        User user = userRepository.save(new User("test", "test", new ArrayList<>(), new HashSet<>(), new HashSet<>(), true, false));
        Post post = postRepository.save(new Post(LocalDateTime.now(), "abc", user, new Location("Poland", "Krakow")));

        int postsBefore = postRepository.findAll().size();

        mockMvc.perform(MockMvcRequestBuilders.delete("/posts/" + post.getId()).with(user(user)))
                .andExpect(MockMvcResultMatchers.status().is(204));

        int postsAfter = postRepository.findAll().size();

        assertEquals(postsBefore - 1, postsAfter);

    }

    @Test
    public void deletePostUserUnauthorized() throws Exception {

        User user = userRepository.save(new User("user", "test", new ArrayList<>(), new HashSet<>(), new HashSet<>(), true, false));
        User author = userRepository.save(new User("author", "test", new ArrayList<>(), new HashSet<>(), new HashSet<>(), true, false));
        Post post = postRepository.save(new Post(LocalDateTime.now(), "abc", author, new Location("Poland", "Krakow")));

        int postsBefore = postRepository.findAll().size();

        mockMvc.perform(MockMvcRequestBuilders.delete("/posts/" + post.getId()).with(user(user)))
                .andExpect(MockMvcResultMatchers.status().is(401));

        int postsAfter = postRepository.findAll().size();

        assertEquals(postsBefore, postsAfter);

    }

}