package ir.moke.aparat;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public interface JsonUtils {
    Gson gson = new Gson();

    static JsonObject fromJson(String json) {
        return gson.fromJson(json,JsonObject.class);
    }
}
