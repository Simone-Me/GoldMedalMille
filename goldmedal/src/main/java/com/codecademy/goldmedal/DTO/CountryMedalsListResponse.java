package com.codecademy.goldmedal.DTO;

import java.util.List;

import com.codecademy.goldmedal.model.GoldMedal;

public class CountryMedalsListResponse {
    private List<GoldMedal> medals;

    public CountryMedalsListResponse(List<GoldMedal> medals) {
        this.medals = medals;
    }

    public List<GoldMedal> getMedals() {
        return medals;
    }

    public void setMedals(List<GoldMedal> medals) {
        this.medals = medals;
    }
}
