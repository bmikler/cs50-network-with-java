package pl.cs50.network.repostiory;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.cs50.network.model.post.Post;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
}
