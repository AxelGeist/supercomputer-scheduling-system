package exceptions;

public class ResourceBiggerThanCpuException extends Exception {

    static final long serialVersionUID = -3387516993124229948L;

    public ResourceBiggerThanCpuException(String resourceOfProblem) {
        super(resourceOfProblem + " usage cannot be greater than the CPU usage.");
    }
}
