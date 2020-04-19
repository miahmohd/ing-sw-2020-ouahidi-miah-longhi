package it.polimi.ingsw.psp44.server.model;

import it.polimi.ingsw.psp44.util.JsonConvert;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

public class Card {
    private int id;
    private String title;
    private String subtitle;
    private String description;

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public String getDescription() {
        return description;
    }
}
