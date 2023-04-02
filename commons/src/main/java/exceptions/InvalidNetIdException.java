package exceptions;

/**
 * Exception to indicate that the provided NetId is invalid. The netId can be invalid if it is null,
 * or it does not correspond to the session netId.
 */
public class InvalidNetIdException extends Exception {

    static final long serialVersionUID = -3387516993124229948L;

    /**
     * Constructor for the InvalidNetIdException.
     *
     * @param netId invalid NetId provided
     */
    public InvalidNetIdException(String netId) {
        super(netId);
    }
}
