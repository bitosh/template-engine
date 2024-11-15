package suhyeon;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.HashMap;
import java.util.Map;

public class DataContext {
    private final Map<String, JsonNode> variables = new HashMap<>();

    public DataContext(final String key, final JsonNode data) {
        this.add(key, data);
    }

    public void add(final String key, final JsonNode data) {
        this.variables.put(key, data);
    }

    public JsonNode get(final String key) {
        return this.variables.get(key);
    }

    public void remove(final String key) {
        this.variables.remove(key);
    }
}
