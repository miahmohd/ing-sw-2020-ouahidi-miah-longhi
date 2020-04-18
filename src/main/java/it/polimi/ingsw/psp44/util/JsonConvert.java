package it.polimi.ingsw.psp44.util;

import com.google.gson.Gson;

import java.util.List;

public final class JsonConvert {

    private static Gson gson = new Gson();

    private JsonConvert() {
    }

    public static <T> T fromJson(String json, Class<T> classOfT) {
        return gson.fromJson(json, classOfT);
    }

    public static <T> String toJson(T obj, Class<T> classOfT) {
        return gson.toJson(obj, classOfT);
    }

}
