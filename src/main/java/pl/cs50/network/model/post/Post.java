package pl.cs50.network.model.post;

import lombok.Getter;
import lombok.NoArgsConstructor;
import pl.cs50.network.model.User.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private LocalDateTime timestamp;
    private String text;
    @ManyToOne
    private User author;
//    @Getter(AccessLevel.NONE)
//    private List<Like> likes;

    public Post(LocalDateTime timestamp, String text, User author) {
        this.timestamp = timestamp;
        this.text = text;
        this.author = author;
//        this.likes = new ArrayList<>();
    }

//    public List<Like> getLikes() {
//        return Collections.unmodifiableList(likes);
//    }
}
