package it.polimi.ingsw.psp44.util;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import it.polimi.ingsw.psp44.util.property.AppProperties;
import it.polimi.ingsw.psp44.util.property.AssetPathProperties;
import it.polimi.ingsw.psp44.util.property.CLIProperties;
import it.polimi.ingsw.psp44.util.property.Property;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

/**
 * Class used for managing resources. R stands for Resources
 */
public final class R {
    /**
     * @param index the index of the god
     * @return a json object with the specified cardSontroller's elements
     */
    public static JsonObject getCard(int index) {
        File file = new File(R.class.getResource("/gods/" + index + ".json").getFile());
        try {
            FileReader reader = new FileReader(file);
            JsonObject jsonObject = JsonParser.parseReader(reader).getAsJsonObject();
            return jsonObject;
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
     * @return a json object with the default cardSontroller's elements
     */
    public static JsonObject getCard() {
        return getCard(0);
    }

    /**
     * @return an array containing the Cards in cards.json. Returns an empty array if there are no saved cards.
     */
    public static Card[] getCards() {
        File file = new File(R.class.getResource("/cards.json").getFile());
        try {
            Reader reader = new FileReader(file);
            Card[] cards = JsonConvert.getInstance().fromJson(reader, Card[].class);
            return cards;
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return new Card[0];
    }

    /**
     * @return the instance of the AppProperties containing properties used in the whole applications.
     */
    public static Property getAppProperties() {
        return AppProperties.getInstance();
    }

    /**
     * @return the instance of the CLIProperties containing properties used in the CLI client.
     */
    public static Property getCLIProperties() {
        return CLIProperties.getInstance();
    }


    public static Property getAssetPathProperties() {
        return AssetPathProperties.getInstance();
    }
}
