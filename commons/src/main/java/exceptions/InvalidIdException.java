package exceptions;

/**
 * Exception to indicate the provided id does not exist in the database.
 */
public class InvalidIdException extends Exception {

    static final long serialVersionUID = -3387516993124229948L;

    /**
     * Constructor for InvalidIdException.
     *
     * @param id invalid id provided
     */
    public InvalidIdException(long id) {
        super(Long.toString(id));
    }
}
