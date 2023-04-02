package nl.tudelft.sem.template.example.chain;

public class JobRejectedException extends Exception {

    static final long serialVersionUID = -3387516993124229948L;

    public JobRejectedException(String message) {
        super(message);
    }
}
