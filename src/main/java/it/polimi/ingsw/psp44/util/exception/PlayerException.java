package it.polimi.ingsw.psp44.util.exception;

/**
 * PlayerException is a class related to all player misbehaviour
 * @see the message property to check what kind of misbehaviour we're dealing with
 */
public class PlayerException extends RuntimeException {
    public PlayerException(String message){
        super(message);
    }
}
