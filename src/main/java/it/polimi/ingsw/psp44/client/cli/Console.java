package it.polimi.ingsw.psp44.client.cli;

import it.polimi.ingsw.psp44.util.Position;

import java.util.Scanner;

/**
 * Based on http://www.termsys.demon.co.uk/vtansi.htm
 */
public class Console {
    private static char ESC = 0x1B;
    private static Position BOARD_SECTION_INITIAL_POSITION = new Position(1, 1);
    private static Position PLAYERS_SECTION_INITIAL_POSITION = new Position(2, 15);
    private static Position TURN_SECTION_INITIAL_POSITION = new Position(4, 15);
    private static Position INTERACTION_SECTION_INITIAL_POSITION = new Position(15, 0);

    private int currentInteractionRowOffset;
    private int currentInteractionColumnOffset;
    private Scanner input;


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


    public void writeLine(Object obj) {
        String message = goToInteractionSection(currentInteractionRowOffset, currentInteractionColumnOffset) + obj.toString();

        while (message.contains(Graphics.Behaviour.NEW_LINE.getEscape())) {
            currentInteractionRowOffset++;
            message = message.replaceFirst(Graphics.Behaviour.NEW_LINE.toString(), goToInteractionSection(
                    currentInteractionRowOffset, currentInteractionColumnOffset));
        }
        System.out.print(message);
    }

    public String readLine() {
        String message = input.nextLine();
        currentInteractionRowOffset++;
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
        } while (!isNumber);

        return number;
    }

    public Position readPosition() {
        int row, column;
        String position;
        boolean isPosition;
        String[] rowAndColumn;

        row = column = 0;
        do {
            this.writeLine("{row},{column}");
            try {
                position = this.readLine();
                rowAndColumn = position.split(",");
                row = Integer.parseInt(rowAndColumn[0]);
                column = Integer.parseInt(rowAndColumn[1]);
                isPosition = true;
            } catch (NumberFormatException | NullPointerException e) {
                isPosition = false;
            }

            //TODO: maybe check bounds
        } while (!isPosition);

        return new Position(row, column);
    }


    public void printOnBoardSection(String board) {
        int rowOffset, columnOffset;
        rowOffset = columnOffset = 0;

        board = goToBoardSection(0, 0) + board;
        while (board.contains(Graphics.Behaviour.NEW_LINE.getEscape())) {
            rowOffset++;
            board = board.replaceFirst(Graphics.Behaviour.NEW_LINE.toString(), goToBoardSection(rowOffset, columnOffset));
        }

        System.out.println(board);
    }

    public void printOnPlayersSection(String players) {
        int rowOffset, columnOffset;
        rowOffset = columnOffset = 0;

        players = goToPlayersSection(0, 0) + players;
        while (players.contains(Graphics.Behaviour.NEW_LINE.getEscape())) {
            rowOffset++;
            players = players.replaceFirst(Graphics.Behaviour.NEW_LINE.getEscape(), goToPlayersSection(rowOffset, columnOffset));
        }

        System.out.println(players);
    }

    public void printOnTurnSection(String turn) {
        int rowOffset, columnOffset;
        rowOffset = columnOffset = 0;

        turn = goToTurnSection(0, 0) + turn;
        while (turn.contains(Graphics.Behaviour.NEW_LINE.getEscape())) {
            rowOffset++;
            turn = turn.replaceFirst(Graphics.Behaviour.NEW_LINE.getEscape(), goToPlayersSection(rowOffset, columnOffset));
        }

        System.out.println(turn);
    }

    private String goToTurnSection(int rowOffset, int columnOffset) {
        return (String.format("%c[%d;%df", ESC,
                TURN_SECTION_INITIAL_POSITION.getRow() + rowOffset,
                TURN_SECTION_INITIAL_POSITION.getColumn() + columnOffset));
    }

    private String goToBoardSection(int rowOffset, int columnOffset) {
        return (String.format("%c[%d;%df", ESC,
                BOARD_SECTION_INITIAL_POSITION.getRow() + rowOffset,
                BOARD_SECTION_INITIAL_POSITION.getColumn() + columnOffset));

    }

    private String goToPlayersSection(int rowOffset, int columnOffset) {
        return (String.format("%c[%d;%df", ESC,
                PLAYERS_SECTION_INITIAL_POSITION.getRow() + rowOffset,
                PLAYERS_SECTION_INITIAL_POSITION.getColumn() + columnOffset));
    }

    private String goToInteractionSection(int rowOffset, int columnOffset) {
        return (String.format("%c[%d;%df", ESC,
                INTERACTION_SECTION_INITIAL_POSITION.getRow() + rowOffset,
                INTERACTION_SECTION_INITIAL_POSITION.getColumn() + columnOffset));
    }
}
