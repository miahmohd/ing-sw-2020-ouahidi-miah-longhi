package it.polimi.ingsw.psp44.client.cli;

import java.util.Scanner;

import it.polimi.ingsw.psp44.client.IView;
import it.polimi.ingsw.psp44.network.Message;
import it.polimi.ingsw.psp44.util.network.IVirtual;


public class CLIView implements IView<Message> {

    //TODO: Need a board rappresentation to do stuff on, 
    //then add a constructor that accepts the board
    private IVirtual<Message> virtualServer;
    private Scanner scanner;

    /**
     * This is just for testing purpuses
     * @param scanner
     */
    public CLIView(Scanner scanner) {
        this.scanner = scanner;
        //INIT Board
    }

    public CLIView() {
        this(new Scanner(System.in));
    }
    

    public void sendAction(){
        //ASK Action input
    }

    public void sendWorkerPosition(){
        // FIXME: this is just an example
        System.out.println("choose position");
        String option = scanner.nextLine();

        //format the message
        Message message = new Message("worker", option);
        virtualServer.sendMessage(message);

    }


    @Override
    public void workers(Message workers) {
        // TODO: get the workers by parsing the message
        // call sendWorkerPosition by passing the workers Positions
    }

    @Override
    public void actions(Message actions) {
        // TODO: get the actions by parsing the message
        // call sendAction by passing possible actions

    }

    @Override
    public void lost(Message lost) {
        // TODO: What to do when you lose
        // The message body may contain some info
        
    }

    @Override
    public void won(Message won) {
        // TODO: what to do when you win
        // The message body may contain some info
        
    }

    @Override
    public void startTurn(Message startTurn) {
        // TODO: What to do when you turn is up
    }

    @Override
    public void endTurn(Message endTurn) {
        // TODO: What to do when your turn is over

    }

    @Override
    public void update(Message update) {
        // TODO: extract the info from Message
        // Update the view
    }


    @Override
    public void setVirtual(IVirtual<Message> virtualServer) {
        this.virtualServer = virtualServer;
    }

}