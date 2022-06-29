package pl.cs50.network.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;

@SpringBootTest
@AutoConfigureMockMvc
public class LikeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @Test
    @Transactional
    public void like() throws Exception {
        User user = userRepository.save(new User("tester", "password", new ArrayList<>(), new HashSet<>(), new HashSet<>(), true, false));
        Post post = postRepository.save(new Post(LocalDateTime.now(), "tesText", user, new Location("USA", "New York")));

        int likeCounterBefore = post.getLikes().size();

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/posts/" + post.getId() + "/like").with(user(user)))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andReturn();

        PostResponseDto postResponseDto = objectMapper.readValue(result.getResponse().getContentAsString(), PostResponseDto.class);

        int likeCounterAfter = postResponseDto.getLikeCounter();

        assertEquals(likeCounterBefore + 1, likeCounterAfter);

    }

    @Test
    @Transactional
    public void likePostIsAlreadyLikedByThisUser() throws Exception {

        User user = userRepository.save(new User("tester", "password", new ArrayList<>(), new HashSet<>(), new HashSet<>(), true, false));
        Post newPost = new Post(LocalDateTime.now(), "tesText", user, new Location("USA", "New York"));
        newPost.addLike(user);
        Post post = postRepository.save(newPost);

        int likeCounterBefore = post.getLikes().size();

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/posts/" + post.getId() + "/like").with(user(user)))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andReturn();

        PostResponseDto postResponseDto = objectMapper.readValue(result.getResponse().getContentAsString(), PostResponseDto.class);

        int likeCounterAfter = postResponseDto.getLikeCounter();

        assertEquals(likeCounterBefore, likeCounterAfter);
    }

    @Test
    @WithMockUser
    public void likePostNotFound() throws Exception {

        int postNumber = postRepository.findAll().size();

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/posts/" + postNumber + 1 + "/like"))
                .andExpect(MockMvcResultMatchers.status().is(404))
                .andReturn();

    }

    @Test
    @Transactional
    public void unlike() throws Exception {
        User user = userRepository.save(new User("tester", "password", new ArrayList<>(), new HashSet<>(), new HashSet<>(), true, false));
        Post newPost = new Post(LocalDateTime.now(), "tesText", user, new Location("USA", "New York"));
        newPost.addLike(user);
        Post post = postRepository.save(newPost);

        int likeCounterBefore = post.getLikes().size();

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.delete("/posts/" + post.getId() + "/like").with(user(user)))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andReturn();

        PostResponseDto postResponseDto = objectMapper.readValue(result.getResponse().getContentAsString(), PostResponseDto.class);

        int likeCounterAfter = postResponseDto.getLikeCounter();

        assertEquals(likeCounterBefore - 1, likeCounterAfter);
    }

    @Test
    @Transactional
    public void postIsNotYetLikedByThisUser() throws Exception {

        User user = userRepository.save(new User("tester", "password", new ArrayList<>(), new HashSet<>(), new HashSet<>(), true, false));
        Post newPost = new Post(LocalDateTime.now(), "tesText", user, new Location("USA", "New York"));
        Post post = postRepository.save(newPost);

        int likeCounterBefore = post.getLikes().size();

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.delete("/posts/" + post.getId() + "/like").with(user(user)))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andReturn();

        PostResponseDto postResponseDto = objectMapper.readValue(result.getResponse().getContentAsString(), PostResponseDto.class);

        int likeCounterAfter = postResponseDto.getLikeCounter();

        assertEquals(likeCounterBefore, likeCounterAfter);

    }

    @Test
    @WithMockUser
    public void unlikePostNotFound() throws Exception {

        int postNumber = postRepository.findAll().size();

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.delete("/posts/" + postNumber + 1 + "/like"))
                .andExpect(MockMvcResultMatchers.status().is(404))
                .andReturn();


    }

}