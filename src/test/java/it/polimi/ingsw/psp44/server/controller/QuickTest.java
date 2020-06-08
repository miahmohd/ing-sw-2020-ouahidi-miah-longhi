package it.polimi.ingsw.psp44.server.controller;

import it.polimi.ingsw.psp44.network.message.Message;
import it.polimi.ingsw.psp44.server.FileConnection;
import it.polimi.ingsw.psp44.server.model.GameModel;
import it.polimi.ingsw.psp44.server.model.Player;
import it.polimi.ingsw.psp44.server.view.VirtualView;
import it.polimi.ingsw.psp44.util.Card;
import it.polimi.ingsw.psp44.util.JsonConvert;
import it.polimi.ingsw.psp44.util.R;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringWriter;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;


public class QuickTest {

    private Controller match1;
    private Card[] cards;

    @Before
    public void setUp() {
        match1 = new Controller();
        cards = R.getCards();

    }

    @Test
    public void twoPlayer() throws IOException, URISyntaxException {
        Map<String, CardController> players1 = new HashMap<>();
        Map<String, VirtualView> playerViews1 = new HashMap<>();
        GameModel model1 = new GameModel();

        model1.addPlayer(new Player("p15"));
        model1.addPlayer(new Player("p12"));


        //Messages send to views
        // 12 vs 15
        StringWriter p12ActualOut = new StringWriter();
        StringWriter p15ActualOut = new StringWriter();
        //Expected messages received from the views
        // 12 vs 15
        FileReader p12ExpectedIn = new FileReader(new File(getClass().getResource("/controllertest/twoplayer/p12.in.txt").getPath()));
        FileReader p15ExpectedIn = new FileReader(new File(getClass().getResource("/controllertest/twoplayer/p15.in.txt").getPath()));

        //Fake socket connection
        // 12 vs 15
        FileConnection p12Connection = new FileConnection(p12ExpectedIn, p12ActualOut);
        FileConnection p15Connection = new FileConnection(p15ExpectedIn, p15ActualOut);

        //Initialize the views
        // 12 vs 15
        VirtualView p12 = new VirtualView(p12Connection);
        p12.startPingTask();
        VirtualView p15 = new VirtualView(p15Connection);
        p15.startPingTask();
        playerViews1.put("p15", p15);
        playerViews1.put("p12", p12);

        //Initialize card controller
        CardController cardController;
        // 12 vs 15
        cardController = CardFactory.getController(cards[10]);
        cardController.setContext(match1);
        players1.put("p15", cardController);
        cardController = CardFactory.getController(cards[9]);
        cardController.setContext(match1);
        players1.put("p12", cardController);


        //Initialize controller
        // 12 vs 15
        match1.setModel(model1);
        match1.setCardControllers(players1);
        match1.setVirtualViews(playerViews1);
        Message in, out;
        match1.init(false);
        do{
            do {
                in=getNextMessageFrom(p15Connection);
                routeMessage(match1, p15,in);
                out=getLastMessage(p15ActualOut);
            } while (out.getCode() != Message.Code.END_TURN);
            if(p12Connection.isActive()) {
                do {
                    in = getNextMessageFrom(p12Connection);
                    routeMessage(match1, p12, in);
                    out = getLastMessage(p12ActualOut);
                } while (out.getCode() != Message.Code.END_TURN);
            }
        }while(!isFinished(p15ActualOut));



        //Expected messages sent to the views
        List<String> p1ExpectedOut = Files.readAllLines(Paths.get(getClass().getResource("/controllertest/twoplayer/p12.out.txt").toURI()));
        List<String> p2ExpectedOut = Files.readAllLines(Paths.get(getClass().getResource("/controllertest/twoplayer/p15.out.txt").toURI()));

        String br = System.getProperty("line.separator");
        assertEquals(String.join(br, p1ExpectedOut) + br, p12ActualOut.toString());
        assertEquals(String.join(br, p2ExpectedOut) + br, p15ActualOut.toString());

        p12ExpectedIn.close();
        p15ExpectedIn.close();

    }

    private boolean isFinished(StringWriter p9ActualOut) {
        String br = System.getProperty("line.separator");
        String[] messages = p9ActualOut.toString().split(br);
        return messages[messages.length - 2].contains("WON");

    }

    private Message getLastMessage(StringWriter writer) {
        String br = System.getProperty("line.separator");
        String[] messages = writer.toString().split(br);
        return JsonConvert.getInstance().fromJson(messages[messages.length - 1], Message.class);

    }

    private Message getNextMessageFrom(FileConnection c) throws IOException {
        return JsonConvert.getInstance().fromJson(c.readLine(), Message.class);
    }

    private void routeMessage(Controller c, VirtualView v, Message m) {
        switch (m.getCode()) {
            case CHOSEN_WORKERS_INITIAL_POSITION:
                c.chosenWorkersInitialPositionsMessageHandler(v,m);
                break;
            case CHOSEN_ACTION:
                c.chosenActionMessageHandler(v,m);
                break;
            case CHOSEN_WORKER:
                c.chosenWorkerMessageHandler(v,m);
                break;

        }
    }
}