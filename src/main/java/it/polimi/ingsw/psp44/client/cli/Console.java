package it.polimi.ingsw.psp44.client.cli;

import it.polimi.ingsw.psp44.util.Position;

import java.util.Scanner;

/**
 * Class dedicated to interaction with console
 * Based on http://www.termsys.demon.co.uk/vtansi.htm
 */
public class Console {
    private static final char ESC = 0x1B;
    private static final Position BOARD_SECTION_INITIAL_POSITION = new Position(3,50);
    private static final Position LEGEND_SECTION_INITIAL_POSITION = new Position(3,65);
    private static final Position PLAYERS_SECTION_INITIAL_POSITION = new Position(9, 50);
    private static final Position TURN_SECTION_INITIAL_POSITION = new Position(12, 50);
    private static final Position INTERACTION_SECTION_INITIAL_POSITION = new Position(1,0);

    private final Scanner input;
    private int currentInteractionRowOffset;
    private int currentInteractionColumnOffset;

    public Console() {
        this.currentInteractionRowOffset = 0;
        this.currentInteractionColumnOffset = 0;
        this.input = new Scanner(System.in);

        this.clear();
    }

    /**
     * Clears everything on the screen
     */
    public void clear() {
        System.out.print(Graphics.Behaviour.CLEAR.toString());
        System.out.flush();
        currentInteractionRowOffset = currentInteractionColumnOffset = 0;
    }

    /**
     * Writes the param on interaction section
     * @param obj the printed object
     */
    public void writeLine(Object obj){
        String message = goToSection(INTERACTION_SECTION_INITIAL_POSITION ,
                currentInteractionRowOffset, currentInteractionColumnOffset) + obj.toString();

        while (message.contains(Graphics.Behaviour.NEW_LINE.getEscape())) {
            currentInteractionRowOffset++;
            message = message.replaceFirst(Graphics.Behaviour.NEW_LINE.toString(), goToSection(
                    INTERACTION_SECTION_INITIAL_POSITION,
                    currentInteractionRowOffset, currentInteractionColumnOffset));
        }
        currentInteractionRowOffset++;
        System.out.print(message);
    }

    /**
     * reads string until the line
     * @return the read string from a scanner
     */
    public String readLine() {
        return input.nextLine();
    }

    /**
     * Reads a number
     * @return the read number
     */
    public int readNumber() {
        int number;
        boolean isNumber;
        number = 0;
        do {
            try {
                number = Integer.parseInt(this.readLine());
                isNumber = true;
            } catch (NumberFormatException e) {
                this.writeLine("Not a Number");
                isNumber = false;
            }
        } while(!isNumber);

        return number;
    }

    /**
     * Reads a position
     * @return the read position
     */
    public Position readPosition() {
        int row, column;
        String position;
        boolean isPosition;
        String[] rowAndColumn;

        row = column = 0;
        do {
            this.writeLine("{row},{column} ");
            try{
                position = this.readLine();
                rowAndColumn = position.split(",");
                row = Integer.parseInt(rowAndColumn[0]);
                column = Integer.parseInt(rowAndColumn[1]);
                isPosition = true;
            } catch (NumberFormatException|NullPointerException|ArrayIndexOutOfBoundsException e){
                this.writeLine("Not a valid Position ");
                isPosition = false;
            }
        } while (!isPosition);

        return new Position(row, column);
    }

    /**
     * Prints on board section
     * @param board the string to print
     */
    public void printOnBoardSection(String board){
        printOnSection(BOARD_SECTION_INITIAL_POSITION, board);
    }

    /**
     * Prints on players section
     * @param players the string to print
     */
    public void printOnPlayersSection(String players){
        printOnSection(PLAYERS_SECTION_INITIAL_POSITION, players);
    }

    /**
     * Prints on turn section
     * @param turn the string to print
     */
    public void printOnTurnSection(String turn) {
        printOnSection(TURN_SECTION_INITIAL_POSITION, turn);
    }

    /**
     * Prints on legend section
     * @param legend the string to print
     */
    public void printOnLegendSection(String legend) { printOnSection(LEGEND_SECTION_INITIAL_POSITION, legend); }

    private String goToSection(Position sectionPosition, int rowOffset, int columnOffset){
        return (String.format("%c[%d;%df", ESC,
                sectionPosition.getRow() + rowOffset,
                sectionPosition.getColumn() + columnOffset));
    }

    private void printOnSection(Position initialSectionPosition, String message){
        int rowOffset, columnOffset;
        rowOffset = columnOffset = 0;

        message = goToSection(initialSectionPosition, rowOffset,columnOffset) + message;
        while(message.contains(Graphics.Behaviour.NEW_LINE.getEscape())){
            rowOffset++;
            message = message.replaceFirst(Graphics.Behaviour.NEW_LINE.getEscape(),
                    goToSection(initialSectionPosition,rowOffset, columnOffset));
        }

        System.out.println(message);
        resetPointer();
    }

    private void resetPointer() {
        String message = goToSection(INTERACTION_SECTION_INITIAL_POSITION, currentInteractionRowOffset, currentInteractionColumnOffset) + "";
        System.out.print(message);
    }

}
