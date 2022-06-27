package pl.cs50.network.model.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.cs50.network.model.post.PostMapper;

import java.util.ArrayList;
import java.util.HashSet;

@Service
@RequiredArgsConstructor
public class UserMapper {

    private final PasswordEncoder passwordEncoder;
    private final PostMapper postMapper;

    public User map(UserRequestDto userRequestDto) {
        return new User(userRequestDto.getUsername(),
                passwordEncoder.encode(userRequestDto.getPassword()),
                new ArrayList<>(),
                new HashSet<>(),
                new HashSet<>(),
                true,
                false);

    }

    public UserResponseDto map(User user) {
        return new UserResponseDto(
                user.getId(),
                user.getUsername(),
                user.getPosts().stream().map(postMapper::map).toList(),
                user.getFollowings().size(),
                user.getFollowers().size());
    }
}
