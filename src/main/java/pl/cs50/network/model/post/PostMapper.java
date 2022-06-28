package pl.cs50.network.model.post;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.cs50.network.model.location.Location;
import pl.cs50.network.model.user.User;
import pl.cs50.network.util.TimeCounter;

@Service
@RequiredArgsConstructor
public class PostMapper {

    private final TimeCounter timeCounter;

    public Post map(PostRequestDto postRequestDto, User user, Location location) {
        return new Post(timeCounter.getTime(), postRequestDto.getText(), user, location);
    }

    public PostResponseDto map(Post post) {
        return new PostResponseDto(
                post.getId(),
                post.getTimestamp(),
                post.getText(),
                post.getAuthor().getUsername(),
                post.getLocation().toString(),
                post.getLikes().size());
    }

}
