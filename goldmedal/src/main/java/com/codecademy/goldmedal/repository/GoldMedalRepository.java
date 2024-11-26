package com.codecademy.goldmedal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.codecademy.goldmedal.model.GoldMedal;

public interface GoldMedalRepository extends JpaRepository<GoldMedal, Long> {
    List<GoldMedal> findByCountryOrderByYearAsc(String country);

    List<GoldMedal> findByCountryOrderByYearDesc(String country);

    List<GoldMedal> findByCountryOrderBySeasonAsc(String country);

    List<GoldMedal> findByCountryOrderBySeasonDesc(String country);

    List<GoldMedal> findByCountryOrderByCityAsc(String country);

    List<GoldMedal> findByCountryOrderByCityDesc(String country);

    List<GoldMedal> findByCountryOrderByNameAsc(String country);

    List<GoldMedal> findByCountryOrderByNameDesc(String country);

    List<GoldMedal> findByCountryOrderByEventAsc(String country);

    List<GoldMedal> findByCountryOrderByEventDesc(String country);

    List<GoldMedal> findByCountryAndSeasonOrderByYearAsc(String country, String season);

    List<GoldMedal> findByCountryAndGenderOrderByYearAsc(String country, String gender);

    int countByCountry(String country);

    int countByCountryAndGender(String country, String gender);

    int countEventBySeason(String season);

    int countByCountryAndSeason(String country, String season);

}
