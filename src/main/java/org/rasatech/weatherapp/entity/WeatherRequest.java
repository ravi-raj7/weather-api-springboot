package org.rasatech.weatherapp.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "weather_requests", schema = "public")
public class WeatherRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String city;
    private double temperature;
    private String weatherDescription;
    private LocalDateTime requestTime = LocalDateTime.now();
}
