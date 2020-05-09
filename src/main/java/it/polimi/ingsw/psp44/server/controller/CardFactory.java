package it.polimi.ingsw.psp44.server.controller;

import it.polimi.ingsw.psp44.server.controller.VictoryCondition.BaseVictoryCondition;
import it.polimi.ingsw.psp44.server.controller.VictoryCondition.VictoryCondition;
import it.polimi.ingsw.psp44.server.controller.filters.*;
import it.polimi.ingsw.psp44.server.controller.states.SimpleBuildState;
import it.polimi.ingsw.psp44.server.controller.states.SimpleMoveState;
import it.polimi.ingsw.psp44.server.controller.states.State;
import it.polimi.ingsw.psp44.util.Card;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CardFactory {
    public static CardController getController(Card chosen) {
        return getDefaultController();
    }


    public static CardController getDefaultController(){
        List<Transition> transitionList;
        List<VictoryCondition> victoryConditions;
        FilterCollection moveFilter;
        FilterCollection buildFilter;

        State simpleMove;
        State simpleBuild;

        simpleMove = new SimpleMoveState();
        simpleMove.setInitialState(true);
        simpleBuild = new SimpleBuildState();
        simpleBuild.setFinalState(true);
        transitionList = new ArrayList<>();

        transitionList.add(new Transition(simpleMove, simpleBuild, null, null, null));
        transitionList.add(new Transition(simpleBuild, simpleMove, null, null, null));

        victoryConditions = new ArrayList<>(Arrays.asList(new BaseVictoryCondition()));

        moveFilter = new FilterCollection();
        buildFilter = new FilterCollection();

        moveFilter.add(new FilterMyWorkers());
        moveFilter.add(new FilterDome());
        moveFilter.add(new FilterOtherWorkers());
        moveFilter.add(new FilterUpByTwo());

        buildFilter.add(new FilterMyWorkers());
        buildFilter.add(new FilterOtherWorkers());
        buildFilter.add(new FilterDome());

        CardController defaultCardController = new CardController(transitionList, victoryConditions, null, moveFilter, buildFilter);

        return defaultCardController;
    }
}
