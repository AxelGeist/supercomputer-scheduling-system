package nl.tudelft.sem.template.authentication.domain.user;

import commons.NetId;

/**
 * Exception to indicate that a user with this NetID does not exist in the database.
 */
public class NetIdDoesNotExistException extends Exception {
    static final long serialVersionUID = -3387516993124229948L;

    public NetIdDoesNotExistException(NetId netId) {
        super(netId.toString());
    }
}
