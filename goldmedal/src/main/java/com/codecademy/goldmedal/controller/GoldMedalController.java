package com.codecademy.goldmedal.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.text.WordUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codecademy.goldmedal.DTO.CountriesResponse;
import com.codecademy.goldmedal.DTO.CountryDetailsResponse;
import com.codecademy.goldmedal.DTO.CountryMedalsListResponse;
import com.codecademy.goldmedal.model.Country;
import com.codecademy.goldmedal.model.CountrySummary;
import com.codecademy.goldmedal.model.GoldMedal;
import com.codecademy.goldmedal.repository.CountryRepository;
import com.codecademy.goldmedal.repository.GoldMedalRepository;

@RestController
@RequestMapping("/countries")
public class GoldMedalController {
    private final CountryRepository countryRepository;
    private final GoldMedalRepository goldMedalRepository;

    public GoldMedalController(CountryRepository countryRepository, GoldMedalRepository goldMedalRepository) {
        this.countryRepository = countryRepository;
        this.goldMedalRepository = goldMedalRepository;
    }

    @GetMapping
    public CountriesResponse getCountries(@RequestParam String sort_by, @RequestParam String ascending) {
        var ascendingOrder = ascending.toLowerCase().equals("y");
        return new CountriesResponse(getCountrySummaries(sort_by.toLowerCase(), ascendingOrder));
    }

    @GetMapping("/{country}")
    public CountryDetailsResponse getCountryDetails(@PathVariable String country) {
        String countryName = WordUtils.capitalizeFully(country);
        return getCountryDetailsResponse(countryName);
    }

    @GetMapping("/{country}/medals")
    public CountryMedalsListResponse getCountryMedalsList(@PathVariable String country, @RequestParam String sort_by,
            @RequestParam String ascending) {
        String countryName = WordUtils.capitalizeFully(country);
        var ascendingOrder = ascending.toLowerCase().equals("y");
        return getCountryMedalsListResponse(countryName, sort_by.toLowerCase(), ascendingOrder);
    }

    private CountryMedalsListResponse getCountryMedalsListResponse(String countryName, String sortBy,
            boolean ascendingOrder) {
        List<GoldMedal> medalsList;
        switch (sortBy) {
            case "year":
                medalsList = ascendingOrder ? this.goldMedalRepository.findByCountryOrderByYearAsc(countryName)
                        : this.goldMedalRepository.findByCountryOrderByYearDesc(countryName);
                break;
            case "season":
                medalsList = ascendingOrder ? this.goldMedalRepository.findByCountryOrderBySeasonAsc(countryName)
                        : this.goldMedalRepository.findByCountryOrderBySeasonDesc(countryName);
                break;
            case "city":
                medalsList = ascendingOrder ? this.goldMedalRepository.findByCountryOrderByCityAsc(countryName)
                        : this.goldMedalRepository.findByCountryOrderByCityDesc(countryName);
                break;
            case "name":
                medalsList = ascendingOrder ? this.goldMedalRepository.findByCountryOrderByNameAsc(countryName)
                        : this.goldMedalRepository.findByCountryOrderByNameDesc(countryName);
                break;
            case "event":
                medalsList = ascendingOrder ? this.goldMedalRepository.findByCountryOrderByEventAsc(countryName)
                        : this.goldMedalRepository.findByCountryOrderByEventDesc(countryName);
                break;
            default:
                medalsList = new ArrayList<>();
                break;
        }
        return new CountryMedalsListResponse(medalsList);
    }

    private CountryDetailsResponse getCountryDetailsResponse(String countryName) {
        Optional<Country> countryOptional = this.countryRepository.findByName(countryName);

        if (countryOptional.isEmpty()) {
            return new CountryDetailsResponse(countryName);
        }

        Country country = countryOptional.get();
        int goldMedalCount = this.goldMedalRepository.countByCountry(countryName);

        List<GoldMedal> summerWins = this.goldMedalRepository.findByCountryAndSeasonOrderByYearAsc(countryName,
                "Summer");
        Integer numberSummerWins = !summerWins.isEmpty() ? summerWins.size() : null;
        int totalSummerEvents = this.goldMedalRepository.countEventBySeason("Summer");
        Float percentageTotalSummerWins = totalSummerEvents != 0 && numberSummerWins != null
                ? (float) summerWins.size() / totalSummerEvents
                : null;
        Integer yearFirstSummerWin = !summerWins.isEmpty() ? summerWins.get(0).getYear() : null;

        List<GoldMedal> winterWins = this.goldMedalRepository.findByCountryAndSeasonOrderByYearAsc(countryName,
                "Winter");
        Integer numberWinterWins = !winterWins.isEmpty() ? winterWins.size() : null;
        int totalWinterEvents = this.goldMedalRepository.countEventBySeason("Winter");
        Float percentageTotalWinterWins = totalWinterEvents != 0 && numberWinterWins != null
                ? (float) winterWins.size() / totalWinterEvents
                : null;
        Integer yearFirstWinterWin = !winterWins.isEmpty() ? winterWins.get(0).getYear() : null;

        int numberEventsWonByFemaleAthletes = this.goldMedalRepository.countByCountryAndGender(countryName, "Female");
        int numberEventsWonByMaleAthletes = this.goldMedalRepository.countByCountryAndGender(countryName, "Male");

        return new CountryDetailsResponse(
                countryName,
                country.getGdp(),
                country.getPopulation(),
                goldMedalCount,
                numberSummerWins,
                percentageTotalSummerWins,
                yearFirstSummerWin,
                numberWinterWins,
                percentageTotalWinterWins,
                yearFirstWinterWin,
                numberEventsWonByFemaleAthletes,
                numberEventsWonByMaleAthletes);
    }

    private List<CountrySummary> getCountrySummaries(String sortBy, boolean ascendingOrder) {
        List<Country> countries;
        switch (sortBy) {
            case "name":
                countries = ascendingOrder ? this.countryRepository.findByOrderByNameAsc()
                        : this.countryRepository.findByOrderByNameDesc();
                // list of countries sorted by name in the given order
                break;
            case "gdp":
                countries = ascendingOrder ? this.countryRepository.findByOrderByGdpAsc()
                        : this.countryRepository.findByOrderByGdpDesc();
                // list of countries sorted by gdp in the given order

                break;
            case "population":
                countries = ascendingOrder ? this.countryRepository.findByOrderByPopulationAsc()
                        : this.countryRepository.findByOrderByPopulationDesc();
                // list of countries sorted by population in the given order
                break;
            case "medals":
            default:
                countries = this.countryRepository.getAllByOrderByNameAsc();
                // : list of countries in any order you choose; for sorting by medalcount,
                // additional logic below will handle that
                break;
        }

        var countrySummaries = getCountrySummariesWithMedalCount(countries);

        if (sortBy.equalsIgnoreCase("medals")) {
            countrySummaries = sortByMedalCount(countrySummaries, ascendingOrder);
        }

        return countrySummaries;
    }

    private List<CountrySummary> sortByMedalCount(List<CountrySummary> countrySummaries, boolean ascendingOrder) {
        return countrySummaries.stream()
                .sorted((t1, t2) -> ascendingOrder ? t1.getMedals() - t2.getMedals() : t2.getMedals() - t1.getMedals())
                .collect(Collectors.toList());
    }

    private List<CountrySummary> getCountrySummariesWithMedalCount(List<Country> countries) {
        List<CountrySummary> countrySummaries = new ArrayList<>();
        for (var country : countries) {
            var goldMedalCount = goldMedalRepository.countByCountry(country.getName());
            // get count of medals for the given country
            countrySummaries.add(new CountrySummary(country, goldMedalCount));
        }
        return countrySummaries;
    }
}
