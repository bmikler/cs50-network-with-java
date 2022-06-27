package pl.cs50.network.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import pl.cs50.network.model.User.User;
import pl.cs50.network.model.User.UserMapper;
import pl.cs50.network.model.User.UserRequestDto;
import pl.cs50.network.model.User.UserResponseDto;
import pl.cs50.network.repostiory.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User nor found"));
    }

    public UserResponseDto save(UserRequestDto userRequestDto) {

        userRepository.findByUsername(userRequestDto.getUsername())
                .ifPresent(u -> {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User " + u.getUsername() + " already exist");
                });

        User userToSave = userMapper.map(userRequestDto);
        User userSaved = userRepository.save(userToSave);

        return userMapper.map(userSaved);

    }
}
