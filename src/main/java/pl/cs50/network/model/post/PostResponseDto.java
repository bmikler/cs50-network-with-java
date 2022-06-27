package pl.cs50.network.model.post;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Getter
public class PostResponseDto {

    private final long id;
    private final LocalDateTime timestamp;
    private final String text;
    private final String author;

}
