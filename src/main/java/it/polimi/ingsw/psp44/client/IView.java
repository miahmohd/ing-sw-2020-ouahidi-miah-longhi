package it.polimi.ingsw.psp44.client;

import it.polimi.ingsw.psp44.util.network.IVirtual;

/**
 * 
 */
public interface IView<T>{

    /**
     * Method that defines what to do in case of recieving workers
     */
    public void workers(T workers);

    /**
     * Method that defines what to do in case of recieving actions
     */
    public void actions(T actions);

    /**
     * Method that defines what to do in case of defeat
     */
    public void lost(T lost);

    /**
     * Method that defines what to do in case of victory
     */
    public void won(T won);

    /**
     * Method that defines what to do when it's your turn
     */
    public void startTurn(T startTurn);

    /**
     * Method that defines What to do when you finish your turn  
    */
    public void endTurn(T endTurn);

    /**
     * Method that defines what to do in case of update 
     */
    public void update(T update);

    /**
     * method used for setting the virtual sender
     */
    public void setVirtual(IVirtual<T> virtual);

}