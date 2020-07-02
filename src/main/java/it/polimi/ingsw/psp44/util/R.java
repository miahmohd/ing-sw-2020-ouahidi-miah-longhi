package it.polimi.ingsw.psp44.util;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import it.polimi.ingsw.psp44.util.property.AppProperties;
import it.polimi.ingsw.psp44.util.property.AssetPathProperties;
import it.polimi.ingsw.psp44.util.property.Property;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

/**
 * Class used for managing resources. R stands for Resources
 */
public final class R {

    private R() {
    }

    /**
     * @param index the index of the god
     * @return a json object with the specified card controller's elements
     */
    public static JsonObject getCard(int index) {
        InputStream resourceAsStream = R.class.getResourceAsStream("/gods/" + index + ".json");
        Reader reader = new InputStreamReader(resourceAsStream);
        return JsonParser.parseReader(reader).getAsJsonObject();
    }

    /**
     * @return a json object with the default card controller's elements
     */
    public static JsonObject getCard() {
        return getCard(0);
    }

    /**
     * @return an array containing the Cards in cards.json. Returns an empty array if there are no saved cards.
     */
    public static Card[] getCards() {
        InputStream resourceAsStream = R.class.getResourceAsStream("/cards.json");
        Reader reader = new InputStreamReader(resourceAsStream);
        return JsonConvert.getInstance().fromJson(reader, Card[].class);
    }

    /**
     * @return the instance of the AppProperties containing properties used in the whole applications.
     */
    public static Property getAppProperties() {
        return AppProperties.getInstance();
    }

    /**
     * @return the instance of the AssetPathProperty containing Asset Paths used in Gui application.
     */
    public static Property getAssetPathProperties() {
        return AssetPathProperties.getInstance();
    }
}
