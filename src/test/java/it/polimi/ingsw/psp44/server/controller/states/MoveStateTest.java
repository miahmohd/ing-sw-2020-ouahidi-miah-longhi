package it.polimi.ingsw.psp44.server.controller.states;

import it.polimi.ingsw.psp44.server.controller.filters.*;
import it.polimi.ingsw.psp44.server.model.Board;
import it.polimi.ingsw.psp44.server.model.GameModel;
import it.polimi.ingsw.psp44.server.model.Worker;
import it.polimi.ingsw.psp44.server.model.actions.*;
import it.polimi.ingsw.psp44.util.Position;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class MoveStateTest {
    private State stateUnderTest;
    private Position[] field;
    private List<Action> allowedActionExpected;
    private List<Action> allowedActionActual;
    private FilterCollection activeMoveFilter;
    private Board boardTestExpected;
    private Board boardTestActual;
    private GameModel gmExpected;
    private GameModel gmActual;

    @Before
    public void setUp() {
        gmActual=new GameModel();
        gmExpected=new GameModel();
        boardTestExpected = gmExpected.getBoard();
        boardTestActual=gmActual.getBoard();

        Worker testWorker0=new Worker("p1", Worker.Sex.FEMALE);
        Worker testWorker2=new Worker("p1", Worker.Sex.FEMALE);
        Worker testWorker4=new Worker("p1", Worker.Sex.FEMALE);
        Worker testWorker5=new Worker("a1", Worker.Sex.FEMALE);
        Worker testWorker6=new Worker("a1", Worker.Sex.FEMALE);
        Worker testWorker7=new Worker("a1", Worker.Sex.FEMALE);

        field=new Position[25];
        activeMoveFilter=new FilterCollection();
        allowedActionExpected =new ArrayList<>();
        allowedActionActual =new ArrayList<>();
        for(int i=0, level=0;i<field.length;++i){
            field[i]=new Position(i/5,i%5);
            for (int z=0;z<level;++z){
                boardTestExpected.buildUp(field[i]);
                boardTestActual.buildUp(field[i]);
            }
            level=(level+1)%4;
        }
        boardTestActual.buildUp(field[0]);
        boardTestActual.buildUp(field[0]);
        boardTestActual.buildUp(field[0]);
        boardTestActual.buildDome(field[8]);
        boardTestActual.buildDome(field[15]);
        boardTestActual.buildDome(field[18]);
        boardTestActual.setWorker(field[0],testWorker0);
        boardTestActual.setWorker(field[12],testWorker2);
        boardTestActual.setWorker(field[24],testWorker4);
        boardTestActual.setWorker(field[6],testWorker5);
        boardTestActual.setWorker(field[19],testWorker6);
        boardTestActual.setWorker(field[16],testWorker7);

        boardTestExpected.buildUp(field[0]);
        boardTestExpected.buildUp(field[0]);
        boardTestExpected.buildUp(field[0]);
        boardTestExpected.buildDome(field[8]);
        boardTestExpected.buildDome(field[15]);
        boardTestExpected.buildDome(field[18]);
        boardTestExpected.setWorker(field[0],testWorker0);
        boardTestExpected.setWorker(field[12],testWorker2);
        boardTestExpected.setWorker(field[24],testWorker4);
        boardTestExpected.setWorker(field[6],testWorker5);
        boardTestExpected.setWorker(field[19],testWorker6);
        boardTestExpected.setWorker(field[16],testWorker7);

        activeMoveFilter.add(new FilterDome());
        activeMoveFilter.add(new FilterMyWorkers());
        activeMoveFilter.add(new FilterUpByTwo());


    }

    @Test
    public void simpleMoveTest(){
        activeMoveFilter.add(new FilterOtherWorkers());
        stateUnderTest=new SimpleMoveState();
        DynamicFilter f1=new FilterLastMovePosition();
        DynamicFilter f2=new FilterMoveUp();
        activeMoveFilter.add(f1);
        activeMoveFilter.add(f2);

        //0,0

        activeMoveFilter.update(f1, new SimpleMovement(field[5],field[0]));
        allowedActionActual=stateUnderTest.getAvailableActions(boardTestActual, field[0],activeMoveFilter, null);
        allowedActionExpected.add(new SimpleMovement(field[0],field[1]));
        assertTrue(allowedActionExpected.size()==allowedActionActual.size());
        assertTrue(allowedActionExpected.containsAll(allowedActionActual));
        assertFalse(f1.isActive());

        allowedActionActual.addAll(stateUnderTest.getAvailableActions(boardTestActual, field[0],activeMoveFilter, null));
        allowedActionExpected.add(new SimpleMovement(field[0],field[1]));
        allowedActionExpected.add(new SimpleMovement(field[0],field[5]));
        assertTrue(allowedActionExpected.size()==allowedActionActual.size());
        assertTrue(allowedActionExpected.containsAll(allowedActionActual));

        allowedActionActual.removeIf(a->a.getTargetPosition().equals(field[1]));
        allowedActionExpected.removeIf(a->a.getTargetPosition().equals(field[1]));

        //2,2
        activeMoveFilter.update(f2, new SimpleMovement(field[5],field[0]));
        allowedActionActual.addAll(stateUnderTest.getAvailableActions(boardTestActual, field[12],activeMoveFilter, null));
        assertTrue(allowedActionExpected.size()==allowedActionActual.size());
        assertTrue(allowedActionExpected.containsAll(allowedActionActual));
        assertFalse(f1.isActive());

        activeMoveFilter.update(f1, new SimpleMovement(field[17],field[12]));
        allowedActionActual.addAll(stateUnderTest.getAvailableActions(boardTestActual, field[12],activeMoveFilter, null));
        allowedActionExpected.add(new SimpleMovement(field[12],field[13]));
        assertTrue(allowedActionExpected.size()==allowedActionActual.size());
        assertTrue(allowedActionExpected.containsAll(allowedActionActual));
        assertFalse(f2.isActive());
        //4,4
        allowedActionActual.addAll(stateUnderTest.getAvailableActions(boardTestActual, field[24],activeMoveFilter, null));
        assertTrue(allowedActionExpected.size()==allowedActionActual.size());
        assertTrue(allowedActionExpected.containsAll(allowedActionActual));

        for(Action a:allowedActionExpected)
            gmExpected.applyAction(a);
        for(Action a:allowedActionActual)
            gmActual.applyAction(a);
        for (Position p:field)
            assertEquals(boardTestActual.isWorker(p),boardTestExpected.isWorker(p));


    }

    @Test
    public void pushMoveTest(){
        stateUnderTest=new PushMoveState();
        DynamicFilter f2=new FilterMoveUp();
        activeMoveFilter.add(f2);

        //0,0

        allowedActionActual=stateUnderTest.getAvailableActions(boardTestActual, field[0],activeMoveFilter, null);
        allowedActionExpected.add(new SimpleMovement(field[0],field[1]));
        allowedActionExpected.add(new SimpleMovement(field[0],field[5]));
        assertTrue(allowedActionExpected.size()==allowedActionActual.size());
        assertTrue(allowedActionExpected.containsAll(allowedActionActual));
        allowedActionActual.removeIf(a->a.getTargetPosition().equals(field[1]));
        allowedActionExpected.removeIf(a->a.getTargetPosition().equals(field[1]));
        //2,2
        activeMoveFilter.update(f2, new SimpleBuild(field[5]));
        allowedActionActual.addAll(stateUnderTest.getAvailableActions(boardTestActual, field[12],activeMoveFilter, null));
        allowedActionExpected.add(new PushForwardMovement(field[12],field[16],field[20]));
        assertTrue(allowedActionExpected.size()==allowedActionActual.size());
        assertTrue(allowedActionExpected.containsAll(allowedActionActual));
        //4,4
        allowedActionActual.addAll(stateUnderTest.getAvailableActions(boardTestActual, field[24],activeMoveFilter, null));
        assertTrue(allowedActionExpected.size()==allowedActionActual.size());
        assertTrue(allowedActionExpected.containsAll(allowedActionActual));


        for(Action a:allowedActionExpected)
            gmExpected.applyAction(a);
        for(Action a:allowedActionActual)
            gmActual.applyAction(a);
        for (Position p:field){
            assertEquals(boardTestActual.isWorker(p),boardTestExpected.isWorker(p));
        }


    }

    @Test
    public void swapMoveState(){
        stateUnderTest=new SwapMoveState();
        //0,0

        allowedActionActual=stateUnderTest.getAvailableActions(boardTestActual, field[0],activeMoveFilter, null);
        allowedActionExpected.add(new SimpleMovement(field[0],field[1]));
        allowedActionExpected.add(new SwapMovement(field[0],field[6]));
        allowedActionExpected.add(new SimpleMovement(field[0],field[5]));
        assertTrue(allowedActionExpected.size()==allowedActionActual.size());
        assertTrue(allowedActionExpected.containsAll(allowedActionActual));
        allowedActionActual.removeIf(a->a.getTargetPosition().equals(field[1]));
        allowedActionExpected.removeIf(a->a.getTargetPosition().equals(field[1]));
        allowedActionActual.removeIf(a->a.getTargetPosition().equals(field[5]));
        allowedActionExpected.removeIf(a->a.getTargetPosition().equals(field[5]));
        //2,2
        allowedActionActual.addAll(stateUnderTest.getAvailableActions(boardTestActual, field[12],activeMoveFilter, null));
        allowedActionExpected.add(new SimpleMovement(field[12],field[13]));
        allowedActionExpected.add(new SwapMovement(field[12],field[16]));
        allowedActionExpected.add(new SimpleMovement(field[12],field[17]));
        assertTrue(allowedActionExpected.size()==allowedActionActual.size());
        assertTrue(allowedActionExpected.containsAll(allowedActionActual));
        allowedActionActual.removeIf(a->a.getTargetPosition().equals(field[13]));
        allowedActionExpected.removeIf(a->a.getTargetPosition().equals(field[13]));
        allowedActionActual.removeIf(a->a.getTargetPosition().equals(field[17]));
        allowedActionExpected.removeIf(a->a.getTargetPosition().equals(field[17]));
        //4,4
        allowedActionActual.addAll(stateUnderTest.getAvailableActions(boardTestActual, field[24],activeMoveFilter, null));
        assertTrue(allowedActionExpected.size()==allowedActionActual.size());
        assertTrue(allowedActionExpected.containsAll(allowedActionActual));

        for (Position p:field){
            assertEquals(boardTestActual.isWorker(p),boardTestExpected.isWorker(p));
        }
        for(Action a:allowedActionExpected)
            gmExpected.applyAction(a);
        for(Action a:allowedActionActual)
            gmActual.applyAction(a);
        for (Position p:field){
            assertEquals(boardTestActual.isWorker(p),boardTestExpected.isWorker(p));
        }


    }
}
