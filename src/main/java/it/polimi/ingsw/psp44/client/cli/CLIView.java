package it.polimi.ingsw.psp44.client.cli;

import it.polimi.ingsw.psp44.client.IView;
import it.polimi.ingsw.psp44.network.message.Message;
import it.polimi.ingsw.psp44.util.JsonConvert;
import it.polimi.ingsw.psp44.util.Position;
import it.polimi.ingsw.psp44.util.network.IVirtual;

import java.util.Scanner;


public class CLIView implements IView<Message> {

    private final Board board;

    //TODO: Need a board rappresentation to do stuff on,
    //then add a constructor that accepts the board
    private IVirtual<Message> virtualServer;
    private final Scanner input;
    private final StringBuffer display;


    /**
     *
     * @param input
     * @param display
     * @param board
     */
    public CLIView(Scanner input, StringBuffer display, Board board) {
        this.input = input;
        this.display = display;
        this.board = board;
    }

    public CLIView() {
        this(new Scanner(System.in), new StringBuffer(), new Board());
    }

    private void sendAction() {
        //ASK Action input
    }

    private void sendWorkerPosition() {
        // FIXME: this is just an example
        System.out.println("choose position");
        String option = input.nextLine();

        //format the message
        //Message message = new Message("worker", option);
        //virtualServer.sendMessage(message);

    }


    @Override
    public void workers(Message workers) {
        String body = workers.getBody();
        Position[] workerPositions = JsonConvert.getInstance().fromJson(body, Position[].class);

        board.highlightPositions(workerPositions);

        for (Position workerPosition : workerPositions){
            display.append(workerPosition);
        }

        //TODO: add the stuff
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
    public void players(Message players) {

    }

    @Override
    public void update(Message update) {
        // TODO: extract the info from Message
        // Update the view
    }


    @Override
    public void setServer(IVirtual<Message> virtualServer) {
        this.virtualServer = virtualServer;
    }

}


/**
 public static void main(String[] args) {

 String myPlayer = "ciao";
 String opponent1 = "mio dio";

 board = new Board(myPlayer, Arrays.asList(opponent1));

 List<Cell> cellsToUpdate;

 cellsToUpdate = new ArrayList<Cell>(Arrays.asList(
 new Cell(new Position(1, 0), 1, false, null, ""),
 new Cell(new Position(1, 1), 2, false, null, ""),
 new Cell(new Position(0, 1), 3, false, null, ""),
 new Cell(new Position(2, 4), 2, false, null, myPlayer),
 new Cell(new Position(2, 2), 0, false, null, opponent1),
 new Cell(new Position(1, 4), 1, false, null, myPlayer),
 new Cell(new Position(3, 3), 2, false, null, opponent1),
 new Cell(new Position(4, 4), 1, true, null, "")
 ));

 System.out.print(board.update(cellsToUpdate));
 //System.out.println(Graphics.Color.FIRST_LEVEL+Graphics.Color.DOME.getEscape()+Graphics.Element.FEMALE_WORKER.getEscape()+" "+Graphics.Color.SECOND_LEVEL+"secondo"+Graphics.Color.THIRD_LEVEL+"terzo"+ Graphics.Color.RESET);
 }
 */