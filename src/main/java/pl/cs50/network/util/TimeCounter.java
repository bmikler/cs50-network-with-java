package pl.cs50.network.util;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class TimeCounter {

    public LocalDateTime getTime() {
        return LocalDateTime.now();
    }

}
