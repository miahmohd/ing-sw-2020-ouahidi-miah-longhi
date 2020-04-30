package it.polimi.ingsw.psp44.util.property;

import java.nio.file.Path;
import java.nio.file.Paths;

public class AssetPathProperties extends Property {
    private static AssetPathProperties instance;
    private static final String propertyPath = "/asset.properties";

    //TODO: set assetDirectory
    private final String assetDirectory = "";

    private AssetPathProperties(String path) {
        super(path);
    }

    public static AssetPathProperties getInstance() {
        if (instance == null) {
            instance = new AssetPathProperties(propertyPath);
        }
        return instance;
    }

    /**
     * @return absolute path to asset file
     */
    @Override
    public String get(String code) {
        String assetName = super.get(code);
        Path path = Paths.get(assetDirectory, assetName);
        return path.toString();
    }


}