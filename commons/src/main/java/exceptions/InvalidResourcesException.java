package exceptions;


/**
 * Exception to indicate that some requested resources are invalid.
 */
public class InvalidResourcesException extends Exception {

    static final long serialVersionUID = -3387516993124229948L;

    /**
     * Constructor of InvalidResourcesException.
     *
     * @param resource the invalid resource
     */
    public InvalidResourcesException(int resource) {
        super(Integer.toString(resource));
    }

}
