package it.polimi.ingsw.psp44.server.controller;

import it.polimi.ingsw.psp44.network.message.Message;
import it.polimi.ingsw.psp44.server.FileConnection;
import it.polimi.ingsw.psp44.server.view.VirtualView;
import it.polimi.ingsw.psp44.util.JsonConvert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringWriter;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;


public class SetupControllerTest {

    private SetupController setupController;

    @Before
    public void setUp() {
        setupController = new SetupController();
    }

    @Test
    public void threePlayer() throws IOException, URISyntaxException {

//        Actual messages sent to the views
        StringWriter p1ActualOut = new StringWriter();
        StringWriter p2ActualOut = new StringWriter();
        StringWriter p3ActualOut = new StringWriter();

//        Expected messages received from the views
        FileReader p1ExpectedIn = new FileReader(new File(getClass().getResource("/setuptest/threeplayer/p1.in.txt").getPath()));
        FileReader p2ExpectedIn = new FileReader(new File(getClass().getResource("/setuptest/threeplayer/p2.in.txt").getPath()));
        FileReader p3ExpectedIn = new FileReader(new File(getClass().getResource("/setuptest/threeplayer/p3.in.txt").getPath()));

        FileConnection p1Connection = new FileConnection(p1ExpectedIn, p1ActualOut);
        FileConnection p2Connection = new FileConnection(p2ExpectedIn, p2ActualOut);
        FileConnection p3Connection = new FileConnection(p3ExpectedIn, p3ActualOut);

//        Initialize the views
        VirtualView p1 = new VirtualView(p1Connection);
        VirtualView p2 = new VirtualView(p2Connection);
        VirtualView p3 = new VirtualView(p3Connection);


        setupController.addPlayer("p1", p1);
        assertEquals(1, setupController.getRegisteredPlayer());
        setupController.addPlayer("p2", p2);
        assertEquals(2, setupController.getRegisteredPlayer());
        setupController.addPlayer("p3", p3);
        assertEquals(3, setupController.getRegisteredPlayer());

//        start() must sent messages only to the first player,
//        start() does not need inputs.
//        Send START CHOOSE_CARDS to p1
        setupController.start();

//        Receive CHOSEN_CARDS from p1 and send CHOOSE_CARD to p2
        setupController.chosenCardsMessageHandler(p1, getNextMessageFrom(p1Connection));

//        Receive CHOSEN_CARD from p2, and send CHOOSE_CARD to p3
        setupController.chosenCardMessageHandler(p2, getNextMessageFrom(p2Connection));

//        Receive CHOSEN_CARD from p3, and send CHOOSE_WORKERS_INITIAL_POSITION to p1
        setupController.chosenCardMessageHandler(p3, getNextMessageFrom(p3Connection));

//        Receive CHOSEN_WORKERS_INITIAL_POSITION from p1, notify changed positions to all, and send CHOOSE_WORKERS_INITIAL_POSITION to p2
        setupController.chosenWorkersInitialPositionsMessageHandler(p1, getNextMessageFrom(p1Connection));

//        Receive CHOSEN_WORKERS_INITIAL_POSITION from p2, notify changed positions to all, and send CHOOSE_WORKERS_INITIAL_POSITION to p3
        setupController.chosenWorkersInitialPositionsMessageHandler(p2, getNextMessageFrom(p2Connection));

//        Receive CHOSEN_WORKERS_INITIAL_POSITION from p3, notify changed positions to all, and send START, CHOOSE_WORKER to p1 for game phase.
//        setupController.chosenWorkersInitialPositionsMessageHandler(p3, getNextMessageFrom(p3Connection));


//        Expected messages sent to the views
        String p1ExpectedOut = new String(Files.readAllBytes(Paths.get(getClass().getResource("/setuptest/threeplayer/p1.out.txt").toURI())));
        String p2ExpectedOut = new String(Files.readAllBytes(Paths.get(getClass().getResource("/setuptest/threeplayer/p2.out.txt").toURI())));
        String p3ExpectedOut = new String(Files.readAllBytes(Paths.get(getClass().getResource("/setuptest/threeplayer/p3.out.txt").toURI())));

        p1ExpectedIn.close();
        p2ExpectedIn.close();
        p3ExpectedIn.close();

        assertEquals(p1ExpectedOut, p1ActualOut.toString());
        assertEquals(p2ExpectedOut, p2ActualOut.toString());
        assertEquals(p3ExpectedOut, p3ActualOut.toString());
    }


    private Message getNextMessageFrom(FileConnection c) {
        return JsonConvert.getInstance().fromJson(c.readLine(), Message.class);
    }

}