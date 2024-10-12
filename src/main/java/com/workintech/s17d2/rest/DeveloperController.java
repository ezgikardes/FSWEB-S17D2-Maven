package com.workintech.s17d2.rest;

import com.workintech.s17d2.enums.Experience;
import com.workintech.s17d2.model.Developer;
import com.workintech.s17d2.tax.Taxable;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/developers")
public class DeveloperController {
    public Map<Integer, Developer> developers;
    private Taxable developerTax;

    @PostConstruct
    public void init(){
        developers = new HashMap<>();
        this.developers.put(1,new Developer(1,"ali", 60000, Experience.MID));
    }

    @Autowired
    public DeveloperController(Taxable developerTax) {
        this.developerTax = developerTax;
    }

    @GetMapping("")
    public List<Developer> getDevelopers(){
        return developers.values().stream().toList();
    }

    @GetMapping("/{id}")
    public Developer getDeveloperById(@PathVariable int id){
        if (developers.containsKey(id)){
            return developers.get(id);
        }
        return null;
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public void addDeveloper(@RequestBody Developer developer){
        double taxRate = 0;
        switch (developer.getExperience()){
            case JUNIOR:
                taxRate = developerTax.getSimpleTaxRate();
                break;
            case MID:
                taxRate = developerTax.getMiddleTaxRate();
                break;
            case SENIOR:
                taxRate = developerTax.getUpperTaxRate();
                break;
            default:
                return;
        }

        developer.setSalary(developer.getSalary() - (developer.getSalary()) * taxRate);
        developers.put(developer.getId(), developer);
    }

    @PutMapping("/{id}")
    public void updateDeveloper(@PathVariable int id, @RequestBody Developer developer){
        developers.replace(developer.getId(), developer);
    }

    @DeleteMapping("/{id}")
    public void deleteDeveloper(@PathVariable int id){
        developers.remove(id);
    }


}
