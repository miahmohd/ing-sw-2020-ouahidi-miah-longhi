package it.polimi.ingsw.psp44.util.exception;

/**
 * PlayerException is a class related to all player misbehaviour
 * @see the message property to check what kind of misbehaviour we're dealing with
 */
public class PlayerException extends RuntimeException {

    
    /**
     *this serialVersionUID os generated automatically by @see VSCode
     */
    private static final long serialVersionUID = -4963270003496877859L;

    public PlayerException(String message) {
        super(message);
    }
}
