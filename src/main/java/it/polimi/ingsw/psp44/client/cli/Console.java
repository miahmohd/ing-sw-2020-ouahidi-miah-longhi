package it.polimi.ingsw.psp44.client.cli;

import it.polimi.ingsw.psp44.util.Position;

import java.util.Scanner;

/**
 * Based on http://www.termsys.demon.co.uk/vtansi.htm
 */
public class Console {
    private static final char ESC = 0x1B;
    private static final Position BOARD_SECTION_INITIAL_POSITION = new Position(3,50);
    private static final Position PLAYERS_SECTION_INITIAL_POSITION = new Position(9, 50);
    private static final Position TURN_SECTION_INITIAL_POSITION = new Position(12, 50);
    private static final Position INTERACTION_SECTION_INITIAL_POSITION = new Position(1,0);

    private final Scanner input;
    private int currentInteractionRowOffset;
    private int currentInteractionColumnOffset;

    public Console() {
        this(0, 0, new Scanner(System.in));
    }

    public Console(int currentInteractionRowOffset, int currentInteractionColumnOffset, Scanner input) {
        this.currentInteractionRowOffset = currentInteractionRowOffset;
        this.currentInteractionColumnOffset = currentInteractionColumnOffset;
        this.input = input;

        this.clear();
    }

    public void clear() {
        System.out.print(Graphics.Behaviour.CLEAR.toString());
        System.out.flush();
        currentInteractionRowOffset = currentInteractionColumnOffset = 0;
    }

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

    public String readLine() {
        String message = input.nextLine();
        return message;
    }

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

    public void printOnBoardSection(String board){
        printOnSection(BOARD_SECTION_INITIAL_POSITION, board);
    }

    public void printOnPlayersSection(String players){
        printOnSection(PLAYERS_SECTION_INITIAL_POSITION, players);
    }

    public void printOnTurnSection(String turn) {
        printOnSection(TURN_SECTION_INITIAL_POSITION, turn);
    }

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
    }
}
