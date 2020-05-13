package it.polimi.ingsw.psp44.util;

import java.util.Objects;

/**
 * This class shapes an abstract position inside a table
 */
public class Position {
    private int row;
    private int column;

    /**
     * Needed for Gson serialization-deserialization
     * see https://github.com/google/gson/blob/master/UserGuide.md#writing-an-instance-creator
     */
    private Position() {

    }

    public Position(int row, int column) {
        this.row = row;
        this.column = column;
    }

    public int getRow() {
        return row;
    }


    public int getColumn() {
        return column;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Position position = (Position) o;
        return row == position.row &&
                column == position.column;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, column);
    }
}
