package it.polimi.ingsw.psp44.server.controller;

import it.polimi.ingsw.psp44.network.message.Message;
import it.polimi.ingsw.psp44.network.VirtualView;
import it.polimi.ingsw.psp44.server.model.GameModel;
import it.polimi.ingsw.psp44.server.model.Player;

import java.util.HashMap;
import java.util.Map;

public class SetupController {
    private Controller controller;
    private GameModel model;

    private Map<String, VirtualView> playerViews;
    private Map<String, CardController> playerCardController;

    public SetupController() {
        this(new Controller());
    }

    public SetupController(Controller controller) {
        this.controller = controller;
        this.model = new GameModel();
        this.playerViews = new HashMap<>();
    }


    public void setup() {


    }

    /**
     * Callback that handles and processes "get card" message type.
     *
     * @param view    the VirtualView that sended the message
     * @param message the message containing information for ending the turn
     * @return <code>true</code> if the message does not require further processing, <code>false</code>  otherwise.
     */
    public Boolean getCardMessageHandler(VirtualView view, Message message) {
        if (message.getHeader() == "get card") {
            //menage request
            return true;
        }
        return false;
    }

    public void addPlayer(String nickname, VirtualView view) {
        Player player = new Player(nickname);
        this.playerViews.put(nickname, view);
        this.model.addPlayer(player);
        setHandler(view);
    }

    /**
     * Arranges the message handlers for the turn management
     */
    private void setHandler(VirtualView view) {
        view.addMessageHandler(this::chosenCardsMessageHandler);
        view.addMessageHandler(this::chosenCardsMessageHandler);
        //...dopo tutti gli handler
    }

    public int getRegisteredPlayer() {
        return this.model.getNumberOfPlayer();
    }

    public void start() {
        this.playerViews.get(this.model.getCurrentPlayerNickname()).chooseCardsFrom(/*Lista delle carte, con numero di carte da scegleire*/);
    }


    // todo sistemare Handlers con HashMap

    /**
     * @param view
     * @param message message that contains information about the chosen cards.
     * @return
     */
    public boolean chosenCardsMessageHandler(VirtualView view, Message message) {

        this.model.nextTurn();
        VirtualView currentPlayer = this.playerViews.get(this.model.getCurrentPlayerNickname());

//        List cards = json.parse(message)
        currentPlayer.chooseCardFrom(/*cards*/);

        return false;
    }

    /**
     * @param view
     * @param message contine la carta scelta, e la lista delle rimanenti.
     * @return
     */
    public boolean chosenCardMessageHandler(VirtualView view, Message message) {

/*
        List carteRimanenti = message.parse;
        String chosenCard = message.parse;

       this.controller.addPlayer(this.model.getCurrentPlayerNickname(), chosenCard);
       this.playerCardController.put(this.model.getCurrentPlayerNickname(), CardFactory.getController(chosenCard))

        this.model.nextTurn();
        VirtualView nextPlayer = this.playerViews.get(this.model.getCurrentPlayerNickname());

        if (!this.model.isFullRound())
            nextPlayer.chooseCardFrom(cards);
        else {
            view.startTurn(); // resetta l'interfaccia della view
            nextPlayer.chooseWorkersInitialPosition(25 posizioni);
        }
*/
        return false;
    }


    /**
     * posiziona gli worker sulla board
     *
     * @param view
     * @param message messaggio contenente le due positioni scelte
     * @return
     */
    public boolean chosenWorkersInitialPositionsMessageHandler(VirtualView view, Message message){

        /*

            Position[] chosenPosition = message.parse; // first male, second female

            Worker male = new Worker(this.model.getCurrentPlayerNickname(), Worker.MALE);
            Worker female = new Worker(this.model.getCurrentPlayerNickname(), Worker.FEMALE);

//          opzione 1
            Action a = new InitialPositionament(chosenPosition[0], male);
            this.model.applyAction(a)

//          opzione 2
            this.model.getBoard().setWorker(chosenPosition[0], male);
            this.model.getBoard().setWorker(chosenPosition[1], female);

            // todo Definire il ModelView
            modelView.notify();

            this.model.nextTurn();
            VirtualView nextPlayer = this.playerViews.get(this.model.getCurrentPlayerNickname());
            if(!this.model.isFullRound()){
                nextPlayer.chooseWorkersInitialPosition(this.mode.getBoard().getUnoccupiedPosition());
            }else{
                this.controller.setVirtualViews(this.playerViews)
                this.controller.setCardControllers(this.playerCardControllers)
                this.controller.setModel(this.model)

                this.controller.start()

            }

        */



        return false;
    }


}
