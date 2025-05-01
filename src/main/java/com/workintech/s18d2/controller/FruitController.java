package com.workintech.s18d2.controller;

import com.workintech.s18d2.dto.FruitResponse;
import com.workintech.s18d2.entity.Fruit;
import com.workintech.s18d2.services.FruitService;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/fruit")

public class FruitController {

    private final FruitService fruitService;

    @Autowired
    public FruitController(FruitService fruitService) {
        this.fruitService = fruitService;
    }

    @GetMapping
    public List<Fruit> getAllAsc() {
        return fruitService.getByPriceAsc();
    }

    @GetMapping("/desc")
    public List<Fruit> getAllDesc() {
        return fruitService.getByPriceDesc();
    }

    @GetMapping("/{id}")
    public FruitResponse getById(@Positive(message = "id sıfırdan küçük olamaz") @PathVariable Long id) {
        return new FruitResponse("get by id succeed", fruitService.getById(id));
    }



    @PostMapping
    public Fruit save(@Validated @RequestBody Fruit fruit) {
        return fruitService.save(fruit);
    }

    @GetMapping ("/name/{name}")
    public List<Fruit> searchByName(@Size(min =2, max = 45, message = "name size must be between 2 to 45") @PathVariable("name") String name) {
        return fruitService.searchByName(name);
    }

    @DeleteMapping("/{id}")
    public Fruit delete(@NotNull @Positive @PathVariable Long id) {
        return fruitService.delete(id);
    }
}
