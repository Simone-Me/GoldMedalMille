package com.codecademy.goldmedal.DTO;

import java.util.List;

import com.codecademy.goldmedal.model.CountrySummary;

public class CountriesResponse {
    private List<CountrySummary> countries;

    public CountriesResponse(List<CountrySummary> countries) {
        this.countries = countries;
    }

    public List<CountrySummary> getCountries() {
        return countries;
    }
}
