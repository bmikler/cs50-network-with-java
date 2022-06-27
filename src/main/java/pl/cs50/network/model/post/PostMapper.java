package pl.cs50.network.model.post;

import org.springframework.stereotype.Service;
import pl.cs50.network.model.User.User;

import java.time.LocalDateTime;

@Service
public class PostMapper {

    public Post map(PostRequestDto postRequestDto, User user) {
        return new Post(LocalDateTime.now(), postRequestDto.getText(), user);
    }

    public PostResponseDto map(Post post) {
        return new PostResponseDto(
                post.getId(),
                post.getTimestamp(),
                post.getText(),
                post.getAuthor().getUsername());


    }

}
