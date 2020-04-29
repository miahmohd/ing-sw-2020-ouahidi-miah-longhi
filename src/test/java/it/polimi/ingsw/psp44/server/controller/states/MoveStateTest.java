package it.polimi.ingsw.psp44.server.controller.states;

import it.polimi.ingsw.psp44.server.controller.filters.*;
import it.polimi.ingsw.psp44.server.model.Board;
import it.polimi.ingsw.psp44.server.model.GameModel;
import it.polimi.ingsw.psp44.server.model.Worker;
import it.polimi.ingsw.psp44.server.model.actions.Action;
import it.polimi.ingsw.psp44.util.Position;
import org.junit.Before;

import java.util.ArrayList;
import java.util.List;

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

        activeMoveFilter.add(new FilterDome());
        activeMoveFilter.add(new FilterMyWorkers());
        activeMoveFilter.add(new FilterUpByTwo());

    }
}
