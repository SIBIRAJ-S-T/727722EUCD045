package com.example.demo.Controller;

import com.example.demo.Service.NumberService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/numbers")
public class NumberController {

    private final NumberService numberService;

    public NumberController(NumberService numberService) {
        this.numberService = numberService;
    }

    @GetMapping("/{numberid}")
    public Map<String, Object> getNumbers(@PathVariable String numberid) {
        return numberService.fetchAndCalculateNumbers(numberid);
    }
}
