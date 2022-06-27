package pl.cs50.network.model.post;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.Objects;

@RequiredArgsConstructor
@Getter
public class PostResponseDto {

    private final long id;
    private final LocalDateTime timestamp;
    private final String text;
    private final String author;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PostResponseDto that = (PostResponseDto) o;
        return id == that.id && timestamp.equals(that.timestamp) && text.equals(that.text) && author.equals(that.author);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, timestamp, text, author);
    }
}
