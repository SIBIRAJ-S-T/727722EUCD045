package com.example.demo.Repository;

import org.springframework.stereotype.Repository;
import java.util.*;

@Repository
public class NumberRepository {
    private static final int WINDOW_SIZE = 10;
    private final LinkedList<Integer> storedNumbers = new LinkedList<>();

    public NumberRepository() {
        initializeNumbers();
    }

    private void initializeNumbers() {
        for (int i = 1; i <= 500; i++) {
            storedNumbers.add(i);
        }
    }

    public List<Integer> getStoredNumbers() {
        return storedNumbers;
    }
}
