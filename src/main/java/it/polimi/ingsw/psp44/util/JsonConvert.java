package it.polimi.ingsw.psp44.util;

import com.google.gson.Gson;

import java.io.Reader;

public final class JsonConvert {

    private static JsonConvert instance;
    private final Gson gson;

    private JsonConvert() {
        this.gson = new Gson();
    }

    public static JsonConvert getInstance() {
        if (instance == null) {
            instance = new JsonConvert();
        }
        return instance;
    }

    public synchronized <T> T fromJson(String json, Class<T> classOfT) {
        return gson.fromJson(json, classOfT);
    }

    public synchronized <T> T fromJson(Reader reader, Class<T> classOfT) {
        return gson.fromJson(reader, classOfT);
    }

    public synchronized <T> String toJson(T obj, Class<T> classOfT) {
        return gson.toJson(obj, classOfT);
    }

}
