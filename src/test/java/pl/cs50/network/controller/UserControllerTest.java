package pl.cs50.network.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.assertj.core.api.Assertions;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import pl.cs50.network.model.user.User;
import pl.cs50.network.repostiory.UserRepository;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper mapper;

    @Test
    @WithMockUser
    @Transactional
    public void getUserOk() throws Exception {

        User user = userRepository.save(new User(
                "test", "test", new ArrayList<>(),
                new HashSet<>(), new HashSet<>(), true, false));

        mockMvc.perform(MockMvcRequestBuilders.get("/user/profile/" + user.getId()))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect((MockMvcResultMatchers.jsonPath("$.id", Matchers.is((int)user.getId()))))
                .andExpect(MockMvcResultMatchers.jsonPath("$.username", Matchers.is("test")));

    }

    @Test
    @WithMockUser
    public void getUserNotFound() throws Exception {

        int usersNumber = userRepository.findAll().size();

        mockMvc.perform(MockMvcRequestBuilders.get("/user/profile/" + usersNumber + 1))
                .andExpect(MockMvcResultMatchers.status().is(404));

    }


    @Test
    @Transactional
    public void createUserOk() throws Exception {

        String jsonInput = "{\"username\":\"createUserOk\",\"password\":\"password\"}";
        int databaseSizeBefore = userRepository.findAll().size();

        mockMvc.perform(MockMvcRequestBuilders.post("/user/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonInput))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.username").value("createUserOk"));

        int databaseSizeAfter = userRepository.findAll().size();
        assertEquals(databaseSizeBefore + 1, databaseSizeAfter);

    }

    @Test
    @Transactional
    public void createUserAlreadyExist() throws Exception {

        userRepository.save(new User("createUserAlreadyExist", "password",new ArrayList<>(), new HashSet<>(), new HashSet<>(), true, false));

        String jsonInput = "{\"username\":\"user\",\"password\":\"password\"}";
        int databaseSizeBefore = userRepository.findAll().size();

        mockMvc.perform(MockMvcRequestBuilders.post("/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonInput))
                .andExpect(MockMvcResultMatchers.status().is(400));

        int databaseSizeAfter = userRepository.findAll().size();
        assertEquals(databaseSizeBefore, databaseSizeAfter);

    }

    @Test
    public void createUserEmptyPassword() throws Exception {

        String jsonInput = "{\"username\":\"user\",\"password\":\"\"}";
        int databaseSizeBefore = userRepository.findAll().size();

        mockMvc.perform(MockMvcRequestBuilders.post("/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonInput))
                .andExpect(MockMvcResultMatchers.status().is(400));

        int databaseSizeAfter = userRepository.findAll().size();
        assertEquals(databaseSizeBefore, databaseSizeAfter);


    }

    @Test
    public void createUserEmptyUsername() throws Exception {

        String jsonInput = "{\"username\":\"\",\"password\":\"password\"}";
        int databaseSizeBefore = userRepository.findAll().size();

        mockMvc.perform(MockMvcRequestBuilders.post("/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonInput))
                .andExpect(MockMvcResultMatchers.status().is(400));

        int databaseSizeAfter = userRepository.findAll().size();
        assertEquals(databaseSizeBefore, databaseSizeAfter);


    }

    @Test
    @Transactional
    public void followOk() throws Exception {
        User follower = userRepository.save(new User("follower", "password", new ArrayList<>(), new HashSet<>(), new HashSet<>(), true, false));
        User userToFollow = userRepository.save(new User("userToFollow", "password", new ArrayList<>(), new HashSet<>(), new HashSet<>(), true, false));

        mockMvc.perform(MockMvcRequestBuilders.post("/user/profile/" + userToFollow.getId()).with(user(follower)))
                .andExpect(MockMvcResultMatchers.status().is(200));

        assertTrue(userRepository.findByUsername("follower").get().getFollowings().contains(userToFollow));
        assertTrue(userRepository.findByUsername("userToFollow").get().getFollowers().contains(follower));

    }

    @Test
    @Transactional
    public void followUserNotFound() throws Exception {
        User follower = userRepository.save(new User("followUserNotFound", "password", new ArrayList<>(), new HashSet<>(), new HashSet<>(), true, false));
        int dbSize = userRepository.findAll().size();

        mockMvc.perform(MockMvcRequestBuilders.post("/user/profile/" + dbSize + 1).with(user(follower)))
                .andExpect(MockMvcResultMatchers.status().is(404));

        assertTrue(userRepository.findByUsername("followUserNotFound").get().getFollowings().isEmpty());

    }

    @Test
    @Transactional
    public void followUserHimself() throws Exception {
        User follower = userRepository.save(new User("follower", "password", new ArrayList<>(), new HashSet<>(), new HashSet<>(), true, false));

        mockMvc.perform(MockMvcRequestBuilders.post("/user/profile/" + follower.getId()).with(user(follower)))
                .andExpect(MockMvcResultMatchers.status().is(400));

        assertTrue(userRepository.findByUsername("follower").get().getFollowings().isEmpty());

    }

    @Test
    public void followUserAlreadyFollowed() throws Exception {


        User userToFollow = userRepository.save(new User("userToFollow", "password", new ArrayList<>(), new HashSet<>(), new HashSet<>(), true, false));
        User follower = userRepository.save(new User("follower", "password", new ArrayList<>(), Set.of(userToFollow), new HashSet<>(), true, false));

        mockMvc.perform(MockMvcRequestBuilders.post("/user/profile/" + userToFollow.getId()).with(user(follower)))
                .andExpect(MockMvcResultMatchers.status().is(400));

        int actaulFollowingsSize = userRepository.findById(follower.getId()).get().getFollowings().size();

        assertEquals(1, actaulFollowingsSize);
    }

    //TODO unfollow test
}