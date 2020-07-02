package it.polimi.ingsw.psp44.server.controller;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import it.polimi.ingsw.psp44.server.controller.VictoryCondition.VictoryCondition;
import it.polimi.ingsw.psp44.server.controller.filters.DynamicFilter;
import it.polimi.ingsw.psp44.server.controller.filters.Filter;
import it.polimi.ingsw.psp44.server.controller.filters.FilterCollection;
import it.polimi.ingsw.psp44.server.controller.states.CompoundState;
import it.polimi.ingsw.psp44.server.controller.states.State;
import it.polimi.ingsw.psp44.util.Card;
import it.polimi.ingsw.psp44.util.R;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CardFactory {


    private CardFactory() {
    }

    /**
     * Factory method for card controller object
     *
     * @param chosen the selected god
     * @return the card controller of the selected god
     */
    public static CardController getController(Card chosen) {
        return buildCardController(chosen, R.getCard(chosen.getId()));
    }

    /**
     * Factory method for basic card controller, used for a match without gods power
     *
     * @return the default card controller
     */
    public static CardController getDefaultController() {
        return buildCardController(new Card(0, "", "", ""), R.getCard());
    }

    /**
     * Built the card controller from the json object red in the file
     *
     * @param jsonCard informations to build the card controller
     * @return Card controller of the selected god
     */
    private static CardController buildCardController(Card card, JsonObject jsonCard) {
        List<Transition> transitionList = new ArrayList<>();
        List<VictoryCondition> victoryConditionList = new ArrayList<>();
        FilterCollection buildFilter = new FilterCollection();
        FilterCollection moveFilter = new FilterCollection();
        //Transitions
        jsonCard.getAsJsonArray("transitions").iterator().forEachRemaining(t -> {
            JsonObject transition = t.getAsJsonObject();
            State initialState = null;
            State finalState = null;
            List<DynamicFilter> dynamicBuildFilters = null;
            List<DynamicFilter> dynamicMoveFilter = null;
            Transition.Condition condition = null;
            try {
                initialState = getState(transition.getAsJsonArray("initial-state"));
                finalState = getState(transition.getAsJsonArray("final-state"));
                if (transition.getAsJsonPrimitive("final") != null)
                    finalState.setFinalState(transition.getAsJsonPrimitive("final").getAsBoolean());
                if (transition.getAsJsonPrimitive("initial") != null)
                    finalState.setInitialState(transition.getAsJsonPrimitive("initial").getAsBoolean());
                condition = getCondition(transition.getAsJsonPrimitive("condition"));
                dynamicBuildFilters = getFilterAsList(transition.getAsJsonArray("build-filters"));
                dynamicMoveFilter = getFilterAsList(transition.getAsJsonArray("move-filters"));
            } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
                e.printStackTrace();
            }

            transitionList.add(new Transition(initialState, finalState, condition, dynamicBuildFilters, dynamicMoveFilter));

        });

        //Victory conditions
        jsonCard.getAsJsonArray("win-conditions").iterator().forEachRemaining(wc -> {
            try {
                victoryConditionList.add((VictoryCondition) Class.forName(wc.getAsString()).getConstructor().newInstance());
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        });

        //Build filters
        jsonCard.getAsJsonArray("build-filters").iterator().forEachRemaining(bf -> {
            try {
                buildFilter.add((Filter) Class.forName(bf.getAsString()).getConstructor().newInstance());
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        });

        //Move filters
        jsonCard.getAsJsonArray("move-filters").iterator().forEachRemaining(bf -> {
            try {
                moveFilter.add((Filter) Class.forName(bf.getAsString()).getConstructor().newInstance());
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        });

        return new CardController(transitionList, victoryConditionList, buildFilter, moveFilter, card);

    }

    private static State getState(JsonArray states) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        if (states.size() == 1) {
            return (State) Class.forName(states.get(0).getAsString()).getConstructor().newInstance();
        }
        Iterator<JsonElement> stateIterator = states.iterator();
        if (states.size() > 1) {
            CompoundState compoundState = new CompoundState();
            while (stateIterator.hasNext())
                compoundState.addState((State) Class.forName(stateIterator.next().getAsString()).getConstructor().newInstance());
            return compoundState;
        }
        return null;
    }

    private static Transition.Condition getCondition(JsonPrimitive condition) {
        switch (condition.getAsInt()) {
            case -1:
                return null;
            case 0:
                return Transition.Condition.MOVE;
            case 1:
                return Transition.Condition.BUILD;
            case 2:
                return Transition.Condition.FORCE_OPONENTS_WORKER;
        }
        return null;
    }

    private static List<DynamicFilter> getFilterAsList(JsonArray filters) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        List<DynamicFilter> filterList = new ArrayList<>();
        Iterator<JsonElement> filtersIterator = filters.iterator();
        while (filtersIterator.hasNext())
            filterList.add((DynamicFilter) Class.forName(filtersIterator.next().getAsString()).getConstructor().newInstance());

        return filterList;

    }
}
