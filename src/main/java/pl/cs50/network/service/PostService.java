package pl.cs50.network.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.cs50.network.model.User.User;
import pl.cs50.network.model.post.Post;
import pl.cs50.network.model.post.PostMapper;
import pl.cs50.network.model.post.PostRequestDto;
import pl.cs50.network.model.post.PostResponseDto;
import pl.cs50.network.repostiory.PostRepository;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final PostMapper postMapper;

    public List<PostResponseDto> getAll() {
        return postRepository.findAll()
                .stream()
                .map(postMapper::map)
                .sorted(Comparator.comparing(PostResponseDto::getTimestamp).reversed())
                .toList();
    }

    public List<PostResponseDto> gotPostsFollowedByUser(User user) {
        return postRepository.findAll()
                .stream()
                .filter(p -> user.getFollowings().contains(p.getAuthor()))
                .map(postMapper::map)
                .toList();
    }

    public PostResponseDto createPost(PostRequestDto postRequestDto, User user) {
        Post postToSave = postMapper.map(postRequestDto, user);
        Post postSaved = postRepository.save(postToSave);
        return postMapper.map(postSaved);
    }

}
