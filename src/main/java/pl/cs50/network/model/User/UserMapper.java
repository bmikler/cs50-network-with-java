package pl.cs50.network.model.User;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserMapper {

    private final PasswordEncoder passwordEncoder;

    public User map(UserRequestDto userRequestDto) {
        return new User(userRequestDto.getUsername(), passwordEncoder.encode(userRequestDto.getPassword()));

    }

    public UserResponseDto map(User user) {
        return new UserResponseDto(user.getId(), user.getUsername());
    }
}
