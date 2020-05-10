package it.polimi.ingsw.psp44.client.cli;

import it.polimi.ingsw.psp44.client.VirtualServer;
import it.polimi.ingsw.psp44.network.communication.BodyFactory;
import it.polimi.ingsw.psp44.network.message.Message;
import it.polimi.ingsw.psp44.network.message.MessageHeader;
import it.polimi.ingsw.psp44.util.Card;
import it.polimi.ingsw.psp44.util.JsonConvert;
import it.polimi.ingsw.psp44.util.Position;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SetupView {

    private Console console;
    private VirtualServer virtualServer;


    private String playerNickname;
    private Board board;

    public SetupView(String playerNickname, Console console){
        this.playerNickname = playerNickname;
        this.console = console;
        this.board = new Board();
    }

    public void chooseCardsFrom(Message cards) {
        Card[] cardList, chosenCards;
        String body;
        Message response;

        int cardinality = Integer.parseInt(cards.getHeader().get(MessageHeader.CARDINALITY));

        cardList = BodyFactory.fromCards(cards.getBody());
        chosenCards = new Card[cardinality];

        console.writeLine(String.format("You need to choose %d cards from this pile (just id)", cardinality));

        for (Card card : cardList) {
            console.writeLine(card);
            console.writeLine(Graphics.Behaviour.NEW_LINE);
        }
        for(int numberOfCardCounter = 0; numberOfCardCounter < cardinality; numberOfCardCounter++) {
            chosenCards[numberOfCardCounter] = getChosenCard(cardList);
        }
        body = BodyFactory.toCards(chosenCards); JsonConvert.getInstance().toJson(chosenCards, Card[].class);
        response = new Message(Message.Code.CHOSEN_CARDS, body);
        virtualServer.sendMessage(response);
    }

    public void chooseCardFrom(Message cards) {
        Card[] cardList, restOfCards;
        Card chosenCard;

        String body;
        Message message;

        cardList = BodyFactory.fromCards(cards.getBody());

        for (Card card : cardList) {
            console.writeLine(card);
            console.writeLine(Graphics.Element.EMPTY);
        }

        chosenCard = getChosenCard(cardList);
        restOfCards = Arrays.stream(cardList).filter(card -> !card.equals(chosenCard)).toArray(Card[]::new);

        body = BodyFactory.toChosenCard(chosenCard, restOfCards);

        message = new Message(Message.Code.CHOSEN_CARD, body);
        virtualServer.sendMessage(message);
    }

    public void chooseWorkersInitialPositionFrom(Message workers) {
        Position femalePosition, malePosition;
        Position[] positionsToSend;

        String body;
        Message message;

        List<Position> positionsToChooseFrom = new ArrayList<>(
                Arrays.asList(BodyFactory.fromPositions(workers.getBody()))
        );

        //TODO: check if the chosen position is right positionsToChooseFrom.contains(position)
        String board = this.board.highlightPositions(positionsToChooseFrom);
        console.printOnBoardSection(board);

        console.writeLine("choose positions ");
        console.writeLine("gimme the female ");
        femalePosition = console.readPosition();
        console.writeLine("gimme the male ");
        malePosition = console.readPosition();

        positionsToSend = new Position[]{femalePosition, malePosition};

        body = BodyFactory.toPositions(positionsToSend);
        message = new Message(Message.Code.CHOSEN_WORKERS_INITIAL_POSITION, body);
        virtualServer.sendMessage(message);
    }

    public void start(Message start){
        //TODO: create coso di game
    }

    public void end(Message end) {

    }

    public void setServer(VirtualServer virtual){
        this.virtualServer = virtual;

        virtualServer.cleanRoutes();
        console.writeLine("polito");

        virtualServer.addRoute(Message.Code.START, this::start);
        virtualServer.addRoute(Message.Code.CHOOSE_CARD, this::chooseCardFrom);
        virtualServer.addRoute(Message.Code.CHOOSE_CARDS, this::chooseCardsFrom);
        virtualServer.addRoute(Message.Code.CHOOSE_WORKERS_INITIAL_POSITION, this::chooseWorkersInitialPositionFrom);
        virtualServer.addRoute(Message.Code.TURN_END, this::end);

        console.writeLine("aggiunto");

    }

    private Card getChosenCard(Card[] cards){
        Card chosenCard;

        console.writeLine("gimmie the id ");
         do {
            int chosenCardId = console.readNumber();

            chosenCard = Arrays.stream(cards).filter(
                    card -> card.getId() == chosenCardId)
                    .findFirst()
                    .orElse(null);

            if(chosenCard == null)
                console.writeLine("not a valid id");

        } while(chosenCard == null);


        return chosenCard;
    }
}