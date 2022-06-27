package pl.cs50.network.model.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import pl.cs50.network.model.post.PostResponseDto;

import java.util.List;
import java.util.Objects;

@Data
@AllArgsConstructor
public class UserResponseDto {
    private long id;
    private String username;
    private List<PostResponseDto> posts;
    private int followings;
    private int followers;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserResponseDto that = (UserResponseDto) o;
        return id == that.id && followings == that.followings && followers == that.followers && username.equals(that.username) && posts.equals(that.posts);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, posts, followings, followers);
    }
}
