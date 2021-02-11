package com.lambdaschool.countries.controllers;

import com.lambdaschool.countries.models.Country;
import com.lambdaschool.countries.repositories.CountryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class CountryController {
    //filter function
    private List<Country> findCountries(List<Country> myList, CheckCountry tester)
    {
        List<Country> tempList = new ArrayList<>();
        for(Country c : myList)
        {
            if (tester.test(c))
            {
                tempList.add(c);
            }
        }
        return tempList;
    }

    //feilds
    @Autowired
    CountryRepository countres;

    @GetMapping(value="/names/all", produces={"application/json"})
    public ResponseEntity<?> listAllCountries()
    {
        List<Country> countryList = new ArrayList<>();
        countres.findAll().iterator().forEachRemaining(countryList::add);
        return new ResponseEntity<>(countryList, HttpStatus.OK);
    }

    @GetMapping(value="/names/start/{letter}", produces={"application/json"})
    public ResponseEntity<?> namesStartWith(@PathVariable char letter)
    {
        List<Country> countries = new ArrayList<>();
        countres.findAll().iterator().forEachRemaining(countries::add);

        List<Country> startsWith = findCountries(countries, e->e.getName().charAt(0)==Character.toUpperCase(letter));
        return new ResponseEntity<>(startsWith, HttpStatus.OK);
    }
}
