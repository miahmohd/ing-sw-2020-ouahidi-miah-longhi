package it.polimi.ingsw.psp44.server.controller.states;

import it.polimi.ingsw.psp44.server.controller.filters.*;
import it.polimi.ingsw.psp44.server.model.Board;
import it.polimi.ingsw.psp44.server.model.GameModel;
import it.polimi.ingsw.psp44.server.model.Worker;
import it.polimi.ingsw.psp44.server.model.actions.Action;
import it.polimi.ingsw.psp44.server.model.actions.DomeBuild;
import it.polimi.ingsw.psp44.server.model.actions.SimpleBuild;
import it.polimi.ingsw.psp44.util.Position;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class BuildStateTest {
    private State stateUnderTest;
    private Position[] field;
    private List<Action> allowedActionExpected;
    private List<Action> allowedActionActual;
    private FilterCollection activeBuildFilter;
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
        activeBuildFilter=new FilterCollection();
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
        boardTestActual.buildDome(field[8]);
        boardTestActual.buildDome(field[15]);
        boardTestActual.buildDome(field[18]);
        boardTestActual.setWorker(field[0],testWorker0);
        boardTestActual.setWorker(field[12],testWorker2);
        boardTestActual.setWorker(field[24],testWorker4);
        boardTestActual.setWorker(field[6],testWorker5);
        boardTestActual.setWorker(field[19],testWorker6);
        boardTestActual.setWorker(field[16],testWorker7);

        boardTestExpected.buildDome(field[8]);
        boardTestExpected.buildDome(field[15]);
        boardTestExpected.buildDome(field[18]);
        boardTestExpected.setWorker(field[0],testWorker0);
        boardTestExpected.setWorker(field[12],testWorker2);
        boardTestExpected.setWorker(field[24],testWorker4);
        boardTestExpected.setWorker(field[6],testWorker5);
        boardTestExpected.setWorker(field[19],testWorker6);
        boardTestExpected.setWorker(field[16],testWorker7);

        activeBuildFilter.add(new FilterDome());
        activeBuildFilter.add(new FilterMyWorkers());
        activeBuildFilter.add(new FilterOtherWorkers());


    }

    @Test
    public void simpleBuild(){
        stateUnderTest=new SimpleBuildState();
        //0,0
        allowedActionActual=stateUnderTest.getAvailableActions(boardTestActual, field[0],null, activeBuildFilter);
        allowedActionExpected.add(new SimpleBuild(field[1]));
        allowedActionExpected.add(new SimpleBuild(field[5]));
        assertTrue(allowedActionExpected.size()==allowedActionActual.size());
        assertTrue(allowedActionExpected.containsAll(allowedActionActual));
        //2,2
        allowedActionActual.addAll(stateUnderTest.getAvailableActions(boardTestActual, field[12],null, activeBuildFilter));
        allowedActionExpected.add(new DomeBuild(field[7]));
        allowedActionExpected.add(new DomeBuild(field[11]));
        allowedActionExpected.add(new SimpleBuild(field[13]));
        allowedActionExpected.add(new SimpleBuild(field[17]));
        assertTrue(allowedActionExpected.size()==allowedActionActual.size());
        assertTrue(allowedActionExpected.containsAll(allowedActionActual));
        //4,4
        allowedActionActual.addAll(stateUnderTest.getAvailableActions(boardTestActual, field[24],null, activeBuildFilter));
        allowedActionExpected.add(new DomeBuild(field[23]));
        assertTrue(allowedActionExpected.size()==allowedActionActual.size());
        assertTrue(allowedActionExpected.containsAll(allowedActionActual));

        for(Action a:allowedActionExpected)
            gmExpected.applyAction(a);
        for(Action a:allowedActionActual)
            gmActual.applyAction(a);
        for (Position p:field){
            assertEquals(boardTestExpected.getLevel(p),boardTestActual.getLevel(p));
            assertEquals(boardTestExpected.isDome(p),boardTestActual.isDome(p));
        }


    }

    @Test
    public void domeBuild(){
        stateUnderTest=new DomeBuildState();
        //0,0
        allowedActionActual=stateUnderTest.getAvailableActions(boardTestActual, field[0],null, activeBuildFilter);
        allowedActionExpected.add(new SimpleBuild(field[1]));
        allowedActionExpected.add(new DomeBuild(field[1]));
        allowedActionExpected.add(new SimpleBuild(field[5]));
        allowedActionExpected.add(new DomeBuild(field[5]));
        assertTrue(allowedActionExpected.size()==allowedActionActual.size());
        assertTrue(allowedActionExpected.containsAll(allowedActionActual));
        //2,2
        allowedActionActual.addAll(stateUnderTest.getAvailableActions(boardTestActual, field[12],null, activeBuildFilter));
        allowedActionExpected.add(new DomeBuild(field[7]));
        allowedActionExpected.add(new DomeBuild(field[11]));
        allowedActionExpected.add(new SimpleBuild(field[13]));
        allowedActionExpected.add(new DomeBuild(field[13]));
        allowedActionExpected.add(new SimpleBuild(field[17]));
        allowedActionExpected.add(new DomeBuild(field[17]));
        assertTrue(allowedActionExpected.size()==allowedActionActual.size());
        assertTrue(allowedActionExpected.containsAll(allowedActionActual));
        //4,4
        allowedActionActual.addAll(stateUnderTest.getAvailableActions(boardTestActual, field[24],null, activeBuildFilter));
        allowedActionExpected.add(new DomeBuild(field[23]));
        assertTrue(allowedActionExpected.size()==allowedActionActual.size());
        assertTrue(allowedActionExpected.containsAll(allowedActionActual));

        for(Action a:allowedActionExpected) {
            if(!((a.getTargetPosition().equals(field[1])||
                    a.getTargetPosition().equals(field[5])||
                    a.getTargetPosition().equals(field[13])||
                    a.getTargetPosition().equals(field[17])||
                    a.getTargetPosition().equals(field[23]))&&a instanceof SimpleBuild))
            gmExpected.applyAction(a);
        }
        for(Action a:allowedActionActual) {
            if(!((a.getTargetPosition().equals(field[1])||
                    a.getTargetPosition().equals(field[5])||
                    a.getTargetPosition().equals(field[13])||
                    a.getTargetPosition().equals(field[17])||
                    a.getTargetPosition().equals(field[23]))&&a instanceof SimpleBuild))
                gmActual.applyAction(a);
        }
        for (Position p:field){
            assertEquals(boardTestExpected.getLevel(p),boardTestActual.getLevel(p));
            assertEquals(boardTestExpected.isDome(p),boardTestActual.isDome(p));
        }


    }

    @Test
    public void secondBuild(){
        stateUnderTest=new SecondBuildState();
        Filter f1=new FilterLastBuildPosition();
        Filter f2=new FilterOnlyLastPosition();
        Filter f3=new FilterMaxLevel();
        activeBuildFilter.add(f1);
        activeBuildFilter.add(f2);
        activeBuildFilter.add(f3);
        //0,0
        activeBuildFilter.update(f1,new SimpleBuild(field[5]));
        allowedActionActual=stateUnderTest.getAvailableActions(boardTestActual, field[0],null, activeBuildFilter);
        allowedActionExpected.add(new SimpleBuild(field[1]));
        assertTrue(allowedActionExpected.size()==allowedActionActual.size());
        assertTrue(allowedActionExpected.containsAll(allowedActionActual));
        assertFalse(f1.isActive());
        //2,2
        activeBuildFilter.update(f2,new SimpleBuild(field[11]));
        activeBuildFilter.update(f3,new SimpleBuild(field[11]));
        allowedActionActual.addAll(stateUnderTest.getAvailableActions(boardTestActual, field[12],null, activeBuildFilter));
        assertTrue(allowedActionExpected.size()==allowedActionActual.size());
        assertTrue(allowedActionExpected.containsAll(allowedActionActual));
        assertFalse(f2.isActive());
        assertFalse(f3.isActive());

        activeBuildFilter.update(f2,new SimpleBuild(field[13]));
        activeBuildFilter.update(f3,new SimpleBuild(field[13]));
        allowedActionActual.addAll(stateUnderTest.getAvailableActions(boardTestActual, field[12],null,activeBuildFilter));
        allowedActionExpected.add(new SimpleBuild(field[13]));
        assertTrue(allowedActionExpected.size()==allowedActionActual.size());
        assertTrue(allowedActionExpected.containsAll(allowedActionActual));
        assertFalse(f2.isActive());
        assertFalse(f3.isActive());

        //4,4
        activeBuildFilter.update(f2,new SimpleBuild(field[23]));
        allowedActionActual.addAll(stateUnderTest.getAvailableActions(boardTestActual, field[24],null,activeBuildFilter));
        allowedActionExpected.add(new DomeBuild(field[23]));
        assertTrue(allowedActionExpected.size()==allowedActionActual.size());
        assertTrue(allowedActionExpected.containsAll(allowedActionActual));
        assertFalse(f2.isActive());

        for(Action a:allowedActionExpected)
            gmExpected.applyAction(a);
        for(Action a:allowedActionActual)
            gmActual.applyAction(a);
        for (Position p:field){
            assertEquals(boardTestExpected.getLevel(p),boardTestActual.getLevel(p));
            assertEquals(boardTestExpected.isDome(p),boardTestActual.isDome(p));
        }


    }
}
