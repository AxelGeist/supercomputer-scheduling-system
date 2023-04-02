package nl.tudelft.sem.template.example.domain.dto;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ChangeSchedulingStrategy {
    private String strategy;

    public ChangeSchedulingStrategy(String strategy) {
        this.strategy = strategy;
    }

    public String getStrategy() {
        return strategy;
    }

    public void setStrategy(String strategy) {
        this.strategy = strategy;
    }
}
