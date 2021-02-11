package com.lambdaschool.countries.controllers;

import com.lambdaschool.countries.models.Country;
import com.lambdaschool.countries.repositories.CountryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.net.ssl.HttpsURLConnection;
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

    //get all countries
    @GetMapping(value="/names/all", produces={"application/json"})
    public ResponseEntity<?> listAllCountries()
    {
        List<Country> countryList = new ArrayList<>();
        countres.findAll().iterator().forEachRemaining(countryList::add);
        return new ResponseEntity<>(countryList, HttpStatus.OK);
    }

    //get countries that start with _
    @GetMapping(value="/names/start/{letter}", produces={"application/json"})
    public ResponseEntity<?> namesStartWith(@PathVariable char letter)
    {
        List<Country> countries = new ArrayList<>();
        countres.findAll().iterator().forEachRemaining(countries::add);

        List<Country> startsWith = findCountries(countries, e->e.getName().charAt(0)==Character.toUpperCase(letter));
        return new ResponseEntity<>(startsWith, HttpStatus.OK);
    }

    //return String with sum of all populations
    @GetMapping(value="/population/total")
    public ResponseEntity<?> populationTotal()
    {
        List<Country> countries = new ArrayList<>();
        countres.findAll().iterator().forEachRemaining(countries::add);

        double total=0;
        for (Country c : countries) {
            System.out.println(c.getPopulation());
            total = c.getPopulation() + total;
        }
        String popTotal = "The total population sum is "+total;
        return new ResponseEntity<>(popTotal, HttpStatus.OK);
    }

    //get the country with the least populous
    @GetMapping(value="/population/min")
    public ResponseEntity<?> populationMin()
    {
        List<Country> countries = new ArrayList<>();
        countres.findAll().iterator().forEachRemaining(countries::add);
        int min = 0;
        for(int i = 0; i < countries.size(); i++)
        {
            if(Math.abs(countries.get(i).getPopulation()-countries.get(min).getPopulation())>= 0.000001E8)
            {
                System.out.println(countries.get(i).getPopulation());
                min = i;
            }
        }

        return new ResponseEntity<>(countries.get(min), HttpStatus.OK);
    }

    //get country with the biggest population
    @GetMapping(value="/population/max", produces = {"application/json"})
    public ResponseEntity<?> populationMax()
    {
        List<Country> countries = new ArrayList<>();
        countres.findAll().iterator().forEachRemaining(countries::add);
        int max = 0;
        for(int i = 0; i < countries.size(); i++)
        {
            if(Math.abs(countries.get(i).getPopulation()-countries.get(max).getPopulation())<= 0.000001E8)
            {
                System.out.println(countries.get(i).getPopulation());
                max = i;
            }
        }

        return new ResponseEntity<>(countries.get(max), HttpStatus.OK);
    }

}
