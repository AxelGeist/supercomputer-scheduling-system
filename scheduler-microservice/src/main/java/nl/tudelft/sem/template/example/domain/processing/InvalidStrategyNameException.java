package nl.tudelft.sem.template.example.domain.processing;

public class InvalidStrategyNameException extends Exception {
    static final long serialVersionUID = -3387516993124229948L;

    public InvalidStrategyNameException(String strategy) {
        super("Strategy " + strategy + " does not exist.");
    }
}
