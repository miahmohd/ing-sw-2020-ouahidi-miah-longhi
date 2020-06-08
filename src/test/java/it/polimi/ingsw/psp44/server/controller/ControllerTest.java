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
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;


public class ControllerTest {

    private Controller match1;
    private Controller match2;
    private Card[] cards;

    @Before
    public void setUp() {
        match1 = new Controller();
        match2 = new Controller();
        cards = R.getCards();

    }

    @Test
    public void threePlayer() throws IOException, URISyntaxException {
        //Map<String, CardController> players1 = new HashMap<>();
        Map<String, CardController> players2 = new HashMap<>();
        //Map<String, VirtualView> playerViews1 = new HashMap<>();
        Map<String, VirtualView> playerViews2 = new HashMap<>();
        //GameModel model1 = new GameModel();
        GameModel model2 = new GameModel();

        //model1.addPlayer(new Player("p1"));
        //model1.addPlayer(new Player("p2"));
        //model1.addPlayer(new Player("p10"));
        model2.addPlayer(new Player("p5"));
        model2.addPlayer(new Player("p6"));
        model2.addPlayer(new Player("p9"));


        //Messages send to views
        // 1 vs 2 vs 10
        //StringWriter p1ActualOut = new StringWriter();
        //StringWriter p2ActualOut = new StringWriter();
        //StringWriter p10ActualOut = new StringWriter();
        // 5 vs 6 vs 9
        StringWriter p5ActualOut = new StringWriter();
        StringWriter p6ActualOut = new StringWriter();
        StringWriter p9ActualOut = new StringWriter();
        //URL c = getClass().getResource("/controllertest/threeplayer/p1.in.txt");
        //Expected messages received from the views
        // 1 vs 2 vs 10
        //FileReader p1ExpectedIn = new FileReader(new File(getClass().getResource("/controllertest/threeplayer/p1.in.txt").getPath()));
        //FileReader p2ExpectedIn = new FileReader(new File(getClass().getResource("/controllertest/threeplayer/p2.in.txt").getPath()));
        //FileReader p10ExpectedIn = new FileReader(new File(getClass().getResource("/controllertest/threeplayer/p10.in.txt").getPath()));
        // 5 vs 6 vs 9
        FileReader p5ExpectedIn = new FileReader(new File(getClass().getResource("/controllertest/threeplayer/p5.in.txt").getPath()));
        FileReader p6ExpectedIn = new FileReader(new File(getClass().getResource("/controllertest/threeplayer/p6.in.txt").getPath()));
        FileReader p9ExpectedIn = new FileReader(new File(getClass().getResource("/controllertest/threeplayer/p9.in.txt").getPath()));

        //Fake socket connection
        // 1 vs 2 vs 10
        //FileConnection p1Connection = new FileConnection(p1ExpectedIn, p1ActualOut);
        //FileConnection p2Connection = new FileConnection(p2ExpectedIn, p2ActualOut);
        //FileConnection p10Connection = new FileConnection(p10ExpectedIn, p10ActualOut);
        // 5 vs 6 vs 9
        FileConnection p5Connection = new FileConnection(p5ExpectedIn, p5ActualOut);
        FileConnection p6Connection = new FileConnection(p6ExpectedIn, p6ActualOut);
        FileConnection p9Connection = new FileConnection(p9ExpectedIn, p9ActualOut);


        //Initialize the views
        // 1 vs 2 vs 10
        //VirtualView p1 = new VirtualView(p1Connection);
        //VirtualView p2 = new VirtualView(p2Connection);
        //VirtualView p10 = new VirtualView(p10Connection);
        //playerViews1.put("p1", p1);
        //playerViews1.put("p2", p2);
        //playerViews1.put("p10", p10);
        // 5 vs 6 vs 9
        VirtualView p5 = new VirtualView(p5Connection);
        p5.startPingTask();
        VirtualView p6 = new VirtualView(p6Connection);
        p6.startPingTask();
        VirtualView p9 = new VirtualView(p9Connection);
        p9.startPingTask();
        playerViews2.put("p5", p5);
        playerViews2.put("p6", p6);
        playerViews2.put("p9", p9);

        //Initialize card controller
        CardController cardController;
        // 1 vs 2 vs 10
        //cardController = CardFactory.getController(cards[0]);
        //cardController.setContext(match1);
        //players1.put("p1", cardController);
        //cardController = CardFactory.getController(cards[1]);
        //cardController.setContext(match1);
        //players1.put("p2", cardController);
        //cardController = CardFactory.getController(cards[8]);
        //cardController.setContext(match1);
        //players1.put("p10", cardController);
        // 5 vs 6 vs 9
        cardController = CardFactory.getController(cards[4]);
        cardController.setContext(match2);
        players2.put("p5", cardController);
        cardController = CardFactory.getController(cards[5]);
        cardController.setContext(match2);
        players2.put("p6", cardController);
        cardController = CardFactory.getController(cards[7]);
        cardController.setContext(match2);
        players2.put("p9", cardController);

        //Initialize controller
        // 1 vs 2 vs 10
        //match1.setModel(model1);
        //match1.setCardControllers(players1);
        //match1.setVirtualViews(playerViews1);
//      // 5 vs 6 vs 9
        match2.setModel(model2);
        match2.setCardControllers(players2);
        match2.setVirtualViews(playerViews2);

        /*match1.init(false);
        do{
            do {
                routeMessage(match1, p1,getNextMessageFrom(p1Connection));
            } while (getLastMessage(p1ActualOut).getCode() != Message.Code.END_TURN);
            do {
                routeMessage(match1, p2,getNextMessageFrom(p2Connection));
            } while (getLastMessage(p2ActualOut).getCode() != Message.Code.END_TURN);
            do {
                routeMessage(match1, p10,getNextMessageFrom(p10Connection));
            } while (getLastMessage(p10ActualOut).getCode() != Message.Code.END_TURN);
        }while(p10Connection.readLine()==null);*/

        match2.init(false);
        Message sent=null,recived=null;
        do{
            do {
                sent=getNextMessageFrom(p5Connection);
                    routeMessage(match2, p5, sent);
                    recived = getLastMessage(p5ActualOut);
            } while(recived.getCode() != Message.Code.END_TURN);
            do {
                sent=getNextMessageFrom(p6Connection);
                    routeMessage(match2, p6, sent);
                    recived = getLastMessage(p6ActualOut);
            } while (recived.getCode() != Message.Code.END_TURN);
            do {
                sent=getNextMessageFrom(p9Connection);
                    routeMessage(match2, p9, sent);
                    recived = getLastMessage(p9ActualOut);
            } while (recived.getCode() != Message.Code.END_TURN);
        }while (!isFinished(p9ActualOut));

        //Expected messages sent to the views
        //List<String> p1ExpectedOut = Files.readAllLines(Paths.get(getClass().getResource("/controllertest/threeplayer/p1.out.txt").toURI()));
        //List<String> p2ExpectedOut = Files.readAllLines(Paths.get(getClass().getResource("/controllertest/threeplayer/p2.out.txt").toURI()));
        //List<String> p10ExpectedOut = Files.readAllLines(Paths.get(getClass().getResource("/controllertest/threeplayer/p10.out.txt").toURI()));
        List<String> p5ExpectedOut = Files.readAllLines(Paths.get(getClass().getResource("/controllertest/threeplayer/p5.out.txt").toURI()));
        List<String> p6ExpectedOut = Files.readAllLines(Paths.get(getClass().getResource("/controllertest/threeplayer/p6.out.txt").toURI()));
        List<String> p9ExpectedOut = Files.readAllLines(Paths.get(getClass().getResource("/controllertest/threeplayer/p9.out.txt").toURI()));

        String br = System.getProperty("line.separator");
        //assertEquals(String.join(br, p1ExpectedOut) + br, p1ActualOut.toString());
        //assertEquals(String.join(br, p2ExpectedOut) + br, p2ActualOut.toString());
        //assertEquals(String.join(br, p10ExpectedOut) + br, p10ActualOut.toString());
        assertEquals(String.join(br, p5ExpectedOut) + br, p5ActualOut.toString());
        assertEquals(String.join(br, p6ExpectedOut) + br, p6ActualOut.toString());
        assertEquals(String.join(br, p9ExpectedOut) + br, p9ActualOut.toString());

        //p1ExpectedIn.close();
        //p2ExpectedIn.close();
        //p10ExpectedIn.close();
        p5ExpectedIn.close();
        p6ExpectedIn.close();
        p9ExpectedIn.close();

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