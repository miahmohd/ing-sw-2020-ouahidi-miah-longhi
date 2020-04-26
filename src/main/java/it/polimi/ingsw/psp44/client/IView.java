package it.polimi.ingsw.psp44.client;

import it.polimi.ingsw.psp44.util.network.IVirtual;

/**
 *
 */
public interface IView<T> {

    /**
     * Method that defines what to do in case of recieving workers
     */
    void workers(T workers);

    /**
     * Method that defines what to do in case of recieving actions
     */
    void actions(T actions);

    /**
     * Method that defines what to do in case of defeat
     */
    void lost(T lost);

    /**
     * Method that defines what to do in case of victory
     */
    void won(T won);

    /**
     * Method that defines what to do when it's your turn
     */
    void startTurn(T startTurn);

    /**
     * Method that defines What to do when you finish your turn
     */
    void endTurn(T endTurn);

    /**
     * Method that defines what to do when players are sent
     * @param players
     */
    void players(T players);

    /**
     * Method that defines what to do in case of update
     */
    void update(T update);

    /**
     * method used for setting the virtual sender
     */
    void setServer(IVirtual<T> virtual);

}