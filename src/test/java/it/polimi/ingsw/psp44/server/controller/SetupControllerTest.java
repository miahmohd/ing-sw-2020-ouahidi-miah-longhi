package it.polimi.ingsw.psp44.server.controller;

import it.polimi.ingsw.psp44.network.message.Message;
import it.polimi.ingsw.psp44.server.FileConnection;
import it.polimi.ingsw.psp44.server.model.GameModel;
import it.polimi.ingsw.psp44.server.view.VirtualView;
import it.polimi.ingsw.psp44.util.JsonConvert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringWriter;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

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
        assertEquals(1, setupController.getRegisteredPlayers().size());
        setupController.addPlayer("p2", p2);
        assertEquals(2, setupController.getRegisteredPlayers().size());
        setupController.addPlayer("p3", p3);
        assertEquals(3, setupController.getRegisteredPlayers().size());

//        start() must sent messages only to the first player,
//        start() does not need inputs.
//        Send START CHOOSE_CARDS to p1
        setupController.start();

//        Receive unexpected CHOSEN_CARDS from p2
        setupController.chosenCardsMessageHandler(p2, getNextMessageFrom(p2Connection));

//        Receive CHOSEN_CARDS from p1 and send CHOOSE_CARD to p2
        setupController.chosenCardsMessageHandler(p1, getNextMessageFrom(p1Connection));

//        Receive unexpected CHOSEN_CARD from p3
        setupController.chosenCardMessageHandler(p3, getNextMessageFrom(p3Connection));

//        Receive CHOSEN_CARD from p2, and send CHOOSE_CARD to p3
        setupController.chosenCardMessageHandler(p2, getNextMessageFrom(p2Connection));

//        Receive CHOSEN_CARD from p3, and send CHOOSE_WORKERS_INITIAL_POSITION to p1
        setupController.chosenCardMessageHandler(p3, getNextMessageFrom(p3Connection));


//        Expected messages sent to the views
        List<String> p1ExpectedOut = Files.readAllLines(Paths.get(getClass().getResource("/setuptest/threeplayer/p1.out.txt").toURI()));
        List<String> p2ExpectedOut = Files.readAllLines(Paths.get(getClass().getResource("/setuptest/threeplayer/p2.out.txt").toURI()));
        List<String> p3ExpectedOut = Files.readAllLines(Paths.get(getClass().getResource("/setuptest/threeplayer/p3.out.txt").toURI()));

        String br = System.getProperty("line.separator");
        assertEquals(String.join(br, p1ExpectedOut) + br, new String(p1ActualOut.toString().getBytes(), StandardCharsets.UTF_8));
        assertEquals(String.join(br, p2ExpectedOut) + br, new String(p2ActualOut.toString().getBytes(), StandardCharsets.UTF_8));
        assertEquals(String.join(br, p3ExpectedOut) + br, new String(p3ActualOut.toString().getBytes(), StandardCharsets.UTF_8));


        p1ExpectedIn.close();
        p2ExpectedIn.close();
        p3ExpectedIn.close();
    }


    private Message getNextMessageFrom(FileConnection c) throws IOException {
        return JsonConvert.getInstance().fromJson(c.readLine(), Message.class);
    }

}