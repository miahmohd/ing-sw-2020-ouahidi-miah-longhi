package it.polimi.ingsw.psp44.client.cli;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import it.polimi.ingsw.psp44.client.IView;
import it.polimi.ingsw.psp44.network.message.Message;
import it.polimi.ingsw.psp44.network.modelview.Cell;
import it.polimi.ingsw.psp44.util.Position;
import it.polimi.ingsw.psp44.util.network.IVirtual;


public class CLIView implements IView<Message> {

    private static Board board;

    //TODO: Need a board rappresentation to do stuff on, 
    //then add a constructor that accepts the board
    private IVirtual<Message> virtualServer;
    private Scanner scanner;



    public static void main(String[] args) {

        String myPlayer = "ciao";
        String opponent1 = "mio dio";

        board = new Board(myPlayer, Arrays.asList(opponent1));

        List<Cell> cellsToUpdate;

        cellsToUpdate = new ArrayList<Cell>(Arrays.asList(
            new Cell(new Position(1, 0), 1 , false, null, ""),
            new Cell(new Position(1, 1), 2 , false, null, ""),
            new Cell(new Position(0, 1), 3 , false, null, ""),
                new Cell(new Position(2, 4), 2 , false, null, myPlayer),
                new Cell(new Position(2, 2), 0 , false, null, opponent1),
                new Cell(new Position(1, 4), 1 , false, null, myPlayer),
                new Cell(new Position(3, 3), 2 , false, null, opponent1),
            new Cell(new Position(4, 4), 1 , true, null, "")
        ));

        System.out.print(board.update(cellsToUpdate));
        //System.out.println(Graphics.Color.FIRST_LEVEL+Graphics.Color.DOME.getEscape()+Graphics.Element.FEMALE_WORKER.getEscape()+" "+Graphics.Color.SECOND_LEVEL+"secondo"+Graphics.Color.THIRD_LEVEL+"terzo"+ Graphics.Color.RESET);
    }

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
        //Message message = new Message("worker", option);
        //virtualServer.sendMessage(message);

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