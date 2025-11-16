package org.rasatech.weatherapp.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.rasatech.weatherapp.config.RestTemplateConfig;
import org.rasatech.weatherapp.entity.WeatherRequest;
import org.rasatech.weatherapp.repository.WeatherRequestRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.Map;

@Service
public class WeatherService {

    private static final Logger LOG = LoggerFactory.getLogger(WeatherService.class);

    private static final Duration REDIS_CACHE_TTL = Duration.ofMinutes(10);

    @Value("${weather.api.url}")
    private String apiUrl;

    @Value(("${weather.api.key}"))
    private String apiKey;

    private final RestTemplateConfig restTemplate;

    private final RedisTemplate<String, Object> redisTemplate;

    private final WeatherRequestRepository weatherRequestRepository;

    public WeatherService(RestTemplateConfig restTemplate, RedisTemplate<String, Object> redisTemplate, WeatherRequestRepository weatherRequestRepository) {
        this.restTemplate = restTemplate;
        this.redisTemplate = redisTemplate;
        this.weatherRequestRepository = weatherRequestRepository;
    }

    public Map<String, Object> getWeather(String city) throws JsonProcessingException {
        String cacheKey = "weather:" + city.toLowerCase();
        ValueOperations<String, Object> operations = redisTemplate.opsForValue();

        //check Redis cache
        if (Boolean.TRUE.equals(redisTemplate.hasKey(cacheKey))) {
            return (Map<String, Object>) operations.get(cacheKey);
        }

        //Fetch from API
        String url = apiUrl + city + "?key=" + apiKey;
        ResponseEntity<Map> response = restTemplate.restTemplate().getForEntity(url, Map.class);
        Map<String, Object> body = response.getBody();
        String json = new ObjectMapper().writeValueAsString(body);


        //Extract weather data
        Map<String, Object> currentConditions = (Map<String, Object>) ((List<?>) body.get("days")).get(0);
        double temperature = (double) currentConditions.get("temp");
        String description = (String) currentConditions.get("description");

        //save response in db
        WeatherRequest weatherRequest = new WeatherRequest();
        weatherRequest.setCity(city);
        weatherRequest.setTemperature(temperature);
        weatherRequest.setWeatherDescription(description);
        weatherRequestRepository.save(weatherRequest);

        //cache into Redis
        operations.set(cacheKey, body, REDIS_CACHE_TTL);

        return body;
    }


}
