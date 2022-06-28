package pl.cs50.network.model.post;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import pl.cs50.network.model.location.Location;
import pl.cs50.network.model.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Getter
@ToString
@NoArgsConstructor
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private LocalDateTime timestamp;
    private String text;
    @OneToOne(cascade = CascadeType.ALL)
    private Location location;
    @ManyToOne
    private User author;
    @OneToMany
    @Getter(AccessLevel.NONE)
    private Set<User> likes;

    public Post(LocalDateTime timestamp, String text, User author, Location location) {
        this.timestamp = timestamp;
        this.text = text;
        this.author = author;
        this.location = location;
        this.likes = new HashSet<>();
    }

    public void setText(String text) {
        this.text = text;
    }

    public Set<User> getLikes() {
        return Collections.unmodifiableSet(likes);
    }

    public void addLike(User user) {
        likes.add(user);
    }

    public void removeLike(User user) {
        likes.remove(user);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Post post = (Post) o;
        return id == post.id && timestamp.equals(post.timestamp) && text.equals(post.text) && author.equals(post.author);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, timestamp, text, author);
    }
}
