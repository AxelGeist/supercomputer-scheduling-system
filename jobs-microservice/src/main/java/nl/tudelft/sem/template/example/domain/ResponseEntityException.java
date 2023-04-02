package nl.tudelft.sem.template.example.domain;

public class ResponseEntityException extends Exception {
    static final long serialVersionUID = -3387516993124229948L;

    public ResponseEntityException() {
        super("Problem: ResponseEntity was null!");
    }
}
