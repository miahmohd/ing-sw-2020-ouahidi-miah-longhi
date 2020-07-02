package it.polimi.ingsw.psp44.client;


public abstract class AbstractView {

    /**
     * AbstractView's user
     */
    protected String playerNickname;

    /**
     * Sender attribute
     */
    protected VirtualServer virtualServer;

    /**
     * method used for setting the virtual sender
     */
    public abstract void setServer(VirtualServer virtualServer);

}