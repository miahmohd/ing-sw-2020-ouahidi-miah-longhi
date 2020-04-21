package it.polimi.ingsw.psp44.client.cli;

import java.time.chrono.ThaiBuddhistEra;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import it.polimi.ingsw.psp44.client.cli.Graphics.Color;
import it.polimi.ingsw.psp44.network.modelview.Action;
import it.polimi.ingsw.psp44.network.modelview.Cell;
import it.polimi.ingsw.psp44.util.Position;

public class Board {

    //Matrix
    private Cell[][] cells;
    private static final int DIMENTION = 5;

    private Map<String, Graphics.Color> playerColors;
    private Map<Integer, Graphics.Color> levelColors;
    private StringBuffer sb;


    public Board(String myPlayer, List<String> opponents){
        this.cells = new Cell[DIMENTION][DIMENTION];
        this.playerColors = new HashMap<>();
        this.levelColors = new HashMap<>();
        this.sb = new StringBuffer();

        //FIXME: it's badly written
        Color[] opponentColors = {Color.OPPONENT_1, Color.OPPONENT_2};
        int count = 0;
        this.playerColors.put(myPlayer, Color.MY_PLAYER);
        for(String opponent : opponents){
            this.playerColors.put(opponent, opponentColors[count]);
        }


        initBoard();
        initLevelColors();


    }


    public Board(){
        this("ciao", new LinkedList<>());
    }




    /**
     *
     * @param actions
     * @return String formatted according to Graphics specification standard
     */
    public String highlight(List<Action> actions) {

        return "";
    }

    /**
     * Updated cells in the board
     * @param cellsToUpdate cells that need to be updated
     * @return String formatted according to Graphics specification standard
     */
    public String update(List<Cell> cellsToUpdate) {
        Position position;
        for(Cell cell : cellsToUpdate){
            position = cell.getPosition();
            cells[position.getRow()][position.getColumn()] = cell;
        }
        return this.getBoard();
    }

    /**
     * Method that generates the string board representation.
     * @return String formatted according to Graphics specification standard
     */
    public String getBoard() {
        Cell currentCell;
        cleanBoard();
        for(int row = 0; row<DIMENTION; row++){
            for(int column = 0; column < DIMENTION; column++){
                currentCell = cells[row][column];
                sb.append(getStringFromCell(currentCell));
            }
            sb.append(Graphics.Behaviour.NEW_LINE);
        }
        sb.append(Color.RESET);

        return sb.toString();
    }


    private void initLevelColors() {
        this.levelColors.put(0, Color.GROUND_LEVEL);
        this.levelColors.put(1, Color.FIRST_LEVEL);
        this.levelColors.put(2, Color.SECOND_LEVEL);
        this.levelColors.put(3, Color.THIRD_LEVEL);
    }

    private void initBoard() {
        for(int row = 0; row<DIMENTION; row++){
            for(int column = 0; column < DIMENTION; column++){
                cells[row][column] = new Cell();
            }
        }
    }

    private StringBuffer getStringFromCell(Cell cell){
        StringBuffer cellBuffer = new StringBuffer();
        int currentLevel = cell.getLevel();

        cellBuffer.append(levelColors.get(currentLevel));

        if(cell.isEmpty()){
            if(cell.isDome()){
                cellBuffer.append(Graphics.Color.DOME);
                cellBuffer.append(Graphics.Element.DOME);
            } else
                cellBuffer.append(Graphics.Element.EMPTY);
        } else {
            cellBuffer.append(playerColors.get(cell.getPlayerNickname()));
            cellBuffer.append(Graphics.Element.MALE_WORKER);
        }

        return cellBuffer;
    }

    private void cleanBoard(){
        sb.delete(0, sb.length());
    }
}