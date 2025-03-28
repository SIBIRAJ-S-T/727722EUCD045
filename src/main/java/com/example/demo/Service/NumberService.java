package com.example.demo.Service;//

import com.example.demo.Repository.NumberRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class NumberService {

    private final NumberRepository numberRepository;
    private static final int WINDOW_SIZE = 10;
    private static final int MAX_VALUE = 15; // Ensure all numbers are within 1-30
    private final Map<String, LinkedList<Integer>> windowMap = new HashMap<>();

    public NumberService(NumberRepository numberRepository) {
        this.numberRepository = numberRepository;
    }

    public Map<String, Object> fetchAndCalculateNumbers(String numberid) {
        List<Integer> fetchedNumbers = getNumbersByType(numberid);
        List<Integer> prevWindow = new ArrayList<>(windowMap.getOrDefault(numberid, new LinkedList<>()));

        updateSlidingWindow(numberid, fetchedNumbers);

        List<Integer> currentWindow = new ArrayList<>(windowMap.get(numberid));
        double avg = currentWindow.stream().mapToInt(Integer::intValue).average().orElse(0.0);

        return Map.of(
            "windowPrevState", prevWindow,
            "windowCurrState", currentWindow,
            "numbers", fetchedNumbers,
            "avg", avg
        );
    }

    private List<Integer> getNumbersByType(String numberid) {
        List<Integer> allNumbers = numberRepository.getStoredNumbers();

        // Ensure only numbers within the range 1 to 30
        List<Integer> filteredNumbers = allNumbers.stream()
            .filter(num -> num >= 1 && num <= MAX_VALUE)
            .toList();

        return switch (numberid) {
            case "p" -> filterPrimes(filteredNumbers);
            case "e" -> filterEvenNumbers(filteredNumbers);
            case "f" -> filterFibonacci(filteredNumbers);
            case "r" -> getRandomNumbers(filteredNumbers);
            default -> Collections.emptyList();
        };
    }

    private void updateSlidingWindow(String numberid, List<Integer> newNumbers) {
        windowMap.putIfAbsent(numberid, new LinkedList<>());
        LinkedList<Integer> window = windowMap.get(numberid);

        for (Integer num : newNumbers) {
            if (!window.contains(num)) {
                if (window.size() >= WINDOW_SIZE) {
                    window.pollFirst(); // Remove the oldest number
                }
                window.add(num);
            }
        }
    }

    private List<Integer> filterPrimes(List<Integer> numbers) {
        return numbers.stream().filter(this::isPrime).toList();
    }

    private boolean isPrime(int num) {
        if (num < 2) return false;
        for (int i = 2; i <= Math.sqrt(num); i++) {
            if (num % i == 0) return false;
        }
        return true;
    }

    private List<Integer> filterEvenNumbers(List<Integer> numbers) {
        return numbers.stream().filter(n -> n % 2 == 0).toList();
    }

    private List<Integer> filterFibonacci(List<Integer> numbers) {
        Set<Integer> fibSet = generateFibonacciUpTo(MAX_VALUE); // Changed max to 30
        return numbers.stream().filter(fibSet::contains).toList();
    }

    private Set<Integer> generateFibonacciUpTo(int max) {
        Set<Integer> fibSet = new HashSet<>();
        int a = 0, b = 1;
        while (a <= max) {
            fibSet.add(a);
            int temp = a + b;
            a = b;
            b = temp;
        }
        return fibSet;
    }

    private List<Integer> getRandomNumbers(List<Integer> numbers) {
        List<Integer> shuffled = new ArrayList<>(numbers);
        Collections.shuffle(shuffled);
        return shuffled.subList(0, Math.min(WINDOW_SIZE, shuffled.size()));
    }
}
