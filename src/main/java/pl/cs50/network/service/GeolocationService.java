package pl.cs50.network.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import pl.cs50.network.model.location.Location;

@Service
@RequiredArgsConstructor
public class GeolocationService {

    private final RestTemplate restTemplate;

    public Location getLocation(String ip) {
        String realIp = !ip.equals("0:0:0:0:0:0:0:1") ? ip : "78.10.231.204";
        return restTemplate.getForObject("http://ip-api.com/json/{ip}", Location.class, realIp);
    }

}
