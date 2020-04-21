package it.polimi.ingsw.psp44.client.cli;

import java.time.chrono.ThaiBuddhistEra;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import it.polimi.ingsw.psp44.client.cli.Graphics.Color;
import it.polimi.ingsw.psp44.network.modelview.Cell;
import it.polimi.ingsw.psp44.util.Position;

public class Board {

    //Matrix
    private Cell[][] cells;
    private static final int DIMENTION = 5;

    private Map<String, Graphics.Color> playerColors;
    private Map<Integer, Graphics.Color> levelColors;

    public Board(String myPlayer, List<String> opponents){
        this.cells = new Cell[DIMENTION][DIMENTION];
        this.playerColors = new HashMap<>();
        this.levelColors = new HashMap<>();

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


    private void initLevelColors() {
        this.levelColors.put(0, Color.GROUND_LEVEL);
        this.levelColors.put(1, Color.FIRST_LEVEL);
        this.levelColors.put(2, Color.SECOND_LEVEL);
        this.levelColors.put(3, Color.THIRD_LEVEL);
    }

    public String highlight() {
        return "";
    }


    public String update(List<Cell> cellsToUpdate) {
        Position position;
        for(Cell cell : cellsToUpdate){
            position = cell.getPosition();
            cells[position.getRow()][position.getColumn()] = cell;
        }
        return this.getBoard();
    }

    public String getBoard() {
        //StringBuffer sb = new StringBuffer();
        String sb = "";
        Cell currentCell;
        int currentLevel;
        for(int row = 0; row<DIMENTION; row++){
            for(int column = 0; column < DIMENTION; column++){
                currentCell = cells[row][column];
                currentLevel = currentCell.getLevel();

                //sb.append(levelColors.get(currentLevel));
                sb += (levelColors.get(currentLevel));


                if(currentCell.isEmpty()){
                    if(currentCell.isDome()){
                        //sb.append(Graphics.Color.DOME);
                        sb += (Graphics.Color.DOME);
                        //sb.append(Graphics.Element.DOME);
                        sb += (Graphics.Element.DOME);
                    } else 
                        //sb.append(Graphics.Element.EMPTY);
                        sb += (Graphics.Element.EMPTY);
                } else {
                    sb+=(playerColors.get(currentCell.getPlayerNickname()));
                    //sb.append(playerColors.get(currentCell.getPlayerNickname()));
                    sb+=(Graphics.Element.MALE_WORKER);
                    //sb.append(Graphics.Element.MALE_WORKER);
                }

            }
            sb+=(Graphics.Behaviour.NEW_LINE);
            //sb.append(Graphics.Behaviour.NEW_LINE);
        }

        //sb.append(Color.RESET);
        sb+=(Color.RESET);

        //return sb.toString();
        return sb;
    }


    private void initBoard() {
        for(int row = 0; row<DIMENTION; row++){
            for(int column = 0; column < DIMENTION; column++){
                cells[row][column] = new Cell();
            }
        }
    }

}