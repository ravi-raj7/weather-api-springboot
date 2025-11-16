package org.rasatech.weatherapp.repository;

import org.rasatech.weatherapp.entity.WeatherRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WeatherRequestRepository extends JpaRepository<WeatherRequest, Integer> {
}
